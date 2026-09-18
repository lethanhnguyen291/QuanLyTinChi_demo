package ui;

import config.DBConnect;
import service.AcademicService;
import service.Session;
import service.StudentManagerService;
import utils.Db;
import utils.UIUtils;
import utils.Ui;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TotNghiepPanel extends JPanel {

    private static final DateTimeFormatter TIME =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private final String studentId;

    private final JPanel content =
            new JPanel(new BorderLayout(0, 16));

    private final JLabel status =
            new JLabel("Đang tải hồ sơ…");

    private final JButton refresh =
            UIUtils.createSecondaryBtn("Cập nhật dữ liệu");

    private final JButton export =
            UIUtils.createPrimaryBtn("Xuất phiếu A4");

    private Report report;

    private record Report(
            String id,
            String name,
            String className,
            String program,
            int earned,
            int required,
            BigDecimal debt,
            boolean language,
            LocalDateTime checkedAt
    ) {
        boolean creditsPassed() {
            return earned >= required;
        }

        boolean feesPassed() {
            return debt.signum() == 0;
        }

        int passed() {
            return (creditsPassed() ? 1 : 0)
                    + (feesPassed() ? 1 : 0)
                    + (language ? 1 : 0);
        }

        List<String> nextSteps() {
            List<String> steps = new ArrayList<>();

            if (!creditsPassed()) {
                steps.add(
                        "Hoàn thành thêm " + (required - earned)
                                + " tín chỉ đạt theo chương trình đào tạo."
                );
            }

            if (!feesPassed()) {
                steps.add(
                        "Đối chiếu và hoàn tất " + money(debt)
                                + " học phí còn nợ với bộ phận tài chính."
                );
            }

            if (!language) {
                steps.add(
                        "Bổ sung minh chứng ngoại ngữ và đề nghị cán bộ "
                                + "cập nhật kết quả."
                );
            }

            if (steps.isEmpty()) {
                steps.add(
                        "In phiếu rà soát và liên hệ phòng đào tạo để được "
                                + "kiểm tra hồ sơ, hướng dẫn xét tốt nghiệp."
                );
            }

            return steps;
        }
    }

    public TotNghiepPanel(StudentManagerService service, String maSV) {
        studentId = maSV;

        setLayout(new BorderLayout(0, 16));
        setBackground(UIUtils.BG_APP);
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel header = new JPanel(new BorderLayout(0, 12));
        header.setOpaque(false);

        JLabel title = new JLabel("Hồ sơ xét tốt nghiệp");
        title.setFont(UIUtils.FONT_TITLE);
        title.setForeground(UIUtils.MIT_RED);

        header.add(title, BorderLayout.NORTH);

        JPanel actions =
                new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));

        actions.setOpaque(false);
        actions.add(refresh);
        actions.add(Box.createHorizontalStrut(10));
        actions.add(export);

        header.add(actions, BorderLayout.CENTER);

        status.setFont(UIUtils.FONT_NORMAL);
        header.add(status, BorderLayout.SOUTH);

        content.setOpaque(false);

        add(header, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);

        refresh.addActionListener(e -> reload());
        export.addActionListener(e -> exportForm());

        reload();
    }

    private void reload() {
        report = null;

        refresh.setEnabled(false);
        export.setEnabled(false);

        status.setForeground(UIUtils.TEXT_MUTED);
        status.setText("Đang đối chiếu tín chỉ, học phí và ngoại ngữ…");

        content.removeAll();
        content.revalidate();
        content.repaint();

        new SwingWorker<Report, Void>() {
            @Override
            protected Report doInBackground() throws Exception {
                return loadReport();
            }

            @Override
            protected void done() {
                try {
                    Session.requireStudent(studentId);

                    report = get();
                    showReport(report);

                    export.setEnabled(true);
                } catch (Exception ex) {
                    report = null;

                    status.setForeground(UIUtils.MIT_RED);
                    status.setText(
                            "Chưa tải được hồ sơ. "
                                    + "Bấm Cập nhật dữ liệu để thử lại."
                    );

                    ex.printStackTrace();
                } finally {
                    refresh.setEnabled(true);
                }
            }
        }.execute();
    }

    private Report loadReport() throws Exception {
        Session.requireStudent(studentId);

        try (Connection c = DBConnect.getConnection()) {
            var students = Db.rows(c, """
                    SELECT s.MaSV, s.HoTen, s.MaLop,
                           s.DatChuanNgoaiNgu,
                           p.TenCTDT, p.TongTinChiYeuCau
                    FROM SINH_VIEN s
                    LEFT JOIN CHUONG_TRINH_DAO_TAO p
                        ON p.MaCTDT = s.MaCTDT
                    WHERE s.MaSV = ?
                    """, studentId);

            if (students.isEmpty()) {
                throw new IllegalStateException(
                        "Không tìm thấy sinh viên."
                );
            }

            var s = students.get(0);

            int required = Integer.parseInt(
                    Db.str(s, "TongTinChiYeuCau").trim()
            );

            if (required <= 0) {
                throw new IllegalStateException(
                        "Chưa có tín chỉ yêu cầu hợp lệ."
                );
            }

            var grades = Db.rows(c, """
                    SELECT l.MaMon, m.SoTinChi, k.DiemTongKet
                    FROM KET_QUA_DANG_KY k
                    JOIN LOP_HOC_PHAN l ON l.MaLHP = k.MaLHP
                    JOIN MON_HOC m ON m.MaMon = l.MaMon
                    WHERE k.MaSV = ?
                    """, studentId);

            int earned = AcademicService.summarize(grades).earned();

            BigDecimal debt = BigDecimal.ZERO;

            var fees = Db.rows(c, """
                    SELECT TongTienPhaiDong, SoTienDaDong
                    FROM CONG_NO_HOC_PHI
                    WHERE MaSV = ?
                    """, studentId);

            for (var fee : fees) {
                BigDecimal due = new BigDecimal(
                        Db.str(fee, "TongTienPhaiDong").trim()
                );

                String paidText =
                        Db.str(fee, "SoTienDaDong").trim();

                BigDecimal paid = paidText.isEmpty()
                        ? BigDecimal.ZERO
                        : new BigDecimal(paidText);

                if (due.signum() < 0 || paid.signum() < 0) {
                    throw new IllegalStateException(
                            "Dữ liệu học phí không hợp lệ."
                    );
                }

                debt = debt.add(
                        due.subtract(paid).max(BigDecimal.ZERO)
                );
            }

            return new Report(
                    studentId,
                    Db.str(s, "HoTen"),
                    Db.str(s, "MaLop"),
                    Db.str(s, "TenCTDT"),
                    earned,
                    required,
                    debt,
                    AcademicService.languagePassed(
                            s.get("DatChuanNgoaiNgu")
                    ),
                    LocalDateTime.now()
            );
        }
    }

    private void showReport(Report r) {
        status.setForeground(
                r.passed() == 3
                        ? UIUtils.GREEN_500
                        : UIUtils.MIT_RED
        );

        status.setText(
                "Đáp ứng " + r.passed()
                        + "/3 điều kiện · Cập nhật "
                        + TIME.format(r.checkedAt())
        );

        JPanel profile = new JPanel(
                new GridLayout(0, 2, 20, 12)
        );

        profile.setBackground(Color.WHITE);
        profile.setBorder(
                UIUtils.cardBorder(UIUtils.BORDER, 20)
        );

        profile.add(info("Họ tên", r.name()));
        profile.add(info("Mã sinh viên", r.id()));
        profile.add(info("Lớp", r.className()));
        profile.add(info("Chương trình đào tạo", r.program()));

        JTable table = new JTable(
                Ui.model(
                        "Điều kiện",
                        "Yêu cầu",
                        "Kết quả hiện tại",
                        "Trạng thái"
                )
        );

        var model =
                (javax.swing.table.DefaultTableModel) table.getModel();

        for (Object[] row : criteria(r)) {
            model.addRow(row);
        }

        UIUtils.styleTable(table);
        UIUtils.columnWidths(table, 160, 185, 185, 110);

        table.getColumnModel().getColumn(3).setCellRenderer(
                new javax.swing.table.DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(
                            JTable t,
                            Object value,
                            boolean selected,
                            boolean focus,
                            int row,
                            int col
                    ) {
                        super.getTableCellRendererComponent(
                                t, value, selected, focus, row, col
                        );

                        setForeground(
                                "Đạt".equals(value)
                                        ? UIUtils.GREEN_500
                                        : UIUtils.MIT_RED
                        );

                        setFont(UIUtils.FONT_BOLD);
                        return this;
                    }
                }
        );

        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setPreferredSize(new Dimension(660, 178));

        StringBuilder steps = new StringBuilder();

        for (String step : r.nextSteps()) {
            steps.append("• ").append(step).append("\n\n");
        }

        steps.append(
                "Kết quả sơ bộ theo 3 điều kiện hiện có trong hệ thống. "
                        + "Phòng đào tạo kiểm tra và phê duyệt theo quy định."
        );

        JPanel next = UIUtils.noteCard(
                r.passed() == 3
                        ? "Bước tiếp theo"
                        : "Việc cần bổ sung",
                steps.toString()
        );

        next.setPreferredSize(
                new Dimension(660, r.nextSteps().size() * 45 + 120)
        );

        JPanel details = new JPanel(new BorderLayout(0, 16));
        details.setOpaque(false);
        details.add(tableScroll, BorderLayout.NORTH);
        details.add(next, BorderLayout.CENTER);

        content.removeAll();
        content.add(profile, BorderLayout.NORTH);
        content.add(details, BorderLayout.CENTER);

        UIUtils.applyTheme(content);

        content.revalidate();
        content.repaint();
    }

    private static JLabel info(String title, String value) {
        JLabel label = new JLabel(
                "<html><font color='#8B0000'>"
                        + html(title)
                        + "</font><br><b><font color='#232E40'>"
                        + html(value)
                        + "</font></b></html>"
        );

        label.setFont(UIUtils.FONT_NORMAL);
        return label;
    }

    private static Object[][] criteria(Report r) {
        return new Object[][] {
                {
                        "Tín chỉ tích lũy",
                        "Tối thiểu " + r.required() + " tín chỉ",
                        r.earned() + " / " + r.required() + " tín chỉ",
                        r.creditsPassed() ? "Đạt" : "Chưa đạt"
                },
                {
                        "Nghĩa vụ tài chính",
                        "Hoàn tất học phí",
                        r.feesPassed()
                                ? "Không còn nợ"
                                : "Còn nợ " + money(r.debt()),
                        r.feesPassed() ? "Đạt" : "Chưa đạt"
                },
                {
                        "Chuẩn ngoại ngữ",
                        "Được xác nhận đạt chuẩn",
                        r.language()
                                ? "Đã được ghi nhận"
                                : "Chưa được ghi nhận",
                        r.language() ? "Đạt" : "Chưa đạt"
                }
        };
    }

    private void exportForm() {
        if (report == null || !export.isEnabled()) {
            return;
        }

        Report snapshot = report;

        try {
            Session.requireStudent(studentId);

            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle(
                    "Lưu phiếu rà soát tốt nghiệp khổ A4"
            );

            chooser.setFileFilter(
                    new FileNameExtensionFilter(
                            "Phiếu HTML (*.html)", "html"
                    )
            );

            chooser.setSelectedFile(
                    new java.io.File(
                            "Phieu_tot_nghiep_"
                                    + studentId.replaceAll(
                                            "[^a-zA-Z0-9_-]", "_"
                                    )
                                    + ".html"
                    )
            );

            if (chooser.showSaveDialog(this)
                    != JFileChooser.APPROVE_OPTION) {
                return;
            }

            Path file = chooser.getSelectedFile().toPath();

            if (!file.toString()
                    .toLowerCase(Locale.ROOT)
                    .endsWith(".html")) {
                file = Path.of(file + ".html");
            }

            if (Files.exists(file)
                    && JOptionPane.showConfirmDialog(
                            this,
                            "File đã tồn tại. Ghi đè?",
                            "Lưu phiếu",
                            JOptionPane.YES_NO_OPTION
                    ) != JOptionPane.YES_OPTION) {
                return;
            }

            Session.requireStudent(studentId);

            Files.writeString(
                    file,
                    formHtml(snapshot),
                    StandardCharsets.UTF_8
            );

            try {
                if (!Desktop.isDesktopSupported()
                        || !Desktop.getDesktop().isSupported(
                                Desktop.Action.BROWSE
                        )) {
                    throw new UnsupportedOperationException();
                }

                Desktop.getDesktop().browse(file.toUri());
            } catch (Exception openError) {
                JOptionPane.showMessageDialog(
                        this,
                        "Đã lưu: " + file
                                + "\nMở file bằng trình duyệt, "
                                + "nhấn Ctrl+P để in hoặc lưu PDF."
                );
            }
        } catch (Exception ex) {
            Ui.error(this, ex);
        }
    }

    private static String formHtml(Report r) {
        StringBuilder out = new StringBuilder("""
                <!doctype html>
                <html lang="vi">
                <head>
                <meta charset="UTF-8">
                <title>Phiếu rà soát điều kiện tốt nghiệp</title>
                <style>
                    @page {
                        size: A4;
                        margin: 18mm;
                    }

                    * {
                        box-sizing: border-box;
                    }

                    body {
                        margin: 0;
                        background: #edf1f5;
                        color: #202938;
                        font: 13pt 'Times New Roman', serif;
                    }

                    .tools {
                        padding: 16px;
                        text-align: center;
                        font: 14px 'Segoe UI', sans-serif;
                    }

                    button {
                        background: #8b0000;
                        color: white;
                        border: 0;
                        border-radius: 6px;
                        padding: 12px 22px;
                        cursor: pointer;
                    }

                    .sheet {
                        max-width: 210mm;
                        margin: 0 auto 24px;
                        padding: 18mm;
                        background: white;
                    }

                    .brand {
                        color: #8b0000;
                        font-weight: bold;
                    }

                    .meta {
                        color: #556477;
                        font-size: 11pt;
                    }

                    h1 {
                        text-align: center;
                        font-size: 18pt;
                        line-height: 1.4;
                        margin: 24px 0 8px;
                    }

                    h2 {
                        font-size: 14pt;
                        color: #8b0000;
                        margin-top: 22px;
                    }

                    .subtitle {
                        text-align: center;
                        font-style: italic;
                    }

                    table {
                        width: 100%;
                        border-collapse: collapse;
                        margin: 14px 0;
                        font-size: 12pt;
                        table-layout: fixed;
                    }

                    th, td {
                        border: 1px solid #bccde0;
                        padding: 9px 8px;
                        text-align: left;
                        overflow-wrap: anywhere;
                    }

                    th {
                        background: #ddeeff;
                        color: #194475;
                    }

                    .good {
                        color: #167049;
                        font-weight: bold;
                    }

                    .pending {
                        color: #8b0000;
                        font-weight: bold;
                    }

                    p, li {
                        line-height: 1.5;
                    }

                    li {
                        margin-bottom: 7px;
                    }

                    .note {
                        background: #f3f7fc;
                        padding: 12px;
                        font-size: 11pt;
                    }

                    .signatures {
                        display: flex;
                        gap: 24px;
                        text-align: center;
                        margin-top: 26px;
                        break-inside: avoid;
                    }

                    .signatures > div {
                        width: 50%;
                        min-height: 100px;
                    }

                    .signatures small {
                        display: block;
                        margin-top: 6px;
                    }

                    tr {
                        break-inside: avoid;
                    }

                    @media print {
                        body {
                            background: white;
                        }

                        .tools {
                            display: none;
                        }

                        .sheet {
                            max-width: none;
                            margin: 0;
                            padding: 0;
                        }
                    }
                </style>
                </head>
                <body>
                <div class="tools">
                    <button onclick="window.print()">In / Lưu PDF</button>
                    <p>
                        Chọn khổ A4; có thể tắt đầu trang/chân trang
                        của trình duyệt khi in.
                    </p>
                </div>
                <main class="sheet">
                    <div class="brand">
                        MIT PORTAL · QUẢN LÝ ĐÀO TẠO
                    </div>
                    <h1>PHIẾU RÀ SOÁT<br>ĐIỀU KIỆN TỐT NGHIỆP</h1>
                    <p class="subtitle">
                        Thông tin phục vụ đối chiếu hồ sơ sinh viên
                    </p>
                """);

        out.append("<p class='meta'>Dữ liệu được kiểm tra lúc: ")
                .append(TIME.format(r.checkedAt()))
                .append("</p>");

        out.append("<p><b>Họ và tên:</b> ")
                .append(html(r.name()))
                .append("<br><b>Mã sinh viên:</b> ")
                .append(html(r.id()))
                .append("<br><b>Lớp:</b> ")
                .append(html(r.className()))
                .append("<br><b>Chương trình đào tạo:</b> ")
                .append(html(r.program()))
                .append("</p>");

        out.append("""
                <h2>1. Kết quả đối chiếu</h2>
                <table>
                    <colgroup>
                        <col style="width:24%">
                        <col style="width:25%">
                        <col style="width:31%">
                        <col style="width:20%">
                    </colgroup>
                    <thead>
                        <tr>
                            <th>Điều kiện</th>
                            <th>Yêu cầu</th>
                            <th>Kết quả hiện tại</th>
                            <th>Trạng thái</th>
                        </tr>
                    </thead>
                    <tbody>
                """);

        for (Object[] row : criteria(r)) {
            out.append("<tr>");

            for (int i = 0; i < row.length; i++) {
                out.append("<td");

                if (i == 3) {
                    out.append(" class='")
                            .append(
                                    row[i].equals("Đạt")
                                            ? "good"
                                            : "pending"
                            )
                            .append("'");
                }

                out.append(">")
                        .append(html(row[i]))
                        .append("</td>");
            }

            out.append("</tr>");
        }

        out.append(
                "</tbody></table><p><b>Kết luận sơ bộ: "
        );

        if (r.passed() == 3) {
            out.append(
                    "Đáp ứng 3/3 điều kiện đang được hệ thống kiểm tra."
            );
        } else {
            out.append("Chưa đáp ứng đủ điều kiện (")
                    .append(r.passed())
                    .append(
                            "/3). Cần bổ sung các nội dung dưới đây."
                    );
        }

        out.append(
                "</b></p><h2>2. Nội dung cần thực hiện</h2><ol>"
        );

        for (String step : r.nextSteps()) {
            out.append("<li>")
                    .append(html(step))
                    .append("</li>");
        }

        out.append("""
                </ol>
                <p class="note">
                    Phiếu được lập từ dữ liệu hiện có tại thời điểm
                    kiểm tra. Đây là kết quả rà soát sơ bộ, không phải
                    quyết định công nhận tốt nghiệp. Các yêu cầu bổ sung
                    và kết quả chính thức do phòng đào tạo kiểm tra,
                    phê duyệt.
                </p>
                <div class="signatures">
                    <div>
                        <b>SINH VIÊN</b>
                        <small>(Ký, ghi rõ họ tên)</small>
                    </div>
                    <div>
                        <b>CÁN BỘ TIẾP NHẬN</b>
                        <small>(Kiểm tra, xác nhận)</small>
                    </div>
                </div>
                </main>
                </body>
                </html>
                """);

        return out.toString();
    }

    private static String money(BigDecimal value) {
        return NumberFormat.getNumberInstance(
                Locale.forLanguageTag("vi-VN")
        ).format(value) + " đ";
    }

    private static String html(Object value) {
        return Objects.toString(value, "")
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}