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

public class AdminDashboardPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private String currentMaHK = "";
    private DefaultTableModel tableModel;
    private JPanel statsRow;
    private JLabel lblTableTitle;
    private JTable table;

    public AdminDashboardPanel() {
        setLayout(new BorderLayout(0, 20));
        setBackground(UIUtils.BG_APP);

        // --- 1. KHU VỰC THẺ THỐNG KÊ (TOP) ---
        statsRow = new JPanel(new GridLayout(1, 4, 20, 0));
        statsRow.setBackground(UIUtils.BG_APP);
        statsRow.setPreferredSize(new Dimension(0, 100));

        // --- 2. KHU VỰC BẢNG DANH SÁCH (BOTTOM) ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(UIUtils.WHITE);
        tablePanel.setBorder(new LineBorder(UIUtils.BORDER, 1, true));

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(UIUtils.WHITE);
        tableHeader.setBorder(new EmptyBorder(15, 20, 15, 20));
        lblTableTitle = new JLabel("Lớp học phần đang mở (Kỳ này)");
        lblTableTitle.setFont(UIUtils.FONT_BOLD);
        lblTableTitle.setForeground(UIUtils.BLUE_DARK);
        tableHeader.add(lblTableTitle, BorderLayout.WEST);

        String[] columns = {"Mã LHP", "Tên Môn", "TC", "Lịch học", "Phòng", "Sĩ số", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);UIUtils.columnWidths(table,140,240,50,195,90,90,125);
        
        // Kẻ lưới cho bảng đẹp hơn
        table.setShowGrid(true);
        table.setGridColor(UIUtils.BORDER);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        // Áp dụng Zebra Renderer để tô màu xen kẽ và cảnh báo lớp đầy
        ZebraRenderer zebra = new ZebraRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(zebra);
        }

        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(new MatteBorder(1, 0, 0, 0, UIUtils.BORDER));

        tablePanel.add(tableHeader, BorderLayout.NORTH);
        tablePanel.add(scrollTable, BorderLayout.CENTER);

        add(statsRow, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);
    }

    // ========================================================
    // HÀM ĐƯỢC GỌI TỪ ADMINPANEL KHI CHỌN COMBOBOX HỌC KỲ
    // ========================================================
    public void updateData(String maHK) {
        this.currentMaHK = maHK;
        
        int tongSV = 0;
        int tongLHP = 0;
        double doanhThu = 0.0;
        int svNoTien = 0;

        tableModel.setRowCount(0);

        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return;

            // 1. Lấy tổng số Sinh viên (Toàn trường)
            String sqlSV = "SELECT COUNT(*) FROM SINH_VIEN";
            try (PreparedStatement ps = conn.prepareStatement(sqlSV); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) tongSV = rs.getInt(1);
            }

            // 2. Lấy số Lớp học phần mở trong kỳ
            String sqlLHP = "SELECT COUNT(*) FROM LOP_HOC_PHAN WHERE MaHK = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlLHP)) {
                ps.setString(1, maHK);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) tongLHP = rs.getInt(1);
                }
            }

            // 3. Tính dự toán Doanh thu học phí trong kỳ
            String sqlTien = "SELECT SUM(TongTienPhaiDong) FROM CONG_NO_HOC_PHI WHERE MaHK = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlTien)) {
                ps.setString(1, maHK);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) doanhThu = rs.getDouble(1);
                }
            }

            // 4. Đếm số SV đang nợ học phí trong kỳ (Cảnh báo)
            String sqlNo = "SELECT COUNT(DISTINCT MaSV) FROM CONG_NO_HOC_PHI WHERE MaHK = ? AND TrangThai != N'Đã hoàn thành'";
            try (PreparedStatement ps = conn.prepareStatement(sqlNo)) {
                ps.setString(1, maHK);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) svNoTien = rs.getInt(1);
                }
            }

            // 5. Tải danh sách Lớp học phần lên bảng
            String sqlTable = "SELECT lhp.MaLHP, m.TenMon, m.SoTinChi, lhp.Thu, lhp.TietHoc, lhp.PhongHoc, lhp.SucChua, " +
                              "(SELECT COUNT(*) FROM KET_QUA_DANG_KY WHERE MaLHP = lhp.MaLHP) as DaDK " +
                              "FROM LOP_HOC_PHAN lhp JOIN MON_HOC m ON lhp.MaMon = m.MaMon WHERE lhp.MaHK = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlTable)) {
                ps.setString(1, maHK);
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        int daDk = rs.getInt("DaDK");
                        int sucChua = rs.getInt("SucChua");
                        String thu = rs.getString("Thu");
                        String lichHoc = (thu != null && thu.matches("[2-7]") ? "Thứ " + thu : thu) + " (Tiết " + rs.getString("TietHoc") + ")";
                        String siSo = daDk + " / " + sucChua;
                        String trangThai = daDk >= sucChua ? "Đã đầy" : "Còn chỗ";
                        
                        tableModel.addRow(new Object[]{
                            rs.getString("MaLHP"), rs.getString("TenMon"), rs.getInt("SoTinChi"),
                            lichHoc, rs.getString("PhongHoc"), siSo, trangThai
                        });
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Cập nhật lại UI Thẻ thống kê
        statsRow.removeAll();
        // Thẻ 1: Xanh dương (Tổng SV)
        statsRow.add(createColoredStatCard("Tổng sinh viên", String.format("%,d SV", tongSV), 1, new Color(239, 246, 255), new Color(30, 64, 175))); 
        // Thẻ 2: Xanh lá (Tổng LHP)
        statsRow.add(createColoredStatCard("Lớp học phần mở", String.format("%,d Lớp", tongLHP), 2, new Color(240, 253, 244), UIUtils.GREEN_500)); 
        // Thẻ 3: Cam (Doanh thu)
        statsRow.add(createColoredStatCard("Doanh thu dự kiến", String.format("%,.0f Đ", doanhThu), 3, new Color(255, 247, 237), UIUtils.MIT_ORANGE)); 
        // Thẻ 4: Đỏ (Cảnh báo)
        statsRow.add(createColoredStatCard("SV nợ học phí", String.format("%,d SV", svNoTien), 4, new Color(254, 242, 242), UIUtils.RED_500)); 

        statsRow.revalidate();
        statsRow.repaint();
        
        lblTableTitle.setText("Danh sách Lớp học phần đang mở (" + maHK + ")");
    }

    // ========================================================
    // TIỆN ÍCH VẼ GIAO DIỆN (UI BUILDERS)
    // ========================================================
    private JPanel createColoredStatCard(
        String title, String val, int iconType,
        Color bgColor, Color textColor) {

    JPanel card = new JPanel(new BorderLayout(12, 0));
    card.setBackground(bgColor);
    card.setBorder(UIUtils.cardBorder(UIUtils.BORDER, 16));

    JPanel textPanel = new JPanel();
    textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
    textPanel.setOpaque(false);

    JLabel label = new JLabel(title);
    label.setFont(new Font("Segoe UI", Font.BOLD, 12));
    label.setForeground(UIUtils.TEXT_MUTED);
    label.setToolTipText(title);

    JLabel value = new JLabel(val);
    value.setFont(new Font("Segoe UI", Font.BOLD, 22));
    value.setForeground(textColor);
    value.setToolTipText(val);

    textPanel.add(label);
    textPanel.add(Box.createVerticalStrut(7));
    textPanel.add(value);

    JLabel icon = new JLabel(new StatVectorIcon(iconType, textColor));

    card.add(textPanel, BorderLayout.CENTER);
    card.add(icon, BorderLayout.EAST);
    return card;
}

    // ========================================================
    // VECTOR ICONS & RENDERERS
    // ========================================================
    class ZebraRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : UIUtils.BLUE_SOFT);
            } else {
                c.setBackground(UIUtils.BLUE_LIGHT);
            }
            
            // Xử lý màu chữ cho cột Trạng thái
            if (column == 6 && value != null) {
                String text = value.toString();
                if (text.equals("Đã đầy")) {
                    c.setForeground(UIUtils.RED_500);
                    c.setFont(new Font("Segoe UI", Font.BOLD, 14));
                } else {
                    c.setForeground(UIUtils.GREEN_500);
                    c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                }
            } else {
                c.setForeground(UIUtils.TEXT_MAIN);
                c.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            }
            
            setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 1, UIUtils.BORDER), new EmptyBorder(0, 15, 0, 15)
            ));
            return c;
        }
    }

    class StatVectorIcon implements Icon {
        private int type; 
        private Color color;
        public StatVectorIcon(int type, Color c) { this.type = type; this.color = c; }
        public int getIconWidth() { return 36; } 
        public int getIconHeight() { return 36; }
        
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create(); 
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color); 
            g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            switch (type) {
                case 1: // Group of Users
                    g2.drawOval(x+16, y+4, 10, 10);
                    g2.drawArc(x+12, y+18, 18, 14, 0, 180);
                    g2.drawOval(x+6, y+10, 8, 8);
                    g2.drawArc(x+2, y+24, 14, 10, 0, 180);
                    break;
                case 2: // Whiteboard / Classes
                    g2.drawRoundRect(x+4, y+6, 28, 20, 4, 4);
                    g2.drawLine(x+8, y+26, x+4, y+34);
                    g2.drawLine(x+28, y+26, x+32, y+34);
                    g2.drawLine(x+8, y+30, x+28, y+30);
                    g2.fillRect(x+12, y+12, 10, 2);
                    g2.fillRect(x+12, y+18, 6, 2);
                    break;
                case 3: // Money
                    g2.drawOval(x+4, y+4, 28, 28); 
                    g2.setFont(new Font("Arial", Font.BOLD, 18)); 
                    g2.drawString("$", x+12, y+24); 
                    break;
                case 4: // Warning Alert
                    int[] xP = {x+18, x+4, x+32};
                    int[] yP = {y+4, y+30, y+30};
                    g2.drawPolygon(xP, yP, 3);
                    g2.drawLine(x+18, y+12, x+18, y+22);
                    g2.fillOval(x+16, y+25, 4, 4);
                    break;
            }
            g2.dispose();
        }
    }
}