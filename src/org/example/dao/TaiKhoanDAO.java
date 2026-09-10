package org.example.dao;
import org.example.dto.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TaiKhoanDAO {

    public TaiKhoanDTO dangNhap(String username, String password) {
        String sql = "SELECT username, password, position, uy_quyen FROM taikhoan WHERE username = ? AND password = ?";

        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new TaiKhoanDTO(
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("position"),
                            rs.getBoolean("uy_quyen")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean taoTaiKhoanNhanVien(String maNV, String chucVu) {
        String position = chucVu == null ? "" : chucVu.trim();
        String matKhauMacDinh = laQuanLy(position) ? "456" : "123";
        String sql = "INSERT INTO taikhoan (username, password, position, uy_quyen) VALUES (?, ?, ?, 0)";

        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, maNV);
            pstmt.setString(2, matKhauMacDinh);
            pstmt.setString(3, position);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatUyQuyen(String username, boolean uyQuyen) {
        String sql = "UPDATE taikhoan SET uy_quyen = ? WHERE username = ?";
        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, uyQuyen);
            pstmt.setString(2, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatChucVu(String username, String chucVu) {
        String sql = "UPDATE taikhoan SET position = ? WHERE username = ?";
        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, chucVu);
            pstmt.setString(2, username);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean dangCoQuanLyUyQuyen() {
        String sql = "SELECT COUNT(*) FROM taikhoan WHERE (LOWER(position) LIKE '%quản lý%' OR LOWER(position) LIKE '%quan ly%' OR LOWER(position) LIKE '%quan li%' OR LOWER(position) LIKE '%quản lí%') AND uy_quyen = 1";
        try (Connection conn = _MyConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean laQuanLy(String chucVu) {
        String lower = chucVu.toLowerCase();
        return lower.equals("quản lí") || lower.equals("quan li") || lower.equals("quản lý") || lower.equals("quan ly");
    }
}
