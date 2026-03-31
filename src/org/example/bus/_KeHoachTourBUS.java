package org.example.bus;

import org.example.dao._KeHoachTourDAO;
import org.example.dto._KeHoachTourDTO;

import javax.swing.*;
import java.util.ArrayList;

public class _KeHoachTourBUS {
    private ArrayList<_KeHoachTourDTO> lsKeHoachTour;
    _KeHoachTourDAO keHoachTourDAO;

    public _KeHoachTourBUS(){
        lsKeHoachTour = new ArrayList<>();
        keHoachTourDAO = new _KeHoachTourDAO();
    }

    public ArrayList<_KeHoachTourDTO> getAllKeHoachTours(){
        lsKeHoachTour = keHoachTourDAO.getAllKeHoachTours();
        return lsKeHoachTour;
    }

    public ArrayList<_KeHoachTourDTO> getAllKeHoachToursByID(String maTour){
        lsKeHoachTour = keHoachTourDAO.getAllKeHoachTours();
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

        boolean success = keHoachTourDAO.addKeHoachTour(t);

        if(success)
            lsKeHoachTour.add(t);
        return success;
    }

    public String validateKeHoachTour(_KeHoachTourDTO t){
        if(t == null) return "Dữ liệu không hợp lệ";

        if(t.getNgayKhoiHanh() == null || t.getNgayKetThuc() == null)
            return "Ngày không được để trống";

        if(t.getNgayKetThuc().isBefore(t.getNgayKhoiHanh()))
            return "Ngày kết thúc phải sau ngày khởi hành";

        if(t.getTongChi() < 0)
            return "Tổng chi không hợp lệ";

        if(t.getTongThu() < 0)
            return "Tổng thu không hợp lệ";

        if(t.getTongSoVe() < 0)
            return "Tổng số vé không hợp lệ";

        return null;
    }

    public boolean editKeHoachTour(_KeHoachTourDTO t){
        return keHoachTourDAO.editKeHoachTour(t);
    }

    public boolean removeKeHoachTour(String matour){
        boolean success = keHoachTourDAO.removeKeHoachTour(matour);
        return success;
    }

    public _KeHoachTourDTO getById(String maKHTour){
        _KeHoachTourDTO result = new _KeHoachTourDTO();
        for (_KeHoachTourDTO kt : lsKeHoachTour){
            if(kt.getMaKHTour().trim().equalsIgnoreCase(maKHTour)) {
                result = kt;
                break;
            }
        }
        return result;
    }

    public boolean existedKeHoachTourWithID(String maKHTour){
        for (_KeHoachTourDTO kt : lsKeHoachTour){
            if(kt.getMaTour().trim().equalsIgnoreCase(maKHTour))
                return true;
        }
        return false;
    }
}
