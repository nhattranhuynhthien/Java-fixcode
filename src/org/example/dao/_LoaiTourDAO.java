package org.example.dao;

import org.example.dto._LoaiTourDTO;
import java.sql.*;
import java.util.ArrayList;

public class _LoaiTourDAO {
    Connection c = _MyConnection.getConnection();

    public _LoaiTourDAO(){
    }

    
    public ArrayList<_LoaiTourDTO> getAllLoaiTour(){
        ArrayList<_LoaiTourDTO> lsCate = new ArrayList<>();
        try {
            String sql = "SELECT * FROM loaitour";
            PreparedStatement ps = c.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                
                _LoaiTourDTO t = new _LoaiTourDTO(
                        rs.getString("MaLoaiTour"),
                        rs.getString("TheLoai"),
                        rs.getString("mota"),        
                        rs.getInt("trangthai")       
                );
                lsCate.add(t);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lsCate;
    }

    
    public boolean addLoaiTour(_LoaiTourDTO t){
        String sql = "INSERT INTO loaitour (MaLoaiTour, TheLoai, trangthai, mota) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, t.getMaLoaiTour());
            ps.setString(2, t.getTheLoai());
            ps.setInt(3, t.getTrangThai());
            ps.setString(4, t.getMoTa());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean removeLoaiTour(String maLoaiTour){
        try{
            String sql = "DELETE FROM loaitour WHERE maloaitour = ?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, maLoaiTour);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public boolean editLoaiTour(_LoaiTourDTO t){
        String sql = "UPDATE loaitour SET theloai = ?, mota = ?, trangthai = ? WHERE maloaitour = ?";
        try {
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, t.getTheLoai());
            ps.setString(2, t.getMoTa());
            ps.setInt(3, t.getTrangThai());
            ps.setString(4, t.getMaLoaiTour());

            return ps.executeUpdate() > 0;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
}