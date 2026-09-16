package ui;
import utils.*;
import service.*;
import javax.swing.*;
import java.awt.*;
import java.nio.file.*;
public final class ExcelImportPanel extends JPanel{
    private final JComboBox<String> type=new JComboBox<>(new String[]{"Sinh viên","Giảng viên"});private final JLabel status=new JLabel("Chọn tệp để xem trước và kiểm tra dữ liệu.");
    private final JPanel body=Ui.page();private final JButton choose=UIUtils.createPrimaryBtn("Chọn Excel"),commit=UIUtils.createPrimaryBtn("Nhập các dòng hợp lệ"),template=new JButton("Tải tệp mẫu");private ImportService.Preview preview;
    public ExcelImportPanel(){setLayout(new BorderLayout(0,16));setBackground(UIUtils.BG_APP);JPanel top=new JPanel(new FlowLayout(FlowLayout.LEFT,12,0));top.setOpaque(false);top.add(type);top.add(choose);top.add(template);add(top,BorderLayout.NORTH);add(body,BorderLayout.CENTER);
        JPanel bottom=Ui.page();bottom.add(status,BorderLayout.NORTH);bottom.add(commit,BorderLayout.EAST);add(bottom,BorderLayout.SOUTH);commit.setEnabled(false);
        type.addActionListener(e->{preview=null;body.removeAll();UIUtils.applyTheme(body);body.revalidate();body.repaint();commit.setEnabled(false);status.setText("Chọn tệp để xem trước.");});
        choose.addActionListener(e->{JFileChooser fc=new JFileChooser();fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Excel (*.xlsx, *.xls)","xlsx","xls"));if(fc.showOpenDialog(this)!=JFileChooser.APPROVE_OPTION)return;
            boolean student=type.getSelectedIndex()==0;Path file=fc.getSelectedFile().toPath();type.setEnabled(false);commit.setEnabled(false);preview=null;status.setText("Đang kiểm tra "+file.getFileName()+"…");
            Ui.async(this,choose,()->new ImportService().preview(file,student),p->{preview=p;render();});
        });choose.addPropertyChangeListener("enabled",e->{if(Boolean.TRUE.equals(e.getNewValue()))type.setEnabled(true);});
        template.addActionListener(e->{JFileChooser fc=new JFileChooser();fc.setSelectedFile(new java.io.File(type.getSelectedIndex()==0?"Mau_SinhVien.xlsx":"Mau_GiangVien.xlsx"));if(fc.showSaveDialog(this)!=JFileChooser.APPROVE_OPTION)return;Path file=fc.getSelectedFile().toPath();if(Files.exists(file)&&JOptionPane.showConfirmDialog(this,"Ghi đè tệp mẫu đã tồn tại?","Xác nhận",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
            try{new ImportService().template(file,type.getSelectedIndex()==0);JOptionPane.showMessageDialog(this,"Đã lưu tệp mẫu. Giữ tên cột; ngày sinh dùng yyyy-MM-dd hoặc dd/MM/yyyy.");}catch(Exception ex){Ui.error(this,ex);}});
        commit.addActionListener(e->{ImportService.Preview snapshot=preview;if(snapshot==null||snapshot.valid()==0)return;
            if(JOptionPane.showConfirmDialog(this,"Nhập "+snapshot.valid()+" dòng hợp lệ; bỏ qua "+(snapshot.rows().size()-snapshot.valid())+" dòng được đánh dấu lỗi?","Xác nhận nhập dữ liệu",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
            type.setEnabled(false);choose.setEnabled(false);Ui.async(this,commit,()->new ImportService().commit(snapshot),result->{JTextArea report=new JTextArea(result);report.setEditable(false);report.setFont(UIUtils.FONT_NORMAL);body.removeAll();body.add(Ui.scroll(report));UIUtils.applyTheme(body);body.revalidate();body.repaint();preview=null;status.setText("Đã hoàn tất. Xem kết quả từng dòng ở trên.");});
        });commit.addPropertyChangeListener("enabled",e->{if(Boolean.TRUE.equals(e.getNewValue())){type.setEnabled(true);choose.setEnabled(true);if(preview==null)SwingUtilities.invokeLater(()->commit.setEnabled(false));}});
        JTextArea help=new JTextArea("1. Tải tệp mẫu và điền dữ liệu.\n2. Chọn Excel để kiểm tra từng dòng.\n3. Xác nhận nhập các dòng hợp lệ.\n\nCác mã đã có được giữ nguyên. Nếu xảy ra lỗi lưu trong giao dịch, toàn bộ lần nhập được hoàn tác.\nGiới hạn: 20 MB, 10.000 dòng. Trang dữ liệu đầu tiên; cột được nhận diện theo tên.\nSinh viên mới chưa có tài khoản: cấp tại mục Tài khoản & Ngoại ngữ.");help.setEditable(false);help.setOpaque(false);help.setFont(UIUtils.FONT_NORMAL);help.setLineWrap(true);help.setWrapStyleWord(true);body.add(UIUtils.noteCard("Nhập dữ liệu từ Excel",help.getText()));
    }
    private void render(){body.removeAll();String[] keys=preview.student()?ImportService.STUDENT:ImportService.TEACHER;String[] columns=new String[keys.length+2];columns[0]="Dòng Excel";System.arraycopy(keys,0,columns,1,keys.length);columns[columns.length-1]="Kiểm tra";var model=Ui.model(columns);
        for(var r:preview.rows()){Object[] row=new Object[columns.length];row[0]=r.line();System.arraycopy(r.values(),0,row,1,keys.length);row[row.length-1]=r.error()==null?"Hợp lệ":r.error();model.addRow(row);}JTable table=new JTable(model);UIUtils.styleTable(table);table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);for(int i=0;i<table.getColumnCount();i++)table.getColumnModel().getColumn(i).setPreferredWidth(i==columns.length-1?300:140);
        body.add(Ui.tableTools(table,"Kiem_tra_nhap_Excel"),BorderLayout.NORTH);body.add(Ui.scroll(table));status.setText(preview.rows().size()+" dòng · "+preview.valid()+" hợp lệ · "+(preview.rows().size()-preview.valid())+" lỗi/bỏ qua");commit.setEnabled(preview.valid()>0);UIUtils.applyTheme(body);body.revalidate();body.repaint();}
}
