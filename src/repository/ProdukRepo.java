
package repository;

import database.Koneksi;
import entity.Produk;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdukRepo {

    // Menampilkan dan mencari barang
    public List<Produk> cariProduk(String keyword, String kategori)
            throws SQLException {

        List<Produk> daftar = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT id, nama_layanan, kategori, harga, stok, satuan "
            + "FROM layanan "
            + "WHERE aktif = 1 AND nama_layanan LIKE ? "
        );

        if (kategori != null && !kategori.equals("Semua Kategori")) {
            sql.append("AND kategori = ? ");
        }

        sql.append("ORDER BY nama_layanan ASC");

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            ps.setString(1, "%" + keyword + "%");

            if (kategori != null && !kategori.equals("Semua Kategori")) {
                ps.setString(2, kategori);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    daftar.add(new Produk(
                        rs.getInt("id"),
                        rs.getString("nama_layanan"),
                        rs.getString("kategori"),
                        rs.getDouble("harga"),
                        rs.getInt("stok"),
                        rs.getString("satuan")
                    ));
                }
            }
        }

        return daftar;
    }

    // Mengambil semua barang aktif
    public List<Produk> getSemuaProduk() throws SQLException {
        return cariProduk("", "Semua Kategori");
    }

    // Menghitung jumlah barang aktif
    public int getTotalBarang() throws SQLException {

        String sql = "SELECT COUNT(*) FROM layanan WHERE aktif = 1";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }

    // Menghitung total stok barang aktif
    public int getTotalStok() throws SQLException {

        String sql = "SELECT COALESCE(SUM(stok), 0) "
                   + "FROM layanan WHERE aktif = 1";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        return 0;
    }

    // Mengubah stok barang
    public boolean ubahStok(int id, int stok) throws SQLException {

        String sql = "UPDATE layanan SET stok = ? "
                   + "WHERE id = ? AND aktif = 1";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, stok);
            ps.setInt(2, id);

            return ps.executeUpdate() > 0;
        }
    }
}
