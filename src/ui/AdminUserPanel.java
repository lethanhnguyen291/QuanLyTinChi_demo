package ui;

import service.StudentManagerService;
import utils.UIUtils;
import config.DBConnect;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminUserPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JButton btnTabSV, btnTabGV;

    // --- COMPONENTS SINH VIÊN ---
    private DefaultTableModel modelSV;
    private JTable tblSV;
    private JTextField txtMaSV, txtHoTenSV, txtNgaySinhSV, txtSdtSV, txtEmailSV, txtLopSV, txtNganhSV;
    private JComboBox<String> cbGioiTinhSV, cbTrangThaiSV;

    // --- COMPONENTS GIẢNG VIÊN ---
    private DefaultTableModel modelGV;
    private JTable tblGV;
    private JTextField txtMaGV, txtHoTenGV, txtSdtGV, txtEmailGV, txtKhoaGV;
    private JComboBox<String> cbGioiTinhGV, cbHocViGV;

    public AdminUserPanel(StudentManagerService service) {
        setLayout(new BorderLayout(0, 15)); // Giảm khoảng cách giữa Tab và Nội dung
        setBackground(UIUtils.BG_APP);
        setBorder(new EmptyBorder(5, 0, 0, 0));

        // ==========================================
        // 1. THANH ĐIỀU HƯỚNG (TOGGLE TABS)
        // ==========================================
        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        togglePanel.setBackground(UIUtils.BG_APP);
        togglePanel.setBorder(new EmptyBorder(0, 0, 5, 0));
        
        btnTabSV = createToggleBtn("DANH SÁCH SINH VIÊN", true);
        btnTabGV = createToggleBtn("DANH SÁCH GIẢNG VIÊN", false);
        
        togglePanel.add(btnTabSV);
        togglePanel.add(btnTabGV);

        // ==========================================
        // 2. KHU VỰC NỘI DUNG (CARD LAYOUT)
        // ==========================================
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UIUtils.BG_APP);
        
        contentPanel.add(createSinhVienView(), "SV");
        contentPanel.add(createGiangVienView(), "GV");

        // Sự kiện chuyển Tab mượt mà
        btnTabSV.addActionListener(e -> {
            setToggleStyle(btnTabSV, true); 
            setToggleStyle(btnTabGV, false);
            cardLayout.show(contentPanel, "SV");
            loadDataSV();
        });
        btnTabGV.addActionListener(e -> {
            setToggleStyle(btnTabGV, true); 
            setToggleStyle(btnTabSV, false);
            cardLayout.show(contentPanel, "GV");
            loadDataGV();
        });

        add(togglePanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        // Load dữ liệu mặc định ban đầu
        loadDataSV(); 
    }

    // ========================================================
    // VIEW 1: QUẢN LÝ SINH VIÊN
    // ========================================================
    private JPanel createSinhVienView() {
        JPanel pnl = new JPanel(new BorderLayout(0, 15));
        pnl.setBackground(UIUtils.BG_APP);

        // --- 1. BẢNG DỮ LIỆU ---
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(Color.WHITE);
        tableWrapper.setBorder(new LineBorder(UIUtils.BORDER, 1, true));

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setBorder(new EmptyBorder(12, 20, 12, 20));
        JLabel lblTblTitle = new JLabel("Bảng Danh Sách Sinh Viên Toàn Trường");
        lblTblTitle.setFont(UIUtils.FONT_TITLE);
        lblTblTitle.setForeground(UIUtils.TEXT_MAIN);
        tableHeader.add(lblTblTitle, BorderLayout.WEST);

        String[] cols = {"Mã SV", "Họ Tên", "Giới Tính", "Ngày Sinh", "SĐT", "Email", "Lớp", "Ngành", "Trạng Thái"};
        modelSV = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblSV = new JTable(modelSV);
        UIUtils.styleTable(tblSV);
        tblSV.setShowGrid(true);
        tblSV.setGridColor(UIUtils.BORDER);
        tblSV.setIntercellSpacing(new Dimension(1, 1));
        
        ZebraRenderer zebra = new ZebraRenderer();
        for (int i = 0; i < tblSV.getColumnCount(); i++) tblSV.getColumnModel().getColumn(i).setCellRenderer(zebra);

        JScrollPane scroll = new JScrollPane(tblSV);
        scroll.setBorder(new MatteBorder(1, 0, 0, 0, UIUtils.BORDER));

        tableWrapper.add(tableHeader, BorderLayout.NORTH);
        tableWrapper.add(scroll, BorderLayout.CENTER);

        // --- 2. KHUNG NHẬP LIỆU THU GỌN (CHỈ CÒN 2 DÒNG) ---
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(Color.WHITE);
        formWrapper.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIUtils.BORDER, 1, true), new EmptyBorder(10, 15, 10, 15) // Giảm padding cực mỏng
        ));

        // Tiêu đề form với gạch chân màu Xanh
        JPanel formHeader = new JPanel(new BorderLayout());
        formHeader.setBackground(Color.WHITE);
        formHeader.setBorder(new MatteBorder(0, 0, 2, 0, new Color(37, 99, 235))); 
        JLabel lblFormTitle = new JLabel("THÔNG TIN CHI TIẾT SINH VIÊN");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblFormTitle.setForeground(new Color(37, 99, 235));
        lblFormTitle.setBorder(new EmptyBorder(0, 0, 8, 0));
        formHeader.add(lblFormTitle, BorderLayout.WEST);

        // Lưới nhập liệu: 2 dòng x 5 cột = 10 ô (Mỏng, rộng ngang)
        JPanel inputGrid = new JPanel(new GridLayout(2, 5, 15, 5));
        inputGrid.setBackground(Color.WHITE);
        inputGrid.setBorder(new EmptyBorder(10, 0, 5, 0));
        
        txtMaSV = UIUtils.createInput();
        txtHoTenSV = UIUtils.createInput();
        cbGioiTinhSV = new JComboBox<>(new String[]{"Nam", "Nữ"}); cbGioiTinhSV.setFont(UIUtils.FONT_NORMAL); cbGioiTinhSV.setBackground(Color.WHITE);
        txtNgaySinhSV = UIUtils.createInput(); 
        txtSdtSV = UIUtils.createInput();
        txtEmailSV = UIUtils.createInput();
        cbTrangThaiSV = new JComboBox<>(new String[]{"Đang học", "Bảo lưu", "Đã tốt nghiệp", "Thôi học"}); cbTrangThaiSV.setFont(UIUtils.FONT_NORMAL); cbTrangThaiSV.setBackground(Color.WHITE);
        txtLopSV = UIUtils.createInput();
        txtNganhSV = UIUtils.createInput();

        inputGrid.add(createCompactFormRow("Mã Sinh Viên:", txtMaSV));
        inputGrid.add(createCompactFormRow("Họ và Tên:", txtHoTenSV));
        inputGrid.add(createCompactFormRow("Giới Tính:", cbGioiTinhSV));
        inputGrid.add(createCompactFormRow("Ngày Sinh:", txtNgaySinhSV));
        inputGrid.add(createCompactFormRow("Số Điện Thoại:", txtSdtSV));

        inputGrid.add(createCompactFormRow("Email:", txtEmailSV));
        inputGrid.add(createCompactFormRow("Trạng Thái:", cbTrangThaiSV));
        inputGrid.add(createCompactFormRow("Mã Lớp:", txtLopSV));
        inputGrid.add(createCompactFormRow("Mã Ngành:", txtNganhSV));
        inputGrid.add(new JLabel("")); // Ô trống lấp đầy

        // --- 3. CÁC NÚT HÀNH ĐỘNG MÀU SẮC ĐẬM ĐÀ ---
        JPanel btnGrid = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnGrid.setBackground(Color.WHITE);

        JButton btnClear = createActionButton("LÀM MỚI", new Color(108, 117, 125)); // Xám đậm
        JButton btnAdd = createActionButton("THÊM MỚI", new Color(25, 135, 84)); // Xanh lá tươi
        JButton btnUpdate = createActionButton("CẬP NHẬT", new Color(13, 110, 253)); // Xanh dương tươi
        JButton btnDel = createActionButton("XÓA BỎ", new Color(220, 53, 69)); // Đỏ tươi

        btnGrid.add(btnClear); btnGrid.add(btnAdd); btnGrid.add(btnUpdate); btnGrid.add(btnDel);

        JPanel formContent = new JPanel(new BorderLayout());
        formContent.setBackground(Color.WHITE);
        formContent.add(inputGrid, BorderLayout.CENTER);
        formContent.add(btnGrid, BorderLayout.SOUTH);

        formWrapper.add(formHeader, BorderLayout.NORTH);
        formWrapper.add(formContent, BorderLayout.CENTER);

        // --- GẮN SỰ KIỆN CỦA BẢNG VÀ NÚT BẤM ---
        tblSV.getSelectionModel().addListSelectionListener(e -> {
            int r = tblSV.getSelectedRow();
            if(r >= 0 && !e.getValueIsAdjusting()) {
                txtMaSV.setText(modelSV.getValueAt(r, 0) != null ? modelSV.getValueAt(r, 0).toString() : "");
                txtHoTenSV.setText(modelSV.getValueAt(r, 1) != null ? modelSV.getValueAt(r, 1).toString() : "");
                cbGioiTinhSV.setSelectedItem(modelSV.getValueAt(r, 2) != null ? modelSV.getValueAt(r, 2).toString() : "Nam");
                txtNgaySinhSV.setText(modelSV.getValueAt(r, 3) != null ? modelSV.getValueAt(r, 3).toString() : "");
                txtSdtSV.setText(modelSV.getValueAt(r, 4) != null ? modelSV.getValueAt(r, 4).toString() : "");
                txtEmailSV.setText(modelSV.getValueAt(r, 5) != null ? modelSV.getValueAt(r, 5).toString() : "");
                txtLopSV.setText(modelSV.getValueAt(r, 6) != null ? modelSV.getValueAt(r, 6).toString() : "");
                txtNganhSV.setText(modelSV.getValueAt(r, 7) != null ? modelSV.getValueAt(r, 7).toString() : "");
                cbTrangThaiSV.setSelectedItem(modelSV.getValueAt(r, 8) != null ? modelSV.getValueAt(r, 8).toString() : "Đang học");
            }
        });

        btnClear.addActionListener(e -> clearFormSV());
        
        btnAdd.addActionListener(e -> {
            String sql = "INSERT INTO SINH_VIEN (MaSV, HoTen, GioiTinh, NgaySinh, SoDienThoai, Email, TrangThaiHocTap, MaCTDT, MaLop, DatChuanNgoaiNgu) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
            executeDB(sql, "Thêm sinh viên", txtMaSV.getText(), txtHoTenSV.getText(), cbGioiTinhSV.getSelectedItem(), txtNgaySinhSV.getText(), txtSdtSV.getText(), txtEmailSV.getText(), cbTrangThaiSV.getSelectedItem(), txtNganhSV.getText(), txtLopSV.getText());
            loadDataSV();
        });

        btnUpdate.addActionListener(e -> {
            String sql = "UPDATE SINH_VIEN SET HoTen=?, GioiTinh=?, NgaySinh=?, SoDienThoai=?, Email=?, TrangThaiHocTap=?, MaCTDT=?, MaLop=? WHERE MaSV=?";
            executeDB(sql, "Cập nhật sinh viên", txtHoTenSV.getText(), cbGioiTinhSV.getSelectedItem(), txtNgaySinhSV.getText(), txtSdtSV.getText(), txtEmailSV.getText(), cbTrangThaiSV.getSelectedItem(), txtNganhSV.getText(), txtLopSV.getText(), txtMaSV.getText());
            loadDataSV();
        });

        btnDel.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa Sinh viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM SINH_VIEN WHERE MaSV=?";
                executeDB(sql, "Xóa sinh viên", txtMaSV.getText());
                loadDataSV();
                clearFormSV();
            }
        });

        pnl.add(tableWrapper, BorderLayout.CENTER);
        pnl.add(formWrapper, BorderLayout.SOUTH);
        return pnl;
    }

    // ========================================================
    // VIEW 2: QUẢN LÝ GIẢNG VIÊN
    // ========================================================
    private JPanel createGiangVienView() {
        JPanel pnl = new JPanel(new BorderLayout(0, 15));
        pnl.setBackground(UIUtils.BG_APP);

        // --- 1. BẢNG DỮ LIỆU ---
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(Color.WHITE);
        tableWrapper.setBorder(new LineBorder(UIUtils.BORDER, 1, true));

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setBorder(new EmptyBorder(12, 20, 12, 20));
        JLabel lblTblTitle = new JLabel("Bảng Danh Sách Giảng Viên Khoa");
        lblTblTitle.setFont(UIUtils.FONT_TITLE);
        lblTblTitle.setForeground(UIUtils.TEXT_MAIN);
        tableHeader.add(lblTblTitle, BorderLayout.WEST);

        String[] cols = {"Mã GV", "Họ Tên", "Giới Tính", "Học Vị", "SĐT", "Email", "Mã Khoa"};
        modelGV = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblGV = new JTable(modelGV);
        UIUtils.styleTable(tblGV);
        tblGV.setShowGrid(true);
        tblGV.setGridColor(UIUtils.BORDER);
        tblGV.setIntercellSpacing(new Dimension(1, 1));
        
        ZebraRenderer zebra = new ZebraRenderer();
        for (int i = 0; i < tblGV.getColumnCount(); i++) tblGV.getColumnModel().getColumn(i).setCellRenderer(zebra);

        JScrollPane scroll = new JScrollPane(tblGV);
        scroll.setBorder(new MatteBorder(1, 0, 0, 0, UIUtils.BORDER));

        tableWrapper.add(tableHeader, BorderLayout.NORTH);
        tableWrapper.add(scroll, BorderLayout.CENTER);

        // --- 2. KHUNG NHẬP LIỆU THU GỌN (CHỈ CÒN 2 DÒNG) ---
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(Color.WHITE);
        formWrapper.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIUtils.BORDER, 1, true), new EmptyBorder(10, 15, 10, 15) // Giảm padding
        ));

        // Tiêu đề form với gạch chân màu Đỏ mận
        JPanel formHeader = new JPanel(new BorderLayout());
        formHeader.setBackground(Color.WHITE);
        formHeader.setBorder(new MatteBorder(0, 0, 2, 0, new Color(153, 27, 27))); 
        JLabel lblFormTitle = new JLabel("THÔNG TIN CHI TIẾT GIẢNG VIÊN");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblFormTitle.setForeground(new Color(153, 27, 27));
        lblFormTitle.setBorder(new EmptyBorder(0, 0, 8, 0));
        formHeader.add(lblFormTitle, BorderLayout.WEST);

        // Lưới nhập liệu: 2 dòng x 4 cột = 8 ô
        JPanel inputGrid = new JPanel(new GridLayout(2, 4, 15, 5));
        inputGrid.setBackground(Color.WHITE);
        inputGrid.setBorder(new EmptyBorder(10, 0, 5, 0));
        
        txtMaGV = UIUtils.createInput();
        txtHoTenGV = UIUtils.createInput();
        cbGioiTinhGV = new JComboBox<>(new String[]{"Nam", "Nữ"}); cbGioiTinhGV.setFont(UIUtils.FONT_NORMAL); cbGioiTinhGV.setBackground(Color.WHITE);
        cbHocViGV = new JComboBox<>(new String[]{"Cử nhân", "Thạc sĩ", "Tiến sĩ", "PGS.TS", "GS.TS"}); cbHocViGV.setFont(UIUtils.FONT_NORMAL); cbHocViGV.setBackground(Color.WHITE);
        txtSdtGV = UIUtils.createInput();
        txtEmailGV = UIUtils.createInput();
        txtKhoaGV = UIUtils.createInput();

        inputGrid.add(createCompactFormRow("Mã Giảng Viên:", txtMaGV));
        inputGrid.add(createCompactFormRow("Họ và Tên:", txtHoTenGV));
        inputGrid.add(createCompactFormRow("Giới Tính:", cbGioiTinhGV));
        inputGrid.add(createCompactFormRow("Học Vị:", cbHocViGV));

        inputGrid.add(createCompactFormRow("Số Điện Thoại:", txtSdtGV));
        inputGrid.add(createCompactFormRow("Email:", txtEmailGV));
        inputGrid.add(createCompactFormRow("Mã Khoa:", txtKhoaGV));
        inputGrid.add(new JLabel("")); // Spacer lấp đầy lưới

        // --- 3. CÁC NÚT HÀNH ĐỘNG MÀU SẮC ĐẬM ĐÀ ---
        JPanel btnGrid = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnGrid.setBackground(Color.WHITE);

        JButton btnClear = createActionButton("LÀM MỚI", new Color(108, 117, 125));
        JButton btnAdd = createActionButton("THÊM MỚI", new Color(25, 135, 84));
        JButton btnUpdate = createActionButton("CẬP NHẬT", new Color(13, 110, 253));
        JButton btnDel = createActionButton("XÓA BỎ", new Color(220, 53, 69));

        btnGrid.add(btnClear); btnGrid.add(btnAdd); btnGrid.add(btnUpdate); btnGrid.add(btnDel);

        JPanel formContent = new JPanel(new BorderLayout());
        formContent.setBackground(Color.WHITE);
        formContent.add(inputGrid, BorderLayout.CENTER);
        formContent.add(btnGrid, BorderLayout.SOUTH);

        formWrapper.add(formHeader, BorderLayout.NORTH);
        formWrapper.add(formContent, BorderLayout.CENTER);

        // --- GẮN SỰ KIỆN CỦA BẢNG VÀ NÚT BẤM ---
        tblGV.getSelectionModel().addListSelectionListener(e -> {
            int r = tblGV.getSelectedRow();
            if(r >= 0 && !e.getValueIsAdjusting()) {
                txtMaGV.setText(modelGV.getValueAt(r, 0) != null ? modelGV.getValueAt(r, 0).toString() : "");
                txtHoTenGV.setText(modelGV.getValueAt(r, 1) != null ? modelGV.getValueAt(r, 1).toString() : "");
                cbGioiTinhGV.setSelectedItem(modelGV.getValueAt(r, 2) != null ? modelGV.getValueAt(r, 2).toString() : "Nam");
                cbHocViGV.setSelectedItem(modelGV.getValueAt(r, 3) != null ? modelGV.getValueAt(r, 3).toString() : "Thạc sĩ");
                txtSdtGV.setText(modelGV.getValueAt(r, 4) != null ? modelGV.getValueAt(r, 4).toString() : "");
                txtEmailGV.setText(modelGV.getValueAt(r, 5) != null ? modelGV.getValueAt(r, 5).toString() : "");
                txtKhoaGV.setText(modelGV.getValueAt(r, 6) != null ? modelGV.getValueAt(r, 6).toString() : "");
            }
        });

        btnClear.addActionListener(e -> clearFormGV());
        
        btnAdd.addActionListener(e -> {
            String sql = "INSERT INTO GIANG_VIEN (MaGV, HoTen, GioiTinh, HocVi, SoDienThoai, Email, MaKhoa) VALUES (?, ?, ?, ?, ?, ?, ?)";
            executeDB(sql, "Thêm giảng viên", txtMaGV.getText(), txtHoTenGV.getText(), cbGioiTinhGV.getSelectedItem(), cbHocViGV.getSelectedItem(), txtSdtGV.getText(), txtEmailGV.getText(), txtKhoaGV.getText());
            loadDataGV();
        });

        btnUpdate.addActionListener(e -> {
            String sql = "UPDATE GIANG_VIEN SET HoTen=?, GioiTinh=?, HocVi=?, SoDienThoai=?, Email=?, MaKhoa=? WHERE MaGV=?";
            executeDB(sql, "Cập nhật giảng viên", txtHoTenGV.getText(), cbGioiTinhGV.getSelectedItem(), cbHocViGV.getSelectedItem(), txtSdtGV.getText(), txtEmailGV.getText(), txtKhoaGV.getText(), txtMaGV.getText());
            loadDataGV();
        });

        btnDel.addActionListener(e -> {
            if(JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa Giảng viên này?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                String sql = "DELETE FROM GIANG_VIEN WHERE MaGV=?";
                executeDB(sql, "Xóa giảng viên", txtMaGV.getText());
                loadDataGV();
                clearFormGV();
            }
        });

        pnl.add(tableWrapper, BorderLayout.CENTER);
        pnl.add(formWrapper, BorderLayout.SOUTH);
        return pnl;
    }

    // ==========================================
    // LOGIC DATABASE
    // ==========================================
    private void loadDataSV() {
        modelSV.setRowCount(0);
        String sql = "SELECT MaSV, HoTen, GioiTinh, NgaySinh, SoDienThoai, Email, MaLop, MaCTDT, TrangThaiHocTap FROM SINH_VIEN";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                modelSV.addRow(new Object[]{
                    rs.getString("MaSV"), rs.getString("HoTen"), rs.getString("GioiTinh"), rs.getString("NgaySinh"),
                    rs.getString("SoDienThoai"), rs.getString("Email"), rs.getString("MaLop"), rs.getString("MaCTDT"), rs.getString("TrangThaiHocTap")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void loadDataGV() {
        modelGV.setRowCount(0);
        String sql = "SELECT MaGV, HoTen, GioiTinh, HocVi, SoDienThoai, Email, MaKhoa FROM GIANG_VIEN";
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                modelGV.addRow(new Object[]{
                    rs.getString("MaGV"), rs.getString("HoTen"), rs.getString("GioiTinh"), rs.getString("HocVi"),
                    rs.getString("SoDienThoai"), rs.getString("Email"), rs.getString("MaKhoa")
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void executeDB(String sql, String actionName, Object... params) {
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, actionName + " thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi " + actionName + ": " + e.getMessage(), "Lỗi Database", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFormSV() {
        txtMaSV.setText(""); txtHoTenSV.setText(""); txtNgaySinhSV.setText(""); txtSdtSV.setText(""); 
        txtEmailSV.setText(""); txtLopSV.setText(""); txtNganhSV.setText("");
        cbGioiTinhSV.setSelectedIndex(0); cbTrangThaiSV.setSelectedIndex(0);
        tblSV.clearSelection();
    }

    private void clearFormGV() {
        txtMaGV.setText(""); txtHoTenGV.setText(""); txtSdtGV.setText(""); txtEmailGV.setText(""); txtKhoaGV.setText("");
        cbGioiTinhGV.setSelectedIndex(0); cbHocViGV.setSelectedIndex(0);
        tblGV.clearSelection();
    }

    // ==========================================
    // UI UTILS: CẤU TRÚC LẠI NÚT VÀ Ô NHẬP LIỆU THU GỌN
    // ==========================================
    
    // Tạo 1 dòng nhập liệu tiết kiệm không gian chiều cao
    private JPanel createCompactFormRow(String labelText, JComponent input) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new EmptyBorder(0, 0, 5, 0)); // Bottom margin rất mỏng
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(71, 85, 105)); // Màu ghi xám
        lbl.setBorder(new EmptyBorder(0, 0, 3, 0)); 
        
        wrapper.add(lbl, BorderLayout.NORTH);
        wrapper.add(input, BorderLayout.CENTER);
        return wrapper;
    }

    private JButton createToggleBtn(String text, boolean isActive) {
        JButton btn = new JButton(text);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI()); // XÓA LỚP PHỦ CỦA WINDOWS Ở ĐÂY
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        setToggleStyle(btn, isActive);
        return btn;
    }

    private void setToggleStyle(JButton btn, boolean isActive) {
        if (isActive) {
            btn.setBackground(new Color(37, 99, 235)); 
            btn.setForeground(Color.WHITE);
            btn.setBorder(new EmptyBorder(12, 30, 12, 30));
        } else {
            btn.setBackground(Color.WHITE);
            btn.setForeground(UIUtils.TEXT_MUTED);
            btn.setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(1, 1, 1, 1, UIUtils.BORDER), new EmptyBorder(11, 29, 11, 29)
            ));
        }
    }

    // Tạo nút bấm với màu sắc cực kỳ rực rỡ và bắt mắt
    private JButton createActionButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI()); // Xóa lớp phủ nhạt màu của Windows
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(bgColor.darker(), 1, true),
            new EmptyBorder(8, 20, 8, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bgColor.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bgColor); }
        });
        return btn;
    }

    class ZebraRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 252));
            } else {
                c.setBackground(UIUtils.MIT_RED_LIGHT);
            }
            c.setForeground(UIUtils.TEXT_MAIN);
            setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 1, UIUtils.BORDER), new EmptyBorder(0, 15, 0, 15)
            ));
            return c;
        }
    }
}