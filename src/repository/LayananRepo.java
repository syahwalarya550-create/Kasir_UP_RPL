
package repository;

import database.Koneksi;
import entity.Layanan;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LayananRepo {

    public List<Layanan> getSemuaLayanan() throws SQLException {

        List<Layanan> daftar = new ArrayList<>();

        String sql = "SELECT id, nama_layanan, kategori, satuan, "
                + "harga, aktif FROM layanan "
                + "WHERE aktif = 1 ORDER BY nama_layanan";

        try (Connection conn = Koneksi.getKoneksi();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                daftar.add(new Layanan(
                        rs.getInt("id"),
                        rs.getString("nama_layanan"),
                        rs.getString("kategori"),
                        rs.getString("satuan"),
                        rs.getBigDecimal("harga"),
                        rs.getBoolean("aktif")
                ));
            }
        }

        return daftar;
    }
}
