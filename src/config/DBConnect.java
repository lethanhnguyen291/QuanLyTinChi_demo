package config;
import java.nio.file.*;
import java.sql.*;
import java.util.*;

public final class DBConnect {
    public static final String DEFAULT_URL="jdbc:sqlserver://localhost:1433;databaseName=QuanLyTinChi_NangCap_20260914;encrypt=true;trustServerCertificate=true";
    private DBConnect(){}
    public static Properties settings(){
        Properties p=new Properties();p.setProperty("db.url",DEFAULT_URL);p.setProperty("db.user","sa");p.setProperty("db.password","");
        Path file=AppPaths.configuration();
        if(!Files.exists(file))file=AppPaths.directory().resolve("config/database.properties.example");
        if(Files.exists(file))try(var reader=Files.newBufferedReader(file)){p.load(reader);}
        catch(Exception e){throw new IllegalStateException("Không đọc được cấu hình kết nối tại: "+file,e);}
        return p;
    }
    public static Properties effectiveSettings(Properties local){
        Properties effective=new Properties();effective.putAll(local);
        for(String name:new String[]{"URL","USER","PASSWORD"}){
            String value=System.getenv("QLTC_DB_"+name);
            if(value!=null)effective.setProperty("db."+name.toLowerCase(Locale.ROOT),value);
        }
        return effective;
    }
    public static boolean needsConfiguration(){return missing(effectiveSettings(settings()));}
    private static boolean missing(Properties p){
        String url=p.getProperty("db.url","");boolean integrated=url.toLowerCase(Locale.ROOT).contains("integratedsecurity=true");
        return url.isBlank()||(!integrated&&(p.getProperty("db.user","").isBlank()||p.getProperty("db.password","").isEmpty()));
    }
    public static Connection getConnection(){return connect(settings());}
    private static Connection connect(Properties local){
        Properties cfg=effectiveSettings(local);
        if(missing(cfg))throw new IllegalStateException("Chưa thiết lập kết nối dữ liệu trên bản ứng dụng này.\nChọn Kết nối dữ liệu và nhập tài khoản, mật khẩu SQL Server của máy bạn.\nĐây là mật khẩu SQL Server, không phải mật khẩu đăng nhập sinh viên.");
        String url=cfg.getProperty("db.url","").trim();
        if(!url.startsWith("jdbc:sqlserver://"))throw new IllegalArgumentException("Địa chỉ kết nối phải bắt đầu bằng jdbc:sqlserver://.");
        Properties props=new Properties();props.setProperty("user",cfg.getProperty("db.user",""));props.setProperty("password",cfg.getProperty("db.password",""));
        props.setProperty("loginTimeout","5");props.setProperty("socketTimeout","15000");
        try{Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");return DriverManager.getConnection(url,props);}
        catch(ClassNotFoundException e){throw new IllegalStateException("Thiếu thư viện SQL Server. Giải nén toàn bộ gói và giữ thư mục lib cạnh QuanLyTinChi.jar.",e);}
        catch(SQLException e){throw new IllegalStateException(connectionMessage(e),e);}
    }
    public static String connectionMessage(SQLException error){
        for(SQLException e=error;e!=null;e=e.getNextException()){
            if(e.getErrorCode()==4060)return "Không mở được cơ sở dữ liệu đã chọn. Kiểm tra tên CSDL, quyền truy cập và khôi phục các tệp SQL trong thư mục database nếu dùng máy mới.";
            if(e.getErrorCode()==18456||e.getErrorCode()==18452)return "SQL Server từ chối tài khoản hoặc mật khẩu kết nối. Chọn Kết nối dữ liệu để nhập lại thông tin SQL Server; tài khoản sinh viên không dùng cho bước này.";
        }
        return "Không kết nối được SQL Server. Kiểm tra dịch vụ SQL Server, địa chỉ máy chủ, cổng và cấu hình kết nối.\nMáy mới cần khôi phục dữ liệu từ thư mục database trước khi sử dụng.";
    }
    /** Test before replacing an existing, working configuration. */
    public static void testAndSave(Properties candidate)throws Exception{
        try(Connection c=connect(candidate);Statement st=c.createStatement();ResultSet rs=st.executeQuery("SELECT COUNT(*) FROM sys.tables WHERE schema_id=SCHEMA_ID('dbo') AND name IN('SINH_VIEN','HOC_KY','LOP_HOC_PHAN','MON_HOC','KET_QUA_DANG_KY','CONG_NO_HOC_PHI')")){
            rs.next();if(rs.getInt(1)!=6)throw new IllegalArgumentException("Đã kết nối SQL Server nhưng CSDL chưa có đủ bảng dữ liệu. Khôi phục các tệp SQL trong thư mục database, rồi thử lại.");
        }
        Path file=AppPaths.configuration();Files.createDirectories(file.getParent());Path temporary=Files.createTempFile(file.getParent(),"connection-",".tmp");
        try{
            try(var writer=Files.newBufferedWriter(temporary)){candidate.store(writer,"Local SQL Server connection - do not commit or share passwords");}
            try{Files.move(temporary,file,StandardCopyOption.ATOMIC_MOVE,StandardCopyOption.REPLACE_EXISTING);}
            catch(AtomicMoveNotSupportedException e){Files.move(temporary,file,StandardCopyOption.REPLACE_EXISTING);}
        }finally{Files.deleteIfExists(temporary);}
    }
}
