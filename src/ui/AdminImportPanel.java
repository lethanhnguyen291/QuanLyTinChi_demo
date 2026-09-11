package ui;

import service.StudentManagerService;
import utils.UIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminImportPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private StudentManagerService service;
    private JTextArea txtLogs;

    public AdminImportPanel(StudentManagerService service) {
        this.service = service;
        setLayout(new BorderLayout(20, 0));
        setBackground(UIUtils.BG_APP);
        setBorder(new EmptyBorder(20, 0, 20, 0));

        // 1. Box chứa nút Import
        JPanel importBox = new JPanel();
        importBox.setLayout(new BoxLayout(importBox, BoxLayout.Y_AXIS));
        importBox.setBackground(Color.WHITE);
        importBox.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(UIUtils.BORDER, 1, true), new EmptyBorder(30, 30, 30, 30)
        ));
        importBox.setPreferredSize(new Dimension(400, 0));

        JLabel lblTitle = new JLabel("NẠP DỮ LIỆU EXCEL (.XLSX)");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(new Color(13, 110, 253));

        JLabel lblSub = new JLabel("<html>Hệ thống cho phép nạp hàng loạt dữ liệu<br>từ tệp Microsoft Excel. Hãy đảm bảo file<br>đúng định dạng cột.</html>");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(UIUtils.TEXT_MUTED);

        JButton btnSV = createActionButton("1. NẠP FILE SINH VIÊN", new Color(13, 110, 253));
        JButton btnGV = createActionButton("2. NẠP FILE GIẢNG VIÊN", new Color(111, 66, 193)); 
        
        btnSV.setMaximumSize(new Dimension(350, 50));
        btnGV.setMaximumSize(new Dimension(350, 50));

        btnSV.addActionListener(e -> processImport(true));
        btnGV.addActionListener(e -> processImport(false));

        importBox.add(lblTitle);
        importBox.add(Box.createVerticalStrut(15));
        importBox.add(lblSub);
        importBox.add(Box.createVerticalStrut(40));
        importBox.add(btnSV);
        importBox.add(Box.createVerticalStrut(20));
        importBox.add(btnGV);

        // 2. Box chứa Console Logs
        JPanel logBox = new JPanel(new BorderLayout());
        logBox.setBackground(Color.WHITE);
        logBox.setBorder(new LineBorder(UIUtils.BORDER, 1, true));

        JPanel logHeader = new JPanel(new BorderLayout());
        logHeader.setBackground(new Color(30, 41, 59)); // Màu xám đen
        logHeader.setBorder(new EmptyBorder(12, 20, 12, 20));
        JLabel lblLogTitle = new JLabel("HỆ THỐNG GHI NHẬN (LOGS)");
        lblLogTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblLogTitle.setForeground(Color.WHITE);
        logHeader.add(lblLogTitle, BorderLayout.WEST);

        txtLogs = new JTextArea("Chưa có tiến trình nào đang chạy...\n");
        txtLogs.setEditable(false);
        txtLogs.setFont(new Font("Consolas", Font.PLAIN, 15));
        txtLogs.setBackground(new Color(15, 23, 42)); // Đen đậm chuẩn Terminal
        txtLogs.setForeground(new Color(52, 211, 153)); // Chữ xanh ngọc
        txtLogs.setBorder(new EmptyBorder(15, 20, 15, 20));

        JScrollPane logScroll = new JScrollPane(txtLogs);
        logScroll.setBorder(BorderFactory.createEmptyBorder());

        logBox.add(logHeader, BorderLayout.NORTH);
        logBox.add(logScroll, BorderLayout.CENTER);

        add(importBox, BorderLayout.WEST);
        add(logBox, BorderLayout.CENTER);
    }

    private void processImport(boolean isSinhVien) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle(isSinhVien ? "Chọn file Excel Danh sách Sinh viên" : "Chọn file Excel Danh sách Giảng viên");
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getAbsolutePath();
            String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
            
            txtLogs.append("\n[" + time + "] Đang phân tích file: " + fc.getSelectedFile().getName() + "...\n");
            try {
                String msg = isSinhVien ? service.importSinhVienFromExcel(path) : service.importGiangVienFromExcel(path);
                txtLogs.append("[" + time + "] HOÀN TẤT:\n" + msg + "\n");
            } catch (Exception ex) {
                txtLogs.append("[" + time + "] LỖI NGHIÊM TRỌNG: " + ex.getMessage() + "\n");
            }
        }
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI()); 
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(bgColor.darker(), 1, true), new EmptyBorder(12, 20, 12, 20)
        ));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bgColor.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bgColor); }
        });
        return btn;
    }
}