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
        topStatsPanel.add(createStatCard("GPA học kỳ · hệ 10", String.format("%.2f", gpa10), 2, new Color(255, 247, 237), UIUtils.MIT_ORANGE));
        topStatsPanel.add(createStatCard("GPA học kỳ · hệ 4", String.format("%.2f", gpa4), 3, new Color(240, 253, 244), UIUtils.GREEN_500));
        topStatsPanel.add(createStatCard("Xếp loại học kỳ", xepLoai, 4, new Color(254, 242, 242), UIUtils.RED_500));

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
        UIUtils.styleTable(table);UIUtils.columnWidths(table,130,235,50,100,90,90,95,65,135); 
        
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

        JPanel titleAndTools=new JPanel(new BorderLayout(0,8));titleAndTools.setOpaque(false);titleAndTools.add(header,BorderLayout.NORTH);titleAndTools.add(utils.Ui.tableTools(table,"Bang_diem_"+currentMaSV),BorderLayout.CENTER);tableContainer.add(titleAndTools, BorderLayout.NORTH); 
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
        try { var summary=service.AcademicService.summary(currentMaSV,currentMaHK);
            tcKyNay=summary.earned();gpa10=summary.gpa10();gpa4=summary.gpa4();
            xepLoai=summary.gradedCredits()==0?"Chưa có điểm":gpa4>=3.6?"Xuất sắc":gpa4>=3.2?"Giỏi":gpa4>=2.5?"Khá":gpa4>=2?"Trung bình":"Yếu";
        }catch(Exception e){throw new IllegalStateException("Không tính được kết quả học tập.",e);}
    }

    private void loadTableData() {
        try { for(var row:utils.Db.rows("SELECT l.MaMon,m.TenMon,m.SoTinChi,k.DiemChuyenCan,k.DiemGiuaKy,k.DiemCuoiKy,k.DiemTongKet,k.TrangThai FROM KET_QUA_DANG_KY k JOIN LOP_HOC_PHAN l ON k.MaLHP=l.MaLHP JOIN MON_HOC m ON m.MaMon=l.MaMon WHERE k.MaSV=? AND l.MaHK=? ORDER BY m.MaMon",currentMaSV,currentMaHK)) {
            Double tk=service.AcademicService.score(row.get("DiemTongKet"));
            tableModel.addRow(new Object[]{row.get("MaMon"),row.get("TenMon"),row.get("SoTinChi"),displayScore(row.get("DiemChuyenCan")),displayScore(row.get("DiemGiuaKy")),displayScore(row.get("DiemCuoiKy")),displayScore(tk),tk==null?"—":formatScore(service.AcademicService.point4(tk)),tk==null?"Chưa có điểm":tk>=4?"Đạt":"Không đạt"});
        }}catch(Exception e){throw new IllegalStateException("Không tải được bảng điểm.",e);}
    }
    private String displayScore(Object value){Double n=service.AcademicService.score(value);return n==null?"—":formatScore(n);}


    private String formatScore(double score) {
        
        if (score == Math.floor(score)) {
            return String.format("%.0f", score);
        }
        return String.format("%.1f", score);
    }

    // ==========================================
    // UI COMPONENTS
    // ==========================================
    private JPanel createStatCard(String title, String val, int iconType, Color bgColor, Color textColor) {
        return UIUtils.statCard(title,val,iconType>=3?UIUtils.MIT_RED:UIUtils.BLUE);
    }

    // ==========================================
    // RENDERERS & ICONS
    // ==========================================
    class ZebraRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? Color.WHITE : UIUtils.BLUE_SOFT);
            } else {
                c.setBackground(UIUtils.BLUE_LIGHT);
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