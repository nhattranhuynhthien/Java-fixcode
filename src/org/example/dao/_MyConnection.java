
package org.example.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class _MyConnection {
    public static Connection getConnection(){
        Connection conn=null;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url="jdbc:mysql://localhost:3306/qltourdulich";
            String user="root";
            String password="";

            conn=DriverManager.getConnection(url,user,password);
            System.out.println("Ket noi thanh cong");

        } catch(ClassNotFoundException e){
            System.out.println("Loi : khong tim thay thu vien MySQL JDBC");
        } catch(SQLException e){
            System.out.println("Loi khong the ket noi");
        }
        return conn;
    }

}