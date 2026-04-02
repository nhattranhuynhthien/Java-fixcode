package org.example.bus;
import org.example.dto.*;
import org.example.dao.*;

import java.util.*;

public class KMHDBUS {
    public ArrayList<KMHDDTO> dsKMHD;
    public static KMHDDAO dao;   

    public KMHDBUS() {
        if (dsKMHD == null) {
            
            
            dsKMHD = dao.getDsKMHD();
        }
    }

    public ArrayList<KMHDDTO> getDsKMHD() {
        return dsKMHD;
    }

    public void setDsKMHD(ArrayList<KMHDDTO> dsKMHD) {
        this.dsKMHD = dsKMHD;
    }

    public boolean timKMHD(KMHDDTO kmhd) {
        for (KMHDDTO km : dsKMHD) {
            if (km.getMaKM().equals(kmhd.getMaKM())) {
                return true;
            }
        }
        return false;
    }

    public boolean themKMHD(KMHDDTO kmhd) {
        if (timKMHD(kmhd)) {
            return false; 
        }
        if (dao.timKMHD(kmhd.getMaKM()) != null) {
            return false; 
        }
        dsKMHD.add(kmhd);
        return true;
    }

    public boolean xoaKMHD(String maKM) {
        KMHDDTO kmhd = null;
        for (KMHDDTO km : dsKMHD) {
            if (km.getMaKM().equals(maKM)) {
                kmhd = km;
                break;
            }
        }
        if (kmhd != null) {
            dsKMHD.remove(kmhd);
            return true;
        }
        if(dao.timKMHD(maKM) != null) {
            dao.xoaKMHD(maKM);
            return true; 
        }
        return false; 
    }

    public boolean suaKMHD(KMHDDTO kmhd) {
        for (int i = 0; i < dsKMHD.size(); i++) {
            if (dsKMHD.get(i).getMaKM().equals(kmhd.getMaKM())) {
                dsKMHD.set(i, kmhd);
                return true; 
            }
        }
        if(dao.timKMHD(kmhd.getMaKM()) != null) {
            dao.suaKMHD(kmhd);
            return true; 
        }
        return false; 
    }

}