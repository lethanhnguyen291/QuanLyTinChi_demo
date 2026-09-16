package ui;

import config.DBConnect;
import service.*;
import utils.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;

/** Login UI: saved SQL connection is checked in the background, without opening dialogs. */
public final class LoginPanel extends JPanel {
    public enum ConnectionState { IDLE, CONNECTING, READY, SETUP_REQUIRED, OFFLINE }
    private static final Color INK=new Color(31,35,46),MUTED=new Color(119,125,139),RED=utils.UIUtils.MIT_RED;
    private final Runnable ready;
    private final JPanel card=new JPanel(new BorderLayout()){
        protected void paintComponent(Graphics graphics){Graphics2D g=(Graphics2D)graphics.create();smooth(g);g.setColor(Color.WHITE);g.fillRoundRect(0,0,getWidth(),getHeight(),28,28);g.dispose();}
        protected void paintChildren(Graphics graphics){Graphics2D g=(Graphics2D)graphics.create();g.clip(new RoundRectangle2D.Double(0,0,getWidth(),getHeight(),28,28));super.paintChildren(g);g.dispose();}
    };
    private final BrandPanel brand=new BrandPanel();
    private final JButton student=button("Sinh viên",false),staff=button("Cán bộ đào tạo",false),submit=button("Đăng nhập",true);
    private final JButton settings=link("Cài đặt"),retry=link("Thử lại"),help=link("Chưa có tài khoản sinh viên?");
    private final JTextField user=new JTextField();
    private final JPasswordField password=new JPasswordField();
    private final JLabel userLabel=label("Mã sinh viên",13,true,INK),connection=label("Đang kết nối…",12,false,MUTED);
    private final JCheckBox showPassword=new JCheckBox("Hiện mật khẩu");
    private final JTextArea message=new JTextArea(2,24);
    private ConnectionState state=ConnectionState.IDLE;
    private boolean adminRole,signingIn;
    private long connectionAttempt;

