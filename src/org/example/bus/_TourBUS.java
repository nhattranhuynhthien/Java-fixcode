package org.example.bus;

import org.example.dao._TourDAO;
import org.example.dto._TourDTO;

import java.util.ArrayList;

public class _TourBUS {
    private ArrayList<_TourDTO> lsTour;
    private _TourDAO tourDAO;

    //constructor
    public _TourBUS(){
        tourDAO = new _TourDAO();
        lsTour = new ArrayList<>();
    }

    public ArrayList<_TourDTO> getAllTours(){
        lsTour = tourDAO.getAllTours();
        return lsTour;
    }

    public boolean addTour(_TourDTO t){
        if(t == null) return false;

        if(t.getSoNgay() <= 0 || t.getSoCho() < 0){
            return false;
        }

        boolean success = tourDAO.addTour(t);
        if(success) lsTour.add(t);

        return success;
    }

    public boolean editTour(_TourDTO t){
        if(t.getSoNgay() <= 0 || t.getSoCho() < 0)
            return false;

        return tourDAO.editTour(t);
    }

    public boolean removeTour(String maTour){
        return tourDAO.removeTour(maTour);
    }

    public long totalCost(String maTour){
        ArrayList<_TourDTO> lsTour = getAllTours();
        for (_TourDTO t : lsTour) {
            if (t.getMaTour().equalsIgnoreCase(maTour))
                return t.getSoNgay() * t.getDonGia();
        }
        return 0;
    }

    public ArrayList<_TourDTO> search(String keyWord){
        ArrayList<_TourDTO> list = new ArrayList<>();
        for (_TourDTO lt : lsTour){
            if(lt.getTen().trim().toLowerCase().contains(keyWord)){
                list.add(lt);
            }
        }
        return list;
    }

    public _TourDTO getByID(String maTour){
        _TourDTO tour = new _TourDTO();
        for (_TourDTO t : lsTour){
            if(t.getMaTour().trim().equalsIgnoreCase(maTour)){
                tour = t;
                break;
            }
        }
        return tour;
    }

    public boolean existedTourWithID(String maTour){
        for (_TourDTO t : lsTour){
            if(t.getMaTour().trim().equalsIgnoreCase(maTour))
                return true;
        }
        return false;
    }
}
