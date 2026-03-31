package org.example.gui.dialog;

import org.example.bus.NhanVienBUS;
import org.example.bus._KeHoachTourBUS;
import org.example.dto.NhanVienDTO;
import org.example.dto._KeHoachTourDTO;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class _KeHoachTourDialog extends JDialog {
    private JLabel jlbMaKHTour, jlbNgayKhoiHanh, jlbNgayKetThuc, jlbTongSoVe, jlbTongChi, jlbTongThu, jlbMaTour, jlbMaNVHD;
    private JTextField txtMaKHTour, txtNgayKhoiHanh, txtNgayKetThuc, txtTongSoVe, txtTongChi, txtTongThu, txtMaTour;
    DefaultTableModel tableModel;
    private JComboBox<NhanVienDTO> cbStaff;
    private DefaultComboBoxModel<NhanVienDTO> staffModel;
    private JButton saveBtn, cancelBtn;

    private NhanVienBUS nhanVienBUS;
    private _KeHoachTourBUS keHoachTourBUS;
    private _KeHoachTourDTO keHoachTourDTO;
    private String maTour;
    private LocalDate today;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public _KeHoachTourDialog(_KeHoachTourBUS keHoachTourBUS, _KeHoachTourDTO keHoachTourDTO, String maTour) {
        this.keHoachTourBUS = keHoachTourBUS;
        this.keHoachTourDTO = keHoachTourDTO;
        this.maTour = maTour;
        this.nhanVienBUS = new NhanVienBUS();

        cbStaff = new JComboBox<>();
        today = LocalDate.now();

        setTitle(keHoachTourDTO == null ? "Thêm kế hoạch tour" : "Sửa kế hoạch Tour");
        setSize(300, 440);
        setLocationRelativeTo(null);
        setModal(true);

        init();
        if (keHoachTourDTO != null) {
            loadData();
        }
    }

    private void init(){
        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(9, 2));
        JPanel southPanel = new JPanel(new FlowLayout());

        save();
        southPanel.add(saveBtn);
        cancel();
        southPanel.add(cancelBtn);

        jlbMaKHTour = new JLabel("Mã kế hoạch tour");
        jlbMaKHTour.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbMaKHTour);
        txtMaKHTour = new JTextField();
        formPanel.add(txtMaKHTour);

        jlbNgayKhoiHanh = new JLabel("Ngày khởi hành");
        jlbNgayKhoiHanh.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbNgayKhoiHanh);
        txtNgayKhoiHanh = new JTextField();
        txtNgayKhoiHanh.setText(today.format(formatter));
        formPanel.add(txtNgayKhoiHanh);

        jlbNgayKetThuc = new JLabel("Ngày kết thúc");
        jlbNgayKetThuc.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbNgayKetThuc);
        txtNgayKetThuc = new JTextField();
        txtNgayKetThuc.setText(today.plusDays(1).format(formatter));
        formPanel.add(txtNgayKetThuc);

        jlbTongSoVe = new JLabel("Tổng số vé");
        jlbTongSoVe.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbTongSoVe);
        txtTongSoVe = new JTextField();
        formPanel.add(txtTongSoVe);

        jlbTongChi = new JLabel("Tổng chi");
        jlbTongChi.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbTongChi);
        txtTongChi = new JTextField();
        formPanel.add(txtTongChi);

        jlbTongThu = new JLabel("Tổng thu");
        jlbTongThu.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbTongThu);
        txtTongThu = new JTextField();
        formPanel.add(txtTongThu);

        jlbMaTour = new JLabel("Mã tour");
        jlbMaTour.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbMaTour);
        txtMaTour = new JTextField(maTour);
        txtMaTour.setEnabled(false);
        formPanel.add(txtMaTour);

        jlbMaNVHD = new JLabel("Nhân viên HD");
        jlbMaNVHD.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbMaNVHD);

        staffModel = CBStaffPresent();
        cbStaff.setModel(staffModel);
        if (staffModel.getSize() > 0) {
            cbStaff.setSelectedIndex(0);
        }
        formPanel.add(cbStaff);

        add(formPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadData () {
        txtMaKHTour.setText(keHoachTourDTO.getMaKHTour());
        txtMaKHTour.setEnabled(false);
        txtNgayKhoiHanh.setText(formatter.format(keHoachTourDTO.getNgayKhoiHanh()));
        txtNgayKetThuc.setText(formatter.format(keHoachTourDTO.getNgayKetThuc()));
        txtTongSoVe.setText(keHoachTourDTO.getTongSoVe() + "");
        txtTongChi.setText(keHoachTourDTO.getTongChi() + "");
        txtTongThu.setText(keHoachTourDTO.getTongThu() + "");
        txtMaTour.setText(keHoachTourDTO.getMaTour());

        // FIX LỖI: Load đúng nhân viên khi chọn chức năng Sửa
        for(int i = 0; i < cbStaff.getItemCount(); i++){
            NhanVienDTO nv = cbStaff.getItemAt(i);
            if(nv.getMaNV().equalsIgnoreCase(keHoachTourDTO.getMaNVHD())){
                cbStaff.setSelectedIndex(i);
                break;
            }
        }
    }

    private DefaultComboBoxModel<NhanVienDTO> CBStaffPresent(){
        DefaultComboBoxModel<NhanVienDTO> model = new DefaultComboBoxModel<>();
        nhanVienBUS.docDSNV();
        ArrayList<NhanVienDTO> lsStaff = NhanVienBUS.dsNV; // Đảm bảo dsNV được lấy từ Static list
        if (lsStaff == null || lsStaff.isEmpty()) return model;

        for (NhanVienDTO t : lsStaff)
            model.addElement(t);
        return model;
    }

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public void save(){
        saveBtn = createBtn("Lưu", UIColors.SAVE);
        saveBtn.addActionListener(e -> {
            if(isEmpty(txtMaKHTour, txtNgayKhoiHanh, txtNgayKetThuc, txtTongSoVe, txtTongChi, txtTongThu)){
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            NhanVienDTO selectedStaff = getStaffSelected();
            if(selectedStaff == null){
                JOptionPane.showMessageDialog(this, "Vui lòng chọn Nhân viên hướng dẫn!");
                return;
            }

            int tongSoVe;
            long tongChi, tongThu;
            LocalDate ngayKhoiHanh, ngayKetThuc;
            try {
                tongSoVe = Integer.parseInt(txtTongSoVe.getText().trim());
                tongChi = Long.parseLong(txtTongChi.getText().trim());
                tongThu = Long.parseLong(txtTongThu.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số!");
                return;
            }

            try {
                ngayKhoiHanh = LocalDate.parse(txtNgayKhoiHanh.getText(), formatter);
                ngayKetThuc = LocalDate.parse(txtNgayKetThuc.getText(), formatter);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Ngày phải đúng định dạng dd/MM/yyyy!");
                return;
            }

            if(keHoachTourDTO == null){
                if(keHoachTourBUS.existedKeHoachTourWithID(txtMaKHTour.getText())){
                    JOptionPane.showMessageDialog(null, "Mã kế hoạch tour đã tồn tại, vui lòng nhập mã khác!");
                }else{
                    _KeHoachTourDTO keHoachTourMoi = new _KeHoachTourDTO(
                            txtMaKHTour.getText().trim(), ngayKhoiHanh,
                            ngayKetThuc, tongSoVe, tongChi, tongThu,
                            selectedStaff.getMaNV(), txtMaTour.getText()
                    );

                    String error = keHoachTourBUS.validateKeHoachTour(keHoachTourMoi);
                    if(error == null){
                        if(keHoachTourBUS.addKeHoachTour(keHoachTourMoi)) {
                            JOptionPane.showMessageDialog(this, "Đã thêm thành công!");
                            dispose();
                        }else{
                            JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                        }
                    }else{
                        JOptionPane.showMessageDialog(this, error);
                    }
                }
            }else{
                keHoachTourDTO.setNgayKhoiHanh(ngayKhoiHanh);
                keHoachTourDTO.setNgayKetThuc(ngayKetThuc);
                keHoachTourDTO.setTongSoVe(tongSoVe);
                keHoachTourDTO.setTongChi(tongChi);
                keHoachTourDTO.setTongThu(tongThu);
                keHoachTourDTO.setMaNVHD(selectedStaff.getMaNV());

                if(keHoachTourBUS.editKeHoachTour(keHoachTourDTO)){
                    JOptionPane.showMessageDialog(this, "Đã chỉnh sửa thành công!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Chỉnh sửa thất bại!");
                }
            }
        });
    }

    public void cancel(){
        cancelBtn = createBtn("Hủy", UIColors.CANCEL);
        cancelBtn.addActionListener(e -> dispose());
    }

    private boolean isEmpty(JTextField... fields){
        for(JTextField field : fields){
            if(field.getText().trim().isEmpty()){
                return true;
            }
        }
        return false;
    }

    private NhanVienDTO getStaffSelected(){
        return (NhanVienDTO) cbStaff.getSelectedItem();
    }
}