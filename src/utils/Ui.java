package utils;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
public final class Ui {
    private Ui(){}
    public static void error(Component owner,Throwable e){
        while((e instanceof java.util.concurrent.ExecutionException||e instanceof java.lang.reflect.InvocationTargetException)&&e.getCause()!=null)e=e.getCause();
        e.printStackTrace();
        String message=e instanceof java.sql.SQLException?"Không lưu/tải được dữ liệu. Có thể mã bị trùng, dữ liệu liên quan còn tồn tại hoặc kết nối bị gián đoạn. Chi tiết được ghi trong nhật ký chạy.":e.getMessage();
        JOptionPane.showMessageDialog(owner,message==null?"Thao tác thất bại. Vui lòng thử lại.":message,"Không thực hiện được",JOptionPane.ERROR_MESSAGE);
    }
    public static <T> void async(Component owner,JButton button,Callable<T> work,Consumer<T> done){
        String title=button==null?"":button.getText();if(button!=null){button.setEnabled(false);button.setText("Đang xử lý…");}
        new SwingWorker<T,Void>(){protected T doInBackground()throws Exception{return work.call();}
            protected void done(){try{done.accept(get());}catch(Exception e){error(owner,e);}finally{if(button!=null){button.setEnabled(true);button.setText(title);}}}}.execute();
    }
    public static DefaultTableModel model(String... columns){return new DefaultTableModel(columns,0){public boolean isCellEditable(int r,int c){return false;}};}
    public static JPanel tableTools(JTable table,String filename){
        JPanel p=new JPanel(new BorderLayout(12,0));p.setBackground(Color.WHITE);p.setBorder(UIUtils.cardBorder(UIUtils.BLUE_BORDER,12));
        JTextField search=UIUtils.createInput();search.putClientProperty("JTextField.placeholderText","Nhập mã, tên hoặc nội dung cần tìm…");search.setToolTipText("Tìm theo bất kỳ cột nào, không phân biệt dấu tiếng Việt");
        JLabel searchLabel=new JLabel("Tìm kiếm");searchLabel.setForeground(UIUtils.BLUE_DARK);searchLabel.setFont(UIUtils.FONT_BOLD);p.add(searchLabel,BorderLayout.WEST);p.add(search,BorderLayout.CENTER);
        @SuppressWarnings("unchecked") TableRowSorter<TableModel> sorter=table.getRowSorter() instanceof TableRowSorter?(TableRowSorter<TableModel>)table.getRowSorter():new TableRowSorter<>(table.getModel());
        table.setRowSorter(sorter);
        search.getDocument().addDocumentListener(new DocumentListener(){void filter(){String q=ScheduleRules.normalize(search.getText());sorter.setRowFilter(new RowFilter<TableModel,Integer>(){public boolean include(Entry<? extends TableModel,? extends Integer> e){for(int i=0;i<e.getValueCount();i++)if(ScheduleRules.normalize(e.getStringValue(i)).contains(q))return true;return false;}});}
            public void insertUpdate(DocumentEvent e){filter();}public void removeUpdate(DocumentEvent e){filter();}public void changedUpdate(DocumentEvent e){filter();}});
        JButton export=UIUtils.createListBtn("Xuất CSV");export.addActionListener(e->export(table,filename));p.add(export,BorderLayout.EAST);return p;
    }
    public static String csv(Object value){String s=value==null?"":value.toString();String trim=s.stripLeading();if(!trim.isEmpty()&&"=+-@\t\r".indexOf(trim.charAt(0))>=0)s="'"+s;return "\""+s.replace("\"","\"\"")+"\"";}
    public static void export(JTable table,String filename){
        JFileChooser chooser=new JFileChooser();chooser.setSelectedFile(new java.io.File(filename+".csv"));
        if(chooser.showSaveDialog(table)!=JFileChooser.APPROVE_OPTION)return;
        Path file=chooser.getSelectedFile().toPath();if(!file.toString().toLowerCase().endsWith(".csv"))file=Path.of(file+".csv");
        if(Files.exists(file)&&JOptionPane.showConfirmDialog(table,"Tệp đã tồn tại. Ghi đè?","Xuất CSV",JOptionPane.YES_NO_OPTION)!=JOptionPane.YES_OPTION)return;
        StringBuilder data=new StringBuilder("\ufeff");for(int c=0;c<table.getColumnCount();c++)data.append(c==0?"":",").append(csv(table.getColumnName(c)));data.append("\r\n");
        for(int r=0;r<table.getRowCount();r++){for(int c=0;c<table.getColumnCount();c++)data.append(c==0?"":",").append(csv(table.getValueAt(r,c)));data.append("\r\n");}
        try{Files.writeString(file,data,StandardCharsets.UTF_8);JOptionPane.showMessageDialog(table,"Đã xuất "+table.getRowCount()+" dòng: "+file);}catch(Exception e){error(table,e);}
    }
    public static JPanel page(){JPanel p=new JPanel(new BorderLayout(0,16));p.setBackground(UIUtils.BG_APP);return p;}
    public static JScrollPane scroll(Component c){JScrollPane s=new JScrollPane(c);s.setBorder(BorderFactory.createEmptyBorder());s.getVerticalScrollBar().setUnitIncrement(22);return s;}
}
