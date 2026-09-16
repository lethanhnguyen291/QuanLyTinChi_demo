package ui;
import service.*;
import utils.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.*;
public final class EnrollmentPanel extends JPanel{
    private final String student,term;
    private final DefaultTableModel available=Ui.model("Mã lớp","Môn học","TC","Giảng viên","Thứ / Tiết / Phòng","Sĩ số","Điều kiện");
    private final DefaultTableModel registered=Ui.model("Mã lớp","Môn đã đăng ký","TC","Thứ / Tiết","Kết quả");
    private final JTable classes=new JTable(available),cart=new JTable(registered);
    private final JButton add=UIUtils.createPrimaryBtn("Đăng ký lớp đã chọn"),cancel=UIUtils.createActionButton("Hủy lớp đã chọn");
    private final JLabel status=new JLabel(),credits=new JLabel();private boolean open,busy;
    public EnrollmentPanel(String student,String term){this.student=student;this.term=term;setLayout(new BorderLayout(0,14));setBackground(UIUtils.BG_APP);
        JPanel banner=new JPanel(new BorderLayout());banner.setBackground(UIUtils.MIT_RED);banner.setBorder(UIUtils.cardBorder(UIUtils.MIT_RED,16));status.setForeground(Color.WHITE);status.setFont(UIUtils.FONT_BOLD);credits.setForeground(new Color(230,230,230));banner.add(status,BorderLayout.NORTH);banner.add(credits,BorderLayout.SOUTH);add(banner,BorderLayout.NORTH);
        UIUtils.styleTable(classes);UIUtils.styleTable(cart);classes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);cart.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        int[] widths={125,210,42,150,210,65,145};for(int i=0;i<widths.length;i++)classes.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);classes.setRowHeight(40);cart.setRowHeight(34);
        JPanel top=Ui.page();top.add(Ui.tableTools(classes,"Lop_mo_"+term),BorderLayout.NORTH);top.add(Ui.scroll(classes),BorderLayout.CENTER);
        JPanel registerActions=new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));registerActions.setOpaque(false);registerActions.add(add);top.add(registerActions,BorderLayout.SOUTH);
        JPanel bottom=Ui.page();JPanel sub=new JPanel(new BorderLayout());sub.setOpaque(false);JLabel label=new JLabel("HỌC PHẦN ĐÃ ĐĂNG KÝ");label.setFont(UIUtils.FONT_BOLD);label.setForeground(UIUtils.BLUE_DARK);sub.add(label,BorderLayout.WEST);sub.add(cancel,BorderLayout.EAST);bottom.add(sub,BorderLayout.NORTH);bottom.add(Ui.scroll(cart),BorderLayout.CENTER);
        JSplitPane split=new JSplitPane(JSplitPane.VERTICAL_SPLIT,top,bottom);split.setBorder(BorderFactory.createEmptyBorder());split.setDividerSize(12);split.setResizeWeight(.65);split.setDividerLocation(340);top.setMinimumSize(new Dimension(0,210));bottom.setMinimumSize(new Dimension(0,140));add(split,BorderLayout.CENTER);
        classes.getSelectionModel().addListSelectionListener(e->buttons());cart.getSelectionModel().addListSelectionListener(e->buttons());add.addActionListener(e->mutate(false));cancel.addActionListener(e->mutate(true));load();
    }
    private void buttons(){int row=classes.getSelectedRow();add.setEnabled(!busy&&open&&row>=0&&"Đủ điều kiện".equals(classes.getValueAt(row,6)));cancel.setEnabled(!busy&&open&&cart.getSelectedRow()>=0);}
    private void load(){available.setRowCount(0);registered.setRowCount(0);open=false;
        try{
            var policies=Db.rows("SELECT *,CASE WHEN MoDangKy=1 AND CAST(GETDATE() AS date) BETWEEN BatDau AND KetThuc THEN 1 ELSE 0 END AS DangMo FROM QLTC_CAU_HINH_HK WHERE MaHK=?",term);int max=24;String dates="";
            if(!policies.isEmpty()){var p=policies.get(0);open=Db.number(p,"DangMo")==1;max=Db.number(p,"TinChiToiDa");if(p.get("BatDau")!=null)dates=" · "+Db.str(p,"BatDau")+" → "+Db.str(p,"KetThuc");}
            status.setText((open?"ĐANG MỞ ĐĂNG KÝ":"ĐỢT ĐĂNG KÝ ĐANG ĐÓNG")+"  /  "+term+dates);
            var own=Db.rows("SELECT l.MaLHP,l.MaMon,m.TenMon,m.SoTinChi,l.Thu,l.TietHoc,k.TrangThai FROM KET_QUA_DANG_KY k JOIN LOP_HOC_PHAN l ON l.MaLHP=k.MaLHP JOIN MON_HOC m ON m.MaMon=l.MaMon WHERE k.MaSV=? AND l.MaHK=? ORDER BY l.MaLHP",student,term);
            Set<String> ownIds=new HashSet<>(),ownSubjects=new HashSet<>();int total=0;for(var r:own){ownIds.add(Db.str(r,"MaLHP"));ownSubjects.add(Db.str(r,"MaMon"));total+=Integer.parseInt(Db.str(r,"SoTinChi"));registered.addRow(new Object[]{r.get("MaLHP"),r.get("TenMon"),r.get("SoTinChi"),Db.str(r,"Thu")+" / "+Db.str(r,"TietHoc"),r.get("TrangThai")});}
            credits.setText("Đã đăng ký: "+total+" / "+max+" tín chỉ"+(total>max?"  ·  Dữ liệu hiện có vượt giới hạn; không thể đăng ký thêm.":"  ·  Học phí cập nhật cùng đăng ký."));
            String sql="SELECT l.*,m.TenMon,m.SoTinChi,g.HoTen,(SELECT COUNT(*) FROM KET_QUA_DANG_KY k WHERE k.MaLHP=l.MaLHP) AS SiSo,(SELECT TOP 1 t.MaMonTQ FROM MON_TIEN_QUYET t WHERE t.MaMon=l.MaMon AND NOT EXISTS(SELECT 1 FROM KET_QUA_DANG_KY k2 JOIN LOP_HOC_PHAN l2 ON l2.MaLHP=k2.MaLHP WHERE k2.MaSV=? AND l2.MaMon=t.MaMonTQ AND TRY_CONVERT(decimal(5,2),k2.DiemTongKet)>=4)) AS Missing FROM LOP_HOC_PHAN l JOIN MON_HOC m ON m.MaMon=l.MaMon LEFT JOIN GIANG_VIEN g ON g.MaGV=l.MaGV WHERE l.MaHK=? ORDER BY l.MaLHP";
            for(var r:Db.rows(sql,student,term)){String condition=ownIds.contains(Db.str(r,"MaLHP"))?"Đã đăng ký":ownSubjects.contains(Db.str(r,"MaMon"))?"Trùng môn":Db.number(r,"SiSo")>=Db.number(r,"SucChua")?"Đã đầy":r.get("Missing")!=null?"Thiếu "+Db.str(r,"Missing"):"Đủ điều kiện";
                available.addRow(new Object[]{r.get("MaLHP"),r.get("TenMon"),r.get("SoTinChi"),r.get("HoTen"),Db.str(r,"Thu")+" / "+Db.str(r,"TietHoc")+" / "+Db.str(r,"PhongHoc"),Db.number(r,"SiSo")+" / "+Db.number(r,"SucChua"),condition});}
        }catch(Exception e){status.setText("KHÔNG TẢI ĐƯỢC DỮ LIỆU ĐĂNG KÝ");Ui.error(this,e);}finally{buttons();}
    }
    private void mutate(boolean deleting){if(busy)return;JTable table=deleting?cart:classes;if(table.getSelectedRow()<0)return;String id=table.getValueAt(table.getSelectedRow(),0).toString(),name=table.getValueAt(table.getSelectedRow(),1).toString();
        if(JOptionPane.showConfirmDialog(this,(deleting?"Hủy đăng ký ":"Đăng ký ")+name+" ("+id+")?",deleting?"Hủy học phần":"Đăng ký học phần",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;busy=true;buttons();
        new SwingWorker<String,Void>(){protected String doInBackground()throws Exception{return deleting?new RegistrationService().cancel(student,id):new RegistrationService().register(student,id);}
            protected void done(){try{JOptionPane.showMessageDialog(EnrollmentPanel.this,get());}catch(Exception e){Ui.error(EnrollmentPanel.this,e);}finally{busy=false;load();}}}.execute();
    }
}
