package service;
import utils.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.nio.file.*;
import java.util.*;
public final class ImportService{
    public static final String[] STUDENT={"MaSV","HoTen","GioiTinh","NgaySinh","SoDienThoai","Email","TrangThaiHocTap","MaCTDT","MaLop"};
    public static final String[] TEACHER={"MaGV","HoTen","GioiTinh","HocVi","SoDienThoai","Email","MaKhoa"};
    public record RowData(int line,String[] values,String error){}
    public record Preview(boolean student,List<RowData> rows){public long valid(){return rows.stream().filter(r->r.error()==null).count();}}
    public Preview preview(Path file,boolean student)throws Exception{
        Session.requireAdmin();if(Files.size(file)>20*1024*1024)throw new IllegalArgumentException("Tệp Excel tối đa 20 MB.");
        String[] expected=student?STUDENT:TEACHER;List<RowData> data=new ArrayList<>();Set<String> ids=new HashSet<>();
        try(var input=Files.newInputStream(file);Workbook wb=WorkbookFactory.create(input)){
            if(wb.getNumberOfSheets()==0)throw new IllegalArgumentException("Excel không có trang dữ liệu.");Sheet sheet=wb.getSheetAt(0);if(sheet.getLastRowNum()>10000)throw new IllegalArgumentException("Tối đa 10.000 dòng mỗi lần.");
            DataFormatter fmt=new DataFormatter(Locale.ROOT);Row header=sheet.getRow(0);if(header==null)throw new IllegalArgumentException("Thiếu dòng tiêu đề.");Map<String,Integer> columns=new HashMap<>();for(Cell cell:header)columns.put(key(fmt.formatCellValue(cell)),cell.getColumnIndex());
            for(String col:expected)if(!columns.containsKey(key(col)))throw new IllegalArgumentException("Thiếu cột "+col+". Hãy dùng tệp mẫu; thứ tự cột có thể thay đổi.");
            for(int n=1;n<=sheet.getLastRowNum();n++){Row row=sheet.getRow(n);if(row==null)continue;String[] values=new String[expected.length];boolean empty=true;
                for(int i=0;i<expected.length;i++){Cell cell=row.getCell(columns.get(key(expected[i])));values[i]=fmt.formatCellValue(cell).trim();if(student&&i==3&&cell!=null&&cell.getCellType()==CellType.NUMERIC&&DateUtil.isCellDateFormatted(cell))values[i]=cell.getLocalDateTimeCellValue().toLocalDate().toString();empty&=values[i].isBlank();}
                if(empty)continue;String error=null;try{values[0]=AccountService.identifier(values[0]);if(!ids.add(values[0]))throw new IllegalArgumentException("Mã trùng trong tệp.");PersonService.contact(values[1],values[4],values[5]);if(student)PersonService.birthDate(values[3]);}
                catch(Exception e){error=e.getMessage();}data.add(new RowData(n+1,values,error));
            }
        }
        try(var c=config.DBConnect.getConnection()){
            List<RowData> checked=new ArrayList<>();for(RowData row:data){String error=row.error();String[] v=row.values();if(error==null){
                if(Db.count(c,student?"SELECT COUNT(*) FROM SINH_VIEN WHERE MaSV=?":"SELECT COUNT(*) FROM GIANG_VIEN WHERE MaGV=?",v[0])>0)error="Mã đã tồn tại trong cơ sở dữ liệu (sẽ bỏ qua).";
                else if(!v[student?7:6].isBlank()&&Db.count(c,student?"SELECT COUNT(*) FROM CHUONG_TRINH_DAO_TAO WHERE MaCTDT=?":"SELECT COUNT(*) FROM KHOA WHERE MaKhoa=?",v[student?7:6])!=1)error=student?"Mã chương trình đào tạo không tồn tại.":"Mã khoa không tồn tại.";
            }checked.add(new RowData(row.line(),v,error));}return new Preview(student,List.copyOf(checked));
        }
    }
    public String commit(Preview preview)throws Exception{
        Session.requireAdmin();return Db.transaction(c->{Db.lock(c,"ACADEMIC");int inserted=0;StringBuilder skipped=new StringBuilder();
            for(RowData row:preview.rows()){String[] v=row.values();if(row.error()!=null){skipped.append("Dòng ").append(row.line()).append(": ").append(row.error()).append('\n');continue;}
                if(Db.count(c,preview.student()?"SELECT COUNT(*) FROM SINH_VIEN WHERE MaSV=?":"SELECT COUNT(*) FROM GIANG_VIEN WHERE MaGV=?",v[0])>0){skipped.append("Dòng ").append(row.line()).append(": mã vừa được thêm bởi phiên khác; bỏ qua.\n");continue;}
                if(preview.student())Db.update(c,"INSERT INTO SINH_VIEN(MaSV,HoTen,GioiTinh,NgaySinh,SoDienThoai,Email,TrangThaiHocTap,MaCTDT,MaLop,DatChuanNgoaiNgu)VALUES(?,?,?,?,?,?,?,?,?,0)",v[0],v[1],v[2],PersonService.birthDate(v[3]),v[4],v[5],v[6].isBlank()?"Đang học":v[6],v[7].isBlank()?null:v[7],v[8]);
                else Db.update(c,"INSERT INTO GIANG_VIEN(MaGV,HoTen,GioiTinh,HocVi,SoDienThoai,Email,MaKhoa)VALUES(?,?,?,?,?,?,?)",v[0],v[1],v[2],v[3],v[4],v[5],v[6].isBlank()?null:v[6]);inserted++;
            }SchemaService.audit(c,"NHẬP EXCEL",(preview.student()?"Sinh viên":"Giảng viên")+" / "+inserted+" dòng");return "Đã thêm: "+inserted+"\nBỏ qua: "+(preview.rows().size()-inserted)+"\n\n"+skipped;
        });
    }
    public void template(Path file,boolean student)throws Exception{
        try(Workbook wb=new XSSFWorkbook()){Sheet s=wb.createSheet(student?"SinhVien":"GiangVien");Row row=s.createRow(0);String[] columns=student?STUDENT:TEACHER;CellStyle style=wb.createCellStyle();Font f=wb.createFont();f.setBold(true);style.setFont(f);for(int i=0;i<columns.length;i++){row.createCell(i).setCellValue(columns[i]);row.getCell(i).setCellStyle(style);s.setColumnWidth(i,22*256);}s.createFreezePane(0,1);try(var out=Files.newOutputStream(file)){wb.write(out);}}
    }
    private static String key(String s){return ScheduleRules.normalize(s).replaceAll("[^a-z0-9]","");}
}
