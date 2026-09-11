package ui;

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

public class AdminCongNoPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private DefaultTableModel model;
    private JTable table;
    private JLabel lblTongNoToanTruong;
    
    private JTextField txtMaPhieu, txtTienThu;

    public AdminCongNoPanel() {
        setLayout(new BorderLayout(0, 20));
        setBackground(UIUtils.BG_APP);
        setBorder(new EmptyBorder(5, 0, 0, 0));

        // ========================================================
        // 1. BẢNG DANH SÁCH CÔNG NỢ TOÀN TRƯỜNG
        // ========================================================
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setBackground(Color.WHITE);
        tableWrapper.setBorder(new LineBorder(UIUtils.BORDER, 1, true));

        JPanel tableHeader = new JPanel(new BorderLayout());
        tableHeader.setBackground(Color.WHITE);
        tableHeader.setBorder(new EmptyBorder(12, 20, 12, 20));
        
        JLabel lblTblTitle = new JLabel("Bảng Danh Sách Công Nợ Học Phí Toàn Trường");
        lblTblTitle.setFont(UIUtils.FONT_TITLE);
        lblTblTitle.setForeground(UIUtils.TEXT_MAIN);
        
        lblTongNoToanTruong = new JLabel("Tổng nợ toàn trường: 0 VNĐ");
        lblTongNoToanTruong.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTongNoToanTruong.setForeground(UIUtils.RED_500);

        tableHeader.add(lblTblTitle, BorderLayout.WEST);
        tableHeader.add(lblTongNoToanTruong, BorderLayout.EAST);

        // Thêm cột "Học Kỳ" để phân biệt do đã gom tất cả dữ liệu
        String[] cols = {"Mã Phiếu", "Mã SV", "Họ Tên SV", "Học Kỳ", "Phải Đóng", "Đã Đóng", "Còn Nợ", "Trạng Thái"};
        model = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        UIUtils.styleTable(table);
        table.setShowGrid(true);
        table.setGridColor(UIUtils.BORDER);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        // Căn chỉnh độ rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(7).setPreferredWidth(110);
        
        ZebraRenderer zebra = new ZebraRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(zebra);
        }

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new MatteBorder(1, 0, 0, 0, UIUtils.BORDER));

        tableWrapper.add(tableHeader, BorderLayout.NORTH);
        tableWrapper.add(scroll, BorderLayout.CENTER);

        // ========================================================
        // 2. KHUNG NHẬP LIỆU GHI NHẬN THU TIỀN
        // ========================================================
        JPanel formWrapper = new JPanel(new BorderLayout());
        formWrapper.setBackground(Color.WHITE);
        formWrapper.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIUtils.BORDER, 1, true), new EmptyBorder(10, 15, 10, 15)
        ));

        // Tiêu đề form với gạch chân màu Xanh ngọc
        JPanel formHeader = new JPanel(new BorderLayout());
        formHeader.setBackground(Color.WHITE);
        formHeader.setBorder(new MatteBorder(0, 0, 2, 0, new Color(16, 185, 129))); 
        JLabel lblFormTitle = new JLabel("GHI NHẬN THANH TOÁN HỌC PHÍ");
        lblFormTitle.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblFormTitle.setForeground(new Color(16, 185, 129));
        lblFormTitle.setBorder(new EmptyBorder(0, 0, 8, 0));
        formHeader.add(lblFormTitle, BorderLayout.WEST);

        // Lưới nhập liệu: 1 dòng x 3 cột
        JPanel inputGrid = new JPanel(new GridLayout(1, 3, 20, 5));
        inputGrid.setBackground(Color.WHITE);
        inputGrid.setBorder(new EmptyBorder(15, 0, 10, 0));
        
        txtMaPhieu = UIUtils.createInput();
        txtMaPhieu.setEditable(false); // Chỉ cho chọn từ bảng
        txtMaPhieu.setBackground(UIUtils.BG_APP);
        
        txtTienThu = UIUtils.createInput();

        inputGrid.add(createCompactFormRow("Mã Phiếu Thu (Chọn từ danh sách):", txtMaPhieu));
        inputGrid.add(createCompactFormRow("Số Tiền Thu (VNĐ):", txtTienThu));
        inputGrid.add(new JLabel("")); // Spacer để form đỡ dài

        // ========================================================
        // 3. NÚT XÁC NHẬN MÀU SẮC ĐẬM ĐÀ
        // ========================================================
        JPanel btnGrid = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        btnGrid.setBackground(Color.WHITE);

        JButton btnThuTien = createActionButton("XÁC NHẬN THU TIỀN", new Color(25, 135, 84));

        btnGrid.add(btnThuTien);

        JPanel formContent = new JPanel(new BorderLayout());
        formContent.setBackground(Color.WHITE);
        formContent.add(inputGrid, BorderLayout.CENTER);
        formContent.add(btnGrid, BorderLayout.SOUTH);

        formWrapper.add(formHeader, BorderLayout.NORTH);
        formWrapper.add(formContent, BorderLayout.CENTER);

        // ========================================================
        // 4. GẮN SỰ KIỆN TƯƠNG TÁC
        // ========================================================
        table.getSelectionModel().addListSelectionListener(e -> {
            int r = table.getSelectedRow();
            if(r >= 0 && !e.getValueIsAdjusting()) {
                txtMaPhieu.setText(model.getValueAt(r, 0).toString());
                // Tự động điền số tiền còn nợ vào ô nhập
                String conNoStr = model.getValueAt(r, 6).toString().replaceAll("[^0-9]", "");
                txtTienThu.setText(conNoStr);
            }
        });

        btnThuTien.addActionListener(e -> {
            String maPhieu = txtMaPhieu.getText().trim();
            String tienThuStr = txtTienThu.getText().trim().replaceAll("[^0-9]", "");

            if (maPhieu.isEmpty() || tienThuStr.isEmpty() || tienThuStr.equals("0")) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu nợ và nhập số tiền hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double tienThu = Double.parseDouble(tienThuStr);

            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận thu " + String.format("%,.0f", tienThu) + " VNĐ cho phiếu " + maPhieu + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // UPDATE số tiền đã đóng và tự động kiểm tra Trạng thái xem đã đủ hay chưa
                String sql = "UPDATE CONG_NO_HOC_PHI SET SoTienDaDong = SoTienDaDong + ?, TrangThai = CASE WHEN TongTienPhaiDong <= SoTienDaDong + ? THEN N'Đã hoàn thành' ELSE N'Còn nợ' END WHERE MaPhieu = ?";
                try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setDouble(1, tienThu);
                    ps.setDouble(2, tienThu);
                    ps.setString(3, maPhieu);
                    ps.executeUpdate();
                    
                    JOptionPane.showMessageDialog(this, "Thu tiền thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    txtMaPhieu.setText("");
                    txtTienThu.setText("");
                    updateData(""); // Load lại toàn bộ danh sách
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi Database", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(tableWrapper, BorderLayout.CENTER);
        add(formWrapper, BorderLayout.SOUTH);
    }

    // ==========================================
    // LOGIC DATABASE (LẤY TẤT CẢ DỮ LIỆU)
    // ==========================================
    public void updateData(String maHK) {
        // Bỏ việc lọc theo maHK, lấy toàn bộ danh sách từ CSDL
        model.setRowCount(0);
        double tongNo = 0;

        String sql = "SELECT c.MaPhieu, c.MaSV, s.HoTen, c.MaHK, c.TongTienPhaiDong, c.SoTienDaDong, c.TrangThai " +
                     "FROM CONG_NO_HOC_PHI c JOIN SINH_VIEN s ON c.MaSV = s.MaSV " +
                     "ORDER BY c.MaHK DESC, c.MaPhieu ASC"; // Lấy tất cả và sắp xếp theo Học kỳ
                     
        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                double phaiDong = rs.getDouble("TongTienPhaiDong");
                double daDong = rs.getDouble("SoTienDaDong");
                double no = phaiDong - daDong;
                
                // Chỉ cộng dồn những phiếu còn nợ
                if (no > 0) tongNo += no;
                
                model.addRow(new Object[]{
                    rs.getString("MaPhieu"), rs.getString("MaSV"), rs.getString("HoTen"), rs.getString("MaHK"),
                    String.format("%,.0f đ", phaiDong), String.format("%,.0f đ", daDong), String.format("%,.0f đ", Math.max(no, 0)), rs.getString("TrangThai")
                });
            }
            lblTongNoToanTruong.setText("Tổng nợ toàn trường: " + String.format("%,.0f VNĐ", tongNo));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==========================================
    // UI UTILS: THIẾT KẾ NÚT & Ô NHẬP LIỆU
    // ==========================================
    private JPanel createCompactFormRow(String labelText, JComponent input) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new EmptyBorder(0, 0, 5, 0)); 
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(71, 85, 105)); 
        lbl.setBorder(new EmptyBorder(0, 0, 3, 0)); 
        
        wrapper.add(lbl, BorderLayout.NORTH);
        wrapper.add(input, BorderLayout.CENTER);
        return wrapper;
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI()); 
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(bgColor.darker(), 1, true),
            new EmptyBorder(10, 25, 10, 25)
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
            
            // Xử lý màu chữ cho cột Trạng thái
            if (column == 7 && value != null) {
                String text = value.toString();
                if (text.equals("Đã hoàn thành")) {
                    c.setForeground(UIUtils.GREEN_500);
                    c.setFont(new Font("Segoe UI", Font.BOLD, 14));
                } else {
                    c.setForeground(UIUtils.RED_500);
                    c.setFont(new Font("Segoe UI", Font.BOLD, 14));
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
}