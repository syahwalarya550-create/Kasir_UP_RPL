
package service;

import entity.Produk;
import repository.ProdukRepo;
import java.sql.SQLException;
import java.util.List;

public class ProdukService {

    private final ProdukRepo repository = new ProdukRepo();

    public List<Produk> getSemuaProduk() throws SQLException {
        return repository.getSemuaProduk();
    }

    public List<Produk> cariProduk(String keyword, String kategori)
            throws SQLException {

        return repository.cariProduk(keyword, kategori);
    }

    public int getTotalBarang() throws SQLException {
        return repository.getTotalBarang();
    }

    public int getTotalStok() throws SQLException {
        return repository.getTotalStok();
    }

    public boolean tambahStok(int id, int jumlah) throws SQLException {
        if (jumlah <= 0) {
            return false;
        }

        Produk produk = cariBerdasarkanId(id);

        if (produk == null) {
            return false;
        }

        return repository.ubahStok(
            id,
            produk.getStok() + jumlah
        );
    }

    public boolean kurangiStok(int id, int jumlah) throws SQLException {
        if (jumlah <= 0) {
            return false;
        }

        Produk produk = cariBerdasarkanId(id);

        if (produk == null || produk.getStok() < jumlah) {
            return false;
        }

        return repository.ubahStok(
            id,
            produk.getStok() - jumlah
        );
    }

    private Produk cariBerdasarkanId(int id) throws SQLException {
        for (Produk produk : repository.getSemuaProduk()) {
            if (produk.getId() == id) {
                return produk;
            }
        }

        return null;
    }
}
