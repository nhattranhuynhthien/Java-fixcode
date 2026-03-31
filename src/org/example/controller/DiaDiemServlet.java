package org.example.controller;

import org.example.bus.DiaDiemBUS;
import org.example.dto.DiaDiemDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "DiaDiemServlet", urlPatterns = {"/DiaDiem"})
public class DiaDiemServlet extends HttpServlet {

    private DiaDiemBUS diaDiemBUS;

    @Override
    public void init() throws ServletException {
        super.init();
        diaDiemBUS = new DiaDiemBUS();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String action = request.getParameter("action");
        ArrayList<DiaDiemDTO> danhSachDiaDiem;

        // Xử lý tìm kiếm dựa trên các hàm có sẵn trong DiaDiemBUS
        if ("search".equals(action)) {
            String searchType = request.getParameter("searchType");
            String keyword = request.getParameter("keyword");

            if (keyword != null && !keyword.trim().isEmpty()) {
                switch (searchType) {
                    case "TenDiaDiem":
                        danhSachDiaDiem = diaDiemBUS.getDsTheoTenDiaDiem(keyword);
                        break;
                    case "DiaChi":
                        danhSachDiaDiem = diaDiemBUS.getDsTheoDiachi(keyword);
                        break;
                    case "QuocGia":
                        danhSachDiaDiem = diaDiemBUS.getDsTheoQuocGia(keyword);
                        break;
                    default:
                        danhSachDiaDiem = DiaDiemBUS.getDs();
                }
            } else {
                danhSachDiaDiem = DiaDiemBUS.getDs();
            }
        } else {
            danhSachDiaDiem = DiaDiemBUS.getDs();
        }

        request.setAttribute("danhSachDiaDiem", danhSachDiaDiem);
        request.getRequestDispatcher("/DiaDiem.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        // Lấy dữ liệu từ form
        String ma = request.getParameter("maDiaDiem");
        String ten = request.getParameter("tenDiaDiem");
        String diaChi = request.getParameter("diaChi");
        String quocGia = request.getParameter("quocGia");

        DiaDiemDTO dd = new DiaDiemDTO(ma, ten, diaChi, quocGia);

        if ("add".equals(action)) {
            diaDiemBUS.themDiaDiem(dd);

        } else if ("delete".equals(action)) {
            diaDiemBUS.xoaDiaDiem(dd);

        } else if ("edit".equals(action)) {
            String oldTen = request.getParameter("oldTenDiaDiem");
            diaDiemBUS.suaDiaDiem(dd, oldTen);
        }

        // Sau khi xử lý xong, quay lại trang danh sách
        response.sendRedirect(request.getContextPath() + "/DiaDiem");
    }
}