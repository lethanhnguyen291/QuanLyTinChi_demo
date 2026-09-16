package ui;
import service.*;
import utils.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;
public class PortalPanel extends JPanel{
    private final JComboBox<String> term=new JComboBox<>();private final JPanel content=new JPanel(new BorderLayout());private final JLabel title=new JLabel();
    private final Map<String,Supplier<JComponent>> pages=new LinkedHashMap<>();private final Map<String,JButton> buttons=new LinkedHashMap<>();private String current;
    private JComponent currentView;private String loadedTerm;private boolean restoringTerm;
    public PortalPanel(StudentManagerService service,boolean admin,String student,Runnable logout){
        setLayout(new BorderLayout());setBackground(UIUtils.BG_APP);
        JPanel sidebar=new JPanel(new BorderLayout());sidebar.setPreferredSize(new Dimension(256,0));sidebar.setBackground(UIUtils.MIT_RED);
        JPanel brand=new JPanel();brand.setLayout(new BoxLayout(brand,BoxLayout.Y_AXIS));brand.setOpaque(false);brand.setBorder(BorderFactory.createEmptyBorder(26,24,20,16));
        JLabel logo=new JLabel("MIT / PORTAL");logo.setForeground(Color.WHITE);logo.setFont(new Font("Segoe UI",Font.BOLD,22));brand.add(logo);brand.add(Box.createVerticalStrut(6));
        JLabel caption=new JLabel(admin?"KHÔNG GIAN QUẢN TRỊ":"KHÔNG GIAN SINH VIÊN");caption.setForeground(new Color(239,183,174));caption.setFont(new Font("Segoe UI",Font.PLAIN,10));brand.add(caption);brand.add(Box.createVerticalStrut(22));
        String identity=admin?"Cán bộ đào tạo":student;try{if(!admin){var r=Db.rows("SELECT HoTen FROM SINH_VIEN WHERE MaSV=?",student);if(!r.isEmpty())identity=Db.str(r.get(0),"HoTen");}}catch(Exception e){Ui.error(this,e);}
        JLabel who=new JLabel(identity);who.setForeground(new Color(255,228,220));who.setFont(new Font("Segoe UI",Font.BOLD,13));who.setToolTipText(identity);brand.add(who);sidebar.add(brand,BorderLayout.NORTH);
        JPanel menu=new JPanel();menu.setLayout(new BoxLayout(menu,BoxLayout.Y_AXIS));menu.setOpaque(false);menu.setBorder(BorderFactory.createEmptyBorder(0,12,8,12));
        if(admin){
            pages.put("Tổng quan",()->{var p=new AdminDashboardPanel();p.updateData(term());return p;});
            pages.put("Sinh viên & Giảng viên",()->new AdminUserPanel(service));
            pages.put("Lớp học phần & Lịch học",()->new ClassAdminPanel(term()));
            pages.put("Nhập & Quản lý điểm",()->new GradePanel(term()));
            pages.put("Thu học phí",()->{var p=new AdminCongNoPanel();p.updateData(term());return p;});
            pages.put("Báo cáo học vụ",()->new AdminBaoCaoPanel(service));
            pages.put("Nhập dữ liệu Excel",()->new ExcelImportPanel());
            pages.put("Tài khoản & Ngoại ngữ",AccountAdminPanel::new);
            pages.put("Cấu hình học kỳ",()->new TermSettingsPanel(term()));
            pages.put("Nhật ký & Phiếu thu",AuditPanel::new);
        }else{
            pages.put("Tổng quan",()->{var p=new DashboardPanel(student);p.updateData(term(),fullTerm());return p;});
            pages.put("Lịch học theo tuần",()->new WeeklySchedulePanel(student,term()));
            pages.put("Kết quả học tập",()->{var p=new DiemPanel(student);p.updateData(term(),fullTerm());return p;});
            pages.put("Đăng ký học phần",()->new EnrollmentPanel(student,term()));
            pages.put("Học phí & Công nợ",()->new CongNoPanel(service,student));
            pages.put("Điều kiện tốt nghiệp",()->new TotNghiepPanel(service,student));
        }

        int index=0;int[] studentIcons={0,2,3,6,4,8};
        for(String name:pages.keySet()){
            JButton button=UIUtils.navigationButton(name,admin?index:studentIcons[index]);index++;button.addActionListener(e->showPage(name));buttons.put(name,button);menu.add(button);menu.add(Box.createVerticalStrut(4));
        }
        JScrollPane menuScroll=Ui.scroll(menu);menuScroll.setOpaque(false);menuScroll.getViewport().setOpaque(false);menuScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);sidebar.add(menuScroll,BorderLayout.CENTER);
        JPanel bottom=new JPanel(new GridLayout(0,1,0,4));bottom.setOpaque(false);bottom.setBorder(BorderFactory.createEmptyBorder(12,12,16,12));
        JButton password=UIUtils.navigationButton("Đổi mật khẩu",7),exit=UIUtils.navigationButton("Đăng xuất",10);password.addActionListener(e->AccountDialog.change(this,false));exit.addActionListener(e->{if(confirmDiscard())logout.run();});bottom.add(password);bottom.add(exit);sidebar.add(bottom,BorderLayout.SOUTH);
        JPanel right=new JPanel(new BorderLayout());right.setBackground(UIUtils.BG_APP);
        JPanel header=new JPanel(new BorderLayout(0,14));header.setBackground(Color.WHITE);header.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createMatteBorder(0,0,1,0,UIUtils.BORDER),BorderFactory.createEmptyBorder(20,26,18,26)));
        JPanel heading=new JPanel(new BorderLayout());heading.setOpaque(false);title.setFont(new Font("Segoe UI",Font.BOLD,24));title.setForeground(UIUtils.TEXT_MAIN);heading.add(title,BorderLayout.WEST);
        JLabel role=new JLabel(admin?"  Cán bộ đào tạo  ":"  Sinh viên  ");role.setFont(new Font("Segoe UI",Font.BOLD,12));role.setForeground(UIUtils.MIT_RED);role.setBackground(UIUtils.MIT_RED_LIGHT);role.setOpaque(true);role.setBorder(BorderFactory.createEmptyBorder(6,10,6,10));heading.add(role,BorderLayout.EAST);header.add(heading,BorderLayout.NORTH);
        JPanel controls=new JPanel(new BorderLayout(10,0));controls.setOpaque(false);JPanel period=new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));period.setOpaque(false);
        JLabel periodLabel=new JLabel("Học kỳ  ");periodLabel.setFont(UIUtils.FONT_BOLD);periodLabel.setForeground(UIUtils.MIT_RED);period.add(periodLabel);
        try{for(var r:Db.rows("SELECT MaHK,TenHK,NamHoc FROM HOC_KY ORDER BY NamHoc DESC,MaHK DESC"))term.addItem(Db.str(r,"MaHK")+" - "+Db.str(r,"TenHK"));}catch(Exception e){Ui.error(this,e);}
        term.setPreferredSize(new Dimension(370,38));period.add(term);controls.add(period,BorderLayout.WEST);
        JButton refresh=UIUtils.createSecondaryBtn("Làm mới dữ liệu");refresh.addActionListener(e->showPage(current));controls.add(refresh,BorderLayout.EAST);header.add(controls,BorderLayout.CENTER);UIUtils.applyTheme(header);
        content.setBackground(UIUtils.BG_APP);content.setBorder(BorderFactory.createEmptyBorder(20,22,18,22));right.add(header,BorderLayout.NORTH);right.add(content,BorderLayout.CENTER);
        JLabel status=new JLabel("MIT PORTAL  ·  Quản lý đào tạo                                      Tài khoản: "+student);status.setFont(new Font("Segoe UI",Font.PLAIN,11));status.setForeground(UIUtils.TEXT_MUTED);status.setBorder(BorderFactory.createEmptyBorder(6,24,10,24));right.add(status,BorderLayout.SOUTH);
        add(sidebar,BorderLayout.WEST);add(right,BorderLayout.CENTER);term.addActionListener(e->{if(current!=null)showPage(current);});showPage(pages.keySet().iterator().next());
    }
    private String fullTerm(){return Objects.toString(term.getSelectedItem(),"");}private String term(){return fullTerm().split(" - ",2)[0];}
    private void showPage(String name){if(name==null||restoringTerm)return;
        if(!confirmDiscard()){restoringTerm=true;term.setSelectedItem(loadedTerm);restoringTerm=false;return;}
        current=name;loadedTerm=fullTerm();title.setText(name);buttons.forEach((key,b)->UIUtils.selectNavigation(b,key.equals(name)));content.removeAll();currentView=null;
        try{if(term().isBlank())throw new IllegalArgumentException("CSDL chưa có học kỳ. Hãy nhập dữ liệu học kỳ trước.");JComponent page=pages.get(name).get();currentView=page;UIUtils.applyTheme(page);if(page instanceof DashboardPanel||page instanceof TotNghiepPanel)content.add(Ui.scroll(page));else content.add(page);}
        catch(Exception e){JLabel error=new JLabel("Không tải được trang. Hãy kiểm tra kết nối và bấm Làm mới.");content.add(error);Ui.error(this,e);}content.revalidate();content.repaint();}
    private boolean confirmDiscard(){return !(currentView instanceof PendingChanges p)||!p.hasUnsavedChanges()||JOptionPane.showConfirmDialog(this,"Điểm vừa sửa chưa được lưu. Rời trang và bỏ thay đổi?","Chưa lưu điểm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;}
}
