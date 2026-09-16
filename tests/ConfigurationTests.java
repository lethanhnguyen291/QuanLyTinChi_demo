import config.*;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.*;
public final class ConfigurationTests {
    static int passed;
    static void check(boolean ok,String label){if(!ok)throw new AssertionError(label);passed++;System.out.println("PASS "+label);}
    static void write(Path path,Properties p)throws Exception{Files.createDirectories(path.getParent());try(var w=Files.newBufferedWriter(path)){p.store(w,"Test fixture");}}
    public static void main(String[] args)throws Exception{
        String oldHome=System.getProperty("qltc.home"),oldConfig=System.getProperty("qltc.config");
        Path root=Files.createTempDirectory("qltc-cau-hinh-");
        try{
            System.setProperty("qltc.home",root.toString());System.clearProperty("qltc.config");
            check(AppPaths.configuration().equals(root.resolve("config/database.properties")),"Configuration belongs to application folder");
            check(DBConnect.settings().getProperty("db.url").contains("databaseName=QuanLyTinChi_NangCap_20260914;"),"Missing config selects upgraded database, never original database");
            check(DBConnect.needsConfiguration(),"Fresh download requests first-run setup");
            try{DBConnect.getConnection();throw new AssertionError("Missing setup must stop before SQL login");}catch(IllegalStateException e){check(e.getCause()==null&&e.getMessage().contains("Chưa thiết lập"),"Missing password gives setup instructions before connecting");}
            Properties p=new Properties();p.setProperty("db.user","THU_NGHIEM");p.setProperty("db.password","");
            write(root.resolve("config/database.properties.example"),p);
            check(DBConnect.settings().getProperty("db.user").equals("THU_NGHIEM"),"GitHub example supplies first-run defaults");
            p.setProperty("db.user","LOCAL");p.setProperty("db.password"," mật khẩu thử ");write(AppPaths.configuration(),p);
            check(DBConnect.settings().getProperty("db.user").equals("LOCAL"),"Local configuration takes precedence over example");
            check(DBConnect.settings().getProperty("db.password").equals(" mật khẩu thử "),"Unicode and password whitespace survive properties round trip");
            check(!DBConnect.needsConfiguration(),"Configured installation does not request setup again");
            System.setProperty("qltc.config","riêng/kết nối.properties");check(AppPaths.configuration().equals(root.resolve("riêng/kết nối.properties")),"Relative override resolves against application directory");
            System.setProperty("qltc.config",root.resolve("absolute.properties").toString());check(AppPaths.configuration().equals(root.resolve("absolute.properties")),"Absolute external configuration is supported");
            check(DBConnect.connectionMessage(new SQLException("Login failure","S0001",18456)).contains("mật khẩu"),"SQL authentication error is actionable");
            check(DBConnect.connectionMessage(new SQLException("Database missing","S0001",4060)).contains("cơ sở dữ liệu"),"Unavailable database has distinct guidance");
            System.out.println("CONFIGURATION TESTS: "+passed+" passed");
        }finally{
            if(oldHome==null)System.clearProperty("qltc.home");else System.setProperty("qltc.home",oldHome);
            if(oldConfig==null)System.clearProperty("qltc.config");else System.setProperty("qltc.config",oldConfig);
            Path safeRoot=root.toRealPath();
            try(var paths=Files.walk(safeRoot)){for(Path p:paths.sorted(Comparator.reverseOrder()).toList()){if(!p.toAbsolutePath().normalize().startsWith(safeRoot))throw new IllegalStateException("Unsafe test cleanup path");Files.delete(p);}}
        }
    }
}
