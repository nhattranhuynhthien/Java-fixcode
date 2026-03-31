/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.example.dto; // Ghi chú: Nếu project của bạn đang dùng package org.example.dto thì nhớ đổi lại nhé

import java.time.LocalDate;

/**
 *
 * @author Admin
 */
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

    // HÀM MỚI: Ghi đè phương thức toString()
    @Override
    public String toString() {
        // Trả về Họ và Tên. Ví dụ: "Nguyễn Văn A"
        return this.ho + " " + this.ten;
    }
}