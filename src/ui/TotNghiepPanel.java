package ui;

import service.StudentManagerService;
import utils.UIUtils;
import config.DBConnect;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TotNghiepPanel extends JPanel {
    private String currentMaSV;

    // --- CÁC BIẾN LƯU TRỮ DỮ LIỆU ĐỂ XÉT ĐIỀU KIỆN ---
    private String hoTen = "Đang tải...";
    private int tinChiTichLuy = 0;
    private int tinChiYeuCau = 0;
    private double tongNo = 0.0;
    private boolean hasNoHocPhi = false;
    private boolean isDatNgoaiNgu = false;

    public TotNghiepPanel(StudentManagerService service, String maSV) {
        this.currentMaSV = maSV;

        // 1. Tải dữ liệu thật từ DB lên trước khi vẽ UI
        loadData();

        // 2. Vẽ giao diện dựa trên đoạn code của bạn
        setLayout(new BorderLayout());
        setBackground( Color.WHITE);
        setBorder(new LineBorder(UIUtils.BORDER, 1, true));
        add(buildSectionHeader("Thẩm định điều kiện tốt nghiệp"), BorderLayout.NORTH);

        boolean tcOk  = (tinChiTichLuy >= tinChiYeuCau);
        boolean noOk  = !hasNoHocPhi;
        boolean nnOk  = isDatNgoaiNgu;
        boolean allOk = tcOk && noOk && nnOk;
        int metCount  = (tcOk ? 1 : 0) + (noOk ? 1 : 0) + (nnOk ? 1 : 0);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(UIUtils.BG_APP);
        body.setBorder(new EmptyBorder(25, 30, 30, 30));

        // ─── Banner trang thai tong ────────────────────────────────
        Color sBg  = allOk ? new Color(240,253,244) : UIUtils.MIT_RED_LIGHT;
        Color sAcc = allOk ? new Color(22,101,52)   : UIUtils.MIT_RED;
        Color sBdr = allOk ? new Color(189,223,201) : new Color(234,205,205);

        JPanel banner = new JPanel(new BorderLayout(16, 0));
        banner.setBackground(sBg);
        banner.setBorder(BorderFactory.createCompoundBorder(
            UIUtils.cardBorder(sBdr, 1), new EmptyBorder(14, 20, 14, 20)));
        banner.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        banner.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel icoLbl = new JLabel(allOk ? "Đạt" : "!");
        icoLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        icoLbl.setForeground(sAcc);

        JPanel bannerTxt = new JPanel();
        bannerTxt.setLayout(new BoxLayout(bannerTxt, BoxLayout.Y_AXIS));
        bannerTxt.setBackground(sBg);

        JLabel lblBT = new JLabel(allOk ? "Đủ điều kiện tốt nghiệp (Sơ bộ)"
                                        : "Chưa đủ điều kiện - cần bổ sung thêm");
        lblBT.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblBT.setForeground(sAcc);

        JLabel lblBS = new JLabel("Đã đáp ứng " + metCount + "/3 điều kiện  -  "
                                  + hoTen + "  (" + currentMaSV + ")");
        lblBS.setFont(UIUtils.FONT_NORMAL);
        lblBS.setForeground(UIUtils.TEXT_MUTED);

        bannerTxt.add(lblBT);
        bannerTxt.add(Box.createVerticalStrut(3));
        bannerTxt.add(lblBS);
        banner.add(icoLbl,    BorderLayout.WEST);
        banner.add(bannerTxt, BorderLayout.CENTER);
        body.add(banner);
        body.add(Box.createVerticalStrut(20));

        // ─── 3 the dieu kien ──────────────────────────────────────
        JPanel condRow = new JPanel(new GridLayout(1, 3, 15, 0));
        condRow.setBackground(UIUtils.BG_APP);
        condRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 175));
        condRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        condRow.add(buildCondCard(
            "1. Tín chỉ tích lũy",
            "Yêu cầu >= " + tinChiYeuCau + " tín chỉ",
            tinChiTichLuy + " / " + tinChiYeuCau + " TC",
            tcOk, tinChiTichLuy, tinChiYeuCau));

        condRow.add(buildCondCard(
            "2. Nghĩa vụ tài chính",
            "Không còn nợ học phí",
            noOk ? "Đã hoàn tất học phí" : String.format("Còn nợ %,.0f đ", tongNo),
            noOk, -1, -1));

        condRow.add(buildCondCard(
            "3. Chuẩn đầu ra ngoại ngữ",
            "Đạt chứng chỉ theo quy định",
            nnOk ? "Đã đạt chuẩn" : "Chưa đạt chuẩn",
            nnOk, -1, -1));

        body.add(condRow);
        body.add(Box.createVerticalStrut(25));

        // ─── Nut gui yeu cau ──────────────────────────────────────
        JButton btnXet = UIUtils.createPrimaryBtn("Kiểm tra điều kiện hiện tại");
        btnXet.setMaximumSize(new Dimension(440, 46));
        btnXet.setAlignmentX(Component.LEFT_ALIGNMENT);
        body.add(btnXet);
        body.add(Box.createVerticalStrut(18));

        // ─── Panel ket qua (an, hien sau khi bam nut) ────────────
        Color rBg  = allOk ? new Color(240,253,244) : UIUtils.MIT_RED_LIGHT;
        Color rBdr = allOk ? new Color(189,223,201) : new Color(234,205,205);

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultPanel.setBackground(rBg);
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
            UIUtils.cardBorder(rBdr, 1), new EmptyBorder(18, 22, 18, 22)));
        resultPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));
        resultPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        resultPanel.setVisible(false);

        btnXet.addActionListener(e -> utils.Ui.async(this,btnXet,()->service.checkGraduation(currentMaSV),rawResult->{
            JTextArea detail=new JTextArea(rawResult+"\n\nKiểm tra lúc: "+java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            detail.setEditable(false);detail.setFont(UIUtils.FONT_NORMAL);detail.setLineWrap(true);detail.setWrapStyleWord(true);detail.setBackground(rBg);
            resultPanel.removeAll();resultPanel.add(detail);resultPanel.setVisible(true);resultPanel.revalidate();resultPanel.repaint();
        }));

        body.add(resultPanel);
        body.add(Box.createVerticalGlue());

        add(body, BorderLayout.CENTER);
    }

    // ============================================================
    // LẤY DỮ LIỆU TỪ DATABASE
    // ============================================================
    private void loadData() {
        try (Connection conn = DBConnect.getConnection()) {
            if (conn == null) return;
            
            // 1. Lấy Họ Tên & Chuẩn đầu ra ngoại ngữ
            String sql1 = "SELECT HoTen, DatChuanNgoaiNgu FROM SINH_VIEN WHERE MaSV = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql1)) {
                ps.setString(1, currentMaSV);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    hoTen = rs.getString("HoTen");
                    isDatNgoaiNgu = service.AcademicService.languagePassed(rs.getObject("DatChuanNgoaiNgu"));
                }
            }

            tinChiTichLuy=service.AcademicService.summary(currentMaSV,null).earned();
            tinChiYeuCau=service.AcademicService.requiredCredits(currentMaSV);
            // 3. Lấy Thông tin Nợ học phí
            String sql3 = "SELECT SUM(CAST(TongTienPhaiDong AS FLOAT) - CAST(SoTienDaDong AS FLOAT)) FROM CONG_NO_HOC_PHI WHERE MaSV = ? AND TRY_CONVERT(decimal(18,2),TongTienPhaiDong) > COALESCE(TRY_CONVERT(decimal(18,2),TRY_CONVERT(float,SoTienDaDong)),0)";
            try (PreparedStatement ps = conn.prepareStatement(sql3)) {
                ps.setString(1, currentMaSV);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    tongNo = rs.getDouble(1);
                    hasNoHocPhi = (tongNo > 0);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Không đủ dữ liệu để xét tốt nghiệp.",e);
        }
    }

    // ============================================================
    // HELPER: THE DIEU KIEN TOT NGHIEP
    // passed = true  → xanh la
    // passed = false → do
    // current/max   >= 0 thi ve progress bar (chi cho dieu kien TC)
    // ============================================================
    private JPanel buildCondCard(String title, String requirement,
                                 String statusText, boolean passed,
                                 int current, int max) {
        Color bg  = passed ? new Color(240,253,244) : UIUtils.MIT_RED_LIGHT;
        Color acc = passed ? new Color(22,101,52)   : UIUtils.MIT_RED;
        Color bdr = passed ? new Color(189,223,201) : new Color(234,205,205);

        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(bg);
        card.setBorder(BorderFactory.createCompoundBorder(
            UIUtils.cardBorder(bdr, 1), new EmptyBorder(16, 18, 16, 18)));

        // Header: ten + icon [OK]/[X]
        JPanel topRow = new JPanel(new BorderLayout(8, 0));
        topRow.setBackground(bg);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblTitle.setForeground(UIUtils.TEXT_MAIN);

        JLabel lblIco = new JLabel(passed ? "Đạt" : "!");
        lblIco.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblIco.setForeground(acc);

        topRow.add(lblTitle, BorderLayout.CENTER);
        topRow.add(lblIco,   BorderLayout.EAST);

        // Body
        JPanel cardBody = new JPanel();
        cardBody.setLayout(new BoxLayout(cardBody, BoxLayout.Y_AXIS));
        cardBody.setBackground(bg);

        JLabel lblReq = new JLabel(requirement);
        lblReq.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblReq.setForeground(UIUtils.TEXT_MUTED);
        lblReq.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblStat = new JLabel(statusText);
        lblStat.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblStat.setForeground(acc);
        lblStat.setAlignmentX(Component.LEFT_ALIGNMENT);

        cardBody.add(Box.createVerticalStrut(10));
        cardBody.add(lblReq);

        // Progress bar chi danh cho dieu kien tin chi
        if (current >= 0 && max > 0) {
            cardBody.add(Box.createVerticalStrut(8));
            JProgressBar bar = new JProgressBar(0, max);
            bar.setValue(Math.min(current, max));
            bar.setStringPainted(false);
            bar.setForeground(passed ? new Color(34,197,94) : UIUtils.MIT_RED);
            bar.setBackground(passed ? new Color(220,252,231) : new Color(242,215,215));
            bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 6));
            bar.setBorderPainted(false);
            bar.setAlignmentX(Component.LEFT_ALIGNMENT);
            cardBody.add(bar);
        }

        cardBody.add(Box.createVerticalStrut(8));
        cardBody.add(lblStat);

        card.add(topRow,   BorderLayout.NORTH);
        card.add(cardBody, BorderLayout.CENTER);
        return card;
    }
    private JPanel buildSectionHeader(String title) {
        JPanel h = new JPanel(new BorderLayout());
        
        // =========================================================
        // ĐÃ SỬA TÊN BIẾN THÀNH CHỮ 'h' CHO KHỚP VỚI FILE CỦA BẠN
        // =========================================================
        h.setBackground(new Color(239, 246, 255)); // Nền Xanh dương nhạt
        h.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 1, 0, new Color(191, 219, 254)), // Viền dưới nổi nhẹ
            new EmptyBorder(15, 20, 15, 20)
        ));

        JLabel l = new JLabel(title);
        l.setFont(UIUtils.FONT_TITLE);
        
        // =========================================================
        // ĐÃ SỬA TÊN BIẾN THÀNH CHỮ 'l' CHO KHỚP VỚI FILE CỦA BẠN
        // =========================================================
        l.setForeground(new Color(30, 64, 175)); // Chữ Xanh dương đậm
        
        h.add(l, BorderLayout.WEST);
        return h;
    }
}
