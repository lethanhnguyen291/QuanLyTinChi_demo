package ui;
import javax.swing.*;import java.awt.*;
public class AdminLopHocPhanPanel extends JPanel{public AdminLopHocPhanPanel(){setLayout(new BorderLayout());}public void updateData(String term){removeAll();add(new ClassAdminPanel(term));revalidate();repaint();}}