    public LoginPanel(Runnable ready){
        this.ready=ready;setLayout(null);setBackground(new Color(244,245,249));card.setOpaque(false);add(card);card.add(brand,BorderLayout.WEST);
        JPanel form=new JPanel();form.setLayout(new BoxLayout(form,BoxLayout.Y_AXIS));form.setBackground(Color.WHITE);form.setBorder(BorderFactory.createEmptyBorder(38,42,26,42));
        addRow(form,label("CỔNG THÔNG TIN ĐÀO TẠO",10,true,MUTED));gap(form,10);
        addRow(form,label("Chào mừng trở lại",28,true,INK));gap(form,6);
        addRow(form,label("Đăng nhập để tiếp tục hành trình học tập.",13,false,MUTED));gap(form,24);
        JPanel roles=new JPanel(new GridLayout(1,2,6,0));roles.setOpaque(false);roles.add(student);roles.add(staff);fixed(roles,40);addRow(form,roles);gap(form,22);
        styleField(user);styleField(password);user.setToolTipText("Nhập mã sinh viên hoặc tài khoản cán bộ được cấp.");
        addRow(form,userLabel);gap(form,7);addRow(form,user);gap(form,16);
        addRow(form,label("Mật khẩu",13,true,INK));gap(form,7);addRow(form,password);gap(form,10);
        showPassword.setFont(new Font("Segoe UI",Font.PLAIN,12));showPassword.setForeground(MUTED);showPassword.setOpaque(false);showPassword.setFocusPainted(false);addRow(form,showPassword);gap(form,20);
        fixed(submit,46);addRow(form,submit);gap(form,10);
        message.setFont(new Font("Segoe UI",Font.PLAIN,12));message.setForeground(RED);message.setEditable(false);message.setFocusable(false);message.setOpaque(false);message.setLineWrap(true);message.setWrapStyleWord(true);message.setBorder(BorderFactory.createEmptyBorder());fixed(message,38);addRow(form,message);gap(form,4);
        help.setFont(new Font("Segoe UI",Font.PLAIN,12));addRow(form,help);gap(form,18);form.add(Box.createVerticalGlue());
        JPanel footer=new JPanel(new BorderLayout(8,0));footer.setOpaque(false);JPanel connectionInfo=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));connectionInfo.setOpaque(false);connectionInfo.add(connection);connectionInfo.add(Box.createHorizontalStrut(10));connectionInfo.add(retry);footer.add(connectionInfo,BorderLayout.WEST);footer.add(settings,BorderLayout.EAST);fixed(footer,24);addRow(form,footer);
        JScrollPane scroll=new JScrollPane(form);scroll.setBorder(BorderFactory.createEmptyBorder());scroll.getViewport().setBackground(Color.WHITE);scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);scroll.getVerticalScrollBar().setUnitIncrement(16);card.add(scroll,BorderLayout.CENTER);
        char echo=password.getEchoChar();showPassword.addActionListener(e->password.setEchoChar(showPassword.isSelected()?'\0':echo));
        student.addActionListener(e->selectRole(false));staff.addActionListener(e->selectRole(true));selectRole(false);retry.setVisible(false);submit.setEnabled(false);
        help.addActionListener(e->JOptionPane.showMessageDialog(this,"Tên đăng nhập sinh viên là mã sinh viên.\nNếu chưa được cấp tài khoản: cán bộ đăng nhập → Tài khoản & Ngoại ngữ\n→ Cấp tài khoản còn thiếu (hoặc chọn riêng một sinh viên).\nMật khẩu tạm do cán bộ cấp; bạn phải đổi ở lần đăng nhập đầu tiên.","Tài khoản sinh viên",JOptionPane.INFORMATION_MESSAGE));
        settings.addActionListener(e->showSettings());retry.addActionListener(e->connectAutomatically());submit.addActionListener(e->signIn());
        password.addActionListener(e->submit.doClick());user.addActionListener(e->password.requestFocusInWindow());
    }
    public ConnectionState connectionState(){return state;}
    public void connectAutomatically(){
        long attempt=++connectionAttempt;setState(ConnectionState.CONNECTING);message.setText("");
        new SwingWorker<Boolean,Void>(){
            protected Boolean doInBackground()throws Exception{
                try(var c=DBConnect.getConnection()){
                    if(Db.count(c,"SELECT COUNT(*) FROM sys.tables WHERE schema_id=SCHEMA_ID('dbo') AND name IN('SINH_VIEN','HOC_KY','LOP_HOC_PHAN','MON_HOC','KET_QUA_DANG_KY','CONG_NO_HOC_PHI')")!=6)throw new IllegalStateException("CSDL chưa có đủ dữ liệu. Khôi phục các tệp SQL trong thư mục database.");
                    if(Db.count(c,"SELECT COUNT(*) FROM sys.tables WHERE schema_id=SCHEMA_ID('dbo') AND name='QLTC_TAI_KHOAN'")==0)return false;
                    return Db.count(c,"SELECT COUNT(*) FROM QLTC_TAI_KHOAN WHERE VaiTro='ADMIN'")>0;
                }
            }
            protected void done(){
                if(attempt!=connectionAttempt)return;
                try{boolean initialized=get();setState(initialized?ConnectionState.READY:ConnectionState.SETUP_REQUIRED);if(!initialized)message.setText("Dữ liệu đã kết nối. Vào Cài đặt để tạo quản trị viên đầu tiên.");}
                catch(Exception error){setState(ConnectionState.OFFLINE);Throwable cause=unwrap(error);connection.setToolTipText(cause.getMessage());message.setText("Chưa kết nối được dữ liệu. Chọn Cài đặt để kiểm tra kết nối.");}
            }
        }.execute();
    }
    private void setState(ConnectionState next){
        ConnectionState old=state;state=next;
        connection.setText(switch(next){case READY->"●  Dữ liệu đã kết nối";case SETUP_REQUIRED->"●  Chờ khởi tạo quản trị";case OFFLINE->"●  Chưa kết nối";default->"●  Đang kết nối…";});
        connection.setForeground(next==ConnectionState.READY?new Color(35,125,94):next==ConnectionState.OFFLINE?new Color(162,112,37):MUTED);
        retry.setVisible(next==ConnectionState.OFFLINE);submit.setEnabled(next==ConnectionState.READY&&!signingIn);firePropertyChange("connectionState",old,next);
    }
    private void selectRole(boolean admin){
        adminRole=admin;userLabel.setText(admin?"Tài khoản cán bộ":"Mã sinh viên");help.setVisible(!admin);
        styleRole(student,!admin);styleRole(staff,admin);message.setText("");
    }
    private void signIn(){
        if(state!=ConnectionState.READY||signingIn)return;
        String id=user.getText().trim(),role=adminRole?"ADMIN":"STUDENT";char[] secret=password.getPassword();
        if(id.isEmpty()||secret.length==0){Arrays.fill(secret,'\0');message.setText("Bạn nhập tài khoản và mật khẩu trước nhé.");return;}
        signingIn=true;authenticationEnabled(false);submit.setText("Đang đăng nhập…");message.setText("");
        new SwingWorker<AccountService.Login,Void>(){
            protected AccountService.Login doInBackground()throws Exception{try{return new AccountService().login(id,secret,role);}finally{Arrays.fill(secret,'\0');}}
            protected void done(){
                try{var account=get();Session.start(account.username(),account.role());password.setText("");if(account.mustChange()&&!AccountDialog.change(LoginPanel.this,true)){Session.clear();return;}ready.run();}
                catch(Exception error){message.setText(unwrap(error).getMessage());}
                finally{signingIn=false;submit.setText("Đăng nhập");authenticationEnabled(true);}
            }
        }.execute();
    }
    private void authenticationEnabled(boolean enabled){student.setEnabled(enabled);staff.setEnabled(enabled);user.setEnabled(enabled);password.setEnabled(enabled);showPassword.setEnabled(enabled);settings.setEnabled(enabled);submit.setEnabled(enabled&&state==ConnectionState.READY);}
    private void showSettings(){
        JPopupMenu menu=new JPopupMenu();JMenuItem configure=new JMenuItem("Cấu hình kết nối dữ liệu");menu.add(configure);
        configure.addActionListener(e->{ConnectionDialog.show(this);connectAutomatically();});
        if(state==ConnectionState.SETUP_REQUIRED){JMenuItem setup=new JMenuItem("Tạo quản trị viên đầu tiên");menu.add(setup);setup.addActionListener(e->setup());}
        menu.show(settings,0,settings.getHeight());
    }
    private void setup(){
        JTextField id=new JTextField("ADMIN");JPasswordField first=new JPasswordField(),again=new JPasswordField();JPanel fields=new JPanel(new GridLayout(0,1,0,8));
        fields.add(new JLabel("Tài khoản quản trị đầu tiên"));fields.add(id);fields.add(new JLabel("Mật khẩu (8–128 ký tự)"));fields.add(first);fields.add(new JLabel("Nhập lại mật khẩu"));fields.add(again);
        if(JOptionPane.showConfirmDialog(this,fields,"Tạo quản trị viên",JOptionPane.OK_CANCEL_OPTION)!=JOptionPane.OK_OPTION)return;
        char[] a=first.getPassword(),b=again.getPassword();if(!Arrays.equals(a,b)){Arrays.fill(a,'\0');Arrays.fill(b,'\0');message.setText("Hai mật khẩu chưa khớp.");return;}Arrays.fill(b,'\0');String username=id.getText();
        Ui.async(this,settings,()->{try{new AccountService().bootstrap(username,a);return true;}finally{Arrays.fill(a,'\0');}},ok->{selectRole(true);user.setText(username);connectAutomatically();});
    }
    public void doLayout(){int width=Math.max(720,Math.min(1040,getWidth()-64)),height=Math.max(480,Math.min(652,getHeight()-56));card.setBounds((getWidth()-width)/2,(getHeight()-height)/2,width,height);brand.setPreferredSize(new Dimension((int)(width*.40),height));super.doLayout();}
    protected void paintComponent(Graphics graphics){super.paintComponent(graphics);Graphics2D g=(Graphics2D)graphics.create();smooth(g);Rectangle b=card.getBounds();for(int i=14;i>0;i--){g.setColor(new Color(38,38,55,2));g.fillRoundRect(b.x-i/2,b.y+i/2,b.width+i,b.height+i,30,30);}g.dispose();}
    private static Throwable unwrap(Throwable t){while((t instanceof java.util.concurrent.ExecutionException||t instanceof java.lang.reflect.InvocationTargetException)&&t.getCause()!=null)t=t.getCause();return t;}
    private static void addRow(JPanel panel,JComponent child){child.setAlignmentX(LEFT_ALIGNMENT);panel.add(child);}
    private static void gap(JPanel panel,int h){panel.add(Box.createVerticalStrut(h));}
    private static void fixed(JComponent c,int height){c.setPreferredSize(new Dimension(320,height));c.setMaximumSize(new Dimension(Integer.MAX_VALUE,height));c.setMinimumSize(new Dimension(20,height));}
    private static JLabel label(String text,int size,boolean bold,Color color){JLabel l=new JLabel(text);l.setFont(new Font("Segoe UI",bold?Font.BOLD:Font.PLAIN,size));l.setForeground(color);return l;}
    private static void smooth(Graphics2D g){g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);}
    private static void styleField(JTextField field){field.setFont(new Font("Segoe UI",Font.PLAIN,15));field.setForeground(INK);field.setBackground(new Color(252,252,254));field.setCaretColor(RED);fixed(field,45);field.setBorder(new AbstractBorder(){
        public Insets getBorderInsets(Component c){return new Insets(10,14,10,14);}
        public void paintBorder(Component c,Graphics graphics,int x,int y,int w,int h){Graphics2D g=(Graphics2D)graphics.create();smooth(g);g.setColor(c.hasFocus()?RED:new Color(222,225,233));g.drawRoundRect(x,y,w-1,h-1,10,10);g.dispose();}
    });field.addFocusListener(new FocusAdapter(){public void focusGained(FocusEvent e){field.repaint();}public void focusLost(FocusEvent e){field.repaint();}});}
    private static JButton link(String text){JButton b=new JButton(text);b.setUI(new javax.swing.plaf.basic.BasicButtonUI());b.setContentAreaFilled(false);b.setBorder(BorderFactory.createEmptyBorder(2,0,2,0));b.setFont(new Font("Segoe UI",Font.PLAIN,12));b.setForeground(MUTED);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;}
    private static void styleRole(JButton b,boolean active){b.setBackground(active?utils.UIUtils.MIT_RED_LIGHT:new Color(244,245,248));b.setForeground(active?RED:MUTED);b.putClientProperty("selectedRole",active);b.getAccessibleContext().setAccessibleDescription(active?"Vai trò đang chọn":"Chọn vai trò này");b.repaint();}
    private static JButton button(String text,boolean primary){JButton b=new JButton(text){protected void paintComponent(Graphics graphics){Graphics2D g=(Graphics2D)graphics.create();smooth(g);g.setColor(!isEnabled()?new Color(229,232,239):primary&&getModel().isRollover()?utils.UIUtils.BURGUNDY_DARK:getBackground());g.fillRoundRect(0,0,getWidth(),getHeight(),12,12);if(hasFocus()){g.setColor(RED);g.drawRoundRect(2,2,getWidth()-5,getHeight()-5,10,10);}g.dispose();super.paintComponent(graphics);}};b.setUI(new javax.swing.plaf.basic.BasicButtonUI());b.setContentAreaFilled(false);b.setOpaque(false);b.setBorder(BorderFactory.createEmptyBorder(8,12,8,12));b.setFont(new Font("Segoe UI",Font.BOLD,13));b.setBackground(primary?RED:new Color(244,245,248));b.setForeground(primary?Color.WHITE:INK);b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;}
    private static final class BrandPanel extends JPanel {
        protected void paintComponent(Graphics graphics){
            Graphics2D g=(Graphics2D)graphics.create();smooth(g);int w=getWidth(),h=getHeight();g.setPaint(new GradientPaint(0,0,utils.UIUtils.BURGUNDY_DARK,w,h,utils.UIUtils.MIT_RED));g.fillRect(0,0,w,h);
            g.setColor(new Color(255,255,255,10));g.fillOval(w-190,-100,330,330);g.fillOval(-160,h-240,340,340);g.setStroke(new BasicStroke(1));g.setColor(new Color(255,255,255,22));g.drawOval(w-230,-145,410,410);
            int x=34;g.setColor(new Color(255,255,255,32));g.fillRoundRect(x,36,54,44,12,12);g.setColor(Color.WHITE);g.setFont(new Font("Segoe UI",Font.BOLD,20));g.drawString("MIT",x+9,65);
            g.setFont(new Font("Segoe UI",Font.BOLD,12));g.drawString("STUDENT PORTAL",x+70,55);g.setFont(new Font("Segoe UI",Font.PLAIN,11));g.setColor(new Color(248,215,209));g.drawString("Quản lý đào tạo",x+70,74);
            int y=Math.max(184,h/3);g.setColor(Color.WHITE);g.setFont(new Font("Segoe UI",Font.BOLD,w<380?30:34));g.drawString("Mỗi học kỳ,",x,y);g.drawString("một bước tiến.",x,y+46);
            g.setFont(new Font("Segoe UI",Font.PLAIN,14));g.setColor(new Color(252,230,226));g.drawString("Kế hoạch học tập, lịch học và kết quả",x,y+94);g.drawString("của bạn — trong một không gian.",x,y+116);
            int gridY=y+158;g.setColor(new Color(255,255,255,12));g.fillRoundRect(x,gridY,w-2*x,110,16,16);
            for(int row=0;row<3;row++)for(int col=0;col<5;col++){int cellW=(w-2*x-36)/5;g.setColor(new Color(255,255,255,(row+col)%4==0?100:26));g.fillRoundRect(x+14+col*(cellW+2),gridY+15+row*27,cellW-5,19,5,5);}
            g.setFont(new Font("Segoe UI",Font.PLAIN,11));g.setColor(new Color(245,203,194));g.drawString("ĐĂNG KÝ  /  LỊCH HỌC  /  KẾT QUẢ",x,h-40);g.dispose();
        }
    }
}
