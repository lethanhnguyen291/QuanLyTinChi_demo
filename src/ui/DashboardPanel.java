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

public class DashboardPanel extends JPanel implements javax.swing.Scrollable {
    public Dimension getPreferredScrollableViewportSize(){return new Dimension(900,620);}
    public boolean getScrollableTracksViewportWidth(){return true;}
    public boolean getScrollableTracksViewportHeight(){return false;}
    public int getScrollableUnitIncrement(Rectangle r,int o,int d){return 24;}
    public int getScrollableBlockIncrement(Rectangle r,int o,int d){return Math.max(24,r.height-24);}
    private String cohort="—";

    // Đã gỡ bỏ static final để biến thành biến có thể thay đổi động
    private String currentHK = "HK1_2425";
    private String currentHKLabel = "Học kỳ 1 – 2024-2025";
    
    private String currentMaSV;
    private String hoTen = "...", maLop = "...", maCTDT = "...";
    private String trangThaiHocTap = "Đang học", ngaySinh = "...", gioiTinh = "...";
    private int tinChiTichLuy = 0, tinChiKyNay = 0, requiredCredits = 0;
    private double gpa = 0.0, tongNo = 0.0;
    
    public DashboardPanel(String maSV) {
        this.currentMaSV = maSV;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(UIUtils.BG_APP);
        buildUI();
    }

    // ========================================================
    // HÀM MỚI: NHẬN TÍN HIỆU TỪ STUDENT PANEL (KHÔNG CÒN BÁO ĐỎ)
    // ========================================================
    public void updateData(String maHK, String fullTenHK) {
        this.currentHK = maHK;
        if (fullTenHK != null && fullTenHK.contains("-")) {
            this.currentHKLabel = fullTenHK.substring(fullTenHK.indexOf("-") + 1).trim(); 
        } else {
            this.currentHKLabel = fullTenHK;
        }
        buildUI();
    }

    private void buildUI() {
        removeAll();
        loadDataFromDatabase();
        
        add(createStudentProfileCard());
        add(Box.createVerticalStrut(20));
        add(createStatsGrid());
        add(Box.createVerticalStrut(20));
        add(createBottomWrapper());
        
        revalidate();
        repaint();
    }

