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
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminBaoCaoPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private StudentManagerService service;
    private DefaultTableModel tableModel;
    private JTable table;
    private TableRowSorter<DefaultTableModel> rowSorter;

    private JTextField txtSearch;
    private JComboBox<String> cbFilterNo;

    public AdminBaoCaoPanel(StudentManagerService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBackground(UIUtils.BG_APP);
        setBorder(new EmptyBorder(5, 0, 0, 0));

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(Color.WHITE);
        wrapper.setBorder(new LineBorder(UIUtils.BORDER, 1, true));

        // 1. Thanh công cụ Lọc (Filter & Export)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(new MatteBorder(0, 0, 1, 0, UIUtils.BORDER));

        JLabel lblTitle = new JLabel("Học vụ & Công nợ");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(UIUtils.TEXT_MAIN);

        txtSearch = UIUtils.createInput();
        txtSearch.setColumns(15);
        txtSearch.setFont(UIUtils.FONT_NORMAL);
        txtSearch.putClientProperty("JTextField.placeholderText", "Tìm theo Tên hoặc Mã SV...");

        cbFilterNo = new JComboBox<>(new String[]{"Tất cả trạng thái", "Sinh viên CÒN NỢ", "Đã nộp đủ học phí"});
        cbFilterNo.setFont(UIUtils.FONT_NORMAL);
        cbFilterNo.setBackground(Color.WHITE);

        JButton btnExport = createActionButton("XUẤT FILE CSV", new Color(25, 135, 84));

        filterPanel.add(lblTitle);
        filterPanel.add(new JLabel("Tìm kiếm:"));
        filterPanel.add(txtSearch);
        filterPanel.add(new JLabel("Tài chính:"));
        filterPanel.add(cbFilterNo);
        filterPanel.add(Box.createHorizontalStrut(12));
        filterPanel.add(btnExport);

        // 2. Bảng Dữ liệu Báo cáo
        String[] cols = {"Mã SV", "Họ tên", "Mã lớp", "Ngành", "Tín chỉ tích lũy", "Nợ học phí (VNĐ)", "Trạng thái"};
        tableModel = new DefaultTableModel(cols, 0) {
            private static final long serialVersionUID = 1L;
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        UIUtils.styleTable(table);
        table.setShowGrid(true);
        table.setGridColor(UIUtils.BORDER);
        table.setIntercellSpacing(new Dimension(1, 1));
        
        table.getColumnModel().getColumn(1).setPreferredWidth(160);
        table.getColumnModel().getColumn(4).setPreferredWidth(140);
        table.getColumnModel().getColumn(5).setPreferredWidth(160);
        UIUtils.columnWidths(table,105,210,100,80,135,175,140);

        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        ZebraRenderer zebra = new ZebraRenderer();
        for (int i = 0; i < table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setCellRenderer(zebra);

        JScrollPane scrollTable = new JScrollPane(table);
        scrollTable.setBorder(BorderFactory.createEmptyBorder());

        // 3. Sự kiện
        txtSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) { applyFilters(); }
        });
        cbFilterNo.addActionListener(e -> applyFilters());
        btnExport.addActionListener(e -> exportToCSV());

        wrapper.add(filterPanel, BorderLayout.NORTH);
        wrapper.add(scrollTable, BorderLayout.CENTER);
        
        add(wrapper, BorderLayout.CENTER);
        loadReportData();
    }

    private void loadReportData() {
        tableModel.setRowCount(0);
        String sql = "SELECT s.MaSV, s.HoTen, s.MaLop, s.MaCTDT, " +
                     "ISNULL((SELECT SUM(TRY_CONVERT(int,m.SoTinChi)) FROM MON_HOC m WHERE EXISTS(SELECT 1 FROM KET_QUA_DANG_KY kq JOIN LOP_HOC_PHAN lhp ON kq.MaLHP=lhp.MaLHP WHERE kq.MaSV=s.MaSV AND lhp.MaMon=m.MaMon AND TRY_CONVERT(decimal(5,2),kq.DiemTongKet)>=4)),0) AS TongTC, " +
                     "ISNULL((SELECT SUM(CAST(TongTienPhaiDong AS FLOAT) - CAST(SoTienDaDong AS FLOAT)) FROM CONG_NO_HOC_PHI WHERE MaSV = s.MaSV AND TRY_CONVERT(decimal(18,2),TongTienPhaiDong) > COALESCE(TRY_CONVERT(decimal(18,2),TRY_CONVERT(float,SoTienDaDong)),0)), 0) AS TongNo " +
                     "FROM SINH_VIEN s";

        try (Connection conn = DBConnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                double no = rs.getDouble("TongNo");
                String trangThai = no > 0 ? "CÒN NỢ" : "Bình thường";
                tableModel.addRow(new Object[]{
                    rs.getString("MaSV"), rs.getString("HoTen"), rs.getString("MaLop"), rs.getString("MaCTDT"), 
                    rs.getInt("TongTC"), String.format("%,.0f", no), trangThai
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private void applyFilters() {
        String searchText = txtSearch.getText().trim().toLowerCase();
        int debtStatus = cbFilterNo.getSelectedIndex(); 

        rowSorter.setRowFilter(new RowFilter<DefaultTableModel, Integer>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Integer> entry) {
                String ma = entry.getStringValue(0).toLowerCase();
                String ten = entry.getStringValue(1).toLowerCase();
                String trangThai = entry.getStringValue(6);

                boolean matchSearch = ma.contains(searchText) || ten.contains(searchText);
                boolean matchDebt = true;
                
                if (debtStatus == 1 && !trangThai.equals("CÒN NỢ")) matchDebt = false; 
                if (debtStatus == 2 && trangThai.equals("CÒN NỢ")) matchDebt = false;  

                return matchSearch && matchDebt;
            }
        });
    }

    private void exportToCSV(){utils.Ui.export(table,"Bao_cao_hoc_vu");}

    private JButton createActionButton(String text, Color bgColor) {
        return UIUtils.createActionButton(text);
    }

    class ZebraRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!isSelected) c.setBackground(row % 2 == 0 ? Color.WHITE : UIUtils.BLUE_SOFT);
            else c.setBackground(UIUtils.BLUE_LIGHT);
            
            if (column == 6 && value != null) {
                if (value.toString().equals("CÒN NỢ")) { c.setForeground(UIUtils.RED_500); c.setFont(new Font("Segoe UI", Font.BOLD, 14)); } 
                else { c.setForeground(UIUtils.GREEN_500); c.setFont(new Font("Segoe UI", Font.PLAIN, 14)); }
            } else { c.setForeground(UIUtils.TEXT_MAIN); c.setFont(new Font("Segoe UI", Font.PLAIN, 14)); }
            setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0, 0, 1, 1, UIUtils.BORDER), new EmptyBorder(0, 15, 0, 15)));
            return c;
        }
    }
}
