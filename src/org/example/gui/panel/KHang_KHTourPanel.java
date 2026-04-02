package org.example.gui.panel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import org.example.bus.KHang_KHTourBUS;
import org.example.dao.KHang_KHTourDAO;
import org.example.dao.KhachHangDAO;
import org.example.dto.KHang_KHTourDTO;
import org.example.dto.KhachHangDTO;
import org.example.gui.dialog.KHang_KHTourDialog;
import org.example.login.PhanQuyen;

public class KHang_KHTourPanel extends JPanel {
    KHang_KHTourDAO ds = new KHang_KHTourDAO();
    KhachHangDAO dsKH = new KhachHangDAO();
    KHang_KHTourBUS khangkhtBUS = new KHang_KHTourBUS();
    KHang_KHTourDialog khangkhtDialog;

    // Khai báo thêm biến cho combobox lọc theo tour
    private JComboBox<String> cbFilterKHTour;

    public KHang_KHTourPanel() {
        khangkhtBUS = new KHang_KHTourBUS();
        initComponents();
        if (!PhanQuyen.laQuanLy()) {
            btnXoa.setEnabled(false);
        }
        hasSelectedRow();
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {

            private void search() {
                String keyword = txtSearch.getText().trim();
                List<KHang_KHTourDTO> list = khangkhtBUS.timKHang_KHTours(getColumnName(jComboBox2.getSelectedItem().toString()), keyword);
                loadKHang_KHTourToTable(list);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                search();
            }
        });
        loadKHang_KHTourToTable(ds.layDanhSachKHang_KHTour());
    }

    private void initComponents() {
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jPanel2 = new JPanel();
        jLabel2 = new JLabel();
        jComboBox2 = new JComboBox<>();
        txtSearch = new JTextField();
        jScrollPane2 = new JScrollPane();
        jTable2 = new JTable();
        jPanel3 = new JPanel();

        setLayout(new BorderLayout());

        jPanel1.setLayout(new BorderLayout());

        jLabel1.setFont(new Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Quản lí khách hàng - kế hoạch tour");
        jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
        jPanel1.add(jLabel1, BorderLayout.CENTER);

        jLabel2.setText("Tìm kiếm");
        jPanel2.add(jLabel2);

        jComboBox2.setModel(new DefaultComboBoxModel<>(new String[] { "Mã khách hàng", "Họ", "Tên", "Mã kế hoạch tour", "Giá vé" }));
        jComboBox2.addActionListener(this::jComboBox2ActionPerformed);
        jPanel2.add(jComboBox2);

        txtSearch.setPreferredSize(new Dimension(250, 22)); // Thu nhỏ ô tìm kiếm lại một chút để nhường chỗ cho ô lọc
        jPanel2.add(txtSearch);

        // --- BẮT ĐẦU: THÊM PHẦN LỌC THEO KẾ HOẠCH TOUR ---
        JLabel lblFilter = new JLabel(" | Lọc theo Tour: ");
        jPanel2.add(lblFilter);

        cbFilterKHTour = new JComboBox<>();
        cbFilterKHTour.addItem("Tất cả");

        // Lấy danh sách các mã KHTour duy nhất từ dữ liệu hiện có để đưa vào Combobox
        List<KHang_KHTourDTO> allItems = ds.layDanhSachKHang_KHTour();
        List<String> uniqueKHTours = new ArrayList<>();
        for(KHang_KHTourDTO kht : allItems) {
            if(!uniqueKHTours.contains(kht.getMaKHTour())) {
                uniqueKHTours.add(kht.getMaKHTour());
                cbFilterKHTour.addItem(kht.getMaKHTour());
            }
        }

        // Thêm sự kiện khi chọn Combobox thì tiến hành lọc bảng
        cbFilterKHTour.addActionListener(e -> {
            String selectedKHTour = cbFilterKHTour.getSelectedItem().toString();
            if (selectedKHTour.equals("Tất cả")) {
                loadKHang_KHTourToTable(ds.layDanhSachKHang_KHTour());
            } else {
                List<KHang_KHTourDTO> filteredList = khangkhtBUS.timKHang_KHTours("MaKHTour", selectedKHTour);
                loadKHang_KHTourToTable(filteredList);
            }
        });
        jPanel2.add(cbFilterKHTour);
        // --- KẾT THÚC: THÊM PHẦN LỌC THEO KẾ HOẠCH TOUR ---

        jPanel1.add(jPanel2, BorderLayout.PAGE_END);

        add(jPanel1, BorderLayout.PAGE_START);

        jTable2.setModel(new DefaultTableModel(
                new Object [][] {
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null}
                },
                new String [] {
                        "Mã khách hàng", "Họ", "Tên khách hàng", "Mã kế hoạch tour", "Giá vé"
                }
        ) {
            Class[] types = new Class [] {
                    String.class, String.class, String.class, String.class, Long.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);
        jScrollPane2.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        add(jScrollPane2, BorderLayout.CENTER);

        // Khởi tạo các nút (GỌI ĐÚNG THỨ TỰ ĐỂ TRÁNH NULL POINTER EXCEPTION)
        them();
        sua();
        xoa();
        lamMoi();

        jPanel3.add(btnThem);
        jPanel3.add(btnXoa);
        jPanel3.add(btnSua);
        jPanel3.add(btnLamMoi);

        add(jPanel3, BorderLayout.PAGE_END);
        jTable2.getSelectionModel().addListSelectionListener(e -> {
            boolean coDongDuocChon = jTable2.getSelectedRow() != -1;
            if (btnSua != null) btnSua.setEnabled(coDongDuocChon);
            if (PhanQuyen.laQuanLy() && btnXoa != null) {
                btnXoa.setEnabled(coDongDuocChon);
            }
        });
    }

    private void jComboBox2ActionPerformed(ActionEvent evt) {
        // TODO add your handling code here:
    }

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorderPainted(false);

        return btn;
    }

    private void them(){
        btnThem = createBtn("Thêm", UIColors.ADD);
        btnThem.addActionListener(v -> {
            khangkhtDialog = new KHang_KHTourDialog(null, true, ds, KHang_KHTourDialog.Mode.ADD, null);
            khangkhtDialog.setVisible(true);
            loadKHang_KHTourToTable(ds.layDanhSachKHang_KHTour());
        });
    }

    private void xoa(){
        btnXoa = createBtn("Xóa", UIColors.DELETE);
        btnXoa.setEnabled(false); // Sửa lỗi NullPointerException: Đã xóa dòng gọi btnSua ở đây
        btnXoa.addActionListener(v -> {
            if (!PhanQuyen.laQuanLy()) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền xóa dữ liệu.");
                return;
            }
            int i = jTable2.getSelectedRow();
            int result = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION && i >= 0) {
                String maKHang = (String) jTable2.getValueAt(i, 0);
                String maKHTour = (String) jTable2.getValueAt(i, 3);
                KHang_KHTourBUS khangkhtBUS = new KHang_KHTourBUS();
                khangkhtBUS.xoaKHang_KHTour(maKHTour, maKHang);
                DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
                model.removeRow(i);
            }
        });
    }

    private void sua(){
        btnSua = createBtn("Sửa", UIColors.EDIT);
        btnSua.setEnabled(false); // Chuyển việc làm mờ nút Sửa mặc định về đây
        btnSua.addActionListener(v -> {
            int i = jTable2.getSelectedRow();

            if (i >= 0) {
                String maKHang = (String) jTable2.getValueAt(i, 0);
                String maKHTour = (String) jTable2.getValueAt(i, 3);
                KHang_KHTourDTO khangkht = khangkhtBUS.timKiemChinhXac(maKHTour, maKHang);
                if (khangkht != null && khangkht.getMaKHang().equals(maKHang)) {
                    khangkhtDialog = new KHang_KHTourDialog(null, true, ds, KHang_KHTourDialog.Mode.EDIT, khangkht);
                    khangkhtDialog.setVisible(true);

                    loadKHang_KHTourToTable(ds.layDanhSachKHang_KHTour());
                }
            }
        });
    }

    private void lamMoi(){
        btnLamMoi = createBtn("Làm mới", UIColors.REFRESH);
        btnLamMoi.addActionListener(v -> {
            loadKHang_KHTourToTable(ds.layDanhSachKHang_KHTour());
            cbFilterKHTour.setSelectedIndex(0); // Reset combobox về "Tất cả"
            txtSearch.setText(""); // Reset thanh tìm kiếm
        });
    }

    private void jTable2MouseClicked(MouseEvent evt) {
        if (PhanQuyen.laQuanLy()) {
            btnXoa.setEnabled(true);
        }
        btnSua.setEnabled(true);
    }

    private void searchKHang_KHTour() {
        String keyword = txtSearch.getText().trim();
        String selected = jComboBox2.getSelectedItem().toString();
        List<KHang_KHTourDTO> list;
        if (keyword.isEmpty()) {
            loadKHang_KHTourToTable(ds.layDanhSachKHang_KHTour());
            return;
        } else {
            String column = getColumnName(selected);
            list = khangkhtBUS.timKHang_KHTours(column, keyword);
        }
        loadKHang_KHTourToTable(list);
    }

    private String getColumnName(String selected) {
        switch (selected) {
            case "Mã khách hàng":
                return "MaKHang";
            case "Họ":
                return "Ho";
            case "Tên":
                return "Ten";
            case "Mã kế hoạch tour":
                return "MaKHTour";
            case "Giá vé":
                return "GiaVe";
            default:
                return "";
        }
    }

    private void loadKHang_KHTourToTable(List<KHang_KHTourDTO> khangkhtList) {
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        model.setRowCount(0);
        for (KHang_KHTourDTO kht : khangkhtList) {
            for (KhachHangDTO kh : dsKH.layDanhSachKHang()) {
                if (kht.getMaKHang().equals(kh.getMaKH())) {
                    Object[] row = {kht.getMaKHang(), kh.getHo(), kh.getTen(), kht.getMaKHTour(), kht.getGiaVe()};
                    model.addRow(row);
                    break;
                }
            }
        }
    }

    private void hasSelectedRow(){
        jTable2.getSelectionModel().addListSelectionListener(e ->{
            boolean hadSelection = jTable2.getSelectedRow() != -1;
            if(btnXoa != null) btnXoa.setEnabled(hadSelection);
            if(btnSua != null) btnSua.setEnabled(hadSelection);
        });
    }

    // Variables declaration - do not modify
    private JButton btnLamMoi, btnSua, btnThem, btnXoa;
    private JComboBox<String> jComboBox2;
    private JLabel jLabel1, jLabel2;
    private JPanel jPanel1, jPanel2, jPanel3;
    private JScrollPane jScrollPane2;
    private JTable jTable2;
    private JTextField txtSearch;
}