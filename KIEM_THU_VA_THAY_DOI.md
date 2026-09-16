# Thay đổi và kết quả kiểm thử

## Dữ liệu và phạm vi

Nguồn mã: `QuanLyTinChi_Java (3).zip`. Nguồn dữ liệu: SQL Server `QuanLyTinChi` đang chạy trên máy. Hai tệp SQL được nhắc tới đều rỗng (2 byte). Đã xuất dữ liệu cũ thành script, sao chép sang `QuanLyTinChi_NangCap_20260914` và chỉ thay đổi bản sao khi kiểm thử.

Giữ nguyên 100 sinh viên, 33 giảng viên, 60 lớp, 356 đăng ký, 54 môn, 57 phiếu học phí cùng các danh mục liên quan. Bản sao chuẩn hóa 7 giá trị tiền dạng số mũ và một giá trị tín chỉ có dấu nháy thừa. Bản xuất gốc giữ các giá trị trước chuẩn hóa để đối chiếu.

## Những thay đổi chính

| Trước | Sau |
|---|---|
| Mật khẩu cố định và đăng nhập dự phòng khi mất DB | Tài khoản theo CSDL, hash có salt, đổi mật khẩu lần đầu, giới hạn số lần sai; mất kết nối không cho đăng nhập. |
| Kiểm tra đăng ký rời rạc, lỗi SQL có thể bị bỏ qua | Một giao dịch gồm kiểm tra, đăng ký/hủy, học phí và nhật ký; lỗi thì hoàn tác. |
| Có thể tranh chỗ cuối từ hai phiên | Khóa giao dịch phối hợp giữa các tiến trình. |
| Thiếu kiểm tra trùng môn/lịch và giới hạn tín chỉ | Kiểm tra tại tầng nghiệp vụ, theo cấu hình học kỳ và toàn bộ tiên quyết. |
| Thiếu màn hình nhập điểm | Nhập điểm theo lớp, xác thực 0–10, phát hiện sửa đồng thời. |
| GPA trung bình giản đơn và đổi hệ 4 tuyến tính | Trọng số tín chỉ, đổi hệ theo từng môn, học lại lấy điểm cao nhất. |
| Tốt nghiệp cố định 150 tín chỉ | Lấy số tín chỉ yêu cầu từ chương trình của sinh viên. |
| Sinh viên tự bấm để công nợ thành đã đóng | Cán bộ xác nhận thu, ghi phiếu và nhật ký; sinh viên xem hướng dẫn. |
| Cộng số tiền vào cột dạng chữ | Tính bằng BigDecimal, kiểm tra số còn nợ, tránh cộng lặp. |
| Lịch gom ca và thiếu Chủ nhật | Lịch theo ngày thật, tiết 1–15, ngày nghỉ và học bù/hủy. |
| Import đếm mã trùng như thành công | Có mẫu, xem trước, lỗi theo dòng, bỏ qua mã trùng, giao dịch khi lưu. |
| CSV đổi dấu phẩy trong nội dung | CSV có trích dẫn đúng, UTF-8 BOM và bảo vệ công thức trong ô. |
| Sửa/xóa có thể tác động dây chuyền vào học bạ | Chặn xóa sinh viên/lớp có lịch sử và giảng viên đang được phân công. |

## Kiểm thử tự động

- **12/12 kiểm tra cấu hình đạt**: đường dẫn theo thư mục ứng dụng; cấu hình mẫu khi tải từ GitHub; giữ cấu hình riêng; tên CSDL mặc định đúng; hướng dẫn khi chưa nhập mật khẩu; Unicode; thông báo lỗi SQL theo nguyên nhân.
- **34/34 kiểm tra logic đạt**: chuẩn hóa thứ/tiết; các trường hợp giao tiết; điểm 0/NULL/NaN; điểm và GPA; môn học lại; CSV; số tiền; ngày sinh; mật khẩu và phân quyền phiên.
- **40/40 kiểm tra tích hợp đạt** trên bản sao SQL Server: cấp/đổi/khóa tài khoản; đăng ký, tín chỉ, tiên quyết, lịch và sĩ số; hoàn tác khi phát sinh lỗi hóa đơn; thu một phần và toàn bộ; chống lặp giao dịch; giữ tiền đã đóng khi hủy; khóa hủy sau khi có điểm 0; phát hiện sửa điểm đồng thời; bảo vệ lịch sử; import có dòng sai/trùng; nhật ký.
- **Hai tiến trình Java độc lập** tranh một chỗ trong cùng lớp: chỉ một tiến trình đăng ký thành công, sĩ số sau cùng bằng 1.
- **17 màn hình** dựng từ dữ liệu thật trên bản sao, kiểm tra bố cục ở kích thước 1280×820. Các ảnh xem trước nằm trong `preview`.
- Khôi phục thử thành công cả 3 script SQL trong gói vào một CSDL kiểm tra riêng; chạy lại script bổ sung lần thứ hai vẫn thành công. CSDL kiểm tra đã được dọn sau khi xác minh.
- Đối chiếu toàn bộ dữ liệu của 12 bảng giữa nguồn gốc và bản nâng cấp: khớp sau khi tính đến 8 sửa định dạng đã nêu. Không còn tài khoản hoặc bản ghi kiểm thử trên bản bàn giao.
- Nhật ký kiểm thử nằm trong `verification`.
- Biên dịch với Java 21, nhắm tương thích **Java 17** (`--release 17`). Bộ kiểm thử dùng JDK 21 trên Windows.

