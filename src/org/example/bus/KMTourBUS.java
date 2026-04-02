package org.example.bus;
import org.example.dto.*;
import org.example.dao.*;

import java.util.*;

public class KMTourBUS {
    private ArrayList<KMTourDTO> dsKMTour;
    public static KMTourDAO dao;
    public KMTourBUS() {
        if (dsKMTour == null) {
            
            
            dsKMTour = dao.getDsKMTour();
        }
    }

    public ArrayList<KMTourDTO> getDsKMTour() {
        return dsKMTour;
    }

    public void setDsKMTour(ArrayList<KMTourDTO> dsKMTour) {
        this.dsKMTour = dsKMTour;
    }

    public KMTourDTO timKMTour(String maKM) {
        for (KMTourDTO kmTour : dsKMTour) {
            if (kmTour.getMaKM().equals(maKM)) {
                return kmTour;
            }
        }
        return null;
    }

    public boolean timKMTour(KMTourDTO kmTour) {
        for (KMTourDTO km : dsKMTour) {
            if (km.getMaKM().equals(kmTour.getMaKM())) {
                return true;
            }
        }
        if(dao.timKMTour(kmTour.getMaKM()) != null) {
            return true; 
        }
        return false;
    }

    public boolean themKMTour(KMTourDTO kmTour) {
        if (timKMTour(kmTour)) {
            return false; 
        }
        dsKMTour.add(kmTour);
        return true;
    }

    public boolean xoaKMTour(String maKM) {
        KMTourDTO kmTour = timKMTour(maKM);
        if (kmTour != null) {
            dsKMTour.remove(kmTour);
            return true; 
        }
        if(dao.timKMTour(maKM) != null) {
            
            dao.xoaKMTour(maKM);
            return true; 
        }   
        return false; 
    }

    public boolean suaKMTour(KMTourDTO kmTour) {
        for (int i = 0; i < dsKMTour.size(); i++) {
            if (dsKMTour.get(i).getMaKM().equals(kmTour.getMaKM())) {
                dsKMTour.set(i, kmTour);
                return true; 
            }
        }
        if(dao.timKMTour(kmTour.getMaKM()) != null) {
            
            dao.suaKMTour(kmTour);
            return true; 
        }   
        return false; 
    }

}   
