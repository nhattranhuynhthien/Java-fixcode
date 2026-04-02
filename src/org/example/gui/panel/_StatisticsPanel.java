package org.example.gui.panel;

import com.toedter.calendar.JDateChooser;
import org.example.bus.*;
import org.example.helper.ExcelHelper;
import org.example.helper.PDFHelper;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import static java.sql.Date.valueOf;

public class _StatisticsPanel extends JPanel {
    private JDateChooser dateFrom;
    private JDateChooser dateTo;
    private JComboBox<Integer> cbYear;

    private _TourBUS tourBUS;
    private HoaDonBUS hoaDonBus;

    private JPanel cardsPanel;
    private JPanel chartsPanel;
    private JPanel chartArea;
    private JButton btnFilter;

    private Font standardFont = new Font("SansSerif", Font.BOLD, 13);

    public _StatisticsPanel() {
        tourBUS = new _TourBUS();
        hoaDonBus = new HoaDonBUS();
        setLayout(new BorderLayout());
        init();
    }

    private void init() {
        // --- 1. PHẦN TIÊU ĐỀ ---
        JLabel lblTitle = new JLabel("THỐNG KÊ DOANH THU & KHÁCH HÀNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 150, 136));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // --- 2. PHẦN CÔNG CỤ: LỌC + XUẤT ---
        JPanel toolPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        toolPanel.setBackground(Color.WHITE);

        JLabel lblTuNgay = new JLabel("Từ ngày:");
        lblTuNgay.setFont(standardFont);
        toolPanel.add(lblTuNgay);

        dateFrom = new JDateChooser();
        dateFrom.setDateFormatString("dd/MM/yyyy");
        dateFrom.setDate(valueOf(LocalDate.now().minusMonths(1)));
        dateFrom.setPreferredSize(new Dimension(130, 30));
        ((JTextField) dateFrom.getDateEditor().getUiComponent()).setHorizontalAlignment(SwingConstants.CENTER);
        ((JTextField) dateFrom.getDateEditor().getUiComponent()).setFont(standardFont);
        toolPanel.add(dateFrom);

        JLabel lblDenNgay = new JLabel("Đến ngày:");
        lblDenNgay.setFont(standardFont);
        toolPanel.add(lblDenNgay);

        dateTo = new JDateChooser();
        dateTo.setDateFormatString("dd/MM/yyyy");
        dateTo.setDate(valueOf(LocalDate.now()));
        dateTo.setPreferredSize(new Dimension(130, 30));
        ((JTextField) dateTo.getDateEditor().getUiComponent()).setHorizontalAlignment(SwingConstants.CENTER);
        ((JTextField) dateTo.getDateEditor().getUiComponent()).setFont(standardFont);
        toolPanel.add(dateTo);

        btnFilter = createBtn("Lọc", new Color(33, 150, 243));
        btnFilter.setPreferredSize(new Dimension(80, 30));
        btnFilter.addActionListener(e -> updateCards());
        toolPanel.add(btnFilter);

        JButton btnExportExcel = createBtn("Xuất Excel", new Color(76, 175, 80));
        btnExportExcel.setPreferredSize(new Dimension(110, 30));
        btnExportExcel.addActionListener(e -> xuatThongke());
        toolPanel.add(btnExportExcel);

        JButton btnExportPDF = createBtn("Xuất PDF", new Color(244, 67, 54));
        btnExportPDF.setPreferredSize(new Dimension(100, 30));
        btnExportPDF.addActionListener(e -> xuatPDF());
        toolPanel.add(btnExportPDF);

        JPanel topContainer = new JPanel(new BorderLayout());
        topContainer.setBackground(Color.WHITE);
        topContainer.add(lblTitle, BorderLayout.NORTH);
        topContainer.add(toolPanel, BorderLayout.CENTER);
        topContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // --- 3. PHẦN CARDS ---
        cardsPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        cardsPanel.setBackground(Color.WHITE);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        // --- 4. PHẦN BIỂU ĐỒ ---
        chartsPanel = new JPanel(new BorderLayout());
        chartsPanel.setBackground(Color.WHITE);
        chartsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                BorderFactory.createLineBorder(new Color(33, 150, 243), 2)
        ));

        JPanel chartHeaderPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        chartHeaderPanel.setBackground(Color.WHITE);

