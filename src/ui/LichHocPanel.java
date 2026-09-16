package ui;
import javax.swing.*;import java.awt.*;import service.StudentManagerService;
public class LichHocPanel extends JPanel{private final String student;public LichHocPanel(StudentManagerService service,String student){this.student=student;setLayout(new BorderLayout());}public void updateData(String term,String name){removeAll();add(new WeeklySchedulePanel(student,term));revalidate();repaint();}}
