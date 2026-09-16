package service;
import utils.*;
import java.math.*;
import java.util.*;
public final class PaymentService{
    public String collect(String invoice,BigDecimal amount,String note,String requestId)throws Exception{
        Session.requireAdmin();validate(amount);UUID.fromString(requestId);
        return Db.transaction(c->{
            var r=Db.rows(c,"SELECT MaSV,MaHK FROM CONG_NO_HOC_PHI WHERE MaPhieu=?",invoice);if(r.size()!=1)throw new IllegalArgumentException("Không tìm thấy phiếu học phí.");
            Db.lock(c,"BILL:"+Db.str(r.get(0),"MaSV")+":"+Db.str(r.get(0),"MaHK"));
            if(Db.count(c,"SELECT COUNT(*) FROM QLTC_PHIEU_THU WHERE MaGiaoDich=?",requestId)>0)return "Giao dịch đã được ghi nhận trước đó: "+requestId;
            var b=Db.rows(c,"SELECT TongTienPhaiDong,SoTienDaDong FROM CONG_NO_HOC_PHI WITH(UPDLOCK,HOLDLOCK) WHERE MaPhieu=?",invoice).get(0);
            BigDecimal total=new BigDecimal(Objects.toString(b.get("TongTienPhaiDong"),"0")),paid=new BigDecimal(Objects.toString(b.get("SoTienDaDong"),"0"));
            if(amount.compareTo(total.subtract(paid))>0)throw new IllegalArgumentException("Số tiền thu vượt số còn nợ: "+total.subtract(paid).toPlainString()+" đồng.");
            BigDecimal next=paid.add(amount);
            if(Db.update(c,"UPDATE CONG_NO_HOC_PHI SET SoTienDaDong=?,TrangThai=? WHERE MaPhieu=?",next.toPlainString(),next.compareTo(total)>=0?"Đã hoàn thành":"Còn nợ",invoice)!=1)throw new IllegalArgumentException("Không lưu được phiếu thu.");
            Db.update(c,"INSERT INTO QLTC_PHIEU_THU(MaGiaoDich,MaPhieu,SoTien,NguoiThu,GhiChu)VALUES(?,?,?,?,?)",requestId,invoice,amount,Session.user(),Objects.toString(note,""));
            SchemaService.audit(c,"THU HỌC PHÍ",invoice+" / "+amount.toPlainString()+" / "+requestId);return "Đã ghi nhận "+amount.toPlainString()+" đồng. Mã phiếu thu: "+requestId;
        });
    }
    public static void validate(BigDecimal amount){if(amount==null||amount.signum()<=0||amount.scale()>2||amount.compareTo(new BigDecimal("999999999999.99"))>0)throw new IllegalArgumentException("Số tiền phải lớn hơn 0, tối đa 2 chữ số thập phân.");}
}
