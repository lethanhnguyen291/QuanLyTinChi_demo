package dao;

import entity.HocKy;
import entity.LichHocDTO;
import config.DBConnect; 

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LichHocDAO {

    private static final String SQL_GET_HOC_KY = "SELECT MaHK, TenHK, NamHoc FROM HOC_KY ORDER BY NamHoc DESC, MaHK DESC";

    public List<HocKy> getDanhSachHocKy() throws SQLException {
        List<HocKy> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_HOC_KY);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new HocKy(rs.getString("MaHK"), rs.getString("TenHK"), rs.getString("NamHoc")));
            }
        }
        return list;
    }

    private static final String SQL_GET_LICH_HOC = 
            "SELECT lhp.MaLHP, lhp.MaMon, mh.TenMon, lhp.Thu, lhp.TietHoc, lhp.PhongHoc, lhp.MaHK " +
            "FROM KET_QUA_DANG_KY kq " +
            "JOIN LOP_HOC_PHAN lhp ON kq.MaLHP = lhp.MaLHP " +
            "JOIN MON_HOC mh ON lhp.MaMon = mh.MaMon " +
            "WHERE kq.MaSV = ? AND lhp.MaHK = ? " +
            "ORDER BY lhp.Thu, lhp.TietHoc";

    public List<LichHocDTO> getLichHocSinhVien(String maSV, String maHK) throws SQLException {
        List<LichHocDTO> list = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement(SQL_GET_LICH_HOC)) {
            ps.setString(1, maSV);
            ps.setString(2, maHK);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Tạm thời giả lập tên giảng viên (Khoa chuyên môn) do DB chưa có bảng phân công
                    String tenGV = "Khoa chuyên môn";
                    list.add(new LichHocDTO(
                        rs.getString("MaLHP"), rs.getString("MaMon"), rs.getString("TenMon"),
                        rs.getString("Thu"), rs.getString("TietHoc"), rs.getString("PhongHoc"), rs.getString("MaHK"),
                        tenGV
                    ));
                }
            }
        }
        return list;
    }
}