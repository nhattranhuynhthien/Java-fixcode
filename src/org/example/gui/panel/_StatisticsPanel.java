package org.example.gui.panel;

import com.toedter.calendar.JDateChooser;
import org.example.bus.*;
import org.example.dto.*;
import org.example.helper.ExcelHelper;
import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.sql.Date.valueOf;

public class _StatisticsPanel extends JPanel {
    private JDateChooser dateFrom;
    private JDateChooser dateTo;
    private JComboBox<Integer> cbYear;

    private _TourBUS tourBUS;
    private HoaDonBUS hoaDonBus;

    private JPanel cardsPanel;
    private JPanel chartsPanel;
    private JButton btnFilter;

    public _StatisticsPanel() {
        tourBUS = new _TourBUS();
        hoaDonBus = new HoaDonBUS();
        setLayout(new BorderLayout());
        init();
    }

    private void init() {
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        JLabel lblTitle = new JLabel("THỐNG KÊ DOANH THU & KHÁCH HÀNG");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(new Color(0, 150, 136));
        titlePanel.add(lblTitle, BorderLayout.WEST);

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filterPanel.setBackground(Color.WHITE);

        filterPanel.add(new JLabel("Ngày bắt đầu:"));
        dateFrom = new JDateChooser();
        dateFrom.setDateFormatString("dd/MM/yyyy");
        dateFrom.setDate(valueOf(LocalDate.now().minusMonths(1)));
        filterPanel.add(dateFrom);

        filterPanel.add(new JLabel("Đến ngày:"));
        dateTo = new JDateChooser();
        dateTo.setDateFormatString("dd/MM/yyyy");
        dateTo.setDate(valueOf(LocalDate.now()));
        filterPanel.add(dateTo);

        btnFilter = createBtn("Lọc", new Color(33, 150, 243));
        btnFilter.addActionListener(e -> updateCards());
        filterPanel.add(btnFilter);

        // Nút Xuất Excel
        JButton btnExportExcel = createBtn("Xuất Excel", new Color(76, 175, 80));
        btnExportExcel.addActionListener(e -> xuatThongke());
        filterPanel.add(btnExportExcel);

        // Nút Xuất PDF
        JButton btnExportPDF = createBtn("Xuất PDF", new Color(244, 67, 54));
        btnExportPDF.addActionListener(e -> xuatPDF());
        filterPanel.add(btnExportPDF);

        titlePanel.add(filterPanel, BorderLayout.EAST);

        cardsPanel = new JPanel(new GridLayout(1, 4, 15, 15));
        cardsPanel.setBackground(Color.WHITE);
        cardsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        chartsPanel = new JPanel(new GridLayout(1, 1, 15, 15));
        chartsPanel.setBackground(Color.WHITE);
        chartsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        cbYear = new JComboBox<>();
        int currentYear = LocalDate.now().getYear();
        for (int i = currentYear - 3; i <= currentYear + 3; i++) {
            cbYear.addItem(i);
        }
        cbYear.setSelectedItem(currentYear);
        cbYear.addActionListener(e -> updateChart());

        updateCards();
        updateChart();

        add(titlePanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(cardsPanel, BorderLayout.NORTH);
        centerPanel.add(chartsPanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JButton createBtn(String text, Color color) {
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

    private void updateCards() {
        cardsPanel.removeAll();
        try {
            LocalDate fromDate = dateFrom.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate toDate = dateTo.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            hoaDonBus.docDs();

            float totalIncome = hoaDonBus.getTongThu(fromDate, toDate); // Tổng thu thực tế
            float totalExpenditure = hoaDonBus.getTongchi(fromDate, toDate); // Tổng chi thực tế

            // Tính logic tổng thu phải tính & tổng chi dự kiến dựa trên Plan nếu cần.
            // Ở đây gộp thành dữ liệu để thể hiện ra UI
            float profit = totalIncome - totalExpenditure;

            DecimalFormat formatter = new DecimalFormat("###,###,### VNĐ");

            cardsPanel.add(createStatCard("Tổng chi (Dự kiến & TT)", formatter.format(totalExpenditure), new Color(244, 67, 54)));
            cardsPanel.add(createStatCard("Tổng thu (Phải tính)", formatter.format(totalIncome), new Color(76, 175, 80)));
            cardsPanel.add(createStatCard("Lợi nhuận", formatter.format(profit), new Color(3, 169, 244)));

            // Logic Lấy Khách Hàng Có Doanh Thu Cao Nhất
            HashMap<String, Float> tkKH = hoaDonBus.getThongKeDoanhThuTheoKhachHang(fromDate, toDate);
            String bestKH = "Chưa có";
            float maxDoanhThuKH = 0;
            for(Map.Entry<String, Float> entry : tkKH.entrySet()) {
                if(entry.getValue() > maxDoanhThuKH) {
                    maxDoanhThuKH = entry.getValue();
                    bestKH = entry.getKey();
                }
            }
            cardsPanel.add(createStatCard("KH Đặt Nhiều Nhất", bestKH + " ("+formatter.format(maxDoanhThuKH)+")", new Color(255, 152, 0)));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tải thống kê! Vui lòng kiểm tra lại ngày tháng.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    private void updateChart() {
        chartsPanel.removeAll();
        try {
            int selectedYear = (Integer) cbYear.getSelectedItem();
            int[] monthlyIncomeInt = hoaDonBus.getTongThuTungThang(selectedYear);
            float[] monthlyIncome = new float[12];

            for (int i = 0; i < 12; i++) {
                monthlyIncome[i] = (float) monthlyIncomeInt[i];
            }

            chartsPanel.add(createIncomeStatisticsChartByMonth(monthlyIncome, selectedYear));

        } catch (Exception e) {
            e.printStackTrace();
        }

        chartsPanel.revalidate();
        chartsPanel.repaint();
    }

    private JPanel createStatCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(color);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.darker(), 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblTitle.setForeground(Color.WHITE);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblValue.setForeground(Color.WHITE);
        lblValue.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    private JPanel createIncomeStatisticsChartByMonth(float[] monthlyIncome, int selectedYear) {
        // ... (Giữ nguyên logic vẽ biểu đồ cột)
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(33, 150, 243), 2),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        headerPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Thống Kê Doanh Thu Theo Tháng - Năm ");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblTitle.setForeground(new Color(33, 150, 243));

        headerPanel.add(lblTitle);
        headerPanel.add(cbYear);

        JPanel chartArea = new JPanel(new GridLayout(1, 12, 10, 10));
        chartArea.setBackground(Color.WHITE);

        String[] months = {"T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"};

        float maxIncome = 0;
        for (float v : monthlyIncome) {
            if (v > maxIncome) maxIncome = v;
        }
        if (maxIncome == 0) maxIncome = 1;

        for (int i = 0; i < months.length; i++) {
            chartArea.add(createBarChart(months[i], monthlyIncome[i], maxIncome));
        }

        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(chartArea, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createBarChart(String label, float value, float maxValue) {
        // ... (Giữ nguyên form chart cũ)
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel barPanel = new JPanel();
        barPanel.setLayout(new BoxLayout(barPanel, BoxLayout.Y_AXIS));
        barPanel.setBackground(Color.WHITE);

        int maxPixelHeight = 200;
        int barHeight = (int) ((value / maxValue) * maxPixelHeight);

        JPanel bar = new JPanel();
        bar.setBackground(new Color(33, 150, 243));
        bar.setPreferredSize(new java.awt.Dimension(30, barHeight));
        bar.setMaximumSize(new java.awt.Dimension(30, barHeight));
        bar.setBorder(BorderFactory.createLineBorder(new Color(33, 150, 243).darker(), 1));

        String displayValue = (value == 0) ? "0" : String.format("%.1fM", value / 1_000_000);
        JLabel lblValue = new JLabel(displayValue, SwingConstants.CENTER);
        lblValue.setFont(new Font("SansSerif", Font.BOLD, 10));

        JLabel lblLabel = new JLabel(label, SwingConstants.CENTER);
        lblLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));

        barPanel.add(javax.swing.Box.createVerticalGlue());
        if (value > 0 || maxValue == 1) {
            barPanel.add(bar);
        }

        panel.add(lblValue, BorderLayout.NORTH);
        panel.add(barPanel, BorderLayout.CENTER);
        panel.add(lblLabel, BorderLayout.SOUTH);

        return panel;
    }

    // Logic Xuất Excel
    private void xuatThongke() {
        try {
            int selectedYear = (Integer) cbYear.getSelectedItem();
            int[] monthlyIncome =hoaDonBus.getTongThuTungThang(selectedYear);
            int[] monthlyCost = hoaDonBus.getTongChiTungThang(selectedYear);
            float totalIncomeYear = 0;
            float totalCostYear=0;

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
            ExcelHelper.xuatExcel(new JTable(model), null, "ThongKe_" + selectedYear);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Có lỗi xảy ra khi chuẩn bị dữ liệu xuất Excel!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ====== LOGIC MỚI: XUẤT PDF ======
    private void xuatPDF() {
        try {
            int selectedYear = (Integer) cbYear.getSelectedItem();
            int[] monthlyIncome = hoaDonBus.getTongThuTungThang(selectedYear);
            int[] monthlyCost = hoaDonBus.getTongChiTungThang(selectedYear);

            String[] columnNames = {"Tháng", "Doanh Thu (VNĐ)", "Chi Phí (VNĐ)", "Lợi Nhuận (VNĐ)"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columnNames, 0);
            DecimalFormat formatter = new DecimalFormat("###,###,###");

            for (int i = 0; i < 12; i++) {
                model.addRow(new Object[]{
                        "Tháng " + (i + 1),
                        formatter.format(monthlyIncome[i]),
                        formatter.format(monthlyCost[i]),
                        formatter.format(monthlyIncome[i] - monthlyCost[i])
                });
            }
            JTable tableForPDF = new JTable(model);

            // Swing hỗ trợ in Table trực tiếp ra file PDF thông qua Microsoft Print to PDF
            boolean complete = tableForPDF.print(JTable.PrintMode.FIT_WIDTH,
                    new java.text.MessageFormat("Báo cáo doanh thu năm " + selectedYear),
                    new java.text.MessageFormat("Trang - {0}"));
            if (complete) {
                JOptionPane.showMessageDialog(this, "Xuất PDF thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (PrinterException pe) {
            JOptionPane.showMessageDialog(this, "Lỗi in PDF: " + pe.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}