    private void loadDataFromDatabase() {
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return;
            String sqlInfo = "SELECT HoTen, MaLop, MaCTDT, TrangThaiHocTap, NgaySinh, GioiTinh FROM SINH_VIEN WHERE MaSV = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlInfo)) {
                ps.setString(1, currentMaSV);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    hoTen = rs.getString("HoTen"); maLop = rs.getString("MaLop"); maCTDT = rs.getString("MaCTDT");
                    trangThaiHocTap = rs.getString("TrangThaiHocTap");
                    
                    // Xử lý chuyển đổi định dạng ngày sinh từ yyyy-MM-dd sang dd/MM/yyyy
                    String rawDate = rs.getString("NgaySinh");
                    if (rawDate != null && !rawDate.isEmpty()) {
                        try {
                            // Cắt bỏ phần giờ phút (nếu có) để parse chính xác ngày
                            if (rawDate.contains(" ")) rawDate = rawDate.split(" ")[0];
                            java.util.Date date = new java.text.SimpleDateFormat("yyyy-MM-dd").parse(rawDate);
                            ngaySinh = new java.text.SimpleDateFormat("dd/MM/yyyy").format(date);
                        } catch (Exception ex) {
                            ngaySinh = rawDate; // Nếu lỗi format thì in nguyên gốc
                        }
                    } else {
                        ngaySinh = "Chưa cập nhật";
                    }

                    gioiTinh = rs.getString("GioiTinh") != null ? rs.getString("GioiTinh") : "Chưa cập nhật";
                }
            }
            var summary=service.AcademicService.summary(currentMaSV,null);
            tinChiTichLuy=summary.earned();gpa=summary.gpa4();
            requiredCredits=service.AcademicService.requiredCredits(currentMaSV);
            cohort=java.util.Objects.toString(utils.Db.scalar(conn,"SELECT p.NienKhoaApDung FROM SINH_VIEN s JOIN CHUONG_TRINH_DAO_TAO p ON p.MaCTDT=s.MaCTDT WHERE s.MaSV=?",currentMaSV),"—");
            // Đã thay CURRENT_HK bằng biến currentHK
            String sqlKyNay = "SELECT SUM(CAST(m.SoTinChi AS INT)) as TCKyNay FROM KET_QUA_DANG_KY kq JOIN LOP_HOC_PHAN lhp ON kq.MaLHP = lhp.MaLHP JOIN MON_HOC m ON lhp.MaMon = m.MaMon WHERE kq.MaSV = ? AND lhp.MaHK = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlKyNay)) {
                ps.setString(1, currentMaSV); ps.setString(2, currentHK); ResultSet rs = ps.executeQuery();
                if (rs.next()) tinChiKyNay = rs.getInt("TCKyNay");
            }
            String sqlNo = "SELECT SUM(CAST(TongTienPhaiDong AS FLOAT) - CAST(SoTienDaDong AS FLOAT)) as ConNo FROM CONG_NO_HOC_PHI WHERE MaSV = ? AND TRY_CONVERT(decimal(18,2),TongTienPhaiDong) > COALESCE(TRY_CONVERT(decimal(18,2),TRY_CONVERT(float,SoTienDaDong)),0)";
            try (PreparedStatement ps = conn.prepareStatement(sqlNo)) {
                ps.setString(1, currentMaSV); ResultSet rs = ps.executeQuery();
                if (rs.next()) tongNo = rs.getDouble("ConNo");
            }
        } catch (Exception e) { throw new IllegalStateException("Không tải được thông tin sinh viên.",e); }
    }

    // ========================================================
    // KHU VỰC THỐNG KÊ (THẺ STATS)
    // ========================================================
    private JPanel createStatsGrid() {
        JPanel gridStats = new JPanel(new GridLayout(1, 4, 15, 0));
        gridStats.setBackground(UIUtils.BG_APP);
        gridStats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        
        // Sử dụng Icon Type (1: Sao, 2: Biểu đồ, 3: Sách, 4: Tiền) để vẽ đồ họa Vector thay vì dùng Emoji bị lỗi
        gridStats.add(createColoredStatCard("Tín chỉ tích lũy", tinChiTichLuy + " / " + requiredCredits, 1, new Color(254, 242, 242), new Color(220, 38, 38))); 
        gridStats.add(createColoredStatCard("Điểm tích lũy (GPA)", String.format("%.2f", gpa), 2, new Color(255, 247, 237), new Color(234, 88, 12))); 
        gridStats.add(createColoredStatCard("Tín chỉ kỳ này", tinChiKyNay + " TC", 3, new Color(240, 253, 244), new Color(22, 163, 74))); 
        gridStats.add(createColoredStatCard("Công nợ hiện tại", String.format("%,.0f đ", tongNo), 4, new Color(239, 246, 255), new Color(37, 99, 235))); 
        
        return gridStats;
    }

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

    JLabel icon = new JLabel(new StatIcon(iconType, textColor));

    card.add(textPanel, BorderLayout.CENTER);
    card.add(icon, BorderLayout.EAST);
    return card;
}

    // ========================================================
    // KHU VỰC BÊN DƯỚI (BẢNG & THÔNG BÁO)
    // ========================================================
    private JPanel createBottomWrapper() {
        JPanel bottomWrapper = new JPanel(new BorderLayout(20, 0));
        bottomWrapper.setBackground(UIUtils.BG_APP);

        // --- BẢNG LỊCH SỬ HỌC TẬP ---
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new LineBorder(UIUtils.BORDER, 1, true));

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(new Color(239, 246, 255));
        tableHeader.setBorder(BorderFactory.createCompoundBorder(
        new MatteBorder(0, 0, 1, 0, new Color(191, 219, 254)), 
        // Viền dưới nổi nhẹ
        new EmptyBorder(15, 20, 15, 20)

));
    
    

