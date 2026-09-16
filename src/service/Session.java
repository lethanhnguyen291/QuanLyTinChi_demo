package service;
public final class Session {
    private static volatile String username,role;
    private Session() {}
    public static void start(String user,String value){username=user;role=value;}
    public static void clear(){username=null;role=null;}
    public static String user(){if(username==null)throw new IllegalStateException("Vui lòng đăng nhập lại.");return username;}
    public static boolean isAdmin(){return "ADMIN".equals(role);}
    public static void requireAdmin(){user();if(!isAdmin())throw new IllegalStateException("Chỉ dành cho cán bộ đào tạo.");}
    public static void requireStudent(String id){if(isAdmin()||!user().equalsIgnoreCase(id))throw new IllegalStateException("Bạn không có quyền thao tác cho sinh viên này.");}
}
