
package entity;

import java.math.BigDecimal;

public class Layanan {

    private int id;
    private String namaLayanan;
    private String kategori;
    private String satuan;
    private BigDecimal harga;
    private boolean aktif;

    public Layanan(int id, String namaLayanan, String kategori,
            String satuan, BigDecimal harga, boolean aktif) {
        this.id = id;
        this.namaLayanan = namaLayanan;
        this.kategori = kategori;
        this.satuan = satuan;
        this.harga = harga;
        this.aktif = aktif;
    }

    public int getId() {
        return id;
    }

    public String getNamaLayanan() {
        return namaLayanan;
    }

    public String getKategori() {
        return kategori;
    }

    public String getSatuan() {
        return satuan;
    }

    public BigDecimal getHarga() {
        return harga;
    }

    public boolean isAktif() {
        return aktif;
    }

    @Override
    public String toString() {
        return namaLayanan;
    }
}
