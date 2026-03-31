package org.example.bus;

import org.example.dao._LoaiTourDAO;
import org.example.dto._LoaiTourDTO;

import java.util.ArrayList;

public class _LoaiTourBUS {
    private ArrayList<_LoaiTourDTO> lsCate;
    private _LoaiTourDAO loaiTourDAO;

    public _LoaiTourBUS(){
        loaiTourDAO = new _LoaiTourDAO();
        lsCate = new ArrayList<>();
    }

    public ArrayList<_LoaiTourDTO> getAllLoaiTour(){
        lsCate = loaiTourDAO.getAllLoaiTour();
        return lsCate;
    }

    public boolean addLoaiTour(_LoaiTourDTO cate){
        if(cate == null) return false;

        boolean success = loaiTourDAO.addLoaiTour(cate);
        if(success) lsCate.add(cate);

        return success;
    }

    public boolean editLoaiTour(_LoaiTourDTO cate){
        return loaiTourDAO.editLoaiTour(cate);
    }

    public boolean removeLoaiTour(String maLoaiTour){
        return loaiTourDAO.removeLoaiTour(maLoaiTour);
    }

    public ArrayList<_LoaiTourDTO> search(String keyWord){
        ArrayList<_LoaiTourDTO> list = new ArrayList<>();

        for (_LoaiTourDTO lt : lsCate){
            if(lt.getTheLoai().trim().toLowerCase().contains(keyWord)){
                list.add(lt);
            }
        }
        return list;
    }

    public _LoaiTourDTO getById(String maLoaiTour){
        _LoaiTourDTO result = null;
        lsCate = loaiTourDAO.getAllLoaiTour();
        for (_LoaiTourDTO lt : lsCate){
            if(lt.getMaLoaiTour().trim().equalsIgnoreCase(maLoaiTour)) {
                result = lt;
                break;
            }
        }
        return result;
    }

    public boolean existedLoaiTourWithID(String maLoaiTour){
        for (_LoaiTourDTO lt : lsCate){
            if(lt.getMaLoaiTour().trim().equalsIgnoreCase(maLoaiTour))
                return true;
        }
        return false;
    }
}
