package org.example.bus;

import org.example.dao.HoaDonDAO;
import org.example.dto.HoaDonDTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class HoaDonBUS {
    public static ArrayList<HoaDonDTO> ds;
    public static HoaDonDAO dao = new HoaDonDAO();

    public HoaDonBUS(){
        if(ds == null)
            ds = dao.getDsHoaDon();
    }

    public void docDs(){
        ds = dao.getDsHoaDon();
    }

    public static ArrayList<HoaDonDTO> getDs(){
        if (ds == null)
            ds = dao.getDsHoaDon();
        return ds;
    }

    public boolean timHd(HoaDonDTO h){
        if(dao.timHoaDon(h.getMaHD()) != null){
            for(HoaDonDTO hd: ds){
                if(hd.getMaHD().equals(h.getMaHD())){
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<HoaDonDTO> docDS(){
        if(ds == null) {
            ds = new ArrayList<HoaDonDTO>();
            ds = dao.getDsHoaDon();
        }
        return ds;
    }

    public HoaDonDTO timHd(String mahd){
        if(dao.timHoaDon(mahd) != null){
            for(HoaDonDTO hd: ds){
                if(hd.getMaHD().equals(mahd)){
                    return dao.timHoaDon(mahd);
                }
            }
        }
        return null;
    }

    public boolean themHoaDon(HoaDonDTO hd){
        if(timHd(hd)){
            return false;
        }

        boolean kq = dao.themHoaDon(hd);
        if(kq){
            ds.add(hd);
            return true;
        }
        return false;
    }

    public boolean xoaHoaDon(String mahd){
        HoaDonDTO hd = dao.timHoaDon(mahd);
        boolean b = timHd(hd);
        if(hd == null){
            return false;
        }

        if(!b){
            return false;
        }
        dao.xoaHd(hd);
        ds.remove(hd);
        return true;
    }

    public boolean suaHoaDon(HoaDonDTO hd){
        if(!timHd(hd)){
            return false;
        }
        if(dao.timHoaDon(hd.getMaHD()) != null){
            boolean kt = dao.suaHd(hd);

            if(kt){
                for(int i=0; i < ds.size(); i++){
                    if(hd.getMaHD().equals(ds.get(i).getMaHD())){
                        ds.set(i, hd);
                    }
                }
            }else return false;
            return true;
        }
        return false;
    }

    public ArrayList<HoaDonDTO> timNangcao(String loai, String key){
        if(key.trim().isEmpty()){
            return getDs();
        }
        String tencot = "";
        if(loai.equals("Mã hóa đơn")){
            tencot = "mahd";
        }
        else if(loai.equals("Mã kế hoạch tour")){
            tencot = "makhtour";
        }
        else if(loai.equals("Mã khách hàng đặt")){
            tencot = "makhangdat";
        }else if(loai.equals("Mã nhân viên")){
            tencot = "manv";
        }
        return dao.timNangcao(tencot, key);
    }

    public ArrayList<HoaDonDTO> getHDtheongay(Date ngay){
        if(ngay == null){
            return ds;
        }
        else{
            return dao.getHdtheoNgay(ngay);
        }
    }

    public int getTongchi(LocalDate tungay, LocalDate denngay){
        if(tungay == null || denngay == null){
            return 0;
        }
        if(tungay.isAfter(denngay) || denngay.isBefore(tungay)){
            return 0;
        }
        return dao.getTongChi(tungay, denngay);
    }

    public int getTongThu(LocalDate tungay, LocalDate denngay){
        if(tungay == null || denngay == null){
            return 0;
        }
        if(tungay.isAfter(denngay) || denngay.isBefore(tungay)){
            return 0;
        }
        return dao.getTongThu(tungay, denngay);
    }

    public int[] getTongThuTungThang(int nam){
        if(nam < 2000){
            return new int[12];
        }
        return dao.getTongThuTungThang(nam);
    }

    public int[] getTongChiTungThang(int nam){
        if(nam < 2000){
            return new int[12];
        }
        return dao.getTongChiTungThang(nam);
    }

    // ====== LOGIC MỚI: Tính tổng doanh thu theo khách hàng ======
    public HashMap<String, Float> getThongKeDoanhThuTheoKhachHang(LocalDate tuNgay, LocalDate denNgay) {
        HashMap<String, Float> doanhThuKH = new HashMap<>();
        ArrayList<HoaDonDTO> danhSach = getDs();
        if(danhSach == null) return doanhThuKH;

        for (HoaDonDTO hd : danhSach) {
            LocalDate ngayHD = hd.getNgay();
            if (ngayHD != null &&
                    (ngayHD.isEqual(tuNgay) || ngayHD.isAfter(tuNgay)) &&
                    (ngayHD.isEqual(denNgay) || ngayHD.isBefore(denNgay))) {

                String maKH = hd.getMaKHDat();
                float tienHienTai = doanhThuKH.getOrDefault(maKH, 0f);
                doanhThuKH.put(maKH, tienHienTai + hd.getTongTien());
            }
        }
        return doanhThuKH;
    }
}