/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.dto;import java.time.LocalDate;
/**
 *
 * @author Admin
 */
public class NhanVienDTO extends Person {
    private String maNV;
    private String chucVu;

    public NhanVienDTO() {
    }

    public NhanVienDTO(String maNV, String chucVu, String ho, String ten, String diaChi, String sdt, LocalDate ngaySinh) {
        super(ho, ten, diaChi, sdt, ngaySinh);
        this.maNV = maNV;
        this.chucVu = chucVu;
    }

    @Override
    public String toString() {
        return this.maNV + " - " + this.ho + " " + this.ten;
    }

    public String getMaNV() {
        return maNV;
    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }
    
}
