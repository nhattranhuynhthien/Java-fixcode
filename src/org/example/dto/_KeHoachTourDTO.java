package org.example.dto;

import java.time.LocalDate;

public class _KeHoachTourDTO {
    private String maKHTour;
    private LocalDate ngayKhoiHanh;
    private LocalDate ngayKetThuc;
    private int tongSoVe;
    private long tongChi;
    private long tongThu;
    private String maTour;
    private String maNVHD;

    public _KeHoachTourDTO() {
        this.maKHTour = "";
        this.ngayKhoiHanh = null;
        this.ngayKetThuc = null;
        this.tongSoVe = 0;
        this.tongChi = 0;
        this.tongThu = 0;
        this.maTour = "";
        this.maNVHD = "";
    }

    public _KeHoachTourDTO(String maKHTour, LocalDate ngayKhoiHanh, LocalDate ngayKetThuc, int tongSoVe, long tongChi, long tongThu, String maTour, String maNVHD) {
        this.maKHTour = maKHTour;
        this.ngayKhoiHanh = ngayKhoiHanh;
        this.ngayKetThuc = ngayKetThuc;
        this.tongSoVe = tongSoVe;
        this.tongChi = tongChi;
        this.tongThu = tongThu;
        this.maTour = maTour;
        this.maNVHD = maNVHD;
    }

    public void setMaKHTour(String maKHTour) {
        this.maKHTour = maKHTour;
    }

    public void setNgayKhoiHanh(LocalDate ngayKhoiHanh) {
        this.ngayKhoiHanh = ngayKhoiHanh;
    }

    public void setNgayKetThuc(LocalDate ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public void setTongSoVe(int tongSoVe) {
        this.tongSoVe = tongSoVe;
    }

    public void setTongChi(long tongChi) {
        this.tongChi = tongChi;
    }

    public void setTongThu(long tongThu) {
        this.tongThu = tongThu;
    }

    public void setMaTour(String maTour){
        this.maTour = maTour;
    }

    public void setMaNVHD(String maNVHD){
        this.maNVHD = maNVHD;
    }


    public String getMaKHTour() {
        return maKHTour;
    }

    public LocalDate getNgayKhoiHanh() {
        return ngayKhoiHanh;
    }

    public LocalDate getNgayKetThuc() {
        return ngayKetThuc;
    }

    public int getTongSoVe() {
        return tongSoVe;
    }

    public long getTongChi() {
        return tongChi;
    }

    public long getTongThu() {
        return tongThu;
    }

    public String getMaTour() {
        return maTour;
    }

    public String getMaNVHD() {
        return maNVHD;
    }
}
