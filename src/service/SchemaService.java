package service;
import config.DBConnect;
import utils.Db;
import java.sql.*;
/** Additive migration; original records and tables are retained. */
public final class SchemaService {
    public static boolean ready(){
        try(Connection c=DBConnect.getConnection()){return Db.count(c,"SELECT COUNT(*) FROM sys.tables WHERE name='QLTC_TAI_KHOAN'")==1;}
        catch(SQLException e){throw new IllegalStateException("Không kiểm tra được cấu trúc CSDL.",e);}
    }
    public static void install(Connection c)throws SQLException{
        for(String t:new String[]{"SINH_VIEN","HOC_KY","LOP_HOC_PHAN","MON_HOC","KET_QUA_DANG_KY","CONG_NO_HOC_PHI"})
            if(Db.count(c,"SELECT COUNT(*) FROM sys.tables WHERE name=?",t)==0)throw new SQLException("Thiếu bảng "+t+". Nhập dữ liệu SQL cũ trước.");
        Db.update(c,"IF OBJECT_ID('dbo.QLTC_TAI_KHOAN','U') IS NULL CREATE TABLE dbo.QLTC_TAI_KHOAN(TenDangNhap nvarchar(100) PRIMARY KEY,MatKhauHash varchar(256) NOT NULL,VaiTro varchar(10) NOT NULL CHECK(VaiTro IN('ADMIN','STUDENT')),BatBuocDoi bit NOT NULL DEFAULT 1,Khoa bit NOT NULL DEFAULT 0,SoLanSai int NOT NULL DEFAULT 0,KhoaDen datetime2 NULL,TaoLuc datetime2 NOT NULL DEFAULT SYSDATETIME())");
        Db.update(c,"IF OBJECT_ID('dbo.QLTC_CAU_HINH_HK','U') IS NULL CREATE TABLE dbo.QLTC_CAU_HINH_HK(MaHK nvarchar(100) PRIMARY KEY,MoDangKy bit NOT NULL DEFAULT 0,BatDau date NULL,KetThuc date NULL,DonGia decimal(18,2) NOT NULL DEFAULT 450000 CHECK(DonGia>=0),TinChiToiDa int NOT NULL DEFAULT 24 CHECK(TinChiToiDa BETWEEN 1 AND 60))");
        Db.update(c,"IF OBJECT_ID('dbo.QLTC_NHAT_KY','U') IS NULL CREATE TABLE dbo.QLTC_NHAT_KY(Id bigint IDENTITY PRIMARY KEY,ThoiGian datetime2 NOT NULL DEFAULT SYSDATETIME(),NguoiDung nvarchar(100) NOT NULL,HanhDong nvarchar(100) NOT NULL,ChiTiet nvarchar(2000) NOT NULL)");
        Db.update(c,"IF OBJECT_ID('dbo.QLTC_PHIEU_THU','U') IS NULL CREATE TABLE dbo.QLTC_PHIEU_THU(MaGiaoDich varchar(36) PRIMARY KEY,MaPhieu nvarchar(150) NOT NULL,SoTien decimal(18,2) NOT NULL CHECK(SoTien>0),NguoiThu nvarchar(100) NOT NULL,GhiChu nvarchar(500) NOT NULL,ThoiGian datetime2 NOT NULL DEFAULT SYSDATETIME())");
        Db.update(c,"INSERT INTO QLTC_CAU_HINH_HK(MaHK) SELECT h.MaHK FROM HOC_KY h WHERE NOT EXISTS(SELECT 1 FROM QLTC_CAU_HINH_HK q WHERE q.MaHK=h.MaHK)");
    }
    public static void audit(Connection c,String action,String detail)throws SQLException{
        Db.update(c,"INSERT INTO QLTC_NHAT_KY(NguoiDung,HanhDong,ChiTiet)VALUES(?,?,?)",Session.user(),action,detail);
    }
}
