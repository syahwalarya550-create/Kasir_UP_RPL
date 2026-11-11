
package entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Riwayat {

    private long id;
    private String kodePesanan;
    private String namaPelanggan;
    private String kelas;
    private Timestamp tanggalPesan;
    private BigDecimal total;

    public Riwayat() {
    }

    public Riwayat(
            long id,
            String kodePesanan,
            String namaPelanggan,
            String kelas,
            Timestamp tanggalPesan,
            BigDecimal total) {

        this.id = id;
        this.kodePesanan = kodePesanan;
        this.namaPelanggan = namaPelanggan;
        this.kelas = kelas;
        this.tanggalPesan = tanggalPesan;
        this.total = total;
    }

    public long getId() {
        return id;
    }

    public String getKodePesanan() {
        return kodePesanan;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public String getKelas() {
        return kelas;
    }

    public Timestamp getTanggalPesan() {
        return tanggalPesan;
    }

    public BigDecimal getTotal() {
        return total;
    }
}
