package org.example.helper;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PDFHelper {

    /**
     * Hàm xuất toàn bộ dữ liệu từ JTable ra file PDF
     */
    public static void xuatPDF(JTable table, String title) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file PDF");
        fileChooser.setSelectedFile(new File("TaiLieu.pdf"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }

            try {
                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                // 1. Cấu hình Font
                Font titleFont, headerFont, normalFont, italicFont;
                try {
                    BaseFont bf = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    titleFont = new Font(bf, 18, Font.BOLD, BaseColor.BLUE);
                    headerFont = new Font(bf, 12, Font.BOLD, BaseColor.WHITE);
                    normalFont = new Font(bf, 12, Font.NORMAL, BaseColor.BLACK);
                    italicFont = new Font(bf, 12, Font.ITALIC, BaseColor.DARK_GRAY);
                } catch (Exception e) {
                    titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                    headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                    normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
                    italicFont = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC);
                }

                Paragraph pdfTitle = new Paragraph(title, titleFont);
                pdfTitle.setAlignment(Element.ALIGN_CENTER);
                pdfTitle.setSpacingAfter(5);
                document.add(pdfTitle);

                String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                Paragraph pdfDate = new Paragraph("Ngày xuất: " + currentDate, italicFont);
                pdfDate.setAlignment(Element.ALIGN_RIGHT);
                pdfDate.setSpacingAfter(20);
                document.add(pdfDate);

                TableModel model = table.getModel();
                PdfPTable pdfTable = new PdfPTable(model.getColumnCount());

                // --- TÍNH TOÁN CHIỀU RỘNG CHUẨN ---
                float[] columnWidths = new float[model.getColumnCount()];
                for (int col = 0; col < model.getColumnCount(); col++) {
                    int maxLength = model.getColumnName(col).length();
                    for (int row = 0; row < model.getRowCount(); row++) {
                        Object val = model.getValueAt(row, col);
                        if (val != null) {
                            int cellLength = val.toString().length();
                            if (cellLength > maxLength) {
                                maxLength = cellLength;
                            }
                        }
                    }
                    // Nhân hệ số 10 + 20 padding để iTextPDF có dư không gian chia tỷ lệ
                    columnWidths[col] = (maxLength * 10f) + 20f;
                }

                // LƯU Ý QUAN TRỌNG: Gán Widths (tỷ lệ) TRƯỚC, ép 100% trang SAU
                pdfTable.setWidths(columnWidths);
                pdfTable.setWidthPercentage(100);
                // ------------------------------------

                // Thêm Header
                for (int i = 0; i < model.getColumnCount(); i++) {
                    PdfPCell headerCell = new PdfPCell(new Phrase(model.getColumnName(i), headerFont));
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerCell.setBackgroundColor(new BaseColor(33, 150, 243));
                    headerCell.setPadding(8);
                    pdfTable.addCell(headerCell);
                }

                // Thêm Dữ liệu
                for (int rows = 0; rows < model.getRowCount(); rows++) {
                    for (int cols = 0; cols < model.getColumnCount(); cols++) {
                        Object val = model.getValueAt(rows, cols);
                        String text = (val == null) ? "" : val.toString();
                        PdfPCell dataCell = new PdfPCell(new Phrase(text, normalFont));
                        dataCell.setPadding(5);
                        pdfTable.addCell(dataCell);
                    }
                }

                document.add(pdfTable);
                document.close();

                JOptionPane.showMessageDialog(null, "Xuất PDF thành công!\nĐã lưu tại: " + filePath, "Thành công", JOptionPane.INFORMATION_MESSAGE);

            } catch (DocumentException | IOException e) {
                JOptionPane.showMessageDialog(null, "Lỗi khi tạo file PDF: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Hàm xuất 1 dòng dữ liệu từ JTable ra file PDF
     */
    public static void xuatPDF1Dong(JTable table, int row, String title) {
        if (row < 0 || row >= table.getRowCount()) {
            JOptionPane.showMessageDialog(null, "Dòng được chọn không hợp lệ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file PDF cho dòng này");
        fileChooser.setSelectedFile(new File("TaiLieu_1Dong.pdf"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }

            try {
                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                Font titleFont, headerFont, normalFont, italicFont;
                try {
                    BaseFont bf = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    titleFont = new Font(bf, 18, Font.BOLD, BaseColor.BLUE);
                    headerFont = new Font(bf, 12, Font.BOLD, BaseColor.WHITE);
                    normalFont = new Font(bf, 12, Font.NORMAL, BaseColor.BLACK);
                    italicFont = new Font(bf, 12, Font.ITALIC, BaseColor.DARK_GRAY);
                } catch (Exception e) {
                    titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                    headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                    normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
                    italicFont = new Font(Font.FontFamily.HELVETICA, 12, Font.ITALIC);
                }

                Paragraph pdfTitle = new Paragraph(title, titleFont);
                pdfTitle.setAlignment(Element.ALIGN_CENTER);
                pdfTitle.setSpacingAfter(5);
                document.add(pdfTitle);

                String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                Paragraph pdfDate = new Paragraph("Ngày xuất: " + currentDate, italicFont);
                pdfDate.setAlignment(Element.ALIGN_RIGHT);
                pdfDate.setSpacingAfter(20);
                document.add(pdfDate);

                int colCountToExport = table.getColumnCount() - 1;
                TableModel model = table.getModel();
                PdfPTable pdfTable = new PdfPTable(colCountToExport);

                // --- TÍNH TOÁN ĐỘ RỘNG CHO XUẤT 1 DÒNG ---
                float[] columnWidths = new float[colCountToExport];
                for (int col = 0; col < colCountToExport; col++) {
                    int maxLength = model.getColumnName(col).length();
                    Object val = model.getValueAt(row, col);
                    if (val != null) {
                        int cellLength = val.toString().length();
                        if (cellLength > maxLength) {
                            maxLength = cellLength;
                        }
                    }
                    columnWidths[col] = (maxLength * 10f) + 20f;
                }

                // LƯU Ý QUAN TRỌNG: Gán Widths TRƯỚC, ép 100% trang SAU
                pdfTable.setWidths(columnWidths);
                pdfTable.setWidthPercentage(100);
                // ------------------------------------------

                for (int i = 0; i < colCountToExport; i++) {
                    PdfPCell headerCell = new PdfPCell(new Phrase(model.getColumnName(i), headerFont));
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerCell.setBackgroundColor(new BaseColor(33, 150, 243));
                    headerCell.setPadding(8);
                    pdfTable.addCell(headerCell);
                }

                for (int j = 0; j < colCountToExport; j++) {
                    Object val = model.getValueAt(row, j);
                    String text = (val == null) ? "" : val.toString();
                    PdfPCell dataCell = new PdfPCell(new Phrase(text, normalFont));
                    dataCell.setPadding(5);
                    pdfTable.addCell(dataCell);
                }

                document.add(pdfTable);
                document.close();

                JOptionPane.showMessageDialog(null, "Xuất dòng " + (row + 1) + " thành công!\nĐã lưu tại: " + filePath, "Thành công", JOptionPane.INFORMATION_MESSAGE);

            } catch (DocumentException | IOException e) {
                JOptionPane.showMessageDialog(null, "Lỗi khi tạo file PDF: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }
}