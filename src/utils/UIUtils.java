package utils;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.*;
import javax.swing.table.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.*;
import java.util.Locale;

/** Shared visual language: burgundy actions/properties, blue data lists. */
public class UIUtils {
    public static final Color MIT_RED=new Color(139,0,0),BURGUNDY_DARK=new Color(112,0,0),BURGUNDY_HOVER=new Color(169,35,35);
    public static final Color MIT_RED_LIGHT=new Color(253,240,240),MIT_ORANGE=new Color(183,110,39),MIT_YELLOW=new Color(241,188,65);
    public static final Color BLUE=new Color(35,93,157),BLUE_DARK=new Color(25,68,117),BLUE_HEADER=new Color(221,238,255),BLUE_LIGHT=new Color(214,234,253),BLUE_SOFT=new Color(242,248,255),BLUE_BORDER=new Color(203,223,243);
    public static final Color BG_APP=new Color(244,247,252),BORDER=new Color(225,230,239),TEXT_MAIN=new Color(35,46,64),TEXT_MUTED=new Color(111,125,146);
    public static final Color GREEN_500=new Color(29,132,101),RED_500=new Color(188,53,66),WHITE=Color.WHITE;
    public static final Font FONT_NORMAL=new Font("Segoe UI",Font.PLAIN,14),FONT_BOLD=new Font("Segoe UI",Font.BOLD,14),FONT_TITLE=new Font("Segoe UI",Font.BOLD,18);
    public static final Font FONT_EMOJI=new Font("Segoe UI Emoji",Font.PLAIN,14),FONT_LOGO=new Font("Segoe UI",Font.BOLD,22);

