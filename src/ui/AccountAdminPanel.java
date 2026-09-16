package ui;
import utils.*;
import service.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public final class AccountAdminPanel extends JPanel{
    private final DefaultTableModel model=Ui.model("Mã SV","Họ tên","Tên đăng nhập","Trạng thái","Chuẩn ngoại ngữ");
    private final JTable table=new JTable(model);
    private final JLabel summary=new JLabel("Đang tải tài khoản…");
    private final JButton bulk=UIUtils.createPrimaryBtn("Cấp tài khoản còn thiếu");
    private final JButton reset=new JButton("Cấp / Đặt lại mật khẩu"),lock=new JButton("Khóa / Mở khóa"),language=new JButton("Cập nhật ngoại ngữ"),refresh=new JButton("Làm mới");
    private boolean busy;
    public AccountAdminPanel(){
        setLayout(new BorderLayout(0,14));setBackground(UIUtils.BG_APP);UIUtils.styleTable(table);
        JPanel top=new JPanel(new BorderLayout(0,12));top.setOpaque(false);
        summary.setFont(UIUtils.FONT_BOLD);summary.setForeground(UIUtils.TEXT_MAIN);
        JPanel stats=new JPanel(new BorderLayout(10,0));stats.setOpaque(false);stats.add(summary,BorderLayout.CENTER);stats.add(refresh,BorderLayout.EAST);
        top.add(stats,BorderLayout.NORTH);top.add(Ui.tableTools(table,"Tai_khoan_sinh_vien"),BorderLayout.SOUTH);add(top,BorderLayout.NORTH);add(Ui.scroll(table),BorderLayout.CENTER);
        JPanel bottom=new JPanel(new BorderLayout(0,8));bottom.setOpaque(false);JPanel bar=new JPanel(new FlowLayout(FlowLayout.LEFT,8,0));bar.setOpaque(false);
        bar.add(bulk);bar.add(reset);bar.add(lock);bar.add(language);bottom.add(bar,BorderLayout.NORTH);
        JLabel hint=new JLabel("Tên đăng nhập = mã sinh viên. Tài khoản mới cần đổi mật khẩu ở lần đăng nhập đầu tiên.");hint.setFont(new Font("Segoe UI",Font.PLAIN,12));hint.setForeground(UIUtils.TEXT_MUTED);bottom.add(hint,BorderLayout.SOUTH);add(bottom,BorderLayout.SOUTH);
        bulk.addActionListener(e->provisionMissing());refresh.addActionListener(e->load());
        reset.addActionListener(e->{String sv=selected();if(sv==null)return;char[] secret=temporaryPassword("Cấp / Đặt lại mật khẩu cho "+sv,"Mật khẩu mới sẽ thay mật khẩu hiện tại của sinh viên này.");if(secret==null)return;
            setBusy(true);
            run(()->{try{new AccountService().provisionStudent(sv,secret);return "Đã cấp/đặt lại tài khoản "+sv+".";}finally{Arrays.fill(secret,'\0');}});
        });
        lock.addActionListener(e->{String sv=selected();if(sv==null)return;int row=table.convertRowIndexToModel(table.getSelectedRow());
            if("Chưa cấp".equals(model.getValueAt(row,2))){JOptionPane.showMessageDialog(this,"Sinh viên này chưa có tài khoản. Hãy cấp tài khoản trước.");return;}
            boolean locked="Đã khóa".equals(model.getValueAt(row,3));
            if(JOptionPane.showConfirmDialog(this,(locked?"Mở khóa ":"Khóa ")+sv+"?","Xác nhận",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
            setBusy(true);run(()->{new AccountService().setLocked(sv,!locked);return null;});
        });
        language.addActionListener(e->{String sv=selected();if(sv==null)return;Object choice=JOptionPane.showInputDialog(this,"Trạng thái chuẩn ngoại ngữ của "+sv,"Chuẩn đầu ra",JOptionPane.QUESTION_MESSAGE,null,new String[]{"Đạt","Chưa đạt"},"Chưa đạt");if(choice==null)return;
            setBusy(true);run(()->Db.transaction(c->{Session.requireAdmin();Db.update(c,"UPDATE SINH_VIEN SET DatChuanNgoaiNgu=? WHERE MaSV=?",choice.equals("Đạt")?"1":"0",sv);SchemaService.audit(c,"CẬP NHẬT NGOẠI NGỮ",sv+" / "+choice);return null;}));
        });
        load();
    }
    private void provisionMissing(){
        ArrayList<String> ids=new ArrayList<>();for(int r=0;r<model.getRowCount();r++)if("Chưa cấp".equals(model.getValueAt(r,2)))ids.add(model.getValueAt(r,0).toString());
        if(ids.isEmpty()){JOptionPane.showMessageDialog(this,"Tất cả sinh viên đã có tài khoản.");return;}
        char[] secret=temporaryPassword("Cấp "+ids.size()+" tài khoản sinh viên","Áp dụng cho toàn bộ "+ids.size()+" sinh viên chưa có tài khoản, kể cả dòng đang bị lọc.");
        if(secret==null)return;setBusy(true);summary.setText("Đang cấp "+ids.size()+" tài khoản, vui lòng chờ…");
        run(()->{try{var result=new AccountService().provisionMissingStudents(ids,secret);return "Đã tạo "+result.created()+" tài khoản. Bỏ qua "+result.skipped()+" tài khoản đã có.\nTên đăng nhập là mã sinh viên; mật khẩu tạm là mật khẩu bạn vừa nhập.\nSinh viên phải đổi mật khẩu khi đăng nhập lần đầu.";}finally{Arrays.fill(secret,'\0');}});
    }
    private char[] temporaryPassword(String title,String description){
        JPasswordField field=new JPasswordField(),again=new JPasswordField();JPanel form=new JPanel(new GridLayout(0,1,0,8));
        form.add(new JLabel(description));form.add(new JLabel("Mật khẩu tạm (8–128 ký tự)"));form.add(field);form.add(new JLabel("Nhập lại mật khẩu tạm"));form.add(again);
        JCheckBox reveal=new JCheckBox("Hiện mật khẩu");char echo=field.getEchoChar();reveal.addActionListener(e->{field.setEchoChar(reveal.isSelected()?'\0':echo);again.setEchoChar(reveal.isSelected()?'\0':echo);});form.add(reveal);
        form.add(new JLabel("Tài khoản mới dùng mật khẩu tạm này và buộc đổi ở lần đầu đăng nhập."));
        UIUtils.applyTheme(form);
        if(JOptionPane.showConfirmDialog(this,form,title,JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE)!=JOptionPane.OK_OPTION)return null;
        char[] a=field.getPassword(),b=again.getPassword();try{
            if(!Arrays.equals(a,b))throw new IllegalArgumentException("Hai mật khẩu chưa khớp.");
            PasswordHash.validate(a);return a;
        }catch(Exception e){Arrays.fill(a,'\0');Ui.error(this,e);return null;}finally{Arrays.fill(b,'\0');field.setText("");again.setText("");}
    }
    private void run(java.util.concurrent.Callable<String> action){
        new SwingWorker<String,Void>(){
            protected String doInBackground()throws Exception{return action.call();}
            protected void done(){try{String result=get();if(result!=null)JOptionPane.showMessageDialog(AccountAdminPanel.this,result);}catch(Exception e){Ui.error(AccountAdminPanel.this,e);}finally{load();}}
        }.execute();
    }
    private void setBusy(boolean value){busy=value;for(JButton b:new JButton[]{bulk,reset,lock,language,refresh})b.setEnabled(!value);}
    private String selected(){if(busy||table.getSelectedRow()<0){JOptionPane.showMessageDialog(this,"Chọn một sinh viên trong bảng.");return null;}return table.getValueAt(table.getSelectedRow(),0).toString();}
    private void load(){
        setBusy(true);
        new SwingWorker<java.util.List<java.util.Map<String,Object>>,Void>(){
            protected java.util.List<java.util.Map<String,Object>> doInBackground()throws Exception{
                Session.requireAdmin();return Db.rows("SELECT s.MaSV,s.HoTen,s.DatChuanNgoaiNgu,a.TenDangNhap,a.Khoa,a.VaiTro,a.BatBuocDoi FROM SINH_VIEN s LEFT JOIN QLTC_TAI_KHOAN a ON a.TenDangNhap=s.MaSV ORDER BY s.MaSV");
            }
            protected void done(){
                try{
                    var rows=get();model.setRowCount(0);int missing=0,issued=0;
                    for(var r:rows){
                        boolean exists=r.get("TenDangNhap")!=null,isStudent="STUDENT".equals(r.get("VaiTro"));
                        if(!exists)missing++;if(isStudent)issued++;
                        String state=!exists?"Chưa có tài khoản":!isStudent?"Trùng mã quản trị":Boolean.TRUE.equals(r.get("Khoa"))?"Đã khóa":Boolean.TRUE.equals(r.get("BatBuocDoi"))?"Cần đổi mật khẩu":"Hoạt động";
                        model.addRow(new Object[]{r.get("MaSV"),r.get("HoTen"),exists?r.get("TenDangNhap"):"Chưa cấp",state,AcademicService.languagePassed(r.get("DatChuanNgoaiNgu"))?"Đạt":"Chưa đạt"});
                    }
                    summary.setText(rows.size()+" sinh viên  •  "+issued+" tài khoản đã cấp  •  "+missing+" chưa cấp");
                }catch(Exception e){summary.setText("Không tải được tài khoản. Bấm Làm mới để thử lại.");Ui.error(AccountAdminPanel.this,e);}
                finally{setBusy(false);}
            }
        }.execute();
    }
}
