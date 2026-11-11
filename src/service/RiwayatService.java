
package service;

import entity.Riwayat;
import repository.RiwayatRepo;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class RiwayatService {

    private final RiwayatRepo repository =
            new RiwayatRepo();

    public List<Riwayat> getSemuaRiwayat()
            throws SQLException {

        return repository.getSemuaRiwayat();
    }

    public List<Riwayat> filterRiwayat(
            Integer tahun,
            Integer bulan,
            Integer minggu,
            Integer hari) throws SQLException {

        return repository.filterRiwayat(
                tahun, bulan, minggu, hari);
    }

    public int getTotalPesanan() throws SQLException {
        return repository.getTotalPesanan();
    }

    public BigDecimal getTotalPendapatan()
            throws SQLException {

        return repository.getTotalPendapatan();
    }
}
