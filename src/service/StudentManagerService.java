package service;

import dao.TruongHocDAO;
import entity.LopHocPhan;
import exception.BusinessLogicException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.sql.ResultSet;
import java.util.List;

public class StudentManagerService {
    private TruongHocDAO dao;

    public StudentManagerService() { 
        this.dao = new TruongHocDAO(); 
    }

    // ==========================================================
    // CÁC HÀM PHỤC VỤ TRANG CÔNG NỢ (HÌNH 1 - CÓ BẢNG CHI TIẾT)
    // ==========================================================
    public List<String> getDanhSachHocKy() {
        return dao.getAllHocKy();
    }

    public double[] getChiTietCongNo(String maSV, String maHK) {
        return dao.getThongTinCongNo(maSV, maHK);
    }

    // NGHIỆP VỤ MỚI: Lấy danh sách công nợ cho Bảng tổng hợp
    public ResultSet getDanhSachCongNo(String maSV) {
        return dao.getLichSuCongNo(maSV);
    }

    public ResultSet getBangDiemChiTiet(String maSV, String maHK) {
        return dao.getChiTietDangKyTrongKy(maSV, maHK);
    }

    // NGHIỆP VỤ MỚI: Thanh toán công nợ
    public String thanhToanCongNo(String maSV,String maHK) throws BusinessLogicException {
        throw new BusinessLogicException("Vui lòng nộp học phí qua kênh của nhà trường. Cán bộ sẽ xác nhận sau khi đối soát; ứng dụng chưa kết nối cổng thanh toán.");
    }
    public String registerCourse(String maSV,String maLHP) throws BusinessLogicException {
        try { return new RegistrationService().register(maSV,maLHP); } catch(Exception e) { throw new BusinessLogicException(e.getMessage()); }
    }
    public String cancelRegistration(String maSV,String maLHP) throws BusinessLogicException {
        try { return new RegistrationService().cancel(maSV,maLHP); } catch(Exception e) { throw new BusinessLogicException(e.getMessage()); }
    }
    public String calculateTuition(String maSV,String maHK) throws BusinessLogicException {
        try { return new RegistrationService().recalculate(maSV,maHK); } catch(Exception e) { throw new BusinessLogicException(e.getMessage()); }
    }

    public String checkGraduation(String maSV) {
        StringBuilder sb = new StringBuilder("KẾT QUẢ XÉT DUYỆT TỐT NGHIỆP:\n");
        boolean pass = true;
        
        int tcTichLuy = dao.getTongTinChiTichLuy(maSV);
        int required;try{required=AcademicService.requiredCredits(maSV);}catch(Exception e){return "Không đủ dữ liệu để xét tốt nghiệp: "+e.getMessage();}
        if (tcTichLuy < required) { 
            sb.append("- [X] Chưa đủ tín chỉ (").append(tcTichLuy).append("/").append(required).append(")\n"); 
            pass = false; 
        } else {
            sb.append("- [OK] Đã đủ tín chỉ\n");
        }
        
        if (dao.checkNoHocPhi(maSV)) { 
            sb.append("- [X] Còn nợ học phí\n"); 
            pass = false; 
        } else {
            sb.append("- [OK] Đã hoàn thành học phí\n");
        }
        
        if (!dao.checkChuanNgoaiNgu(maSV)) { 
            sb.append("- [X] Chưa đạt chuẩn Ngoại ngữ\n"); 
            pass = false; 
        } else {
            sb.append("- [OK] Đã đạt chuẩn Ngoại ngữ\n");
        }
        
        sb.append("\nKẾT LUẬN: ").append(pass ? "ĐỦ ĐIỀU KIỆN TỐT NGHIỆP" : "CHƯA ĐỦ ĐIỀU KIỆN TỐT NGHIỆP");
        return sb.toString();
    }

    public String importSinhVienFromExcel(String path) throws BusinessLogicException {
        try{ImportService imp=new ImportService();return imp.commit(imp.preview(java.nio.file.Path.of(path),true));}catch(Exception e){throw new BusinessLogicException(e.getMessage());}
    }
    public String importGiangVienFromExcel(String path) throws BusinessLogicException {
        try{ImportService imp=new ImportService();return imp.commit(imp.preview(java.nio.file.Path.of(path),false));}catch(Exception e){throw new BusinessLogicException(e.getMessage());}
    }
}
