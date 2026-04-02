
package org.example.dto; 

import java.time.LocalDate;


public class Person {
    protected String ho;
    protected String ten;
    protected String diaChi;
    protected String sdt;
    protected LocalDate ngaySinh;

    public Person() {
    }

    public Person(String ho, String ten, String diaChi, String sdt, LocalDate ngaySinh) {
        this.ho = ho;
        this.ten = ten;
        this.diaChi = diaChi;
        this.sdt = sdt;
        this.ngaySinh = ngaySinh;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    
    @Override
    public String toString() {
        
        return this.ho + " " + this.ten;
    }
}