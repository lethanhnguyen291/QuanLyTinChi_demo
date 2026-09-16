-- Run after 01_du_lieu_cu.sql in the isolated upgrade database.
-- Repairs formatting only; retains the original snapshot in 01_du_lieu_cu.sql.
USE [QuanLyTinChi_NangCap_20260914];
SET XACT_ABORT ON;
IF DB_NAME() <> N'QuanLyTinChi_NangCap_20260914'
    THROW 50002,N'Wrong database. Do not run on the original database.',1;
BEGIN TRANSACTION;
-- Seven amounts were exported in scientific notation (for example 1.08e+007).
UPDATE CONG_NO_HOC_PHI
SET SoTienDaDong=CONVERT(varchar(50),CONVERT(decimal(18,2),TRY_CONVERT(float,SoTienDaDong)))
WHERE TRY_CONVERT(decimal(18,2),SoTienDaDong) IS NULL
  AND TRY_CONVERT(float,SoTienDaDong) IS NOT NULL;
-- One credit value contains an accidental trailing quotation mark.
UPDATE MON_HOC SET SoTinChi='2' WHERE MaMon='TC100' AND LTRIM(RTRIM(SoTinChi))='2"';
COMMIT;
