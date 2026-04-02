package org.example.bus;

import java.util.ArrayList;
import org.example.dao.NhanVienDAO;
import org.example.dto.NhanVienDTO;
import java.util.List;

public class NhanVienBUS {
    public static ArrayList<NhanVienDTO> dsNV;
    public static NhanVienDAO dataNV = new NhanVienDAO();

    public NhanVienBUS() {
        if (dsNV == null) {
            docDSNV();
        }
    }

    public void docDSNV() {
        dsNV = dataNV.layDanhSachNV();
        if (dsNV == null) {
            dsNV = new ArrayList<>();
        }
    }

    
    public static ArrayList<NhanVienDTO> getDsNV() {
        if (dsNV == null) {
            dsNV = dataNV.layDanhSachNV();
            if (dsNV == null) dsNV = new ArrayList<>();
        }
        return dsNV;
    }

    public void them(NhanVienDTO nv) {
        if (nv == null || nv.getMaNV() == null || nv.getMaNV().isEmpty()) {
            return;
        }
        if (dsNV == null) docDSNV();

        for (NhanVienDTO existingNV : dsNV) {
            if (existingNV.getMaNV().equals(nv.getMaNV())) {
                return; 
            }
        }

        
        if (dataNV.themNhanVien(nv)) {
            dsNV.add(nv);
        }
    }

    public void xoaNhanVien(String maNV) {
        if (dsNV == null) docDSNV();
        if (dataNV.xoaNhanVien(maNV)) {
            dsNV.removeIf(nv -> nv.getMaNV().equals(maNV));
        }
    }

    public NhanVienDTO timNhanVienTheoMa(String maNV) {
        if (dsNV == null) docDSNV();

        for (NhanVienDTO nv : dsNV) {
            if (nv.getMaNV().equals(maNV)) {
                return nv;
            }
        }
        
        return dataNV.timNhanVienTheoMa(maNV);
    }

    public List<NhanVienDTO> timNhanVien(String type, String keyword) {
        return dataNV.timNhanVien(type, keyword);
    }

    public boolean suaNhanVien(NhanVienDTO nv) {
        if (dsNV == null) docDSNV();

        boolean success = dataNV.suaNhanVien(nv);
        if (success) {
            for (int i = 0; i < dsNV.size(); i++) {
                if (dsNV.get(i).getMaNV().equals(nv.getMaNV())) {
                    dsNV.set(i, nv);
                    break;
                }
            }
        }
        return success;
    }
}