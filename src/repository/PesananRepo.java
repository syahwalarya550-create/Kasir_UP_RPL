package repository;

import database.Koneksi;
import entity.DetailPesanan;
import entity.Pesanan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PesananRepo {
    public String simpan(Pesanan pesanan)
            throws SQLException {

        if (pesanan == null) {
            throw new IllegalArgumentException(
                    "Data pesanan tidak boleh kosong");
        }

        if (pesanan.getDaftarDetail() == null
                || pesanan.getDaftarDetail().isEmpty()) {

            throw new IllegalArgumentException(
                    "Detail pesanan tidak boleh kosong");
        }

        String sqlPesanan =
                "INSERT INTO pesanan "
                + "(kode_pesanan, nama_pelanggan, kelas, "
                + "no_hp, total, catatan) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        String sqlDetail =
                "INSERT INTO detail_pesanan "
                + "(pesanan_id, layanan_id, nama_layanan, "
                + "jumlah, harga, subtotal) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = Koneksi.getKoneksi()) {

            if (conn == null) {
                throw new SQLException(
                        "Koneksi database tidak tersedia");
            }

            conn.setAutoCommit(false);

            try {

                long idPesanan;

                /*
                 * 1. Simpan data utama pesanan.
                 */
                try (PreparedStatement ps =
                        conn.prepareStatement(
                                sqlPesanan,
                                Statement.RETURN_GENERATED_KEYS)) {

                    ps.setString(
                            1,
                            "TMP-" + System.nanoTime());

                    /*
                     * Nama pelanggan opsional.
                     */
                    setNullableString(
                            ps,
                            2,
                            pesanan.getNamaPelanggan());

                    /*
                     * Kelas opsional.
                     */
                    setNullableString(
                            ps,
                            3,
                            pesanan.getKelas());

                    /*
                     * Nomor HP belum digunakan.
                     */
                    ps.setNull(
                            4,
                            Types.VARCHAR);

                    ps.setBigDecimal(
                            5,
                            pesanan.getTotal());

                    /*
                     * Catatan opsional.
                     */
                    setNullableString(
                            ps,
                            6,
                            pesanan.getCatatan());

                    ps.executeUpdate();

                    /*
                     * Ambil ID pesanan yang baru dibuat.
                     */
                    try (ResultSet rs =
                            ps.getGeneratedKeys()) {

                        if (!rs.next()) {
                            throw new SQLException(
                                    "ID pesanan tidak ditemukan");
                        }

                        idPesanan = rs.getLong(1);
                    }
                }

                /*
                 * 2. Buat nomor pesanan otomatis.
                 *
                 * Contoh:
                 * PSN-20261108-001
                 */
                String kodePesanan =
                        "PSN-"
                        + LocalDate.now().format(
                                DateTimeFormatter.ofPattern(
                                        "yyyyMMdd"))
                        + "-"
                        + String.format(
                                "%03d",
                                idPesanan);

                /*
                 * 3. Update nomor pesanan.
                 */
                String sqlUpdate =
                        "UPDATE pesanan "
                        + "SET kode_pesanan = ? "
                        + "WHERE id = ?";

                try (PreparedStatement ps =
                        conn.prepareStatement(sqlUpdate)) {

                    ps.setString(
                            1,
                            kodePesanan);

                    ps.setLong(
                            2,
                            idPesanan);

                    ps.executeUpdate();
                }

                /*
                 * 4. Simpan detail layanan.
                 */
                try (PreparedStatement ps =
                        conn.prepareStatement(sqlDetail)) {

                    for (DetailPesanan detail
                            : pesanan.getDaftarDetail()) {

                        if (detail == null
                                || detail.getLayanan() == null) {

                            throw new SQLException(
                                    "Detail layanan tidak valid");
                        }

                        ps.setLong(
                                1,
                                idPesanan);

                        ps.setInt(
                                2,
                                detail.getLayanan().getId());

                        ps.setString(
                                3,
                                detail.getLayanan()
                                        .getNamaLayanan());

                        ps.setInt(
                                4,
                                detail.getJumlah());

                        ps.setBigDecimal(
                                5,
                                detail.getHarga());

                        ps.setBigDecimal(
                                6,
                                detail.getSubtotal());

                        ps.addBatch();
                    }

                    ps.executeBatch();
                }

                /*
                 * 5. Jika semuanya berhasil,
                 * simpan perubahan secara permanen.
                 */
                conn.commit();

                return kodePesanan;

            } catch (SQLException | RuntimeException e) {

                /*
                 * Jika terjadi kesalahan,
                 * batalkan seluruh penyimpanan.
                 */
                try {
                    conn.rollback();
                } catch (SQLException rollbackError) {
                    e.addSuppressed(rollbackError);
                }

                throw e;

            } finally {

                conn.setAutoCommit(true);
            }
        }
    }

    /**
     * Menyimpan string nullable.
     * String kosong akan disimpan sebagai NULL.
     */
    private void setNullableString(
            PreparedStatement ps,
            int index,
            String value) throws SQLException {

        if (value == null
                || value.trim().isEmpty()) {

            ps.setNull(
                    index,
                    Types.VARCHAR);

        } else {

            ps.setString(
                    index,
                    value.trim());
        }
    }
}
