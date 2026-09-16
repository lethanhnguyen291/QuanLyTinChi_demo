import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.*;
import javax.imageio.ImageIO;
import service.*;
import ui.*;
public class VisualSmoke{
    static Path destination;static int count;
    static void layout(Component c){if(c instanceof Container container){container.doLayout();for(Component child:container.getComponents())layout(child);}}
    static void render(String name,JComponent panel)throws Exception{
        if(!panel.isDisplayable())panel.addNotify();panel.setSize(1280,820);for(int i=0;i<5;i++)layout(panel);
        BufferedImage image=new BufferedImage(1280,820,BufferedImage.TYPE_INT_RGB);Graphics2D g=image.createGraphics();g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g.setColor(Color.WHITE);g.fillRect(0,0,1280,820);panel.printAll(g);g.dispose();ImageIO.write(image,"png",destination.resolve(name+".png").toFile());System.out.println("RENDERED "+name);count++;
    }
    static JButton button(Container c,String text){for(Component child:c.getComponents()){if(child instanceof JButton b&&b.getText().equals(text))return b;if(child instanceof Container container){JButton result=button(container,text);if(result!=null)return result;}}return null;}
    public static void main(String[] args)throws Exception{
        destination=Path.of(args[0]);Files.createDirectories(destination);
        SwingUtilities.invokeAndWait(()->{try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            var keys=UIManager.getDefaults().keys();while(keys.hasMoreElements()){Object key=keys.nextElement();if(UIManager.get(key) instanceof javax.swing.plaf.FontUIResource)UIManager.put(key,new javax.swing.plaf.FontUIResource("Segoe UI",Font.PLAIN,14));}
            render("01_login",App.loginPanel(()->{}));Session.start("QA_ADMIN","ADMIN");PortalPanel admin=new PortalPanel(new StudentManagerService(),true,"QA_ADMIN",Session::clear);render("02_admin_dashboard",admin);
            String[] pages={"Sinh viên & Giảng viên","Lớp học phần & Lịch học","Nhập & Quản lý điểm","Thu học phí","Báo cáo học vụ","Nhập dữ liệu Excel","Tài khoản & Ngoại ngữ","Cấu hình học kỳ","Nhật ký & Phiếu thu"};int n=3;
            for(String page:pages){JButton b=button(admin,page);if(b==null)throw new AssertionError("Missing menu: "+page);b.doClick();render(String.format("%02d_admin",n++),admin);}
            Session.start("BA01002","STUDENT");PortalPanel student=new PortalPanel(new StudentManagerService(),false,"BA01002",Session::clear);render("12_student_dashboard",student);
            String[] studentPages={"Lịch học theo tuần","Kết quả học tập","Đăng ký học phần","Học phí & Công nợ","Điều kiện tốt nghiệp"};n=13;
            for(String page:studentPages){JButton b=button(student,page);if(b==null)throw new AssertionError("Missing student menu");b.doClick();render(String.format("%02d_student",n++),student);}
        }catch(Exception e){throw new RuntimeException(e);}});
        System.out.println("VISUAL SMOKE: "+count+" screens rendered");
    }
}
