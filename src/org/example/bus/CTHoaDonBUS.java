package org.example.bus;
import org.example.dao.CTietHDDAO;
import org.example.dao._KeHoachTourDAO;
import org.example.dao.HoaDonDAO;
import org.example.dto.CTietHDDTO;
import org.example.dto.HoaDonDTO;

import javax.swing.JOptionPane;
import java.util.ArrayList;

public class CTHoaDonBUS {
    public static ArrayList<CTietHDDTO> ds;
    public static CTietHDDAO dao;
    public static _KeHoachTourDAO khtdao;

    public CTHoaDonBUS(){
        if(ds==null){
            dao=new CTietHDDAO();
            khtdao=new _KeHoachTourDAO();
            ds=dao.getDs();
        }
    }

    public void docDs(){
        ds=dao.getDs();
    }

    public static ArrayList<CTietHDDTO> getDs(){
        return ds;
    }

    public boolean timCtiethd(CTietHDDTO ct){
        for(CTietHDDTO cthd:ds){
            if(ct.getMaHD().equals(cthd.getMaHD()) && ct.getMaKHDi().equals(cthd.getMaKHDi()))
                return true;
        }
        return false;
    }

    public CTietHDDTO timCt(String mact, String makh){
        for(CTietHDDTO cthd:ds)
            if(cthd.getMaHD().equals(mact) && cthd.getMaKHDi().equals(makh))
                return cthd;
        return null;
    }

    private void dongBoTienHoaDon(String maHD) {
        float tongTienMoi = 0;
        ArrayList<CTietHDDTO> list = getDstheoma(maHD);
        for (CTietHDDTO c : list) {
            tongTienMoi += c.getGiaVe();
        }
        HoaDonDAO hdDao = new HoaDonDAO();
        HoaDonDTO hd = hdDao.timHoaDon(maHD);
        if (hd != null) {
            String maKM = hd.getMaKM();
            if (maKM != null && !maKM.isEmpty()) {
                CTrinhKMBUS kmBus = new CTrinhKMBUS();
                org.example.dto.CTrinhKMDTO km = kmBus.getFullCTrinhKM(maKM);
                if (km != null) {
                    tongTienMoi = tongTienMoi - (tongTienMoi * km.getChietKhau() / 100);
                }
            }
            hd.setTongTien((int) tongTienMoi);
            hdDao.suaHd(hd);
            new HoaDonBUS().docDs();
            hdDao.dongBoDoanhThuKeHoachTour(hd.getMaKHTour());
        }
    }

    public boolean themCTietHd(CTietHDDTO ct){
        HoaDonBUS hdBus = new HoaDonBUS();
        HoaDonDTO hd = hdBus.timHd(ct.getMaHD());
        float giaVeGoc = layGia(ct.getMaHD());

        // KIỂM TRA SỐ LƯỢNG NGƯỜI QUA TỔNG TIỀN (Cho phép mua nhiều vé/người)
        if (hd != null && giaVeGoc > 0) {
            int tongSoVeHienTai = 0;
            ArrayList<CTietHDDTO> danhSachHienTai = getDstheoma(ct.getMaHD());
            for (CTietHDDTO c : danhSachHienTai) {
                tongSoVeHienTai += Math.round(c.getGiaVe() / giaVeGoc);
            }

            if (tongSoVeHienTai >= hd.getSoLuong()) {
                JOptionPane.showMessageDialog(null, "Lỗi: Hóa đơn này đã đủ " + hd.getSoLuong() + " vé!\nKhông thể thêm hành khách.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        }

        if(dao.themCtietHD(ct)){
            boolean isUpdated = false;
            for(CTietHDDTO c : ds){
                if(c.getMaHD().equals(ct.getMaHD()) && c.getMaKHDi().equals(ct.getMaKHDi())){
                    c.setGiaVe(c.getGiaVe() + ct.getGiaVe()); // Cập nhật cộng dồn trên RAM
                    isUpdated = true;
                    break;
                }
            }
            if(!isUpdated){
                ds.add(ct);
            }
            dongBoTienHoaDon(ct.getMaHD());
            return true;
        }
        return false;
    }

    public boolean xoaCtietHd(String mact,String makh){
        CTietHDDTO ct = timCt(mact, makh);
        if (ct == null) return false;

        float giaVeGoc = layGia(mact);

        if(dao.xoaCtietHd(mact, makh)) {
            if (ct.getGiaVe() > giaVeGoc) {
                ct.setGiaVe(ct.getGiaVe() - giaVeGoc); // Giảm trừ tiền từ từ trên RAM
            } else {
                ds.remove(ct); // Tiền đã cạn -> Xóa
            }
            dongBoTienHoaDon(mact);
            return true;
        }
        return false;
    }

    public boolean suaCtiethd(CTietHDDTO ct, String maKhCung){
        boolean flag = false;

        for(int i = 0; i < ds.size(); i++){
            if(ds.get(i).getMaHD().equals(ct.getMaHD()) && ds.get(i).getMaKHDi().equals(maKhCung)){
                ds.set(i, ct);
                flag = true;
                break;
            }
        }

        if(dao.TimHD(ct.getMaHD()) == null) {
            flag = false;
        } else {
            if (dao.suaCthd(ct, maKhCung)) {
                dongBoTienHoaDon(ct.getMaHD());
            } else {
                flag = false;
            }
        }
        return flag;
    }

    public ArrayList<CTietHDDTO> getDstheoma(String mahd){
        CTietHDDAO dao = new CTietHDDAO();
        return dao.getDstheoma(mahd);
    }

    public float layGia(String mahd){
        CTietHDDAO daoCTietHD=new CTietHDDAO();
        return daoCTietHD.laygia(mahd);
    }

    public ArrayList<CTietHDDTO> timNangcao(String loai, String key){
        ArrayList<CTietHDDTO> ds =new ArrayList<>();
        String tencot="";
        if(loai.equals("Mã hóa đơn")){
            tencot="mahd";
        }
        else if(loai.equals("Mã khách hàng")){
            tencot="makhang";
        }else{
            return null;
        }
        return dao.timNangcao(tencot, key);
    }

    public boolean capNhatSoluong(int sl, String makhtour){
        if(makhtour.isEmpty()) return false;
        if(sl<=0) {
            return false;
        }else{
            return khtdao.capNhatSoluong(sl, makhtour);
        }
    }
}