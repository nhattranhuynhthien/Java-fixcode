import java.sql.*;
public class UpdateDB {
    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/qltourdulich", "root", "");
        c.createStatement().executeUpdate("ALTER TABLE taikhoan ADD COLUMN uy_quyen TINYINT(1) DEFAULT 0");
        System.out.println("Column added");
    }
}
