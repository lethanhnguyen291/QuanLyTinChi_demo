# MIT PORTAL 2.0 — Bản nâng cấp quản lý tín chỉ

Bản này phát triển trực tiếp từ dự án Java Swing được cung cấp, giữ phong cách đỏ/cam và dùng dữ liệu SQL Server cũ. Chương trình, mã nguồn, thư viện, dữ liệu SQL, hướng dẫn và kiểm thử đều nằm trong cùng thư mục.

**Nếu mới giải nén hoặc tải từ GitHub:** xem [BẮT ĐẦU Ở ĐÂY](BAT_DAU_O_DAY.md). Ứng dụng tự kết nối bằng cấu hình đã lưu; nếu chưa có cấu hình, chọn **Cài đặt → Cấu hình kết nối dữ liệu**. Cấu hình nằm cạnh ứng dụng và không phụ thuộc thư mục Codex; tệp chứa mật khẩu cá nhân không có trong ZIP hay kho mã.

## Chạy trên máy hiện tại

1. Giải nén toàn bộ gói, giữ nguyên các thư mục đi kèm. Cần Java 17 trở lên; máy đã kiểm thử có Java 21.
2. Mở **RUN.bat**. Nếu muốn tự biên dịch mã nguồn, mở **BUILD.bat** trước; thao tác này cần JDK 17 trở lên.
3. Trong màn hình đăng nhập, chọn **Cài đặt → Cấu hình kết nối dữ liệu**. Tên cơ sở dữ liệu nâng cấp là **QuanLyTinChi_NangCap_20260914**, máy chủ **localhost:1433**. Nhập tài khoản và mật khẩu SQL Server trên máy. Gói không kèm mật khẩu kết nối cá nhân.
4. Chọn **Cài đặt → Tạo quản trị viên đầu tiên**, tự đặt tài khoản quản trị và mật khẩu từ 8–128 ký tự. Bước này chỉ tạo được quản trị viên đầu tiên.
5. Chọn vai trò **Cán bộ đào tạo**, đăng nhập bằng tài khoản vừa tạo.
6. Trong **Tài khoản & Ngoại ngữ**, chọn sinh viên, bấm **Cấp / Đặt lại mật khẩu**, nhập mật khẩu tạm. Sinh viên phải đổi mật khẩu ở lần đăng nhập đầu tiên.

Bản sao `QuanLyTinChi_NangCap_20260914` đã được tạo và kiểm tra trên máy trong quá trình nâng cấp. Cơ sở dữ liệu gốc `QuanLyTinChi` không bị sửa. Các tài khoản và dữ liệu kiểm thử được dọn khỏi bản sao trước khi bàn giao.

Nếu chạy trên máy khác, thực hiện phần khôi phục SQL bên dưới trước bước 3.

## Khôi phục SQL trên máy khác

Mở SQL Server Management Studio và bật **Query → SQLCMD Mode**. Chạy lần lượt:

1. `database/01_du_lieu_cu.sql` — tạo **cơ sở dữ liệu mới**, khôi phục cấu trúc và dữ liệu gốc. Script dừng nếu tên cơ sở dữ liệu đã tồn tại; không chạy lại trên bản sao đang sử dụng.
2. `database/02_chuan_hoa_du_lieu.sql` — chuẩn hóa định dạng 7 khoản tiền dạng số mũ và sửa số tín chỉ `2"` của môn `TC100` thành `2`.
3. `database/03_bo_sung_chuc_nang.sql` — bổ sung 4 bảng tài khoản, cấu hình đăng ký, nhật ký và phiếu thu. Có thể chạy lại bước 3; không xóa bảng hoặc học bạ cũ.

Sau đó mở ứng dụng, cấu hình kết nối tới máy chủ mới và **Cài đặt → Tạo quản trị viên đầu tiên**. Các script dành cho bản sao có tên nêu trên; không đổi tên đích sang cơ sở dữ liệu gốc.

Tệp SQL ban đầu được gửi có dung lượng 2 byte, nên dữ liệu bàn giao được xuất trực tiếp từ cơ sở dữ liệu `QuanLyTinChi` đang chạy: 100 sinh viên, 33 giảng viên, 60 lớp học phần, 356 lượt đăng ký, 54 môn học, 57 phiếu học phí, 14 chương trình đào tạo, 5 học kỳ, 24 quan hệ tiên quyết, 4 khoa và 21 ngày nghỉ. Bảng buổi học ngoại lệ hiện chưa có dữ liệu.

## Dùng các chức năng mới

### Đăng ký học phần

Quản trị viên chọn học kỳ, vào **Cấu hình học kỳ**, bật mở đăng ký, nhập ngày bắt đầu/kết thúc và giới hạn tín chỉ. Các kỳ mặc định đóng để không tự mở lại học kỳ lịch sử. Các kỳ trong dữ liệu cũ đều đã qua; nếu demo đăng ký, cần chủ động đặt khoảng ngày đăng ký chứa ngày demo.

