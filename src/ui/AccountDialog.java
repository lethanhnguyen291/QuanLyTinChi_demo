package ui;
import service.AccountService;
import utils.*;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
public final class AccountDialog{
    public static boolean change(Component parent,boolean required){
        JPasswordField old=new JPasswordField(),next=new JPasswordField(),again=new JPasswordField();
        JPanel p=new JPanel(new GridLayout(0,1,0,7));p.add(new JLabel(required?"Bạn cần đổi mật khẩu tạm trước khi vào hệ thống.":"Mật khẩu mới: từ 8 đến 128 ký tự."));
        p.add(new JLabel("Mật khẩu hiện tại"));p.add(old);p.add(new JLabel("Mật khẩu mới"));p.add(next);p.add(new JLabel("Nhập lại mật khẩu mới"));p.add(again);
        UIUtils.applyTheme(p);
        while(JOptionPane.showConfirmDialog(parent,p,"Đổi mật khẩu",JOptionPane.OK_CANCEL_OPTION,JOptionPane.PLAIN_MESSAGE)==JOptionPane.OK_OPTION){
            char[] a=old.getPassword(),b=next.getPassword(),c=again.getPassword();
            try{if(!Arrays.equals(b,c))throw new IllegalArgumentException("Hai mật khẩu mới chưa khớp.");new AccountService().changePassword(a,b);return true;}
            catch(Exception e){Ui.error(parent,e);}finally{Arrays.fill(a,'\0');Arrays.fill(b,'\0');Arrays.fill(c,'\0');}
        }return false;
    }
}
