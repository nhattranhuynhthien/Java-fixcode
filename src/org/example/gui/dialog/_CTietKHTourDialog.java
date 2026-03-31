package org.example.gui.dialog;

import org.example.bus._CTietKHTourBUS;
import org.example.dto._CTietKHTourDTO;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class _CTietKHTourDialog extends JDialog {
    private JLabel jlbMaCTietKHTour, jlbNgayThucHien, jlbTongChi, jlbTienO, jlbTienAn, jlbTienDiLai, jlbDiemDi, jlbDiemDen, jlbMaKHtour;
    private JTextField txtMaCTietKHTour, txtNgayThucHien, txtTongChi, txtTienO, txtTienAn, txtTienDiLai, txtDiemDi, txtDiemDen, txtMaKHtour;
    private JButton saveBtn, cancelBtn;
    private _CTietKHTourBUS cTietKHTourBUS;
    private _CTietKHTourDTO cTietKHTourDTO;
    private String maKHTour;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate today;

    public _CTietKHTourDialog(_CTietKHTourBUS cTietKHTourBUS, _CTietKHTourDTO cTietKHTourDTO, String maKHTour){
        this.cTietKHTourBUS = cTietKHTourBUS;
        this.cTietKHTourDTO = cTietKHTourDTO;
        this.maKHTour = maKHTour;
        today = LocalDate.now();

        setTitle(cTietKHTourDTO == null ? "Thêm chi tiết kế hoạch tour" : "Sửa chi tiết kế hoạch Tour");
        setSize(300, 440);
        setLocationRelativeTo(null);
        setModal(true);

        init();
        if (cTietKHTourDTO != null) {
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

        jlbMaCTietKHTour = new JLabel("Mã chi tiết kế hoạch tour");
        jlbMaCTietKHTour.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbMaCTietKHTour);
        txtMaCTietKHTour = new JTextField();
        formPanel.add(txtMaCTietKHTour);

        jlbNgayThucHien = new JLabel("Ngày thực hiện");
        jlbNgayThucHien.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbNgayThucHien);
        txtNgayThucHien = new JTextField();
        txtNgayThucHien.setText(today.format(formatter));
        formPanel.add(txtNgayThucHien);

        jlbTongChi = new JLabel("Tổng chi");
        jlbTongChi.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbTongChi);
        txtTongChi = new JTextField();
        formPanel.add(txtTongChi);

        jlbTienO = new JLabel("Tiền ở");
        jlbTienO.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbTienO);
        txtTienO = new JTextField();
        formPanel.add(txtTienO);

        jlbTienAn = new JLabel("Tiền ăn");
        jlbTienAn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbTienAn);
        txtTienAn = new JTextField();
        formPanel.add(txtTienAn);

        jlbTienDiLai = new JLabel("Tiền đi lại");
        jlbTienDiLai.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbTienDiLai);
        txtTienDiLai = new JTextField();
        formPanel.add(txtTienDiLai);

        jlbDiemDi = new JLabel("Điểm đi");
        jlbDiemDi.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbDiemDi);
        txtDiemDi = new JTextField();
        formPanel.add(txtDiemDi);

        jlbDiemDen = new JLabel("Điểm đến");
        jlbDiemDen.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbDiemDen);
        txtDiemDen = new JTextField();
        formPanel.add(txtDiemDen);

        jlbMaKHtour = new JLabel("Mã kế hoạch tour");
        jlbMaKHtour.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbMaKHtour);
        txtMaKHtour = new JTextField(maKHTour);
        txtMaKHtour.setEnabled(false);
        formPanel.add(txtMaKHtour);

        add(formPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadData(){
        txtMaCTietKHTour.setText(cTietKHTourDTO.getMaCTietKHTour());
        txtMaCTietKHTour.setEnabled(false);
        txtNgayThucHien.setText(cTietKHTourDTO.getNgayThucHien());
        txtTongChi.setText(cTietKHTourDTO.getTongChi() + "");
        txtTienO.setText(cTietKHTourDTO.getTienO() + "");
        txtTienAn.setText(cTietKHTourDTO.getTienAn() + "");
        txtTienDiLai.setText(cTietKHTourDTO.getTienDiLai() + "");
        txtDiemDi.setText(cTietKHTourDTO.getDiemDi());
        txtDiemDen.setText(cTietKHTourDTO.getDiemDen());
        txtMaKHtour.setText(maKHTour);
    }

    private JButton createBtn(String text, Color color){
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void save(){
        saveBtn = createBtn("Lưu", UIColors.SAVE);
        saveBtn.addActionListener(e -> {
            if(isEmpty(txtMaCTietKHTour, txtNgayThucHien, txtTongChi, txtTienO, txtTienAn, txtTienDiLai, txtDiemDi, txtDiemDen)){
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            // FIX LỖI: Sửa lại đúng tên các txtTienO, txtTienAn, txtTienDiLai
            long tongChi, tienO, tienAn, tienDiLai;
            try {
                tongChi = Long.parseLong(txtTongChi.getText().trim());
                tienO = Long.parseLong(txtTienO.getText().trim());
                tienAn = Long.parseLong(txtTienAn.getText().trim());
                tienDiLai = Long.parseLong(txtTienDiLai.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số cho chi phí!");
                return;
            }

            if(cTietKHTourDTO == null){
                if(cTietKHTourBUS.existedCTietKHTourWithID(txtMaCTietKHTour.getText())){
                    JOptionPane.showMessageDialog(null, "Mã chi tiết kế hoạch tour đã tồn tại, vui lòng nhập mã khác!");
                }else{
                    _CTietKHTourDTO cTietkHTourMoi = new _CTietKHTourDTO(
                            txtMaCTietKHTour.getText().trim(), txtNgayThucHien.getText().trim(),
                            tongChi, tienO, tienAn,
                            tienDiLai, txtDiemDi.getText().trim(), txtDiemDen.getText().trim(), txtMaKHtour.getText()
                    );

                    if(cTietKHTourBUS.addCTietKHTour(cTietkHTourMoi)) {
                        JOptionPane.showMessageDialog(this, "Đã thêm thành công!");
                        dispose();
                    }else{
                        JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                    }
                }
            }else{
                cTietKHTourDTO.setNgayThucHien(txtNgayThucHien.getText().trim());
                cTietKHTourDTO.setTongChi(tongChi);
                cTietKHTourDTO.setTienO(tienO);
                cTietKHTourDTO.setTienAn(tienAn);
                cTietKHTourDTO.setTienDiLai(tienDiLai);
                cTietKHTourDTO.setDiemDi(txtDiemDi.getText().trim());
                cTietKHTourDTO.setDiemDen(txtDiemDen.getText().trim());

                if(cTietKHTourBUS.editCTietKHTour(cTietKHTourDTO)){
                    JOptionPane.showMessageDialog(this, "Đã chỉnh sửa thành công!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Chỉnh sửa thất bại!");
                }
            }
        });
    }

    private void cancel(){
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
}