    public static void installTheme(){
        var keys=UIManager.getDefaults().keys();while(keys.hasMoreElements()){Object key=keys.nextElement();if(UIManager.get(key) instanceof javax.swing.plaf.FontUIResource)UIManager.put(key,new javax.swing.plaf.FontUIResource(FONT_NORMAL));}
        UIManager.put("Panel.background",WHITE);UIManager.put("OptionPane.background",WHITE);UIManager.put("TextField.background",WHITE);
        UIManager.put("TextField.selectionBackground",BLUE_LIGHT);UIManager.put("TextField.selectionForeground",BLUE_DARK);
        UIManager.put("ComboBox.selectionBackground",MIT_RED_LIGHT);UIManager.put("ComboBox.selectionForeground",MIT_RED);
        UIManager.put("Table.selectionBackground",BLUE_LIGHT);UIManager.put("Table.selectionForeground",BLUE_DARK);
        UIManager.put("ScrollBar.width",12);UIManager.put("ToolTip.background",WHITE);UIManager.put("ToolTip.foreground",TEXT_MAIN);
    }
    public static void smooth(Graphics2D g){g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);}
    public static class RoundedBorder extends AbstractBorder{
        private final Color color;private final int radius;private final Insets insets;
        public RoundedBorder(Color color,int radius,Insets insets){this.color=color;this.radius=radius;this.insets=insets;}
        public Insets getBorderInsets(Component c){return (Insets)insets.clone();}
        public Insets getBorderInsets(Component c,Insets target){target.set(insets.top,insets.left,insets.bottom,insets.right);return target;}
        public void paintBorder(Component c,Graphics graphics,int x,int y,int w,int h){Graphics2D g=(Graphics2D)graphics.create();smooth(g);g.setColor(c.hasFocus()?MIT_RED:color);g.drawRoundRect(x,y,w-1,h-1,radius,radius);g.dispose();}
    }
    public static Border cardBorder(Color color,int padding){return new RoundedBorder(color,14,new Insets(padding,padding,padding,padding));}
    public static JButton createPrimaryBtn(String text){JButton b=new JButton(text);styleButton(b,"primary");return b;}
    public static JButton createSecondaryBtn(String text){JButton b=new JButton(text);styleButton(b,"secondary");return b;}
    public static JButton createListBtn(String text){JButton b=new JButton(text);styleButton(b,"list");return b;}
    public static JButton createActionButton(String text){
        String caption=switch(text){case "LÀM MỚI"->"Làm mới";case "THÊM MỚI"->"Thêm mới";case "CẬP NHẬT"->"Cập nhật";case "XÓA BỎ"->"Xóa";case "XUẤT FILE CSV"->"Xuất CSV";default->text;};
        JButton b=new JButton(caption);styleButton(b,actionVariant(caption));return b;
    }
    private static String actionVariant(String text){
        String s=text.toLowerCase(Locale.ROOT);
        if(s.contains("xóa")||s.startsWith("hủy"))return "danger";
        if(s.contains("xuất")||s.contains("tệp mẫu"))return "list";
        if(s.startsWith("lưu")||s.startsWith("cấp")||s.startsWith("thêm")||s.startsWith("cập nhật")||s.startsWith("thu ")||s.startsWith("xác nhận")||s.startsWith("đăng ký"))return "primary";
        return "secondary";
    }
    public static void styleButton(JButton b,String variant){
        b.putClientProperty("portal.button",variant);b.setUI(new BasicButtonUI(){
            public void paint(Graphics graphics,JComponent c){
                AbstractButton button=(AbstractButton)c;Graphics2D g=(Graphics2D)graphics.create();smooth(g);
                Color base=button.isEnabled()?button.getBackground():BORDER;
                if(button.isEnabled()&&button.getModel().isRollover())base=blend(base,variant.equals("secondary")?MIT_RED_LIGHT:Color.BLACK,variant.equals("secondary")?.6f:.10f);
                g.setColor(base);g.fillRoundRect(0,0,c.getWidth(),c.getHeight(),10,10);
                if(variant.equals("secondary")||button.hasFocus()){g.setColor(button.hasFocus()?MIT_RED:BORDER);g.drawRoundRect(0,0,c.getWidth()-1,c.getHeight()-1,10,10);}
                g.dispose();super.paint(graphics,c);
            }
            protected void paintText(Graphics g,JComponent c,Rectangle r,String text){
                if(c.isEnabled()){super.paintText(g,c,r,text);return;}
                g.setColor(TEXT_MUTED);g.setFont(c.getFont());g.drawString(text,r.x,r.y+g.getFontMetrics().getAscent());
            }
        });
        b.setOpaque(false);b.setContentAreaFilled(false);b.setFocusPainted(false);b.setFont(new Font("Segoe UI",Font.BOLD,13));
        b.setBorder(new EmptyBorder(10,16,10,16));b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setBackground(switch(variant){case "primary"->MIT_RED;case "list"->BLUE_HEADER;case "danger"->MIT_RED_LIGHT;default->WHITE;});
        b.setForeground(variant.equals("primary")?WHITE:variant.equals("list")?BLUE_DARK:MIT_RED);
    }
    public static void styleToggle(JButton button,boolean selected){
        styleButton(button,"secondary");button.setBackground(selected?MIT_RED:WHITE);button.setForeground(selected?WHITE:MIT_RED);
        button.putClientProperty("portal.toggle",selected);
    }
    public static JTextField createInput(){
        JTextField field=new JTextField(){
            protected void paintComponent(Graphics graphics){super.paintComponent(graphics);Object hint=getClientProperty("JTextField.placeholderText");if(getText().isEmpty()&&!hasFocus()&&hint!=null){Graphics2D g=(Graphics2D)graphics.create();smooth(g);g.setColor(TEXT_MUTED);g.setFont(getFont());g.drawString(hint.toString(),getInsets().left,(getHeight()-g.getFontMetrics().getHeight())/2+g.getFontMetrics().getAscent());g.dispose();}}
        };styleInput(field);return field;
    }
    public static void styleInput(JTextComponent field){
        if(Boolean.TRUE.equals(field.getClientProperty("portal.field")))return;field.putClientProperty("portal.field",true);
        field.setFont(FONT_NORMAL);field.setForeground(TEXT_MAIN);field.setBackground(WHITE);field.setCaretColor(MIT_RED);field.setSelectionColor(BLUE_LIGHT);field.setSelectedTextColor(BLUE_DARK);
        field.setBorder(new RoundedBorder(BORDER,10,new Insets(9,12,9,12)));
        field.addFocusListener(new FocusAdapter(){public void focusGained(FocusEvent e){field.repaint();}public void focusLost(FocusEvent e){field.repaint();}});
    }
    public static JPanel createFormRow(String text,JComponent input){
        JPanel p=new JPanel(new BorderLayout(0,6));p.setOpaque(false);p.setMaximumSize(new Dimension(600,80));p.setBorder(new EmptyBorder(0,0,14,0));
        JLabel label=new JLabel(text);label.setFont(new Font("Segoe UI",Font.BOLD,13));label.setForeground(MIT_RED);p.add(label,BorderLayout.NORTH);p.add(input);return p;
    }
    public static void styleTable(JTable table){
        table.setFont(FONT_NORMAL);table.setRowHeight(42);table.setSelectionBackground(BLUE_LIGHT);table.setSelectionForeground(BLUE_DARK);
        table.setBackground(WHITE);table.setForeground(TEXT_MAIN);table.setShowVerticalLines(false);table.setShowHorizontalLines(true);table.setGridColor(BLUE_BORDER);table.setIntercellSpacing(new Dimension(0,1));table.setFillsViewportHeight(true);
        table.setDefaultRenderer(Object.class,new DataRenderer());table.setDefaultRenderer(Number.class,new DataRenderer());
        styleTableHeader(table);
    }
    public static void styleTableHeader(JTable table){
        JTableHeader header=table.getTableHeader();header.setFont(FONT_BOLD);header.setBackground(BLUE_HEADER);header.setForeground(BLUE_DARK);header.setPreferredSize(new Dimension(100,44));
        header.setDefaultRenderer(new DefaultTableCellRenderer(){
            public Component getTableCellRendererComponent(JTable t,Object value,boolean selected,boolean focus,int row,int col){
                super.getTableCellRendererComponent(t,value,selected,focus,row,col);setOpaque(true);setBackground(BLUE_HEADER);setForeground(BLUE_DARK);setFont(new Font("Segoe UI",Font.BOLD,13));setHorizontalAlignment(LEFT);setIcon(null);
                if(t.getRowSorter()!=null)for(var key:t.getRowSorter().getSortKeys())if(key.getColumn()==t.convertColumnIndexToModel(col)&&key.getSortOrder()!=SortOrder.UNSORTED){setIcon(new SortArrow(key.getSortOrder()==SortOrder.ASCENDING));setHorizontalTextPosition(LEFT);break;}
                setBorder(BorderFactory.createCompoundBorder(new MatteBorder(0,0,1,1,BLUE_BORDER),new EmptyBorder(0,11,0,9)));setToolTipText(value==null?null:value.toString());return this;
            }
        });
        for(int i=0;i<table.getColumnCount();i++)table.getColumnModel().getColumn(i).setHeaderRenderer(null);
    }
    private static class DataRenderer extends DefaultTableCellRenderer{
        public Component getTableCellRendererComponent(JTable t,Object value,boolean selected,boolean focus,int r,int c){
            super.getTableCellRendererComponent(t,value,selected,focus,r,c);setFont(FONT_NORMAL);setForeground(selected?BLUE_DARK:TEXT_MAIN);setBackground(selected?BLUE_LIGHT:r%2==0?WHITE:BLUE_SOFT);
            setHorizontalAlignment(value instanceof Number?RIGHT:LEFT);setBorder(new EmptyBorder(0,12,0,12));setToolTipText(value==null?null:value.toString());return this;
        }
    }
    private static class ListRenderer implements TableCellRenderer{
        private final TableCellRenderer delegate;ListRenderer(TableCellRenderer delegate){this.delegate=delegate;}
        public Component getTableCellRendererComponent(JTable t,Object v,boolean selected,boolean focus,int row,int col){
            Component c=delegate.getTableCellRendererComponent(t,v,selected,focus,row,col);
            if(c instanceof JLabel label){
                label.setBackground(selected?BLUE_LIGHT:row%2==0?WHITE:BLUE_SOFT);
                label.setFont(new Font("Segoe UI",label.getFont().getStyle(),14));label.setBorder(new EmptyBorder(0,12,0,12));label.setToolTipText(v==null?null:v.toString());
                if(selected)label.setForeground(BLUE_DARK);
            }
            return c;
        }
    }
    public static void columnWidths(JTable t,int...widths){
        t.putClientProperty("portal.widths",widths.clone());
        for(int i=0;i<Math.min(widths.length,t.getColumnCount());i++){t.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);t.getColumnModel().getColumn(i).setMinWidth(Math.min(60,widths[i]));}
        if(!Boolean.TRUE.equals(t.getClientProperty("portal.fit"))){
            t.putClientProperty("portal.fit",true);
            t.addHierarchyBoundsListener(new HierarchyBoundsAdapter(){public void ancestorResized(HierarchyEvent e){fitTableWidth(t);}});
            t.addHierarchyListener(e->{if((e.getChangeFlags()&HierarchyEvent.PARENT_CHANGED)!=0)SwingUtilities.invokeLater(()->fitTableWidth(t));});
        }
        fitTableWidth(t);
    }
    /** Fill wide windows while retaining horizontal scrolling in compact windows. */
    public static void fitTableWidth(JTable t){
        if(!(t.getClientProperty("portal.widths") instanceof int[] widths))return;
        int total=0;for(int width:widths)total+=width;
        int available=t.getParent() instanceof JViewport view?view.getExtentSize().width:0;
        int mode=available>=total?JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS:JTable.AUTO_RESIZE_OFF;
        if(t.getAutoResizeMode()!=mode){t.setAutoResizeMode(mode);for(int i=0;i<t.getColumnCount();i++){TableColumn column=t.getColumnModel().getColumn(i);int model=column.getModelIndex();if(model<widths.length){column.setPreferredWidth(widths[model]);column.setWidth(widths[model]);}}}
    }
    public static void applyTheme(Component root){
        if(root instanceof JTable table){
            styleTableHeader(table);table.setFont(FONT_NORMAL);table.setSelectionBackground(BLUE_LIGHT);table.setSelectionForeground(BLUE_DARK);table.setGridColor(BLUE_BORDER);table.setShowVerticalLines(false);table.setShowHorizontalLines(true);table.setIntercellSpacing(new Dimension(0,1));
            if(!Boolean.TRUE.equals(table.getClientProperty("portal.calendar"))){
                table.setRowHeight(42);for(int i=0;i<table.getColumnCount();i++){TableColumn col=table.getColumnModel().getColumn(i);TableCellRenderer renderer=col.getCellRenderer();if(renderer==null)renderer=table.getDefaultRenderer(table.getColumnClass(i));if(!(renderer instanceof ListRenderer))col.setCellRenderer(new ListRenderer(renderer));}
            }
            return;
        }
        if(root instanceof JButton b&&b.getClientProperty("portal.button")==null)styleButton(b,actionVariant(b.getText()));
        else if(root instanceof JTextField field)styleInput(field);
        else if(root instanceof JComboBox<?> combo&&combo.getClientProperty("portal.combo")==null){
            combo.putClientProperty("portal.combo",true);combo.setFont(FONT_NORMAL);combo.setBackground(WHITE);combo.setForeground(TEXT_MAIN);combo.setBorder(new RoundedBorder(BORDER,8,new Insets(2,8,2,4)));
            combo.setUI(new BasicComboBoxUI(){protected JButton createArrowButton(){JButton b=new JButton(){protected void paintComponent(Graphics graphics){Graphics2D g=(Graphics2D)graphics.create();smooth(g);g.setColor(isEnabled()?MIT_RED:TEXT_MUTED);g.setStroke(new BasicStroke(1.6f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));int x=getWidth()/2,y=getHeight()/2;g.drawLine(x-4,y-2,x,y+2);g.drawLine(x,y+2,x+4,y-2);g.dispose();}};b.setBackground(WHITE);b.setBorder(new EmptyBorder(0,8,0,8));b.setPreferredSize(new Dimension(26,30));b.setFocusPainted(false);b.getAccessibleContext().setAccessibleName("Mở danh sách lựa chọn");return b;}});
            combo.setPreferredSize(new Dimension(Math.max(150,combo.getPreferredSize().width),38));return;
        }else if(root instanceof JCheckBox check){check.setOpaque(false);check.setFont(FONT_NORMAL);check.setForeground(TEXT_MAIN);
        }else if(root instanceof JTabbedPane tabs&&tabs.getClientProperty("portal.tabs")==null){
            tabs.putClientProperty("portal.tabs",true);tabs.setFont(FONT_BOLD);tabs.setBackground(BG_APP);tabs.setForeground(MIT_RED);
            tabs.setUI(new BasicTabbedPaneUI(){
                protected void installDefaults(){super.installDefaults();tabInsets=new Insets(12,18,12,18);contentBorderInsets=new Insets(14,0,0,0);}
                protected void paintTabBackground(Graphics g,int placement,int index,int x,int y,int w,int h,boolean selected){g.setColor(selected?WHITE:BG_APP);g.fillRect(x,y,w,h);}
                protected void paintTabBorder(Graphics g,int placement,int index,int x,int y,int w,int h,boolean selected){if(selected){g.setColor(MIT_RED);g.fillRect(x+12,y+h-3,w-24,3);}}
                protected void paintContentBorder(Graphics g,int placement,int selected){}
                protected void paintFocusIndicator(Graphics g,int p,Rectangle[] r,int i,Rectangle icon,Rectangle text,boolean selected){}
            });
        }else if(root instanceof JScrollPane scroll){
            scroll.getViewport().setBackground(root.getBackground());if(scroll.getViewport().getView() instanceof JTable){scroll.getViewport().setBackground(WHITE);scroll.setBorder(new RoundedBorder(BLUE_BORDER,8,new Insets(1,1,1,1)));JPanel corner=new JPanel();corner.setBackground(BLUE_HEADER);scroll.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER,corner);}
            styleScrollBar(scroll.getVerticalScrollBar());styleScrollBar(scroll.getHorizontalScrollBar());
        }else if(root instanceof JLabel label){
            Font font=label.getFont();if(font!=null)label.setFont(new Font("Segoe UI",font.getStyle(),Math.max(12,font.getSize())));
            Container p=label.getParent();boolean property=false;
            if(p!=null&&p.getLayout() instanceof BorderLayout layout){Component input=layout.getLayoutComponent(BorderLayout.CENTER);property=input instanceof JTextComponent||input instanceof JComboBox;}
            if(p!=null&&p.getLayout() instanceof GridLayout grid&&grid.getColumns()==2){Component[] children=p.getComponents();for(int i=0;i<children.length;i+=2)if(children[i]==label)property=true;}
            if(p!=null&&p.getLayout() instanceof GridLayout grid&&grid.getColumns()==1){Component[] children=p.getComponents();for(int i=0;i<children.length-1;i++)if(children[i]==label&&children[i+1] instanceof JTextComponent)property=true;}
            if(property){label.setForeground(MIT_RED);label.setFont(new Font("Segoe UI",Font.BOLD,13));}
        }
        if(root instanceof JPanel panel&&Color.WHITE.equals(panel.getBackground())){
            Border border=panel.getBorder();if(border instanceof LineBorder)panel.setBorder(cardBorder(BORDER,1));
            else if(border instanceof CompoundBorder cb&&cb.getOutsideBorder() instanceof LineBorder)panel.setBorder(new CompoundBorder(cardBorder(BORDER,1),cb.getInsideBorder()));
        }
        if(root instanceof Container container)for(Component c:container.getComponents())applyTheme(c);
    }
    private static void styleScrollBar(JScrollBar bar){
        if(Boolean.TRUE.equals(bar.getClientProperty("portal.scroll")))return;bar.putClientProperty("portal.scroll",true);bar.setPreferredSize(new Dimension(12,12));bar.setUI(new BasicScrollBarUI(){
            protected void configureScrollBarColors(){thumbColor=new Color(190,205,225);trackColor=BLUE_SOFT;}
            protected JButton createDecreaseButton(int orientation){return zero();}protected JButton createIncreaseButton(int orientation){return zero();}
            private JButton zero(){JButton b=new JButton();b.setPreferredSize(new Dimension(0,0));return b;}
            protected void paintThumb(Graphics graphics,JComponent c,Rectangle r){Graphics2D g=(Graphics2D)graphics.create();smooth(g);g.setColor(isThumbRollover()?new Color(148,176,210):thumbColor);g.fillRoundRect(r.x+2,r.y+2,Math.max(1,r.width-4),Math.max(1,r.height-4),8,8);g.dispose();}
        });
    }
    public static JPanel statCard(String title,String value,Color accent){
        JPanel card=new JPanel(new BorderLayout(0,10));card.setBackground(WHITE);card.setBorder(cardBorder(BORDER,17));card.setPreferredSize(new Dimension(200,108));card.setMaximumSize(new Dimension(Integer.MAX_VALUE,112));
        JLabel name=new JLabel(title);name.setFont(new Font("Segoe UI",Font.PLAIN,13));name.setForeground(TEXT_MUTED);name.setToolTipText(title);
        JLabel number=new JLabel(value){
            protected void paintComponent(Graphics graphics){int size=25;while(size>13&&getFontMetrics(new Font("Segoe UI",Font.BOLD,size)).stringWidth(getText())>getWidth())size--;Font fitted=new Font("Segoe UI",Font.BOLD,size);setFont(fitted);Graphics2D g=(Graphics2D)graphics.create();g.setFont(fitted);super.paintComponent(g);g.dispose();}
        };number.setForeground(accent);number.setToolTipText(value);number.setFont(new Font("Segoe UI",Font.BOLD,25));card.add(name,BorderLayout.NORTH);card.add(number,BorderLayout.CENTER);
        JPanel line=new JPanel();line.setBackground(accent);line.setPreferredSize(new Dimension(0,3));card.add(line,BorderLayout.SOUTH);return card;
    }
    public static JPanel noteCard(String title,String text){
        JPanel card=new JPanel(new BorderLayout(0,12));card.setBackground(WHITE);card.setBorder(cardBorder(BORDER,24));JLabel name=new JLabel(title);name.setFont(FONT_TITLE);name.setForeground(MIT_RED);
        JTextArea description=new JTextArea(text);description.setEditable(false);description.setOpaque(false);description.setLineWrap(true);description.setWrapStyleWord(true);description.setFont(FONT_NORMAL);description.setForeground(TEXT_MUTED);card.add(name,BorderLayout.NORTH);card.add(description,BorderLayout.CENTER);return card;
    }
    public static JButton navigationButton(String text,int index){
        JButton b=new JButton(text);b.putClientProperty("portal.button","navigation");b.putClientProperty("nav.selected",false);
        b.setUI(new BasicButtonUI(){public void paint(Graphics graphics,JComponent component){JButton button=(JButton)component;Graphics2D g=(Graphics2D)graphics.create();smooth(g);boolean selected=Boolean.TRUE.equals(button.getClientProperty("nav.selected"));if(selected||button.getModel().isRollover()){g.setColor(selected?BURGUNDY_HOVER:BURGUNDY_DARK);g.fillRoundRect(0,1,component.getWidth(),component.getHeight()-2,10,10);if(selected){g.setColor(new Color(255,202,173));g.fillRoundRect(0,12,3,component.getHeight()-24,3,3);}}g.dispose();super.paint(graphics,component);}});
        b.setContentAreaFilled(false);b.setOpaque(false);b.setForeground(WHITE);b.setFont(new Font("Segoe UI",Font.PLAIN,13));b.setHorizontalAlignment(SwingConstants.LEFT);b.setFocusPainted(false);b.setBorder(new EmptyBorder(11,12,11,8));b.setIcon(new NavigationIcon(index));b.setIconTextGap(11);b.setMaximumSize(new Dimension(Integer.MAX_VALUE,44));b.setPreferredSize(new Dimension(216,44));b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));return b;
    }
    public static void selectNavigation(JButton b,boolean selected){b.putClientProperty("nav.selected",selected);b.setFont(new Font("Segoe UI",selected?Font.BOLD:Font.PLAIN,13));b.repaint();}
    private static Color blend(Color a,Color b,float ratio){return new Color(Math.round(a.getRed()*(1-ratio)+b.getRed()*ratio),Math.round(a.getGreen()*(1-ratio)+b.getGreen()*ratio),Math.round(a.getBlue()*(1-ratio)+b.getBlue()*ratio));}
    private record SortArrow(boolean up) implements Icon{
        public int getIconWidth(){return 9;}public int getIconHeight(){return 10;}public void paintIcon(Component c,Graphics g,int x,int y){g.setColor(BLUE_DARK);g.fillPolygon(new int[]{x,x+8,x+4},up?new int[]{y+7,y+7,y+2}:new int[]{y+2,y+2,y+7},3);}
    }
    private record NavigationIcon(int index) implements Icon{
        public int getIconWidth(){return 19;}public int getIconHeight(){return 19;}
        public void paintIcon(Component c,Graphics graphics,int x,int y){Graphics2D g=(Graphics2D)graphics.create();g.translate(x,y);smooth(g);g.setColor(new Color(255,221,214));g.setStroke(new BasicStroke(1.5f,BasicStroke.CAP_ROUND,BasicStroke.JOIN_ROUND));
            switch(index){case 0->{g.drawLine(2,8,9,2);g.drawLine(9,2,16,8);g.drawRect(4,8,11,9);g.drawRect(8,12,3,5);}case 1->{g.drawOval(6,2,6,6);g.drawArc(3,10,13,12,0,180);g.drawArc(0,9,6,8,75,90);}case 2->{g.drawRoundRect(2,4,15,13,2,2);g.drawLine(2,8,17,8);g.drawLine(6,2,6,6);g.drawLine(13,2,13,6);g.fillRect(5,11,3,3);}case 3->{g.drawRoundRect(3,2,13,15,2,2);g.drawLine(6,7,12,7);g.drawLine(6,11,13,11);g.drawLine(6,14,10,14);}case 4->{g.drawRoundRect(1,4,17,12,3,3);g.drawLine(2,8,17,8);g.drawLine(11,12,15,12);}case 5->{g.drawLine(2,17,17,17);g.drawRect(4,10,2,7);g.drawRect(9,5,2,12);g.drawRect(14,2,2,15);}case 6->{g.drawLine(9,3,9,13);g.drawLine(5,7,9,3);g.drawLine(9,3,13,7);g.drawLine(3,12,3,17);g.drawLine(3,17,16,17);g.drawLine(16,17,16,12);}case 7->{g.drawOval(2,2,8,8);g.drawLine(9,9,16,16);g.drawLine(13,13,16,10);}case 8->{g.drawLine(2,4,17,4);g.drawLine(2,10,17,10);g.drawLine(2,16,17,16);g.fillOval(5,2,4,4);g.fillOval(12,8,4,4);g.fillOval(7,14,4,4);}case 10->{g.drawLine(8,2,2,2);g.drawLine(2,2,2,17);g.drawLine(2,17,8,17);g.drawLine(7,9,17,9);g.drawLine(13,5,17,9);g.drawLine(17,9,13,13);}default->{g.drawOval(2,2,15,15);g.drawLine(9,5,9,10);g.drawLine(9,10,13,12);}}g.dispose();
        }
    }
}
