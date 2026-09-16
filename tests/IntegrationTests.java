import config.DBConnect;
import service.*;
import utils.*;
import java.sql.*;
import java.math.*;
import java.nio.file.*;
import java.util.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/** Runs only on the named, isolated copy; test rows are removed in finally. */
public class IntegrationTests{
    static int passed;static final String TERM="QA_HK",STUDENT="QA_S1";static String cfg;
    interface Attempt{void run()throws Exception;}
    static void check(boolean condition,String name){if(!condition)throw new AssertionError(name);passed++;System.out.println("PASS "+name);}
    static void rejects(Attempt action,String name)throws Exception{try{action.run();throw new AssertionError("Expected rejection: "+name);}catch(AssertionError e){throw e;}catch(Exception expected){passed++;System.out.println("PASS "+name+" ["+expected.getClass().getSimpleName()+"]");}}
    static void sql(String sql,Object... args)throws Exception{try(var c=DBConnect.getConnection()){Db.update(c,sql,args);}}
    static int count(String sql,Object... args)throws Exception{try(var c=DBConnect.getConnection()){return Db.count(c,sql,args);}}
    static BigDecimal amount(String column)throws Exception{try(var c=DBConnect.getConnection()){return new BigDecimal(Db.scalar(c,"SELECT "+column+" FROM CONG_NO_HOC_PHI WHERE MaSV=? AND MaHK=?",STUDENT,TERM).toString());}}
    static void admin(){Session.start("QA_ADMIN","ADMIN");}static void student(){Session.start(STUDENT,"STUDENT");}
    static void setup()throws Exception{
        if(count("SELECT COUNT(*) FROM SINH_VIEN WHERE MaSV LIKE 'QA[_]%'")>0)throw new IllegalStateException("QA fixture rows already exist; refusing to overwrite.");
        sql("INSERT INTO CHUONG_TRINH_DAO_TAO(MaCTDT,TenCTDT,TongTinChiYeuCau)VALUES('QA_CT',N'Kiểm thử',12)");
        sql("INSERT INTO GIANG_VIEN(MaGV,HoTen)VALUES('QA_GV1',N'Giảng viên kiểm thử 1'),('QA_GV2',N'Giảng viên kiểm thử 2')");
        sql("INSERT INTO SINH_VIEN(MaSV,HoTen,TrangThaiHocTap,DatChuanNgoaiNgu,MaCTDT)VALUES('QA_S1',N'Sinh viên kiểm thử 1',N'Đang học','1','QA_CT'),('QA_S2',N'Sinh viên kiểm thử 2',N'Đang học','0','QA_CT')");
        sql("INSERT INTO HOC_KY(MaHK,TenHK,NamHoc,NgayBatDau,NgayKetThuc)VALUES('QA_HK',N'Kỳ kiểm thử','2098-2099','2026-01-01','2026-12-31'),('QA_OLD',N'Kỳ cũ kiểm thử','2097-2098','2025-01-01','2025-12-31')");
        sql("INSERT INTO QLTC_CAU_HINH_HK(MaHK,MoDangKy,BatDau,KetThuc,DonGia,TinChiToiDa)VALUES('QA_HK',1,DATEADD(day,-1,GETDATE()),DATEADD(day,1,GETDATE()),450000,24)");
        for(var entry:Map.of("QA_A",3,"QA_B",4,"QA_C",2,"QA_P1",2,"QA_P2",2,"QA_D",3,"QA_BIG",30,"QA_R",2).entrySet())sql("INSERT INTO MON_HOC(MaMon,TenMon,SoTinChi)VALUES(?,?,?)",entry.getKey(),"Môn kiểm thử "+entry.getKey(),entry.getValue());
        cls("QA_A1","QA_A","Thứ 2","1-3",10);cls("QA_A2","QA_A","Thứ 4","1-3",10);cls("QA_B1","QA_B","Thứ 2","3-5",10);cls("QA_C1","QA_C","Thứ 3","1-3",10);cls("QA_D1","QA_D","Thứ 5","1-3",10);cls("QA_FULL","QA_P2","Thứ 6","1-3",1);cls("QA_BIG1","QA_BIG","Thứ 6","4-6",10);cls("QA_RACE","QA_R","Chủ Nhật","13-15",1);
        sql("INSERT INTO LOP_HOC_PHAN(MaLHP,MaMon,MaHK,MaGV,Thu,TietHoc,PhongHoc,SucChua)VALUES('QA_POLD','QA_P1','QA_OLD','QA_GV1',N'Thứ 2','1-3','QA_P',10)");
        sql("INSERT INTO MON_TIEN_QUYET(MaMon,MaMonTQ)VALUES('QA_D','QA_P1'),('QA_D','QA_P2')");
        sql("INSERT INTO KET_QUA_DANG_KY(MaSV,MaLHP,DiemTongKet,TrangThai)VALUES('QA_S1','QA_POLD','6',N'Đạt'),('QA_S2','QA_FULL',NULL,N'Chưa có điểm')");
    }
    static void cls(String id,String subject,String day,String periods,int capacity)throws Exception{sql("INSERT INTO LOP_HOC_PHAN(MaLHP,MaMon,MaHK,MaGV,Thu,TietHoc,PhongHoc,SucChua)VALUES(?,?,'QA_HK','QA_GV1',?,?,?,?)",id,subject,day,periods,"ROOM_"+id,capacity);}
    public static void cleanup()throws Exception{
        for(String statement:new String[]{"DELETE FROM QLTC_PHIEU_THU WHERE NguoiThu='QA_ADMIN'","DELETE FROM QLTC_NHAT_KY WHERE NguoiDung LIKE 'QA[_]%'","DELETE FROM QLTC_TAI_KHOAN WHERE TenDangNhap LIKE 'QA[_]S%' OR TenDangNhap LIKE 'QA[_]IMP%'","DELETE FROM BUOI_HOC_NGOAI_LE WHERE MaLHP LIKE 'QA[_]%'","DELETE FROM KET_QUA_DANG_KY WHERE MaSV LIKE 'QA[_]%' OR MaLHP LIKE 'QA[_]%'","DELETE FROM CONG_NO_HOC_PHI WHERE MaSV LIKE 'QA[_]%'","DELETE FROM LOP_HOC_PHAN WHERE MaLHP LIKE 'QA[_]%'","DELETE FROM MON_TIEN_QUYET WHERE MaMon LIKE 'QA[_]%' OR MaMonTQ LIKE 'QA[_]%'","DELETE FROM MON_HOC WHERE MaMon LIKE 'QA[_]%'","DELETE FROM QLTC_CAU_HINH_HK WHERE MaHK LIKE 'QA[_]%'","DELETE FROM HOC_KY WHERE MaHK LIKE 'QA[_]%'","DELETE FROM SINH_VIEN WHERE MaSV LIKE 'QA[_]%'","DELETE FROM GIANG_VIEN WHERE MaGV LIKE 'QA[_]%'","DELETE FROM CHUONG_TRINH_DAO_TAO WHERE MaCTDT='QA_CT'"})sql(statement);
    }
    public static void main(String[] args)throws Exception{
        cfg=System.getProperty("qltc.config");try(var c=DBConnect.getConnection()){if(!c.getCatalog().equals("QuanLyTinChi_NangCap_20260914"))throw new IllegalStateException("Requires isolated QA database.");}
        if(args.length>0&&args[0].equals("race")){Session.start(args[1],"STUDENT");try{new RegistrationService().register(args[1],"QA_RACE");System.out.println("REGISTERED");}catch(Exception e){System.out.println("REJECTED: "+e.getMessage());}return;}
        if(count("SELECT COUNT(*) FROM SINH_VIEN WHERE MaSV LIKE 'QA[_]%'")>0)throw new IllegalStateException("QA fixture rows already exist; refusing to overwrite.");
        boolean createdAdmin=count("SELECT COUNT(*) FROM QLTC_TAI_KHOAN WHERE VaiTro='ADMIN'")==0;
        if(createdAdmin)new AccountService().bootstrap("QA_ADMIN","Qa_Test_2026!".toCharArray());
        try{setup();
            AccountService accounts=new AccountService();RegistrationService registration=new RegistrationService();admin();
            rejects(()->accounts.bootstrap("QA_OTHER","Any_Password8".toCharArray()),"Bootstrap cannot create second administrator");
            accounts.provisionStudent(STUDENT,"Temporary_2026!".toCharArray());var login=accounts.login("qa_s1","Temporary_2026!".toCharArray(),"STUDENT");check(login.mustChange(),"Provisioned account requires password change");
            student();accounts.changePassword("Temporary_2026!".toCharArray(),"New_Password_2026!".toCharArray());check(!accounts.login(STUDENT,"New_Password_2026!".toCharArray(),"STUDENT").mustChange(),"Password change persisted");
            for(int i=0;i<5;i++)rejects(()->accounts.login(STUDENT,"wrong".toCharArray(),"STUDENT"),"Incorrect password rejected "+(i+1));
            rejects(()->accounts.login(STUDENT,"New_Password_2026!".toCharArray(),"STUDENT"),"Lockout after five failed attempts");admin();accounts.setLocked(STUDENT,false);check(accounts.login(STUDENT,"New_Password_2026!".toCharArray(),"STUDENT")!=null,"Staff unlock restores login");
            student();registration.register(STUDENT,"QA_A1");check(count("SELECT COUNT(*) FROM KET_QUA_DANG_KY WHERE MaSV=? AND MaLHP='QA_A1'",STUDENT)==1,"Registration persisted");check(amount("TongTienPhaiDong").compareTo(new BigDecimal("1350000"))==0,"Tuition generated with registration");
            rejects(()->registration.register(STUDENT,"QA_A1"),"Duplicate registration rejected");rejects(()->registration.register(STUDENT,"QA_A2"),"Second section of same course rejected");rejects(()->registration.register(STUDENT,"QA_B1"),"Overlapping schedule rejected");rejects(()->registration.register(STUDENT,"QA_D1"),"All prerequisites required");rejects(()->registration.register(STUDENT,"QA_FULL"),"Full class rejected");rejects(()->registration.register(STUDENT,"QA_BIG1"),"Credit maximum enforced");
            sql("UPDATE QLTC_CAU_HINH_HK SET MoDangKy=0 WHERE MaHK='QA_HK'");rejects(()->registration.cancel(STUDENT,"QA_A1"),"Closed registration period enforced for cancellation");sql("UPDATE QLTC_CAU_HINH_HK SET MoDangKy=1 WHERE MaHK='QA_HK'");
            sql("INSERT INTO CONG_NO_HOC_PHI(MaPhieu,MaSV,MaHK,TongTienPhaiDong,SoTienDaDong)VALUES('QA_DUP','QA_S1','QA_HK',0,'0')");rejects(()->registration.register(STUDENT,"QA_C1"),"Tuition failure aborts registration");check(count("SELECT COUNT(*) FROM KET_QUA_DANG_KY WHERE MaSV='QA_S1' AND MaLHP='QA_C1'")==0,"Failed transaction leaves no enrollment");sql("DELETE FROM CONG_NO_HOC_PHI WHERE MaPhieu='QA_DUP'");
            PaymentService payment=new PaymentService();String invoice="HP_QA_S1_QA_HK",request=UUID.randomUUID().toString();rejects(()->payment.collect(invoice,new BigDecimal("100"),"test",request),"Student cannot record payment");admin();
            payment.collect(invoice,new BigDecimal("100000"),"QA partial",request);payment.collect(invoice,new BigDecimal("100000"),"QA retry",request);check(amount("SoTienDaDong").compareTo(new BigDecimal("100000"))==0,"Payment retry cannot double charge");
            rejects(()->payment.collect(invoice,new BigDecimal("2000000"),"QA excessive",UUID.randomUUID().toString()),"Overpayment rejected");payment.collect(invoice,new BigDecimal("1250000"),"QA balance",UUID.randomUUID().toString());check(amount("SoTienDaDong").compareTo(new BigDecimal("1350000"))==0,"Partial payments add numerically in legacy text column");
            student();registration.cancel(STUDENT,"QA_A1");check(amount("TongTienPhaiDong").signum()==0&&amount("SoTienDaDong").compareTo(new BigDecimal("1350000"))==0,"Cancellation preserves paid credit balance");registration.register(STUDENT,"QA_C1");
            admin();var grade=new HashMap<String,Object>();grade.put("MaSV",STUDENT);grade.put("DiemChuyenCan",0);grade.put("DiemGiuaKy",0);grade.put("DiemCuoiKy",0);new AcademicService().saveGrades("QA_C1",List.of(grade));
            student();rejects(()->registration.cancel(STUDENT,"QA_C1"),"Zero grade blocks cancellation");admin();rejects(()->new AcademicService().saveGrades("QA_C1",List.of(grade)),"Concurrent grade change detected");
            check(AcademicService.requiredCredits(STUDENT)==12,"Graduation threshold from actual degree program");
            rejects(()->new PersonService().execute("DELETE FROM SINH_VIEN WHERE MaSV=?","QA delete",new Object[]{STUDENT}),"Cannot cascade-delete a student with history");
            rejects(()->new ClassService().save(true,"QA_COLLISION","QA_A",TERM,"QA_GV1","Thứ 2","2-4","QA_NEW",20),"Teacher schedule conflict rejected");rejects(()->new ClassService().delete("QA_C1"),"Cannot delete enrolled class");
            // Separate JVMs simulate two independent signed-in desktop clients.
            String javaExe=Path.of(System.getProperty("java.home"),"bin","java.exe").toString(),cp=System.getProperty("java.class.path");
            Process one=new ProcessBuilder(javaExe,"-Dqltc.config="+cfg,"-cp",cp,"IntegrationTests","race","QA_S1").redirectErrorStream(true).start();
            Process two=new ProcessBuilder(javaExe,"-Dqltc.config="+cfg,"-cp",cp,"IntegrationTests","race","QA_S2").redirectErrorStream(true).start();
            String output1=new String(one.getInputStream().readAllBytes()),output2=new String(two.getInputStream().readAllBytes());check(one.waitFor()==0&&two.waitFor()==0,"Concurrent clients completed");
            check(count("SELECT COUNT(*) FROM KET_QUA_DANG_KY WHERE MaLHP='QA_RACE'")==1,"Last seat sold once across two processes");check(output1.contains("REGISTERED")^output2.contains("REGISTERED"),"Exactly one concurrent registration succeeds");
            Path excel=Files.createTempFile("qltc-import-", ".xlsx");try(var wb=new XSSFWorkbook()){var sh=wb.createSheet();var h=sh.createRow(0);String[] headers=ImportService.STUDENT;for(int i=0;i<headers.length;i++)h.createCell(i).setCellValue(headers[i]);
                String[][] values={{"QA_IMP1","Tên kiểm thử","Nam","29/02/2004","0123456789","test@example.test","Đang học","QA_CT","QA"},{"QA_IMP1","Bị trùng","Nam","2004-02-29","0123456789","test@example.test","Đang học","QA_CT","QA"},{"QA_IMP2","Ngày sai","Nam","31/02/2004","0123456789","test@example.test","Đang học","QA_CT","QA"}};
                for(int r=0;r<values.length;r++){var row=sh.createRow(r+1);for(int col=0;col<headers.length;col++)row.createCell(col).setCellValue(values[r][col]);}try(var out=Files.newOutputStream(excel)){wb.write(out);}}
            ImportService imp=new ImportService();var preview=imp.preview(excel,true);check(preview.rows().size()==3&&preview.valid()==1,"Import preview flags duplicate and invalid date");imp.commit(preview);check(count("SELECT COUNT(*) FROM SINH_VIEN WHERE MaSV='QA_IMP1'")==1,"Import commits valid rows only");check(imp.preview(excel,true).valid()==0,"Existing IDs not counted as successful imports");Files.delete(excel);
            student();var events=new ScheduleService().week(STUDENT,TERM,java.time.LocalDate.of(2026,9,14));check(events.stream().anyMatch(e->e.classId().equals("QA_C1")),"Weekly schedule uses actual registrations");
            check(count("SELECT COUNT(*) FROM QLTC_NHAT_KY WHERE NguoiDung LIKE 'QA[_]%'")>10,"Mutations write audit entries");
            System.out.println("INTEGRATION TESTS: "+passed+" passed");
        }finally{admin();cleanup();if(createdAdmin)sql("DELETE FROM QLTC_TAI_KHOAN WHERE TenDangNhap='QA_ADMIN'");Session.clear();}
    }
}
