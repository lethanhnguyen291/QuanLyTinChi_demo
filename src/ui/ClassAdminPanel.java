package ui;
import utils.*;
import service.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;
public final class ClassAdminPanel extends JPanel{
    private final String term;private final DefaultTableModel model=Ui.model("Mã lớp","Môn học","Giảng viên","Thứ","Tiết","Phòng","Sức chứa","Đã đăng ký");private final JTable table=new JTable(model);private List<Map<String,Object>> rows=List.of();
    public ClassAdminPanel(String term){this.term=term;setLayout(new BorderLayout(0,14));setBackground(UIUtils.BG_APP);UIUtils.styleTable(table);UIUtils.columnWidths(table,145,255,200,100,80,90,95,100);add(Ui.tableTools(table,"Lop_hoc_phan"),BorderLayout.NORTH);add(Ui.scroll(table),BorderLayout.CENTER);
        JPanel buttons=new JPanel(new FlowLayout(FlowLayout.RIGHT));buttons.setOpaque(false);JButton add=UIUtils.createPrimaryBtn("Mở lớp mới"),edit=UIUtils.createPrimaryBtn("Sửa lịch / Phân công"),del=UIUtils.createPrimaryBtn("Xóa lớp trống");buttons.add(add);buttons.add(edit);buttons.add(del);add(buttons,BorderLayout.SOUTH);
        add.addActionListener(e->form(null));edit.addActionListener(e->{var r=selected();if(r!=null)form(r);});del.addActionListener(e->{var r=selected();if(r==null)return;String id=Db.str(r,"MaLHP");if(JOptionPane.showConfirmDialog(this,"Xóa lớp trống "+id+"?","Xác nhận",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
            Ui.async(this,del,()->{new ClassService().delete(id);return true;},v->load());});load();}
    private Map<String,Object> selected(){if(table.getSelectedRow()<0){JOptionPane.showMessageDialog(this,"Chọn một lớp học phần.");return null;}return rows.get(table.convertRowIndexToModel(table.getSelectedRow()));}
    private void load(){model.setRowCount(0);try{rows=Db.rows("SELECT l.*,m.TenMon,g.HoTen,(SELECT COUNT(*) FROM KET_QUA_DANG_KY k WHERE k.MaLHP=l.MaLHP) AS Enrolled FROM LOP_HOC_PHAN l JOIN MON_HOC m ON m.MaMon=l.MaMon LEFT JOIN GIANG_VIEN g ON g.MaGV=l.MaGV WHERE l.MaHK=? ORDER BY l.MaLHP",term);
        for(var r:rows)model.addRow(new Object[]{r.get("MaLHP"),r.get("TenMon"),r.get("HoTen"),r.get("Thu"),r.get("TietHoc"),r.get("PhongHoc"),r.get("SucChua"),r.get("Enrolled")});}catch(Exception e){Ui.error(this,e);}}
    private void form(Map<String,Object> old){
        JTextField id=UIUtils.createInput(),period=UIUtils.createInput(),room=UIUtils.createInput(),capacity=UIUtils.createInput();JComboBox<String> subject=new JComboBox<>(),teacher=new JComboBox<>(),day=new JComboBox<>(new String[]{"Thứ 2","Thứ 3","Thứ 4","Thứ 5","Thứ 6","Thứ 7","Chủ Nhật"});
        try{for(var r:Db.rows("SELECT MaMon,TenMon FROM MON_HOC ORDER BY MaMon"))subject.addItem(Db.str(r,"MaMon")+" • "+Db.str(r,"TenMon"));for(var r:Db.rows("SELECT MaGV,HoTen FROM GIANG_VIEN ORDER BY MaGV"))teacher.addItem(Db.str(r,"MaGV")+" • "+Db.str(r,"HoTen"));}catch(Exception e){Ui.error(this,e);return;}
        if(old!=null){id.setText(Db.str(old,"MaLHP"));id.setEditable(false);select(subject,Db.str(old,"MaMon"));select(teacher,Db.str(old,"MaGV"));try{day.setSelectedIndex(ScheduleRules.day(Db.str(old,"Thu"))-2);}catch(Exception ignored){}period.setText(Db.str(old,"TietHoc"));room.setText(Db.str(old,"PhongHoc"));capacity.setText(Db.str(old,"SucChua"));}else{period.setText("1-3");capacity.setText("40");}
        JPanel form=new JPanel(new GridLayout(0,2,14,12));form.add(new JLabel("Mã lớp"));form.add(id);form.add(new JLabel("Môn học"));form.add(subject);form.add(new JLabel("Giảng viên"));form.add(teacher);form.add(new JLabel("Thứ học"));form.add(day);form.add(new JLabel("Tiết học (1–15; ví dụ 1-3)"));form.add(period);form.add(new JLabel("Phòng"));form.add(room);form.add(new JLabel("Sức chứa"));form.add(capacity);
        UIUtils.applyTheme(form);
        while(JOptionPane.showConfirmDialog(this,form,old==null?"Mở lớp mới":"Điều chỉnh lớp học phần",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE)==JOptionPane.OK_OPTION){
            try{new ClassService().save(old==null,id.getText().trim(),code(subject),term,code(teacher),Objects.toString(day.getSelectedItem()),period.getText().trim(),room.getText().trim(),Integer.parseInt(capacity.getText().trim()));load();return;}catch(Exception e){Ui.error(this,e);}}
    }
    private static String code(JComboBox<String> cb){return Objects.toString(cb.getSelectedItem(),"").split(" • ",2)[0];}
    private static void select(JComboBox<String> cb,String code){for(int i=0;i<cb.getItemCount();i++)if(cb.getItemAt(i).startsWith(code+" • ")){cb.setSelectedIndex(i);return;}}
}
