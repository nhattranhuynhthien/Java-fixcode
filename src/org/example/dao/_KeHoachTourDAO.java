package org.example.dao;

import org.example.dto._KeHoachTourDTO;

import java.sql.*;
import java.util.ArrayList;

public class _KeHoachTourDAO {
    Connection c = _MyConnection.getConnection();

    public _KeHoachTourDAO(){
    }

    public ArrayList<_KeHoachTourDTO> getAllKeHoachTours(){
        ArrayList<_KeHoachTourDTO> lsKeHoachTour = new ArrayList<>();
        try {
            String sql = "select * from kehoachtour";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                _KeHoachTourDTO t = new _KeHoachTourDTO(
                        rs.getString(1),
                        rs.getDate(2).toLocalDate(),
                        rs.getDate(3).toLocalDate(),
                        rs.getInt(4),
                        rs.getLong(5),
                        rs.getLong(6),
                        rs.getString(8),
                        rs.getString(7)
                );
                lsKeHoachTour.add(t);
            }
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return lsKeHoachTour;
    }

    public boolean addKeHoachTour(_KeHoachTourDTO t){
        String sql = "INSERT INTO kehoachtour VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, t.getMaKHTour());
            ps.setDate(2, Date.valueOf(t.getNgayKhoiHanh()));
            ps.setDate(3, Date.valueOf(t.getNgayKetThuc()));
            ps.setInt(4, t.getTongSoVe());
            ps.setLong(5, t.getTongChi());
            ps.setLong(6, t.getTongThu());
            ps.setString(7, t.getMaTour());
            ps.setString(8, t.getMaNVHD());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeKeHoachTour(String maKeHoachTour){
        try{
            String sql = "delete from kehoachtour where makhtour = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, maKeHoachTour);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean editKeHoachTour(_KeHoachTourDTO t){
        String sql = "UPDATE kehoachtour SET ngaykhoihanh=?, ngayketthuc=?, tongsove=?, tongchi=?, tongthu=?, matour=?, manvhd=? WHERE makhtour=?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(t.getNgayKhoiHanh()));
            ps.setDate(2, Date.valueOf(t.getNgayKetThuc()));
            ps.setInt(3, t.getTongSoVe());
            ps.setLong(4, t.getTongChi());
            ps.setLong(5, t.getTongThu());
            ps.setString(6, t.getMaTour());
            ps.setString(7, t.getMaNVHD());
            ps.setString(8, t.getMaKHTour());

            return ps.executeUpdate() > 0;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhatSoluong(int sl, String makhtour){
        String sql="Update kehoachtour set tongsove=tongsove-? where makhtour=?";

        try(PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, sl);
            ps.setString(2, makhtour);
            ps.executeUpdate();
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    
    
    
    
    public boolean capNhatDoanhThuVaChiPhi(String maKHTour) {
        String sqlChi = "UPDATE kehoachtour SET tongchi = COALESCE((SELECT SUM(tongchi) FROM ctietkhtour WHERE makhtour = ?), 0) WHERE makhtour = ?";
        String sqlThu = "UPDATE kehoachtour SET tongthu = COALESCE((SELECT SUM(giave) FROM khang_khtour WHERE makhtour = ?), 0) WHERE makhtour = ?";

        try {
            try(PreparedStatement psChi = c.prepareStatement(sqlChi)) {
                psChi.setString(1, maKHTour);
                psChi.setString(2, maKHTour);
                psChi.executeUpdate();
            }
            try(PreparedStatement psThu = c.prepareStatement(sqlThu)) {
                psThu.setString(1, maKHTour);
                psThu.setString(2, maKHTour);
                psThu.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}