package ui;
import service.*;
public class AdminPanel extends PortalPanel { public AdminPanel(StudentManagerService s){super(s,true,Session.user(),Session::clear);} }
