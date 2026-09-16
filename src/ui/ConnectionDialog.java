package ui;
import config.*;
import utils.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public final class ConnectionDialog {
    private ConnectionDialog(){}
    public static boolean show(Component parent){
        ConnectionForm form=new ConnectionForm();
        Window owner=parent instanceof Window w?w:SwingUtilities.getWindowAncestor(parent);
        JDialog dialog=new JDialog(owner,"Kết nối dữ liệu trên máy này",Dialog.ModalityType.APPLICATION_MODAL);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JLabel status=new JLabel("Cấu hình chỉ được lưu sau khi kiểm tra kết nối thành công.");status.setForeground(UIUtils.TEXT_MUTED);
        JButton save=UIUtils.createPrimaryBtn("Kiểm tra & Lưu"),cancel=new JButton("Để sau");
        JPanel buttons=new JPanel(new FlowLayout(FlowLayout.RIGHT));buttons.setOpaque(false);buttons.add(cancel);buttons.add(save);
        JPanel footer=new JPanel(new BorderLayout(0,12));footer.setOpaque(false);footer.add(status,BorderLayout.CENTER);footer.add(buttons,BorderLayout.SOUTH);
        JPanel content=new JPanel(new BorderLayout(0,20));content.setBackground(Color.WHITE);content.setBorder(BorderFactory.createEmptyBorder(24,28,16,28));content.add(form,BorderLayout.CENTER);content.add(footer,BorderLayout.SOUTH);
        dialog.setContentPane(content);dialog.pack();dialog.setMinimumSize(dialog.getSize());dialog.setLocationRelativeTo(parent);
        boolean[] saved={false};cancel.addActionListener(e->dialog.dispose());
        save.addActionListener(e->{
            Properties candidate=form.values();save.setEnabled(false);cancel.setEnabled(false);form.setFieldsEnabled(false);
            dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);status.setText("Đang kiểm tra kết nối và dữ liệu…");
            new SwingWorker<Void,Void>(){
                protected Void doInBackground()throws Exception{DBConnect.testAndSave(candidate);return null;}
                protected void done(){
                    try{get();saved[0]=true;dialog.dispose();JOptionPane.showMessageDialog(parent,"Kết nối thành công và đã lưu cấu hình cho bản ứng dụng này.\nNếu chưa có tài khoản quản trị, chọn Thiết lập lần đầu.");}
                    catch(Exception error){status.setText("Chưa lưu. Kiểm tra thông tin và thử lại.");Ui.error(dialog,error);}
                    finally{candidate.remove("db.password");save.setEnabled(true);cancel.setEnabled(true);form.setFieldsEnabled(true);dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);}
                }
            }.execute();
        });
        dialog.getRootPane().setDefaultButton(save);dialog.setVisible(true);form.password.setText("");return saved[0];
    }
    /** Also used to verify the form layout without opening a desktop window. */
    public static final class ConnectionForm extends JPanel {
        private final Properties original=DBConnect.settings();
        private final JTextField url=new JTextField(original.getProperty("db.url",DBConnect.DEFAULT_URL),56);
        private final JTextField user=new JTextField(original.getProperty("db.user","sa"));
        private final JPasswordField password=new JPasswordField(original.getProperty("db.password",""));
        public ConnectionForm(){
            setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));setBackground(Color.WHITE);
            JLabel title=new JLabel("Thiết lập dữ liệu cho bản ứng dụng này");title.setFont(new Font("Segoe UI",Font.BOLD,22));addRow(title);add(Box.createVerticalStrut(12));
            addRow(new JLabel("<html>Chỉ cần thiết lập một lần sau khi giải nén hoặc tải mã từ GitHub.<br>Nhập thông tin <b>SQL Server</b> trên máy bạn; đây không phải tài khoản sinh viên.</html>"));add(Box.createVerticalStrut(20));
            addField("Địa chỉ kết nối SQL Server",url);addField("Tài khoản SQL Server",user);addField("Mật khẩu SQL Server",password);
            JCheckBox reveal=new JCheckBox("Hiện mật khẩu");reveal.setOpaque(false);char echo=password.getEchoChar();reveal.addActionListener(e->password.setEchoChar(reveal.isSelected()?'\0':echo));addRow(reveal);add(Box.createVerticalStrut(16));
            addRow(new JLabel("<html><b>Máy đang dùng:</b> giữ địa chỉ mặc định nếu CSDL nâng cấp đã được tạo.<br><b>Máy mới:</b> chạy lần lượt 3 tệp SQL trong thư mục <b>database</b>, rồi nhập<br>địa chỉ và tài khoản của máy đó. Xem <b>BAT_DAU_O_DAY.md</b> trong gói.</html>"));
            StringJoiner overridden=new StringJoiner(", ");for(String name:new String[]{"URL","USER","PASSWORD"})if(System.getenv("QLTC_DB_"+name)!=null)overridden.add("QLTC_DB_"+name);
            if(overridden.length()>0){add(Box.createVerticalStrut(12));addRow(new JLabel("<html>Biến môi trường đang được ưu tiên: "+overridden+".<br>Giá trị nhập bên trên không thay thế các biến này.</html>"));}
        }
        private void addRow(JComponent c){c.setAlignmentX(LEFT_ALIGNMENT);add(c);}
        private void addField(String label,JTextField field){addRow(new JLabel(label));add(Box.createVerticalStrut(6));field.setMaximumSize(new Dimension(Integer.MAX_VALUE,34));field.setPreferredSize(new Dimension(660,34));addRow(field);add(Box.createVerticalStrut(12));}
        private Properties values(){Properties p=new Properties();p.putAll(original);p.setProperty("db.url",url.getText().trim());p.setProperty("db.user",user.getText().trim());char[] secret=password.getPassword();try{p.setProperty("db.password",new String(secret));}finally{Arrays.fill(secret,'\0');}return p;}
        private void setFieldsEnabled(boolean enabled){url.setEnabled(enabled);user.setEnabled(enabled);password.setEnabled(enabled);}
    }
}
