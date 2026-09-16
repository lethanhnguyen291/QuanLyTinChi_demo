# Cập nhật giao diện — 16/09/2026

Đã sửa trực tiếp trong thư mục `QuanLyTinChi_NangCap` và biên dịch lại `QuanLyTinChi.jar`.

## Mở bản mới

Đóng cửa sổ ứng dụng đang mở rồi chạy **[RUN.bat](RUN.bat)** trong thư mục này. File chạy đã được cập nhật, không cần giải nén hay tải lại ZIP.

Nếu tự sửa mã Java về sau: lưu file → chạy **BUILD.bat** → chạy **RUN.bat**. Chỉ sửa file `.java` thì bản `.jar` đang chạy chưa tự thay đổi.

## Màu sắc và bố cục

| Thành phần | Màu |
| --- | --- |
| Thanh chức năng, nút chính, nhãn thông tin | Đỏ đô theo mẫu gốc `#8B0000` |
| Mục chức năng đang chọn | Đỏ sáng hơn `#A92323` |
| Tiêu đề bảng | Xanh dương nhạt `#DDEEFF` |
| Chữ tiêu đề bảng | Xanh đậm `#194475` |
| Dòng xen kẽ | Xanh rất nhạt `#F2F8FF` |
| Dòng được chọn | Xanh nhạt `#D6EAFD` |

Font chính là **Segoe UI**. Menu, nút, ô nhập, bộ lọc, thẻ thống kê và các tab dùng cùng cách căn lề, khoảng cách và bo góc. Bảng rộng có thanh cuộn ngang khi cửa sổ nhỏ; nội dung dài có chú thích khi rê chuột. Trạng thái đã hoàn thành và cảnh báo vẫn có màu riêng để dễ phân biệt.

## Sửa ở file nào về sau?

| Muốn chỉnh | File |
| --- | --- |
| Màu chung, font, bảng, nút, ô nhập, biểu tượng | [src/utils/UIUtils.java](src/utils/UIUtils.java) — các hằng màu ở đầu file |
| Thanh chức năng bên trái, tiêu đề, chọn học kỳ | [src/ui/PortalPanel.java](src/ui/PortalPanel.java) |
| Đăng nhập | [src/ui/LoginPanel.java](src/ui/LoginPanel.java) |
| Tìm kiếm và xuất CSV trên bảng | [src/utils/Ui.java](src/utils/Ui.java), hàm `tableTools` |
| Tổng quan quản trị / sinh viên | `src/ui/AdminDashboardPanel.java`, `src/ui/DashboardPanel.java` |
| Danh sách và thông tin sinh viên / giảng viên | `src/ui/AdminUserPanel.java` |
| Lớp học phần, nhập điểm | `src/ui/ClassAdminPanel.java`, `src/ui/GradePanel.java` |
| Báo cáo, thu học phí | `src/ui/AdminBaoCaoPanel.java`, `src/ui/AdminCongNoPanel.java` |
| Tài khoản, đổi mật khẩu | `src/ui/AccountAdminPanel.java`, `src/ui/AccountDialog.java` |
| Nhập Excel, cấu hình học kỳ | `src/ui/ExcelImportPanel.java`, `src/ui/TermSettingsPanel.java` |
| Lịch học, đăng ký, kết quả | `src/ui/WeeklySchedulePanel.java`, `src/ui/EnrollmentPanel.java`, `src/ui/DiemPanel.java` |
| Công nợ sinh viên, tốt nghiệp | `src/ui/CongNoPanel.java`, `src/ui/TotNghiepPanel.java` |
| Khởi tạo bộ giao diện khi mở ứng dụng | `src/App.java` |

Trang Nhật ký & Phiếu thu nhận màu và font qua bộ giao diện chung, nên không cần sửa riêng `AuditPanel.java`.

## Kiểm tra đã thực hiện

- Biên dịch thành công với đích Java 17.
- 34 kiểm tra nghiệp vụ hiện có đạt.
- Dựng 22 lượt hiển thị của các trang quản trị/sinh viên và các tab, gồm cửa sổ nhỏ 1000×700 và 1280×820; rà ảnh và kiểm tra 27 lượt tiêu đề bảng.
- Đăng nhập: kiểm tra tự kết nối thành công bằng cấu hình kiểm thử, trạng thái mất cấu hình, bố cục 1280×820 và 1000×700.
- Các kiểm tra giao diện dùng dữ liệu đọc từ CSDL kiểm thử. Đợt thay đổi này không sửa các file nghiệp vụ trong `src/service` hoặc mã kết nối trong `src/config`.

Ảnh mới nằm trong **[preview/giao_dien_do_do_xanh](preview/giao_dien_do_do_xanh)**. Đây là ảnh dựng từ Swing để rà bố cục; chưa thay thế việc thao tác toàn bộ quy trình bằng chuột trên cửa sổ thật.

Xem nhanh: [Tổng quan](preview/giao_dien_do_do_xanh/01_quan_tri_tong_quan.png) · [Sinh viên](preview/giao_dien_do_do_xanh/02_sinh_vien.png) · [Lịch học](preview/giao_dien_do_do_xanh/14_lich_hoc.png) · [Đăng nhập](preview/giao_dien_do_do_xanh/23_dang_nhap.png).
