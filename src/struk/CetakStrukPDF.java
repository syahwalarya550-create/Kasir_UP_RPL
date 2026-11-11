
package struk;

import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.Image;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Rectangle;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

public final class CetakStrukPDF {

    private CetakStrukPDF() {
    }

    private static final float MM = 72f / 25.4f;

    private static final Font FONT_JUDUL =
            new Font(Font.HELVETICA, 9, Font.BOLD);

    private static final Font FONT_NORMAL =
            new Font(Font.HELVETICA, 7, Font.NORMAL);

    private static final Font FONT_KECIL =
            new Font(Font.HELVETICA, 6, Font.NORMAL);

    public static void cetak(
            Component parent,
            String nomorPesanan,
            LocalDateTime tanggalPesanan,
            JTable tabelLayanan,
            BigDecimal total,
            String namaUP,
            String logoResource,
            String namaPelanggan,
            String kelas,
            boolean tampilIdentitas
    ) {

        // VALIDASI DATA
        if (nomorPesanan == null
                || tanggalPesanan == null
                || tabelLayanan == null
                || total == null) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Data struk belum lengkap!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (tabelLayanan.getModel().getRowCount() == 0) {

            JOptionPane.showMessageDialog(
                    parent,
                    "Tidak ada layanan untuk dicetak!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // KONFIRMASI PENYIMPANAN
        int konfirmasi = JOptionPane.showConfirmDialog(
                parent,
                "Apakah Anda ingin menyimpan struk ke folder komputer?",
                "Simpan Struk PDF",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (konfirmasi != JOptionPane.YES_OPTION) {
            return;
        }

        // PILIH FOLDER DAN NAMA FILE
        JFileChooser pilihFile = new JFileChooser();

        pilihFile.setDialogTitle("Pilih Folder Penyimpanan Struk");
        pilihFile.setFileSelectionMode(JFileChooser.FILES_ONLY);

        pilihFile.setSelectedFile(
                new File("Struk_" + nomorPesanan + ".pdf")
        );

        if (pilihFile.showSaveDialog(parent)
                != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = pilihFile.getSelectedFile();

        if (!file.getName().toLowerCase(Locale.ROOT).endsWith(".pdf")) {
            file = new File(file.getAbsolutePath() + ".pdf");
        }

        // CEK FILE SUDAH ADA
        if (file.exists()) {

            int ganti = JOptionPane.showConfirmDialog(
                    parent,
                    "File sudah ada. Apakah ingin menggantinya?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION
            );

            if (ganti != JOptionPane.YES_OPTION) {
                return;
            }
        }

        // IDENTITAS PELANGGAN
        if (tampilIdentitas) {

            namaPelanggan = (namaPelanggan == null
                    || namaPelanggan.trim().isEmpty())
                    ? "-"
                    : namaPelanggan.trim();

            kelas = (kelas == null
                    || kelas.trim().isEmpty())
                    ? "-"
                    : kelas.trim();

        }

        Document document = null;

        try {

            TableModel model = tabelLayanan.getModel();
            int jumlahBaris = model.getRowCount();

            // UKURAN KERTAS 58 MM
            float lebar = 58 * MM;

            float tinggi = (tampilIdentitas ? 105 : 90)
                    * MM + jumlahBaris * 14 * MM;

            Rectangle ukuranKertas =
                    new Rectangle(lebar, tinggi);

            document = new Document(
                    ukuranKertas,
                    3 * MM,
                    3 * MM,
                    3 * MM,
                    3 * MM
            );

            PdfWriter.getInstance(
                    document,
                    new FileOutputStream(file)
            );

            document.open();

            // LOGO
            if (logoResource != null
                    && !logoResource.trim().isEmpty()) {

                java.net.URL url =
                        CetakStrukPDF.class.getResource(logoResource);

                if (url != null) {

                    Image logo = Image.getInstance(url);

                    logo.scaleToFit(18 * MM, 12 * MM);
                    logo.setAlignment(Element.ALIGN_CENTER);

                    document.add(logo);
                }
            }

            // HEADER
            Paragraph judul = new Paragraph(
                    namaUP,
                    FONT_JUDUL
            );

            judul.setAlignment(Element.ALIGN_CENTER);
            document.add(judul);

            Paragraph subjudul = new Paragraph(
                    "UNIT PRODUKSI RPL",
                    FONT_NORMAL
            );

            subjudul.setAlignment(Element.ALIGN_CENTER);
            document.add(subjudul);

            Paragraph garis = new Paragraph(
                    "--------------------------------",
                    FONT_KECIL
            );

            garis.setAlignment(Element.ALIGN_CENTER);
            document.add(garis);

            // INFORMASI PESANAN
            document.add(new Paragraph(
                    "No. Pesanan: " + nomorPesanan,
                    FONT_NORMAL
            ));

            String tanggalFormat = tanggalPesanan.format(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
            );

            document.add(new Paragraph(
                    "Tanggal: " + tanggalFormat,
                    FONT_NORMAL
            ));

            // IDENTITAS HANYA JIKA DIPILIH
            if (tampilIdentitas) {

                document.add(new Paragraph(
                        "Nama Pelanggan: " + namaPelanggan,
                        FONT_NORMAL
                ));

                document.add(new Paragraph(
                        "Kelas: " + kelas,
                        FONT_NORMAL
                ));
            }

            document.add(new Paragraph(" "));

            // TABEL LAYANAN
            PdfPTable tabel = new PdfPTable(
                    new float[]{4.5f, 1f, 2.2f, 2.2f}
            );

            tabel.setWidthPercentage(100);
            tabel.setSpacingBefore(2f);
            tabel.setSpacingAfter(2f);
            tabel.setSplitRows(true);

            tambahHeader(tabel, "Layanan", FONT_KECIL);
            tambahHeader(tabel, "Qty", FONT_KECIL);
            tambahHeader(tabel, "Harga", FONT_KECIL);
            tambahHeader(tabel, "Subtotal", FONT_KECIL);

            for (int i = 0; i < jumlahBaris; i++) {

                String nama = String.valueOf(
                        model.getValueAt(i, 0)
                );

                String jumlah = String.valueOf(
                        model.getValueAt(i, 1)
                );

                String harga = String.valueOf(
                        model.getValueAt(i, 2)
                );

                String subtotal = String.valueOf(
                        model.getValueAt(i, 3)
                );

                tabel.addCell(buatCell(
                        nama, FONT_KECIL, Element.ALIGN_LEFT
                ));

                tabel.addCell(buatCell(
                        jumlah, FONT_KECIL, Element.ALIGN_CENTER
                ));

                tabel.addCell(buatCell(
                        harga, FONT_KECIL, Element.ALIGN_RIGHT
                ));

                tabel.addCell(buatCell(
                        subtotal, FONT_KECIL, Element.ALIGN_RIGHT
                ));
            }

            document.add(tabel);

            // TOTAL
            Paragraph garisTotal = new Paragraph(
                    "--------------------------------",
                    FONT_KECIL
            );

            garisTotal.setAlignment(Element.ALIGN_CENTER);
            document.add(garisTotal);

            Paragraph totalParagraph = new Paragraph(
                    "TOTAL: " + formatRupiah(total),
                    FONT_JUDUL
            );

            totalParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(totalParagraph);

            // PENUTUP
            Paragraph terimaKasih = new Paragraph(
                    "\nTerima kasih telah menggunakan\n"
                    + "Unit Produksi RPL!\n"
                    + "Semoga puas dengan layanan kami.",
                    FONT_NORMAL
            );

            terimaKasih.setAlignment(Element.ALIGN_CENTER);
            document.add(terimaKasih);

            // SELESAI
            document.close();
            document = null;

            JOptionPane.showMessageDialog(
                    parent,
                    "Struk PDF berhasil disimpan!\n\n"
                    + "Lokasi: " + file.getAbsolutePath(),
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // BUKA PDF OTOMATIS
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(file);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(
                            parent,
                            "PDF berhasil disimpan, tetapi tidak dapat dibuka otomatis.\n"
                            + e.getMessage(),
                            "Informasi",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }

        } catch (Exception e) {

            if (document != null) {
                try {
                    document.close();
                } catch (Exception ignored) {
                }
            }

            JOptionPane.showMessageDialog(
                    parent,
                    "Gagal membuat PDF:\n" + e.getMessage(),
                    "Kesalahan",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private static void tambahHeader(
            PdfPTable tabel,
            String teks,
            Font font
    ) {

        PdfPCell cell = buatCell(
                teks,
                font,
                Element.ALIGN_CENTER
        );

        cell.setBackgroundColor(
                new java.awt.Color(230, 230, 230)
        );

        tabel.addCell(cell);
    }

    private static PdfPCell buatCell(
            String teks,
            Font font,
            int alignment
    ) {

        PdfPCell cell = new PdfPCell(
                new Paragraph(teks, font)
        );

        cell.setPadding(2f);
        cell.setHorizontalAlignment(alignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        return cell;
    }

    private static String formatRupiah(BigDecimal nilai) {

        NumberFormat format =
                NumberFormat.getCurrencyInstance(
                        new Locale("id", "ID")
                );

        format.setMaximumFractionDigits(0);
        format.setMinimumFractionDigits(0);

        return format.format(nilai);
    }
}
