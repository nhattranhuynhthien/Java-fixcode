package org.example.bus;

import org.example.dao.*;
import org.example.dao.KMHDDAO;
import org.example.dao.KMTourDAO;
import org.example.dto.CTrinhKMDTO;
import org.example.dto.KMHDDTO;
import org.example.dto.KMTourDTO;

import java.util.*;

public class CTrinhKMBUS {
    public ArrayList<CTrinhKMDTO> dsCTrinhKM;
    private CTrinhKMDAO dao;
    public CTrinhKMBUS() {
        if (dsCTrinhKM == null) {
            dao = new CTrinhKMDAO();
            
            dsCTrinhKM = dao.getDsCTrinhKM();
        }
    }

    public ArrayList<CTrinhKMDTO> getDsCTrinhKM() {
        return dsCTrinhKM;
    }

    public void setDsCTrinhKM(ArrayList<CTrinhKMDTO> dsCTrinhKM) {
        this.dsCTrinhKM = dsCTrinhKM;
    }

    public boolean timCTrinhKM(CTrinhKMDTO ct) {
        for (CTrinhKMDTO c : dsCTrinhKM) {
            if (c.getMaKM().equals(ct.getMaKM())) {
                return true;
            }
        }
        return false;
    }

    public boolean themCTrinhKM(CTrinhKMDTO ct) {
        
        if (dao.timCTrinhKM(ct.getMaKM()) != null) return false;

        
        boolean resultCha = dao.themCTrinhKM(ct);
        if (!resultCha) return false;

        boolean resultCon = false;
        try {
            if (ct instanceof KMTourDTO) {
                KMTourDAO daoTour = new KMTourDAO();
                resultCon = daoTour.themKMTour((KMTourDTO) ct);
            } else if (ct instanceof KMHDDTO) {
                KMHDDAO daoKmhd = new KMHDDAO();
                resultCon = daoKmhd.themKMHD((KMHDDTO) ct);
            } else {
                
                resultCon = true;
            }

            
            if (!resultCon) {
                dao.xoaCTrinhKM(ct.getMaKM());
                return false;
            }
        } catch (Exception e) {
            
            dao.xoaCTrinhKM(ct.getMaKM());
            e.printStackTrace();
            return false;
        }

        
        dsCTrinhKM.add(ct);
        return true;
    }

    public CTrinhKMDTO getFullCTrinhKM(String maKM) {
        CTrinhKMDTO basic = dao.timCTrinhKM(maKM);
        if (basic == null) return null;
        if (basic.getHinhThucKM()) {
            KMHDDAO daoHD = new KMHDDAO();
            return daoHD.timKMHD(maKM);   
        } else {
            KMTourDAO daoTour = new KMTourDAO();
            return daoTour.timKMTour(maKM);
        }
    }

    public boolean xoaCTrinhKM(String maKM) {
        CTrinhKMDTO ct = timCTrinhKM(maKM);
        if (ct == null) return false;

        boolean result = false;
        if (ct instanceof KMTourDTO) {
            KMTourDAO daoTour = new KMTourDAO();
            result = daoTour.xoaKMTour(maKM);
        } else if (ct instanceof KMHDDTO) {
            KMHDDAO daoKmhd = new KMHDDAO();
            result = daoKmhd.xoaKMHD(maKM);
        } else {
            result = dao.xoaCTrinhKM(maKM);
        }

        if (result) {
            dsCTrinhKM.remove(ct);
        }
        return result;
    }

    public boolean suaCTrinhKM(CTrinhKMDTO ct) {
        boolean result = false;
        if (ct instanceof KMTourDTO) {
            KMTourDAO daoTour = new KMTourDAO();
            result = daoTour.suaKMTour((KMTourDTO) ct);
        } else if (ct instanceof KMHDDTO) {
            KMHDDAO daoKmhd = new KMHDDAO();
            result = daoKmhd.suaKMHD((KMHDDTO) ct);
        } else {
            result = dao.suaCTrinhKM(ct);
        }

        if (result) {
            
            for (int i = 0; i < dsCTrinhKM.size(); i++) {
                if (dsCTrinhKM.get(i).getMaKM().equals(ct.getMaKM())) {
                    dsCTrinhKM.set(i, ct);
                    break;
                }
            }
        }
        return result;
    }

    public CTrinhKMDTO timCTrinhKM(String maKM) {
        for (CTrinhKMDTO ct : dsCTrinhKM) {
            if (ct.getMaKM().equals(maKM)) {
                return ct;
            }
        }
        return null; 
    }

    public ArrayList<CTrinhKMDTO> searchCTrinhKM(String loai, String keyword) {
        ArrayList<CTrinhKMDTO> result = new ArrayList<>();
        for (CTrinhKMDTO ct : dsCTrinhKM) {
            switch (loai) {
                case "Tất cả":
                    if (ct.getMaKM().toLowerCase().contains(keyword.toLowerCase())
                            || ct.getTenKM().toLowerCase().contains(keyword.toLowerCase())) {
                        result.add(ct);
                    }
                    break;
                case "KMHD":
                    if (ct.getHinhThucKM() && (ct.getMaKM().toLowerCase().contains(keyword.toLowerCase())||ct.getTenKM().toLowerCase().contains(keyword.toLowerCase()))) {
                        result.add(ct);
                    }
                    break;
                case "KMTour":
                    if (!ct.getHinhThucKM() && (ct.getMaKM().toLowerCase().contains(keyword.toLowerCase())||ct.getTenKM().toLowerCase().contains(keyword.toLowerCase()))) {
                        result.add(ct);
                    }
                    break;
            }
        }
        return result;
    }

    public void docDsCTrinhKM() {
        dsCTrinhKM = dao.getDsCTrinhKM();
    }

    public void ghiDsCTrinhKM() {
        
        for (CTrinhKMDTO ct : dsCTrinhKM) {
            if (dao.timCTrinhKM(ct.getMaKM()) != null) {
                dao.suaCTrinhKM(ct); 
            } else {
                dao.themCTrinhKM(ct); 
            }
        }
    }

    public CTrinhKMDTO maptoCTrinhKM(String maKM) {
        CTrinhKMDTO ct = dao.timCTrinhKM(maKM);

        if (ct != null) {
            return ct;
        }
        return null;
    }
}
