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

    // ====== HÀM MỚI: Đồng bộ tổng tiền về Hóa đơn gốc (Lỗi 2) ======
    private void dongBoTienHoaDon(String maHD) {
        float tongTienMoi = 0;
        ArrayList<CTietHDDTO> list = getDstheoma(maHD);
        for (CTietHDDTO c : list) {
            tongTienMoi += c.getGiaVe();
        }
        HoaDonDAO hdDao = new HoaDonDAO();
        HoaDonDTO hd = hdDao.timHoaDon(maHD);
        if (hd != null) {
            hd.setTongTien((int) tongTienMoi);
            hdDao.suaHd(hd); // Cập nhật thẳng vào DB
            new HoaDonBUS().docDs(); // Ép Hóa Đơn BUS tải lại dữ liệu mới
            hdDao.dongBoDoanhThuKeHoachTour(hd.getMaKHTour()); // Kéo theo đồng bộ KHTour
        }
    }

    public boolean themCTietHd(CTietHDDTO ct){
        // ====== KIỂM TRA SỐ LƯỢNG NGƯỜI TỐI ĐA (Lỗi 1) ======
        HoaDonBUS hdBus = new HoaDonBUS();
        HoaDonDTO hd = hdBus.timHd(ct.getMaHD());
        if (hd != null) {
            ArrayList<CTietHDDTO> danhSachHienTai = getDstheoma(ct.getMaHD());
            if (danhSachHienTai.size() >= hd.getSoLuong()) {
                JOptionPane.showMessageDialog(null, "Lỗi: Hóa đơn này chỉ được phép nhập tên cho tối đa " + hd.getSoLuong() + " hành khách!\nĐể thêm người, vui lòng sửa lại số lượng bên bảng Hóa Đơn.", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return false; // Chặn thêm
            }
        }

        if(dao.themCtietHD(ct)){
            ds.add(ct);
            dongBoTienHoaDon(ct.getMaHD()); // Đồng bộ tiền lên Hóa Đơn
            return true;
        }
        return false;
    }

    public boolean xoaCtietHd(String mact,String makh){
        CTietHDDTO ct = timCt(mact, makh);
        if (ct == null) return false;

        if(dao.xoaCtietHd(mact, makh)) {
            ds.remove(ct);
            dongBoTienHoaDon(mact); // Đồng bộ trừ tiền
            return true;
        }
        return false;
    }

    public boolean suaCtiethd(CTietHDDTO ct){
        boolean flag = false;
        if(!timCtiethd(ct))
            flag = false;
        else {
            flag = true;
            for(int i=0;i<ds.size();i++){
                if(ds.get(i).getMaHD().equals(ct.getMaHD()) && ds.get(i).getMaKHDi().equals(ct.getMaKHDi())){
                    ds.set(i,ct);
                    flag=true;
                }
            }
        }
        if(dao.TimHD(ct.getMaHD())==null) {
            flag=false;
        } else {
            if (dao.suaCthd(ct)) {
                dongBoTienHoaDon(ct.getMaHD()); // Đồng bộ tiền lại nếu giá vé bị sửa đổi
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