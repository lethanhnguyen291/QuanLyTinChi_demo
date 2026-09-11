package entity;

public class LopHocPhan {
    private String maLHP;
    private String maMon;
    private int sucChua;
    private int soTinChi;
    private String maHK; // Bổ sung thuộc tính Mã Học Kỳ

    // 1. Constructor 5 tham số (Phiên bản đầy đủ)
    public LopHocPhan(String maLHP, String maMon, int sucChua, int soTinChi, String maHK) {
        this.maLHP = maLHP; 
        this.maMon = maMon; 
        this.sucChua = sucChua; 
        this.soTinChi = soTinChi;
        this.maHK = maHK;
    }

    // 2. Constructor 4 tham số (Phiên bản tương thích ngược để sửa lỗi của bạn)
    public LopHocPhan(String maLHP, String maMon, int sucChua, int soTinChi) {
        this.maLHP = maLHP; 
        this.maMon = maMon; 
        this.sucChua = sucChua; 
        this.soTinChi = soTinChi;
        this.maHK = ""; // Mặc định chuỗi rỗng nếu không được truyền vào
    }
    
    public String getMaLHP() { return maLHP; }
    public String getMaMon() { return maMon; }
    public int getSucChua() { return sucChua; }
    public int getSoTinChi() { return soTinChi; }
    public String getMaHK() { return maHK; } 
}