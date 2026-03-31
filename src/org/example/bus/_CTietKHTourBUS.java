package org.example.bus;

import org.example.dao._CTietKHTourDAO;
import org.example.dto._CTietKHTourDTO;

import java.util.ArrayList;

public class _CTietKHTourBUS {
    private ArrayList<_CTietKHTourDTO> lsCTietKHTours;
    private _CTietKHTourDAO cTietKHTourDAO;

    //constructor
    public _CTietKHTourBUS(){
        cTietKHTourDAO = new _CTietKHTourDAO();
        lsCTietKHTours = new ArrayList<>();
    }

    public ArrayList<_CTietKHTourDTO> getAllCTietKHTours(){
        lsCTietKHTours = cTietKHTourDAO.getAllCTietKHTours();
        return lsCTietKHTours;
    }
    public boolean addCTietKHTour(_CTietKHTourDTO t){
        if(t == null) return false;

        boolean success = cTietKHTourDAO.addCTietKHTour(t);
        if(success) lsCTietKHTours.add(t);

        return success;
    }

    public boolean editCTietKHTour(_CTietKHTourDTO t){
        return cTietKHTourDAO.editCTietKHTour(t);
    }

    public boolean removeCTietKHTour(String maCTietKHTour){
        return cTietKHTourDAO.removeCTietKHTour(maCTietKHTour);
    }

    public _CTietKHTourDTO getCTietKHTourById(String maCTietKHTour){
        _CTietKHTourDTO result = null;
        for (_CTietKHTourDTO ct : lsCTietKHTours){
            if(ct.getMaKHTour().trim().equalsIgnoreCase(maCTietKHTour)) {
                result = ct;
                break;
            }
        }
        return result;
    }

    // get CTietKeHTour equal with maKHTour
    public ArrayList<_CTietKHTourDTO> getLsCTietKHToursById(String maKHTour){
        ArrayList<_CTietKHTourDTO> result = new ArrayList<>(); // result CTietKHTours
        ArrayList<_CTietKHTourDTO> list = getAllCTietKHTours(); // list CTietKHTours

        for (_CTietKHTourDTO ct : list){
            if(ct.getMaKHTour().trim().equalsIgnoreCase(maKHTour)) {
                result.add(ct);
            }
        }
        return result;
    }

    public boolean existedCTietKHTourWithID(String maCTKHTour){
        ArrayList<_CTietKHTourDTO> list = getAllCTietKHTours(); // list CTietKHTours

        for (_CTietKHTourDTO ct : list){
            if(ct.getMaKHTour().trim().equalsIgnoreCase(maCTKHTour))
                return true;
        }
        return false;
    }
}
