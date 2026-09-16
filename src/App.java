import service.*;
import ui.*;
import utils.Ui;
import javax.swing.*;
import java.awt.*;

public class App {
    private static JFrame frame;
    public static void main(String[] args){SwingUtilities.invokeLater(()->{
        try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());}catch(Exception ignored){}
        var keys=UIManager.getDefaults().keys();while(keys.hasMoreElements()){Object key=keys.nextElement();if(UIManager.get(key) instanceof javax.swing.plaf.FontUIResource)UIManager.put(key,new javax.swing.plaf.FontUIResource("Segoe UI",Font.PLAIN,14));}
        utils.UIUtils.installTheme();
        Thread.setDefaultUncaughtExceptionHandler((thread,error)->SwingUtilities.invokeLater(()->Ui.error(frame,error)));
        frame=new JFrame("MIT PORTAL · Quản lý tín chỉ");frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);frame.setMinimumSize(new Dimension(1000,700));frame.setSize(1280,820);frame.setLocationRelativeTo(null);
        login();frame.setVisible(true);
    });}
    public static JPanel loginPanel(Runnable ready){return new LoginPanel(ready);}
    private static void login(){
        Session.clear();
        LoginPanel panel=new LoginPanel(()->{
            StudentManagerService service=new StudentManagerService();
            frame.setContentPane(new PortalPanel(service,Session.isAdmin(),Session.user(),App::login));
            frame.revalidate();frame.repaint();
        });
        frame.setContentPane(panel);frame.revalidate();frame.repaint();
        panel.connectAutomatically();
    }
}
