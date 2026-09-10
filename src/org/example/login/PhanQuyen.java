package org.example.login;

import org.example.dto.TaiKhoanDTO;

public class PhanQuyen {
    private static TaiKhoanDTO currentTaiKhoan;

    private PhanQuyen() {
    }

    public static void dangNhap(TaiKhoanDTO taiKhoan) {
        currentTaiKhoan = taiKhoan;
    }

    public static void dangXuat() {
        currentTaiKhoan = null;
    }

    public static TaiKhoanDTO getCurrentTaiKhoan() {
        return currentTaiKhoan;
    }

    public static boolean laQuanLy() {
        return getRole() == TaiKhoanDTO.Role.MANAGER;
    }

    public static TaiKhoanDTO.Role getRole() {
        if (currentTaiKhoan == null) {
            return TaiKhoanDTO.Role.STAFF;
        }

        TaiKhoanDTO.Role baseRole = currentTaiKhoan.getBaseRole();

        if (baseRole == TaiKhoanDTO.Role.MANAGER) {
            if (currentTaiKhoan.isUyQuyen()) {
                return TaiKhoanDTO.Role.STAFF; 
            } else {
                return TaiKhoanDTO.Role.MANAGER;
            }
        }

        if (baseRole == TaiKhoanDTO.Role.VICE_MANAGER) {
            org.example.dao.TaiKhoanDAO dao = new org.example.dao.TaiKhoanDAO();
            if (dao.dangCoQuanLyUyQuyen()) {
                return TaiKhoanDTO.Role.MANAGER; 
            } else {
                return TaiKhoanDTO.Role.VICE_MANAGER;
            }
        }

        return TaiKhoanDTO.Role.STAFF;
    }
}