Sinh viên chọn lớp trong danh sách và bấm **Đăng ký lớp đã chọn**. Chương trình kiểm tra trạng thái đang học, đợt đăng ký, trùng môn, trùng tiết, tất cả môn tiên quyết, sĩ số và tổng tín chỉ. Việc lưu đăng ký/hủy và cập nhật học phí diễn ra trong cùng giao dịch. Lớp đã có bất kỳ điểm nào, kể cả điểm 0, không được hủy.

Mức giới hạn mặc định là 24 tín chỉ. Một số dữ liệu lịch sử có hơn 24 tín chỉ: chúng được giữ nguyên, còn đăng ký mới tuân theo cấu hình học kỳ.

### Lớp học phần và lịch học

**Lớp học phần & Lịch học** hỗ trợ mở lớp, chọn môn/giảng viên từ dữ liệu, sửa lịch và xóa lớp trống. Chương trình chặn trùng phòng, trùng giảng viên, lịch mới xung đột với sinh viên đang học, sức chứa thấp hơn sĩ số và xóa lớp có đăng ký.

Sinh viên xem **Lịch học theo tuần** để thấy tiết 1–15, cả Chủ nhật, ngày nghỉ và buổi học bù/hủy từ dữ liệu hiện có. Nếu ngày hiện tại ngoài học kỳ, ứng dụng mở tuần đầu học kỳ. Tab **Danh sách & Xuất lịch** có tên giảng viên, phòng và ghi chú đầy đủ.

### Điểm và tốt nghiệp

Vào **Nhập & Quản lý điểm**, chọn lớp, sửa 3 cột điểm và bấm **Lưu điểm đã sửa**. Chỉ các dòng đã đổi được lưu; mỗi dòng cần đủ 3 điểm từ 0 đến 10. Khi phát hiện điểm vừa bị sửa từ phiên khác, chương trình yêu cầu tải lại.

Quy ước áp dụng cho đồ án:

- Điểm tổng kết = chuyên cần × 10% + giữa kỳ × 30% + cuối kỳ × 60%, làm tròn 2 chữ số.
- Đạt từ 4/10. Điểm chưa nhập hiển thị `—`, điểm 0 vẫn là điểm đã nhập.
- Hệ 4 theo các ngưỡng: 8,5→4; 8→3,5; 7→3; 6,5→2,5; 5,5→2; 5→1,5; 4→1; dưới 4→0.
- GPA có trọng số tín chỉ. Khi học lại, lấy điểm cao nhất của từng môn; không cộng trùng tín chỉ.
- Xét tốt nghiệp sơ bộ dựa trên tín chỉ yêu cầu của **chương trình đào tạo thực tế**, công nợ và chuẩn ngoại ngữ. Đây là ba điều kiện hiện có của đồ án, không bao gồm mọi điều kiện xét tốt nghiệp có thể có của từng trường.

Các quy ước điểm tập trung tại `src/service/AcademicService.java`, cần điều chỉnh nếu đề bài/quy chế trường khác. Điểm tổng kết cũ được giữ nguyên; công thức mới áp dụng khi lưu điểm mới hoặc sửa điểm.

### Học phí và phiếu thu

Sinh viên xem toàn bộ công nợ và hướng dẫn nộp tiền. Ứng dụng **chưa tích hợp cổng thanh toán ngân hàng**; thao tác của sinh viên không tự đánh dấu đã thanh toán.

Cán bộ chọn học kỳ, vào **Thu học phí**, chọn phiếu, nhập số tiền bằng số, không dùng dấu phân cách hàng nghìn. Có thể thu từng phần; không thu âm, bằng 0 hoặc vượt số còn nợ. Mỗi khoản thu có mã giao dịch và nhật ký; gửi lại cùng mã không cộng tiền lần nữa. Xem và xuất lịch sử tại **Nhật ký & Phiếu thu**.

Tiền đã đóng được giữ nguyên khi hủy môn. Nếu phát sinh đóng dư, tổng đã đóng vẫn hiển thị; chưa có quy trình chi hoàn tiền/chuyển số dư sang học kỳ khác. Phiếu thu chi tiết chỉ ghi các giao dịch từ lúc nâng cấp; số đã đóng trước đó vẫn được giữ trong dữ liệu học phí.

### Excel, tìm kiếm và báo cáo

**Nhập dữ liệu Excel** có tải mẫu `.xlsx`, xem trước, báo lỗi theo dòng và xác nhận trước khi nhập. Nhận `.xls`/`.xlsx`, tối đa 20 MB và 10.000 dòng, trang dữ liệu đầu tiên. Giữ tên cột trong mẫu; có thể đổi thứ tự cột. Ngày sinh dùng `yyyy-MM-dd`, `dd/MM/yyyy` hoặc ô ngày Excel.

