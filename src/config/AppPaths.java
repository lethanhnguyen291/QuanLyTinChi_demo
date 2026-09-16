package config;
import java.nio.file.*;
/** Local files belong to the application directory, not the caller's directory. */
public final class AppPaths {
    private AppPaths() {}
    public static Path directory() {
        String override=System.getProperty("qltc.home");
        if(override!=null&&!override.isBlank())return Path.of(override).toAbsolutePath().normalize();
        try {
            Path location=Path.of(AppPaths.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if(Files.isRegularFile(location))return location.getParent();
            for(Path p=location;p!=null;p=p.getParent())if(Files.isDirectory(p.resolve("src"))&&Files.isDirectory(p.resolve("lib")))return p;
        }catch(Exception e){throw new IllegalStateException("Không xác định được thư mục ứng dụng. Hãy chạy RUN.bat.",e);}
        throw new IllegalStateException("Không tìm thấy thư mục dự án. Hãy chạy RUN.bat hoặc mở đúng thư mục chứa src và lib.");
    }
    public static Path configuration() {
        String override=System.getProperty("qltc.config");
        Path file=Path.of(override==null?"config/database.properties":override);
        return (file.isAbsolute()?file:directory().resolve(file)).toAbsolutePath().normalize();
    }
}