// 2. Đổi màu chữ của JLabel tiêu đề thành Xanh dương đậm
        
        // Thay đổi title để hiển thị tên Học kỳ động
        JLabel lblTableTitle = new JLabel("Kết quả trong học kỳ đang chọn");
        lblTableTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTableTitle.setForeground(new Color(30, 64, 175));// Màu xanh dương đậm
        lblTableTitle.setAlignmentX(Component.CENTER_ALIGNMENT);        
        tableHeader.add(lblTableTitle, BorderLayout.WEST);

        DefaultTableModel model = new DefaultTableModel(new String[]{"Mã HP", "Tên Môn", "Điểm", "Kết quả"}, 0){
            public boolean isCellEditable(int r, int c) { return false; }
        };
        try (Connection conn = DBConnect.getConnection()) {
            // Đã bổ sung lhp.MaHK = ? để lọc bảng theo Học kỳ
            String sql = "SELECT lhp.MaMon, m.TenMon, kq.DiemTongKet, kq.TrangThai FROM KET_QUA_DANG_KY kq JOIN LOP_HOC_PHAN lhp ON kq.MaLHP = lhp.MaLHP JOIN MON_HOC m ON lhp.MaMon = m.MaMon WHERE kq.MaSV = ? AND lhp.MaHK = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, currentMaSV);
            ps.setString(2, currentHK);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) model.addRow(new Object[]{rs.getString("MaMon"), rs.getString("TenMon"), java.util.Objects.toString(rs.getObject("DiemTongKet"),"—"), rs.getString("TrangThai")});
        } catch (Exception e) {}

        JTable table = new JTable(model);
        UIUtils.styleTable(table);UIUtils.columnWidths(table,110,250,70,140);
        
       
        DefaultTableCellRenderer customHeaderRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                label.setBackground(new Color(30, 64, 175)); // Nền Xanh dương đậm
                label.setForeground(Color.WHITE);            // Chữ Trắng
                label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                label.setHorizontalAlignment(SwingConstants.LEFT);
                label.setBorder(BorderFactory.createCompoundBorder(
                    new MatteBorder(0, 0, 1, 1, new Color(40, 74, 185)), // Vạch chia cột
                    new EmptyBorder(10, 10, 10, 10)
                ));
                return label;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(customHeaderRenderer);
        }
        // =========================================================

        // Gắn ZebraRenderer cho 3 cột đầu và BadgeRenderer (Huy hiệu xanh/đỏ) cho cột cuối
        ZebraRenderer zebra = new ZebraRenderer();
        for (int i = 0; i < table.getColumnCount() - 1; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(zebra);
        }
        int lastColumnIndex = table.getColumnCount() - 1;
        table.getColumnModel().getColumn(lastColumnIndex).setCellRenderer(new BadgeRenderer());
        
        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(new MatteBorder(1, 0, 0, 0, UIUtils.BORDER));
        scrollTable.getViewport().setBackground(Color.WHITE);
        
        tablePanel.add(tableHeader, BorderLayout.NORTH);
        tablePanel.add(scrollTable, BorderLayout.CENTER);

        // --- KHUNG NHẮC NHỞ TỪ HỆ THỐNG ---
        JPanel alertPanel = new JPanel();
        alertPanel.setLayout(new BoxLayout(alertPanel, BoxLayout.Y_AXIS));
        alertPanel.setBackground(Color.WHITE);
        alertPanel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIUtils.BORDER, 1, true), new EmptyBorder(20, 20, 20, 20)
        ));
        alertPanel.setPreferredSize(new Dimension(270, 0));

        JPanel alertHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        alertHeaderPanel.setBackground(Color.WHITE);
        JLabel lblAlertTitle = new JLabel("Nhắc nhở từ hệ thống");
        lblAlertTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblAlertTitle.setForeground(UIUtils.TEXT_MAIN);
        alertHeaderPanel.add(lblAlertTitle);
        alertHeaderPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        String noMsg = (tongNo > 0) ? "Bạn đang còn nợ học phí. Xem chi tiết tại mục Học phí & Công nợ." : "Bạn không còn số dư nợ trong dữ liệu hiện tại.";
        alertHeaderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE,45));
        alertHeaderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        alertPanel.add(alertHeaderPanel);
        alertPanel.add(createAlertItem("Tài chính", noMsg, tongNo > 0 ? "warn" : "info"));
        alertPanel.add(Box.createVerticalStrut(15));
        
        // Cập nhật tên Học kỳ trong thẻ thông báo
        alertPanel.add(createAlertItem("Học vụ", "Xem trạng thái đợt đăng ký và giới hạn tín chỉ tại mục Đăng ký học phần.", "info"));
        alertPanel.add(Box.createVerticalGlue()); 

        bottomWrapper.add(tablePanel, BorderLayout.CENTER);
        bottomWrapper.add(alertPanel, BorderLayout.EAST);
        return bottomWrapper;
    }

    private JPanel createStudentProfileCard() {
        JPanel profileCard = new JPanel(new BorderLayout(25, 0));
        profileCard.setBackground(Color.WHITE);
        profileCard.setBorder(BorderFactory.createCompoundBorder(new LineBorder(UIUtils.BORDER, 1, true), new EmptyBorder(20, 25, 20, 25)));
        profileCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        JPanel leftProfile = new JPanel(new BorderLayout(15, 0));
        leftProfile.setBackground(Color.WHITE);
        String firstChar = hoTen.length() > 0 && !hoTen.equals("Đang tải...") ? hoTen.substring(0, 1) : "A";
        JLabel lblAvatar = new JLabel(firstChar, SwingConstants.CENTER);
        lblAvatar.setFont(new Font("Segoe UI", Font.BOLD, 36)); lblAvatar.setForeground(Color.WHITE); lblAvatar.setBackground(UIUtils.MIT_RED); lblAvatar.setOpaque(true); lblAvatar.setPreferredSize(new Dimension(80, 80));
        
        JPanel namePanel = new JPanel(); namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.Y_AXIS)); namePanel.setBackground(Color.WHITE);
        JLabel lblName = new JLabel(hoTen); lblName.setFont(new Font("Segoe UI", Font.BOLD, 20)); lblName.setForeground(UIUtils.TEXT_MAIN);
        JLabel lblMSSV = new JLabel("MSSV: " + currentMaSV); lblMSSV.setForeground(UIUtils.TEXT_MUTED);
        JLabel lblStatus = new JLabel(" " + trangThaiHocTap + " "); lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12)); lblStatus.setBackground(new Color(220, 252, 231)); lblStatus.setForeground(new Color(22, 101, 52)); lblStatus.setOpaque(true);
        namePanel.add(Box.createVerticalStrut(5)); namePanel.add(lblName); namePanel.add(Box.createVerticalStrut(5)); namePanel.add(lblMSSV); namePanel.add(Box.createVerticalStrut(5)); namePanel.add(lblStatus);

        leftProfile.add(lblAvatar, BorderLayout.WEST); leftProfile.add(namePanel, BorderLayout.CENTER);

        JPanel gridInfo = new JPanel(new GridLayout(3, 2, 20, 10));
        gridInfo.setBackground(Color.WHITE); gridInfo.setBorder(new MatteBorder(0, 1, 0, 0, UIUtils.BORDER)); 
        gridInfo.add(createProfileAttr("Ngày sinh:", ngaySinh)); gridInfo.add(createProfileAttr("Niên khóa:", cohort));
        gridInfo.add(createProfileAttr("Giới tính:", gioiTinh)); gridInfo.add(createProfileAttr("Bậc đào tạo:", "Đại học"));
        gridInfo.add(createProfileAttr("Lớp học:", maLop)); gridInfo.add(createProfileAttr("Ngành:", maCTDT));

        JPanel rightWrapper = new JPanel(new BorderLayout()); rightWrapper.setBackground(Color.WHITE); rightWrapper.setBorder(new EmptyBorder(0, 20, 0, 0)); rightWrapper.add(gridInfo, BorderLayout.CENTER);
        profileCard.add(leftProfile, BorderLayout.WEST); profileCard.add(rightWrapper, BorderLayout.CENTER);
        return profileCard;
    }

    private JPanel createProfileAttr(String label, String value) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); p.setBackground(Color.WHITE);
        JLabel l1 = new JLabel(label + "  "); l1.setForeground(UIUtils.MIT_RED);
        JLabel l2 = new JLabel(value); l2.setFont(new Font("Segoe UI", Font.BOLD, 14)); l2.setForeground(UIUtils.TEXT_MAIN);
        p.add(l1); p.add(l2); return p;
    }

    // ========================================================
    // TẠO THẺ THÔNG BÁO VỚI VIỀN TRÁI NỔI BẬT NHƯ WEB
    // ========================================================
    private JPanel createAlertItem(String tag,String body,String type){
        JPanel panel=new JPanel(new BorderLayout(0,8));panel.setBackground(new Color(248,250,252));
        panel.setBorder(BorderFactory.createCompoundBorder(new LineBorder(UIUtils.BORDER),new EmptyBorder(10,12,10,12)));
        JLabel label=new JLabel(tag);label.setFont(new Font("Segoe UI",Font.BOLD,12));label.setForeground(type.equals("warn")?UIUtils.RED_500:new Color(30,64,175));
        JTextArea text=new JTextArea(body);text.setEditable(false);text.setLineWrap(true);text.setWrapStyleWord(true);text.setOpaque(false);text.setFont(new Font("Segoe UI",Font.PLAIN,12));text.setRows(4);
        panel.add(label,BorderLayout.NORTH);panel.add(text,BorderLayout.CENTER);panel.setMaximumSize(new Dimension(Integer.MAX_VALUE,135));panel.setAlignmentX(Component.LEFT_ALIGNMENT);return panel;
    }

    // ========================================================
    // CÁC LỚP RENDERER VẼ ĐỒ HỌA (ICONS, BẢNG)
    // ========================================================

    // Lớp vẽ ICONS Vector cho 4 ô Thống kê (Chống lỗi ô vuông hoàn toàn)
    class StatIcon implements Icon {
        private int type;
        private Color color;
        public StatIcon(int type, Color color) { this.type = type; this.color = color; }
        public int getIconWidth() { return 36; }
        public int getIconHeight() { return 36; }
        
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            
            switch(type) {
                case 1: // Ngôi sao (Tín chỉ tích lũy)
                    int[] xP = {x+18, x+23, x+34, x+25, x+29, x+18, x+7, x+11, x+2, x+13};
                    int[] yP = {y+2, y+12, y+13, y+22, y+34, y+28, y+34, y+22, y+13, y+12};
                    g2.fillPolygon(xP, yP, 10);
                    break;
                case 2: // Biểu đồ tăng trưởng (GPA)
                    g2.drawLine(x+4, y+32, x+32, y+32); // Trục X
                    g2.drawLine(x+4, y+4, x+4, y+32);   // Trục Y
                    g2.drawLine(x+8, y+22, x+16, y+12);
                    g2.drawLine(x+16, y+12, x+24, y+18);
                    g2.drawLine(x+24, y+18, x+32, y+4);
                    g2.fillOval(x+14, y+10, 5, 5);
                    g2.fillOval(x+22, y+16, 5, 5);
                    g2.fillOval(x+30, y+2, 5, 5);
                    break;
                case 3: // Sách (Tín chỉ kỳ này)
                    g2.drawRect(x+6, y+8, 24, 22);
                    g2.drawLine(x+18, y+8, x+18, y+30);
                    g2.drawLine(x+8, y+14, x+15, y+14);
                    g2.drawLine(x+8, y+20, x+15, y+20);
                    g2.drawLine(x+21, y+14, x+28, y+14);
                    break;
                case 4: // Tiền xu (Công nợ)
                    g2.drawOval(x+6, y+6, 24, 24);
                    g2.drawOval(x+10, y+10, 16, 16);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
                    g2.drawString("đ", x+13, y+25);
                    break;
            }
            g2.dispose();
        }
    }

    // Zebra Renderer (Kẻ sọc cho bảng)
    class ZebraRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) c.setBackground(row % 2 == 0 ? Color.WHITE : UIUtils.BLUE_SOFT);
            else c.setBackground(UIUtils.BLUE_LIGHT);
            c.setForeground(UIUtils.TEXT_MAIN);
            setBorder(new EmptyBorder(0, 15, 0, 15)); return c;
        }
    }

    // Badge Renderer (Tạo huy hiệu nền màu cho cột Kết quả)
    class BadgeRenderer extends DefaultTableCellRenderer {
        private JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 6));
        private JLabel lbl = new JLabel();

        public BadgeRenderer() {
            p.setOpaque(true); lbl.setOpaque(true);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setBorder(new EmptyBorder(4, 10, 4, 10));
            p.add(lbl);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            p.setBackground(isSelected ? table.getSelectionBackground() : (row % 2 == 0 ? Color.WHITE : UIUtils.BLUE_SOFT));
            if (value != null) {
                String text = value.toString();
                lbl.setText(text);
                if (text.equalsIgnoreCase("Đạt") || text.equalsIgnoreCase("Đã hoàn thành")) {
                    lbl.setBackground(new Color(220, 252, 231)); 
                    lbl.setForeground(new Color(21, 128, 61));
                } else if (text.toLowerCase().contains("không đạt") || text.toLowerCase().contains("chưa")) {
                    lbl.setBackground(new Color(254, 226, 226)); 
                    lbl.setForeground(new Color(220, 38, 38));
                } else {
                    lbl.setBackground(new Color(241, 245, 249)); 
                    lbl.setForeground(new Color(71, 85, 105));
                }
            } else {
                lbl.setText("");
            }
            return p;
        }
    }
}