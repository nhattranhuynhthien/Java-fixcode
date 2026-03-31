package org.example.dao;
import org.example.dto.DiaDiemDTO;
import java.util.*;
import java.sql.*;

public class DiaDiemDAO {

    public ArrayList<DiaDiemDTO> getDs() {
        ArrayList<DiaDiemDTO> ds = new ArrayList<>();

        String sql = "Select * from Diadiem";
        try(Connection conn = _MyConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                DiaDiemDTO dd = maptoDiaDiem(rs);
                ds.add(dd);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ds;
    }

    public DiaDiemDTO maptoDiaDiem(ResultSet rs) throws SQLException{
        String madiadiem = rs.getString("MaDiaDiem");
        String tendd = rs.getString("TenDiaDiem");
        String diachi = rs.getString("DiaChi");
        String quocgia = rs.getString("QuocGia");

        return new DiaDiemDTO(madiadiem, tendd, diachi, quocgia);
    }

    public boolean themDiaDiem(DiaDiemDTO dd){
        String sql = "Insert into DiaDiem(MaDiaDiem,TenDiaDiem,DiaChi,QuocGia) Values (?,?,?,?) ";

        try(Connection conn = _MyConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setNString(1, dd.getMaDiaDiem());
            ps.setNString(2, dd.getTenDiaDiem());
            ps.setNString(3, dd.getdiachi());
            ps.setNString(4, dd.getQuocGia());

            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaDiaDiem(DiaDiemDTO dd){
        String sql = "Delete from DiaDiem where TenDiaDiem=?";

        try(Connection conn = _MyConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, dd.getTenDiaDiem());
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean suaDiaDiem(DiaDiemDTO dd){
        String sql = "Update DiaDiem set tendiadiem=?, DiaChi=?,QuocGia=? where madiadiem=?";

        try(Connection conn = _MyConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setNString(1, dd.getTenDiaDiem());
            ps.setNString(2, dd.getdiachi());
            ps.setNString(3, dd.getQuocGia());
            ps.setNString(4, dd.getMaDiaDiem());
            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }



    // Đã sửa lỗi thiếu dấu ? trong câu SQL
    public ArrayList<DiaDiemDTO> getDstheoDiaChi(String diachi){
        ArrayList<DiaDiemDTO> ds = new ArrayList<>();
        String sql = "Select * from DiaDiem where diachi like ?";
        try(Connection conn = _MyConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, "%" + diachi + "%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                DiaDiemDTO dd = maptoDiaDiem(rs);
                ds.add(dd);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<DiaDiemDTO> getDstheoQuocGia(String quocgia){
        ArrayList<DiaDiemDTO> ds = new ArrayList<>();
        String sql = "Select * from DiaDiem where quocgia like ?";
        try(Connection conn = _MyConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setString(1, "%" + quocgia + "%");
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                DiaDiemDTO dd = maptoDiaDiem(rs);
                ds.add(dd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // HÀM MỚI: Tìm kiếm theo Tên Địa Điểm
    public ArrayList<DiaDiemDTO> getDstheoTenDiaDiem(String tenDiaDiem){
        ArrayList<DiaDiemDTO> ds = new ArrayList<>();
        String sql = "Select * from DiaDiem where TenDiaDiem like ?";
        try(Connection conn = _MyConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setNString(1, "%" + tenDiaDiem + "%"); // Dùng setNString để hỗ trợ tìm kiếm Tiếng Việt có dấu
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                DiaDiemDTO dd = maptoDiaDiem(rs);
                ds.add(dd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
}