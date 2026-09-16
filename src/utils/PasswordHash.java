package utils;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.*;
import java.util.*;
public final class PasswordHash {
    private static final int ITERATIONS=600000;
    private PasswordHash() {}
    public static void validate(char[] password) {
        if(password.length<8||password.length>128) throw new IllegalArgumentException("Mật khẩu cần từ 8 đến 128 ký tự.");
    }
    public static String create(char[] password) {
        validate(password); byte[] salt=new byte[16];new SecureRandom().nextBytes(salt);
        return "pbkdf2-sha256$"+ITERATIONS+"$"+Base64.getEncoder().encodeToString(salt)+"$"+Base64.getEncoder().encodeToString(derive(password,salt,ITERATIONS));
    }
    public static boolean verify(char[] password,String encoded) {
        try { String[] p=encoded.split("\\$");if(p.length!=4||!p[0].equals("pbkdf2-sha256"))return false;
            int rounds=Integer.parseInt(p[1]);if(rounds<10000||rounds>2000000)return false;
            return MessageDigest.isEqual(Base64.getDecoder().decode(p[3]),derive(password,Base64.getDecoder().decode(p[2]),rounds));
        } catch(RuntimeException e) { return false; }
    }
    private static byte[] derive(char[] password,byte[] salt,int rounds) {
        PBEKeySpec spec=new PBEKeySpec(password,salt,rounds,256);
        try { return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded(); }
        catch(GeneralSecurityException e) {throw new IllegalStateException(e);}finally {spec.clearPassword();}
    }
}
