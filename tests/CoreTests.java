import service.*;
import utils.*;
import java.util.*;
import java.math.*;
public final class CoreTests{
    static int passed;
    static void check(boolean condition,String name){if(!condition)throw new AssertionError(name);passed++;System.out.println("PASS "+name);}
    interface Attempt{void run()throws Exception;}
    static void rejects(Attempt action,String name){try{action.run();throw new AssertionError("Expected rejection: "+name);}catch(AssertionError e){throw e;}catch(Exception expected){passed++;System.out.println("PASS "+name);}}
    static Map<String,Object> grade(String subject,int credits,Object score){Map<String,Object> m=new HashMap<>();m.put("MaMon",subject);m.put("SoTinChi",credits);m.put("DiemTongKet",score);return m;}
    public static void main(String[] args)throws Exception{
        check(ScheduleRules.day("Chủ Nhật")==8&&ScheduleRules.day("Thu 3")==3,"Vietnamese weekday normalization");
        check(ScheduleRules.overlaps("Thứ 2","1-3","2","3-5"),"Boundary period conflict");
        check(!ScheduleRules.overlaps("Thứ 2","1-3","2","4-6"),"Adjacent periods allowed");
        check(!ScheduleRules.overlaps("Thứ 2","1-3","3","1-3"),"Different weekdays allowed");
        check(ScheduleRules.periods("Tiết 1–3,5;7").cardinality()==5,"Period range and list parsing");
        rejects(()->ScheduleRules.periods("4-1"),"Reject reversed range");rejects(()->ScheduleRules.periods("0-2"),"Reject zero period");rejects(()->ScheduleRules.periods("14-16"),"Reject out of range");rejects(()->ScheduleRules.day("Thứ 23"),"Reject ambiguous day");
        check(AcademicService.score(null)==null&&AcademicService.score(0)==0,"Zero differs from ungraded");
        rejects(()->AcademicService.score("NaN"),"Reject NaN grade");rejects(()->AcademicService.score("Infinity"),"Reject infinite grade");rejects(()->AcademicService.score("10.1"),"Reject grade above 10");
        check(AcademicService.total(10,8,6)==7.0,"Weighted grade calculation");
        var s=AcademicService.summarize(List.of(grade("A",3,9),grade("A",3,5),grade("B",1,5),grade("C",2,0),grade("D",5,null)));
        check(s.earned()==4&&s.gradedCredits()==6,"Unique earned credits with repeat attempts");check(Math.abs(s.gpa10()-32.0/6)<0.0001,"Weighted GPA includes failed zero grade");check(Math.abs(s.gpa4()-13.5/6)<0.0001,"Per-course grade point conversion");
        check(AcademicService.point4(3.99)==0&&AcademicService.point4(4)==1&&AcademicService.point4(8.5)==4,"Grade boundaries");
        check(AcademicService.languagePassed("Đạt")&&!AcademicService.languagePassed("Chưa đạt"),"Legacy language qualification");
        check(Ui.csv("Tên, \"A\"\nB").equals("\"Tên, \"\"A\"\"\nB\""),"CSV commas quotes and newlines preserved");check(Ui.csv("=1+1").equals("\"'=1+1\""),"CSV formula escaping");
        rejects(()->PaymentService.validate(new BigDecimal("-1")),"Reject negative payment");rejects(()->PaymentService.validate(BigDecimal.ZERO),"Reject zero payment");rejects(()->PaymentService.validate(new BigDecimal("0.001")),"Reject excessive decimal precision");
        rejects(()->PersonService.birthDate("31/02/2024"),"Reject invalid calendar date");check(PersonService.birthDate("29/02/2024").toString().equals("2024-02-29"),"Valid leap-day birthday");rejects(()->PersonService.contact("","0123456789","a@b.vn"),"Require name");
        char[] pass=" QA mật khẩu 2026 ".toCharArray();String hash=PasswordHash.create(pass),another=PasswordHash.create(pass);check(!hash.equals(another),"Random salt per credential");check(PasswordHash.verify(pass,hash),"Password hash round trip");check(!PasswordHash.verify("QA mật khẩu 2026".toCharArray(),hash),"Password whitespace preserved");check(!PasswordHash.verify(pass,"malformed"),"Reject malformed hash");
        Session.start("SV01","STUDENT");rejects(Session::requireAdmin,"Student cannot invoke staff services");rejects(()->Session.requireStudent("SV02"),"Student cannot act for other students");Session.clear();rejects(Session::user,"Logged out session denied");
        System.out.println("CORE TESTS: "+passed+" passed");
    }
}
