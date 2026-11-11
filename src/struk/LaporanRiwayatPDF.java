
package struk;

import entity.Riwayat;
import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.PageSize;
import org.openpdf.text.Paragraph;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfWriter;

public final class LaporanRiwayatPDF {

    private LaporanRiwayatPDF() {
    }

    public static void ekspor(
            Component parent,
            List<Riwayat> daftar,
            String periode) {

        if (daftar == null || daftar.isEmpty()) {
            JOptionPane.showMessageDialog(
                    parent,
                    "Tidak ada riwayat pesanan pada periode tersebut.",
                    "Data Kosong",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int jawab = JOptionPane.showConfirmDialog(
                parent,
                "Ekspor laporan riwayat dengan pilihan:\n\n"
                + periode + "\n\n"
                + "Jumlah pesanan: " + daftar.size()
                + "\nLanjutkan ekspor PDF?",
                "Konfirmasi Ekspor PDF",
                JOptionPane.YES_NO_OPTION
        );

        if (jawab != JOptionPane.YES_OPTION) return;

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan Laporan Riwayat");
        chooser.setSelectedFile(new File("Laporan_Riwayat.pdf"));

        if (chooser.showSaveDialog(parent) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File file = chooser.getSelectedFile();

        if (!file.getName().toLowerCase(Locale.ROOT).endsWith(".pdf")) {
            file = new File(file.getAbsolutePath() + ".pdf");
        }

        if (file.exists()) {
            int ganti = JOptionPane.showConfirmDialog(
                    parent,
                    "File sudah ada. Ganti file tersebut?",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION
            );

            if (ganti != JOptionPane.YES_OPTION) return;
        }

        Document document = new Document(PageSize.A4.rotate(), 30, 30, 30, 30);

        try {
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            Font judul = new Font(Font.HELVETICA, 16, Font.BOLD);
            Font normal = new Font(Font.HELVETICA, 10, Font.NORMAL);
            Font header = new Font(Font.HELVETICA, 9, Font.BOLD);

            Paragraph pJudul = new Paragraph(
                    "LAPORAN RIWAYAT PESANAN",
                    judul
            );
            pJudul.setAlignment(Element.ALIGN_CENTER);
            document.add(pJudul);

            Paragraph pSub = new Paragraph(
                    "KASIR UNIT PRODUKSI RPL",
                    normal
            );
            pSub.setAlignment(Element.ALIGN_CENTER);
            document.add(pSub);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Periode: " + periode, normal));
            document.add(new Paragraph(
                    "Tanggal cetak: "
                    + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(
                            new java.util.Date()
                    ),
                    normal
            ));
            document.add(new Paragraph(" "));

            PdfPTable tabel = new PdfPTable(
                    new float[]{0.7f, 2f, 2.2f, 1.3f, 2f, 1.8f}
            );
            tabel.setWidthPercentage(100);

            String[] kolom = {
                "No", "Nomor Pesanan", "Nama Pelanggan",
                "Kelas", "Tanggal Pesanan", "Total"
            };

            for (String namaKolom : kolom) {
                PdfPCell cell = new PdfPCell(
                        new Paragraph(namaKolom, header)
                );
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(7);
                tabel.addCell(cell);
            }

            NumberFormat rupiah =
                    NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            rupiah.setMaximumFractionDigits(0);

            SimpleDateFormat tanggal =
                    new SimpleDateFormat("dd-MM-yyyy HH:mm");

            java.math.BigDecimal totalSemua =
                    java.math.BigDecimal.ZERO;

            int no = 1;

            for (Riwayat r : daftar) {
                tabel.addCell(String.valueOf(no++));
                tabel.addCell(r.getKodePesanan());
                tabel.addCell(
                        r.getNamaPelanggan() == null
                        ? "-" : r.getNamaPelanggan()
                );
                tabel.addCell(
                        r.getKelas() == null ? "-" : r.getKelas()
                );
                tabel.addCell(
                        r.getTanggalPesan() == null
                        ? "-" : tanggal.format(r.getTanggalPesan())
                );
                tabel.addCell(rupiah.format(r.getTotal()));

                totalSemua = totalSemua.add(r.getTotal());
            }

            document.add(tabel);
            document.add(new Paragraph(" "));
            Paragraph total = new Paragraph(
                    "Total pendapatan pada periode ini: "
                    + rupiah.format(totalSemua),
                    new Font(Font.HELVETICA, 12, Font.BOLD)
            );
            total.setAlignment(Element.ALIGN_RIGHT);
            document.add(total);

            document.close();

            JOptionPane.showMessageDialog(
                    parent,
                    "Laporan PDF berhasil disimpan:\n"
                    + file.getAbsolutePath(),
                    "Berhasil",
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open(file);
                } catch (Exception ignored) {
                }
            }

        } catch (Exception e) {
            if (document.isOpen()) document.close();

            JOptionPane.showMessageDialog(
                    parent,
                    "Gagal membuat PDF:\n" + e.getMessage(),
                    "Kesalahan",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
