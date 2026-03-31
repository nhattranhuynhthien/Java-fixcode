/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.dao;
import org.example.dto.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Admin
 */
public class KhachHangDAO {
    public ArrayList<KhachHangDTO> layDanhSachKHang() {
        ArrayList<KhachHangDTO> dsKH = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang";
        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                KhachHangDTO khang = mapToKhachHang(rs);
                dsKH.add(khang);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsKH;
    }

    public boolean themKhachHang(KhachHangDTO khang) {
        String sql = "INSERT INTO KhachHang (MaKH, Ho, Ten, DiaChi, SDT, NgaySinh) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, khang.getMaKH());
            pstmt.setString(2, khang.getHo());
            pstmt.setString(3, khang.getTen());
            pstmt.setString(4, khang.getDiaChi());
            pstmt.setString(5, khang.getSdt());
            pstmt.setDate(6, Date.valueOf(khang.getNgaySinh()));
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public KhachHangDTO timKhachHangTheoMa(String maKH) {
        String sql = "SELECT * FROM KhachHang WHERE MaKH = ?";
        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKH);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapToKhachHang(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<KhachHangDTO> timKhachHangTheoNgaySinh(LocalDate date) {
        List<KhachHangDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE NgaySinh = ?";
        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDate(1, Date.valueOf(date));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapToKhachHang(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<KhachHangDTO> timKhachHang(String column, String keyword) {
        List<KhachHangDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM KhachHang WHERE " + column + " LIKE ?";
        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapToKhachHang(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean xoaKhachHang(String maKH) {
        String sql = "DELETE FROM KhachHang WHERE MaKH = ?";
        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, maKH);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean suaKhachHang(KhachHangDTO khang) {
        String sql = "UPDATE KhachHang SET Ho = ?, Ten = ?, DiaChi = ?, SDT = ?, NgaySinh = ? WHERE MaKH = ?";
        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, khang.getHo());
            pstmt.setString(2, khang.getTen());
            pstmt.setString(3, khang.getDiaChi());
            pstmt.setString(4, khang.getSdt());
            pstmt.setDate(5, Date.valueOf(khang.getNgaySinh()));
            pstmt.setString(6, khang.getMaKH());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private KhachHangDTO mapToKhachHang(ResultSet rs) throws SQLException {
        String maKH = rs.getString("MaKH");
        String ho = rs.getString("Ho");
        String ten = rs.getString("Ten");
        String diaChi = rs.getString("DiaChi");
        String sdt = rs.getString("SDT");
        LocalDate ngaySinh = rs.getDate("NgaySinh").toLocalDate();
        return new KhachHangDTO(maKH, ho, ten, diaChi, sdt, ngaySinh);
    }

    public boolean capNhatKhachHang(KhachHangDTO khang) {
        String sql = "UPDATE KhachHang SET Ho = ?, Ten = ?, DiaChi = ?, SDT = ?, NgaySinh = ? WHERE MaKH = ?";
        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, khang.getHo());
            pstmt.setString(2, khang.getTen());
            pstmt.setString(3, khang.getDiaChi());
            pstmt.setString(4, khang.getSdt());
            pstmt.setDate(5, Date.valueOf(khang.getNgaySinh()));
            pstmt.setString(6, khang.getMaKH());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
