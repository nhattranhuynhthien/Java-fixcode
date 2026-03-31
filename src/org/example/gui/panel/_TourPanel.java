package org.example.gui.panel;

import org.example.bus._TourBUS;
import org.example.dto._TourDTO;
import org.example.gui.dialog._TourDetailDialog;
import org.example.gui.dialog._TourDiaLog;
import org.example.helper.PDFHelper; // Import PDFHelper

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;

public class _TourPanel extends JPanel {
    // text field
    private JTextField txtSearch;

    // define panel
    private JPanel northPanel, southPanel, searchPanel;

    // define button

    private JButton addBtn, deleteBtn, editBtn, refreshBtn, detailBtn, xuatPDFBtn;

    // relate to table
    private JTable table;
    private JScrollPane scrollPane;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;

    // comboBox
    private JComboBox<String> cmbSearchType;

    private _TourBUS tourBUS;

    public _TourPanel(){
        tourBUS = new _TourBUS();
        init();
        loadTable();
        hasSelectedRow();
    }

    private void init(){
        setLayout(new BorderLayout());

        // northPanel
        northPanel = new JPanel(new BorderLayout());
        JLabel jlbTitle = new JLabel("QUẢN LÝ TOUR DU LỊCH", JLabel.CENTER);
        jlbTitle.setFont(new Font("Arial", Font.BOLD, 18));
        northPanel.add(jlbTitle, BorderLayout.NORTH); // northPanel add components

        // Search panel (define)
        searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(Color.WHITE);

        // titleBorder
        TitledBorder titleSearch = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.CYAN, 2), " TÌM KIẾM TOUR "
        );
        titleSearch.setTitleFont(new Font("Arial", Font.BOLD, 14));
        titleSearch.setTitleColor(new Color(0, 102, 204));
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 10, 20, 10),
                titleSearch)
        );

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Khoảng cách giữa các ô
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // column 1 : type label
        gbc.gridx = 0; gbc.gridy = 0;
        searchPanel.add(new JLabel("Loại:"), gbc);

        // column 2 : cmbSearch By Type
        cmbSearchType = new JComboBox<>(new String[]{
                " Tất cả", " Tên Tour", " Địa điểm khởi hành"
        });
        cmbSearchType.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        searchPanel.add(cmbSearchType, gbc);

        // column 3 : keyWord label
        gbc.gridx = 2;
        searchPanel.add(new JLabel("Từ khóa:"), gbc);

        // column 4 : keyWord txtField
        txtSearch = new JTextField(15);
        txtSearch.addCaretListener(e -> searchByType());
        gbc.gridx = 3; gbc.weightx = 1.0;
        searchPanel.add(txtSearch, gbc);

        northPanel.add(searchPanel, BorderLayout.CENTER);

        initTable(); // init table

        // southPanel contain buttons
        southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        add();
        southPanel.add(addBtn);
        delete();
        southPanel.add(deleteBtn);
        edit();
        southPanel.add(editBtn);
        viewDetail();
        southPanel.add(detailBtn);
        refresh();
        southPanel.add(refreshBtn);
        xuatPDF(); // Khởi tạo và add nút xuất PDF
        southPanel.add(xuatPDFBtn);

        add(northPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void initTable(){
        // columns of table
        String[] columns = {"Mã tour", "Tên", "Số ngày", "Đơn giá", "Số chỗ", "Địa điểm khởi hành", "Mã loại tour", "Mã địa điểm"};

        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);

        table.setDefaultEditor(Object.class, null);

        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);

        scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
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

    private void searchByType(){
        String keyWord = txtSearch.getText().trim().toLowerCase();
        String searchType = (String) cmbSearchType.getSelectedItem();

        RowFilter<DefaultTableModel, Object> rf = new RowFilter<DefaultTableModel, Object>() {
            @Override
            public boolean include(Entry<? extends DefaultTableModel, ? extends Object> entry) {
                if(!keyWord.isEmpty()){
                    boolean found = false;
                    switch (searchType) {
                        case " Tên Tour":
                            found = entry.getStringValue(1).toLowerCase().contains(keyWord);
                            break;
                        case " Địa điểm khởi hành":
                            found = entry.getStringValue(5).toLowerCase().contains(keyWord);
                            break;
                        default: // Tất cả
                            for (int i = 0; i < entry.getValueCount(); i++) {
                                if (entry.getStringValue(i).toLowerCase().contains(keyWord)) {
                                    found = true;
                                    break;
                                }
                            }
                    }
                    if(!found) return false;
                }

                return true;
            }
        };
        rowSorter.setRowFilter(rf);
    }

    private void add(){
        addBtn = createBtn("Thêm Tour", UIColors.ADD);
        addBtn.addActionListener(e -> openDiaLog(null));
    }

    private void openDiaLog(_TourDTO tourDTO){
        _TourDiaLog tourDiaLog = new _TourDiaLog(tourBUS, tourDTO);
        tourDiaLog.setVisible(true);
        loadTable();
    }

    private void delete(){
        deleteBtn = createBtn("Xóa tour", UIColors.DELETE);
        deleteBtn.setEnabled(false);
        deleteBtn.addActionListener(e ->{
            int row = table.getSelectedRow();
            if (row == -1){
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tour cần xóa");
                return;
            }

            String maTour = tableModel.getValueAt(row, 0).toString();
            int confirm = JOptionPane.showConfirmDialog(this, "Xác nhận xóa?");
            if(confirm == JOptionPane.YES_OPTION){
                boolean result = tourBUS.removeTour(maTour);
                if(result)
                    JOptionPane.showMessageDialog(null, "Đã xóa tour có mã: " + maTour);
                else
                    JOptionPane.showMessageDialog(null, "Mã tour không tồn tại: " + maTour);
                loadTable();
            }
        });
    }

    private void edit(){
        editBtn = createBtn("Chỉnh sửa", UIColors.EDIT);
        editBtn.setEnabled(false);
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn tour cần sửa");
                return;
            }
            String maTour = tableModel.getValueAt(row, 0).toString();
            _TourDTO t = tourBUS.getByID(maTour);
            openDiaLog(t);
        });
    }

    private void viewDetail(){
        detailBtn = createBtn("Xem chi tiết", UIColors.VIEW);
        detailBtn.setEnabled(false);
        detailBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn kế tour để xem");
                return;
            }

            String maTour = tableModel.getValueAt(row, 0).toString();
            _TourDTO t = tourBUS.getByID(maTour);
            _TourDetailDialog dialog = new _TourDetailDialog(t);
            dialog.setVisible(true);
        });
    }

    private void refresh(){
        refreshBtn = createBtn("Làm mới", UIColors.REFRESH);
        refreshBtn.addActionListener(e -> {
            String keyWord = txtSearch.getText().trim().toLowerCase();
            ArrayList<_TourDTO> lsTour = tourBUS.search(keyWord);
            loadTableByName(lsTour);
        });
    }

    // HÀM MỚI: Khởi tạo sự kiện Xuất PDF
    private void xuatPDF(){
        xuatPDFBtn = createBtn("Xuất PDF", new Color(244, 67, 54)); // Màu đỏ chuẩn UIColors
        xuatPDFBtn.addActionListener(e -> {
            PDFHelper.xuatPDF(table, "DANH SÁCH TOUR DU LỊCH");
        });
    }

    public void loadTable(){
        tableModel.setRowCount(0);

        for(_TourDTO t : tourBUS.getAllTours()){
            tableModel.addRow(new Object[]{
                    t.getMaTour(),
                    t.getTen(),
                    t.getSoNgay(),
                    t.getDonGia(),
                    t.getSoCho(),
                    t.getDiaDiemKhoiHanh(),
                    t.getMaLoaiTour(),
                    t.getMaDiaDiem()
            });
        }
    }

    private void loadTableByName(ArrayList<_TourDTO> list){
        tableModel.setRowCount(0);

        for(_TourDTO t : list) {
            tableModel.addRow(new Object[]{
                    t.getMaTour(),
                    t.getTen(),
                    t.getSoNgay(),
                    t.getDonGia(),
                    t.getSoCho(),
                    t.getDiaDiemKhoiHanh(),
                    t.getMaLoaiTour(),
                    t.getMaDiaDiem()
            });
        }
    }

    private void hasSelectedRow(){
        table.getSelectionModel().addListSelectionListener(e ->{
            boolean hadSelection = table.getSelectedRow() != -1;
            deleteBtn.setEnabled(hadSelection);
            editBtn.setEnabled(hadSelection);
            detailBtn.setEnabled(hadSelection);
        });
    }
}