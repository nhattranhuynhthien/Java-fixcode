package org.example.gui.dialog;

import com.toedter.calendar.JDateChooser;
import org.example.bus.KhachHangBUS;
import org.example.dao.KhachHangDAO;
import org.example.dto.KhachHangDTO;
import org.example.gui.panel.KhachHangPanel;
import org.example.gui.panel.UIColors;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Date;

public class KhachHangDialog extends JDialog {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(KhachHangDialog.class.getName());
    private KhachHangDAO dsKhachHang = new KhachHangDAO();
    private KhachHangPanel parentPanel;

    public enum Mode {
        ADD, EDIT
    }

    private Mode mode;
    private KhachHangDTO currentKhachHang;
    private KhachHangDAO ds;

    // Components
    private JTextField txtMaKH, txtHoKH, txtTenKH, txtSoDienThoaiKH, txtDiaChiKH;
    private JDateChooser jDateChooser1;
    private JButton btnLuu, btnHuy;

    public KhachHangDialog(java.awt.Frame parent, boolean modal, KhachHangDAO ds, Mode mode, KhachHangDTO kh) {
        super(parent, modal);
        this.ds = ds;
        this.mode = mode;
        this.currentKhachHang = kh;

        initComponents();
        this.setLocationRelativeTo(null); // set location after init

        if (mode == Mode.EDIT && kh != null) {
            setKhachHangData(kh);
            txtMaKH.setEditable(false);
            txtMaKH.setBackground(new Color(240, 240, 240)); // Đổi màu xám cho trường không được sửa
            txtHoKH.requestFocus();
            setTitle("Sửa khách hàng");
        } else {
            setTitle("Thêm khách hàng");
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Khởi tạo các trường nhập liệu
        txtMaKH = new JTextField();
        txtHoKH = new JTextField();
        txtTenKH = new JTextField();
        jDateChooser1 = new JDateChooser();
        txtSoDienThoaiKH = new JTextField();
        txtDiaChiKH = new JTextField();

        jDateChooser1.setDateFormatString("dd/MM/yyyy");

        // Gắn Validator (Logic kiểm tra lỗi giữ nguyên)
        setupValidators();

        // Khởi tạo nút bấm
        luu();
        huy();

        // Xây dựng bố cục chính (Main Layout)
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);

        // 1. Panel Form nhập liệu sử dụng GridBagLayout để căn lề hoàn hảo
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30)); // Padding xung quanh

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Khoảng cách giữa các ô (margin)
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Thêm lần lượt từng dòng vào form
        addFormRow(formPanel, "Mã khách hàng:", txtMaKH, gbc, 0);
        addFormRow(formPanel, "Họ:", txtHoKH, gbc, 1);
        addFormRow(formPanel, "Tên:", txtTenKH, gbc, 2);
        addFormRow(formPanel, "Ngày sinh:", jDateChooser1, gbc, 3);
        addFormRow(formPanel, "Số điện thoại:", txtSoDienThoaiKH, gbc, 4);
        addFormRow(formPanel, "Địa chỉ:", txtDiaChiKH, gbc, 5);

        // 2. Panel Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 15));
        buttonPanel.setBackground(Color.WHITE);
        btnLuu.setPreferredSize(new Dimension(100, 35));
        btnHuy.setPreferredSize(new Dimension(100, 35));
        buttonPanel.add(btnLuu);
        buttonPanel.add(btnHuy);

        // Ghép các Panel vào hộp thoại
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);

        pack(); // Tự động căn chỉnh kích thước chuẩn
    }

    // Hàm hỗ trợ vẽ từng dòng (Label + TextField)
    private void addFormRow(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc, int row) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));

        // Cột Label (bên trái)
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.0;
        panel.add(label, gbc);

        // Cột Text Field (bên phải)
        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.weightx = 1.0;
        field.setPreferredSize(new Dimension(250, 30)); // Cố định chiều dài/cao của các ô nhập liệu
        field.setFont(new Font("SansSerif", Font.PLAIN, 12));
        panel.add(field, gbc);
    }

    private void setupValidators() {
        txtMaKH.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String ma = txtMaKH.getText().trim();
                if (!ma.matches("^KH\\d{3}$")) {
                    JOptionPane.showMessageDialog(null, "Mã khách hàng phải có dạng KHxxx!");
                    return false;
                }
                if (ma.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Mã khách hàng không được để trống!");
                    return false;
                }
                // Chỉ kiểm tra trùng mã khi ở chế độ THÊM MỚI
                if (mode == Mode.ADD) {
                    for (KhachHangDTO kh : ds.layDanhSachKHang()) {
                        if (kh.getMaKH().equals(ma)) {
                            JOptionPane.showMessageDialog(null, "Mã khách hàng đã tồn tại!");
                            return false;
                        }
                    }
                }
                return true;
            }
        });

        txtHoKH.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String ho = txtHoKH.getText().trim();
                if (!ho.matches("^[\\p{L}]+(\\s[\\p{L}]+)*$")) {
                    JOptionPane.showMessageDialog(null, "Họ chỉ được chứa chữ cái và khoảng trắng!");
                    return false;
                }
                if (ho.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Họ không được để trống!");
                    return false;
                }
                return true;
            }
        });

        txtTenKH.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String ten = txtTenKH.getText().trim();
                if (!ten.matches("^[\\p{L}]+(\\s[\\p{L}]+)*$")) {
                    JOptionPane.showMessageDialog(null, "Tên chỉ được chứa chữ cái và khoảng trắng!");
                    return false;
                }
                if (ten.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Tên không được để trống!");
                    return false;
                }
                return true;
            }
        });

        jDateChooser1.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                if (jDateChooser1.getDate() == null) {
                    JOptionPane.showMessageDialog(null, "Ngày sinh không được để trống!");
                    return false;
                } else if (jDateChooser1.getDate().after(new Date())) {
                    JOptionPane.showMessageDialog(null, "Ngày sinh không được lớn hơn ngày hiện tại!");
                    return false;
                } else {
                    return true;
                }
            }
        });

        txtSoDienThoaiKH.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String sdt = txtSoDienThoaiKH.getText().trim();
                if (!sdt.matches("^0\\d{9}$")) {
                    JOptionPane.showMessageDialog(null, "Số điện thoại phải có 10 chữ số và bắt đầu bằng 0!");
                    return false;
                }
                if (sdt.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Số điện thoại không được để trống!");
                    return false;
                }
                return true;
            }
        });

        txtDiaChiKH.setInputVerifier(new InputVerifier() {
            @Override
            public boolean verify(JComponent input) {
                String diaChi = txtDiaChiKH.getText().trim();
                if (!diaChi.matches("^[\\p{L}0-9\\s,.-]+$")) {
                    JOptionPane.showMessageDialog(null, "Địa chỉ chỉ được chứa chữ cái, số, khoảng trắng và các ký tự ,.-!");
                    return false;
                }
                if (diaChi.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Địa chỉ không được để trống!");
                    return false;
                }
                return true;
            }
        });
    }

    private JButton createBtn(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void luu() {
        btnLuu = createBtn("Lưu", UIColors.SAVE);
        btnLuu.addActionListener(v -> {
            // Validate thủ công trước khi lưu đề phòng người dùng bấm Lưu ngay mà chưa rời ô nhập liệu
            if(txtMaKH.getText().trim().isEmpty() || txtTenKH.getText().trim().isEmpty() ||
                    txtHoKH.getText().trim().isEmpty() || jDateChooser1.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String maKH = txtMaKH.getText().trim();
            String ten = txtTenKH.getText().trim();
            String diaChi = txtDiaChiKH.getText().trim();
            LocalDate ngaySinh = jDateChooser1.getDate() != null ?
                    jDateChooser1.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate() : null;
            String ho = txtHoKH.getText().trim();
            String sdt = txtSoDienThoaiKH.getText().trim();

            KhachHangBUS khachHangBUS = new KhachHangBUS();
            if (mode == Mode.ADD) {
                KhachHangDTO newKhachHang = new KhachHangDTO(maKH, ho, ten, diaChi, sdt, ngaySinh);
                khachHangBUS.them(newKhachHang);
            } else if (mode == Mode.EDIT && currentKhachHang != null) {
                currentKhachHang.setHo(ho);
                currentKhachHang.setTen(ten);
                currentKhachHang.setDiaChi(diaChi);
                currentKhachHang.setNgaySinh(ngaySinh);
                currentKhachHang.setSdt(sdt);
                khachHangBUS.suaKhachHang(currentKhachHang);
            }
            dispose(); // Đóng dialog sau khi lưu
        });
    }

    private void huy() {
        btnHuy = createBtn("Hủy", UIColors.CANCEL);
        btnHuy.addActionListener(v -> dispose());
    }

    public void setKhachHangData(KhachHangDTO kh) {
        txtMaKH.setText(kh.getMaKH());
        txtTenKH.setText(kh.getTen());
        txtDiaChiKH.setText(kh.getDiaChi());
        if (kh.getNgaySinh() != null) {
            jDateChooser1.setDate(Date.from(kh.getNgaySinh().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
        } else {
            jDateChooser1.setDate(null);
        }
        txtHoKH.setText(kh.getHo());
        txtSoDienThoaiKH.setText(kh.getSdt());
    }
}