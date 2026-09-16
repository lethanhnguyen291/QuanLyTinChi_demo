import config.DBConnect;
import service.*;
import utils.*;
import java.util.*;
public class AccountProvisionTests {
    static int passed;
    static void check(boolean value,String name){if(!value)throw new AssertionError(name);passed++;System.out.println("PASS "+name);}
    interface Action{void run()throws Exception;}
    static void rejects(Action action,String name)throws Exception{try{action.run();throw new AssertionError("Expected rejection: "+name);}catch(AssertionError e){throw e;}catch(Exception expected){check(true,name);}}
    static void sql(String text,Object...args)throws Exception{try(var c=DBConnect.getConnection()){Db.update(c,text,args);}}
    static int count(String text)throws Exception{try(var c=DBConnect.getConnection()){return Db.count(c,text);}}
    static Object value(String col,String id)throws Exception{try(var c=DBConnect.getConnection()){return Db.scalar(c,"SELECT "+col+" FROM QLTC_TAI_KHOAN WHERE TenDangNhap=?",id);}}
    static void admin(){Session.start("QA_ACCOUNT_BATCH","ADMIN");}
    public static void main(String[] args)throws Exception{
        try(var c=DBConnect.getConnection()){if(!"QuanLyTinChi_NangCap_20260914".equals(c.getCatalog()))throw new IllegalStateException("Only run on the isolated upgraded database.");}
        if(count("SELECT COUNT(*) FROM SINH_VIEN WHERE MaSV IN('QA_ACC1','QA_ACC2','QA_ACC3','QA_ACC4','QA_ACC5')")!=0
            ||count("SELECT COUNT(*) FROM QLTC_TAI_KHOAN WHERE TenDangNhap IN('QA_ACC1','QA_ACC2','QA_ACC3','QA_ACC4','QA_ACC5')")!=0
            ||count("SELECT COUNT(*) FROM sys.triggers WHERE name='QLTC_QA_ACCOUNT_ROLLBACK'")!=0)throw new IllegalStateException("Reserved QA fixtures exist; refusing to overwrite.");
        int studentsBefore=count("SELECT COUNT(*) FROM SINH_VIEN"),accountsBefore=count("SELECT COUNT(*) FROM QLTC_TAI_KHOAN");
        boolean trigger=false;
        try{
            for(int i=1;i<=5;i++)sql("INSERT INTO SINH_VIEN(MaSV,HoTen,TrangThaiHocTap)VALUES(?,?,N'Đang học')","QA_ACC"+i,"Kiểm thử cấp tài khoản "+i);
            AccountService service=new AccountService();char[] initial="QA temporary 2026!".toCharArray();
            Session.start("QA_ACC1","STUDENT");rejects(()->service.provisionMissingStudents(List.of("QA_ACC1"),initial),"Students cannot batch-create accounts");
            admin();rejects(()->service.provisionMissingStudents(List.of("QA_ACC1"),"short".toCharArray()),"Short temporary password rejected");
            var result=service.provisionMissingStudents(List.of("qa_acc1","QA_ACC1","QA_ACC2"),initial);check(result.created()==2&&result.skipped()==0,"Create missing accounts once despite duplicate IDs");
            check(!value("MatKhauHash","QA_ACC1").equals(value("MatKhauHash","QA_ACC2")),"Separate salt for each student's password hash");
            check(service.login("QA_ACC1",initial,"STUDENT").mustChange()&&service.login("QA_ACC2",initial,"STUDENT").mustChange(),"New students can log in and must change temporary password");
            Session.start("QA_ACC1","STUDENT");service.changePassword(initial,"QA changed password!".toCharArray());Object originalHash=value("MatKhauHash","QA_ACC1");
            admin();service.setLocked("QA_ACC1",true);
            var again=service.provisionMissingStudents(List.of("QA_ACC1","QA_ACC2"),"QA replacement ignored!".toCharArray());check(again.created()==0&&again.skipped()==2,"Repeat batch skips existing accounts");
            check(originalHash.equals(value("MatKhauHash","QA_ACC1"))&&Boolean.TRUE.equals(value("Khoa","QA_ACC1"))&&!Boolean.TRUE.equals(value("BatBuocDoi","QA_ACC1")),"Existing password, lock and first-login state are preserved");
            var mixed=service.provisionMissingStudents(List.of("QA_ACC1","QA_ACC2","QA_ACC3"),initial);check(mixed.created()==1&&mixed.skipped()==2,"Mixed batch only creates the missing account");
            rejects(()->service.provisionMissingStudents(List.of("QA_ACC4","QA_MISSING_STUDENT"),initial),"Unknown student rejects batch before writes");
            check(count("SELECT COUNT(*) FROM QLTC_TAI_KHOAN WHERE TenDangNhap='QA_ACC4'")==0,"Rejected batch leaves no partial account");
            sql("CREATE TRIGGER dbo.QLTC_QA_ACCOUNT_ROLLBACK ON dbo.QLTC_TAI_KHOAN AFTER INSERT AS BEGIN SET NOCOUNT ON; IF EXISTS(SELECT 1 FROM inserted WHERE TenDangNhap='QA_ACC5') THROW 51000,'Forced QA account failure',1; END");trigger=true;
            rejects(()->service.provisionMissingStudents(List.of("QA_ACC4","QA_ACC5"),initial),"SQL failure aborts batch");
            check(count("SELECT COUNT(*) FROM QLTC_TAI_KHOAN WHERE TenDangNhap IN('QA_ACC4','QA_ACC5')")==0,"Whole account batch rolls back on SQL failure");
            check(count("SELECT COUNT(*) FROM QLTC_NHAT_KY WHERE NguoiDung='QA_ACCOUNT_BATCH' AND HanhDong=N'CẤP TÀI KHOẢN SINH VIÊN'")==3,"Only committed account creations enter audit log");
        }finally{
            if(trigger)sql("DROP TRIGGER dbo.QLTC_QA_ACCOUNT_ROLLBACK");
            sql("DELETE FROM QLTC_NHAT_KY WHERE NguoiDung IN('QA_ACCOUNT_BATCH','QA_ACC1')");
            sql("DELETE FROM QLTC_TAI_KHOAN WHERE TenDangNhap IN('QA_ACC1','QA_ACC2','QA_ACC3','QA_ACC4','QA_ACC5')");
            sql("DELETE FROM SINH_VIEN WHERE MaSV IN('QA_ACC1','QA_ACC2','QA_ACC3','QA_ACC4','QA_ACC5')");Session.clear();
        }
        check(count("SELECT COUNT(*) FROM SINH_VIEN")==studentsBefore&&count("SELECT COUNT(*) FROM QLTC_TAI_KHOAN")==accountsBefore,"Test fixtures cleaned; existing data and accounts retained");
        System.out.println("ACCOUNT PROVISION TESTS: "+passed+" passed");
    }
}
