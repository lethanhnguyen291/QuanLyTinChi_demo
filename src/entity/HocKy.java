package entity;

public class HocKy {
    private String maHK;
    private String tenHK;
    private String namHoc;

    public HocKy(String maHK, String tenHK, String namHoc) {
        this.maHK   = maHK;
        this.tenHK  = tenHK;
        this.namHoc = namHoc;
    }

    public String getMaHK()   { return maHK;   }
    public String getTenHK()  { return tenHK;  }
    public String getNamHoc() { return namHoc; }

    @Override
    public String toString() {
        return maHK + " - " + tenHK;
    }
}