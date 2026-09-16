package service;
import utils.*;
import java.util.*;
public final class ClassService{
    public void save(boolean create,String id,String subject,String term,String teacher,String day,String periods,String room,int capacity)throws Exception{
        Session.requireAdmin();AccountService.identifier(id);ScheduleRules.day(day);ScheduleRules.periods(periods);
        if(room.isBlank()||capacity<1||capacity>1000)throw new IllegalArgumentException("Nhập phòng học và sức chứa từ 1 đến 1.000.");
        Db.transaction(c->{Db.lock(c,"ACADEMIC");var old=Db.rows(c,"SELECT * FROM LOP_HOC_PHAN WITH(UPDLOCK,HOLDLOCK) WHERE MaLHP=?",id);
            if(create&&!old.isEmpty())throw new IllegalArgumentException("Mã lớp đã tồn tại.");if(!create&&old.size()!=1)throw new IllegalArgumentException("Lớp không còn tồn tại.");
            if(Db.count(c,"SELECT COUNT(*) FROM MON_HOC WHERE MaMon=?",subject)!=1||Db.count(c,"SELECT COUNT(*) FROM GIANG_VIEN WHERE MaGV=?",teacher)!=1||Db.count(c,"SELECT COUNT(*) FROM HOC_KY WHERE MaHK=?",term)!=1)throw new IllegalArgumentException("Chọn môn học, giảng viên và học kỳ có trong dữ liệu.");
            int enrolled=Db.count(c,"SELECT COUNT(*) FROM KET_QUA_DANG_KY WHERE MaLHP=?",id);
            if(enrolled>capacity)throw new IllegalArgumentException("Sức chứa không được thấp hơn sĩ số hiện tại: "+enrolled);
            if(!create&&enrolled>0&&(!subject.equalsIgnoreCase(Db.str(old.get(0),"MaMon"))||!term.equalsIgnoreCase(Db.str(old.get(0),"MaHK"))))throw new IllegalArgumentException("Lớp đã có sinh viên; không đổi môn hoặc học kỳ.");
            for(var r:Db.rows(c,"SELECT * FROM LOP_HOC_PHAN WHERE MaHK=? AND MaLHP<>? AND (PhongHoc=? OR MaGV=?)",term,id,room,teacher))
                if(ScheduleRules.overlaps(day,periods,Db.str(r,"Thu"),Db.str(r,"TietHoc")))throw new IllegalArgumentException("Trùng phòng/giảng viên với lớp "+Db.str(r,"MaLHP"));
            if(enrolled>0)for(var r:Db.rows(c,"SELECT DISTINCT l.MaLHP,l.Thu,l.TietHoc FROM KET_QUA_DANG_KY a JOIN KET_QUA_DANG_KY b ON a.MaSV=b.MaSV JOIN LOP_HOC_PHAN l ON l.MaLHP=b.MaLHP WHERE a.MaLHP=? AND l.MaLHP<>? AND l.MaHK=?",id,id,term))
                if(ScheduleRules.overlaps(day,periods,Db.str(r,"Thu"),Db.str(r,"TietHoc")))throw new IllegalArgumentException("Lịch mới trùng lớp "+Db.str(r,"MaLHP")+" của sinh viên đã đăng ký.");
            if(create)Db.update(c,"INSERT INTO LOP_HOC_PHAN(MaLHP,MaMon,MaHK,MaGV,Thu,TietHoc,PhongHoc,SucChua)VALUES(?,?,?,?,?,?,?,?)",id,subject,term,teacher,day,periods,room,capacity);
            else Db.update(c,"UPDATE LOP_HOC_PHAN SET MaMon=?,MaHK=?,MaGV=?,Thu=?,TietHoc=?,PhongHoc=?,SucChua=? WHERE MaLHP=?",subject,term,teacher,day,periods,room,capacity,id);
            SchemaService.audit(c,create?"MỞ LỚP HỌC PHẦN":"SỬA LỚP HỌC PHẦN",id+" / "+day+" / "+periods+" / "+room);return null;});
    }
    public void delete(String id)throws Exception{
        Session.requireAdmin();Db.transaction(c->{Db.lock(c,"ACADEMIC");
            if(Db.count(c,"SELECT COUNT(*) FROM KET_QUA_DANG_KY WHERE MaLHP=?",id)>0)throw new IllegalArgumentException("Lớp đã có đăng ký. Không thể xóa lịch sử học tập.");
            if(Db.update(c,"DELETE FROM LOP_HOC_PHAN WHERE MaLHP=?",id)!=1)throw new IllegalArgumentException("Lớp không tồn tại.");SchemaService.audit(c,"XÓA LỚP TRỐNG",id);return null;});
    }
}
