package service;
import utils.*;
import java.sql.*;
import java.math.*;
import java.time.*;
import java.util.*;
public final class RegistrationService {
    public String register(String student,String classId)throws Exception{
        Session.requireStudent(student);
        return Db.transaction(c->{Db.lock(c,"ACADEMIC");var cls=classInfo(c,classId);String term=Db.str(cls,"MaHK"),subject=Db.str(cls,"MaMon");
            var policy=registrationPolicy(c,term);
            Object status=Db.scalar(c,"SELECT TrangThaiHocTap FROM SINH_VIEN WITH(UPDLOCK,HOLDLOCK) WHERE MaSV=?",student);
            if(!"dang hoc".equals(ScheduleRules.normalize(Objects.toString(status,""))))throw new IllegalArgumentException("Trạng thái sinh viên không cho phép đăng ký học phần.");
            if(Db.count(c,"SELECT COUNT(*) FROM KET_QUA_DANG_KY WITH(UPDLOCK,HOLDLOCK) WHERE MaLHP=?",classId)>=Db.number(cls,"SucChua"))throw new IllegalArgumentException("Lớp học phần đã đủ sĩ số.");
            var existing=Db.rows(c,"SELECT l.MaLHP,l.MaMon,l.Thu,l.TietHoc,m.SoTinChi FROM KET_QUA_DANG_KY k JOIN LOP_HOC_PHAN l ON l.MaLHP=k.MaLHP JOIN MON_HOC m ON m.MaMon=l.MaMon WHERE k.MaSV=? AND l.MaHK=?",student,term);
            int credits=Integer.parseInt(Db.str(cls,"SoTinChi"));
            ScheduleRules.day(Db.str(cls,"Thu"));ScheduleRules.periods(Db.str(cls,"TietHoc"));
            for(var r:existing){
                if(subject.equalsIgnoreCase(Db.str(r,"MaMon")))throw new IllegalArgumentException("Bạn đã đăng ký môn này trong học kỳ.");
                if(ScheduleRules.overlaps(Db.str(cls,"Thu"),Db.str(cls,"TietHoc"),Db.str(r,"Thu"),Db.str(r,"TietHoc")))throw new IllegalArgumentException("Trùng lịch với lớp "+Db.str(r,"MaLHP")+" ("+Db.str(r,"TietHoc")+").");
                credits+=Integer.parseInt(Db.str(r,"SoTinChi"));
            }
            if(credits>Db.number(policy,"TinChiToiDa"))throw new IllegalArgumentException("Vượt giới hạn "+Db.number(policy,"TinChiToiDa")+" tín chỉ/học kỳ.");
            for(var pre:Db.rows(c,"SELECT MaMonTQ FROM MON_TIEN_QUYET WHERE MaMon=?",subject)){
                Object best=Db.scalar(c,"SELECT MAX(TRY_CONVERT(decimal(5,2),k.DiemTongKet)) FROM KET_QUA_DANG_KY k JOIN LOP_HOC_PHAN l ON l.MaLHP=k.MaLHP WHERE k.MaSV=? AND l.MaMon=?",student,Db.str(pre,"MaMonTQ"));
                if(best==null||((Number)best).doubleValue()<4)throw new IllegalArgumentException("Chưa đạt môn tiên quyết: "+Db.str(pre,"MaMonTQ"));
            }
            Db.update(c,"INSERT INTO KET_QUA_DANG_KY(MaSV,MaLHP,NgayDangKy,TrangThai) VALUES(?,?,CAST(GETDATE() AS date),N'Chưa có điểm')",student,classId);
            reconcileTuition(c,student,term);SchemaService.audit(c,"ĐĂNG KÝ HỌC PHẦN",student+" / "+classId);return "Đã đăng ký lớp "+classId+" và cập nhật học phí.";
        });
    }
    public String cancel(String student,String classId)throws Exception{
        Session.requireStudent(student);
        return Db.transaction(c->{Db.lock(c,"ACADEMIC");var cls=classInfo(c,classId);String term=Db.str(cls,"MaHK");registrationPolicy(c,term);
            var rows=Db.rows(c,"SELECT * FROM KET_QUA_DANG_KY WITH(UPDLOCK,HOLDLOCK) WHERE MaSV=? AND MaLHP=?",student,classId);
            if(rows.isEmpty())throw new IllegalArgumentException("Bạn chưa đăng ký lớp này.");
            for(String k:List.of("DiemChuyenCan","DiemGiuaKy","DiemCuoiKy","DiemTongKet"))if(AcademicService.score(rows.get(0).get(k))!=null)throw new IllegalArgumentException("Không thể hủy lớp đã có điểm.");
            if(Db.update(c,"DELETE FROM KET_QUA_DANG_KY WHERE MaSV=? AND MaLHP=?",student,classId)!=1)throw new SQLException("Đăng ký đã thay đổi; hãy tải lại.");
            reconcileTuition(c,student,term);SchemaService.audit(c,"HỦY ĐĂNG KÝ",student+" / "+classId);return "Đã hủy đăng ký và cập nhật học phí.";
        });
    }
    private static Map<String,Object> classInfo(Connection c,String id)throws SQLException{
        var r=Db.rows(c,"SELECT l.*,m.SoTinChi FROM LOP_HOC_PHAN l WITH(UPDLOCK,HOLDLOCK) JOIN MON_HOC m ON m.MaMon=l.MaMon WHERE l.MaLHP=?",id);
        if(r.size()!=1)throw new IllegalArgumentException("Lớp học phần không tồn tại.");return r.get(0);
    }
    static Map<String,Object> registrationPolicy(Connection c,String term)throws SQLException{
        var rows=Db.rows(c,"SELECT *,CAST(GETDATE() AS date) AS HomNay FROM QLTC_CAU_HINH_HK WHERE MaHK=?",term);
        if(rows.isEmpty()||!Boolean.TRUE.equals(rows.get(0).get("MoDangKy")))throw new IllegalArgumentException("Học kỳ chưa mở đăng ký. Liên hệ Phòng đào tạo.");
        var r=rows.get(0);LocalDate today=((java.sql.Date)r.get("HomNay")).toLocalDate();
        if(r.get("BatDau")==null||r.get("KetThuc")==null||today.isBefore(((java.sql.Date)r.get("BatDau")).toLocalDate())||today.isAfter(((java.sql.Date)r.get("KetThuc")).toLocalDate()))throw new IllegalArgumentException("Đã ngoài thời gian đăng ký/hủy học phần.");return r;
    }
    static void reconcileTuition(Connection c,String student,String term)throws SQLException{
        Db.lock(c,"BILL:"+student+":"+term);
        int credits=Db.count(c,"SELECT COALESCE(SUM(TRY_CONVERT(int,m.SoTinChi)),0) FROM KET_QUA_DANG_KY k JOIN LOP_HOC_PHAN l ON l.MaLHP=k.MaLHP JOIN MON_HOC m ON m.MaMon=l.MaMon WHERE k.MaSV=? AND l.MaHK=?",student,term);
        Object price=Db.scalar(c,"SELECT DonGia FROM QLTC_CAU_HINH_HK WHERE MaHK=?",term);if(price==null)throw new IllegalArgumentException("Chưa cấu hình đơn giá học kỳ.");
        BigDecimal total=new BigDecimal(price.toString()).multiply(BigDecimal.valueOf(credits));
        var bills=Db.rows(c,"SELECT MaPhieu,SoTienDaDong FROM CONG_NO_HOC_PHI WITH(UPDLOCK,HOLDLOCK) WHERE MaSV=? AND MaHK=?",student,term);
        if(bills.size()>1)throw new IllegalArgumentException("Có nhiều phiếu học phí cùng kỳ. Cần đối soát dữ liệu trước khi thay đổi.");
        if(bills.isEmpty())Db.update(c,"INSERT INTO CONG_NO_HOC_PHI(MaPhieu,MaSV,MaHK,TongTienPhaiDong,SoTienDaDong,TrangThai)VALUES(?,?,?,?,0,?)","HP_"+student+"_"+term,student,term,total,total.signum()==0?"Đã hoàn thành":"Chưa đóng");
        else {var b=bills.get(0);BigDecimal paid=new BigDecimal(Objects.toString(b.get("SoTienDaDong"),"0"));
            String state=paid.compareTo(total)>=0?"Đã hoàn thành":paid.signum()>0?"Còn nợ":"Chưa đóng";
            Db.update(c,"UPDATE CONG_NO_HOC_PHI SET TongTienPhaiDong=?,TrangThai=? WHERE MaPhieu=?",total,state,Db.str(b,"MaPhieu"));}
    }
    public String recalculate(String student,String term)throws Exception{
        Session.requireAdmin();return Db.transaction(c->{Db.lock(c,"ACADEMIC");reconcileTuition(c,student,term);SchemaService.audit(c,"TÍNH LẠI HỌC PHÍ",student+" / "+term);return "Đã cập nhật học phí.";});
    }
}
