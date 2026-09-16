package service;
import utils.*;
import java.util.*;
public final class AccountService {
    public record Login(String username,String role,boolean mustChange){}
    public record ProvisionResult(int created,int skipped){}
    /** Only create missing accounts; never reset or unlock an existing account. */
    public ProvisionResult provisionMissingStudents(Collection<String> studentIds,char[] password)throws Exception{
        Session.requireAdmin();PasswordHash.validate(password);
        SortedSet<String> requested=new TreeSet<>();for(String id:studentIds)requested.add(identifier(id));
        if(requested.size()>10000)throw new IllegalArgumentException("Mỗi lần chỉ cấp tối đa 10.000 tài khoản.");
        if(requested.isEmpty())return new ProvisionResult(0,0);
        Set<String> existingStudents=new HashSet<>(),existingAccounts=new HashSet<>();
        for(var row:Db.rows("SELECT s.MaSV,a.TenDangNhap FROM SINH_VIEN s LEFT JOIN QLTC_TAI_KHOAN a ON a.TenDangNhap=s.MaSV")){
            existingStudents.add(identifier(Db.str(row,"MaSV")));if(row.get("TenDangNhap")!=null)existingAccounts.add(identifier(Db.str(row,"TenDangNhap")));
        }
        for(String id:requested)if(!existingStudents.contains(id))throw new IllegalArgumentException("Sinh viên không còn tồn tại: "+id+". Hãy tải lại danh sách.");
        // Hash outside the SQL transaction, using an independent salt per account.
        Map<String,String> hashes=new TreeMap<>();for(String id:requested)if(!existingAccounts.contains(id))hashes.put(id,PasswordHash.create(password));
        return Db.transaction(c->{
            int created=0;
            for(var entry:hashes.entrySet()){
                String id=entry.getKey();Db.lock(c,"ACCOUNT:"+id);
                if(Db.count(c,"SELECT COUNT(*) FROM SINH_VIEN WITH(UPDLOCK,HOLDLOCK) WHERE MaSV=?",id)!=1)throw new IllegalArgumentException("Sinh viên không còn tồn tại: "+id);
                if(Db.count(c,"SELECT COUNT(*) FROM QLTC_TAI_KHOAN WHERE TenDangNhap=?",id)>0)continue;
                Db.update(c,"INSERT INTO QLTC_TAI_KHOAN(TenDangNhap,MatKhauHash,VaiTro,BatBuocDoi)VALUES(?,?,'STUDENT',1)",id,entry.getValue());
                SchemaService.audit(c,"CẤP TÀI KHOẢN SINH VIÊN",id);created++;
            }
            return new ProvisionResult(created,requested.size()-created);
        });
    }
    public void bootstrap(String username,char[] password)throws Exception{
        String user=identifier(username),hash=PasswordHash.create(password);
        Db.transaction(c->{Db.lock(c,"SETUP");SchemaService.install(c);
            if(Db.count(c,"SELECT COUNT(*) FROM QLTC_TAI_KHOAN WHERE VaiTro='ADMIN'")>0)throw new IllegalArgumentException("Đã có quản trị viên. Hãy đăng nhập hoặc nhờ quản trị viên đặt lại mật khẩu.");
            Db.update(c,"INSERT INTO QLTC_TAI_KHOAN(TenDangNhap,MatKhauHash,VaiTro,BatBuocDoi)VALUES(?,?,'ADMIN',0)",user,hash);return null;});
    }
    public Login login(String username,char[] password,String role)throws Exception{
        String user=identifier(username);
        Login result=Db.transaction(c->{Db.lock(c,"ACCOUNT:"+user);
            var rows=Db.rows(c,"SELECT *,CASE WHEN KhoaDen>SYSDATETIME() THEN 1 ELSE 0 END AS TamKhoa FROM QLTC_TAI_KHOAN WITH(UPDLOCK,HOLDLOCK) WHERE TenDangNhap=? AND VaiTro=?",user,role);
            if(rows.isEmpty())return null;var r=rows.get(0);
            if(Boolean.TRUE.equals(r.get("Khoa"))||Db.number(r,"TamKhoa")==1)return null;
            if(!PasswordHash.verify(password,Db.str(r,"MatKhauHash"))){
                Db.update(c,"UPDATE QLTC_TAI_KHOAN SET SoLanSai=SoLanSai+1,KhoaDen=CASE WHEN SoLanSai+1>=5 THEN DATEADD(minute,5,SYSDATETIME()) ELSE NULL END WHERE TenDangNhap=?",user);return null;}
            if("STUDENT".equals(role)&&Db.count(c,"SELECT COUNT(*) FROM SINH_VIEN WHERE MaSV=?",user)!=1)return null;
            Db.update(c,"UPDATE QLTC_TAI_KHOAN SET SoLanSai=0,KhoaDen=NULL WHERE TenDangNhap=?",user);
            return new Login(Db.str(r,"TenDangNhap"),role,Boolean.TRUE.equals(r.get("BatBuocDoi")));
        });
        if(result==null)throw new IllegalArgumentException("Sai tài khoản/mật khẩu hoặc tài khoản đã khóa. Sau 5 lần sai, chờ 5 phút rồi thử lại.");return result;
    }
    public void changePassword(char[] current,char[] replacement)throws Exception{
        String user=Session.user();PasswordHash.validate(replacement);
        if(Arrays.equals(current,replacement))throw new IllegalArgumentException("Mật khẩu mới phải khác mật khẩu hiện tại.");
        String hash=PasswordHash.create(replacement);
        Db.transaction(c->{Db.lock(c,"ACCOUNT:"+user);Object stored=Db.scalar(c,"SELECT MatKhauHash FROM QLTC_TAI_KHOAN WHERE TenDangNhap=?",user);
            if(stored==null||!PasswordHash.verify(current,stored.toString()))throw new IllegalArgumentException("Mật khẩu hiện tại không đúng.");
            Db.update(c,"UPDATE QLTC_TAI_KHOAN SET MatKhauHash=?,BatBuocDoi=0,SoLanSai=0,KhoaDen=NULL WHERE TenDangNhap=?",hash,user);
            SchemaService.audit(c,"ĐỔI MẬT KHẨU",user);return null;});
    }
    public void provisionStudent(String id,char[] password)throws Exception{
        Session.requireAdmin();String user=identifier(id),hash=PasswordHash.create(password);
        Db.transaction(c->{Db.lock(c,"ACCOUNT:"+user);
            if(Db.count(c,"SELECT COUNT(*) FROM SINH_VIEN WHERE MaSV=?",user)!=1)throw new IllegalArgumentException("Mã sinh viên không tồn tại.");
            Object role=Db.scalar(c,"SELECT VaiTro FROM QLTC_TAI_KHOAN WHERE TenDangNhap=?",user);
            if("ADMIN".equals(role))throw new IllegalArgumentException("Không thể thay tài khoản quản trị.");
            if(role==null)Db.update(c,"INSERT INTO QLTC_TAI_KHOAN(TenDangNhap,MatKhauHash,VaiTro)VALUES(?,?,'STUDENT')",user,hash);
            else Db.update(c,"UPDATE QLTC_TAI_KHOAN SET MatKhauHash=?,BatBuocDoi=1,Khoa=0,SoLanSai=0,KhoaDen=NULL WHERE TenDangNhap=?",hash,user);
            SchemaService.audit(c,"CẤP / ĐẶT LẠI TÀI KHOẢN",user);return null;});
    }
    public void setLocked(String id,boolean locked)throws Exception{
        Session.requireAdmin();String user=identifier(id);
        Db.transaction(c->{Db.lock(c,"ACCOUNT:"+user);
            if(Db.update(c,"UPDATE QLTC_TAI_KHOAN SET Khoa=?,SoLanSai=0,KhoaDen=NULL WHERE TenDangNhap=? AND VaiTro='STUDENT'",locked,user)!=1)throw new IllegalArgumentException("Chỉ khóa/mở được tài khoản sinh viên đã cấp.");
            SchemaService.audit(c,locked?"KHÓA TÀI KHOẢN":"MỞ KHÓA TÀI KHOẢN",user);return null;});
    }
    public static String identifier(String text){String v=text==null?"":text.trim().toUpperCase(Locale.ROOT);
        if(!v.matches("[A-Z0-9_.-]{1,100}"))throw new IllegalArgumentException("Mã chỉ gồm chữ, số, chấm, gạch ngang hoặc gạch dưới.");return v;}
}
