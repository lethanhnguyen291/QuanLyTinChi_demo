import service.StudentManagerService;
import ui.AdminPanel;
import ui.StudentPanel;
import utils.UIUtils;
import config.DBConnect;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class App {
    private static CardLayout rootLayout;
    private static JPanel rootPanel;
    private static StudentManagerService service;

    public static void main(String[] args) {
        // Áp dụng LookAndFeel hệ thống cho đẹp hơn
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception e) {}

        service = new StudentManagerService();

        JFrame frame = new JFrame("Hệ Thống Quản Lý Đào Tạo - MIT PORTAL");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600); // Kích thước cửa sổ lúc ở màn hình đăng nhập
        frame.setLocationRelativeTo(null);

        rootLayout = new CardLayout();
        rootPanel = new JPanel(rootLayout);

        // Nạp trang Đăng Nhập làm trang mặc định
        rootPanel.add(createLoginPanel(frame), "LOGIN");

        frame.add(rootPanel);
        frame.setVisible(true);
    }

    private static JPanel createLoginPanel(JFrame frame) {
        JPanel loginMain = new JPanel(new GridBagLayout());
        loginMain.setBackground(UIUtils.BG_APP);

        // KHUNG ĐĂNG NHẬP (TẤT CẢ NẰM TRONG 1 Ô)
        JPanel loginBox = new JPanel(new GridBagLayout());
        loginBox.setBackground(Color.WHITE);
        loginBox.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIUtils.BORDER, 1, true),
            new EmptyBorder(40, 50, 40, 50) 
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        // 1. Logo & Tên trường
        gbc.insets = new Insets(0, 0, 10, 0);
        JLabel logoIcon = new JLabel("🎓 MIT", SwingConstants.CENTER);
        logoIcon.setFont(new Font("Segoe UI Emoji", Font.BOLD, 45));
        logoIcon.setForeground(UIUtils.MIT_RED);
        loginBox.add(logoIcon, gbc);

        // 2. Tiêu đề
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 30, 0);
        JLabel lblTitle = new JLabel("Cổng thông tin Sinh Viên", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(UIUtils.TEXT_MAIN);
        loginBox.add(lblTitle, gbc);

        // Kiểu viền đẹp cho các ô nhập liệu
        javax.swing.border.Border inputBorder = BorderFactory.createCompoundBorder(
            new LineBorder(UIUtils.BORDER, 1, true),
            new EmptyBorder(10, 15, 10, 15) 
        );

        // 3. Vai trò đăng nhập
        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 5, 0);
        JLabel lblRole = new JLabel("Vai trò đăng nhập:");
        lblRole.setFont(UIUtils.FONT_BOLD);
        lblRole.setForeground(UIUtils.TEXT_MAIN);
        loginBox.add(lblRole, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);
        JComboBox<String> cbRole = new JComboBox<>(new String[]{"Sinh Viên", "Cán Bộ Đào Tạo"});
        cbRole.setFont(UIUtils.FONT_NORMAL);
        cbRole.setPreferredSize(new Dimension(350, 45)); 
        cbRole.setBackground(Color.WHITE);
        cbRole.setBorder(inputBorder);
        loginBox.add(cbRole, gbc);

        // 4. Ô nhập Tài khoản
        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 5, 0);
        JLabel lblUser = new JLabel("Tài khoản (Mã SV / Mã CB):");
        lblUser.setFont(UIUtils.FONT_BOLD);
        lblUser.setForeground(UIUtils.TEXT_MAIN);
        loginBox.add(lblUser, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 15, 0);
        JTextField txtUser = new JTextField();
        txtUser.setFont(UIUtils.FONT_NORMAL);
        txtUser.setPreferredSize(new Dimension(350, 45));
        txtUser.setBorder(inputBorder);
        loginBox.add(txtUser, gbc);

        // 5. Ô nhập Mật khẩu
        gbc.gridy++;
        gbc.insets = new Insets(5, 0, 5, 0);
        JLabel lblPass = new JLabel("Mật khẩu:");
        lblPass.setFont(UIUtils.FONT_BOLD);
        lblPass.setForeground(UIUtils.TEXT_MAIN);
        loginBox.add(lblPass, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 0, 25, 0);
        JPasswordField txtPass = new JPasswordField();
        txtPass.setFont(UIUtils.FONT_NORMAL);
        txtPass.setPreferredSize(new Dimension(350, 45));
        txtPass.setBorder(inputBorder);
        loginBox.add(txtPass, gbc);

        // 6. Nút Đăng Nhập
        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 0, 0);
        JButton btnLogin = UIUtils.createPrimaryBtn("ĐĂNG NHẬP");
        btnLogin.setPreferredSize(new Dimension(350, 45));
        loginBox.add(btnLogin, gbc);

        // ==========================================
        // XỬ LÝ SỰ KIỆN ĐĂNG NHẬP
        // ==========================================
        btnLogin.addActionListener(e -> {
            String role = (String) cbRole.getSelectedItem();
            String user = txtUser.getText().trim();
            String pass = new String(txtPass.getPassword()).trim();

            if (user.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Vui lòng nhập đầy đủ tài khoản và mật khẩu!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (authenticateDB(role, user, pass)) {
                
                // --- BỔ SUNG FIX 2: Xóa dữ liệu các trang cũ để tránh bị lỗi trắng thông tin (Bóng ma CardLayout) ---
                while (rootPanel.getComponentCount() > 1) {
                    rootPanel.remove(1);
                }
                
                if (role.equals("Sinh Viên")) {
                    // --- BỔ SUNG FIX 3: Luôn in hoa Mã SV để lấy dữ liệu chuẩn xác từ Database ---
                    String maSV = user.toUpperCase();
                    String panelName = "STUDENT_" + maSV;
                    rootPanel.add(new StudentPanel(service, maSV), panelName);
                    rootLayout.show(rootPanel, panelName);
                } else {
                    rootPanel.add(new AdminPanel(service), "ADMIN");
                    rootLayout.show(rootPanel, "ADMIN");
                }
                txtUser.setText("");
                txtPass.setText("");
                
                // Set lại kích thước phù hợp và tự động căn giữa lại sau khi đăng nhập
                frame.setSize(1200, 750); 
                frame.setLocationRelativeTo(null); 
            } else {
                JOptionPane.showMessageDialog(frame, "Sai tài khoản hoặc mật khẩu! (Hoặc mã sinh viên không tồn tại trong hệ thống)", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Hỗ trợ ấn Enter để đăng nhập
        txtPass.addActionListener(e -> btnLogin.doClick());

        loginMain.add(loginBox);
        return loginMain;
    }

    private static boolean authenticateDB(String role, String username, String password) {
        // Tài khoản Cán Bộ Đào Tạo: Fix cứng một tài khoản Admin duy nhất theo yêu cầu
        if (role.equals("Cán Bộ Đào Tạo")) {
            return username.equals("admin123") && password.equals("123");
        }

        // Tài khoản Sinh Viên: Quét trong CSDL xem có tồn tại Mã SV này không
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return username.equals(password); // Chạy tạm nếu DB chưa bật
            
            String sql = "SELECT * FROM SINH_VIEN WHERE MaSV = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    // --- BỔ SUNG FIX 1: Bắt buộc mật khẩu Sinh viên phải là "123" ---
                    if (password.equals("123")) {
                        return true; 
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; 
    }
}