package org.example.bus;

import org.example.dao.KhachHangDAO;
import org.example.dto.KhachHangDTO;

import java.util.ArrayList;
import java.util.List;

public class KhachHangBUS {
    public static ArrayList<KhachHangDTO> dsKH;
    public static KhachHangDAO dataKH = new KhachHangDAO();

    public KhachHangBUS() {
        if (dsKH == null) {
            docDSKH();
        }
    }

    public void docDSKH() {
        dsKH = dataKH.layDanhSachKHang();
        if (dsKH == null) {
            dsKH = new ArrayList<>();
        }
    }

    
    public static ArrayList<KhachHangDTO> getDsKH() {
        if(dsKH == null) {
            dsKH = dataKH.layDanhSachKHang();
            if(dsKH == null) dsKH = new ArrayList<>();
        }
        return dsKH;
    }

    public void them(KhachHangDTO khang) {
        if (khang == null || khang.getMaKH() == null || khang.getMaKH().isEmpty()) {
            return;
        }
        if (dsKH == null) docDSKH();

        for (KhachHangDTO existingKH : dsKH) {
            if (existingKH.getMaKH().equals(khang.getMaKH())) {
                return;
            }
        }

        if(dataKH.themKhachHang(khang)) {
            dsKH.add(khang);
        }
    }

    public void xoaKhachHang(String maKH) {
        if (dsKH == null) docDSKH();
        if(dataKH.xoaKhachHang(maKH)) {
            dsKH.removeIf(kh -> kh.getMaKH().equals(maKH));
        }
    }

    public KhachHangDTO timKiemKH(String maKH){
        if (dsKH == null) docDSKH();

        for (KhachHangDTO kh : dsKH) {
            if (kh.getMaKH().equals(maKH)) {
                return kh;
            }
        }
        return dataKH.timKhachHangTheoMa(maKH);
    }

    public List<KhachHangDTO> timKhachHang(String column, String keyword) {
        return dataKH.timKhachHang(column, keyword);
    }

    public boolean suaKhachHang(KhachHangDTO khang) {
        if (dsKH == null) docDSKH();

        boolean success = dataKH.suaKhachHang(khang);
        if (success) {
            for (int i = 0; i < dsKH.size(); i++) {
                if (dsKH.get(i).getMaKH().equals(khang.getMaKH())) {
                    dsKH.set(i, khang);
                    break;
                }
            }
        }
        return success;
    }
}