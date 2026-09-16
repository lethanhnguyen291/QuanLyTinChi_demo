package ui;
import utils.*;
import service.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
import java.util.List;
public final class GradePanel extends JPanel implements PendingChanges{
    private final JComboBox<String> classes=new JComboBox<>();
    private final DefaultTableModel model=new DefaultTableModel(new String[]{"Mã SV","Họ tên","Chuyên cần","Giữa kỳ","Cuối kỳ","Tổng kết","Trạng thái"},0){public boolean isCellEditable(int r,int c){return c>=2&&c<=4;}};
    private final JTable table=new JTable(model);private List<Map<String,Object>> original=List.of();private Object loadedClass;private boolean restoringClass;
    public GradePanel(String term){
        setLayout(new BorderLayout(0,14));setBackground(UIUtils.BG_APP);
        JPanel top=new JPanel(new BorderLayout(12,8));top.setOpaque(false);top.add(new JLabel("Lớp học phần:"),BorderLayout.WEST);top.add(classes,BorderLayout.CENTER);
        JTextArea hint=new JTextArea("Quy ước đồ án: chuyên cần 10% · giữa kỳ 30% · cuối kỳ 60%. Chỉ lưu các dòng đã sửa, đủ 3 điểm (0–10).");hint.setEditable(false);hint.setOpaque(false);hint.setLineWrap(true);hint.setWrapStyleWord(true);hint.setRows(2);hint.setForeground(UIUtils.TEXT_MUTED);hint.setFont(new Font("Segoe UI",Font.PLAIN,13));top.add(hint,BorderLayout.SOUTH);
        try{for(var r:Db.rows("SELECT l.MaLHP,m.TenMon FROM LOP_HOC_PHAN l JOIN MON_HOC m ON m.MaMon=l.MaMon WHERE l.MaHK=? ORDER BY l.MaLHP",term))classes.addItem(Db.str(r,"MaLHP")+" • "+Db.str(r,"TenMon"));}catch(Exception e){Ui.error(this,e);}
        UIUtils.styleTable(table);UIUtils.columnWidths(table,115,220,115,105,105,110,170);table.setRowHeight(40);JPanel center=Ui.page();center.add(Ui.tableTools(table,"Bang_diem"),BorderLayout.NORTH);center.add(Ui.scroll(table),BorderLayout.CENTER);
        JPanel actions=new JPanel(new FlowLayout(FlowLayout.RIGHT));actions.setOpaque(false);JButton reload=UIUtils.createPrimaryBtn("Tải lại"),save=UIUtils.createPrimaryBtn("Lưu điểm đã sửa");actions.add(reload);actions.add(save);
        reload.addActionListener(e->{if(discardConfirmed())load();});classes.addActionListener(e->{if(restoringClass)return;if(!discardConfirmed()){restoringClass=true;classes.setSelectedItem(loadedClass);restoringClass=false;return;}load();});save.addActionListener(e->save(save));add(top,BorderLayout.NORTH);add(center,BorderLayout.CENTER);add(actions,BorderLayout.SOUTH);load();
    }
    private String id(){return classes.getSelectedItem()==null?"":classes.getSelectedItem().toString().split(" • ",2)[0];}
    private boolean discardConfirmed(){return !hasUnsavedChanges()||JOptionPane.showConfirmDialog(this,"Bỏ các điểm vừa sửa chưa lưu?","Chưa lưu điểm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION;}
    private void load(){loadedClass=classes.getSelectedItem();model.setRowCount(0);original=List.of();if(id().isEmpty())return;
        try{original=Db.rows("SELECT k.*,s.HoTen FROM KET_QUA_DANG_KY k JOIN SINH_VIEN s ON s.MaSV=k.MaSV WHERE k.MaLHP=? ORDER BY s.MaSV",id());
            for(var r:original)model.addRow(new Object[]{r.get("MaSV"),r.get("HoTen"),r.get("DiemChuyenCan"),r.get("DiemGiuaKy"),r.get("DiemCuoiKy"),r.get("DiemTongKet"),r.get("TrangThai")});
        }catch(Exception e){Ui.error(this,e);}}
    private void save(JButton button){if(table.isEditing()&&!table.getCellEditor().stopCellEditing())return;
        try{List<Map<String,Object>> changed=new ArrayList<>();String[] keys={"DiemChuyenCan","DiemGiuaKy","DiemCuoiKy"};
            for(int row=0;row<model.getRowCount();row++){Map<String,Object> entry=new HashMap<>();entry.put("MaSV",model.getValueAt(row,0));boolean dirty=false;
                for(int i=0;i<keys.length;i++){Object now=model.getValueAt(row,i+2),old=original.get(row).get(keys[i]);entry.put(keys[i],now);entry.put("old_"+keys[i],old);dirty|=!Objects.equals(AcademicService.score(now),AcademicService.score(old));}if(dirty)changed.add(entry);}
            if(changed.isEmpty()){JOptionPane.showMessageDialog(this,"Chưa có thay đổi điểm.");return;}
            if(JOptionPane.showConfirmDialog(this,"Lưu điểm cho "+changed.size()+" sinh viên?","Xác nhận",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
            String classId=id();classes.setEnabled(false);Ui.async(this,button,()->{new AcademicService().saveGrades(classId,changed);return changed.size();},n->{JOptionPane.showMessageDialog(this,"Đã lưu điểm cho "+n+" sinh viên.");load();});
            // The class selector is re-enabled when the worker button returns to its normal state.
            button.addPropertyChangeListener("enabled",event->{if(Boolean.TRUE.equals(event.getNewValue()))classes.setEnabled(true);});
        }catch(Exception e){Ui.error(this,e);}}
    public boolean hasUnsavedChanges(){
        if(table.isEditing())return true;
        String[] keys={"DiemChuyenCan","DiemGiuaKy","DiemCuoiKy"};
        try{for(int r=0;r<model.getRowCount();r++)for(int i=0;i<3;i++)
            if(!Objects.equals(AcademicService.score(model.getValueAt(r,i+2)),AcademicService.score(original.get(r).get(keys[i]))))return true;
        }catch(Exception e){return true;}return false;
    }
}
