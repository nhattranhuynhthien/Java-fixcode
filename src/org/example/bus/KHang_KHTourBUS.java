package org.example.bus;
import org.example.dao.KHang_KHTourDAO;
import org.example.dto.KHang_KHTourDTO;

import java.util.ArrayList;
import java.util.List;

public class KHang_KHTourBUS {
    static ArrayList<KHang_KHTourDTO> dsKHKHTour;
    static KHang_KHTourDAO dataKHKHTour = new KHang_KHTourDAO();
    public KHang_KHTourBUS() {}
    public void docDSKHKHTour() {
        if (dsKHKHTour == null) {
            dsKHKHTour = new ArrayList<KHang_KHTourDTO>();
        }
        dsKHKHTour = dataKHKHTour.layDanhSachKHang_KHTour();
    }
    public void them(KHang_KHTourDTO kht) {
        try{
            if (dsKHKHTour == null) {
                dsKHKHTour = new ArrayList<KHang_KHTourDTO>();
            }
            if (kht == null) {
                return;
            }
            if (kht.getMaKHTour() == null || kht.getMaKHTour().isEmpty()) {
                return;
            }
            for (KHang_KHTourDTO existingKHT : dsKHKHTour) {
                if (existingKHT.getMaKHTour().equals(kht.getMaKHTour())) {
                    return;
                }
            }
            dataKHKHTour.themKHang_KHTour(kht);
            if (dsKHKHTour != null) {
                dsKHKHTour.add(kht);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void xoaKHang_KHTour(String maKHTour, String maKHang) {
        try {
            if (dsKHKHTour == null) {
                return;
            }
            dataKHKHTour.xoaKHang_KHTour(maKHTour, maKHang);
            dsKHKHTour.removeIf(kht -> kht.getMaKHTour().equals(maKHTour) && kht.getMaKHang().equals(maKHang));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean suaKHang_KHTour(KHang_KHTourDTO kht) {
        try {
            if (dsKHKHTour == null || kht == null || kht.getMaKHTour() == null || kht.getMaKHTour().isEmpty()) {
                return false;
            }
            for (int i = 0; i < dsKHKHTour.size(); i++) {
                KHang_KHTourDTO existingKHT = dsKHKHTour.get(i);
                if (existingKHT.getMaKHTour().equals(kht.getMaKHTour()) && existingKHT.getMaKHang().equals(kht.getMaKHang())) {
                    dataKHKHTour.capNhatKHang_KHTour(kht);
                    dsKHKHTour.set(i, kht);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public KHang_KHTourDTO timKiemKHang_KHTourTheoMaKHTour(String maKHTour) {
        try {
            if (dsKHKHTour == null || maKHTour == null) {
                return null;
            }
            for (KHang_KHTourDTO kht : dsKHKHTour) {
                if (kht.getMaKHTour().equals(maKHTour)) {
                    return kht;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<KHang_KHTourDTO> timKHang_KHTours(String column, String value) {
        try {
            if (dsKHKHTour == null || column == null || value == null) {
                return new ArrayList<>();
            }
            List<KHang_KHTourDTO> result = new ArrayList<>();
            for (KHang_KHTourDTO kht : dsKHKHTour) {
                switch (column) {
                    case "MaKHTour":
                        if (kht.getMaKHTour().equalsIgnoreCase(value)) {
                            result.add(kht);
                        }
                        break;
                    case "MaKHang":
                        if (kht.getMaKHang().equalsIgnoreCase(value)) {
                            result.add(kht);
                        }
                        break;
                    case "GiaVe":
                        try {
                            long giaVeValue = Long.parseLong(value);
                            if (kht.getGiaVe() == giaVeValue) {
                                result.add(kht);
                            }
                        } catch (NumberFormatException e) {
                            // Ignore invalid number format
                        }
                        break;
                    default:
                        // Invalid column name
                        return new ArrayList<>();
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<KHang_KHTourDTO> timKHang_KHToursTheoHo(String ho) {
        try {
            if (dsKHKHTour == null || ho == null) {
                return new ArrayList<>();
            }
            List<KHang_KHTourDTO> result = new ArrayList<>();
            for (KHang_KHTourDTO kht : dsKHKHTour) {
                String maKHang = kht.getMaKHang();
                if (maKHang != null && maKHang.startsWith(ho)) {
                    result.add(kht);
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public KHang_KHTourDTO timKHang_KHTourTheoTen(String ten) {
        try {
            if (dsKHKHTour == null || ten == null) {
                return null;
            }
            for (KHang_KHTourDTO kht : dsKHKHTour) {
                String maKHang = kht.getMaKHang();
                if (maKHang != null && maKHang.equalsIgnoreCase(ten)) {
                    return kht;
                }
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
