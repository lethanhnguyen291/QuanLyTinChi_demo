package ui;
import javax.swing.*;import java.awt.*;import service.StudentManagerService;
public class DangKyPanel extends JPanel{private final String student;public DangKyPanel(StudentManagerService service,String student){this.student=student;setLayout(new BorderLayout());}public void updateData(String term,String name){removeAll();add(new EnrollmentPanel(student,term));revalidate();repaint();}}
