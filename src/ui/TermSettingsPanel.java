package ui;
import utils.*;
import service.*;
import javax.swing.*;
import java.awt.*;
import java.math.*;
import java.time.*;
public final class TermSettingsPanel extends JPanel{
    public TermSettingsPanel(String term){setLayout(new BorderLayout(0,18));setBackground(UIUtils.BG_APP);
        JPanel form=new JPanel(new GridLayout(0,2,18,18));form.setBackground(Color.WHITE);form.setBorder(UIUtils.cardBorder(UIUtils.BORDER,24));
        JCheckBox open=new JCheckBox("Cho phép đăng ký / hủy");JTextField start=UIUtils.createInput(),end=UIUtils.createInput(),price=UIUtils.createInput(),max=UIUtils.createInput();
        form.add(new JLabel("Học kỳ"));form.add(new JLabel(term));form.add(new JLabel("Trạng thái"));form.add(open);form.add(new JLabel("Mở từ ngày (yyyy-MM-dd)"));form.add(start);form.add(new JLabel("Đến hết ngày (yyyy-MM-dd)"));form.add(end);form.add(new JLabel("Đơn giá / tín chỉ (đồng)"));form.add(price);form.add(new JLabel("Tín chỉ tối đa / học kỳ"));form.add(max);
        try{var list=Db.rows("SELECT * FROM QLTC_CAU_HINH_HK WHERE MaHK=?",term);if(!list.isEmpty()){var r=list.get(0);open.setSelected(Boolean.TRUE.equals(r.get("MoDangKy")));start.setText(Db.str(r,"BatDau"));end.setText(Db.str(r,"KetThuc"));price.setText(Db.str(r,"DonGia"));max.setText(Db.str(r,"TinChiToiDa"));}else{price.setText("450000");max.setText("24");}}catch(Exception e){Ui.error(this,e);}
        JButton save=UIUtils.createPrimaryBtn("Lưu cấu hình học kỳ");JPanel body=Ui.page();body.add(form,BorderLayout.NORTH);
        JTextArea help=new JTextArea("Học kỳ mới mặc định đóng đăng ký.\n\nThời gian đăng ký và đơn giá được áp dụng tại tầng nghiệp vụ.\nKhông đổi đơn giá sau khi đã có lượt đăng ký để tránh lệch phiếu thu.\n\nĐiểm: chuyên cần 10%, giữa kỳ 30%, cuối kỳ 60%; đạt từ 4/10.\nGPA tính trọng số tín chỉ; học lại lấy điểm cao nhất của từng môn.\nTín chỉ tốt nghiệp lấy từ chương trình đào tạo của sinh viên.\nĐây là quy ước cấu hình cho đồ án; cần điều chỉnh nếu trường dùng quy chế khác.");help.setEditable(false);help.setOpaque(false);help.setFont(UIUtils.FONT_NORMAL);help.setLineWrap(true);help.setWrapStyleWord(true);body.add(UIUtils.noteCard("Hướng dẫn cấu hình",help.getText()),BorderLayout.CENTER);add(body,BorderLayout.CENTER);add(save,BorderLayout.SOUTH);
        save.addActionListener(e->{try{java.sql.Date a=start.getText().isBlank()?null:java.sql.Date.valueOf(LocalDate.parse(start.getText().trim())),b=end.getText().isBlank()?null:java.sql.Date.valueOf(LocalDate.parse(end.getText().trim()));boolean enabled=open.isSelected();
            if(enabled&&(a==null||b==null))throw new IllegalArgumentException("Nhập đủ ngày mở và ngày kết thúc.");if(a!=null&&b!=null&&a.after(b))throw new IllegalArgumentException("Ngày mở phải trước hoặc bằng ngày kết thúc.");
            BigDecimal fee=new BigDecimal(price.getText().trim());int cap=Integer.parseInt(max.getText().trim());if(fee.signum()<0||fee.scale()>2||cap<1||cap>60)throw new IllegalArgumentException("Đơn giá không âm; tín chỉ tối đa từ 1 đến 60.");
            Ui.async(this,save,()->Db.transaction(c->{Session.requireAdmin();Db.lock(c,"ACADEMIC");Object old=Db.scalar(c,"SELECT DonGia FROM QLTC_CAU_HINH_HK WHERE MaHK=?",term);
                if(old!=null&&fee.compareTo(new BigDecimal(old.toString()))!=0&&Db.count(c,"SELECT COUNT(*) FROM KET_QUA_DANG_KY k JOIN LOP_HOC_PHAN l ON l.MaLHP=k.MaLHP WHERE l.MaHK=?",term)>0)throw new IllegalArgumentException("Học kỳ đã có đăng ký. Không thể đổi đơn giá trực tiếp.");
                if(old==null)Db.update(c,"INSERT INTO QLTC_CAU_HINH_HK(MaHK)VALUES(?)",term);
                Db.update(c,"UPDATE QLTC_CAU_HINH_HK SET MoDangKy=?,BatDau=?,KetThuc=?,DonGia=?,TinChiToiDa=? WHERE MaHK=?",enabled,a,b,fee,cap,term);SchemaService.audit(c,"CẤU HÌNH HỌC KỲ",term+" / mở="+enabled+" / "+a+" → "+b);return true;}),v->JOptionPane.showMessageDialog(this,"Đã lưu cấu hình."));
        }catch(Exception ex){Ui.error(this,ex);}});
    }
}
