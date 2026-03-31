package org.example.bus;

import org.example.dao.DiaDiemDAO;
import org.example.dto.DiaDiemDTO;
import java.util.ArrayList;

public class DiaDiemBUS {
    public static ArrayList<DiaDiemDTO> ds;
    public static DiaDiemDAO dao = new DiaDiemDAO();

    public DiaDiemBUS(){
        if(ds == null){
            ds = dao.getDs();
        }
    }

    public void DocDs(){
        ds = dao.getDs();
    }

    public static ArrayList<DiaDiemDTO> getDs(){
        if(ds == null){
            ds = dao.getDs();
        }
        return ds;
    }

    public boolean timDiaDiem(DiaDiemDTO dd){
        for(DiaDiemDTO d : ds){
            if(d.getMaDiaDiem().equals(dd.getMaDiaDiem())){
                return true;
            }
        }
        return false;
    }

    public DiaDiemDTO timDiaDiemTheoMa(String maDiaDiem){
        for(DiaDiemDTO dd : ds){
            if(dd.getMaDiaDiem().trim().equalsIgnoreCase(maDiaDiem))
                return dd;
        }
        return null;
    }

    public boolean themDiaDiem(DiaDiemDTO dd){
        if(timDiaDiem(dd)){
            return false;
        }

        ds.add(dd);
        dao.themDiaDiem(dd);
        return true;
    }

    public boolean xoaDiaDiem(DiaDiemDTO dd){
        if(!timDiaDiem(dd)){
            return false;
        }

        ds.remove(dd);
        dao.xoaDiaDiem(dd);
        return true;
    }

    public boolean suaDiaDiem(DiaDiemDTO dd, String maDiaDiem){
        for(int i = 0; i < ds.size(); i++){
            if(ds.get(i).getMaDiaDiem().equals(maDiaDiem)){
                ds.set(i, dd);
                break;
            }
        }
        return dao.suaDiaDiem(dd);
    }



    public ArrayList<DiaDiemDTO> getDsTheoDiachi(String DiaChi){
        if(DiaChi == null || DiaChi.trim().isEmpty()){
            return getDs();
        }
        return dao.getDstheoDiaChi(DiaChi.trim());
    }

    public ArrayList<DiaDiemDTO> getDsTheoQuocGia(String quocgia){
        if(quocgia == null || quocgia.trim().isEmpty()){
            return getDs();
        }
        return dao.getDstheoQuocGia(quocgia.trim());
    }

    // HÀM MỚI: Lấy danh sách Địa Điểm theo Tên Địa Điểm
    public ArrayList<DiaDiemDTO> getDsTheoTenDiaDiem(String tenDiaDiem){
        if(tenDiaDiem == null || tenDiaDiem.trim().isEmpty()){
            return getDs(); // Trả về toàn bộ danh sách nếu không nhập từ khóa
        }
        return dao.getDstheoTenDiaDiem(tenDiaDiem.trim());
    }
}