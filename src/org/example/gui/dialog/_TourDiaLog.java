package org.example.gui.dialog;

import org.example.bus.DiaDiemBUS;
import org.example.bus._LoaiTourBUS;
import org.example.bus._TourBUS;
import org.example.dto.DiaDiemDTO;
import org.example.dto._LoaiTourDTO;
import org.example.dto._TourDTO;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class _TourDiaLog extends JDialog {
    private JLabel jlbMaTour, jlbTen, jlbSoNgay, jlbDonGia, jlbSoCho, jlbDiaDiemKhoiHanh, jlbMaLoaiTour, jlbImgLink, jlbPreview, jlbMaDiaDiem;
    private JTextField txtMaTour, txtTen, txtSoNgay, txtDonGia, txtSoCho, txtDiaDiemKhoiHanh, txtImgLink;
    private JButton saveBtn, cancelBtn, chooseImageBtn;
    private String path;
    private File fileSelected;
    private JPanel formPanel, southPanel;

    private _TourBUS tourBUS;
    private _TourDTO tourDTO;
    private _LoaiTourBUS loaiTourBUS;
    private DiaDiemBUS diaDiemBUS;
    private JComboBox<_LoaiTourDTO> cbLoaiTours;
    private JComboBox<DiaDiemDTO> cbDiaDiem;

    public _TourDiaLog(_TourBUS tourBUS, _TourDTO tourDTO){
        this.tourDTO = tourDTO;
        this.tourBUS = tourBUS;
        loaiTourBUS = new _LoaiTourBUS();
        diaDiemBUS = new DiaDiemBUS();
        cbLoaiTours = new JComboBox<>();
        cbDiaDiem = new JComboBox<>();

        setTitle(tourDTO == null ? "Thêm Tour" : "Sửa Tour");
        setSize(300, 360);
        setLocationRelativeTo(null);
        setModal(true);

        init();
        if(tourDTO != null){
            loadData();
        }
    }

    private void init(){
        setLayout(new BorderLayout());

        formPanel = new JPanel(new GridLayout(9, 2));
        southPanel = new JPanel(new FlowLayout());

        save();
        southPanel.add(saveBtn);
        cancel();
        southPanel.add(cancelBtn);

        
        jlbMaTour = new JLabel("Mã tour");
        jlbMaTour.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbMaTour);
        txtMaTour = new JTextField();
        formPanel.add(txtMaTour);

        
        jlbTen = new JLabel("Tên");
        jlbTen.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbTen);
        txtTen = new JTextField();
        formPanel.add(txtTen);

        
        jlbSoNgay = new JLabel("Số ngày");
        jlbSoNgay.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbSoNgay);
        txtSoNgay = new JTextField();
        formPanel.add(txtSoNgay);

        
        jlbDonGia = new JLabel("Đơn giá");
        jlbDonGia.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbDonGia);
        txtDonGia = new JTextField();
        formPanel.add(txtDonGia);

        
        jlbSoCho = new JLabel("Số chỗ");
        jlbSoCho.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbSoCho);
        txtSoCho = new JTextField();
        formPanel.add(txtSoCho);

        
        jlbDiaDiemKhoiHanh = new JLabel("Địa điểm khởi hành");
        jlbDiaDiemKhoiHanh.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbDiaDiemKhoiHanh);
        txtDiaDiemKhoiHanh = new JTextField();
        txtDiaDiemKhoiHanh.setEnabled(false); 
        formPanel.add(txtDiaDiemKhoiHanh);

        
        jlbMaLoaiTour = new JLabel("Mã loại tour");
        jlbMaLoaiTour.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbMaLoaiTour);

        ArrayList<_LoaiTourDTO> lsCate = loaiTourBUS.getAllLoaiTour();
        DefaultComboBoxModel<_LoaiTourDTO> loaiToursModel = new DefaultComboBoxModel<>();
        if(lsCate != null){
            for(_LoaiTourDTO lt : lsCate){
                loaiToursModel.addElement(lt);
            }
        }
        cbLoaiTours.setModel(loaiToursModel);
        formPanel.add(cbLoaiTours);

        
        jlbImgLink = new JLabel(" Đường dẫn ảnh");
        formPanel.add(jlbImgLink);

        
        JPanel imgPanel = new JPanel(new BorderLayout());
        txtImgLink = new JTextField();
        chooseImage();
        imgPanel.add(chooseImageBtn, BorderLayout.EAST);

        jlbPreview = new JLabel();
        jlbPreview.setPreferredSize(new Dimension(120,80));
        jlbPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        imgPanel.add(jlbPreview, BorderLayout.CENTER);
        formPanel.add(imgPanel);

        
        jlbMaDiaDiem = new JLabel("Mã Địa điểm");
        jlbMaDiaDiem.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 0));
        formPanel.add(jlbMaDiaDiem);

        
        ArrayList<DiaDiemDTO> lsDiaDiem = DiaDiemBUS.getDs();
        DefaultComboBoxModel<DiaDiemDTO> diaDiemModel = new DefaultComboBoxModel<>();
        if(lsDiaDiem != null){
            for(DiaDiemDTO dd : lsDiaDiem){
                diaDiemModel.addElement(dd);
            }
        }
        cbDiaDiem.setModel(diaDiemModel);
        cbDiaDiem.addActionListener(e -> {
            DiaDiemDTO selected = (DiaDiemDTO) cbDiaDiem.getSelectedItem();
            if(selected != null){
                txtDiaDiemKhoiHanh.setText(selected.getdiachi());
            }
        });
        formPanel.add(cbDiaDiem);

        
        if(cbDiaDiem.getItemCount() > 0) {
            cbDiaDiem.setSelectedIndex(0);
        }

        add(formPanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void loadData(){
        txtMaTour.setText(tourDTO.getMaTour());
        txtMaTour.setEnabled(false);

        txtTen.setText(tourDTO.getTen());
        txtSoNgay.setText(tourDTO.getSoNgay() + "");
        txtDonGia.setText(tourDTO.getDonGia() + "");
        txtSoCho.setText(tourDTO.getSoCho() + "");
        txtDiaDiemKhoiHanh.setText(tourDTO.getDiaDiemKhoiHanh());

        
        for(int i = 0; i < cbLoaiTours.getItemCount(); i++){
            _LoaiTourDTO lt = cbLoaiTours.getItemAt(i);
            if(lt.getMaLoaiTour().equalsIgnoreCase(tourDTO.getMaLoaiTour())){
                cbLoaiTours.setSelectedIndex(i);
                break;
            }
        }

        
        for(int i = 0; i < cbDiaDiem.getItemCount(); i++){
            DiaDiemDTO dd = cbDiaDiem.getItemAt(i);
            if(dd.getMaDiaDiem().equalsIgnoreCase(tourDTO.getMaDiaDiem())){
                cbDiaDiem.setSelectedIndex(i);
                break;
            }
        }

        
        txtImgLink.setText(tourDTO.getImgLink());
        if(tourDTO.getImgLink() != null && !tourDTO.getImgLink().isEmpty()){
            try {
                ImageIcon icon = new ImageIcon(tourDTO.getImgLink());
                Image img = icon.getImage().getScaledInstance(120, 80, Image.SCALE_SMOOTH);
                jlbPreview.setIcon(new ImageIcon(img));
            } catch (Exception e) {}
        }
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
            if(isEmpty(txtMaTour, txtTen, txtSoNgay, txtDonGia, txtSoCho, txtDiaDiemKhoiHanh)){
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
                return;
            }

            
            _LoaiTourDTO selectedLoaiTour = (_LoaiTourDTO) cbLoaiTours.getSelectedItem();
            if(selectedLoaiTour == null){
                JOptionPane.showMessageDialog(this, "Vui lòng chọn Loại Tour");
                return;
            }
            DiaDiemDTO selectedLocation = (DiaDiemDTO) cbDiaDiem.getSelectedItem();
            if(selectedLocation == null){
                JOptionPane.showMessageDialog(this, "Vui lòng chọn Địa Điểm");
                return;
            }

            
            int soNgay;
            long donGia;
            int soCho;
            try {
                soNgay = Integer.parseInt(txtSoNgay.getText().trim());
                donGia = Long.parseLong(txtDonGia.getText().trim());
                soCho = Integer.parseInt(txtSoCho.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng số cho Số ngày, Đơn giá và Số chỗ");
                return;
            }

            if(tourDTO == null){
                if(tourBUS.existedTourWithID(txtMaTour.getText())){
                    JOptionPane.showMessageDialog(null, "Mã tour đã tồn tại, vui lòng nhập mã khác!");
                }else{
                    _TourDTO tourMoi = new _TourDTO(
                            txtMaTour.getText().trim(), txtTen.getText().trim(),
                            soNgay, donGia, soCho, txtDiaDiemKhoiHanh.getText().trim(),
                            txtImgLink.getText().trim(), selectedLoaiTour.getMaLoaiTour(), selectedLocation.getMaDiaDiem()
                    );
                    if (tourBUS.addTour(tourMoi)){
                        JOptionPane.showMessageDialog(this, "Đã thêm thành công!");
                        dispose();
                    }else{
                        JOptionPane.showMessageDialog(this, "Thêm thất bại!");
                    }
                }
            }else{
                tourDTO.setTen(txtTen.getText().trim());
                tourDTO.setSoNgay(soNgay);
                tourDTO.setDonGia(donGia);
                tourDTO.setSoCho(soCho);
                tourDTO.setDiaDiemKhoiHanh(txtDiaDiemKhoiHanh.getText().trim());
                tourDTO.setImgLink(txtImgLink.getText().trim());
                tourDTO.setMaLoaiTour(selectedLoaiTour.getMaLoaiTour());
                tourDTO.setMaDiaDiem(selectedLocation.getMaDiaDiem());

                if(tourBUS.editTour(tourDTO)){
                    JOptionPane.showMessageDialog(this, "Chỉnh sửa thành công!");
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(this, "Chỉnh sửa thất bại!");
                }
            }
        });
    }

    private void cancel(){
        cancelBtn = createBtn("Hủy", UIColors.CANCEL);
        cancelBtn.addActionListener(e -> dispose());
    }

    private void chooseImage(){
        chooseImageBtn = createBtn("Chọn", UIColors.CHOSE);
        chooseImageBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            int result = chooser.showOpenDialog(this);

            if(result == JFileChooser.APPROVE_OPTION){
                fileSelected = chooser.getSelectedFile();
                path = fileSelected.getAbsolutePath();
                txtImgLink.setText(path);

                ImageIcon icon = new ImageIcon(path);
                Image img = icon.getImage().getScaledInstance(120, 80, Image.SCALE_SMOOTH);
                jlbPreview.setIcon(new ImageIcon(img));
            }
        });
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