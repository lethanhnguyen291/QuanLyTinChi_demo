package service;
import utils.*;
import java.time.*;
import java.util.*;
public final class ScheduleService{
    public record Event(LocalDate date,String periods,String subject,String classId,String room,String teacher,String note){}
    public List<Event> week(String student,String term,LocalDate monday)throws Exception{
        List<Event> events=new ArrayList<>();var terms=Db.rows("SELECT NgayBatDau,NgayKetThuc FROM HOC_KY WHERE MaHK=?",term);
        if(terms.isEmpty()||terms.get(0).get("NgayBatDau")==null||terms.get(0).get("NgayKetThuc")==null)throw new IllegalArgumentException("Học kỳ chưa có ngày bắt đầu/kết thúc. Cần cập nhật dữ liệu lịch học.");
        LocalDate begin=((java.sql.Date)terms.get(0).get("NgayBatDau")).toLocalDate(),end=((java.sql.Date)terms.get(0).get("NgayKetThuc")).toLocalDate();
        Set<LocalDate> holidays=new HashSet<>();for(var r:Db.rows("SELECT Ngay FROM NGAY_NGHI WHERE Ngay BETWEEN ? AND ?",java.sql.Date.valueOf(monday),java.sql.Date.valueOf(monday.plusDays(6))))holidays.add(((java.sql.Date)r.get("Ngay")).toLocalDate());
        var classes=Db.rows("SELECT l.*,m.TenMon,g.HoTen FROM KET_QUA_DANG_KY k JOIN LOP_HOC_PHAN l ON l.MaLHP=k.MaLHP JOIN MON_HOC m ON m.MaMon=l.MaMon LEFT JOIN GIANG_VIEN g ON g.MaGV=l.MaGV WHERE k.MaSV=? AND l.MaHK=?",student,term);
        var extras=Db.rows("SELECT b.* FROM BUOI_HOC_NGOAI_LE b JOIN LOP_HOC_PHAN l ON l.MaLHP=b.MaLHP JOIN KET_QUA_DANG_KY k ON k.MaLHP=l.MaLHP WHERE k.MaSV=? AND l.MaHK=? AND b.Ngay BETWEEN ? AND ?",student,term,java.sql.Date.valueOf(monday),java.sql.Date.valueOf(monday.plusDays(6)));
        for(var cls:classes){String id=Db.str(cls,"MaLHP");LocalDate date=monday.plusDays(ScheduleRules.day(Db.str(cls,"Thu"))-2);
            boolean cancelled=extras.stream().anyMatch(x->Db.str(x,"MaLHP").equals(id)&&((java.sql.Date)x.get("Ngay")).toLocalDate().equals(date)&&ScheduleRules.normalize(Db.str(x,"LoaiBuoi")).equals("huy"));
            if(!date.isBefore(begin)&&!date.isAfter(end)&&!holidays.contains(date)&&!cancelled)events.add(new Event(date,Db.str(cls,"TietHoc"),Db.str(cls,"TenMon"),id,Db.str(cls,"PhongHoc"),Db.str(cls,"HoTen"),""));
            for(var x:extras)if(Db.str(x,"MaLHP").equals(id)&&ScheduleRules.normalize(Db.str(x,"LoaiBuoi")).equals("bu"))events.add(new Event(((java.sql.Date)x.get("Ngay")).toLocalDate(),Db.str(x,"TietHoc").isBlank()?Db.str(cls,"TietHoc"):Db.str(x,"TietHoc"),Db.str(cls,"TenMon"),id,Db.str(x,"PhongHoc").isBlank()?Db.str(cls,"PhongHoc"):Db.str(x,"PhongHoc"),Db.str(cls,"HoTen"),"Học bù · "+Db.str(x,"GhiChu")));
        }events.sort(Comparator.comparing(Event::date).thenComparing(e->ScheduleRules.periods(e.periods()).nextSetBit(1)));return events;
    }
}
