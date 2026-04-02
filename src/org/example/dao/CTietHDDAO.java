package org.example.dao;

import java.sql.*;
import org.example.dto.CTietHDDTO;
import java.util.*;

public class CTietHDDAO {
    public CTietHDDAO() {
    }

    public ArrayList<CTietHDDTO> getDs() {
        ArrayList<CTietHDDTO> ds = new ArrayList<>();
        String sql ="Select * from CThoadon";

        try(Connection conn= _MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                CTietHDDTO ct=maptoCthd(rs);
                ds.add(ct);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public ArrayList<CTietHDDTO> getDstheoma(String mahd){
        ArrayList<CTietHDDTO> ds=new ArrayList<>();
        String sql ="Select * from CThoadon where mahd=?";
        try(Connection conn = _MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1, mahd);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                CTietHDDTO cthd=maptoCthd(rs);
                ds.add(cthd);
            }
        } catch(SQLException ex){
            ex.printStackTrace();
        }
        return ds;
    }

    public CTietHDDTO maptoCthd(ResultSet rs) throws SQLException{
        String MaHD = rs.getString("MaHD");
        String MaKHDi = rs.getString("MaKHang");
        float GiaVe = rs.getFloat("GiaVe");
        return new CTietHDDTO(MaHD, MaKHDi, GiaVe);
    }

