package org.example.bus;

import org.example.dao._CTietKHTourDAO;
import org.example.dto._CTietKHTourDTO;

import java.util.ArrayList;

public class _CTietKHTourBUS {
    public static ArrayList<_CTietKHTourDTO> lsCTietKHTours; 
    private _CTietKHTourDAO cTietKHTourDAO;

    public _CTietKHTourBUS(){
        cTietKHTourDAO = new _CTietKHTourDAO();
        if(lsCTietKHTours == null) {
            lsCTietKHTours = cTietKHTourDAO.getAllCTietKHTours();
        }
    }

    public void docDs() {
        lsCTietKHTours = cTietKHTourDAO.getAllCTietKHTours();
    }

    public ArrayList<_CTietKHTourDTO> getAllCTietKHTours(){
        if(lsCTietKHTours == null) {
            lsCTietKHTours = cTietKHTourDAO.getAllCTietKHTours();
        }
        return lsCTietKHTours;
    }

    public boolean addCTietKHTour(_CTietKHTourDTO t){
        if(t == null) return false;

        boolean success = cTietKHTourDAO.addCTietKHTour(t);
        if(success) {
            lsCTietKHTours.add(t);

            
            new _KeHoachTourBUS().docDs();
        }

        return success;
    }

    public boolean editCTietKHTour(_CTietKHTourDTO t){
        boolean success = cTietKHTourDAO.editCTietKHTour(t);
        if(success) {
            for (int i = 0; i < lsCTietKHTours.size(); i++) {
                if (lsCTietKHTours.get(i).getMaCTietKHTour().equals(t.getMaCTietKHTour())) {
                    lsCTietKHTours.set(i, t);
                    break;
                }
            }
            
            new _KeHoachTourBUS().docDs();
        }
        return success;
    }

    public boolean removeCTietKHTour(String maCTietKHTour){
        boolean success = cTietKHTourDAO.removeCTietKHTour(maCTietKHTour);
        if(success) {
            lsCTietKHTours.removeIf(ct -> ct.getMaCTietKHTour().equals(maCTietKHTour));

            
            new _KeHoachTourBUS().docDs();
        }
        return success;
    }

    public _CTietKHTourDTO getCTietKHTourById(String maCTietKHTour){
        if(lsCTietKHTours == null) getAllCTietKHTours();

        for (_CTietKHTourDTO ct : lsCTietKHTours){
            
            if(ct.getMaCTietKHTour().trim().equalsIgnoreCase(maCTietKHTour)) {
                return ct;
            }
        }
        return null;
    }

    public ArrayList<_CTietKHTourDTO> getLsCTietKHToursById(String maKHTour){
        if(lsCTietKHTours == null) getAllCTietKHTours();
        ArrayList<_CTietKHTourDTO> result = new ArrayList<>();

        for (_CTietKHTourDTO ct : lsCTietKHTours){
            if(ct.getMaKHTour().trim().equalsIgnoreCase(maKHTour)) {
                result.add(ct);
            }
        }
        return result;
    }

    public boolean existedCTietKHTourWithID(String maCTKHTour){
        if(lsCTietKHTours == null) getAllCTietKHTours();

        for (_CTietKHTourDTO ct : lsCTietKHTours){
            
            if(ct.getMaCTietKHTour().trim().equalsIgnoreCase(maCTKHTour))
                return true;
        }
        return false;
    }
}