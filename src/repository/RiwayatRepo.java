
package repository;

import database.Koneksi;
import entity.Riwayat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RiwayatRepo {

    // ==========================================
    // MENAMPILKAN SEMUA RIWAYAT
    // ==========================================
    public List<Riwayat> getSemuaRiwayat()
            throws SQLException {

        String sql =
                "SELECT id, kode_pesanan, nama_pelanggan, "
                + "kelas, tanggal_pesan, total "
                + "FROM pesanan "
                + "ORDER BY tanggal_pesan DESC, id DESC";

        List<Riwayat> daftar = new ArrayList<>();

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                daftar.add(buatRiwayat(rs));
            }
        }

        return daftar;
    }

    // ==========================================
    // FILTER RIWAYAT TAHUN, BULAN, MINGGU, HARI
    // ==========================================
    public List<Riwayat> filterRiwayat(
            Integer tahun,
            Integer bulan,
            Integer minggu,
            Integer hari) throws SQLException {

        StringBuilder sql = new StringBuilder(
                "SELECT id, kode_pesanan, nama_pelanggan, "
                + "kelas, tanggal_pesan, total "
                + "FROM pesanan WHERE 1=1 "
        );

        List<Integer> parameter = new ArrayList<>();

        // Filter tahun
        if (tahun != null) {
            sql.append("AND YEAR(tanggal_pesan) = ? ");
            parameter.add(tahun);
        }

        // Filter bulan
        if (bulan != null) {
            sql.append("AND MONTH(tanggal_pesan) = ? ");
            parameter.add(bulan);
        }

        // Filter minggu dalam bulan
        // Minggu 1 = tanggal 1-7
        // Minggu 2 = tanggal 8-14
        // Minggu 3 = tanggal 15-21
        // Minggu 4 = tanggal 22-28
        // Minggu 5 = tanggal 29-31
        if (minggu != null) {
            sql.append(
                    "AND CEIL(DAYOFMONTH(tanggal_pesan) / 7.0) = ? "
            );
            parameter.add(minggu);
        }

        // Filter tanggal tertentu
        if (hari != null) {
            sql.append("AND DAYOFMONTH(tanggal_pesan) = ? ");
            parameter.add(hari);
        }

        // Urutkan dari pesanan terbaru
        sql.append(
                "ORDER BY tanggal_pesan DESC, id DESC"
        );

        List<Riwayat> daftar = new ArrayList<>();

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps =
                     conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < parameter.size(); i++) {
                ps.setInt(i + 1, parameter.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    daftar.add(buatRiwayat(rs));
                }
            }
        }

        return daftar;
    }

    // ==========================================
    // TOTAL SELURUH PESANAN
    // ==========================================
    public int getTotalPesanan() throws SQLException {

        String sql = "SELECT COUNT(*) FROM pesanan";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }

    // ==========================================
    // TOTAL SELURUH PENDAPATAN
    // ==========================================
    public java.math.BigDecimal getTotalPendapatan()
            throws SQLException {

        String sql =
                "SELECT COALESCE(SUM(total), 0) FROM pesanan";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        }

        return java.math.BigDecimal.ZERO;
    }

    // ==========================================
    // MENGUBAH RESULTSET MENJADI OBJECT RIWAYAT
    // ==========================================
    private Riwayat buatRiwayat(ResultSet rs)
            throws SQLException {

        return new Riwayat(
                rs.getLong("id"),
                rs.getString("kode_pesanan"),
                rs.getString("nama_pelanggan"),
                rs.getString("kelas"),
                rs.getTimestamp("tanggal_pesan"),
                rs.getBigDecimal("total")
        );
    }
}
