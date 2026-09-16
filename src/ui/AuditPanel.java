package ui;
import utils.*;
import javax.swing.*;
import java.awt.*;
public final class AuditPanel extends JPanel{
    public AuditPanel(){setLayout(new BorderLayout());setBackground(UIUtils.BG_APP);JTabbedPane tabs=new JTabbedPane();
        tabs.addTab("Nhật ký thao tác",view("SELECT TOP 1000 ThoiGian,NguoiDung,HanhDong,ChiTiet FROM QLTC_NHAT_KY ORDER BY Id DESC",new String[]{"Thời gian","Tài khoản","Thao tác","Chi tiết"},new String[]{"ThoiGian","NguoiDung","HanhDong","ChiTiet"},"Nhat_ky"));
        tabs.addTab("Lịch sử phiếu thu",view("SELECT TOP 1000 ThoiGian,MaGiaoDich,MaPhieu,SoTien,NguoiThu,GhiChu FROM QLTC_PHIEU_THU ORDER BY ThoiGian DESC",new String[]{"Thời gian","Mã giao dịch","Phiếu học phí","Số tiền","Người thu","Ghi chú"},new String[]{"ThoiGian","MaGiaoDich","MaPhieu","SoTien","NguoiThu","GhiChu"},"Phieu_thu"));add(tabs);}
    private JPanel view(String sql,String[] cols,String[] keys,String file){JPanel p=Ui.page();var model=Ui.model(cols);JTable table=new JTable(model);UIUtils.styleTable(table);p.add(Ui.tableTools(table,file),BorderLayout.NORTH);p.add(Ui.scroll(table),BorderLayout.CENTER);p.add(new JLabel("Hiển thị tối đa 1.000 bản ghi gần nhất; nhật ký bắt đầu từ lúc nâng cấp."),BorderLayout.SOUTH);
        try{for(var r:Db.rows(sql)){Object[] data=new Object[keys.length];for(int i=0;i<keys.length;i++)data[i]=r.get(keys[i]);model.addRow(data);}}catch(Exception e){Ui.error(this,e);}return p;}
}
