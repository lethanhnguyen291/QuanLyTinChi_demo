public class PackageSmoke{
 public static void main(String[] args)throws Exception{
  for(String name:new String[]{"App","ui.PortalPanel","service.RegistrationService","org.apache.poi.xssf.usermodel.XSSFWorkbook","com.microsoft.sqlserver.jdbc.SQLServerDriver"}){Class.forName(name);System.out.println("LOADED "+name);}
 }
}
