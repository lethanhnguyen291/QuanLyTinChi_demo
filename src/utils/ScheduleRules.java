package utils;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.*;
public final class ScheduleRules{
    private ScheduleRules(){}
    public static String normalize(String v){return Normalizer.normalize(Objects.toString(v,""),Normalizer.Form.NFD).replaceAll("\\p{M}","").replace('đ','d').replace('Đ','D').toLowerCase(Locale.ROOT).trim();}
    public static int day(String v){String s=normalize(v);if(s.equals("cn")||s.contains("chu nhat")||s.equals("8"))return 8;
        Matcher m=Pattern.compile("^(?:thu\\s*)?([2-7])$").matcher(s);if(!m.matches())throw new IllegalArgumentException("Thứ học không hợp lệ: "+v);return Integer.parseInt(m.group(1));}
    public static BitSet periods(String v){String s=normalize(v).replace("tiet","").replace('–','-').replace('—','-').replaceAll("\\s+","");BitSet result=new BitSet(16);
        for(String token:s.split("[,;]",-1)){if(!token.matches("\\d{1,2}(-\\d{1,2})?"))throw new IllegalArgumentException("Tiết học cần dạng 1-3 hoặc 1,3,5: "+v);
            String[] r=token.split("-");int a=Integer.parseInt(r[0]),b=Integer.parseInt(r[r.length-1]);if(a<1||b>15||a>b)throw new IllegalArgumentException("Tiết học phải nằm trong 1–15, bắt đầu không vượt kết thúc.");result.set(a,b+1);}return result;}
    public static boolean overlaps(String d1,String p1,String d2,String p2){if(day(d1)!=day(d2))return false;BitSet a=periods(p1);a.and(periods(p2));return !a.isEmpty();}
}
