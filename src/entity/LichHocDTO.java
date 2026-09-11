package entity;

public class LichHocDTO {
    private final String maLHP;
    private final String maMon;
    private final String tenMon;    
    private final String thu;       
    private final String tietHoc;   
    private final String phongHoc;
    private final String maHK;
    private final String tenGV; // THÊM MỚI

    public LichHocDTO(String maLHP, String maMon, String tenMon, String thu, String tietHoc, String phongHoc, String maHK, String tenGV) {
        this.maLHP    = maLHP;
        this.maMon    = maMon;
        this.tenMon   = tenMon;
        this.thu      = thu;
        this.tietHoc  = tietHoc;
        this.phongHoc = phongHoc;
        this.maHK     = maHK;
        this.tenGV    = tenGV; // THÊM MỚI
    }

    public String getMaLHP()    { return maLHP;    }
    public String getMaMon()    { return maMon;    }
    public String getTenMon()   { return tenMon;   }
    public String getThu()      { return thu;      }
    public String getTietHoc()  { return tietHoc;  }
    public String getPhongHoc() { return phongHoc; }
    public String getMaHK()     { return maHK;     }
    public String getTenGV()    { return tenGV;    } // THÊM MỚI
}