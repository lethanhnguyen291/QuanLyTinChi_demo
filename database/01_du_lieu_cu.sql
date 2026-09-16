:ON ERROR EXIT
-- Snapshot of existing QuanLyTinChi, 2026-09-14. Original database is not modified.
-- Run on a NEW database only. Never overwrite the original.
USE [master]
GO
IF DB_ID(N'QuanLyTinChi_NangCap_20260914') IS NOT NULL THROW 50001,N'Database already exists. Choose a new name or use the existing copy.',1;
GO
CREATE DATABASE [QuanLyTinChi_NangCap_20260914] COLLATE Latin1_General_100_CI_AI_SC_UTF8
GO
USE [QuanLyTinChi_NangCap_20260914]
GO
CREATE TABLE [dbo].[BUOI_HOC_NGOAI_LE](
  [MaBuoi] varchar(50) NOT NULL,
  [MaLHP] varchar(50) NOT NULL,
  [Ngay] date NOT NULL,
  [TietHoc] nvarchar(20) NULL,
  [PhongHoc] nvarchar(20) NULL,
  [LoaiBuoi] nvarchar(5) NOT NULL DEFAULT (N'Bu'),
  [GhiChu] nvarchar(300) NULL
);
GO
CREATE TABLE [dbo].[CHUONG_TRINH_DAO_TAO](
  [MaCTDT] varchar(50) NOT NULL,
  [TenCTDT] nvarchar(255) NOT NULL,
  [TongTinChiYeuCau] varchar(10) NULL,
  [NienKhoaApDung] varchar(50) NULL,
  [MaKhoa] nvarchar(20) NULL
);
GO
INSERT INTO [dbo].[CHUONG_TRINH_DAO_TAO]([MaCTDT],[TenCTDT],[TongTinChiYeuCau],[NienKhoaApDung],[MaKhoa]) VALUES(N'AC01',N'Kế toán',N'140',N'2024-2028',N'KTQL');
GO
INSERT INTO [dbo].[CHUONG_TRINH_DAO_TAO]([MaCTDT],[TenCTDT],[TongTinChiYeuCau],[NienKhoaApDung],[MaKhoa]) VALUES(N'AU01',N'Kỹ thuật Ô tô',N'150',N'2024-2028',N'CNKT');
GO
INSERT INTO [dbo].[CHUONG_TRINH_DAO_TAO]([MaCTDT],[TenCTDT],[TongTinChiYeuCau],[NienKhoaApDung],[MaKhoa]) VALUES(N'BA01',N'Quản trị Kinh doanh',N'140',N'2024-2028',N'KTQL');
GO
INSERT INTO [dbo].[CHUONG_TRINH_DAO_TAO]([MaCTDT],[TenCTDT],[TongTinChiYeuCau],[NienKhoaApDung],[MaKhoa]) VALUES(N'EE01',N'Kỹ thuật Điện – Điện tử',N'148',N'2024-2028',N'CNKT');
GO
INSERT INTO [dbo].[CHUONG_TRINH_DAO_TAO]([MaCTDT],[TenCTDT],[TongTinChiYeuCau],[NienKhoaApDung],[MaKhoa]) VALUES(N'EN01',N'Ngôn ngữ Anh',N'132',N'2024-2028',N'NN');
GO
INSERT INTO [dbo].[CHUONG_TRINH_DAO_TAO]([MaCTDT],[TenCTDT],[TongTinChiYeuCau],[NienKhoaApDung],[MaKhoa]) VALUES(N'IT01',N'Công nghệ Thông tin',N'150',N'2024-2028',N'CNKT');
GO
INSERT INTO [dbo].[CHUONG_TRINH_DAO_TAO]([MaCTDT],[TenCTDT],[TongTinChiYeuCau],[NienKhoaApDung],[MaKhoa]) VALUES(N'IT02',N'Kỹ thuật Phần mềm',N'148',N'2024-2028',N'CNKT');
GO
INSERT INTO [dbo].[CHUONG_TRINH_DAO_TAO]([MaCTDT],[TenCTDT],[TongTinChiYeuCau],[NienKhoaApDung],[MaKhoa]) VALUES(N'JP01',N'Ngôn ngữ Nhật',N'132',N'2024-2028',N'NN');
GO
INSERT INTO [dbo].[CHUONG_TRINH_DAO_TAO]([MaCTDT],[TenCTDT],[TongTinChiYeuCau],[NienKhoaApDung],[MaKhoa]) VALUES(N'KR01',N'Ngôn ngữ Hàn',N'132',N'2024-2028',N'NN');
GO
INSERT INTO [dbo].[CHUONG_TRINH_DAO_TAO]([MaCTDT],[TenCTDT],[TongTinChiYeuCau],[NienKhoaApDung],[MaKhoa]) VALUES(N'LA01',N'Logistics & Quản lý Chuỗi cung ứng',N'144',N'2024-2028',N'KTQL');
GO
INSERT INTO [dbo].[CHUONG_TRINH_DAO_TAO]([MaCTDT],[TenCTDT],[TongTinChiYeuCau],[NienKhoaApDung],[MaKhoa]) VALUES(N'MK01',N'Marketing',N'140',N'2024-2028',N'KTQL');
GO
INSERT INTO [dbo].[CHUONG_TRINH_DAO_TAO]([MaCTDT],[TenCTDT],[TongTinChiYeuCau],[NienKhoaApDung],[MaKhoa]) VALUES(N'ML01',N'Y khoa',N'180',N'2024-2028',N'KHSK');
GO
INSERT INTO [dbo].[CHUONG_TRINH_DAO_TAO]([MaCTDT],[TenCTDT],[TongTinChiYeuCau],[NienKhoaApDung],[MaKhoa]) VALUES(N'NU01',N'Điều dưỡng',N'145',N'2024-2028',N'KHSK');
GO
INSERT INTO [dbo].[CHUONG_TRINH_DAO_TAO]([MaCTDT],[TenCTDT],[TongTinChiYeuCau],[NienKhoaApDung],[MaKhoa]) VALUES(N'PH01',N'Dược học',N'148',N'2024-2028',N'KHSK');
GO
CREATE TABLE [dbo].[CONG_NO_HOC_PHI](
  [MaPhieu] varchar(100) NOT NULL,
  [MaSV] varchar(50) NULL,
  [MaHK] varchar(50) NULL,
  [TongTienPhaiDong] decimal(15,2) NULL,
  [SoTienDaDong] varchar(50) NULL,
  [TrangThai] nvarchar(100) NULL
);
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_AC01001_HK1_2425',N'AC01001',N'HK1_2425',9000000.00,N'9e+006',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_AC01002_HK1_2425',N'AC01002',N'HK1_2425',7650000.00,N'2295000',N'Đóng một phần');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_AU01001_HK1_2425',N'AU01001',N'HK1_2425',8100000.00,N'5670000',N'Đóng một phần');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_AU01002_HK1_2425',N'AU01002',N'HK1_2425',6750000.00,N'0',N'Chưa đóng');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_AU01003_HK1_2425',N'AU01003',N'HK1_2425',8550000.00,N'8550000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_AU01004_HK1_2425',N'AU01004',N'HK1_2425',6750000.00,N'6.75e+006',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_BA01001_HK1_2425',N'BA01001',N'HK1_2425',7650000.00,N'7650000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_BA01002_HK1_2324',N'BA01002',N'HK1_2324',10800000.00,N'10800000',N'Da hoan thanh');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_BA01002_HK1_2425',N'BA01002',N'HK1_2425',8100000.00,N'8.1e+006',N'Da hoan thanh');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_BA01002_HK1_2526',N'BA01002',N'HK1_2526',11700000.00,N'1.08e+007',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_BA01002_HK2_2425',N'BA01002',N'HK2_2425',9450000.00,N'0',N'Chua dong');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_BA01003_HK1_2425',N'BA01003',N'HK1_2425',8100000.00,N'8100000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_BA01004_HK1_2425',N'BA01004',N'HK1_2425',8550000.00,N'8550000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_EE01001_HK1_2425',N'EE01001',N'HK1_2425',7200000.00,N'5040000',N'Đóng một phần');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_EE01002_HK1_2425',N'EE01002',N'HK1_2425',8100000.00,N'0',N'Chưa đóng');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_EE01003_HK1_2425',N'EE01003',N'HK1_2425',8100000.00,N'5670000',N'Đóng một phần');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_EE01004_HK1_2425',N'EE01004',N'HK1_2425',8100000.00,N'0',N'Chưa đóng');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_EE01005_HK1_2425',N'EE01005',N'HK1_2425',9000000.00,N'9000000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_EN01001_HK1_2425',N'EN01001',N'HK1_2425',8550000.00,N'5985000',N'Đóng một phần');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_EN01002_HK1_2425',N'EN01002',N'HK1_2425',8100000.00,N'8100000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_EN01003_HK1_2425',N'EN01003',N'HK1_2425',7200000.00,N'7200000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_IT01001_HK1_2425',N'IT01001',N'HK1_2425',11700000.00,N'8.55e+006',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_IT01002_HK1_2425',N'IT01002',N'HK1_2425',9000000.00,N'2700000',N'Đóng một phần');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_IT01003_HK1_2425',N'IT01003',N'HK1_2425',8550000.00,N'8550000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_IT01004_HK1_2425',N'IT01004',N'HK1_2425',9000000.00,N'0',N'Chưa đóng');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_IT01005_HK1_2425',N'IT01005',N'HK1_2425',10350000.00,N'10350000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_IT01008_HK2_2425',N'IT01008',N'HK2_2425',4950000.00,N'0',N'Chưa đóng');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_IT01009_HK1_2324',N'IT01009',N'HK1_2324',5400000.00,N'0',N'Chưa đóng');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_IT01009_HK1_2425',N'IT01009',N'HK1_2425',11700000.00,N'0',N'Chưa đóng');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_IT01009_HK1_2526',N'IT01009',N'HK1_2526',1800000.00,N'1.8e+006',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_IT02001_HK1_2425',N'IT02001',N'HK1_2425',8550000.00,N'2565000',N'Đóng một phần');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_IT02002_HK1_2425',N'IT02002',N'HK1_2425',8550000.00,N'5985000',N'Đóng một phần');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_IT02003_HK1_2425',N'IT02003',N'HK1_2425',9000000.00,N'9000000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_IT02004_HK1_2425',N'IT02004',N'HK1_2425',9000000.00,N'0',N'Chưa đóng');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_JP01001_HK1_2425',N'JP01001',N'HK1_2425',9450000.00,N'9450000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_JP01002_HK1_2425',N'JP01002',N'HK1_2425',9000000.00,N'0',N'Chưa đóng');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_JP01003_HK1_2425',N'JP01003',N'HK1_2425',7650000.00,N'5355000',N'Đóng một phần');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_JP01004_HK1_2425',N'JP01004',N'HK1_2425',8550000.00,N'8.55e+006',N'Da hoan thanh');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_KR01001_HK1_2425',N'KR01001',N'HK1_2425',8100000.00,N'4050000',N'Đóng một phần');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_KR01002_HK1_2425',N'KR01002',N'HK1_2425',9000000.00,N'9000000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_KR01003_HK1_2425',N'KR01003',N'HK1_2425',9000000.00,N'9000000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_LA01001_HK1_2425',N'LA01001',N'HK1_2425',8100000.00,N'4050000',N'Đóng một phần');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_LA01002_HK1_2425',N'LA01002',N'HK1_2425',8550000.00,N'8550000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_LA01003_HK1_2425',N'LA01003',N'HK1_2425',7650000.00,N'7650000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_MK01001_HK1_2425',N'MK01001',N'HK1_2425',7200000.00,N'7200000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_MK01002_HK1_2425',N'MK01002',N'HK1_2425',9450000.00,N'6615000',N'Đóng một phần');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_MK01003_HK1_2425',N'MK01003',N'HK1_2425',8100000.00,N'8100000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_ML01001_HK1_2425',N'ML01001',N'HK1_2425',8550000.00,N'8550000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_ML01002_HK1_2425',N'ML01002',N'HK1_2425',8550000.00,N'5985000',N'Đóng một phần');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_ML01003_HK1_2425',N'ML01003',N'HK1_2425',8550000.00,N'4275000',N'Đóng một phần');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_ML01004_HK1_2425',N'ML01004',N'HK1_2425',7650000.00,N'2295000',N'Đóng một phần');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_NU01001_HK1_2425',N'NU01001',N'HK1_2425',9000000.00,N'9000000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_NU01002_HK1_2425',N'NU01002',N'HK1_2425',9450000.00,N'9450000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_NU01003_HK1_2425',N'NU01003',N'HK1_2425',9000000.00,N'9000000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_PH01001_HK1_2425',N'PH01001',N'HK1_2425',8550000.00,N'8550000',N'Đã hoàn thành');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_PH01002_HK1_2425',N'PH01002',N'HK1_2425',9000000.00,N'0',N'Chưa đóng');
GO
INSERT INTO [dbo].[CONG_NO_HOC_PHI]([MaPhieu],[MaSV],[MaHK],[TongTienPhaiDong],[SoTienDaDong],[TrangThai]) VALUES(N'HP_PH01003_HK1_2425',N'PH01003',N'HK1_2425',8100000.00,N'8100000',N'Đã hoàn thành');
GO
CREATE TABLE [dbo].[GIANG_VIEN](
  [MaGV] varchar(50) NOT NULL,
  [HoTen] nvarchar(255) NOT NULL,
  [GioiTinh] nvarchar(20) NULL,
  [HocVi] nvarchar(100) NULL,
  [SoDienThoai] varchar(50) NULL,
  [Email] varchar(100) NULL,
  [MaKhoa] nvarchar(20) NULL
);
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVCNKT01',N'Phạm Thị Nga',N'Nữ',N'Thạc sĩ',N'845758951',N'gvcnkt01@eaut.edu.vn',N'CNKT');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVCNKT02',N'Đặng Hữu Đạt',N'Nam',N'Tiến sĩ',N'348866764',N'gvcnkt02@eaut.edu.vn',N'CNKT');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVCNKT03',N'Phạm Văn Đạt',N'Nam',N'Thạc sĩ',N'920090262',N'gvcnkt03@eaut.edu.vn',N'CNKT');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVCNKT04',N'Phan Thu Thảo',N'Nữ',N'PGS.TS',N'375139339',N'gvcnkt04@eaut.edu.vn',N'CNKT');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVCNKT05',N'Trần Minh Trí',N'Nam',N'Tiến sĩ',N'920425262',N'gvcnkt05@eaut.edu.vn',N'CNKT');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVCNKT06',N'Phạm Thanh Tâm',N'Nữ',N'Thạc sĩ',N'826102838',N'gvcnkt06@eaut.edu.vn',N'CNKT');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVCNKT07',N'Võ Thu Trâm',N'Nữ',N'Tiến sĩ',N'897770184',N'gvcnkt07@eaut.edu.vn',N'CNKT');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVCNKT08',N'Phạm Ngọc Nhi',N'Nữ',N'Tiến sĩ',N'922948995',N'gvcnkt08@eaut.edu.vn',N'CNKT');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVCNKT09',N'Võ Đức Thắng',N'Nam',N'Thạc sĩ',N'943114586',N'gvcnkt09@eaut.edu.vn',N'CNKT');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVKHSK01',N'Phạm Quang Văn Long',N'Nam',N'Tiến sĩ',N'893633706',N'gvkhsk01@eaut.edu.vn',N'KHSK');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVKHSK02',N'Vũ Thu Trâm',N'Nữ',N'Thạc sĩ',N'346959437',N'gvkhsk02@eaut.edu.vn',N'KHSK');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVKHSK03',N'Bùi Khắc Văn Bảo',N'Nam',N'PGS.TS',N'847049567',N'gvkhsk03@eaut.edu.vn',N'KHSK');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVKHSK04',N'Lý Phương Vy',N'Nữ',N'PGS.TS',N'345614496',N'gvkhsk04@eaut.edu.vn',N'KHSK');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVKHSK05',N'Hồ Cẩm Thị Nhi',N'Nữ',N'PGS.TS',N'972658316',N'gvkhsk05@eaut.edu.vn',N'KHSK');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVKHSK06',N'Hoàng Hữu Hải',N'Nam',N'Tiến sĩ',N'894718191',N'gvkhsk06@eaut.edu.vn',N'KHSK');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVKHSK07',N'Ngô Khắc Hữu Anh',N'Nam',N'Tiến sĩ',N'919001180',N'gvkhsk07@eaut.edu.vn',N'KHSK');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVKHSK08',N'Bùi Thu Trang',N'Nữ',N'Thạc sĩ',N'356523673',N'gvkhsk08@eaut.edu.vn',N'KHSK');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVKHSK09',N'Hồ Thị Lan',N'Nữ',N'Thạc sĩ',N'994615770',N'gvkhsk09@eaut.edu.vn',N'KHSK');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVKTQL01',N'Bùi Thanh Vy',N'Nữ',N'Thạc sĩ',N'340058017',N'gvktql01@eaut.edu.vn',N'KTQL');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVKTQL02',N'Lê Thị Thảo',N'Nữ',N'Tiến sĩ',N'378518809',N'gvktql02@eaut.edu.vn',N'KTQL');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVKTQL03',N'Nguyễn Tuyết Ngọc Nga',N'Nữ',N'Tiến sĩ',N'793625968',N'gvktql03@eaut.edu.vn',N'KTQL');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVNN01',N'Hoàng Đức Duy',N'Nam',N'Tiến sĩ',N'815369179',N'gvnn01@eaut.edu.vn',N'NN');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVNN02',N'Bùi Tuyết Thu Dung',N'Nữ',N'Tiến sĩ',N'817826326',N'gvnn02@eaut.edu.vn',N'NN');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVNN03',N'Dương Ngọc Nhi',N'Nữ',N'Thạc sĩ',N'940098405',N'gvnn03@eaut.edu.vn',N'NN');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVNN04',N'Phạm Thu Quyên',N'Nữ',N'Tiến sĩ',N'896987269',N'gvnn04@eaut.edu.vn',N'NN');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVNN05',N'Đặng Hữu Huy',N'Nam',N'Tiến sĩ',N'972357749',N'gvnn05@eaut.edu.vn',N'NN');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVNN06',N'Võ Văn Long',N'Nam',N'Thạc sĩ',N'766883057',N'gvnn06@eaut.edu.vn',N'NN');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVNN07',N'Lý Thanh Vy',N'Nữ',N'Tiến sĩ',N'855015514',N'gvnn07@eaut.edu.vn',N'NN');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVNN08',N'Lê Quang Văn Nam',N'Nam',N'Tiến sĩ',N'915071279',N'gvnn08@eaut.edu.vn',N'NN');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVNN09',N'Bùi Phương Tâm',N'Nữ',N'Tiến sĩ',N'385424534',N'gvnn09@eaut.edu.vn',N'NN');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVNN10',N'Phạm Thanh Thảo',N'Nữ',N'Thạc sĩ',N'989428465',N'gvnn10@eaut.edu.vn',N'NN');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVNN11',N'Ngô Hải Thu Vy',N'Nữ',N'Thạc sĩ',N'969009928',N'gvnn11@eaut.edu.vn',N'NN');
GO
INSERT INTO [dbo].[GIANG_VIEN]([MaGV],[HoTen],[GioiTinh],[HocVi],[SoDienThoai],[Email],[MaKhoa]) VALUES(N'GVNN12',N'Bùi Gia Đạt',N'Nam',N'Tiến sĩ',N'961051571',N'gvnn12@eaut.edu.vn',N'NN');
GO
CREATE TABLE [dbo].[HOC_KY](
  [MaHK] varchar(50) NOT NULL,
  [TenHK] nvarchar(255) NOT NULL,
  [NamHoc] varchar(50) NOT NULL,
  [NgayBatDau] date NULL,
  [NgayKetThuc] date NULL
);
GO
INSERT INTO [dbo].[HOC_KY]([MaHK],[TenHK],[NamHoc],[NgayBatDau],[NgayKetThuc]) VALUES(N'HK1_2324',N'Học kỳ 1 – Năm học 2023-2024',N'2023-2024',N'2023-09-04',N'2024-01-05');
GO
INSERT INTO [dbo].[HOC_KY]([MaHK],[TenHK],[NamHoc],[NgayBatDau],[NgayKetThuc]) VALUES(N'HK1_2425',N'Học kỳ 1 – Năm học 2024-2025',N'2024-2025',N'2024-08-12',N'2024-12-31');
GO
INSERT INTO [dbo].[HOC_KY]([MaHK],[TenHK],[NamHoc],[NgayBatDau],[NgayKetThuc]) VALUES(N'HK1_2526',N'Học kỳ 1 – Năm học 2025-2026',N'2025-2026',N'2025-08-11',N'2025-12-31');
GO
INSERT INTO [dbo].[HOC_KY]([MaHK],[TenHK],[NamHoc],[NgayBatDau],[NgayKetThuc]) VALUES(N'HK2_2324',N'Học kỳ 2 – Năm học 2023-2024',N'2023-2024',N'2024-01-15',N'2024-05-24');
GO
INSERT INTO [dbo].[HOC_KY]([MaHK],[TenHK],[NamHoc],[NgayBatDau],[NgayKetThuc]) VALUES(N'HK2_2425',N'Học kỳ 2 – Năm học 2024-2025',N'2024-2025',N'2025-01-06',N'2025-05-31');
GO
CREATE TABLE [dbo].[KET_QUA_DANG_KY](
  [MaSV] varchar(50) NOT NULL,
  [MaLHP] varchar(50) NOT NULL,
  [NgayDangKy] date NULL,
  [DiemChuyenCan] varchar(20) NULL,
  [DiemGiuaKy] varchar(20) NULL,
  [DiemCuoiKy] varchar(20) NULL,
  [DiemTongKet] varchar(20) NULL,
  [TrangThai] nvarchar(100) NULL
);
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AC01001',N'CT101_N01',N'2025-08-18',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AC01001',N'CT103_N01',N'2025-08-16',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AC01001',N'CT110_N01',N'2025-08-16',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AC01001',N'ML010_N01',N'2025-08-20',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AC01001',N'TN001_N02',N'2025-08-15',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AC01001',N'TN002_N01',N'2025-08-18',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AC01002',N'CT101_N03',N'2025-08-15',N'9.5',N'5.5',N'5.8',N'6.08',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AC01002',N'CT107_N01',N'2025-08-18',N'9.5',N'4.6',N'4.8',N'5.21',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AC01002',N'CT109_N01',N'2025-08-16',N'6.3',N'6.5',N'6.2',N'6.3',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AC01002',N'CT110_N02',N'2025-08-20',N'9.3',N'7.1',N'7.6',N'7.62',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AC01002',N'KL001_N01',N'2025-08-16',N'9.5',N'6.0',N'3.1',N'4.61',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AC01002',N'XH001_N01',N'2025-08-15',N'7.3',N'5.9',N'7.4',N'6.94',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01001',N'CT103_N01',N'2025-08-18',N'9.1',N'5.0',N'3.2',N'4.33',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01001',N'CT104_N01',N'2025-08-17',N'8.0',N'6.8',N'3.8',N'5.12',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01001',N'CT106_N01',N'2025-08-20',N'6.5',N'7.9',N'3.6',N'5.18',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01001',N'CT107_HK2N01',N'2025-12-20',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01001',N'CT107_N01',N'2025-08-15',N'5.7',N'8.5',N'8.2',N'8.04',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01001',N'CT110_HK2N01',N'2025-12-20',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01001',N'CT110_N02',N'2025-08-20',N'6.8',N'8.1',N'9.4',N'8.75',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01001',N'ML010_HK2N01',N'2025-12-21',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01001',N'TN001_N01',N'2025-08-18',N'10.0',N'6.8',N'6.4',N'6.88',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01001',N'TN002_HK2N01',N'2025-12-21',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01001',N'XH001_HK2N01',N'2025-12-22',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01002',N'CT103_HK2N01',N'2025-12-20',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01002',N'CT104_N01',N'2025-08-16',N'6.2',N'5.6',N'6.6',N'6.26',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01002',N'CT107_N01',N'2025-08-20',N'5.4',N'9.6',N'8.3',N'8.4',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01002',N'CT110_N02',N'2025-08-16',N'8.9',N'8.7',N'6.0',N'7.1',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01002',N'KL001_HK2N01',N'2025-12-21',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01002',N'KL001_N01',N'2025-08-15',N'9.7',N'5.2',N'3.1',N'4.39',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01002',N'ML011_HK2N01',N'2025-12-20',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01002',N'XH001_N02',N'2025-08-15',N'6.4',N'4.3',N'7.6',N'6.49',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01002',N'XH002_N01',N'2025-08-17',N'6.3',N'4.4',N'5.7',N'5.37',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01002',N'XH003_HK2N01',N'2025-12-22',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01003',N'CT101_FULL',N'2026-08-18',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01003',N'CT106_N02',N'2025-08-20',N'8.5',N'7.9',N'9.9',N'9.16',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01003',N'CT112_N02',N'2025-08-15',N'5.5',N'5.9',N'3.0',N'4.12',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01003',N'ML009_N01',N'2025-08-17',N'9.8',N'10.0',N'4.6',N'6.74',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01003',N'TN010_N01',N'2025-08-20',N'9.5',N'9.0',N'3.3',N'5.63',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01003',N'XH001_N02',N'2025-08-16',N'8.4',N'5.8',N'7.1',N'6.84',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01003',N'XH003_N01',N'2025-08-15',N'9.7',N'9.6',N'3.4',N'5.89',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01004',N'CT110_N02',N'2025-08-17',N'5.4',N'7.5',N'9.5',N'8.49',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01004',N'CT114_N01',N'2025-08-15',N'7.6',N'6.6',N'9.7',N'8.56',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01004',N'KL001_N01',N'2025-08-20',N'6.3',N'8.1',N'4.9',N'6.0',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01004',N'ML009_N01',N'2025-08-17',N'5.6',N'6.1',N'4.2',N'4.91',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01004',N'TN001_N01',N'2025-08-15',N'8.0',N'9.7',N'6.6',N'7.67',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'AU01004',N'XH002_N01',N'2025-08-20',N'9.0',N'4.2',N'3.7',N'4.38',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01001',N'CT103_HK2N01',N'2025-12-20',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01001',N'CT106_N01',N'2025-08-20',N'9.3',N'5.7',N'4.0',N'5.04',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01001',N'CT107_N01',N'2025-08-20',N'7.9',N'4.3',N'4.5',N'4.78',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01001',N'CT110_N02',N'2025-08-15',N'7.9',N'7.8',N'5.6',N'6.49',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01001',N'CT112_HK2N01',N'2025-12-21',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01001',N'CT114_N01',N'2025-08-20',N'8.6',N'7.4',N'7.3',N'7.46',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01001',N'ML009_N01',N'2025-08-15',N'7.9',N'7.4',N'7.8',N'7.69',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01001',N'ML011_HK2N01',N'2025-12-20',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01001',N'TN001_HK2N01',N'2025-12-21',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01001',N'XH001_N01',N'2025-08-18',N'8.4',N'6.6',N'5.2',N'5.94',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01001',N'XH002_HK2N01',N'2025-12-22',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'CT101_HK0N01',N'2024-08-20',N'9.0',N'7.5',N'7.0',N'7.45',N'Dat');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'CT101_HK3N01',N'2026-08-18',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'CT101_N03',N'2025-08-15',N'5.6',N'6.6',N'8.3',N'7.52',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'CT103_HK2N01',N'2025-12-21',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'CT103_N01',NULL,NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'CT106_HK0N01',N'2024-08-20',N'8.5',N'6.5',N'7.2',N'7.18',N'Dat');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'CT106_HK3N01',N'2026-08-20',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'CT106_N01',N'2025-08-18',N'9.8',N'6.9',N'3.5',N'5.15',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'CT107_HK0N01',N'2024-08-20',N'7.5',N'6.0',N'6.8',N'6.68',N'Dat');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'CT107_HK3N01',N'2026-08-19',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'CT109_HK3N01',N'2026-08-19',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'CT109_N01',N'2025-08-15',N'9.7',N'8.3',N'7.5',N'7.96',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'CT110_HK2N01',N'2025-12-22',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'CT112_HK2N01',N'2025-12-20',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'CT114_N01',N'2025-08-15',N'6.1',N'4.9',N'9.8',N'7.96',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'KL001_HK3N01',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'ML009_HK0N01',N'2024-08-21',N'9.5',N'8.0',N'8.5',N'8.55',N'Dat');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'ML009_HK3N01',N'2026-08-18',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'ML009_N01',N'2025-08-20',N'9.3',N'9.8',N'4.7',N'6.69',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'ML010_HK2N01',N'2025-12-21',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'ML011_HK2N01',N'2025-12-20',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'TN001_HK0N01',N'2024-08-21',N'8.0',N'7.0',N'7.5',N'7.45',N'Dat');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'TN001_HK2N01',N'2025-12-22',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'TN002_HK3N01',N'2026-08-21',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'XH001_HK0N01',N'2024-08-22',N'9.0',N'8.5',N'4.5',N'6.3',N'Dat');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'XH001_HK2N01',N'2025-12-23',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'XH002_N01',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01002',N'XH003_HK3N01',N'2026-08-21',NULL,NULL,NULL,NULL,N'Chua co diem');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01003',N'CT103_N02',N'2025-08-17',N'9.3',N'6.8',N'8.5',N'8.07',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01003',N'CT107_N01',N'2025-08-20',N'5.4',N'5.2',N'8.3',N'7.08',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01003',N'CT109_N01',N'2025-08-16',N'9.6',N'5.1',N'8.6',N'7.65',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01003',N'KL001_N01',N'2025-08-15',N'7.4',N'9.7',N'9.7',N'9.47',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01003',N'ML010_N01',N'2025-08-17',N'9.1',N'8.6',N'7.3',N'7.87',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01003',N'XH001_N01',N'2025-08-18',N'8.9',N'9.5',N'8.7',N'8.96',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01004',N'CT103_N02',N'2025-08-16',N'5.3',N'6.1',N'8.3',N'7.34',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01004',N'CT104_N01',N'2025-08-16',N'6.6',N'7.7',N'9.3',N'8.55',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01004',N'CT107_N01',N'2025-08-16',N'8.0',N'7.7',N'4.7',N'5.93',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01004',N'TN001_N02',N'2025-08-20',N'8.7',N'8.9',N'4.4',N'6.18',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01004',N'TN002_N01',N'2025-08-18',N'6.4',N'8.9',N'8.6',N'8.47',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'BA01004',N'XH002_N01',N'2025-08-18',N'5.7',N'7.0',N'9.4',N'8.31',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01001',N'CT104_N01',N'2025-08-17',N'7.7',N'5.2',N'6.1',N'5.99',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01001',N'CT107_N01',N'2025-08-16',N'6.3',N'5.1',N'9.5',N'7.86',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01001',N'CT110_N02',N'2025-08-15',N'7.6',N'5.4',N'3.8',N'4.66',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01001',N'CT114_N01',N'2025-08-15',N'6.3',N'4.7',N'3.1',N'3.9',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01001',N'TN010_N01',N'2025-08-16',N'6.4',N'8.8',N'10.0',N'9.28',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01001',N'XH002_N01',N'2025-08-20',N'7.1',N'9.5',N'7.4',N'8.0',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01002',N'CT101_N02',N'2025-08-16',N'6.4',N'9.4',N'4.3',N'6.04',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01002',N'CT106_N01',N'2025-08-17',N'6.0',N'5.5',N'4.7',N'5.07',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01002',N'CT107_N02',N'2025-08-20',N'9.2',N'8.0',N'3.8',N'5.6',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01002',N'CT110_N02',N'2025-08-15',N'7.0',N'10.0',N'6.6',N'7.66',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01002',N'KL001_N01',N'2025-08-17',N'6.5',N'7.4',N'5.6',N'6.23',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01002',N'XH001_N02',N'2025-08-16',N'8.2',N'4.6',N'6.2',N'5.92',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01003',N'CT104_N01',N'2025-08-15',N'8.9',N'4.2',N'6.6',N'6.11',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01003',N'CT106_N02',N'2025-08-15',N'6.7',N'8.2',N'6.8',N'7.21',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01003',N'CT107_N01',N'2025-08-20',N'7.5',N'9.1',N'7.3',N'7.86',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01003',N'CT114_N01',N'2025-08-15',N'8.6',N'6.2',N'5.2',N'5.84',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01003',N'ML010_N01',N'2025-08-16',N'8.6',N'9.0',N'7.0',N'7.76',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01003',N'TN001_N01',N'2025-08-17',N'5.9',N'4.0',N'4.4',N'4.43',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01004',N'CT106_N01',N'2025-08-15',N'7.1',N'7.9',N'5.6',N'6.44',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01004',N'CT109_N01',N'2025-08-17',N'6.3',N'9.4',N'6.5',N'7.35',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01004',N'CT110_N02',N'2025-08-17',N'5.5',N'9.6',N'9.2',N'8.95',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01004',N'CT112_N02',N'2025-08-16',N'7.7',N'8.5',N'8.3',N'8.3',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01004',N'TN001_N01',N'2025-08-18',N'9.9',N'7.8',N'9.6',N'9.09',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01004',N'TN010_N01',N'2025-08-15',N'6.7',N'6.0',N'4.1',N'4.93',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01005',N'CT101_N02',N'2025-08-18',N'10.0',N'8.1',N'4.3',N'6.01',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01005',N'CT106_N01',N'2025-08-15',N'5.8',N'5.9',N'7.9',N'7.09',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01005',N'CT107_N02',N'2025-08-17',N'8.2',N'4.2',N'7.3',N'6.46',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01005',N'CT110_N01',N'2025-08-18',N'8.3',N'9.0',N'5.6',N'6.89',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01005',N'TN002_N01',N'2025-08-20',N'5.3',N'6.8',N'4.0',N'4.97',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EE01005',N'XH001_N01',N'2025-08-18',N'6.2',N'5.0',N'7.2',N'6.44',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EN01001',N'CT101_N03',N'2025-08-15',N'8.8',N'7.4',N'9.1',N'8.56',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EN01001',N'CT103_N01',N'2025-08-15',N'9.7',N'6.1',N'7.3',N'7.18',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EN01001',N'CT107_N01',N'2025-08-17',N'6.7',N'6.1',N'6.5',N'6.4',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EN01001',N'CT110_N02',N'2025-08-15',N'5.3',N'8.2',N'7.5',N'7.49',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EN01001',N'TN010_N01',N'2025-08-18',N'6.4',N'6.3',N'7.7',N'7.15',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EN01001',N'XH001_N02',N'2025-08-18',N'5.3',N'4.6',N'4.9',N'4.85',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EN01002',N'CT101_N03',N'2025-08-16',N'7.4',N'5.1',N'8.5',N'7.37',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EN01002',N'CT104_N01',N'2025-08-15',N'9.2',N'4.7',N'5.7',N'5.75',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EN01002',N'CT110_N02',N'2025-08-20',N'9.2',N'6.9',N'7.6',N'7.55',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EN01002',N'TN001_N01',N'2025-08-16',N'7.4',N'6.0',N'6.4',N'6.38',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EN01002',N'XH001_N01',N'2025-08-17',N'6.0',N'7.6',N'5.4',N'6.12',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EN01002',N'XH003_N01',N'2025-08-17',N'9.8',N'6.2',N'4.5',N'5.54',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EN01003',N'CT110_N02',N'2025-08-15',N'7.2',N'7.9',N'9.5',N'8.79',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EN01003',N'CT114_N01',N'2025-08-17',N'9.4',N'4.8',N'5.2',N'5.5',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EN01003',N'KL001_N01',N'2025-08-17',N'9.3',N'5.1',N'3.2',N'4.38',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EN01003',N'ML010_N01',N'2025-08-18',N'7.9',N'9.5',N'6.1',N'7.3',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EN01003',N'TN001_N02',N'2025-08-15',N'7.5',N'7.1',N'8.8',N'8.16',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'EN01003',N'TN010_N01',N'2025-08-15',N'8.4',N'7.6',N'10.0',N'9.12',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01001',N'CT101_N01',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01001',N'CT101_N02',N'2025-08-20',N'5.9',N'5.7',N'4.0',N'4.7',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01001',N'CT106_N02',N'2025-08-20',N'9.8',N'7.9',N'8.2',N'8.27',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01001',N'CT107_N02',N'2025-08-20',N'7.0',N'6.4',N'3.7',N'4.84',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01001',N'CT109_N01',N'2025-08-18',N'9.5',N'8.7',N'9.1',N'9.02',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01001',N'CT110_N02',N'2025-08-20',N'6.8',N'7.4',N'9.7',N'8.72',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01001',N'TN001_N02',N'2025-08-16',N'5.8',N'8.0',N'3.1',N'4.84',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01001',N'XH001_N01',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01002',N'CT103_N02',N'2025-08-16',N'8.5',N'9.3',N'9.6',N'9.4',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01002',N'CT106_N01',N'2025-08-17',N'7.3',N'7.2',N'6.3',N'6.67',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01002',N'CT107_N01',N'2025-08-20',N'9.9',N'7.6',N'4.4',N'5.91',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01002',N'KL001_N01',N'2025-08-15',N'8.8',N'9.5',N'6.1',N'7.39',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01002',N'XH002_N01',N'2025-08-17',N'5.2',N'4.6',N'6.2',N'5.62',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01002',N'XH003_N01',N'2025-08-17',N'9.6',N'9.4',N'4.4',N'6.42',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01003',N'CT101_N01',N'2025-08-17',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01003',N'ML009_N01',N'2025-08-15',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01003',N'TN001_N02',N'2025-08-15',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01003',N'TN002_N01',N'2025-08-16',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01003',N'TN010_N01',N'2025-08-18',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01003',N'XH001_N01',N'2025-08-16',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01004',N'CT101_N01',N'2025-08-20',N'9.6',N'9.4',N'8.1',N'8.64',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01004',N'CT103_N01',N'2025-08-17',N'9.1',N'5.5',N'7.5',N'7.06',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01004',N'CT106_N02',N'2025-08-18',N'6.2',N'7.7',N'9.7',N'8.75',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01004',N'CT110_N02',N'2025-08-15',N'6.1',N'5.7',N'7.4',N'6.76',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01004',N'CT114_N01',N'2025-08-17',N'7.9',N'6.2',N'8.4',N'7.69',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01004',N'XH002_N01',N'2025-08-18',N'7.6',N'9.4',N'3.9',N'5.92',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01005',N'CT101_N02',N'2025-08-16',N'6.7',N'4.9',N'3.2',N'4.06',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01005',N'CT103_N02',N'2025-08-15',N'9.4',N'8.5',N'8.0',N'8.29',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01005',N'CT106_N02',N'2025-08-16',N'5.2',N'4.7',N'7.4',N'6.37',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01005',N'TN002_N01',N'2025-08-17',N'6.0',N'9.7',N'6.7',N'7.53',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01005',N'TN010_N01',N'2025-08-15',N'8.7',N'4.4',N'7.1',N'6.45',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01005',N'XH003_N01',N'2025-08-15',N'5.7',N'7.9',N'3.3',N'4.92',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01008',N'KL001_HK2N01',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01008',N'ML010_HK2N01',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01008',N'TN001_HK2N01',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01008',N'XH001_HK2N01',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01009',N'CT101_HK0N01',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01009',N'CT101_HK3N01',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01009',N'CT101_N01',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01009',N'CT101_N02',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01009',N'CT101_N03',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01009',N'CT104_N01',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01009',N'ML009_HK0N01',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01009',N'TN001_HK0N01',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01009',N'TN001_N01',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01009',N'TN010_N01',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01009',N'XH001_HK0N01',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01009',N'XH001_N01',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT01009',N'XH001_N02',NULL,NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02001',N'CT101_N01',N'2025-08-20',N'9.5',N'7.0',N'4.5',N'5.75',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02001',N'CT103_N01',N'2025-08-17',N'10.0',N'6.7',N'4.0',N'5.41',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02001',N'CT110_N02',N'2025-08-18',N'5.6',N'7.9',N'3.5',N'5.03',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02001',N'CT112_N01',N'2025-08-17',N'6.2',N'5.6',N'7.0',N'6.5',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02001',N'ML009_N01',N'2025-08-16',N'6.2',N'5.0',N'6.9',N'6.26',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02001',N'TN002_N01',N'2025-08-17',N'7.0',N'5.4',N'6.4',N'6.16',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02002',N'CT103_N02',N'2025-08-15',N'6.3',N'7.9',N'3.9',N'5.34',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02002',N'CT107_N02',N'2025-08-20',N'5.1',N'5.6',N'4.7',N'5.01',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02002',N'ML009_N01',N'2025-08-18',N'8.4',N'9.5',N'9.8',N'9.57',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02002',N'TN001_N01',N'2025-08-18',N'5.4',N'7.9',N'4.2',N'5.43',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02002',N'XH001_N02',N'2025-08-17',N'8.2',N'9.8',N'4.5',N'6.46',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02002',N'XH003_N01',N'2025-08-17',N'8.6',N'5.6',N'6.9',N'6.68',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02003',N'CT101_N03',N'2025-08-17',N'6.5',N'9.0',N'5.8',N'6.83',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02003',N'CT109_N01',N'2025-08-16',N'8.3',N'8.8',N'5.3',N'6.65',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02003',N'ML010_N01',N'2025-08-17',N'8.6',N'4.8',N'9.8',N'8.18',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02003',N'TN001_N02',N'2025-08-18',N'7.7',N'4.3',N'5.1',N'5.12',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02003',N'TN002_N01',N'2025-08-20',N'9.9',N'9.2',N'5.4',N'6.99',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02003',N'XH002_N01',N'2025-08-15',N'8.9',N'4.2',N'8.1',N'7.01',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02004',N'CT101_N03',N'2025-08-20',N'8.0',N'4.6',N'4.4',N'4.82',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02004',N'CT103_N02',N'2025-08-17',N'7.1',N'9.7',N'8.4',N'8.66',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02004',N'CT109_N01',N'2025-08-15',N'5.8',N'7.6',N'5.4',N'6.1',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02004',N'KL001_N01',N'2025-08-17',N'9.3',N'6.2',N'6.2',N'6.51',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02004',N'TN001_N02',N'2025-08-20',N'7.4',N'7.4',N'4.8',N'5.84',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'IT02004',N'TN002_N01',N'2025-08-20',N'9.8',N'5.5',N'3.3',N'4.61',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01001',N'CT103_N01',N'2025-08-16',N'5.2',N'6.0',N'3.3',N'4.3',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01001',N'CT106_N02',N'2025-08-20',N'5.8',N'7.2',N'7.6',N'7.3',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01001',N'CT107_N01',N'2025-08-20',N'7.4',N'6.4',N'8.6',N'7.82',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01001',N'CT109_N01',N'2025-08-20',N'6.6',N'5.2',N'8.6',N'7.38',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01001',N'ML011_N01',N'2025-08-15',N'9.1',N'6.5',N'5.6',N'6.22',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01001',N'TN002_N01',N'2025-08-17',N'5.2',N'8.4',N'9.4',N'8.68',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01002',N'CT101_N02',N'2025-08-17',N'8.2',N'5.8',N'4.7',N'5.38',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01002',N'CT103_N02',N'2025-08-18',N'8.8',N'8.7',N'6.2',N'7.21',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01002',N'CT106_N02',N'2025-08-15',N'5.0',N'9.9',N'6.3',N'7.25',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01002',N'CT110_N02',N'2025-08-17',N'5.6',N'7.6',N'6.9',N'6.98',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01002',N'CT114_N01',N'2025-08-15',N'9.4',N'9.1',N'7.7',N'8.29',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01002',N'XH001_N01',N'2025-08-18',N'8.3',N'6.7',N'6.1',N'6.5',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01003',N'CT106_N01',N'2025-08-16',N'9.9',N'8.8',N'8.1',N'8.49',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01003',N'CT107_N01',N'2025-08-17',N'6.0',N'6.3',N'3.2',N'4.41',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01003',N'CT109_N01',N'2025-08-15',N'5.8',N'7.9',N'4.4',N'5.59',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01003',N'CT110_N02',N'2025-08-16',N'6.6',N'5.1',N'9.8',N'8.07',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01003',N'ML009_N01',N'2025-08-16',N'8.6',N'7.6',N'5.4',N'6.38',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01003',N'ML010_N01',N'2025-08-18',N'6.4',N'4.7',N'9.4',N'7.69',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01004',N'CT106_N01',N'2025-08-16',N'9.4',N'5.6',N'4.3',N'5.2',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01004',N'CT107_N02',N'2025-08-15',N'5.6',N'8.3',N'8.7',N'8.27',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01004',N'ML011_N01',N'2025-08-17',N'5.2',N'8.2',N'7.0',N'7.18',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01004',N'TN001_N02',N'2025-08-15',N'6.8',N'9.6',N'9.8',N'9.44',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01004',N'TN010_N01',N'2025-08-20',N'9.1',N'6.0',N'8.9',N'8.05',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'JP01004',N'XH001_N01',N'2025-08-18',N'5.5',N'6.5',N'8.3',N'7.48',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'KR01001',N'CT101_N01',N'2025-08-16',N'8.3',N'9.6',N'4.6',N'6.47',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'KR01001',N'CT107_N02',N'2025-08-18',N'9.4',N'7.9',N'3.6',N'5.47',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'KR01001',N'CT109_N01',N'2025-08-18',N'7.6',N'7.3',N'3.2',N'4.87',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'KR01001',N'ML009_N01',N'2025-08-18',N'8.5',N'5.8',N'3.2',N'4.51',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'KR01001',N'TN001_N01',N'2025-08-20',N'5.0',N'7.2',N'10.0',N'8.66',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'KR01001',N'XH001_N01',N'2025-08-17',N'9.8',N'7.9',N'9.2',N'8.87',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'KR01002',N'CT101_N03',N'2025-08-16',N'5.6',N'4.3',N'4.0',N'4.25',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'KR01002',N'CT106_N01',N'2025-08-20',N'5.9',N'6.1',N'4.1',N'4.88',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'KR01002',N'CT107_N01',N'2025-08-18',N'5.4',N'7.7',N'5.6',N'6.21',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'KR01002',N'ML011_N01',N'2025-08-15',N'7.0',N'9.3',N'6.2',N'7.21',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'KR01002',N'TN001_N02',N'2025-08-16',N'6.5',N'9.0',N'3.3',N'5.33',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'KR01002',N'XH001_N01',N'2025-08-16',N'9.6',N'4.7',N'6.4',N'6.21',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'KR01003',N'CT101_N03',N'2025-08-16',N'6.9',N'8.1',N'7.2',N'7.44',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'KR01003',N'CT103_N02',N'2025-08-20',N'5.7',N'9.9',N'8.6',N'8.7',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'KR01003',N'CT104_N01',N'2025-08-16',N'8.0',N'5.9',N'6.0',N'6.17',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'KR01003',N'ML011_N01',N'2025-08-16',N'9.0',N'5.7',N'3.0',N'4.41',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'KR01003',N'TN002_N01',N'2025-08-15',N'6.4',N'4.8',N'9.2',N'7.6',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'KR01003',N'XH002_N01',N'2025-08-17',N'6.3',N'4.9',N'9.4',N'7.74',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'LA01001',N'CT106_N01',N'2025-08-17',N'8.0',N'7.5',N'7.2',N'7.37',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'LA01001',N'KL001_N01',N'2025-08-15',N'5.3',N'4.2',N'4.3',N'4.37',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'LA01001',N'ML010_N01',N'2025-08-20',N'8.7',N'5.5',N'9.3',N'8.1',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'LA01001',N'ML011_N01',N'2025-08-17',N'9.8',N'4.3',N'8.7',N'7.49',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'LA01001',N'TN001_N02',N'2025-08-16',N'9.4',N'8.7',N'5.8',N'7.03',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'LA01001',N'XH002_N01',N'2025-08-16',N'8.5',N'6.7',N'3.4',N'4.9',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'LA01002',N'CT101_N01',N'2025-08-16',N'9.8',N'9.0',N'7.3',N'8.06',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'LA01002',N'CT103_N02',N'2025-08-18',N'8.7',N'5.1',N'3.4',N'4.44',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'LA01002',N'CT104_N01',N'2025-08-16',N'8.6',N'4.5',N'7.4',N'6.65',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'LA01002',N'CT112_N01',N'2025-08-20',N'5.4',N'5.9',N'8.1',N'7.17',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'LA01002',N'TN010_N01',N'2025-08-18',N'7.3',N'9.6',N'4.8',N'6.49',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'LA01002',N'XH001_N02',N'2025-08-15',N'8.6',N'4.1',N'3.1',N'3.95',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'LA01003',N'CT112_N01',N'2025-08-18',N'6.3',N'9.3',N'4.5',N'6.12',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'LA01003',N'KL001_N01',N'2025-08-15',N'6.2',N'5.3',N'6.6',N'6.17',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'LA01003',N'ML010_N01',N'2025-08-17',N'7.2',N'4.4',N'5.7',N'5.46',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'LA01003',N'ML011_N01',N'2025-08-18',N'9.8',N'6.4',N'9.5',N'8.6',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'LA01003',N'XH001_N01',N'2025-08-15',N'8.9',N'5.0',N'3.5',N'4.49',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'LA01003',N'XH002_N01',N'2025-08-16',N'9.3',N'5.4',N'6.9',N'6.69',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'MK01001',N'CT107_N01',N'2025-08-20',N'7.1',N'4.8',N'9.4',N'7.79',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'MK01001',N'CT110_N02',N'2025-08-15',N'7.6',N'9.6',N'6.0',N'7.24',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'MK01001',N'CT114_N01',N'2025-08-17',N'9.5',N'8.0',N'8.7',N'8.57',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'MK01001',N'KL001_N01',N'2025-08-17',N'7.5',N'8.6',N'5.3',N'6.51',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'MK01001',N'ML011_N01',N'2025-08-16',N'9.1',N'5.3',N'4.8',N'5.38',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'MK01001',N'TN010_N01',N'2025-08-20',N'7.1',N'9.5',N'6.5',N'7.46',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'MK01002',N'CT101_N01',N'2025-08-16',N'6.2',N'7.9',N'6.2',N'6.71',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'MK01002',N'CT103_N01',N'2025-08-20',N'5.3',N'4.1',N'6.7',N'5.78',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'MK01002',N'CT106_N01',N'2025-08-18',N'5.4',N'9.5',N'5.0',N'6.39',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'MK01002',N'CT112_N01',N'2025-08-20',N'9.5',N'4.6',N'6.7',N'6.35',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'MK01002',N'XH001_N02',N'2025-08-18',N'6.3',N'4.4',N'4.9',N'4.89',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'MK01002',N'XH002_N01',N'2025-08-16',N'7.4',N'4.0',N'8.6',N'7.1',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'MK01003',N'CT101_N01',N'2025-08-18',N'8.0',N'7.6',N'3.3',N'5.06',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'MK01003',N'CT107_N01',N'2025-08-16',N'9.7',N'4.1',N'7.4',N'6.64',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'MK01003',N'CT110_N01',N'2025-08-16',N'5.7',N'7.4',N'8.2',N'7.71',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'MK01003',N'TN001_N02',N'2025-08-17',N'9.1',N'9.1',N'3.4',N'5.68',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'MK01003',N'TN010_N01',N'2025-08-17',N'8.6',N'9.1',N'7.0',N'7.79',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'MK01003',N'XH001_N01',N'2025-08-15',N'8.9',N'6.0',N'4.7',N'5.51',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01001',N'CT101_N03',N'2025-08-15',N'9.0',N'7.8',N'6.3',N'7.02',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01001',N'CT103_N02',N'2025-08-18',N'7.3',N'5.0',N'9.5',N'7.93',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01001',N'CT104_N01',N'2025-08-18',N'9.7',N'7.2',N'4.5',N'5.83',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01001',N'CT109_N01',N'2025-08-17',N'7.9',N'4.7',N'5.1',N'5.26',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01001',N'ML011_N01',N'2025-08-15',N'7.1',N'6.5',N'7.9',N'7.4',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01001',N'XH001_N02',N'2025-08-15',N'6.3',N'6.3',N'3.4',N'4.56',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01002',N'CT112_N02',N'2025-08-15',N'6.0',N'4.4',N'5.1',N'4.98',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01002',N'ML010_N01',N'2025-08-16',N'7.7',N'6.1',N'7.1',N'6.86',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01002',N'ML011_N01',N'2025-08-17',N'5.8',N'5.8',N'5.7',N'5.74',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01002',N'TN002_N01',N'2025-08-17',N'7.4',N'6.6',N'7.5',N'7.22',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01002',N'XH001_N02',N'2025-08-20',N'5.5',N'4.9',N'4.6',N'4.78',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01002',N'XH002_N01',N'2025-08-15',N'9.1',N'9.4',N'8.5',N'8.83',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01003',N'CT101_N03',N'2025-08-20',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01003',N'CT103_N02',N'2025-08-17',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01003',N'CT109_N01',N'2025-08-16',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01003',N'CT112_N01',N'2025-08-20',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01003',N'CT114_N01',N'2025-08-18',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01003',N'ML009_N01',N'2025-08-15',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01004',N'CT101_N02',N'2025-08-15',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01004',N'CT104_N01',N'2025-08-20',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01004',N'CT107_N01',N'2025-08-15',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01004',N'CT110_N02',N'2025-08-15',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01004',N'TN001_N02',N'2025-08-16',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'ML01004',N'XH001_N02',N'2025-08-17',NULL,NULL,NULL,NULL,N'Chưa có điểm');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'NU01001',N'CT101_N01',N'2025-08-15',N'5.7',N'7.5',N'5.8',N'6.3',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'NU01001',N'CT106_N02',N'2025-08-16',N'6.2',N'8.7',N'3.6',N'5.39',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'NU01001',N'CT109_N01',N'2025-08-15',N'6.7',N'4.5',N'5.0',N'5.02',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'NU01001',N'CT110_N02',N'2025-08-17',N'6.5',N'5.4',N'7.1',N'6.53',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'NU01001',N'CT112_N01',N'2025-08-20',N'6.9',N'6.0',N'9.9',N'8.43',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'NU01001',N'XH003_N01',N'2025-08-20',N'9.3',N'4.9',N'9.2',N'7.92',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'NU01002',N'CT101_N02',N'2025-08-16',N'5.5',N'4.5',N'3.6',N'4.06',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'NU01002',N'CT103_N02',N'2025-08-18',N'5.2',N'6.5',N'8.7',N'7.69',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'NU01002',N'CT106_N01',N'2025-08-20',N'9.6',N'5.2',N'5.5',N'5.82',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'NU01002',N'CT109_N01',N'2025-08-18',N'5.4',N'8.2',N'4.4',N'5.64',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'NU01002',N'CT112_N01',N'2025-08-16',N'6.9',N'8.6',N'5.2',N'6.39',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'NU01002',N'XH002_N01',N'2025-08-18',N'9.8',N'4.7',N'9.7',N'8.21',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'NU01003',N'CT103_N02',N'2025-08-18',N'5.0',N'9.0',N'6.7',N'7.22',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'NU01003',N'CT106_N02',N'2025-08-15',N'7.2',N'7.0',N'6.6',N'6.78',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'NU01003',N'CT107_N01',N'2025-08-16',N'9.6',N'4.6',N'5.0',N'5.34',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'NU01003',N'CT112_N01',N'2025-08-17',N'7.9',N'4.8',N'4.3',N'4.81',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'NU01003',N'TN001_N01',N'2025-08-16',N'6.8',N'4.2',N'5.9',N'5.48',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'NU01003',N'TN010_N01',N'2025-08-16',N'5.3',N'8.4',N'5.1',N'6.11',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'PH01001',N'CT106_N02',N'2025-08-20',N'8.1',N'7.1',N'3.5',N'5.04',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'PH01001',N'CT112_N02',N'2025-08-16',N'9.9',N'9.0',N'4.1',N'6.15',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'PH01001',N'ML009_N01',N'2025-08-16',N'9.9',N'4.6',N'4.9',N'5.31',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'PH01001',N'TN001_N02',N'2025-08-17',N'6.6',N'8.3',N'3.1',N'5.01',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'PH01001',N'TN002_N01',N'2025-08-18',N'10.0',N'6.4',N'5.9',N'6.46',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'PH01001',N'XH002_N01',N'2025-08-20',N'7.3',N'8.2',N'5.7',N'6.61',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'PH01002',N'CT101_N02',N'2025-08-17',N'5.7',N'8.9',N'6.6',N'7.2',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'PH01002',N'CT103_N02',N'2025-08-17',N'9.9',N'9.6',N'3.1',N'5.73',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'PH01002',N'CT107_N02',N'2025-08-18',N'5.4',N'7.0',N'10.0',N'8.64',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'PH01002',N'ML010_N01',N'2025-08-17',N'6.9',N'9.5',N'9.5',N'9.24',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'PH01002',N'ML011_N01',N'2025-08-18',N'8.8',N'10.0',N'6.8',N'7.96',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'PH01002',N'XH001_N01',N'2025-08-15',N'7.9',N'4.9',N'6.7',N'6.28',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'PH01003',N'CT104_N01',N'2025-08-16',N'8.9',N'9.7',N'9.5',N'9.5',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'PH01003',N'CT107_N01',N'2025-08-17',N'7.1',N'5.5',N'3.2',N'4.28',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'PH01003',N'CT112_N01',N'2025-08-17',N'6.6',N'5.4',N'3.8',N'4.56',N'Không đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'PH01003',N'TN001_N01',N'2025-08-18',N'9.2',N'7.8',N'6.2',N'6.98',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'PH01003',N'XH002_N01',N'2025-08-18',N'7.8',N'4.9',N'9.1',N'7.71',N'Đạt');
GO
INSERT INTO [dbo].[KET_QUA_DANG_KY]([MaSV],[MaLHP],[NgayDangKy],[DiemChuyenCan],[DiemGiuaKy],[DiemCuoiKy],[DiemTongKet],[TrangThai]) VALUES(N'PH01003',N'XH003_N01',N'2025-08-17',N'5.7',N'5.3',N'3.4',N'4.2',N'Không đạt');
GO
CREATE TABLE [dbo].[KHOA](
  [MaKhoa] nvarchar(20) NOT NULL,
  [TenKhoa] nvarchar(255) NOT NULL,
  [LienHe] nvarchar(255) NULL
);
GO
INSERT INTO [dbo].[KHOA]([MaKhoa],[TenKhoa],[LienHe]) VALUES(N'CNKT',N'Khoa Công nghệ Kỹ thuật',N'0292.3847.100 | cnkt@eaut.edu.vn');
GO
INSERT INTO [dbo].[KHOA]([MaKhoa],[TenKhoa],[LienHe]) VALUES(N'KHSK',N'Khoa Khoa học Sức khỏe',N'0292.3847.400 | khsk@eaut.edu.vn');
GO
INSERT INTO [dbo].[KHOA]([MaKhoa],[TenKhoa],[LienHe]) VALUES(N'KTQL',N'Khoa Kinh tế Quản lý',N'0292.3847.200 | ktql@eaut.edu.vn');
GO
INSERT INTO [dbo].[KHOA]([MaKhoa],[TenKhoa],[LienHe]) VALUES(N'NN',N'Khoa Ngoại ngữ',N'0292.3847.300 | nn@eaut.edu.vn');
GO
CREATE TABLE [dbo].[LOP_HOC_PHAN](
  [MaLHP] varchar(50) NOT NULL,
  [Thu] nvarchar(20) NULL,
  [TietHoc] varchar(20) NULL,
  [PhongHoc] nvarchar(100) NULL,
  [SucChua] int NULL,
  [MaMon] varchar(50) NULL,
  [MaGV] varchar(50) NULL,
  [MaHK] varchar(50) NULL
);
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT101_FULL',N'Thu 5',N'1-3',N'Lab3',1,N'CT101',N'GVCNKT03',N'HK1_2526');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT101_HK0N01',N'Thu 2',N'1-4',N'Lab2',40,N'CT101',N'GVCNKT02',N'HK1_2324');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT101_HK3N01',N'Thu 3',N'1-4',N'Lab1',40,N'CT101',N'GVCNKT01',N'HK1_2526');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT101_N01',N'Thứ 2',N'1-4',N'Lab1',40,N'CT101',N'GVCNKT01',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT101_N02',N'Thứ 4',N'6-9',N'Lab2',40,N'CT101',N'GVCNKT02',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT101_N03',N'Thứ 6',N'1-4',N'Lab3',38,N'CT101',N'GVCNKT03',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT103_HK2N01',N'Thu 4',N'1-3',N'A101',45,N'CT103',N'GVCNKT04',N'HK2_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT103_N01',N'Thứ 3',N'1-3',N'A101',45,N'CT103',N'GVCNKT04',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT103_N02',N'Thứ 5',N'6-8',N'A102',45,N'CT103',N'GVCNKT05',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT104_N01',N'Thứ 5',N'6-8',N'A101',42,N'CT104',N'GVCNKT02',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT106_HK0N01',N'Thu 4',N'1-3',N'A102',40,N'CT106',N'GVCNKT07',N'HK1_2324');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT106_HK2N01',N'Thu 6',N'3-5',N'A103',40,N'CT106',N'GVCNKT06',N'HK2_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT106_HK3N01',N'Thu 5',N'6-8',N'A103',40,N'CT106',N'GVCNKT06',N'HK1_2526');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT106_N01',N'Thứ 2',N'6-8',N'A103',40,N'CT106',N'GVCNKT06',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT106_N02',N'Thứ 4',N'3-5',N'B201',40,N'CT106',N'GVCNKT07',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT107_HK0N01',N'Thu 3',N'3-5',N'B202',40,N'CT107',N'GVCNKT01',N'HK1_2324');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT107_HK2N01',N'Thu 2',N'3-5',N'B202',40,N'CT107',N'GVCNKT01',N'HK2_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT107_HK3N01',N'Thu 2',N'6-8',N'B202',40,N'CT107',N'GVCNKT01',N'HK1_2526');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT107_N01',N'Thứ 2',N'3-5',N'B202',40,N'CT107',N'GVCNKT01',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT107_N02',N'Thứ 6',N'6-8',N'B301',40,N'CT107',N'GVCNKT02',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT109_HK3N01',N'Thu 4',N'3-5',N'A101',42,N'CT109',N'GVCNKT03',N'HK1_2526');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT109_N01',N'Thứ 3',N'3-5',N'A101',42,N'CT109',N'GVCNKT03',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT110_HK2N01',N'Thu 6',N'6-9',N'Lab1',35,N'CT110',N'GVCNKT08',N'HK2_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT110_N01',N'Thứ 3',N'6-9',N'Lab1',35,N'CT110',N'GVCNKT08',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT110_N02',N'Thứ 5',N'3-5',N'Lab2',35,N'CT110',N'GVCNKT09',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT112_HK2N01',N'Thu 3',N'1-3',N'A102',42,N'CT112',N'GVCNKT04',N'HK2_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT112_HK3N01',N'Thu 6',N'1-3',N'A103',42,N'CT112',N'GVCNKT05',N'HK1_2526');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT112_N01',N'Thứ 4',N'1-3',N'A102',42,N'CT112',N'GVCNKT04',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT112_N02',N'Thứ 6',N'3-5',N'A103',42,N'CT112',N'GVCNKT05',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'CT114_N01',N'Thứ 3',N'6-8',N'Lab3',35,N'CT114',N'GVCNKT01',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'KL001_HK2N01',N'Thu 2',N'6-8',N'B301',50,N'KL001',N'GVKTQL01',N'HK2_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'KL001_HK3N01',N'Thu 4',N'6-8',N'B301',50,N'KL001',N'GVKTQL01',N'HK1_2526');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'KL001_N01',N'Thứ 6',N'8-10',N'B301',50,N'KL001',N'GVKTQL01',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'ML009_HK0N01',N'Thu 5',N'3-5',N'C401',55,N'ML009',N'GVKTQL01',N'HK1_2324');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'ML009_HK2N01',N'Thu 7',N'1-3',N'C401',55,N'ML009',N'GVKTQL01',N'HK2_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'ML009_HK3N01',N'Thu 2',N'1-3',N'C401',55,N'ML009',N'GVKTQL01',N'HK1_2526');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'ML009_N01',N'Thứ 6',N'1-3',N'C401',55,N'ML009',N'GVKTQL01',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'ML010_HK2N01',N'Thu 5',N'3-5',N'C402',55,N'ML010',N'GVKTQL02',N'HK2_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'ML010_HK3N01',N'Thu 5',N'3-5',N'C402',55,N'ML010',N'GVKTQL02',N'HK1_2526');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'ML010_N01',N'Thứ 2',N'1-3',N'C402',55,N'ML010',N'GVKTQL02',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'ML011_HK2N01',N'Thu 2',N'1-3',N'A103',55,N'ML011',N'GVKTQL03',N'HK2_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'ML011_N01',N'Thứ 4',N'1-3',N'A103',55,N'ML011',N'GVKTQL03',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'TN001_HK0N01',N'Thu 3',N'6-8',N'A101',50,N'TN001',N'GVCNKT06',N'HK1_2324');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'TN001_HK2N01',N'Thu 4',N'6-8',N'A101',50,N'TN001',N'GVCNKT06',N'HK2_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'TN001_N01',N'Thứ 2',N'6-8',N'A101',50,N'TN001',N'GVCNKT06',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'TN001_N02',N'Thứ 4',N'6-8',N'A102',50,N'TN001',N'GVCNKT07',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'TN002_HK2N01',N'Thu 5',N'1-3',N'B201',48,N'TN002',N'GVCNKT08',N'HK2_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'TN002_HK3N01',N'Thu 7',N'1-3',N'B201',48,N'TN002',N'GVCNKT08',N'HK1_2526');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'TN002_N01',N'Thứ 3',N'1-3',N'B201',48,N'TN002',N'GVCNKT08',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'TN010_N01',N'Thứ 5',N'1-3',N'B202',48,N'TN010',N'GVCNKT09',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'XH001_HK0N01',N'Thu 5',N'8-10',N'C401',50,N'XH001',N'GVNN02',N'HK1_2324');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'XH001_HK2N01',N'Thu 3',N'8-10',N'C401',50,N'XH001',N'GVNN01',N'HK2_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'XH001_HK3N01',N'2',N'1-3',N'C401',50,N'XH001',N'GVNN01',N'HK1_2526');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'XH001_N01',N'Thứ 2',N'8-10',N'C401',50,N'XH001',N'GVNN01',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'XH001_N02',N'Thứ 4',N'8-10',N'C402',50,N'XH001',N'GVNN02',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'XH002_HK2N01',N'Thu 5',N'8-10',N'C401',48,N'XH002',N'GVNN03',N'HK2_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'XH002_N01',N'Thứ 3',N'8-10',N'C401',48,N'XH002',N'GVNN03',N'HK1_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'XH003_HK2N01',N'Thu 7',N'8-10',N'C402',48,N'XH003',N'GVNN04',N'HK2_2425');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'XH003_HK3N01',N'Thu 3',N'8-10',N'C402',48,N'XH003',N'GVNN04',N'HK1_2526');
GO
INSERT INTO [dbo].[LOP_HOC_PHAN]([MaLHP],[Thu],[TietHoc],[PhongHoc],[SucChua],[MaMon],[MaGV],[MaHK]) VALUES(N'XH003_N01',N'Thứ 5',N'8-10',N'C402',48,N'XH003',N'GVNN04',N'HK1_2425');
GO
CREATE TABLE [dbo].[MON_HOC](
  [MaMon] varchar(50) NOT NULL,
  [TenMon] nvarchar(255) NOT NULL,
  [SoTinChi] varchar(10) NULL,
  [TinChiLyThuyet] varchar(10) NULL,
  [TinChiThucHanh] varchar(10) NULL,
  [LoaiMon] nvarchar(100) NULL
);
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT101',N'Lập trình Căn bản A',N'4',N'2',N'2',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT102',N'Toán rời rạc 1',N'3',N'3',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT103',N'Cấu trúc Dữ liệu',N'4',N'3',N'1',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT104',N'Kiến trúc Máy tính',N'2',N'2',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT106',N'Hệ Cơ sở Dữ liệu',N'4',N'4',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT107',N'Hệ Điều hành',N'3',N'2',N'1',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT108',N'Niên luận 1 – Tin học (Lập trình)',N'1',N'0',N'1',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT109',N'Phân tích & Thiết kế Hệ thống T.Tin',N'3',N'2',N'1',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT110',N'Hệ Quản trị Cơ sở Dữ liệu',N'2',N'0',N'2',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT111',N'Niên luận 2 – Tin học',N'1',N'0',N'1',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT112',N'Mạng Máy tính',N'3',N'2',N'1',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT113',N'Nhập môn Công nghệ Phần mềm',N'2',N'1',N'1',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT114',N'Lập trình Hướng đối tượng C++',N'3',N'2',N'1',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT115',N'Chuyên đề Ngôn ngữ Lập trình 1',N'2',N'1',N'1',N'Tự chọn');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT118',N'Anh văn Chuyên môn Tin học',N'2',N'2',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT119',N'Toán rời rạc 2',N'3',N'3',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT120',N'Phân tích & Thiết kế Thuật toán',N'2',N'2',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT121',N'Tin học Lý thuyết',N'3',N'3',N'0',N'Tự chọn');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT122',N'UML',N'2',N'2',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT123',N'Quy hoạch Tuyến tính – CNTT',N'2',N'2',N'0',N'Tự chọn');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT128',N'Kỹ thuật Đồ hoạ – CNTT',N'2',N'2',N'0',N'Tự chọn');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT301',N'Lập trình Web',N'2',N'1',N'1',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT302',N'Phát triển Phần mềm Mã nguồn mở',N'2',N'1',N'1',N'Tự chọn');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT304',N'Giao diện Người – Máy',N'2',N'2',N'0',N'Tự chọn');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT305',N'Thực tập Thực tế – Tin học',N'3',N'0',N'3',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT306',N'Niên luận 3 – Tin học',N'1',N'0',N'1',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT309',N'Quản lý Dự án Tin học',N'2',N'1',N'1',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT310',N'Phân tích & Thiết kế Hệ thống HĐT',N'4',N'3',N'1',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT312',N'Khai khoáng Dữ liệu',N'3',N'2',N'1',N'Tự chọn');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT313',N'An toàn & Bảo mật Thông tin',N'2',N'1',N'1',N'Tự chọn');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT314',N'An toàn Hệ thống & An ninh Mạng',N'2',N'2',N'0',N'Tự chọn');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT315',N'Hệ Cơ sở Dữ liệu Đa phương tiện',N'3',N'2',N'1',N'Tự chọn');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT316',N'Xử lý Ảnh',N'3',N'2',N'1',N'Tự chọn');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT321',N'Phát triển Hệ thống Thương mại Điện tử',N'3',N'0',N'3',N'Tự chọn');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT349',N'Thương mại Điện tử',N'2',N'1',N'1',N'Tự chọn');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'CT358',N'Luận văn Tốt nghiệp – Tin học',N'10',N'0',N'10',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'KL001',N'Pháp luật Đại cương',N'2',N'2',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'ML004',N'Chủ nghĩa Xã hội Khoa học',N'2',N'2',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'ML006',N'Tư tưởng Hồ Chí Minh',N'2',N'2',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'ML007',N'Logic học Đại cương',N'2',N'2',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'ML009',N'Những nguyên lý cơ bản của CN Mác-Lênin 1',N'2',N'2',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'ML010',N'Những nguyên lý cơ bản của CN Mác-Lênin 2',N'3',N'3',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'ML011',N'Đường lối Cách mạng của Đảng CSVN',N'3',N'2',N'1',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'QP001',N'Giáo dục Quốc phòng',N'6',N'4',N'2',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'TC100',N'"Giáo dục Thể chất 1',N' 2"',N'2',N'0',N'2,Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'TN001',N'Vi – Tích phân A1',N'3',N'3',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'TN002',N'Vi – Tích phân A2',N'4',N'4',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'TN010',N'Xác suất Thống kê',N'3',N'3',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'TN012',N'Đại số Tuyến tính & Hình học',N'4',N'4',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'TN033',N'Tin học Căn bản',N'1',N'1',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'TN034',N'TT. Tin học Căn bản',N'2',N'0',N'2',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'XH001',N'Anh văn Căn bản 1',N'3',N'3',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'XH002',N'Anh văn Căn bản 2',N'3',N'3',N'0',N'Bắt buộc');
GO
INSERT INTO [dbo].[MON_HOC]([MaMon],[TenMon],[SoTinChi],[TinChiLyThuyet],[TinChiThucHanh],[LoaiMon]) VALUES(N'XH003',N'Anh văn Căn bản 3',N'4',N'4',N'0',N'Bắt buộc');
GO
CREATE TABLE [dbo].[MON_TIEN_QUYET](
  [MaMon] varchar(50) NOT NULL,
  [MaMonTQ] varchar(50) NOT NULL
);
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT103',N'CT101');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT106',N'CT103');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT107',N'CT104');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT108',N'CT101');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT109',N'CT106');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT110',N'CT106');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT111',N'CT106');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT112',N'CT107');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT114',N'CT101');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT115',N'CT101');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT118',N'XH003');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT120',N'CT103');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT121',N'CT101');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT304',N'CT114');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT310',N'CT122');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT312',N'TN010');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT314',N'CT112');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT315',N'CT106');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'CT321',N'CT349');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'ML004',N'ML009');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'ML011',N'ML010');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'TN002',N'TN001');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'XH002',N'XH001');
GO
INSERT INTO [dbo].[MON_TIEN_QUYET]([MaMon],[MaMonTQ]) VALUES(N'XH003',N'XH002');
GO
CREATE TABLE [dbo].[NGAY_NGHI](
  [MaNghi] nvarchar(50) NOT NULL,
  [Ngay] date NOT NULL,
  [LyDo] nvarchar(200) NOT NULL,
  [LoaiNghi] nvarchar(10) NOT NULL DEFAULT (N'Le')
);
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_1_5_2024',N'2024-05-01',N'Quốc Tế Lao Động 1/5',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_1_5_2025',N'2025-05-01',N'Quốc Tế Lao Động 1/5',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_1_5_2026',N'2026-05-01',N'Quốc Tế Lao Động 1/5',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_2_9_2024',N'2024-09-02',N'Quốc Khánh 2/9 - NGHỈ BÙ 3/9',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_2_9_2025',N'2025-09-02',N'Quốc Khánh 2/9',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_2_9_BU_2024',N'2024-09-03',N'Nghỉ bù Quốc Khánh 2/9',N'Truong');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_30_4_2024',N'2024-04-30',N'Giải Phóng Miền Nam 30/4',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_30_4_2025',N'2025-04-30',N'Giải Phóng Miền Nam 30/4',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_30_4_2026',N'2026-04-30',N'Giải Phóng Miền Nam 30/4',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_HUNG_2024',N'2024-04-18',N'Giỗ Tổ Hùng Vương (10/3 âm lịch)',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_HUNG_2025',N'2025-04-07',N'Giỗ Tổ Hùng Vương (10/3 âm lịch)',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_TET_2025_1',N'2025-01-27',N'Tết Nguyên Đán Ất Tỵ',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_TET_2025_2',N'2025-01-28',N'Tết Nguyên Đán Ất Tỵ',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_TET_2025_3',N'2025-01-29',N'Tết Nguyên Đán Ất Tỵ',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_TET_2025_4',N'2025-01-30',N'Tết Nguyên Đán Ất Tỵ',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_TET_2025_5',N'2025-01-31',N'Tết Nguyên Đán Ất Tỵ',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_TET_2026_1',N'2026-02-16',N'Tết Nguyên Đán Bính Ngọ',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_TET_2026_2',N'2026-02-17',N'Tết Nguyên Đán Bính Ngọ',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_TET_2026_3',N'2026-02-18',N'Tết Nguyên Đán Bính Ngọ',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_TET_2026_4',N'2026-02-19',N'Tết Nguyên Đán Bính Ngọ',N'Le');
GO
INSERT INTO [dbo].[NGAY_NGHI]([MaNghi],[Ngay],[LyDo],[LoaiNghi]) VALUES(N'NN_TET_2026_5',N'2026-02-20',N'Tết Nguyên Đán Bính Ngọ',N'Le');
GO
CREATE TABLE [dbo].[SINH_VIEN](
  [MaSV] varchar(50) NOT NULL,
  [HoTen] nvarchar(255) NOT NULL,
  [GioiTinh] nvarchar(20) NULL,
  [NgaySinh] date NULL,
  [SoDienThoai] varchar(50) NULL,
  [Email] varchar(100) NULL,
  [MaLop] varchar(50) NULL,
  [TrangThaiHocTap] nvarchar(100) NULL,
  [DatChuanNgoaiNgu] nvarchar(50) NULL,
  [MaCTDT] varchar(50) NULL
);
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'AC01001',N'Bùi Đức Tuấn',N'Nam',N'2005-09-03',N'837163935',N'AC01-001@eaut.edu.vn',N'24AC01',N'Bảo lưu',N'0',N'AC01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'AC01002',N'Bùi Thị Lan',N'Nữ',N'2004-02-01',N'998583791',N'AC01-002@eaut.edu.vn',N'24AC01',N'Đang học',N'0',N'AC01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'AC01003',N'Dương Thị Linh',N'Nữ',N'2005-10-08',N'770297399',N'AC01-003@eaut.edu.vn',N'24AC01',N'Đang học',N'1',N'AC01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'AC01004',N'Vũ Quang Hữu Nam',N'Nam',N'2004-08-28',N'884353306',N'AC01-004@eaut.edu.vn',N'24AC01',N'Đang học',N'1',N'AC01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'AC01005',N'Phạm Xuân Thị Nga',N'Nữ',N'2004-10-02',N'399828967',N'AC01-005@eaut.edu.vn',N'24AC01',N'Đang học',N'0',N'AC01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'AU01001',N'Trần Đức Thắng',N'Nam',N'2004-05-23',N'396959055',N'AU01-001@eaut.edu.vn',N'24AU01',N'Đang học',N'1',N'AU01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'AU01002',N'Hồ Văn Nam',N'Nam',N'2005-12-28',N'905924593',N'AU01-002@eaut.edu.vn',N'24AU01',N'Đang học',N'1',N'AU01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'AU01003',N'Trần Ngọc Vy',N'Nữ',N'2004-09-21',N'382063333',N'AU01-003@eaut.edu.vn',N'24AU01',N'Đang học',N'1',N'AU01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'AU01004',N'Dương Hữu Nam',N'Nam',N'2004-02-13',N'796513323',N'AU01-004@eaut.edu.vn',N'24AU01',N'Đang học',N'1',N'AU01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'AU01005',N'Phan Thu Tâm',N'Nữ',N'2005-02-18',N'325391584',N'AU01-005@eaut.edu.vn',N'24AU01',N'Đang học',N'1',N'AU01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'AU01006',N'Võ Thị Nga',N'Nữ',N'2005-12-22',N'343537231',N'AU01-006@eaut.edu.vn',N'24AU01',N'Đang học',N'1',N'AU01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'AU01007',N'Lý Xuân Thị Nhi',N'Nữ',N'2005-07-05',N'815483754',N'AU01-007@eaut.edu.vn',N'24AU01',N'Đang học',N'0',N'AU01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'AU01008',N'Nguyễn Hải Thu Trâm',N'Nữ',N'2004-02-26',N'949986006',N'AU01-008@eaut.edu.vn',N'24AU01',N'Đang học',N'1',N'AU01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'BA01001',N'Phạm Ngọc Quyên',N'Nữ',N'2005-02-10',N'828309418',N'BA01-001@eaut.edu.vn',N'24BA01',N'Đang học',N'1',N'BA01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'BA01002',N'Bùi Đức Phát',N'Nam',N'2005-12-17',N'991335093',N'BA01-002@eaut.edu.vn',N'24BA01',N'Đang học',N'1',N'BA01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'BA01003',N'Nguyễn Quang Minh Bảo',N'Nam',N'2005-08-17',N'920666918',N'BA01-003@eaut.edu.vn',N'24BA01',N'Đang học',N'1',N'BA01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'BA01004',N'Lý Thanh Tâm',N'Nữ',N'2004-04-12',N'342536312',N'BA01-004@eaut.edu.vn',N'24BA01',N'Đang học',N'1',N'BA01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'BA01005',N'Ngô Tuyết Phương Trâm',N'Nữ',N'2005-04-16',N'825436277',N'BA01-005@eaut.edu.vn',N'24BA01',N'Đang học',N'1',N'BA01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'BA01006',N'Ngô Tuyết Thị Thảo',N'Nữ',N'2005-07-18',N'396694356',N'BA01-006@eaut.edu.vn',N'24BA01',N'Đang học',N'0',N'BA01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'BA01007',N'Lê Khắc Văn Long',N'Nam',N'2004-08-12',N'848567039',N'BA01-007@eaut.edu.vn',N'24BA01',N'Đang học',N'1',N'BA01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'BA01008',N'Lý Gia Duy',N'Nam',N'2005-07-23',N'844413674',N'BA01-008@eaut.edu.vn',N'24BA01',N'Bảo lưu',N'0',N'BA01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'EE01001',N'Lý Quang Đức Trí',N'Nam',N'2004-10-04',N'889398662',N'EE01-001@eaut.edu.vn',N'24EE01',N'Đang học',N'1',N'EE01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'EE01002',N'Lý Khắc Văn Long',N'Nam',N'2004-11-09',N'700812525',N'EE01-002@eaut.edu.vn',N'24EE01',N'Đang học',N'0',N'EE01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'EE01003',N'Bùi Quang Hữu Trí',N'Nam',N'2004-07-17',N'794632821',N'EE01-003@eaut.edu.vn',N'24EE01',N'Đang học',N'1',N'EE01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'EE01004',N'Phan Đức Cường',N'Nam',N'2004-10-08',N'855251724',N'EE01-004@eaut.edu.vn',N'24EE01',N'Đang học',N'0',N'EE01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'EE01005',N'Phan Hoàng Văn Bảo',N'Nam',N'2004-04-25',N'363770787',N'EE01-005@eaut.edu.vn',N'24EE01',N'Đang học',N'0',N'EE01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'EE01006',N'Lê Bích Thanh Linh',N'Nữ',N'2004-05-26',N'932136770',N'EE01-006@eaut.edu.vn',N'24EE01',N'Đang học',N'1',N'EE01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'EE01007',N'Huỳnh Hải Thu Nhi',N'Nữ',N'2005-11-27',N'961105789',N'EE01-007@eaut.edu.vn',N'24EE01',N'Đang học',N'1',N'EE01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'EN01001',N'Lý Thu Nhi',N'Nữ',N'2005-01-03',N'385983843',N'EN01-001@eaut.edu.vn',N'24EN01',N'Đang học',N'1',N'EN01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'EN01002',N'Phạm Xuân Thanh Trang',N'Nữ',N'2004-10-08',N'891658119',N'EN01-002@eaut.edu.vn',N'24EN01',N'Đang học',N'0',N'EN01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'EN01003',N'Phạm Cẩm Thanh Yến',N'Nữ',N'2004-10-24',N'924076091',N'EN01-003@eaut.edu.vn',N'24EN01',N'Đang học',N'1',N'EN01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'EN01004',N'Lê Thị Trang',N'Nữ',N'2004-07-02',N'926087075',N'EN01-004@eaut.edu.vn',N'24EN01',N'Đang học',N'1',N'EN01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'EN01005',N'Phan Thanh Yến',N'Nữ',N'2004-10-15',N'901558840',N'EN01-005@eaut.edu.vn',N'24EN01',N'Đang học',N'1',N'EN01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'EN01006',N'Vũ Minh Duy',N'Nam',N'2005-06-15',N'353615611',N'EN01-006@eaut.edu.vn',N'24EN01',N'Đang học',N'0',N'EN01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'EN01007',N'Bùi Gia Bảo',N'Nam',N'2004-06-11',N'836141838',N'EN01-007@eaut.edu.vn',N'24EN01',N'Đang học',N'1',N'EN01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'EN01008',N'Võ Minh Hải',N'Nam',N'2005-12-06',N'902268030',N'EN01-008@eaut.edu.vn',N'24EN01',N'Đang học',N'1',N'EN01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT01001',N'Hoàng Cẩm Thị Nhi',N'Nữ',N'2004-07-15',N'393649974',N'IT01-001@eaut.edu.vn',N'24IT01',N'Đang học',N'1',N'IT01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT01002',N'Hồ Ngọc Yến',N'Nữ',N'2005-03-21',N'391039162',N'IT01-002@eaut.edu.vn',N'24IT01',N'Đang học',N'0',N'IT01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT01003',N'Dương Bích Thu Giang',N'Nữ',N'2004-08-19',N'371164065',N'IT01-003@eaut.edu.vn',N'24IT01',N'Bảo lưu',N'0',N'IT01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT01004',N'Đặng Hữu Thắng',N'Nam',N'2004-08-20',N'765832616',N'IT01-004@eaut.edu.vn',N'24IT01',N'Đang học',N'1',N'IT01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT01005',N'Phan Ngọc Lan',N'Nữ',N'2005-07-23',N'829134378',N'IT01-005@eaut.edu.vn',N'24IT01',N'Đang học',N'1',N'IT01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT01006',N'Đỗ Văn Anh',N'Nam',N'2005-05-26',N'974171954',N'IT01-006@eaut.edu.vn',N'24IT01',N'Đang học',N'1',N'IT01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT01007',N'Phan Thị Giang',N'Nữ',N'2004-02-17',N'349885645',N'IT01-007@eaut.edu.vn',N'24IT01',N'Đang học',N'0',N'IT01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT01008',N'Võ Hoàng Đức Duy',N'Nam',N'2004-10-28',N'834702272',N'IT01-008@eaut.edu.vn',N'24IT01',N'Đang học',N'1',N'IT01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT01009',N'Vũ Thu Vy',N'Nữ',N'2005-10-07',N'811767296',N'IT01-009@eaut.edu.vn',N'24IT01',N'Đang học',N'1',N'IT01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT01010',N'Phạm Đức Anh',N'Nam',N'2005-12-27',N'946569460',N'IT01-010@eaut.edu.vn',N'24IT01',N'Đang học',N'1',N'IT01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT01011',N'Dương Hữu Quân',N'Nam',N'2005-01-02',N'856521090',N'IT01-011@eaut.edu.vn',N'24IT01',N'Đang học',N'1',N'IT01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT01012',N'Đặng Cẩm Thanh Giang',N'Nữ',N'2005-11-14',N'772521482',N'IT01-012@eaut.edu.vn',N'24IT01',N'Bảo lưu',N'0',N'IT01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT02001',N'Nguyễn Tuyết Ngọc Trâm',N'Nữ',N'2004-10-05',N'704433711',N'IT02-001@eaut.edu.vn',N'24IT02',N'Đang học',N'1',N'IT02');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT02002',N'Bùi Quang Minh Quân',N'Nam',N'2005-07-12',N'386454167',N'IT02-002@eaut.edu.vn',N'24IT02',N'Đang học',N'1',N'IT02');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT02003',N'Võ Quang Đức Phát',N'Nam',N'2005-04-19',N'836212402',N'IT02-003@eaut.edu.vn',N'24IT02',N'Đang học',N'1',N'IT02');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT02004',N'Lê Quang Đức Hải',N'Nam',N'2005-10-18',N'794758652',N'IT02-004@eaut.edu.vn',N'24IT02',N'Đang học',N'0',N'IT02');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT02005',N'Phạm Quang Đức Anh',N'Nam',N'2004-11-22',N'963753914',N'IT02-005@eaut.edu.vn',N'24IT02',N'Đang học',N'1',N'IT02');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT02006',N'Nguyễn Ngọc Nga',N'Nữ',N'2005-12-16',N'704236741',N'IT02-006@eaut.edu.vn',N'24IT02',N'Đang học',N'1',N'IT02');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT02007',N'Bùi Hữu Duy',N'Nam',N'2004-02-02',N'334572881',N'IT02-007@eaut.edu.vn',N'24IT02',N'Đang học',N'1',N'IT02');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'IT02008',N'Phạm Xuân Thu Giang',N'Nữ',N'2005-07-12',N'944676553',N'IT02-008@eaut.edu.vn',N'24IT02',N'Bảo lưu',N'0',N'IT02');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'JP01001',N'Lê Tuyết Phương Trâm',N'Nữ',N'2004-06-07',N'325672352',N'JP01-001@eaut.edu.vn',N'24JP01',N'Đang học',N'1',N'JP01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'JP01002',N'Võ Phương Dung',N'Nữ',N'2005-02-08',N'371520187',N'JP01-002@eaut.edu.vn',N'24JP01',N'Đang học',N'1',N'JP01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'JP01003',N'Trần Quang Hữu Huy',N'Nam',N'2005-06-13',N'962550011',N'JP01-003@eaut.edu.vn',N'24JP01',N'Đang học',N'1',N'JP01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'JP01004',N'Phạm Văn Long',N'Nam',N'2004-02-24',N'795089350',N'JP01-004@eaut.edu.vn',N'24JP01',N'Đang học',N'1',N'JP01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'JP01005',N'Huỳnh Hải Thị Nga',N'Nữ',N'2005-02-25',N'386629101',N'JP01-005@eaut.edu.vn',N'24JP01',N'Đang học',N'0',N'JP01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'KR01001',N'Hồ Đức Bảo',N'Nam',N'2004-11-02',N'840835709',N'KR01-001@eaut.edu.vn',N'24KR01',N'Đang học',N'1',N'KR01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'KR01002',N'Bùi Quang Văn Bảo',N'Nam',N'2004-04-25',N'766688296',N'KR01-002@eaut.edu.vn',N'24KR01',N'Đang học',N'0',N'KR01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'KR01003',N'Hoàng Thị Giang',N'Nữ',N'2005-11-20',N'855570289',N'KR01-003@eaut.edu.vn',N'24KR01',N'Đang học',N'1',N'KR01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'KR01004',N'Phan Cẩm Thu Trâm',N'Nữ',N'2005-08-09',N'985765584',N'KR01-004@eaut.edu.vn',N'24KR01',N'Đang học',N'1',N'KR01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'KR01005',N'Đặng Khắc Đức Hải',N'Nam',N'2005-08-04',N'819407253',N'KR01-005@eaut.edu.vn',N'24KR01',N'Đang học',N'1',N'KR01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'KR01006',N'Lý Phương Tâm',N'Nữ',N'2004-03-17',N'986133596',N'KR01-006@eaut.edu.vn',N'24KR01',N'Đang học',N'0',N'KR01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'KR01007',N'Lê Đức Khoa',N'Nam',N'2005-03-06',N'947489412',N'KR01-007@eaut.edu.vn',N'24KR01',N'Đang học',N'1',N'KR01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'LA01001',N'Phan Khắc Minh Long',N'Nam',N'2004-02-01',N'337434321',N'LA01-001@eaut.edu.vn',N'24LA01',N'Đang học',N'0',N'LA01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'LA01002',N'Võ Văn Đạt',N'Nam',N'2005-02-23',N'823080677',N'LA01-002@eaut.edu.vn',N'24LA01',N'Đang học',N'0',N'LA01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'LA01003',N'Ngô Thị Dung',N'Nữ',N'2005-07-11',N'328197017',N'LA01-003@eaut.edu.vn',N'24LA01',N'Đang học',N'1',N'LA01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'LA01004',N'Huỳnh Bích Thu Nhi',N'Nữ',N'2004-05-04',N'946454128',N'LA01-004@eaut.edu.vn',N'24LA01',N'Thôi học',N'0',N'LA01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'LA01005',N'Đặng Thanh Dung',N'Nữ',N'2004-05-21',N'709550028',N'LA01-005@eaut.edu.vn',N'24LA01',N'Đang học',N'0',N'LA01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'LA01006',N'Lê Quang Hữu Duy',N'Nam',N'2005-02-03',N'915622050',N'LA01-006@eaut.edu.vn',N'24LA01',N'Đang học',N'1',N'LA01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'LA01007',N'Phan Ngọc Hà',N'Nữ',N'2005-04-03',N'343238938',N'LA01-007@eaut.edu.vn',N'24LA01',N'Đang học',N'1',N'LA01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'MK01001',N'Huỳnh Hoàng Hữu Huy',N'Nam',N'2005-07-08',N'770492717',N'MK01-001@eaut.edu.vn',N'24MK01',N'Đang học',N'1',N'MK01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'MK01002',N'Đỗ Minh Bảo',N'Nam',N'2004-12-27',N'767500209',N'MK01-002@eaut.edu.vn',N'24MK01',N'Đang học',N'1',N'MK01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'MK01003',N'Dương Thị Mai',N'Nữ',N'2004-07-19',N'393297575',N'MK01-003@eaut.edu.vn',N'24MK01',N'Đang học',N'1',N'MK01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'MK01004',N'Hồ Hữu Duy',N'Nam',N'2005-09-15',N'847832618',N'MK01-004@eaut.edu.vn',N'24MK01',N'Đang học',N'1',N'MK01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'MK01005',N'Đặng Xuân Thanh Tâm',N'Nữ',N'2005-10-11',N'380244221',N'MK01-005@eaut.edu.vn',N'24MK01',N'Đang học',N'1',N'MK01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'ML01001',N'Võ Đức Đạt',N'Nam',N'2005-04-05',N'993287808',N'ML01-001@eaut.edu.vn',N'24ML01',N'Đang học',N'0',N'ML01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'ML01002',N'Vũ Đức Phát',N'Nam',N'2005-11-09',N'966554029',N'ML01-002@eaut.edu.vn',N'24ML01',N'Đang học',N'1',N'ML01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'ML01003',N'Hồ Bích Thanh Dung',N'Nữ',N'2005-07-16',N'764044936',N'ML01-003@eaut.edu.vn',N'24ML01',N'Bảo lưu',N'0',N'ML01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'ML01004',N'Nguyễn Minh Đạt',N'Nam',N'2004-09-03',N'942797389',N'ML01-004@eaut.edu.vn',N'24ML01',N'Bảo lưu',N'0',N'ML01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'ML01005',N'Đặng Ngọc Vy',N'Nữ',N'2004-03-15',N'920554424',N'ML01-005@eaut.edu.vn',N'24ML01',N'Đang học',N'0',N'ML01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'ML01006',N'Hồ Phương Dung',N'Nữ',N'2005-02-23',N'396075258',N'ML01-006@eaut.edu.vn',N'24ML01',N'Đang học',N'1',N'ML01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'ML01007',N'Huỳnh Xuân Phương Lan',N'Nữ',N'2005-03-24',N'962018976',N'ML01-007@eaut.edu.vn',N'24ML01',N'Đang học',N'1',N'ML01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'ML01008',N'Phạm Đức Duy',N'Nam',N'2005-07-17',N'811876340',N'ML01-008@eaut.edu.vn',N'24ML01',N'Bảo lưu',N'0',N'ML01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'ML01009',N'Lý Đức Cường',N'Nam',N'2004-03-08',N'944611072',N'ML01-009@eaut.edu.vn',N'24ML01',N'Đang học',N'1',N'ML01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'NU01001',N'Bùi Minh Tuấn',N'Nam',N'2004-09-25',N'970325557',N'NU01-001@eaut.edu.vn',N'24NU01',N'Đang học',N'1',N'NU01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'NU01002',N'Huỳnh Thanh Trâm',N'Nữ',N'2004-05-12',N'364694761',N'NU01-002@eaut.edu.vn',N'24NU01',N'Đang học',N'1',N'NU01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'NU01003',N'Vũ Đức Cường',N'Nam',N'2004-07-25',N'797969186',N'NU01-003@eaut.edu.vn',N'24NU01',N'Đang học',N'1',N'NU01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'NU01004',N'Nguyễn Thị Mai',N'Nữ',N'2004-07-13',N'831244786',N'NU01-004@eaut.edu.vn',N'24NU01',N'Đang học',N'0',N'NU01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'NU01005',N'Đặng Hoàng Văn Trí',N'Nam',N'2005-11-21',N'964043373',N'NU01-005@eaut.edu.vn',N'24NU01',N'Đang học',N'1',N'NU01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'NU01006',N'Trần Hữu Nam',N'Nam',N'2005-03-18',N'929195007',N'NU01-006@eaut.edu.vn',N'24NU01',N'Thôi học',N'0',N'NU01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'PH01001',N'Đặng Minh Huy',N'Nam',N'2005-07-21',N'976627724',N'PH01-001@eaut.edu.vn',N'24PH01',N'Đang học',N'1',N'PH01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'PH010011',N'Nguyễn Minh Hải',N'Nam',N'2005-03-02',N'864669628',N'PH01-003@eaut.edu.vn',N'24PH01',N'Đang học',N'0',N'PH01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'PH01002',N'Phạm Hải Ngọc Lan',N'Nữ',N'2004-01-01',N'938180669',N'PH01-002@eaut.edu.vn',N'24PH01',N'Đang học',N'1',N'PH01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'PH01003',N'Nguyễn Minh Huy',N'Nam',N'2005-03-02',N'854669628',N'PH01-003@eaut.edu.vn',N'24PH01',N'Đang học',N'0',N'PH01');
GO
INSERT INTO [dbo].[SINH_VIEN]([MaSV],[HoTen],[GioiTinh],[NgaySinh],[SoDienThoai],[Email],[MaLop],[TrangThaiHocTap],[DatChuanNgoaiNgu],[MaCTDT]) VALUES(N'PH01004',N'Võ Thu Quyên',N'Nữ',N'2004-12-19',N'896507177',N'PH01-004@eaut.edu.vn',N'24PH01',N'Đang học',N'1',N'PH01');
GO
ALTER TABLE [dbo].[NGAY_NGHI] ADD CONSTRAINT [PK__NGAY_NGH__238150F3E1BBBF2F] PRIMARY KEY CLUSTERED([MaNghi] ASC);
GO
ALTER TABLE [dbo].[BUOI_HOC_NGOAI_LE] ADD CONSTRAINT [PK__BUOI_HOC__E471029F8FAAA2D1] PRIMARY KEY CLUSTERED([MaBuoi] ASC);
GO
ALTER TABLE [dbo].[KHOA] ADD CONSTRAINT [PK__KHOA__6539040543E1AC69] PRIMARY KEY CLUSTERED([MaKhoa] ASC);
GO
ALTER TABLE [dbo].[HOC_KY] ADD CONSTRAINT [PK__HOC_KY__2725A6E7F0B9E90C] PRIMARY KEY CLUSTERED([MaHK] ASC);
GO
ALTER TABLE [dbo].[MON_HOC] ADD CONSTRAINT [PK__MON_HOC__3A5B29A8A90ADC7C] PRIMARY KEY CLUSTERED([MaMon] ASC);
GO
ALTER TABLE [dbo].[CHUONG_TRINH_DAO_TAO] ADD CONSTRAINT [PK__CHUONG_T__1E4E40E44C239ADD] PRIMARY KEY CLUSTERED([MaCTDT] ASC);
GO
ALTER TABLE [dbo].[GIANG_VIEN] ADD CONSTRAINT [PK__GIANG_VI__2725AEF3E48CF8BD] PRIMARY KEY CLUSTERED([MaGV] ASC);
GO
ALTER TABLE [dbo].[MON_TIEN_QUYET] ADD CONSTRAINT [PK__MON_TIEN__A14A6D63BCD08C3C] PRIMARY KEY CLUSTERED([MaMon] ASC,[MaMonTQ] ASC);
GO
ALTER TABLE [dbo].[CONG_NO_HOC_PHI] ADD CONSTRAINT [PK__CONG_NO___2660BFE04B370839] PRIMARY KEY CLUSTERED([MaPhieu] ASC);
GO
ALTER TABLE [dbo].[SINH_VIEN] ADD CONSTRAINT [PK__SINH_VIE__2725081A80DCBD44] PRIMARY KEY CLUSTERED([MaSV] ASC);
GO
ALTER TABLE [dbo].[LOP_HOC_PHAN] ADD CONSTRAINT [PK__LOP_HOC___3B9B96903248C292] PRIMARY KEY CLUSTERED([MaLHP] ASC);
GO
ALTER TABLE [dbo].[KET_QUA_DANG_KY] ADD CONSTRAINT [PK__KET_QUA___349CB173FA5CA154] PRIMARY KEY CLUSTERED([MaSV] ASC,[MaLHP] ASC);
GO
ALTER TABLE [dbo].[BUOI_HOC_NGOAI_LE] WITH CHECK ADD CONSTRAINT [FK_BuoiNL_LHP] FOREIGN KEY([MaLHP]) REFERENCES [dbo].[LOP_HOC_PHAN]([MaLHP]) ON DELETE NO ACTION ON UPDATE NO ACTION;
GO
ALTER TABLE [dbo].[CHUONG_TRINH_DAO_TAO] WITH NOCHECK ADD CONSTRAINT [FK__CHUONG_TR__MaKho__3D5E1FD2] FOREIGN KEY([MaKhoa]) REFERENCES [dbo].[KHOA]([MaKhoa]) ON DELETE SET NULL ON UPDATE CASCADE;
GO
ALTER TABLE [dbo].[GIANG_VIEN] WITH NOCHECK ADD CONSTRAINT [FK__GIANG_VIE__MaKho__403A8C7D] FOREIGN KEY([MaKhoa]) REFERENCES [dbo].[KHOA]([MaKhoa]) ON DELETE SET NULL ON UPDATE CASCADE;
GO
ALTER TABLE [dbo].[MON_TIEN_QUYET] WITH NOCHECK ADD CONSTRAINT [FK__MON_TIEN___MaMon__45F365D3] FOREIGN KEY([MaMon]) REFERENCES [dbo].[MON_HOC]([MaMon]) ON DELETE CASCADE ON UPDATE CASCADE;
GO
ALTER TABLE [dbo].[MON_TIEN_QUYET] WITH NOCHECK ADD CONSTRAINT [FK__MON_TIEN___MaMon__46E78A0C] FOREIGN KEY([MaMonTQ]) REFERENCES [dbo].[MON_HOC]([MaMon]) ON DELETE NO ACTION ON UPDATE NO ACTION;
GO
ALTER TABLE [dbo].[CONG_NO_HOC_PHI] WITH NOCHECK ADD CONSTRAINT [FK__CONG_NO_HO__MaHK__534D60F1] FOREIGN KEY([MaHK]) REFERENCES [dbo].[HOC_KY]([MaHK]) ON DELETE NO ACTION ON UPDATE NO ACTION;
GO
ALTER TABLE [dbo].[SINH_VIEN] WITH NOCHECK ADD CONSTRAINT [FK__SINH_VIEN__MaCTD__4316F928] FOREIGN KEY([MaCTDT]) REFERENCES [dbo].[CHUONG_TRINH_DAO_TAO]([MaCTDT]) ON DELETE SET NULL ON UPDATE CASCADE;
GO
ALTER TABLE [dbo].[CONG_NO_HOC_PHI] WITH NOCHECK ADD CONSTRAINT [FK__CONG_NO_HO__MaSV__52593CB8] FOREIGN KEY([MaSV]) REFERENCES [dbo].[SINH_VIEN]([MaSV]) ON DELETE CASCADE ON UPDATE CASCADE;
GO
ALTER TABLE [dbo].[LOP_HOC_PHAN] WITH NOCHECK ADD CONSTRAINT [FK__LOP_HOC_P__MaMon__49C3F6B7] FOREIGN KEY([MaMon]) REFERENCES [dbo].[MON_HOC]([MaMon]) ON DELETE CASCADE ON UPDATE CASCADE;
GO
ALTER TABLE [dbo].[LOP_HOC_PHAN] WITH NOCHECK ADD CONSTRAINT [FK__LOP_HOC_PH__MaGV__4AB81AF0] FOREIGN KEY([MaGV]) REFERENCES [dbo].[GIANG_VIEN]([MaGV]) ON DELETE SET NULL ON UPDATE CASCADE;
GO
ALTER TABLE [dbo].[LOP_HOC_PHAN] WITH NOCHECK ADD CONSTRAINT [FK__LOP_HOC_PH__MaHK__4BAC3F29] FOREIGN KEY([MaHK]) REFERENCES [dbo].[HOC_KY]([MaHK]) ON DELETE CASCADE ON UPDATE CASCADE;
GO
ALTER TABLE [dbo].[KET_QUA_DANG_KY] WITH NOCHECK ADD CONSTRAINT [FK__KET_QUA_DA__MaSV__4E88ABD4] FOREIGN KEY([MaSV]) REFERENCES [dbo].[SINH_VIEN]([MaSV]) ON DELETE CASCADE ON UPDATE CASCADE;
GO
ALTER TABLE [dbo].[KET_QUA_DANG_KY] WITH NOCHECK ADD CONSTRAINT [FK__KET_QUA_D__MaLHP__4F7CD00D] FOREIGN KEY([MaLHP]) REFERENCES [dbo].[LOP_HOC_PHAN]([MaLHP]) ON DELETE NO ACTION ON UPDATE NO ACTION;
GO
ALTER TABLE [dbo].[CHUONG_TRINH_DAO_TAO] WITH CHECK ADD CONSTRAINT [FK_CHUONG_TRINH_DAO_TAO_CHUONG_TRINH_DAO_TAO] FOREIGN KEY([MaCTDT]) REFERENCES [dbo].[CHUONG_TRINH_DAO_TAO]([MaCTDT]) ON DELETE NO ACTION ON UPDATE NO ACTION;
GO
ALTER TABLE [dbo].[NGAY_NGHI] ADD CONSTRAINT [CHK_NGAYNG_Loai] CHECK ([LoaiNghi]=N'Truong' OR [LoaiNghi]=N'Le');
GO
ALTER TABLE [dbo].[BUOI_HOC_NGOAI_LE] ADD CONSTRAINT [CHK_BuoiNL_Loai] CHECK ([LoaiBuoi]=N'Huy' OR [LoaiBuoi]=N'Bu');
GO
