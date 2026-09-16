package ui;
import javax.swing.*;import java.awt.*;import service.StudentManagerService;
public class AdminImportPanel extends JPanel{public AdminImportPanel(StudentManagerService service){setLayout(new BorderLayout());add(new ExcelImportPanel());}}
