package service;
import utils.*;
import config.DBConnect;
import java.sql.*;
import java.math.*;
import java.util.*;
public final class AcademicService {
    public record Summary(int earned,int gradedCredits,double gpa10,double gpa4){}
    public static Double score(Object value){
        if(value==null||value.toString().isBlank())return null;
        double n=Double.parseDouble(value.toString().trim().replace(',','.'));
        if(!Double.isFinite(n)||n<0||n>10)throw new IllegalArgumentException("Điểm phải nằm trong 0–10.");return n;
    }
    public static double point4(double s){return s>=8.5?4:s>=8?3.5:s>=7?3:s>=6.5?2.5:s>=5.5?2:s>=5?1.5:s>=4?1:0;}
    public static double total(double cc,double gk,double ck){return BigDecimal.valueOf(cc).multiply(new BigDecimal("0.1")).add(BigDecimal.valueOf(gk).multiply(new BigDecimal("0.3"))).add(BigDecimal.valueOf(ck).multiply(new BigDecimal("0.6"))).setScale(2,RoundingMode.HALF_UP).doubleValue();}
    public static Summary summarize(List<Map<String,Object>> rows){
        Map<String,Map<String,Object>> best=new LinkedHashMap<>();
        for(var r:rows){Double v=score(r.get("DiemTongKet"));if(v==null)continue;String key=Db.str(r,"MaMon");
            if(!best.containsKey(key)||v>score(best.get(key).get("DiemTongKet")))best.put(key,r);}
        int earned=0,credits=0;double weighted10=0,weighted4=0;
        for(var r:best.values()){int tc=Integer.parseInt(Db.str(r,"SoTinChi"));double s=score(r.get("DiemTongKet"));
            if(tc<=0)throw new IllegalArgumentException("Số tín chỉ không hợp lệ: "+Db.str(r,"MaMon"));
            credits+=tc;weighted10+=tc*s;weighted4+=tc*point4(s);if(s>=4)earned+=tc;}
        return new Summary(earned,credits,credits==0?0:weighted10/credits,credits==0?0:weighted4/credits);
    }
    public static Summary summary(String student,String term)throws SQLException{
        return summarize(Db.rows("SELECT l.MaMon,m.SoTinChi,k.DiemTongKet FROM KET_QUA_DANG_KY k JOIN LOP_HOC_PHAN l ON l.MaLHP=k.MaLHP JOIN MON_HOC m ON m.MaMon=l.MaMon WHERE k.MaSV=?"+(term==null?"":" AND l.MaHK=?"),term==null?new Object[]{student}:new Object[]{student,term}));
    }
    public static int requiredCredits(String student)throws SQLException{
        try(Connection c=DBConnect.getConnection()){
            Object v=Db.scalar(c,"SELECT p.TongTinChiYeuCau FROM SINH_VIEN s LEFT JOIN CHUONG_TRINH_DAO_TAO p ON p.MaCTDT=s.MaCTDT WHERE s.MaSV=?",student);
            if(v==null||v.toString().isBlank())throw new IllegalArgumentException("Chưa có số tín chỉ yêu cầu của chương trình đào tạo.");
            int n=Integer.parseInt(v.toString());if(n<=0)throw new IllegalArgumentException("Số tín chỉ yêu cầu không hợp lệ.");return n;
        }
    }
    public static boolean languagePassed(Object value){String s=ScheduleRules.normalize(Objects.toString(value,""));return Set.of("1","true","dat","da dat","co").contains(s);}
    public void saveGrades(String classId,List<Map<String,Object>> grades)throws Exception{
        Session.requireAdmin();
        for(var r:grades){Double cc=score(r.get("DiemChuyenCan")),gk=score(r.get("DiemGiuaKy")),ck=score(r.get("DiemCuoiKy"));
            if(cc==null||gk==null||ck==null)throw new IllegalArgumentException("Nhập đủ 3 điểm cho sinh viên "+Db.str(r,"MaSV")+". Chỉ lưu những dòng đã sửa.");}
        Db.transaction(c->{Db.lock(c,"ACADEMIC");
            for(var r:grades){String sv=Db.str(r,"MaSV");var actual=Db.rows(c,"SELECT DiemChuyenCan,DiemGiuaKy,DiemCuoiKy FROM KET_QUA_DANG_KY WITH(UPDLOCK,HOLDLOCK) WHERE MaSV=? AND MaLHP=?",sv,classId);
                if(actual.size()!=1)throw new IllegalArgumentException("Đăng ký đã thay đổi. Hãy tải lại danh sách.");
                for(String key:List.of("DiemChuyenCan","DiemGiuaKy","DiemCuoiKy"))
                    if(!Objects.equals(score(actual.get(0).get(key)),score(r.get("old_"+key))))throw new IllegalArgumentException("Điểm của "+sv+" vừa được sửa ở phiên khác. Hãy tải lại.");
                double cc=score(r.get("DiemChuyenCan")),gk=score(r.get("DiemGiuaKy")),ck=score(r.get("DiemCuoiKy")),tk=total(cc,gk,ck);
                if(Db.update(c,"UPDATE KET_QUA_DANG_KY SET DiemChuyenCan=?,DiemGiuaKy=?,DiemCuoiKy=?,DiemTongKet=?,TrangThai=? WHERE MaSV=? AND MaLHP=?",cc,gk,ck,tk,tk>=4?"Đạt":"Không đạt",sv,classId)!=1)throw new SQLException("Không lưu được điểm.");
                SchemaService.audit(c,"LƯU ĐIỂM",sv+" / "+classId+" / "+tk);
            }return null;});
    }
}
