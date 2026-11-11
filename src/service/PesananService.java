package service;

import entity.DetailPesanan;
import entity.Layanan;
import entity.Pesanan;
import repository.LayananRepo;
import repository.PesananRepo;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class PesananService {

    private final LayananRepo layananRepository =
            new LayananRepo();

    private final PesananRepo pesananRepository =
            new PesananRepo();
    public List<Layanan> getSemuaLayanan()
            throws SQLException {

        return layananRepository.getSemuaLayanan();
    }
    public DetailPesanan buatDetail(
            Layanan layanan,
            int jumlah) {

        if (layanan == null) {
            throw new IllegalArgumentException(
                    "Pilih layanan terlebih dahulu");
        }

        if (jumlah < 1) {
            throw new IllegalArgumentException(
                    "Jumlah minimal 1");
        }

        return new DetailPesanan(layanan, jumlah);
    }

    /**
     * Menghitung subtotal layanan.
     */
    public BigDecimal hitungSubtotal(
            Layanan layanan,
            int jumlah) {

        return buatDetail(layanan, jumlah)
                .getSubtotal();
    }

    /**
     * Menyimpan pesanan ke database.
     *
     * Nama pelanggan dan kelas bersifat opsional.
     */
    public String simpanPesanan(Pesanan pesanan)
        throws SQLException {

    if (pesanan == null) {
        throw new IllegalArgumentException(
                "Data pesanan tidak boleh kosong");
    }

    if (pesanan.getDaftarDetail().isEmpty()) {
        throw new IllegalArgumentException(
                "Tambahkan minimal satu layanan");
    }

    // Nama pelanggan opsional
    if (pesanan.getNamaPelanggan() != null
            && pesanan.getNamaPelanggan().trim().isEmpty()) {

        pesanan.setNamaPelanggan(null);
    }

    // Kelas opsional
    if (pesanan.getKelas() != null
            && pesanan.getKelas().trim().isEmpty()) {

        pesanan.setKelas(null);
    }

    // Catatan opsional
    if (pesanan.getCatatan() != null
            && pesanan.getCatatan().trim().isEmpty()) {

        pesanan.setCatatan(null);
    }

    return pesananRepository.simpan(pesanan);
}
}
