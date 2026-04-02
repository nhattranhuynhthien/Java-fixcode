package org.example.bus;

import org.example.dao.HoaDonDAO;
import org.example.dto.HoaDonDTO;
import org.example.dto._KeHoachTourDTO;
import org.example.dto._TourDTO;
import org.example.dto._LoaiTourDTO;
import org.example.dao.KMHDDAO;
import org.example.dto.KMHDDTO;

import javax.swing.JOptionPane;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class HoaDonBUS {
    public static ArrayList<HoaDonDTO> ds;
    public static HoaDonDAO dao = new HoaDonDAO();

    private _KeHoachTourBUS khTourBUS = new _KeHoachTourBUS();
    private _TourBUS tourBUS = new _TourBUS();
    private _LoaiTourBUS loaiTourBUS = new _LoaiTourBUS();

    public HoaDonBUS(){
        if(ds == null) ds = dao.getDsHoaDon();
    }

    public void docDs(){
        ds = dao.getDsHoaDon();
    }

    public static ArrayList<HoaDonDTO> getDs(){
        if (ds == null) ds = dao.getDsHoaDon();
        return ds;
    }

    public boolean timHd(HoaDonDTO h){
        if(dao.timHoaDon(h.getMaHD()) != null){
            for(HoaDonDTO hd: ds){
                if(hd.getMaHD().equals(h.getMaHD())){ return true; }
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
                if(hd.getMaHD().equals(mahd)){ return dao.timHoaDon(mahd); }
            }
        }
        return null;
    }

    private _KeHoachTourDTO layKeHoachTourTrucTiep(String maKHTour) {
        ArrayList<_KeHoachTourDTO> dsTour = khTourBUS.getAllKeHoachTours();
        for (_KeHoachTourDTO t : dsTour) {
            if (t.getMaKHTour().equalsIgnoreCase(maKHTour)) { return t; }
        }
        return null;
    }

    public boolean themHoaDon(HoaDonDTO hd){
        if(timHd(hd)) return false;

        _KeHoachTourDTO kht = layKeHoachTourTrucTiep(hd.getMaKHTour());
        if (kht == null) {
            JOptionPane.showMessageDialog(null, "Lỗi: Không tìm thấy Kế Hoạch Tour tương ứng!");
            return false;
        }

        
        if (hd.getMaKM() != null && !hd.getMaKM().trim().isEmpty() && !hd.getMaKM().equalsIgnoreCase("Không có")) {
            KMHDDAO kmDao = new KMHDDAO();
            KMHDDTO km = kmDao.timKMHD(hd.getMaKM());
            if (km != null) {
                if (hd.getNgay().isBefore(km.getNgayBD()) || hd.getNgay().isAfter(km.getNgayKT())) {
                    JOptionPane.showMessageDialog(null, "Lỗi: Mã khuyến mãi '" + hd.getMaKM() + "' không hợp lệ hoặc đã hết hạn!\n(Khuyến mãi chỉ áp dụng từ " + km.getNgayBD() + " đến " + km.getNgayKT() + ")", "Lỗi mã ưu đãi", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        }

        
        _TourDTO tour = null;
        for (_TourDTO t : tourBUS.getAllTours()) {
            if (t.getMaTour().equalsIgnoreCase(kht.getMaTour())) { tour = t; break; }
        }
        if (tour != null) {
            for (_LoaiTourDTO lt : loaiTourBUS.getAllLoaiTour()) {
                if (lt.getMaLoaiTour().equalsIgnoreCase(tour.getMaLoaiTour())) {
                    if (lt.getTrangThai() == 0) {
                        JOptionPane.showMessageDialog(null, "Lỗi: Loại tour này đang bị tạm ngưng/ẩn. Không thể tạo hóa đơn đặt vé!");
                        return false;
                    }
                    break;
                }
            }
        }

        if (hd.getNgay().isAfter(kht.getNgayKhoiHanh())) {
            JOptionPane.showMessageDialog(null, "Lỗi: Tour này đã khởi hành vào ngày " + kht.getNgayKhoiHanh() + ", không thể đặt vé!");
            return false;
        }

        if (kht.getTongSoVe() < hd.getSoLuong()) {
            JOptionPane.showMessageDialog(null, "Lỗi: Kế hoạch tour không đủ số lượng vé. Chỉ còn " + kht.getTongSoVe() + " vé trống.");
            return false;
        }

        boolean kq = dao.themHoaDon(hd);
        if(kq){
            ds.add(hd);
            dao.capNhatSoluongvaTongthu(hd.getSoLuong(), hd.getMaKHTour());
            return true;
        }
        return false;
    }

    public boolean xoaHoaDon(String mahd){
        HoaDonDTO hd = dao.timHoaDon(mahd);
        if(hd == null) return false;
        if(!timHd(hd)) return false;

        boolean kq = dao.xoaHd(hd);
        if(kq){
            ds.remove(hd);
            dao.dongBoDoanhThuKeHoachTour(hd.getMaKHTour());
            return true;
        }
        return false;
    }

    public boolean suaHoaDon(HoaDonDTO hdMoi){
        if(!timHd(hdMoi)){
            return false;
        }

        HoaDonDTO hdCu = dao.timHoaDon(hdMoi.getMaHD());
        if(hdCu == null) return false;

        int chenhLechVe = hdMoi.getSoLuong() - hdCu.getSoLuong();

        _KeHoachTourDTO kht = layKeHoachTourTrucTiep(hdMoi.getMaKHTour());
        if (kht != null) {
            
            if (hdMoi.getMaKM() != null && !hdMoi.getMaKM().trim().isEmpty() && !hdMoi.getMaKM().equalsIgnoreCase("Không có")) {
                KMHDDAO kmDao = new KMHDDAO();
                KMHDDTO km = kmDao.timKMHD(hdMoi.getMaKM());
                if (km != null) {
                    if (hdMoi.getNgay().isBefore(km.getNgayBD()) || hdMoi.getNgay().isAfter(km.getNgayKT())) {
                        JOptionPane.showMessageDialog(null, "Lỗi: Mã khuyến mãi '" + hdMoi.getMaKM() + "' không hợp lệ hoặc đã hết hạn!\n(Khuyến mãi chỉ áp dụng từ " + km.getNgayBD() + " đến " + km.getNgayKT() + ")", "Lỗi mã ưu đãi", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            }

            _TourDTO tour = null;
            for (_TourDTO t : tourBUS.getAllTours()) {
                if (t.getMaTour().equalsIgnoreCase(kht.getMaTour())) { tour = t; break; }
            }
            if (tour != null) {
                for (_LoaiTourDTO lt : loaiTourBUS.getAllLoaiTour()) {
                    if (lt.getMaLoaiTour().equalsIgnoreCase(tour.getMaLoaiTour())) {
                        if (lt.getTrangThai() == 0) {
                            JOptionPane.showMessageDialog(null, "Lỗi: Loại tour này đang bị tạm ngưng/ẩn. Không thể thay đổi hóa đơn!");
                            return false;
                        }
                        break;
                    }
                }
            }

            if (hdMoi.getNgay().isAfter(kht.getNgayKhoiHanh())) {
                JOptionPane.showMessageDialog(null, "Lỗi: Tour đã khởi hành vào ngày " + kht.getNgayKhoiHanh() + ", ngày lập hóa đơn không hợp lệ!");
                return false;
            }
            if (chenhLechVe > 0 && kht.getTongSoVe() < chenhLechVe) {
                JOptionPane.showMessageDialog(null, "Lỗi: Tour không đủ vé để cấp thêm! Chỉ còn " + kht.getTongSoVe() + " vé.");
                return false;
            }
        }

        boolean kt = dao.suaHd(hdMoi);

        if(kt){
            for(int i=0; i < ds.size(); i++){
                if(hdMoi.getMaHD().equals(ds.get(i).getMaHD())){
                    ds.set(i, hdMoi);
                    break;
                }
            }
            dao.capNhatSoluongvaTongthu(chenhLechVe, hdMoi.getMaKHTour());
            return true;
        }
        return false;
    }

    public ArrayList<HoaDonDTO> timNangcao(String loai, String key){
        if(key.trim().isEmpty()){ return getDs(); }
        String tencot = "";
        if(loai.equals("Mã hóa đơn")) tencot = "mahd";
        else if(loai.equals("Mã kế hoạch tour")) tencot = "makhtour";
        else if(loai.equals("Mã khách hàng đặt")) tencot = "makhangdat";
        else if(loai.equals("Mã nhân viên")) tencot = "manv";

        return dao.timNangcao(tencot, key);
    }

    public ArrayList<HoaDonDTO> getHDtheongay(Date ngay){
        if(ngay == null) return ds;
        else return dao.getHdtheoNgay(ngay);
    }

    public int getTongchi(LocalDate tungay, LocalDate denngay){
        if(tungay == null || denngay == null) return 0;
        if(tungay.isAfter(denngay) || denngay.isBefore(tungay)) return 0;
        return dao.getTongChi(tungay, denngay);
    }

    public int getTongThu(LocalDate tungay, LocalDate denngay){
        if(tungay == null || denngay == null) return 0;
        if(tungay.isAfter(denngay) || denngay.isBefore(tungay)) return 0;
        return dao.getTongThu(tungay, denngay);
    }

    public int[] getTongThuTungThang(int nam){
        if(nam < 2000) return new int[12];
        return dao.getTongThuTungThang(nam);
    }

    public int[] getTongChiTungThang(int nam){
        if(nam < 2000) return new int[12];
        return dao.getTongChiTungThang(nam);
    }

    public ArrayList<Object[]> getThongKeDoanhThuTheoKhachHang(LocalDate tuNgay, LocalDate denNgay) {
        if (tuNgay == null || denNgay == null) return new ArrayList<>();
        return dao.thongKeDoanhThuKH(tuNgay, denNgay);
    }
}