        JLabel lblChartTitle = new JLabel("Biểu Đồ Doanh Thu Theo Năm: ");
        lblChartTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblChartTitle.setForeground(new Color(33, 150, 243));

        cbYear = new JComboBox<>();
        cbYear.setFont(standardFont);
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear - 3; i <= currentYear + 3; i++) {
            cbYear.addItem(i);
        }
        cbYear.setSelectedItem(currentYear);
        cbYear.addActionListener(e -> updateChart());

        chartHeaderPanel.add(lblChartTitle);
        chartHeaderPanel.add(cbYear);
        chartsPanel.add(chartHeaderPanel, BorderLayout.NORTH);

        chartArea = new JPanel(new GridLayout(1, 12, 10, 0));
        chartArea.setBackground(Color.WHITE);
        chartArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chartsPanel.add(chartArea, BorderLayout.CENTER);

        // --- 5. GỘP CÁC PHẦN ---
        add(topContainer, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(cardsPanel, BorderLayout.NORTH);
        centerPanel.add(chartsPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        updateCards();
        updateChart();
    }

    private JButton createBtn(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(standardFont);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        return btn;
    }

    private void updateCards() {
        cardsPanel.removeAll();
        try {
            LocalDate fromDate = dateFrom.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate toDate = dateTo.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            hoaDonBus.docDs();

            float totalIncome = hoaDonBus.getTongThu(fromDate, toDate);
            float totalExpenditure = hoaDonBus.getTongchi(fromDate, toDate);
            float profit = totalIncome - totalExpenditure;

            DecimalFormat formatter = new DecimalFormat("###,###,### đ");

            cardsPanel.add(createStatCard("Tổng chi (Dự kiến & TT)", formatter.format(totalExpenditure), new Color(244, 67, 54)));
            cardsPanel.add(createStatCard("Tổng thu (Phải tính)", formatter.format(totalIncome), new Color(76, 175, 80)));
            cardsPanel.add(createStatCard("Lợi nhuận", formatter.format(profit), new Color(3, 169, 244)));

            ArrayList<Object[]> tkKH = hoaDonBus.getThongKeDoanhThuTheoKhachHang(fromDate, toDate);
            String bestKH = "Chưa có";
            float maxDoanhThuKH = 0;

            for(Object[] row : tkKH) {
                float doanhThu = (float) row[1];
                if(doanhThu > maxDoanhThuKH) {
                    maxDoanhThuKH = doanhThu;
                    bestKH = row[0].toString();
                }
            }

            String topCustomerDisplay = "Chưa có";
            if (maxDoanhThuKH > 0) {
                topCustomerDisplay = "<html><center>" + bestKH + "<br><span style='font-size:14px; font-weight:normal;'>(" + formatter.format(maxDoanhThuKH) + ")</span></center></html>";
            }

            cardsPanel.add(createStatCard("KH Đặt Nhiều Nhất", topCustomerDisplay, new Color(255, 152, 0)));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải thống kê! Vui lòng kiểm tra lại ngày tháng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private void updateChart() {
        chartArea.removeAll();
        try {
            int selectedYear = (Integer) cbYear.getSelectedItem();
            int[] monthlyIncomeInt = hoaDonBus.getTongThuTungThang(selectedYear);

            float[] monthlyIncome = new float[12];
            float maxIncome = 0;
            for (int i = 0; i < 12; i++) {
                monthlyIncome[i] = (float) monthlyIncomeInt[i];
                if (monthlyIncome[i] > maxIncome) maxIncome = monthlyIncome[i];
            }
            if (maxIncome == 0) maxIncome = 1;

            String[] months = {"T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"};

            for (int i = 0; i < months.length; i++) {
                chartArea.add(createBarChart(months[i], monthlyIncome[i], maxIncome));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        chartArea.revalidate();
        chartArea.repaint();
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setPreferredSize(new Dimension(200, 100));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel(title, SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblValue = new JLabel(value, SwingConstants.CENTER);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblValue.setForeground(Color.WHITE);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    // --- THUẬT TOÁN ĐÃ ĐƯỢC THAY ĐỔI ĐỂ KHẮC PHỤC LỖI TỶ LỆ ---
    private JPanel createBarChart(String label, float value, float maxValue) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String displayValue = (value == 0) ? "0" : String.format("%.1fM", value / 1_000_000);
        JLabel lblValue = new JLabel(displayValue, SwingConstants.CENTER);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblValue.setForeground(Color.DARK_GRAY);

        float percentage = (maxValue == 0) ? 0 : (value / maxValue);

        // Sử dụng Graphics2D để TỰ VẼ biểu đồ. Bất chấp màn hình to hay nhỏ tỷ lệ vẫn đúng 100%
        JPanel barContainer = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = getWidth();
                int h = getHeight(); // Lấy chiều cao thực tế của vùng trống trên màn hình

                int barHeight = (int) (h * percentage); // Chiều cao thực = Chiều cao khung * tỷ lệ phần trăm
                int barWidth = 35;

                int x = (w - barWidth) / 2; // Tính toán để canh giữa
                int y = h - barHeight;      // Vẽ từ dưới đáy lên

                g.setColor(new Color(33, 150, 243));
                g.fillRect(x, y, barWidth, barHeight);
            }
        };
        barContainer.setBackground(Color.WHITE);
        barContainer.setPreferredSize(new Dimension(40, 150)); // Kích thước gợi ý, sẽ tự co dãn lúc chạy

        JLabel lblLabel = new JLabel(label, SwingConstants.CENTER);
        lblLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

        panel.add(lblValue, BorderLayout.NORTH);
        panel.add(barContainer, BorderLayout.CENTER);
        panel.add(lblLabel, BorderLayout.SOUTH);

        return panel;
    }
    // -----------------------------------------------------------

    private void xuatThongke() {
        try {
            int selectedYear = (Integer) cbYear.getSelectedItem();
            int[] monthlyIncome = hoaDonBus.getTongThuTungThang(selectedYear);
            int[] monthlyCost = hoaDonBus.getTongChiTungThang(selectedYear);
            float totalIncomeYear = 0;
            float totalCostYear = 0;

            String[] columnNames = {"Tháng", "Doanh Thu (VNĐ)", "Chi Phí (VNĐ)", "Lợi Nhuận (VNĐ)"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columnNames, 0);
            DecimalFormat formatter = new DecimalFormat("###,###,###");

            for (int i = 0; i < 12; i++) {
                totalIncomeYear += monthlyIncome[i];
                totalCostYear += monthlyCost[i];
                int dthu = monthlyIncome[i] - monthlyCost[i];
                model.addRow(new Object[]{
                        "Tháng " + (i + 1),
                        formatter.format(monthlyIncome[i]),
                        formatter.format(monthlyCost[i]),
                        formatter.format(dthu)
                });
            }
            model.addRow(new Object[]{"TỔNG CỘNG", formatter.format(totalIncomeYear), formatter.format(totalCostYear), formatter.format(totalIncomeYear - totalCostYear)});

            ExcelHelper.xuatExcel(new JTable(model), this, "ThongKe_" + selectedYear);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi chuẩn bị dữ liệu xuất Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xuatPDF() {
        try {
            int selectedYear = (Integer) cbYear.getSelectedItem();
            int[] monthlyIncome = hoaDonBus.getTongThuTungThang(selectedYear);
            int[] monthlyCost = hoaDonBus.getTongChiTungThang(selectedYear);

            String[] columnNames = {"Tháng", "Doanh Thu (VNĐ)", "Chi Phí (VNĐ)", "Lợi Nhuận (VNĐ)"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columnNames, 0);
            DecimalFormat formatter = new DecimalFormat("###,###,###");

            float totalIncomeYear = 0;
            float totalCostYear = 0;

            for (int i = 0; i < 12; i++) {
                totalIncomeYear += monthlyIncome[i];
                totalCostYear += monthlyCost[i];
                int profit = monthlyIncome[i] - monthlyCost[i];

                model.addRow(new Object[]{
                        "Tháng " + (i + 1),
                        formatter.format(monthlyIncome[i]),
                        formatter.format(monthlyCost[i]),
                        formatter.format(profit)
                });
            }
            model.addRow(new Object[]{"TỔNG CỘNG", formatter.format(totalIncomeYear), formatter.format(totalCostYear), formatter.format(totalIncomeYear - totalCostYear)});

            JTable tableForPDF = new JTable(model);
            PDFHelper.xuatPDF(tableForPDF, "BÁO CÁO DOANH THU NĂM " + selectedYear);

        } catch (Exception pe) {
            JOptionPane.showMessageDialog(this, "Lỗi in PDF: " + pe.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}