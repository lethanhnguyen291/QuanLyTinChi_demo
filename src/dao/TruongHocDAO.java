package dao;
import config.DBConnect;
import java.sql.*;
import java.util.*;
public class TruongHocDAO {
    public List<String> getAllHocKy() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT MaHK, TenHK FROM HOC_KY ORDER BY NamHoc DESC, MaHK ASC";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("MaHK") + " - " + rs.getString("TenHK"));
            }
        } catch (SQLException e) { throw new IllegalStateException("Không tải được dữ liệu học vụ.",e); }
        return list;
    }

    public double[] getThongTinCongNo(String maSV, String maHK) {
        double[] info = new double[]{-1.0, 0.0}; 
        String sql = "SELECT TongTienPhaiDong, SoTienDaDong FROM CONG_NO_HOC_PHI WHERE MaSV = ? AND MaHK = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV); ps.setString(2, maHK);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                info[0] = rs.getDouble("TongTienPhaiDong");
                info[1] = rs.getDouble("SoTienDaDong");
            }
        } catch (SQLException e) { throw new IllegalStateException("Không tải được dữ liệu học vụ.",e); }
        return info; 
    }

    public ResultSet getLichSuCongNo(String maSV) {
        String sql = "SELECT c.MaHK, h.TenHK, c.TongTienPhaiDong, c.SoTienDaDong, c.TrangThai " +
                     "FROM CONG_NO_HOC_PHI c " +
                     "LEFT JOIN HOC_KY h ON c.MaHK = h.MaHK " +
                     "WHERE c.MaSV = ? ORDER BY c.MaHK DESC";
        try (Connection conn=DBConnect.getConnection();PreparedStatement ps=conn.prepareStatement(sql)) {
            ps.setString(1,maSV);
            try(ResultSet rs=ps.executeQuery()){javax.sql.rowset.CachedRowSet copy=javax.sql.rowset.RowSetProvider.newFactory().createCachedRowSet();copy.populate(rs);return copy;}
        } catch(SQLException e){throw new IllegalStateException("Không tải được công nợ.",e);}
    }

    public ResultSet getChiTietDangKyTrongKy(String maSV, String maHK) {
        String sql = "SELECT m.MaMon, m.TenMon, m.SoTinChi " +
                     "FROM KET_QUA_DANG_KY kq " +
                     "JOIN LOP_HOC_PHAN lhp ON kq.MaLHP = lhp.MaLHP " +
                     "JOIN MON_HOC m ON lhp.MaMon = m.MaMon " +
                     "WHERE kq.MaSV = ? AND lhp.MaHK = ?";
        try (Connection conn=DBConnect.getConnection();PreparedStatement ps=conn.prepareStatement(sql)) {
            ps.setString(1,maSV);ps.setString(2,maHK);
            try(ResultSet rs=ps.executeQuery()){javax.sql.rowset.CachedRowSet copy=javax.sql.rowset.RowSetProvider.newFactory().createCachedRowSet();copy.populate(rs);return copy;}
        } catch(SQLException e){throw new IllegalStateException("Không tải được đăng ký học phần.",e);}
    }

    public int getTongTinChiTichLuy(String maSV){try{return service.AcademicService.summary(maSV,null).earned();}catch(Exception e){throw new IllegalStateException("Không tính được tín chỉ.",e);}}

    public boolean checkNoHocPhi(String maSV) {
        String sql = "SELECT COUNT(*) FROM CONG_NO_HOC_PHI WHERE MaSV = ? AND TRY_CONVERT(decimal(18,2),TongTienPhaiDong) > COALESCE(TRY_CONVERT(decimal(18,2),TRY_CONVERT(float,SoTienDaDong)),0)";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV); ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (SQLException e) { throw new IllegalStateException("Không tải được dữ liệu học vụ.",e); }
        return false;
    }

    public boolean checkChuanNgoaiNgu(String maSV) {
        String sql = "SELECT DatChuanNgoaiNgu FROM SINH_VIEN WHERE MaSV = ?";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maSV); ResultSet rs = ps.executeQuery();
            if (rs.next()) return service.AcademicService.languagePassed(rs.getObject(1));
        } catch (SQLException e) { throw new IllegalStateException("Không tải được dữ liệu học vụ.",e); }
        return false;
    }
}
