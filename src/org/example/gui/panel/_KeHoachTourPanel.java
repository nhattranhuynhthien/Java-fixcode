package org.example.gui.panel;

import org.example.bus.NhanVienBUS;
import org.example.bus._KeHoachTourBUS;
import org.example.bus._TourBUS;
import org.example.dto._KeHoachTourDTO;
import org.example.dto._TourDTO;
import org.example.gui.dialog._KeHoachTourDetailDialog;
import org.example.gui.dialog._KeHoachTourDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class _KeHoachTourPanel extends JPanel {
    private JComboBox<_TourDTO> cbTour;
    private DefaultComboBoxModel<_TourDTO> toursModel;

    private JButton addBtn, deleteBtn, editBtn, detailsBtn, refreshBtn;

    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scrollPane;

    private _KeHoachTourBUS keHoachTourBUS;
    private NhanVienBUS nhanVienBUS;
    private _TourBUS tourBUS;
    private JLabel jlbChonTour;
    private ArrayList<_KeHoachTourDTO> lsKeHoachTours;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public _KeHoachTourPanel(){
        keHoachTourBUS = new _KeHoachTourBUS();
        tourBUS = new _TourBUS();
        nhanVienBUS = new NhanVienBUS();
        cbTour = new JComboBox<>();
        init();

        _TourDTO selectedTour = (_TourDTO) cbTour.getSelectedItem();
        if(selectedTour != null)
            loadTable(selectedTour.getMaTour());
        hasSelectedRow();
    }

    private void init(){
        setLayout(new BorderLayout());

        JPanel northPanel = new JPanel(new BorderLayout());
        JLabel lblTitle = new JLabel("QUẢN LÝ KẾ HOẠCH TOUR", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        northPanel.add(lblTitle, BorderLayout.NORTH);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        jlbChonTour = new JLabel("Chọn Tour");
        filterPanel.add(jlbChonTour);

        toursModel = CBTourPresent();
        cbTour.setModel(toursModel);
        filterPanel.add(cbTour);

        cbTour.addActionListener(e -> {
            _TourDTO selectedTour = (_TourDTO) cbTour.getSelectedItem();
            if(selectedTour != null){
                loadTable(selectedTour.getMaTour());
            }
        });

        northPanel.add(filterPanel, BorderLayout.CENTER);

        initTable();

        JPanel southPanel = new JPanel(new FlowLayout());
        add();
        southPanel.add(addBtn);
        delete();
        southPanel.add(deleteBtn);
        edit();
        southPanel.add(editBtn);
        viewDetail();
        southPanel.add(detailsBtn);
        refresh();
        southPanel.add(refreshBtn);

        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void initTable(){
        String[] columns = {"Mã kế hoạch Tour", "Ngày khởi hành", "Ngày kết thúc", "Tổng số vé", "Tổng chi", "Tổng thu", "Mã Tour", "Mã nhân viên hướng dẫn"};

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        table.setDefaultEditor(Object.class, null);

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
    }

    private void loadTable(String maTour){
        tableModel.setRowCount(0);
        // Lấy danh sách kế hoạch theo mã Tour đã chọn
        lsKeHoachTours = keHoachTourBUS.getAllKeHoachToursByID(maTour);

        for (_KeHoachTourDTO kt : lsKeHoachTours){
            tableModel.addRow(new Object[]{
                    kt.getMaKHTour(),
                    kt.getNgayKhoiHanh().format(formatter),
                    kt.getNgayKetThuc().format(formatter),
                    kt.getTongSoVe(),
                    kt.getTongChi(),
                    kt.getTongThu(),
                    kt.getMaTour(),
                    kt.getMaNVHD()
            });
        }
    }

    private DefaultComboBoxModel<_TourDTO> CBTourPresent(){
        DefaultComboBoxModel<_TourDTO> model = new DefaultComboBoxModel<>();
        ArrayList<_TourDTO> lsTours = tourBUS.getAllTours();
        for (_TourDTO t : lsTours){
            model.addElement(t);
        }
        return model;
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

    private void add(){
        addBtn = createBtn("Thêm kế hoạch Tour", UIColors.ADD);
        addBtn.addActionListener(e -> openDiaLog(null));
    }

    private void openDiaLog(_KeHoachTourDTO keHoachTourDTO){
        _TourDTO selectedTour = (_TourDTO) cbTour.getSelectedItem();

        if(selectedTour == null){
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tour");
            return;
        }

        _KeHoachTourDialog keHoachTourDialog = new _KeHoachTourDialog(keHoachTourBUS, keHoachTourDTO, selectedTour.getMaTour());
        keHoachTourDialog.setVisible(true);

        // ĐÃ SỬA: Sau khi Dialog đóng (xong việc Thêm/Sửa), nạp lại dữ liệu từ DB vào BUS và vẽ lại bảng
        keHoachTourBUS.docDs();
        loadTable(selectedTour.getMaTour());
    }

    private void delete(){
        deleteBtn = createBtn("Xóa kế hoạch tour", UIColors.DELETE);
        deleteBtn.setEnabled(false);
        deleteBtn.addActionListener(e ->{
            int row = table.getSelectedRow();
            if (row == -1) return;

            String maKHTour = tableModel.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?");
            if(confirm == JOptionPane.YES_OPTION){
                if(keHoachTourBUS.removeKeHoachTour(maKHTour)){
                    JOptionPane.showMessageDialog(null, "Đã xóa thành công!");
                    _TourDTO selectedTour = (_TourDTO) cbTour.getSelectedItem();
                    loadTable(selectedTour.getMaTour());
                }
            }
        });
    }

    private void edit(){
        editBtn = createBtn("Chỉnh sửa", UIColors.EDIT);
        editBtn.setEnabled(false);
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;
            String maKHTour = tableModel.getValueAt(row, 0).toString();
            _KeHoachTourDTO kt = keHoachTourBUS.getById(maKHTour);
            openDiaLog(kt);
        });
    }

    private void viewDetail(){
        detailsBtn = createBtn("Xem chi tiết", UIColors.VIEW);
        detailsBtn.setEnabled(false);
        detailsBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) return;

            String maKHTour = tableModel.getValueAt(row, 0).toString();
            _KeHoachTourDetailDialog dialog = new _KeHoachTourDetailDialog(maKHTour);
            dialog.setVisible(true);
        });
    }

    private void refresh(){
        refreshBtn = createBtn("Làm mới", UIColors.REFRESH);
        refreshBtn.addActionListener(e -> {
            // 1. Cập nhật lại danh sách Tour trong ComboBox
            cbTour.setModel(CBTourPresent());

            // ĐÃ SỬA: 2. Gọi hàm nạp lại toàn bộ dữ liệu từ Database vào lớp BUS
            keHoachTourBUS.docDs();

            // 3. Hiển thị lại bảng theo Tour đang chọn
            _TourDTO selectedTour = (_TourDTO) cbTour.getSelectedItem();
            if(selectedTour != null){
                loadTable(selectedTour.getMaTour());
            }
            JOptionPane.showMessageDialog(this, "Đã cập nhật dữ liệu mới nhất!");
        });
    }

    private void hasSelectedRow(){
        table.getSelectionModel().addListSelectionListener(e ->{
            boolean hadSelection = table.getSelectedRow() != -1;
            deleteBtn.setEnabled(hadSelection);
            editBtn.setEnabled(hadSelection);
            detailsBtn.setEnabled(hadSelection);
        });
    }
}