Kiểm thử giao diện là dựng và kiểm tra màn hình Swing cùng các luồng điều hướng; không phải chứng nhận mọi thao tác thủ công trên mọi kích thước màn hình. Những giao dịch tác động CSDL được kiểm thử qua tầng nghiệp vụ.

## Sửa việc chạy bản giải nén và bản tải từ GitHub

Bản ZIP đầu để trống mật khẩu, trong khi cấu hình hợp lệ chỉ được lưu ở bản trong thư mục Codex. Mã cũ còn đọc cấu hình theo thư mục chạy lệnh và mặc định chọn CSDL gốc nếu thiếu tệp. Những điểm này đã được sửa:

- Xác định thư mục từ vị trí JAR, hoặc thư mục dự án khi chạy từ IDE; hỗ trợ đường dẫn có dấu và dấu cách.
- Khi thiếu cấu hình riêng, lấy mẫu với tên CSDL nâng cấp và tự mở hộp thiết lập kết nối ở lần chạy đầu.
- Kiểm tra kết nối và sự hiện diện của các bảng dữ liệu trước khi lưu. Lần nhập sai giữ nguyên cấu hình đang hoạt động.
- Đóng gói theo cách tải mã từ GitHub: có cấu hình mẫu, không chứa mật khẩu cá nhân; `.gitignore` bỏ qua cấu hình riêng.

Đã giải nén chính gói ZIP sang một thư mục khác có dấu/dấu cách, chạy JAR từ thư mục làm việc khác và kiểm tra: nhận đúng thư mục ứng dụng, yêu cầu thiết lập khi chưa có cấu hình, kết nối CSDL nâng cấp sau khi lưu, đọc đủ 100 sinh viên và bảo toàn cấu hình khi nhập sai mật khẩu. Đã biên dịch lại mã nguồn trong thư mục giải nén. Nhật ký tại `verification/chay_ban_giai_nen.txt`. Cấu hình tạm của lần kiểm tra này đã được xóa.

Đã dựng và kiểm tra thêm bố cục biểu mẫu kết nối. Đây là xác minh bản giải nén tại máy hiện tại; chưa có lần kiểm thử trên một máy tính vật lý khác hay kho GitHub của người dùng. Máy khác vẫn cần Java, SQL Server, dữ liệu và cấu hình của máy đó.

## Cấu trúc mã

- `src/App.java`: đăng nhập, cấu hình, thiết lập ban đầu.
- `src/ui/PortalPanel.java`: điều hướng, học kỳ chung, làm mới.
- `src/service/AccountService.java`: mật khẩu, khóa tài khoản.
- `src/service/RegistrationService.java`: đăng ký/hủy và học phí nguyên tử.
- `src/service/AcademicService.java`: điểm, GPA, tín chỉ.
- `src/service/PaymentService.java`: phiếu thu, kiểm tra tiền, chống lặp.
- `src/service/ClassService.java`: lịch, giảng viên, phòng, sức chứa.
- `src/service/ImportService.java`: mẫu, xem trước và nhập Excel.
- `src/service/ScheduleService.java`: lịch theo tuần.
- `src/utils/Db.java`: truy vấn, giao dịch, khóa phối hợp.
- `tests`: kiểm thử logic, tích hợp và dựng giao diện.

Các lớp giao diện cũ được giữ tên bằng adapter khi cần, tránh tồn tại hai cách xử lý nghiệp vụ khác nhau. Tệp kết nối cá nhân và log không được đưa vào kho mã.

## Bảng dữ liệu bổ sung

- `QLTC_TAI_KHOAN`: mật khẩu hash, vai trò, trạng thái khóa và yêu cầu đổi mật khẩu.
- `QLTC_CAU_HINH_HK`: thời gian mở đăng ký, đơn giá và giới hạn tín chỉ.
- `QLTC_NHAT_KY`: người thực hiện, thời điểm, thao tác và nội dung.
- `QLTC_PHIEU_THU`: mã giao dịch, phiếu công nợ, số tiền, người thu và ghi chú.

Các quy ước điểm và giới hạn chưa được xác nhận là quy chế chính thức của trường; xem hướng dẫn sử dụng trước khi áp dụng ngoài phạm vi đồ án. Chưa tích hợp ngân hàng, hoàn tiền, cổng giảng viên riêng hoặc hệ thống thông báo bên ngoài.
