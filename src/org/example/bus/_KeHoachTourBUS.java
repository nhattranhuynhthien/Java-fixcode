package org.example.bus;

import org.example.dao._KeHoachTourDAO;
import org.example.dto._KeHoachTourDTO;

import javax.swing.*;
import java.util.ArrayList;

public class _KeHoachTourBUS {
    public static ArrayList<_KeHoachTourDTO> lsKeHoachTour;
    private _KeHoachTourDAO keHoachTourDAO;

    public _KeHoachTourBUS(){
        keHoachTourDAO = new _KeHoachTourDAO();
        if(lsKeHoachTour == null) {
            lsKeHoachTour = keHoachTourDAO.getAllKeHoachTours();
        }
    }

    public void docDs() {
        lsKeHoachTour = keHoachTourDAO.getAllKeHoachTours();
    }

    public ArrayList<_KeHoachTourDTO> getAllKeHoachTours(){
        if(lsKeHoachTour == null) {
            lsKeHoachTour = keHoachTourDAO.getAllKeHoachTours();
        }
        return lsKeHoachTour;
    }

    public ArrayList<_KeHoachTourDTO> getAllKeHoachToursByID(String maTour){
        if (lsKeHoachTour == null) getAllKeHoachTours();
        ArrayList<_KeHoachTourDTO> lsKeHoachToursID = new ArrayList<>();

        for(_KeHoachTourDTO kt : lsKeHoachTour){
            if(kt.getMaTour().trim().equalsIgnoreCase(maTour)){
                lsKeHoachToursID.add(kt);
            }
        }
        return lsKeHoachToursID;
    }

    public boolean addKeHoachTour(_KeHoachTourDTO t){
        if(t == null) return false;
        if(t.getNgayKhoiHanh() == null || t.getNgayKetThuc() == null) {
            JOptionPane.showMessageDialog(null, "Ngày không được để trống");
            return false;
        }
        if(t.getNgayKetThuc().isBefore(t.getNgayKhoiHanh())) {
            JOptionPane.showMessageDialog(null, "Ngày kết thúc phải sau ngày khởi hành");
            return false;
        }
        if (!checkLichTrinhNV(t)) {
            JOptionPane.showMessageDialog(null, "Nhân viên hướng dẫn này đã có lịch trong khoảng thời gian này!");
            return false;
        }

        boolean success = keHoachTourDAO.addKeHoachTour(t);

        if(success)
            lsKeHoachTour.add(t);
        return success;
    }

    public String validateKeHoachTour(_KeHoachTourDTO t){
        if(t == null) return "Dữ liệu không hợp lệ";
        if(t.getNgayKhoiHanh() == null || t.getNgayKetThuc() == null) return "Ngày không được để trống";
        if(t.getNgayKetThuc().isBefore(t.getNgayKhoiHanh())) return "Ngày kết thúc phải sau ngày khởi hành";
        if(t.getTongChi() < 0) return "Tổng chi không hợp lệ";
        if(t.getTongThu() < 0) return "Tổng thu không hợp lệ";
        if(t.getTongSoVe() < 0) return "Tổng số vé không hợp lệ";
        return null;
    }

    public boolean checkLichTrinhNV(_KeHoachTourDTO t) {
        if (lsKeHoachTour == null) getAllKeHoachTours();
        for (_KeHoachTourDTO kt : lsKeHoachTour) {
            if (t.getMaKHTour() != null && t.getMaKHTour().equals(kt.getMaKHTour())) continue;
            if (kt.getMaNVHD() != null && kt.getMaNVHD().equals(t.getMaNVHD())) {
                if (!t.getNgayKetThuc().isBefore(kt.getNgayKhoiHanh()) && !t.getNgayKhoiHanh().isAfter(kt.getNgayKetThuc())) {
                    return false; // Overlap found
                }
            }
        }
        return true;
    }

    public boolean editKeHoachTour(_KeHoachTourDTO t){
        if(t == null) return false;
        if(t.getNgayKhoiHanh() == null || t.getNgayKetThuc() == null) {
            JOptionPane.showMessageDialog(null, "Ngày không được để trống");
            return false;
        }
        if(t.getNgayKetThuc().isBefore(t.getNgayKhoiHanh())) {
            JOptionPane.showMessageDialog(null, "Ngày kết thúc phải sau ngày khởi hành");
            return false;
        }
        if (!checkLichTrinhNV(t)) {
            JOptionPane.showMessageDialog(null, "Nhân viên hướng dẫn này đã có lịch trong khoảng thời gian này!");
            return false;
        }

        boolean success = keHoachTourDAO.editKeHoachTour(t);
        if(success) {
            for (int i = 0; i < lsKeHoachTour.size(); i++) {
                if (lsKeHoachTour.get(i).getMaKHTour().equals(t.getMaKHTour())) {
                    lsKeHoachTour.set(i, t);
                    break;
                }
            }
        }
        return success;
    }

    public boolean removeKeHoachTour(String makhtour){
        
        HoaDonBUS hdBus = new HoaDonBUS();
        for (org.example.dto.HoaDonDTO hd : hdBus.docDS()) {
            if (hd.getMaKHTour().equalsIgnoreCase(makhtour)) {
                JOptionPane.showMessageDialog(null, "Lỗi: Kế hoạch tour này đã có khách hàng đặt vé (Đã lập hóa đơn).\nKHÔNG THỂ XÓA XÓA TRỰC TIẾP!\nBạn chỉ có thể đổi trạng thái hoặc chờ hoàn tất tour.", "Cảnh báo bảo mật", JOptionPane.WARNING_MESSAGE);
                return false; 
            }
        }

        boolean success = keHoachTourDAO.removeKeHoachTour(makhtour);
        if(success) {
            lsKeHoachTour.removeIf(kt -> kt.getMaKHTour().equals(makhtour));
        }
        return success;
    }

    public _KeHoachTourDTO getById(String maKHTour){
        if (lsKeHoachTour == null) getAllKeHoachTours();

        for (_KeHoachTourDTO kt : lsKeHoachTour){
            if(kt.getMaKHTour().trim().equalsIgnoreCase(maKHTour)) {
                return kt;
            }
        }
        return null;
    }

    public boolean existedKeHoachTourWithID(String maKHTour){
        if (lsKeHoachTour == null) getAllKeHoachTours();
        for (_KeHoachTourDTO kt : lsKeHoachTour){
            if(kt.getMaKHTour().trim().equalsIgnoreCase(maKHTour))
                return true;
        }
        return false;
    }
    public void loadDSKHTour() {
        
        if (lsKeHoachTour != null) {
            lsKeHoachTour.clear();
        }
        lsKeHoachTour = keHoachTourDAO.getAllKeHoachTours(); 
    }

    public String taoMaKHTour() {
        if (lsKeHoachTour == null || lsKeHoachTour.isEmpty()) {
            return "KHT01";
        }
        int max = 0;
        for (_KeHoachTourDTO kht : lsKeHoachTour) {
            String ma = kht.getMaKHTour();
            if (ma.startsWith("KHT")) {
                try {
                    int num = Integer.parseInt(ma.substring(3));
                    if (num > max) max = num;
                } catch (Exception e) {}
            }
        }
        return "KHT" + String.format("%02d", max + 1);
    }
}