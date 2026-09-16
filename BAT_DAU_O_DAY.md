# Chạy bản giải nén hoặc bản tải từ GitHub

**Cập nhật giao diện 16/09:** [Chỉ cần thay 4 file mã nguồn](CAP_NHAT_2026-09-16.md).

Ứng dụng là Java desktop dùng SQL Server. Máy chạy cần **Java 17+**, **SQL Server** và **cơ sở dữ liệu đã khôi phục**. GitHub lưu mã và các tệp SQL; tải mã về không tự tạo cơ sở dữ liệu trên máy mới.

## Trên máy của bạn đang có dữ liệu nâng cấp

1. Giải nén toàn bộ thư mục. Giữ `QuanLyTinChi.jar`, `lib`, `config` và các tệp chạy cạnh nhau.
2. Mở **RUN.bat**. Ứng dụng tự kiểm tra kết nối ở nền. Nếu chưa có cấu hình, chọn **Cài đặt → Cấu hình kết nối dữ liệu**.
3. Giữ địa chỉ mặc định nếu dùng SQL Server hiện tại trên `localhost:1433`, CSDL `QuanLyTinChi_NangCap_20260914`.
4. Nhập **tài khoản và mật khẩu SQL Server** đang dùng trên máy, bấm **Kiểm tra & Lưu**. Đây không phải tài khoản sinh viên. Mật khẩu không được đóng sẵn trong ZIP hay đưa lên GitHub.
5. Khi kết nối thành công, nếu CSDL chưa có quản trị viên, bấm **Cài đặt → Tạo quản trị viên đầu tiên**. Nếu đã tạo tài khoản trên cùng CSDL thì đăng nhập bằng tài khoản đó, không cần tạo lại.

Các bản giải nén cùng kết nối tới một CSDL sẽ dùng chung dữ liệu và tài khoản ứng dụng. Mỗi thư mục ứng dụng chỉ cần nhập cấu hình SQL Server một lần.

## Trên máy khác

1. Cài Java 17+ và SQL Server. Nếu cần biên dịch mã, dùng **JDK 17+**.
2. Trong SQL Server Management Studio, bật **Query → SQLCMD Mode** và chạy lần lượt 3 tệp trong `database`: `01_du_lieu_cu.sql`, `02_chuan_hoa_du_lieu.sql`, `03_bo_sung_chuc_nang.sql`.
3. Mở **RUN.bat**, nhập địa chỉ và tài khoản SQL Server của máy mới. Địa chỉ mẫu dành cho cổng 1433; thay máy chủ/cổng nếu nơi cài SQL Server dùng giá trị khác.
4. Bấm **Kiểm tra & Lưu**, sau đó **Cài đặt → Tạo quản trị viên đầu tiên** để tạo quản trị viên.

Script số 1 dừng nếu CSDL đích đã tồn tại. Không chạy lại script đó trên bản đang sử dụng.

## Đưa mã lên GitHub và tải về chạy

Đưa toàn bộ dự án, bao gồm `src`, `lib`, `database`, `tests`, `config/database.properties.example`, tệp JAR và các tệp chạy/hướng dẫn lên kho mã. Giữ `.gitignore` đi kèm: tệp `config/database.properties` chứa mật khẩu riêng nên được bỏ qua.

Sau khi clone hoặc tải ZIP từ GitHub, mở **RUN.bat** và thiết lập kết nối như trên. Nếu đã sửa mã nguồn, chạy **BUILD.bat** trước. Không có đường dẫn bắt buộc tới thư mục Codex; có thể đặt dự án ở thư mục khác.

Ứng dụng này chạy trong cửa sổ Java trên máy tính. Đưa mã lên GitHub không biến ứng dụng Swing thành trang web.

Nếu từng đưa tệp cấu hình có mật khẩu lên GitHub trước đó, `.gitignore` không tự xóa tệp đã được theo dõi. Gỡ tệp đó khỏi kho và đổi mật khẩu đã công khai.

## Bản sửa này giải quyết gì?

- Bản trước chỉ có cấu hình hoạt động trong thư mục Codex; ZIP vẫn chưa có mật khẩu. Khi thiếu tệp cấu hình, mã cũ còn chọn nhầm CSDL mặc định.
- Bản này dùng đúng tên CSDL nâng cấp, tìm cấu hình cạnh ứng dụng và hướng dẫn thiết lập qua mục Cài đặt khi thiếu thông tin.
- Chỉ lưu cấu hình sau khi đã kết nối và kiểm tra đủ bảng dữ liệu. Nhập sai không ghi đè cấu hình đang hoạt động.

Xem [hướng dẫn đầy đủ](HUONG_DAN_SU_DUNG.md) và [các chức năng/kiểm thử](KIEM_THU_VA_THAY_DOI.md).
