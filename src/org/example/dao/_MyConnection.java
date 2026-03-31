/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Admin
 */
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
