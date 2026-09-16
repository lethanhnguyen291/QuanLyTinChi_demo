package service;
import utils.*;
import java.sql.*;
import java.time.*;
import java.time.format.*;
public final class PersonService{
    public static java.sql.Date birthDate(String text){
        if(text==null||text.isBlank())return null;LocalDate date;
        try{date=LocalDate.parse(text.trim());}catch(DateTimeParseException e){date=LocalDate.parse(text.trim(),DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT));}
        if(date.isAfter(LocalDate.now())||date.isBefore(LocalDate.of(1900,1,1)))throw new IllegalArgumentException("Ngày sinh không hợp lệ.");return java.sql.Date.valueOf(date);
    }
    public static void contact(String name,String phone,String email){
        if(name==null||name.isBlank())throw new IllegalArgumentException("Họ tên không được để trống.");
        if(!phone.isBlank()&&!phone.matches("\\+?[0-9]{8,15}"))throw new IllegalArgumentException("Số điện thoại gồm 8–15 chữ số, có thể bắt đầu bằng +.");
        if(!email.isBlank()&&!email.matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+"))throw new IllegalArgumentException("Email chưa đúng định dạng.");
    }
    public void execute(String sql,String action,Object[] params)throws Exception{
        Session.requireAdmin();boolean student=sql.contains("SINH_VIEN"),insert=sql.startsWith("INSERT"),delete=sql.startsWith("DELETE");
        for(int i=0;i<params.length;i++)if(params[i] instanceof String)params[i]=params[i].toString().trim();
        String id=params[insert||delete?0:params.length-1].toString();AccountService.identifier(id);
        if(!delete){int offset=insert?1:0;contact(params[offset].toString(),params[offset+3].toString(),params[offset+4].toString());if(student)params[offset+2]=birthDate(params[offset+2].toString());}
        Db.transaction(c->{Db.lock(c,"ACADEMIC");
            if(delete){
                if(student&&(Db.count(c,"SELECT COUNT(*) FROM KET_QUA_DANG_KY WHERE MaSV=?",id)>0||Db.count(c,"SELECT COUNT(*) FROM CONG_NO_HOC_PHI WHERE MaSV=?",id)>0))throw new IllegalArgumentException("Sinh viên đã có lịch sử đăng ký/công nợ. Hãy đổi trạng thái học tập thay vì xóa.");
                if(!student&&Db.count(c,"SELECT COUNT(*) FROM LOP_HOC_PHAN WHERE MaGV=?",id)>0)throw new IllegalArgumentException("Giảng viên đã được phân công lớp. Hãy phân công lại trước khi xóa.");
                if(student)Db.update(c,"DELETE FROM QLTC_TAI_KHOAN WHERE TenDangNhap=? AND VaiTro='STUDENT'",id);
            }
            if(Db.update(c,sql,params)!=1)throw new IllegalArgumentException("Không có bản ghi được thay đổi. Kiểm tra mã đã chọn.");
            SchemaService.audit(c,action,id);return null;});
    }
}
