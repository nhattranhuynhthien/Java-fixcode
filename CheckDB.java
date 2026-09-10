import java.sql.*;
public class CheckDB {
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/qltourdulich", "root", "");
        ResultSet rs = c.createStatement().executeQuery("SHOW COLUMNS FROM nhanvien");
        while(rs.next()) System.out.println(rs.getString(1));
    }
}
