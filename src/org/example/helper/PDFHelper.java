package org.example.helper;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFHelper {

    /**
     * Hàm xuất dữ liệu từ JTable ra file PDF
     * @param table JTable chứa dữ liệu cần xuất
     * @param title Tiêu đề của file PDF (ví dụ: "BÁO CÁO THỐNG KÊ")
     */
    public static void xuatPDF(JTable table, String title) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn vị trí lưu file PDF");
        // Mặc định tên file
        fileChooser.setSelectedFile(new File("TaiLieu.pdf"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            // Đảm bảo đuôi file là .pdf
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }

            try {
                Document document = new Document(PageSize.A4);
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();

                // 1. Cấu hình Font hỗ trợ Tiếng Việt (Sử dụng font Arial của hệ thống Windows)
                // Lưu ý: Nếu chạy trên Mac/Linux, đường dẫn font này có thể cần thay đổi
                Font titleFont;
                Font headerFont;
                Font normalFont;
                try {
                    BaseFont bf = BaseFont.createFont("c:/windows/fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    titleFont = new Font(bf, 18, Font.BOLD, BaseColor.BLUE);
                    headerFont = new Font(bf, 12, Font.BOLD, BaseColor.WHITE);
                    normalFont = new Font(bf, 12, Font.NORMAL, BaseColor.BLACK);
                } catch (Exception e) {
                    // Fallback nếu không tìm thấy font hệ thống
                    titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                    headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                    normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
                }

                // 2. Thêm Tiêu đề vào tài liệu
                Paragraph pdfTitle = new Paragraph(title, titleFont);
                pdfTitle.setAlignment(Element.ALIGN_CENTER);
                pdfTitle.setSpacingAfter(20);
                document.add(pdfTitle);

                // 3. Khởi tạo Bảng PDF với số cột bằng số cột của JTable
                TableModel model = table.getModel();
                PdfPTable pdfTable = new PdfPTable(model.getColumnCount());
                pdfTable.setWidthPercentage(100);

                // 4. Thêm Header cho bảng
                for (int i = 0; i < model.getColumnCount(); i++) {
                    PdfPCell headerCell = new PdfPCell(new Phrase(model.getColumnName(i), headerFont));
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerCell.setBackgroundColor(new BaseColor(33, 150, 243)); // Màu xanh nước biển
                    headerCell.setPadding(8);
                    pdfTable.addCell(headerCell);
                }

                // 5. Thêm Dữ liệu từng dòng vào bảng
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
}