package ui;

import service.StudentManagerService;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class CongNoPanel extends JPanel {
    private StudentManagerService service;
    private String maSV;
    
    private DefaultTableModel tableModel;
    private JTable table;
    private JButton btnThanhToan;
    private JLabel lblTongNo;

    // Các màu sắc nút bấm đã được làm rực rỡ hơn (Solid Colors)
    private final Color COLOR_PAY_ACTIVE = new Color(13, 110, 253);   // Xanh dương tươi (Bootstrap Blue)
    private final Color COLOR_PAY_DONE = new Color(25, 135, 84);      // Xanh lá đậm (Bootstrap Green)
    private final Color COLOR_PAY_DISABLED = new Color(156, 163, 175); // Xám

    public CongNoPanel(StudentManagerService service, String maSV) {
        this.service = service;
        this.maSV = maSV;

        setLayout(new BorderLayout(0, 20));
        setBackground(UIUtils.BG_APP);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // ==========================================
        // KHÚC TRÊN: TIÊU ĐỀ VÀ THỐNG KÊ NHANH
        // ==========================================
      JPanel topPanel = new JPanel(new BorderLayout());
        
        // 👉 1. Đổi màu nền của topPanel thành Xanh nhạt
        topPanel.setBackground(new Color(239, 246, 255));
        topPanel.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(191, 219, 254)), // Viền dưới nổi nhẹ
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel("Theo dõi thanh toán học phí");
        lblTitle.setFont(UIUtils.FONT_TITLE);
        
        // 👉 2. Đổi màu chữ Tiêu đề thành Xanh đậm
        lblTitle.setForeground(new Color(30, 64, 175));

        lblTongNo = new JLabel("Tổng nợ hiện tại: 0 VNĐ");
        lblTongNo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTongNo.setForeground(UIUtils.RED_500); // Giữ nguyên màu Đỏ cảnh báo nợ

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(lblTongNo, BorderLayout.EAST);

        // ==========================================
        // KHÚC GIỮA: BẢNG DANH SÁCH CÔNG NỢ TỪNG KỲ
        // ==========================================
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(UIUtils.WHITE);
        tablePanel.setBorder(new LineBorder(UIUtils.BORDER, 1, true));

        tableModel = new DefaultTableModel(new String[]{
                "Mã HK", "Học Kỳ", "Tổng phải đóng (VNĐ)", "Đã đóng (VNĐ)", "Còn nợ (VNĐ)", "Trạng Thái"
        }, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        
        // =========================================================
        // 👉 3. ÉP MÀU HEADER XANH ĐẬM CHO BẢNG CÔNG NỢ
        // =========================================================
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
                    new EmptyBorder(10, 15, 10, 15)
                ));
                return label;
            }
        };

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(customHeaderRenderer);
        }
        // =========================================================

        table.setShowGrid(true);
        table.setGridColor(UIUtils.BORDER);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(5).setPreferredWidth(120);
        UIUtils.columnWidths(table,100,265,180,160,160,170);

        // Giữ nguyên đoạn tô màu dòng và Badge của bạn
        RowColorRenderer rowColorRenderer = new RowColorRenderer();
        for (int i = 0; i < table.getColumnCount() - 1; i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(rowColorRenderer);
        }
        table.getColumnModel().getColumn(5).setCellRenderer(new BadgeRenderer());

        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(new MatteBorder(0, 0, 1, 0, UIUtils.BORDER));

        tablePanel.add(scrollTable, BorderLayout.CENTER);

        // ==========================================
        // KHÚC DƯỚI: NÚT THANH TOÁN
        // ==========================================
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 15));
        bottomPanel.setBackground(UIUtils.WHITE);
        bottomPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        btnThanhToan = new JButton("CHỌN HỌC KỲ ĐỂ XEM");
        btnThanhToan.setIcon(new CreditCardIcon()); 
        btnThanhToan.setIconTextGap(12);
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnThanhToan.setBackground(COLOR_PAY_DISABLED); 
        btnThanhToan.setForeground(UIUtils.WHITE);
        btnThanhToan.setPreferredSize(new Dimension(280, 45));
        
        // --- FIX LỖI NÚT LỢT MÀU BỞI WINDOWS LOOK AND FEEL ---
        btnThanhToan.setUI(new javax.swing.plaf.basic.BasicButtonUI()); // Ép dùng Basic UI (Màu đặc phẳng)
        btnThanhToan.setBorder(new EmptyBorder(0, 0, 0, 0)); // Xóa viền nổi 3D của Windows
        btnThanhToan.setFocusPainted(false);
        // -----------------------------------------------------

        btnThanhToan.setEnabled(false);
        btnThanhToan.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnThanhToan.addActionListener(e -> actionThanhToan());

        bottomPanel.add(btnThanhToan);
        tablePanel.add(bottomPanel, BorderLayout.SOUTH);

        // Bắt sự kiện Click vào dòng trên Bảng
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
                int row = table.getSelectedRow();
                String trangThai = table.getValueAt(row, 5).toString();
                
                if (trangThai.equalsIgnoreCase("Đã hoàn thành")) {
                    btnThanhToan.setEnabled(false);
                    btnThanhToan.setBackground(COLOR_PAY_DONE);
                    btnThanhToan.setIcon(new CheckIcon()); 
                    btnThanhToan.setText("ĐÃ HOÀN THÀNH KỲ NÀY");
                } else {
                    btnThanhToan.setEnabled(true);
                    btnThanhToan.setBackground(COLOR_PAY_ACTIVE);
                    btnThanhToan.setIcon(new CreditCardIcon()); 
                    btnThanhToan.setText("HƯỚNG DẪN NỘP HỌC PHÍ");
                }
            }
        });

        add(topPanel, BorderLayout.NORTH);
        add(tablePanel, BorderLayout.CENTER);

        loadTableData();
    }

    // ==========================================
    // LOGIC TẢI DỮ LIỆU TỪ DATABASE LÊN BẢNG
    // ==========================================
    private void loadTableData() {
        tableModel.setRowCount(0); 
        double tongTienNoToanKhoa = 0;

        try {
            ResultSet rs = service.getDanhSachCongNo(maSV);
            if (rs != null) {
                while (rs.next()) {
                    String maHK = rs.getString("MaHK");
                    String tenHK = rs.getString("TenHK") != null ? rs.getString("TenHK") : maHK;
                    double tongPhaiDong = rs.getDouble("TongTienPhaiDong");
                    double daDong = rs.getDouble("SoTienDaDong");
                    double conNo = Math.max(0, tongPhaiDong - daDong);
                    String trangThai = conNo == 0 ? "Đã hoàn thành" : daDong > 0 ? "Còn nợ" : "Chưa đóng";

                    tongTienNoToanKhoa += conNo;

                    tableModel.addRow(new Object[]{
                        maHK, 
                        tenHK, 
                        String.format("%,.0f", tongPhaiDong), 
                        String.format("%,.0f", daDong), 
                        String.format("%,.0f", conNo), 
                        trangThai
                    });
                }
            }
            
            if (tongTienNoToanKhoa > 0) {
                lblTongNo.setText("Tổng nợ hiện tại: " + String.format("%,.0f VNĐ", tongTienNoToanKhoa));
                lblTongNo.setForeground(UIUtils.RED_500);
            } else {
                lblTongNo.setText("Bạn không có khoản nợ nào.");
                lblTongNo.setForeground(UIUtils.GREEN_500);
            }

            btnThanhToan.setEnabled(false);
            btnThanhToan.setBackground(COLOR_PAY_DISABLED);
            btnThanhToan.setIcon(new CreditCardIcon());
            btnThanhToan.setText("CHỌN HỌC KỲ ĐỂ XEM");

        } catch (Exception e) {
            lblTongNo.setText("Chưa tải được công nợ"); utils.Ui.error(this,e);
        }
    }

    // ==========================================
    // LOGIC NÚT THANH TOÁN VÀO CSDL
    // ==========================================
    private void actionThanhToan() {
        JOptionPane.showMessageDialog(this,"Nộp học phí theo hướng dẫn chính thức của nhà trường.\nCán bộ đào tạo ghi nhận số tiền sau khi đối soát.\nỨng dụng chưa tích hợp cổng thanh toán trực tuyến.","Hướng dẫn nộp học phí",JOptionPane.INFORMATION_MESSAGE);
    }

    // ========================================================
    // CÁC LỚP RENDERER VÀ VECTOR ICONS DÀNH CHO GIAO DIỆN
    // ========================================================

    class CreditCardIcon implements Icon {
        public int getIconWidth() { return 22; }
        public int getIconHeight() { return 22; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(1.8f));
            g2.drawRoundRect(x + 1, y + 4, 20, 14, 4, 4);
            g2.fillRect(x + 1, y + 8, 20, 3); 
            g2.fillRect(x + 4, y + 13, 4, 2); 
            g2.dispose();
        }
    }

    class CheckIcon implements Icon {
        public int getIconWidth() { return 22; }
        public int getIconHeight() { return 22; }
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(x + 4, y + 11, x + 9, y + 16);
            g2.drawLine(x + 9, y + 16, x + 18, y + 6);
            g2.dispose();
        }
    }

    class RowColorRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String status = "";
            Object statusObj = table.getModel().getValueAt(table.convertRowIndexToModel(row), 5);
            if (statusObj != null) status = statusObj.toString();

            if (!isSelected) {
                if (status.equalsIgnoreCase("Đã hoàn thành")) {
                    c.setBackground(new Color(240, 253, 244)); 
                    c.setForeground(column == 4 ? UIUtils.GREEN_500 : UIUtils.TEXT_MAIN);
                } else if (status.equalsIgnoreCase("Chưa đóng") || status.toLowerCase().contains("nợ")) {
                    c.setBackground(new Color(254, 226, 226)); 
                    c.setForeground(column == 4 ? UIUtils.MIT_RED : UIUtils.TEXT_MAIN);
                } else {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : UIUtils.BLUE_SOFT);
                    c.setForeground(UIUtils.TEXT_MAIN);
                }
            } else {
                c.setBackground(UIUtils.BLUE_LIGHT);
                c.setForeground(UIUtils.MIT_RED);
            }
            
            setBorder(BorderFactory.createCompoundBorder(
                new MatteBorder(0, 0, 1, 1, UIUtils.BORDER), 
                new EmptyBorder(0, 15, 0, 15)
            )); 
            return c;
        }
    }

    class BadgeRenderer extends DefaultTableCellRenderer {
        private JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 6));
        private JLabel lbl = new JLabel();

        public BadgeRenderer() {
            p.setOpaque(true); 
            lbl.setOpaque(false); 
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            p.add(lbl);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            String status = value != null ? value.toString() : "";
            lbl.setText(status);

            if (!isSelected) {
                if (status.equalsIgnoreCase("Đã hoàn thành")) {
                    p.setBackground(new Color(240, 253, 244)); 
                    lbl.setForeground(new Color(21, 128, 61));
                } else if (status.equalsIgnoreCase("Chưa đóng") || status.toLowerCase().contains("nợ")) {
                    p.setBackground(UIUtils.MIT_RED_LIGHT);
                    lbl.setForeground(UIUtils.MIT_RED);
                } else {
                    p.setBackground(row % 2 == 0 ? Color.WHITE : UIUtils.BLUE_SOFT); 
                    lbl.setForeground(new Color(71, 85, 105));
                }
            } else {
                p.setBackground(UIUtils.BLUE_LIGHT);
                lbl.setForeground(UIUtils.MIT_RED);
            }
            
            p.setBorder(new MatteBorder(0, 0, 1, 1, UIUtils.BORDER));
            return p;
        }
    }
}
