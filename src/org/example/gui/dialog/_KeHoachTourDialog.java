package org.example.gui.dialog;

import com.toedter.calendar.JDateChooser;
import org.example.bus.NhanVienBUS;
import org.example.bus._KeHoachTourBUS;
import org.example.dto.NhanVienDTO;
import org.example.dto._KeHoachTourDTO;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ArrayList;

public class _KeHoachTourDialog extends JDialog {
    private JLabel jlbMaKHTour, jlbNgayKhoiHanh, jlbNgayKetThuc, jlbTongSoVe, jlbTongChi, jlbTongThu, jlbMaTour, jlbMaNVHD;
    private JTextField txtMaKHTour, txtTongSoVe, txtTongChi, txtTongThu, txtMaTour;
    private JDateChooser txtNgayKhoiHanh, txtNgayKetThuc;

    private JComboBox<NhanVienDTO> cbStaff;
    private DefaultComboBoxModel<NhanVienDTO> staffModel;
    private JButton saveBtn, cancelBtn;

    private NhanVienBUS nhanVienBUS;
    private _KeHoachTourBUS keHoachTourBUS;
    private _KeHoachTourDTO keHoachTourDTO;
    private String maTour;

    public _KeHoachTourDialog(_KeHoachTourBUS keHoachTourBUS, _KeHoachTourDTO keHoachTourDTO, String maTour) {
        this.keHoachTourBUS = keHoachTourBUS;
        this.keHoachTourDTO = keHoachTourDTO;
        this.maTour = maTour;
        this.nhanVienBUS = new NhanVienBUS();

        cbStaff = new JComboBox<>();

        setTitle(keHoachTourDTO == null ? "Thêm kế hoạch tour" : "Sửa kế hoạch Tour");
        setSize(350, 500);
        setLocationRelativeTo(null);
        setModal(true);

        init();
        if (keHoachTourDTO != null) {
            loadData();
        }
    }

    private void init(){
        setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel southPanel = new JPanel(new FlowLayout());

        save();
        southPanel.add(saveBtn);
        cancel();
        southPanel.add(cancelBtn);

        formPanel.add(new JLabel("Mã kế hoạch tour"));
        txtMaKHTour = new JTextField();
        formPanel.add(txtMaKHTour);

        formPanel.add(new JLabel("Ngày khởi hành"));
        txtNgayKhoiHanh = new JDateChooser();
        txtNgayKhoiHanh.setDateFormatString("dd/MM/yyyy");
        formPanel.add(txtNgayKhoiHanh);

        formPanel.add(new JLabel("Ngày kết thúc"));
        txtNgayKetThuc = new JDateChooser();
        txtNgayKetThuc.setDateFormatString("dd/MM/yyyy");
        formPanel.add(txtNgayKetThuc);

        formPanel.add(new JLabel("Tổng số vé"));
        txtTongSoVe = new JTextField();
        formPanel.add(txtTongSoVe);

        
        formPanel.add(new JLabel("Tổng chi"));
        txtTongChi = new JTextField("0");
        txtTongChi.setEditable(false);
        formPanel.add(txtTongChi);

        formPanel.add(new JLabel("Tổng thu"));
        txtTongThu = new JTextField("0");
        txtTongThu.setEditable(false);
        formPanel.add(txtTongThu);

        formPanel.add(new JLabel("Mã tour"));
        txtMaTour = new JTextField(maTour);
        txtMaTour.setEnabled(false);
        formPanel.add(txtMaTour);

        formPanel.add(new JLabel("Nhân viên HD"));
        staffModel = CBStaffPresent();
        cbStaff.setModel(staffModel);
        formPanel.add(cbStaff);

        add(formPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        txtMaKHTour.setText(keHoachTourDTO.getMaKHTour());
        txtMaKHTour.setEnabled(false);

        if (keHoachTourDTO.getNgayKhoiHanh() != null) {
            txtNgayKhoiHanh.setDate(Date.from(keHoachTourDTO.getNgayKhoiHanh().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }
        if (keHoachTourDTO.getNgayKetThuc() != null) {
            txtNgayKetThuc.setDate(Date.from(keHoachTourDTO.getNgayKetThuc().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }

        txtTongSoVe.setText(String.valueOf(keHoachTourDTO.getTongSoVe()));

        
        txtTongChi.setText(String.valueOf(keHoachTourDTO.getTongChi()));
        txtTongThu.setText(String.valueOf(keHoachTourDTO.getTongThu()));

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
        ArrayList<NhanVienDTO> lsNhanViens = NhanVienBUS.dsNV;
        for(NhanVienDTO nv : lsNhanViens){
            model.addElement(nv);
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

    public void save(){
        saveBtn = createBtn("Lưu", UIColors.SAVE);
        saveBtn.addActionListener(e -> {
            if(txtMaKHTour.getText().trim().isEmpty() || txtNgayKhoiHanh.getDate() == null || txtNgayKetThuc.getDate() == null){
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            NhanVienDTO selectedStaff = (NhanVienDTO) cbStaff.getSelectedItem();
            LocalDate ngayKhoiHanh = txtNgayKhoiHanh.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate ngayKetThuc = txtNgayKetThuc.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            try {
                int tongSoVe = Integer.parseInt(txtTongSoVe.getText().trim());
                long tongChi = Long.parseLong(txtTongChi.getText().trim());
                long tongThu = Long.parseLong(txtTongThu.getText().trim());

                if(keHoachTourDTO == null){
                    _KeHoachTourDTO keHoachTourMoi = new _KeHoachTourDTO(
                            txtMaKHTour.getText().trim(), ngayKhoiHanh, ngayKetThuc,
                            tongSoVe, tongChi, tongThu, selectedStaff.getMaNV(), txtMaTour.getText()
                    );
                    if(keHoachTourBUS.addKeHoachTour(keHoachTourMoi)) {
                        JOptionPane.showMessageDialog(this, "Đã thêm thành công!");
                        dispose();
                    }
                } else {
                    keHoachTourDTO.setNgayKhoiHanh(ngayKhoiHanh);
                    keHoachTourDTO.setNgayKetThuc(ngayKetThuc);
                    keHoachTourDTO.setTongSoVe(tongSoVe);
                    keHoachTourDTO.setMaNVHD(selectedStaff.getMaNV());
                    
                    if(keHoachTourBUS.editKeHoachTour(keHoachTourDTO)){
                        JOptionPane.showMessageDialog(this, "Đã chỉnh sửa thành công!");
                        dispose();
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi định dạng số: " + ex.getMessage());
            }
        });
    }

    public void cancel(){
        cancelBtn = createBtn("Hủy", UIColors.CANCEL);
        cancelBtn.addActionListener(e -> dispose());
    }
}