Mã trùng, ngày sai, liên hệ không hợp lệ hoặc mã khoa/chương trình không tồn tại được báo trước. Dữ liệu cũ không bị ghi đè. Nếu xảy ra lỗi SQL khi lưu, toàn bộ lần nhập được hoàn tác.

Các bảng mới hỗ trợ tìm kiếm không phân biệt dấu, sắp xếp và xuất CSV UTF-8. CSV giữ tiếng Việt, dấu phẩy, dấu nháy, xuống dòng và chỉ xuất các dòng đang hiển thị sau khi lọc.

## Kịch bản demo ngắn

1. Đăng nhập quản trị; mở bảng sinh viên, thử tìm `bui duc phat`, xuất CSV.
2. Cấp tài khoản cho một sinh viên đang học, chẳng hạn `BA01002`; vào tài khoản sinh viên và đổi mật khẩu.
3. Xem lịch theo tuần, chuyển học kỳ, mở bảng điểm và đối chiếu GPA có trọng số.
4. Quay lại quản trị, xem cấu hình học kỳ và mở một đợt đăng ký dùng cho demo. Kiểm tra giới hạn tín chỉ trước khi chọn sinh viên/lớp demo.
5. Thử đăng ký hợp lệ, trùng lịch, trùng môn và hủy lớp chưa có điểm. Kiểm tra công nợ thay đổi đồng thời.
6. Nhập điểm cho một lớp; thử nhập `11` để xem kiểm tra lỗi, sau đó lưu điểm hợp lệ.
7. Ghi nhận một khoản thu thử **chỉ trên bản sao demo**, xem mã phiếu và nhật ký.
8. Tải mẫu Excel, thử một dòng trùng mã hoặc ngày sinh sai, kiểm tra thông báo trước khi nhập.

## Kiểm thử và giới hạn đã biết

Đã chạy 34 kiểm tra logic, 40 kiểm tra tích hợp trên SQL Server và dựng/kiểm tra 17 màn hình Swing. Có thử hai tiến trình đăng ký cùng chỗ cuối, hoàn tác khi tính học phí lỗi, khóa đăng nhập, thanh toán lặp, tính điểm và nhập Excel. Chi tiết tại `KIEM_THU_VA_THAY_DOI.md`.

`test.ps1` chạy lại kiểm thử logic. Kiểm thử tích hợp chỉ dùng trên bản sao dành cho kiểm thử, vì sẽ thêm/xóa các bản ghi có tiền tố `QA_`; không dùng tiền tố này cho dữ liệu thật.

Một số màn hình cũ vẫn tải dữ liệu đồng bộ trong thời gian kết nối, phù hợp quy mô đồ án trên mạng nội bộ. Mã Java desktop kết nối trực tiếp SQL Server; đây chưa phải kiến trúc máy chủ/API dùng cho triển khai đại học ở quy mô lớn. Học kỳ, danh mục khoa/môn/chương trình và lịch nghỉ/bù bổ sung vẫn quản lý qua SQL nếu cần tạo thêm ngoài dữ liệu hiện có.

## Khi không chạy được

| Hiện tượng | Cách xử lý |
|---|---|
| Không có `java` hoặc `javac` | Cài JDK 17+, mở lại cửa sổ chạy. |
| Không kết nối SQL Server | Bật dịch vụ SQL Server, kiểm tra TCP/IP và cổng, cấu hình URL/tài khoản trong màn hình đăng nhập. |
| Có cơ sở dữ liệu nhưng không đăng nhập được | Tạo quản trị viên bằng **Cài đặt → Tạo quản trị viên đầu tiên**; sinh viên cần được cấp tài khoản. Mật khẩu `123` của bản cũ không còn dùng. |
| Thông báo đợt đăng ký đóng | Quản trị cập nhật khoảng ngày và bật mở đăng ký trong **Cấu hình học kỳ**. |
| Điểm sửa chưa hiển thị ở tài khoản khác | Bấm **Làm mới dữ liệu** hoặc chuyển lại trang. |
| Thiếu thư viện khi mở JAR | Giữ nguyên thư mục `lib` cạnh `QuanLyTinChi.jar`; ưu tiên mở `RUN.bat`. |
| Chương trình gặp lỗi | Xem tệp mới nhất trong `logs`; không gửi kèm mật khẩu SQL khi chia sẻ log/cấu hình. |

Nguồn tham khảo kỹ thuật: [khóa giao dịch SQL Server](https://learn.microsoft.com/en-us/sql/relational-databases/system-stored-procedures/sp-getapplock-transact-sql) và [lưu mật khẩu PBKDF2 theo OWASP](https://cheatsheetseries.owasp.org/cheatsheets/Password_Storage_Cheat_Sheet.html). Bản nâng cấp dùng PBKDF2-HMAC-SHA256, salt ngẫu nhiên và 600.000 vòng lặp.
