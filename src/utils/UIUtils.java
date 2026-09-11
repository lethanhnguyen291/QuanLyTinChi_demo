package utils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

public class UIUtils {
    // BỘ MÀU CHUẨN CỦA ĐẠI HỌC CÔNG NGHỆ MIỀN ĐÔNG (MIT)
    public static final Color MIT_RED       = new Color(139, 0, 0);      // Đỏ đô
    public static final Color MIT_ORANGE    = new Color(230, 81, 0);     // Cam đậm
    public static final Color MIT_YELLOW    = new Color(255, 193, 7);    // Vàng
    public static final Color MIT_RED_LIGHT = new Color(253, 240, 240);  // Nền Tab
    
    public static final Color BG_APP      = new Color(248, 250, 252); 
    public static final Color BORDER      = new Color(226, 232, 240); 
    public static final Color TEXT_MAIN   = new Color(30, 41, 59);    
    public static final Color TEXT_MUTED  = new Color(100, 116, 139); 
    public static final Color GREEN_500   = new Color(34, 197, 94);   
    public static final Color RED_500     = new Color(239, 68, 68);   
    public static final Color WHITE       = Color.WHITE;

    public static final Font FONT_NORMAL  = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD    = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD, 18);
    
    // --- FONT ĐẶC BIỆT ĐỂ HIỂN THỊ EMOJI TRÊN WINDOWS ---
    public static final Font FONT_EMOJI   = new Font("Segoe UI Emoji", Font.BOLD, 14);
    public static final Font FONT_LOGO    = new Font("Segoe UI Emoji", Font.BOLD, 22);

    // ================== CÁC HÀM VẼ GIAO DIỆN PHẲNG (FLAT UI) ==================

    public static JButton createPrimaryBtn(String text) {
        JButton btn = new JButton(text);
        btn.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        btn.setFont(FONT_BOLD);
        btn.setBackground(MIT_RED);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(12, 30, 12, 30));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(MIT_ORANGE); }
            public void mouseExited(java.awt.event.MouseEvent evt) { btn.setBackground(MIT_RED); }
        });
        return btn;
    }

    public static JTextField createInput() {
        JTextField tf = new JTextField();
        tf.setFont(FONT_NORMAL);
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(BORDER, 1), new EmptyBorder(10, 15, 10, 15)
        ));
        return tf;
    }

    public static JPanel createFormRow(String labelText, JComponent input) {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(WHITE);
        wrapper.setMaximumSize(new Dimension(500, 70));
        wrapper.setBorder(new EmptyBorder(0, 0, 15, 0)); 
        
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(TEXT_MAIN);
        lbl.setBorder(new EmptyBorder(0, 0, 5, 0)); 
        
        wrapper.add(lbl, BorderLayout.NORTH);
        wrapper.add(input, BorderLayout.CENTER);
        return wrapper;
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_NORMAL);
        table.setRowHeight(38);
        table.setSelectionBackground(MIT_RED_LIGHT);
        table.setSelectionForeground(MIT_RED);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        
        JTableHeader th = table.getTableHeader();
        th.setFont(FONT_BOLD);
        th.setBackground(BG_APP);
        th.setForeground(TEXT_MAIN);
        th.setPreferredSize(new Dimension(100, 45));
        ((DefaultTableCellRenderer)th.getDefaultRenderer()).setHorizontalAlignment(JLabel.LEFT);
        
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBorder(new MatteBorder(0, 0, 1, 0, BORDER));
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }
}