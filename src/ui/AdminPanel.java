package ui;

import service.StudentManagerService;
import utils.UIUtils;
import config.DBConnect;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AdminPanel extends JPanel {
    private static final long serialVersionUID = 1L; 
    
    private StudentManagerService service;
    private CardLayout cardLayout;
    private JPanel contentArea;
    private ArrayList<JButton> sidebarButtons = new ArrayList<>();
    private JLabel lblHeaderTitle;

    // Khai báo chuẩn 6 Module con
    private AdminDashboardPanel adminDashboardPanel; 
    private AdminUserPanel adminUserPanel;
    private AdminLopHocPhanPanel adminLopHocPhanPanel; 
    private AdminCongNoPanel adminCongNoPanel;
    private AdminBaoCaoPanel adminBaoCaoPanel;
    private AdminImportPanel adminImportPanel; // <-- Module riêng biệt

    public AdminPanel(StudentManagerService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBackground(UIUtils.BG_APP);

        // --- 1. SIDEBAR ---
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(UIUtils.MIT_RED);
        sidebar.setPreferredSize(new Dimension(310, 0));

        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBackground(UIUtils.MIT_RED);
        logoPanel.setBorder(new EmptyBorder(30, 20, 30, 20));
        
        JLabel logo1 = new JLabel(" MIT ADMIN");
        logo1.setIcon(new LogoIcon());
        logo1.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo1.setForeground(Color.WHITE);
        logo1.setAlignmentX(Component.LEFT_ALIGNMENT);
        logoPanel.add(logo1);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(UIUtils.MIT_RED);

        // --- 2. HEADER CÓ CHỌN HỌC KỲ ĐỘNG ---
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(UIUtils.BG_APP);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(UIUtils.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, UIUtils.BORDER), new EmptyBorder(15, 30, 15, 30)
        ));

        lblHeaderTitle = new JLabel("Trang Chủ Tổng Quan");
        lblHeaderTitle.setFont(UIUtils.FONT_TITLE);
        header.add(lblHeaderTitle, BorderLayout.WEST);

        JPanel hkPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        hkPanel.setBackground(UIUtils.WHITE);
        hkPanel.add(new JLabel("Học kỳ làm việc:"));
        JComboBox<String> cbHocKy = new JComboBox<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT MaHK, TenHK FROM HOC_KY ORDER BY NamHoc DESC, MaHK DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) cbHocKy.addItem(rs.getString("MaHK") + " - " + rs.getString("TenHK"));
        } catch (Exception e) {
            cbHocKy.addItem("HK1_2425 - Học kỳ 1 2024-2025"); 
        }
        cbHocKy.setFont(UIUtils.FONT_BOLD);
        cbHocKy.setBackground(UIUtils.WHITE);
        hkPanel.add(cbHocKy);
        header.add(hkPanel, BorderLayout.EAST);

        // --- 3. CARD LAYOUT (GẮN 6 MODULE VÀO) ---
        cardLayout = new CardLayout();
        contentArea = new JPanel(cardLayout);
        contentArea.setBackground(UIUtils.BG_APP);
        contentArea.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Khởi tạo các panel chức năng
        adminDashboardPanel = new AdminDashboardPanel(); 
        adminUserPanel = new AdminUserPanel(service);
        adminLopHocPhanPanel = new AdminLopHocPhanPanel(); 
        adminCongNoPanel = new AdminCongNoPanel();
        adminBaoCaoPanel = new AdminBaoCaoPanel(service);
        adminImportPanel = new AdminImportPanel(service);

        // Đổ 6 màn hình vào bộ khung
        contentArea.add(adminDashboardPanel, "DASHBOARD"); 
        contentArea.add(adminUserPanel, "QL_USERS");
        contentArea.add(adminLopHocPhanPanel, "QL_HOCPHAN"); 
        contentArea.add(adminCongNoPanel, "QL_CONGNO");
        contentArea.add(adminBaoCaoPanel, "BAO_CAO");
        contentArea.add(adminImportPanel, "IMPORT");

        // --- 4. TẠO 6 NÚT BẤM SIDEMENU ---
        JButton btnDash   = createSidebarBtn(1, "Trang Chủ Tổng Quan", true);
        JButton btnQLUser = createSidebarBtn(2, "DS Sinh Viên, Giảng Viên", false);
        JButton btnQLLHP  = createSidebarBtn(3, "Lớp Học Phần & Xếp Lịch", false);
        JButton btnCongNo = createSidebarBtn(4, "Quản Lý Thu Công Nợ", false);
        JButton btnBaoCao = createSidebarBtn(5, "Thống Kê & Báo Cáo", false);
        JButton btnImport = createSidebarBtn(6, "Nạp Dữ Liệu Excel", false);

        btnDash.addActionListener(e -> switchTab(btnDash, "DASHBOARD", "Trang Chủ Tổng Quan"));
        btnQLUser.addActionListener(e -> switchTab(btnQLUser, "QL_USERS", "Danh Sách Sinh Viên và Giảng Viên"));
        btnQLLHP.addActionListener(e -> switchTab(btnQLLHP, "QL_HOCPHAN", "Quản Lý Lớp Học Phần & Xếp Lịch")); 
        btnCongNo.addActionListener(e -> switchTab(btnCongNo, "QL_CONGNO", "Quản Lý Thu Học Phí Sinh Viên"));
        btnBaoCao.addActionListener(e -> switchTab(btnBaoCao, "BAO_CAO", "Báo Cáo & Thống Kê Tổng Hợp"));
        btnImport.addActionListener(e -> switchTab(btnImport, "IMPORT", "Nạp Dữ Liệu Từ Tệp Excel"));

        menuPanel.add(btnDash);
        menuPanel.add(btnQLUser);
        menuPanel.add(btnQLLHP);
        menuPanel.add(btnCongNo);
        menuPanel.add(btnBaoCao);
        menuPanel.add(btnImport);

        // Nút Đăng xuất
        JPanel logoutPanel = new JPanel(new BorderLayout()); 
        logoutPanel.setBackground(UIUtils.MIT_RED); 
        logoutPanel.setBorder(new EmptyBorder(20, 0, 20, 0));
        
        JButton btnLogout = createSidebarBtn(7, "Đăng Xuất", false);
        btnLogout.addActionListener(e -> {
            Container parent = this.getParent();
            if (parent != null && parent.getLayout() instanceof CardLayout) {
                ((CardLayout) parent.getLayout()).show(parent, "LOGIN");
            }
        });
        logoutPanel.add(btnLogout, BorderLayout.CENTER);

        sidebar.add(logoPanel, BorderLayout.NORTH); 
        sidebar.add(menuPanel, BorderLayout.CENTER); 
        sidebar.add(logoutPanel, BorderLayout.SOUTH);
        
        rightPanel.add(header, BorderLayout.NORTH); 
        rightPanel.add(contentArea, BorderLayout.CENTER);

        add(sidebar, BorderLayout.WEST); 
        add(rightPanel, BorderLayout.CENTER);

        // THIẾT LẬP TRIGGER ĐỔI HỌC KỲ CHO CÁC MODULE LIÊN QUAN
        cbHocKy.addActionListener(e -> {
            String fullHocKy = (String) cbHocKy.getSelectedItem();
            if (fullHocKy != null) {
                String maHK = fullHocKy.contains("-") ? fullHocKy.split("-")[0].trim() : fullHocKy;
                if (adminDashboardPanel != null) adminDashboardPanel.updateData(maHK);
                if (adminCongNoPanel != null) adminCongNoPanel.updateData(maHK);
                if (adminLopHocPhanPanel != null) adminLopHocPhanPanel.updateData(maHK); 
            }
        });
        if (cbHocKy.getItemCount() > 0) cbHocKy.setSelectedIndex(0);
    }

    private JButton createSidebarBtn(int iconType, String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setIcon(new AdminMenuIcon(iconType, UIUtils.WHITE)); 
        btn.setIconTextGap(15);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14)); 
        btn.setFocusPainted(false); 
        btn.setBorderPainted(false); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); 
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        
        setSidebarBtnStyle(btn, isActive);
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { 
                if (!btn.getBackground().equals(UIUtils.MIT_ORANGE)) btn.setBackground(new Color(160, 20, 20)); 
            }
            public void mouseExited(java.awt.event.MouseEvent evt) { 
                if (!btn.getBackground().equals(UIUtils.MIT_ORANGE)) btn.setBackground(UIUtils.MIT_RED); 
            }
        });
        
        if (iconType != 7) sidebarButtons.add(btn);
        return btn;
    }

    private void setSidebarBtnStyle(JButton btn, boolean isActive) {
        if (isActive) {
            btn.setBackground(UIUtils.MIT_ORANGE); 
            btn.setForeground(UIUtils.WHITE);
            btn.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 5, 0, 0, UIUtils.MIT_YELLOW), new EmptyBorder(12, 20, 12, 15) 
            ));
        } else {
            btn.setBackground(UIUtils.MIT_RED); 
            btn.setForeground(new Color(255, 200, 200));
            btn.setBorder(new EmptyBorder(12, 25, 12, 15)); 
        }
    }

    private void switchTab(JButton activeBtn, String cardName, String title) {
        lblHeaderTitle.setText(title);
        for (JButton btn : sidebarButtons) setSidebarBtnStyle(btn, btn == activeBtn);
        cardLayout.show(contentArea, cardName);
    }

    class LogoIcon implements Icon {
        public int getIconWidth() { return 36; }
        public int getIconHeight() { return 36; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE); g2.fillRoundRect(x, y, 36, 36, 10, 10);
            g2.setColor(UIUtils.MIT_RED); g2.setFont(new Font("Segoe UI", Font.BOLD, 22)); g2.drawString("M", x + 8, y + 26);
            g2.dispose();
        }
    }

    class AdminMenuIcon implements Icon {
        private int type; private Color color;
        public AdminMenuIcon(int type, Color c) { this.type = type; this.color = c; }
        public int getIconWidth() { return 24; }
        public int getIconHeight() { return 24; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(c.getForeground()); 
            g2.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            switch (type) {
                case 1: g2.drawPolygon(new int[]{x+2, x+12, x+22}, new int[]{y+12, y+2, y+12}, 3); g2.drawRect(x+5, y+12, 14, 10); break;
                case 2: g2.drawOval(x+4, y+2, 8, 8); g2.drawArc(x+2, y+12, 12, 10, 0, 180); g2.drawOval(x+14, y+6, 6, 6); g2.drawArc(x+12, y+14, 10, 8, 0, 180); break;
                case 3: g2.drawRoundRect(x+2, y+4, 20, 18, 4, 4); g2.drawLine(x+2, y+10, x+22, y+10); g2.drawLine(x+7, y+2, x+7, y+6); g2.drawLine(x+17, y+2, x+17, y+6); break;
                case 4: g2.drawOval(x+2, y+2, 20, 20); g2.drawString("$", x+8, y+17); break;
                case 5: g2.drawLine(x+2, y+22, x+22, y+22); g2.drawLine(x+2, y+2, x+2, y+22); g2.fillRect(x+6, y+12, 4, 10); g2.fillRect(x+12, y+6, 4, 16); g2.fillRect(x+18, y+16, 4, 6); break;
                case 6: g2.drawRect(x+3, y+10, 18, 12); g2.drawLine(x+12, y+2, x+12, y+14); g2.drawLine(x+8, y+10, x+12, y+14); g2.drawLine(x+16, y+10, x+12, y+14); break;
                case 7: g2.drawRect(x+2, y+2, 12, 20); g2.drawLine(x+10, y+12, x+22, y+12); g2.drawLine(x+18, y+8, x+22, y+12); g2.drawLine(x+18, y+16, x+22, y+12); break;
            }
            g2.dispose();
        }
    }
}