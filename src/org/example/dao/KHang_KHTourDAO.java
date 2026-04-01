/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.dao;
import org.example.dto.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Admin
 */
public class KHang_KHTourDAO {
    public ArrayList<KHang_KHTourDTO> layDanhSachKHang_KHTour() {
        ArrayList<KHang_KHTourDTO> dsKHang_KHTourDTO = new ArrayList<>();
        String sql = "SELECT * FROM KHang_KHTour";
        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String MaKHTour = rs.getString("MaKHTour");
                String MaKHang = rs.getString("MaKHang");
                long GiaVe = rs.getLong("GiaVe");
                KHang_KHTourDTO kht = new KHang_KHTourDTO(MaKHTour, MaKHang, GiaVe);
                dsKHang_KHTourDTO.add(kht);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsKHang_KHTourDTO;
    }

public boolean themKHang_KHTour(KHang_KHTourDTO kht) {

    String sql = "INSERT INTO khang_khtour (MaKHang, MaKHTour, GiaVe) VALUES (?, ?, ?)";

    try (Connection conn = _MyConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, kht.getMaKHang().trim());
        pstmt.setString(2, kht.getMaKHTour().trim());
        pstmt.setLong(3, kht.getGiaVe());

        int rows = pstmt.executeUpdate();
        return rows > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

    public KHang_KHTourDTO timKHang_KHTourTheoMaTour(String MaKHTour) {
        String sql = "SELECT * FROM KHang_KHTour WHERE MaKHTour = ?";
        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, MaKHTour);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String MaKHang = rs.getString("MaKHang");
                    long GiaVe = rs.getLong("GiaVe");
                    return new KHang_KHTourDTO(MaKHTour, MaKHang, GiaVe);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<KHang_KHTourDTO> timKHang_KHTours(String column, String value) {
        List<KHang_KHTourDTO> results = new ArrayList<>();
        String sql = "SELECT * FROM KHang_KHTour WHERE " + column + " LIKE ?";
        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + value + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String MaKHTour = rs.getString("MaKHTour");
                    String MaKHang = rs.getString("MaKHang");
                    long GiaVe = rs.getLong("GiaVe");
                    results.add(new KHang_KHTourDTO(MaKHTour, MaKHang, GiaVe));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<KHang_KHTourDTO> timKHang_KHToursTheoHo(String ho) {
        List<KHang_KHTourDTO> results = new ArrayList<>();
        String sql = """
            SELECT kk.MaKHTour, kk.MaKHang, kk.GiaVe
            FROM KHang_KHTour kk
            JOIN khachhang kh ON kk.MaKHang = kh.MaKHang
            WHERE kh.Ho LIKE ?
            """;
        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + ho + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String MaKHTour = rs.getString("MaKHTour");
                    String MaKHang = rs.getString("MaKHang");
                    long GiaVe = rs.getLong("GiaVe");
                    results.add(new KHang_KHTourDTO(MaKHTour, MaKHang, GiaVe));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public List<KHang_KHTourDTO> timKHang_KHToursTheoTen(String ten) {
        List<KHang_KHTourDTO> results = new ArrayList<>();
        String sql = """
            SELECT kk.MaKHTour, kk.MaKHang, kk.GiaVe
            FROM KHang_KHTour kk
            JOIN khachhang kh ON kk.MaKHang = kh.MaKHang
            WHERE kh.Ten LIKE ?
            """;
        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + ten + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String MaKHTour = rs.getString("MaKHTour");
                    String MaKHang = rs.getString("MaKHang");
                    long GiaVe = rs.getLong("GiaVe");
                    results.add(new KHang_KHTourDTO(MaKHTour, MaKHang, GiaVe));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public boolean xoaKHang_KHTour(String MaKHTour, String MaKHang) {
        String sql = "DELETE FROM KHang_KHTour WHERE MaKHTour = ? AND MaKHang = ?";
        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, MaKHTour);
            pstmt.setString(2, MaKHang);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private KHang_KHTourDTO mapToKHang_KHTour(ResultSet rs) throws SQLException {
        String MaKHTour = rs.getString("MaKHTour");
        String MaKHang = rs.getString("MaKHang");
        long GiaVe = rs.getLong("GiaVe");
        return new KHang_KHTourDTO(MaKHTour, MaKHang, GiaVe);
    }

    public boolean capNhatKHang_KHTour(KHang_KHTourDTO kht) {
        // Fix: Chỉ cập nhật giá vé dựa trên định danh của cả 2 khóa
        String sql = "UPDATE KHang_KHTour SET GiaVe = ? WHERE MaKHTour = ? AND MaKHang = ?";
        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, kht.getGiaVe());
            pstmt.setString(2, kht.getMaKHTour());
            pstmt.setString(3, kht.getMaKHang());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public long layDonGiaTheoMaKHTour(String maKHTour){
    String sql = """
        SELECT DonGia
        FROM tour t
        JOIN kehoachtour k ON t.MaTour = k.MaTour
        WHERE k.MaKHTour = ?
        """;

    try(Connection conn = _MyConnection.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql)){

        ps.setString(1, maKHTour);
        ResultSet rs = ps.executeQuery();

        if(rs.next()){
            return rs.getLong("DonGia");
        }

    }catch(Exception e){
        e.printStackTrace();
    }

    return 0;
}
}
