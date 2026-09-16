package ui;
import service.*;
public class StudentPanel extends PortalPanel { public StudentPanel(StudentManagerService s,String id){super(s,false,id,Session::clear);} }
