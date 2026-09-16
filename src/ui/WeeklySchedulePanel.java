package ui;
import service.*;
import utils.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;
public final class WeeklySchedulePanel extends JPanel{
    private final String student,term;private LocalDate monday,begin,end;private final JLabel label=new JLabel();private final JPanel body=Ui.page();
    public WeeklySchedulePanel(String student,String term){this.student=student;this.term=term;setLayout(new BorderLayout(0,16));setBackground(UIUtils.BG_APP);
        try{var r=Db.rows("SELECT NgayBatDau,NgayKetThuc FROM HOC_KY WHERE MaHK=?",term).get(0);begin=((java.sql.Date)r.get("NgayBatDau")).toLocalDate();end=((java.sql.Date)r.get("NgayKetThuc")).toLocalDate();}catch(Exception e){throw new IllegalArgumentException("Học kỳ chưa có khoảng ngày học hợp lệ.",e);}
        monday=initial();JPanel top=new JPanel(new BorderLayout());top.setOpaque(false);label.setFont(UIUtils.FONT_BOLD);top.add(label,BorderLayout.WEST);
        JPanel nav=new JPanel(new FlowLayout(FlowLayout.RIGHT,6,0));nav.setOpaque(false);JButton prev=new JButton("‹ Tuần trước"),today=new JButton("Tuần hiện tại"),next=new JButton("Tuần sau ›");nav.add(prev);nav.add(today);nav.add(next);top.add(nav,BorderLayout.EAST);
        prev.addActionListener(e->{monday=monday.minusWeeks(1);load();});next.addActionListener(e->{monday=monday.plusWeeks(1);load();});today.addActionListener(e->{monday=initial();load();});add(top,BorderLayout.NORTH);add(body,BorderLayout.CENTER);load();}
    private LocalDate initial(){LocalDate now=LocalDate.now();return (now.isBefore(begin)||now.isAfter(end)?begin:now).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));}
    private void load(){body.removeAll();label.setText(monday.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))+" – "+monday.plusDays(6).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        try{var events=new ScheduleService().week(student,term,monday);String[] columns=new String[8];columns[0]="Tiết";for(int d=0;d<7;d++)columns[d+1]=(d==6?"CN":"Thứ "+(d+2))+" · "+monday.plusDays(d).format(DateTimeFormatter.ofPattern("dd/MM"));
            DefaultTableModel grid=Ui.model(columns);for(int p=1;p<=15;p++){Object[] row=new Object[8];Arrays.fill(row,"");row[0]="Tiết "+p;grid.addRow(row);}
            for(var event:events){int d=(int)ChronoUnit.DAYS.between(monday,event.date())+1;var periods=ScheduleRules.periods(event.periods());for(int p=periods.nextSetBit(1);p>=0;p=periods.nextSetBit(p+1)){String old=grid.getValueAt(p-1,d).toString();grid.setValueAt(old+(old.isEmpty()?"":" | ")+event.subject()+" · "+event.room(),p-1,d);}}
            JTable table=new JTable(grid){public String getToolTipText(java.awt.event.MouseEvent e){int r=rowAtPoint(e.getPoint()),c=columnAtPoint(e.getPoint());return r<0||c<0?null:Objects.toString(getValueAt(r,c));}};UIUtils.styleTable(table);table.putClientProperty("portal.calendar",true);table.setRowHeight(76);table.getColumnModel().getColumn(0).setMaxWidth(60);table.getColumnModel().getColumn(0).setPreferredWidth(55);
            DefaultTableCellRenderer renderer=new DefaultTableCellRenderer(){public Component getTableCellRendererComponent(JTable t,Object v,boolean selected,boolean focus,int r,int c){JLabel l=(JLabel)super.getTableCellRendererComponent(t,v,selected,focus,r,c);String text=Objects.toString(v,"");l.setText(text.isEmpty()?"":"<html>"+escape(text)+"</html>");l.setFont(new Font("Segoe UI",Font.PLAIN,12));l.setVerticalAlignment(SwingConstants.CENTER);l.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(UIUtils.BORDER),BorderFactory.createEmptyBorder(6,8,6,8)));l.setBackground(selected?UIUtils.BLUE_LIGHT:c==0?UIUtils.MIT_RED_LIGHT:text.isEmpty()?Color.WHITE:UIUtils.BLUE_LIGHT);l.setForeground(c==0?UIUtils.MIT_RED:UIUtils.BLUE_DARK);return l;}};
            for(int i=0;i<8;i++)table.getColumnModel().getColumn(i).setCellRenderer(renderer);
            JTabbedPane tabs=new JTabbedPane();tabs.addTab("Lưới thời khóa biểu",Ui.scroll(table));
            var detail=Ui.model("Ngày","Thứ","Tiết","Môn học","Mã lớp","Phòng","Giảng viên","Ghi chú");for(var e:events)detail.addRow(new Object[]{e.date(),e.date().getDayOfWeek()==DayOfWeek.SUNDAY?"CN":"Thứ "+(e.date().getDayOfWeek().getValue()+1),e.periods(),e.subject(),e.classId(),e.room(),e.teacher(),e.note()});
            JTable list=new JTable(detail);UIUtils.styleTable(list);UIUtils.columnWidths(list,110,90,75,250,145,95,185,240);JPanel details=Ui.page();details.add(Ui.tableTools(list,"Lich_hoc_"+monday),BorderLayout.NORTH);details.add(Ui.scroll(list),BorderLayout.CENTER);tabs.addTab("Danh sách & Xuất lịch",details);body.add(tabs,BorderLayout.CENTER);
            String note=events.isEmpty()?"Tuần này không có buổi học trong dữ liệu.":events.size()+" buổi học · Hiển thị đúng tiết 1–15 và Chủ nhật.";
            for(var h:Db.rows("SELECT Ngay,LyDo FROM NGAY_NGHI WHERE Ngay BETWEEN ? AND ?",java.sql.Date.valueOf(monday),java.sql.Date.valueOf(monday.plusDays(6))))note+="  |  Nghỉ "+Db.str(h,"Ngay")+": "+Db.str(h,"LyDo");
            JTextArea footer=new JTextArea(note);footer.setEditable(false);footer.setLineWrap(true);footer.setWrapStyleWord(true);footer.setOpaque(false);footer.setFont(UIUtils.FONT_NORMAL);footer.setRows(2);body.add(footer,BorderLayout.SOUTH);
        }catch(Exception e){body.add(new JLabel("Không tải được lịch học."));Ui.error(this,e);}UIUtils.applyTheme(body);body.revalidate();body.repaint();}
    private static String escape(String v){return v.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");}
}
