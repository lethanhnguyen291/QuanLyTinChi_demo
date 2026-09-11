package ui;

import config.DBConnect;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DiemPanel extends JPanel {
    
    private String currentMaSV;
    
    // Biến lưu Học kỳ hiện tại
    private String currentMaHK = "HK1_2425";
    private String currentTenHK = "Học kỳ 1 – 2024-2025";
    
    // Các biến lưu trữ thống kê theo TỪNG HỌC KỲ
    private int tcKyNay = 0;
    private double gpa10 = 0.0;
    private double gpa4 = 0.0;
    private String xepLoai = "Chưa xét";
    
    private DefaultTableModel tableModel;
    private JPanel topStatsPanel;
    private JPanel tableContainer;
    private JLabel lblTableTitle;

    public DiemPanel(String maSV) {
        this.currentMaSV = maSV;

        setLayout(new BorderLayout(0, 20));
        setBackground(UIUtils.BG_APP);
        setBorder(new EmptyBorder(10, 0, 0, 0));

        buildUI();
    }
    
    // =====================================================================
    // HÀM NHẬN TÍN HIỆU TỪ COMBOBOX ĐỂ ĐỒNG BỘ HỌC KỲ
    // =====================================================================
    public void updateData(String maHK, String fullTenHK) {
        this.currentMaHK = maHK;
        if (fullTenHK != null && fullTenHK.contains("-")) {
            this.currentTenHK = fullTenHK.substring(fullTenHK.indexOf("-") + 1).trim(); 
        } else {
            this.currentTenHK = fullTenHK;
        }
        buildUI(); // Vẽ lại toàn bộ giao diện và tải lại CSDL
    }

    private void buildUI() {
        // Xóa sạch UI cũ trước khi vẽ lại
        removeAll();
        
        // 1. TẢI DỮ LIỆU TỪ CSDL ĐỂ TÍNH TOÁN THỐNG KÊ CHO KỲ HIỆN TẠI
        loadStatsData();

        // 2. VẼ TOP STATS PANEL (Đã đổi nhãn thành Thống kê Học kỳ)
        topStatsPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        topStatsPanel.setBackground(UIUtils.BG_APP);
        topStatsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        topStatsPanel.add(createStatCard("Tín chỉ đạt kỳ này", tcKyNay + " TC", 1, new Color(239, 246, 255), new Color(30, 64, 175)));
        topStatsPanel.add(createStatCard("Điểm TB Học kỳ (Hệ 10)", String.format("%.2f", gpa10), 2, new Color(255, 247, 237), UIUtils.MIT_ORANGE));
        topStatsPanel.add(createStatCard("Điểm TB Học kỳ (Hệ 4)", String.format("%.2f", gpa4), 3, new Color(240, 253, 244), UIUtils.GREEN_500));
        topStatsPanel.add(createStatCard("Xếp loại Học kỳ", xepLoai, 4, new Color(254, 242, 242), UIUtils.RED_500));

        // 3. VẼ BẢNG ĐIỂM CHI TIẾT
        tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(UIUtils.WHITE);
        tableContainer.setBorder(new LineBorder(UIUtils.BORDER, 1, true));

        // --- ĐỔI MÀU NỀN VÀ CHỮ CHO DÒNG TÊN BẢNG (Màu xanh dương nhạt thanh lịch) ---
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(239, 246, 255)); // Nền Xanh dương nhạt (Light Blue)
        header.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(191, 219, 254)), // Viền nổi nhẹ
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        lblTableTitle = new JLabel("Bảng Điểm Chi Tiết - " + currentTenHK); 
        lblTableTitle.setFont(UIUtils.FONT_TITLE); 
        lblTableTitle.setForeground(new Color(30, 64, 175)); // Chữ màu Xanh dương đậm
        header.add(lblTableTitle, BorderLayout.WEST);

        String[] columns = {"Mã HP", "Tên môn học", "TC", "Chuyên cần", "Giữa kỳ", "Cuối kỳ", "Tổng kết", "Hệ 4", "Kết quả"};
        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(tableModel);
        UIUtils.styleTable(table); 
        
        // =========================================================
        // CÁCH ÉP MÀU HEADER CHỐNG LỖI CỦA WINDOWS LOOK AND FEEL
        // =========================================================
        DefaultTableCellRenderer customHeaderRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(new Color(30, 64, 175)); // Màu xanh dương đậm chuẩn Web
                label.setForeground(Color.WHITE); // Chữ màu trắng
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                label.setHorizontalAlignment(SwingConstants.LEFT);
                label.setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 0, 1, 1, new Color(40, 74, 185)), // Viền nổi
                    new EmptyBorder(10, 15, 10, 15) // Khoảng cách cho thoáng
                ));
                return label;
            }
        };

        // Gắn Renderer mới vào TỪNG cột để vô hiệu hóa Header của Windows
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(customHeaderRenderer);
        }
        // =========================================================
        
        ZebraRenderer zebra = new ZebraRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(zebra);
        }

        // Tải dữ liệu bảng
        loadTableData();

        JScrollPane scrollTable = new JScrollPane(table); 
        scrollTable.setBorder(BorderFactory.createEmptyBorder());

        tableContainer.add(header, BorderLayout.NORTH); 
        tableContainer.add(scrollTable, BorderLayout.CENTER);

        add(topStatsPanel, BorderLayout.NORTH);
        add(tableContainer, BorderLayout.CENTER);
        
        revalidate();
        repaint();
    }

    // =====================================================================
    // HÀM TÍNH TOÁN THỐNG KÊ (ĐÃ CẬP NHẬT ĐỂ TÍNH THEO TỪNG HỌC KỲ)
    // =====================================================================
    private void loadStatsData() {
        // Reset dữ liệu mỗi khi chuyển kỳ
        tcKyNay = 0; gpa10 = 0.0; gpa4 = 0.0; xepLoai = "Chưa xét";
        
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return;

            // Câu SQL lấy tổng tín chỉ những môn ĐẠT và Tính GPA trung bình những môn ĐÃ CÓ ĐIỂM trong kỳ được chọn
            String sqlDiem = "SELECT " +
                             "ISNULL(SUM(CASE WHEN kq.TrangThai = N'Đạt' THEN CAST(m.SoTinChi AS INT) ELSE 0 END), 0) as TCDat, " +
                             "AVG(CAST(kq.DiemTongKet AS FLOAT)) as DiemTB " +
                             "FROM KET_QUA_DANG_KY kq " +
                             "JOIN LOP_HOC_PHAN lhp ON kq.MaLHP = lhp.MaLHP " +
                             "JOIN MON_HOC m ON lhp.MaMon = m.MaMon " +
                             "WHERE kq.MaSV = ? AND lhp.MaHK = ? AND kq.DiemTongKet IS NOT NULL";
                             
            try (PreparedStatement ps = conn.prepareStatement(sqlDiem)) {
                ps.setString(1, currentMaSV); 
                ps.setString(2, currentMaHK); // Ép lọc theo học kỳ hiện tại
                ResultSet rs = ps.executeQuery();
                if (rs.next()) { 
                    tcKyNay = rs.getInt("TCDat"); 
                    gpa10 = rs.getDouble("DiemTB"); // Sẽ trả về 0 nếu chưa có môn nào có điểm
                    
                    if (gpa10 > 0) { 
                        gpa4 = (gpa10 / 10.0) * 4.0;
                        if (gpa4 >= 3.6) xepLoai = "Xuất sắc";
                        else if (gpa4 >= 3.2) xepLoai = "Giỏi";
                        else if (gpa4 >= 2.5) xepLoai = "Khá";
                        else if (gpa4 >= 2.0) xepLoai = "Trung bình";
                        else xepLoai = "Yếu";
                    }
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }

    private void loadTableData() {
        try (Connection conn = DBConnect.getConnection()) {
            String sql = "SELECT lhp.MaMon, m.TenMon, m.SoTinChi, kq.DiemChuyenCan, kq.DiemGiuaKy, kq.DiemCuoiKy, kq.DiemTongKet, kq.TrangThai " +
                         "FROM KET_QUA_DANG_KY kq " +
                         "JOIN LOP_HOC_PHAN lhp ON kq.MaLHP = lhp.MaLHP " +
                         "JOIN MON_HOC m ON lhp.MaMon = m.MaMon " +
                         "WHERE kq.MaSV = ? AND lhp.MaHK = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, currentMaSV);
            ps.setString(2, currentMaHK); 
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                double chuyenCan = rs.getDouble("DiemChuyenCan");
                double giuaKy = rs.getDouble("DiemGiuaKy");
                double cuoiKy = rs.getDouble("DiemCuoiKy");
                double tongKet = rs.getDouble("DiemTongKet");
                double he4 = Math.round((tongKet / 10.0 * 4.0) * 10.0) / 10.0;
                String trangThai = rs.getString("TrangThai");

                // Nếu chưa có điểm (trong DB là NULL hoặc 0) thì in ra khoảng trắng
                String tkStr = formatScore(tongKet);
                String he4Str = tkStr.isEmpty() ? "" : formatScore(he4);

                tableModel.addRow(new Object[]{ 
                    rs.getString("MaMon"), 
                    rs.getString("TenMon"), 
                    rs.getInt("SoTinChi"), 
                    formatScore(chuyenCan), 
                    formatScore(giuaKy), 
                    formatScore(cuoiKy), 
                    tkStr, 
                    he4Str, 
                    trangThai 
                });
            }
        } catch (Exception e) { 
            e.printStackTrace();
            tableModel.addRow(new Object[]{"---", "Lỗi tải dữ liệu", "", "", "", "", "", "", ""}); 
        }
    }

    private String formatScore(double score) {
        if (score <= 0) return "";
        if (score == Math.floor(score)) {
            return String.format("%.0f", score);
        }
        return String.format("%.1f", score);
    }

    // ==========================================
    // UI COMPONENTS
    // ==========================================
    private JPanel createStatCard(String title, String val, int iconType, Color bgColor, Color textColor) {
        JPanel card = new JPanel(new BorderLayout(10, 0)); 
        card.setBackground(bgColor); 
        card.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(bgColor.darker(), 1, true), 
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5)); 
        textPanel.setBackground(bgColor);
        
        JLabel lT = new JLabel(title); 
        lT.setFont(new Font("Segoe UI", Font.BOLD, 12)); 
        lT.setForeground(new Color(100, 116, 139));
        
        JLabel lV = new JLabel(val); 
        lV.setFont(new Font("Segoe UI", Font.BOLD, 22)); 
        lV.setForeground(textColor);
        
        textPanel.add(lT); 
        textPanel.add(lV);
        
        JLabel lIcon = new JLabel(new StatVectorIcon(iconType, textColor));
        card.add(textPanel, BorderLayout.CENTER); 
        card.add(lIcon, BorderLayout.EAST); 
        return card;
    }

    // ==========================================
    // RENDERERS & ICONS
    // ==========================================
    class ZebraRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
            } else {
                c.setBackground(UIUtils.MIT_RED_LIGHT);
            }
            
            if (value != null) {
                String text = value.toString().toLowerCase();
                if (column == table.getColumnCount() - 1) { // Cột Kết quả
                    if (text.contains("không đạt")) {
                        c.setForeground(new Color(185, 28, 28)); // Đỏ đậm
                        c.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    } else if (text.contains("đạt")) {
                        c.setForeground(new Color(22, 101, 52)); // Xanh lá đậm
                        c.setFont(new Font("Segoe UI", Font.BOLD, 13));
                    } else {
                        c.setForeground(UIUtils.TEXT_MAIN);
                        c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                    }
                } else if (column >= 3 && column <= 7 && !text.isEmpty()) { // Các cột điểm
                    c.setForeground(new Color(15, 23, 42));
                    c.setFont(new Font("Segoe UI", Font.BOLD, 13));
                } else {
                    c.setForeground(UIUtils.TEXT_MAIN);
                    c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                }
            }
            setBorder(new EmptyBorder(0, 15, 0, 15)); 
            return c;
        }
    }

    class StatVectorIcon implements Icon {
        private int type; 
        private Color color;
        
        public StatVectorIcon(int type, Color c) { 
            this.type = type; 
            this.color = c; 
        }
        
        public int getIconWidth() { return 36; } 
        public int getIconHeight() { return 36; }
        
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create(); 
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            switch(type) {
                case 1: // Icon Tín chỉ (Stack of books)
                    g2.drawRect(x+6, y+8, 24, 22);
                    g2.drawLine(x+18, y+8, x+18, y+30);
                    g2.drawLine(x+8, y+14, x+15, y+14);
                    g2.drawLine(x+8, y+20, x+15, y+20);
                    g2.drawLine(x+21, y+14, x+28, y+14);
                    break;
                case 2: // Icon GPA 10 (Target/Bullseye)
                    g2.drawOval(x+4, y+4, 28, 28);
                    g2.drawOval(x+10, y+10, 16, 16);
                    g2.fillOval(x+15, y+15, 6, 6);
                    break;
                case 3: // Icon GPA 4 (Star)
                    int[] xP = {x+18, x+23, x+34, x+25, x+29, x+18, x+7, x+11, x+2, x+13};
                    int[] yP = {y+2, y+12, y+13, y+22, y+34, y+28, y+34, y+22, y+13, y+12};
                    g2.fillPolygon(xP, yP, 10);
                    break;
                case 4: // Icon Xếp loại (Medal)
                    g2.drawOval(x+10, y+2, 16, 16);
                    g2.drawLine(x+14, y+16, x+10, y+32);
                    g2.drawLine(x+10, y+32, x+18, y+28);
                    g2.drawLine(x+18, y+28, x+26, y+32);
                    g2.drawLine(x+26, y+32, x+22, y+16);
                    break;
            }
            g2.dispose();
        }
    }
}