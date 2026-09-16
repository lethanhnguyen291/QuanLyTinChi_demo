package utils;
import config.DBConnect;
import java.sql.*;
import java.util.*;
public final class Db {
    private Db() {}
    public interface Work<T> { T run(Connection c) throws Exception; }
    public static <T> T transaction(Work<T> work) throws Exception {
        try(Connection c=DBConnect.getConnection()) {
            c.setAutoCommit(false);
            // The SQL Server driver uses IMPLICIT_TRANSACTIONS. Start exactly one
            // explicit transaction so an initial sp_getapplock cannot create nesting.
            try(Statement start=c.createStatement()){start.execute("SET IMPLICIT_TRANSACTIONS OFF; BEGIN TRANSACTION;");}
            try { T result=work.run(c); c.commit(); return result; }
            catch(Exception e) { try { c.rollback(); } catch(SQLException r) { e.addSuppressed(r); } throw e; }
        }
    }
    public static PreparedStatement prepare(Connection c,String sql,Object... args) throws SQLException {
        PreparedStatement p=c.prepareStatement(sql);
        try { p.setQueryTimeout(15); for(int i=0;i<args.length;i++) p.setObject(i+1,args[i]); return p; }
        catch(SQLException e) { p.close(); throw e; }
    }
    public static int update(Connection c,String sql,Object... args) throws SQLException {
        try(PreparedStatement p=prepare(c,sql,args)) { return p.executeUpdate(); }
    }
    public static List<Map<String,Object>> rows(Connection c,String sql,Object... args) throws SQLException {
        List<Map<String,Object>> list=new ArrayList<>();
        try(PreparedStatement p=prepare(c,sql,args);ResultSet r=p.executeQuery()) {
            ResultSetMetaData m=r.getMetaData();
            while(r.next()) { Map<String,Object> row=new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
                for(int i=1;i<=m.getColumnCount();i++) row.put(m.getColumnLabel(i),r.getObject(i)); list.add(row); }
        } return list;
    }
    public static List<Map<String,Object>> rows(String sql,Object... args) throws SQLException {
        try(Connection c=DBConnect.getConnection()) { return rows(c,sql,args); }
    }
    public static Object scalar(Connection c,String sql,Object... args) throws SQLException {
        try(PreparedStatement p=prepare(c,sql,args);ResultSet r=p.executeQuery()) { return r.next()?r.getObject(1):null; }
    }
    public static int count(Connection c,String sql,Object... args) throws SQLException {
        Object n=scalar(c,sql,args); return n==null?0:((Number)n).intValue();
    }
    public static void lock(Connection c,String key) throws SQLException {
        if(c.getAutoCommit())throw new SQLException("Application lock requires a transaction.");
        try(PreparedStatement p=prepare(c,"DECLARE @rc int; EXEC @rc=sys.sp_getapplock @Resource=?,@LockMode='Exclusive',@LockOwner='Transaction',@LockTimeout=8000; SELECT @rc AS LockResult", "QLTC:"+key.toUpperCase(Locale.ROOT));ResultSet r=p.executeQuery()) {
            if(!r.next()||r.getInt(1)<0) throw new SQLException("Dữ liệu đang được xử lý. Vui lòng thử lại.");
        }
    }
    public static String str(Map<String,Object> r,String key) { return Objects.toString(r.get(key),""); }
    public static int number(Map<String,Object> r,String key) { Object v=r.get(key);return v==null?0:((Number)v).intValue(); }
}