    public CTietHDDTO TimHD(String mahd){
        String sql = "Select * from CThoadon where mahd=?";
        try(Connection conn= _MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1, mahd);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return maptoCthd(rs);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    // ================= LOGIC THÊM CHI TIẾT HÓA ĐƠN =================
    public boolean themCtietHD(CTietHDDTO ct){
        String sqlCheckCT = "Select * from cthoadon where mahd=? and makhang=?";
        String sqlUpdateCT = "Update cthoadon set giave = giave + ? where mahd=? and makhang=?";
        String sqlInsertCT = "Insert into cthoadon(mahd,makhang,giave) Values(?,?,?)";

        String sqlGetMaKHTour = "SELECT makhtour FROM hoadon WHERE mahd = ?";
        String sqlInsertKHKHTour = "INSERT INTO khang_khtour(MaKHang, MaKHTour, GiaVe) VALUES(?, ?, ?)";

        Connection conn = null;
        try {
            conn = _MyConnection.getConnection();
            conn.setAutoCommit(false);

            String maKHTour = "";
            try(PreparedStatement ps = conn.prepareStatement(sqlGetMaKHTour)){
                ps.setString(1, ct.getMaHD());
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    maKHTour = rs.getString("makhtour");
                } else {
                    return false;
                }
            }

            boolean daTonTai = false;
            try (PreparedStatement ps = conn.prepareStatement(sqlCheckCT)) {
                ps.setString(1, ct.getMaHD());
                ps.setString(2, ct.getMaKHDi());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) daTonTai = true;
            }

            if (daTonTai) {
                // Đã tồn tại -> Cho phép cộng dồn giá vé (Gộp mua nhiều vé)
                try (PreparedStatement ps = conn.prepareStatement(sqlUpdateCT)) {
                    ps.setFloat(1, ct.getGiaVe());
                    ps.setString(2, ct.getMaHD());
                    ps.setString(3, ct.getMaKHDi());
                    if (ps.executeUpdate() <= 0) { conn.rollback(); return false; }
                }
            } else {
                // Thêm mới hoàn toàn
                try (PreparedStatement ps = conn.prepareStatement(sqlInsertCT)) {
                    ps.setString(1, ct.getMaHD());
                    ps.setString(2, ct.getMaKHDi());
                    ps.setFloat(3, ct.getGiaVe());
                    if (ps.executeUpdate() <= 0) { conn.rollback(); return false; }
                }
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlInsertKHKHTour)) {
                ps.setString(1, ct.getMaKHDi());
                ps.setString(2, maKHTour);
                ps.setLong(3, (long) ct.getGiaVe());
                ps.executeUpdate();
            } catch (SQLException e) {
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try { conn.setAutoCommit(true); conn.close(); } catch (SQLException ex) {}
            }
        }
        return false;
    }

    // ================= LOGIC XÓA CHI TIẾT HÓA ĐƠN =================
    public boolean xoaCtietHd(String mahd, String makh) {
        float giaVeGoc = laygia(mahd);
        float giaHienTai = 0;

        String sqlCheck = "SELECT giave FROM cthoadon WHERE mahd=? AND makhang=?";
        String sqlGetMaKHTour = "SELECT makhtour FROM hoadon WHERE mahd = ?";
        String sqlDeleteKHKHTour = "DELETE FROM khang_khtour WHERE MaKHang = ? AND MaKHTour = ?";

        Connection conn = null;
        try {
            conn = _MyConnection.getConnection();
            conn.setAutoCommit(false);

            String maKHTour = "";
            try(PreparedStatement ps = conn.prepareStatement(sqlGetMaKHTour)){
                ps.setString(1, mahd);
                ResultSet rs = ps.executeQuery();
                if(rs.next()){
                    maKHTour = rs.getString("makhtour");
                } else return false;
            }

            try (PreparedStatement ps = conn.prepareStatement(sqlCheck)) {
                ps.setString(1, mahd);
                ps.setString(2, makh);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) giaHienTai = rs.getFloat("giave");
                else return false;
            }

            if (giaHienTai > giaVeGoc) {
                // Trừ tiền 1 vé nếu khách mua nhiều vé
                String sqlTruTienCT = "UPDATE cthoadon SET giave = giave - ? WHERE mahd=? AND makhang=?";
                try (PreparedStatement ps = conn.prepareStatement(sqlTruTienCT)) {
                    ps.setFloat(1, giaVeGoc);
                    ps.setString(2, mahd);
                    ps.setString(3, makh);
                    ps.executeUpdate();
                }
            } else {
                // Xóa hẳn nếu chỉ còn 1 vé
                String sqlDelete = "DELETE FROM cthoadon WHERE mahd=? AND makhang=?";
                try (PreparedStatement ps = conn.prepareStatement(sqlDelete)) {
                    ps.setString(1, mahd);
                    ps.setString(2, makh);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = conn.prepareStatement(sqlDeleteKHKHTour)) {
                    ps.setString(1, makh);
                    ps.setString(2, maKHTour);
                    ps.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) {}
        }
    }

    public float laygia(String mahd){
        float gia=0;
        String makht="";
        String sql1="Select makhtour from hoadon where mahd=?";
        try(Connection conn= _MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql1)){
            ps.setString(1, mahd);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                makht=rs.getString("makhtour");
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
        String sql ="Select t.dongia from kehoachtour k join tour t on k.matour = t.matour where k.makhtour=?";
        try(Connection conn= _MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1, makht);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                gia=rs.getFloat("Dongia");
            }}
        catch(SQLException e){
            e.printStackTrace();
        }
        return gia;
    }

    public ArrayList<CTietHDDTO> timNangcao(String tencot,String key){
        ArrayList<CTietHDDTO> ds =new ArrayList<>();
        String sql="Select * from cthoadon where "+tencot+" like ?";
        try(Connection conn = _MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1, "%" + key + "%");
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                CTietHDDTO ct =maptoCthd(rs);
                ds.add(ct);
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return ds;
    }

    // Hỗ trợ cập nhật đổi cả Mã Khách Hàng
    public boolean suaCthd(CTietHDDTO ctMoi, String maKhCung){
        String sql = "Update cthoadon set makhang=?, giave=? where mahd=? and makhang=?";
        try(Connection conn= _MyConnection.getConnection();
            PreparedStatement ps=conn.prepareStatement(sql)){
            ps.setString(1, ctMoi.getMaKHDi());
            ps.setFloat(2, ctMoi.getGiaVe());
            ps.setString(3, ctMoi.getMaHD());
            ps.setString(4, maKhCung);

            return ps.executeUpdate() > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}