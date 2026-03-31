package org.example.dao;

import org.example.dto._CTietKHTourDTO;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class _CTietKHTourDAO {
    Connection c = _MyConnection.getConnection();

    public _CTietKHTourDAO(){
    }

    // ====== HÀM HỖ TRỢ: Đổi ngày từ giao diện (dd/MM/yyyy) sang chuẩn MySQL (YYYY-MM-DD) ======
    private java.sql.Date convertToSqlDate(String dateStr) {
        try {
            if (dateStr == null || dateStr.trim().isEmpty()) return null;
            if (dateStr.contains("/")) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate localDate = LocalDate.parse(dateStr, formatter);
                return java.sql.Date.valueOf(localDate);
            } else {
                return java.sql.Date.valueOf(dateStr); // Nếu đã là yyyy-MM-dd
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ====== HÀM HỖ TRỢ: Đổi ngày từ MySQL (YYYY-MM-DD) sang chuẩn giao diện (dd/MM/yyyy) ======
    private String convertToGuiDateString(Date sqlDate) {
        if (sqlDate == null) return "";
        return sqlDate.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    public ArrayList<_CTietKHTourDTO> getAllCTietKHTours(){
        ArrayList<_CTietKHTourDTO> lsCTietKHTours = new ArrayList<>();
        try {
            String sql = "select * from ctietkhtour";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                // Chuyển đổi ngày từ CSDL ra định dạng dd/MM/yyyy cho DTO hiển thị lên bảng
                String ngayThucHien = convertToGuiDateString(rs.getDate(2));
                if (ngayThucHien.isEmpty()) ngayThucHien = rs.getString(2); // Fallback

                _CTietKHTourDTO t = new _CTietKHTourDTO(
                        rs.getString(1),
                        ngayThucHien,
                        rs.getLong(3),
                        rs.getLong(4),
                        rs.getLong(5),
                        rs.getLong(6),
                        rs.getString(7),
                        rs.getString(8),
                        rs.getString(9)
                );
                lsCTietKHTours.add(t);
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lsCTietKHTours;
    }

    // Lệnh cập nhật tổng chi tự động cho Kế hoạch Tour
    private void updateTongChiKeHoachTour(String maKHTour) throws SQLException {
        String sql = "UPDATE kehoachtour SET tongchi = COALESCE((SELECT SUM(tongchi) FROM ctietkhtour WHERE makhtour = ?), 0) WHERE makhtour = ?";
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maKHTour);
            ps.setString(2, maKHTour);
            ps.executeUpdate();
        }
    }

    public boolean addCTietKHTour(_CTietKHTourDTO t){
        String sql = "INSERT INTO ctietkhtour VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            c.setAutoCommit(false); // Bắt đầu Transaction

            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, t.getMaCTietKHTour());
            // FIX: Dùng hàm convert để giải quyết triệt để lỗi Incorrect Date Value
            ps.setDate(2, convertToSqlDate(t.getNgayThucHien()));
            ps.setLong(3, t.getTongChi());
            ps.setLong(4, t.getTienO());
            ps.setLong(5, t.getTienAn());
            ps.setLong(6, t.getTienDiLai());
            ps.setString(7, t.getDiemDi());
            ps.setString(8, t.getDiemDen());
            ps.setString(9, t.getMaKHTour());

            ps.executeUpdate();

            // Tự động tính lại tổng chi cho KHTour
            updateTongChiKeHoachTour(t.getMaKHTour());

            c.commit();
            return true;
        } catch (SQLException e) {
            try { c.rollback(); } catch(SQLException ex){}
            e.printStackTrace();
            return false;
        } finally {
            try { c.setAutoCommit(true); } catch(SQLException ex){}
        }
    }

    public boolean editCTietKHTour(_CTietKHTourDTO t){
        String sql = "UPDATE ctietkhtour SET ngaythuchien=?, tongchi=?, tieno=?, tienan=?, tiendilai=?, diemdi=?, diemden=?, makhtour=? WHERE mactietkhtour=?";
        try {
            c.setAutoCommit(false);

            PreparedStatement ps = c.prepareStatement(sql);
            // FIX: Tương tự như hàm Thêm
            ps.setDate(1, convertToSqlDate(t.getNgayThucHien()));
            ps.setLong(2, t.getTongChi());
            ps.setLong(3, t.getTienO());
            ps.setLong(4, t.getTienAn());
            ps.setLong(5, t.getTienDiLai());
            ps.setString(6, t.getDiemDi());
            ps.setString(7, t.getDiemDen());
            ps.setString(8, t.getMaKHTour());
            ps.setString(9, t.getMaCTietKHTour());

            ps.executeUpdate();

            updateTongChiKeHoachTour(t.getMaKHTour());

            c.commit();
            return true;
        } catch (SQLException e) {
            try { c.rollback(); } catch(SQLException ex){}
            e.printStackTrace();
            return false;
        } finally {
            try { c.setAutoCommit(true); } catch(SQLException ex){}
        }
    }

    public boolean removeCTietKHTour(String maCTietKHTour){
        String sqlSelect = "SELECT makhtour FROM ctietkhtour WHERE mactietkhtour = ?";
        String sqlDelete = "DELETE FROM ctietkhtour WHERE mactietkhtour = ?";
        try {
            c.setAutoCommit(false);

            String maKHTour = null;
            try (PreparedStatement psSel = c.prepareStatement(sqlSelect)) {
                psSel.setString(1, maCTietKHTour);
                ResultSet rs = psSel.executeQuery();
                if (rs.next()) maKHTour = rs.getString("makhtour");
            }

            if (maKHTour == null) return false;

            try (PreparedStatement psDel = c.prepareStatement(sqlDelete)) {
                psDel.setString(1, maCTietKHTour);
                int rows = psDel.executeUpdate();
                if (rows > 0) {
                    updateTongChiKeHoachTour(maKHTour);
                    c.commit();
                    return true;
                }
            }
            c.rollback();
            return false;
        } catch (SQLException e) {
            try { c.rollback(); } catch(SQLException ex){}
            e.printStackTrace();
            return false;
        } finally {
            try { c.setAutoCommit(true); } catch(SQLException ex){}
        }
    }
}