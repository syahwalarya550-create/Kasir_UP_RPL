package entity;

import java.math.BigDecimal;

public class DetailPesanan {

    private final Layanan layanan;
    private int jumlah;
    private final BigDecimal harga;

    public DetailPesanan(Layanan layanan, int jumlah) {

        if (layanan == null) {
            throw new IllegalArgumentException(
                    "Layanan tidak boleh kosong");
        }

        if (jumlah < 1) {
            throw new IllegalArgumentException(
                    "Jumlah minimal 1");
        }

        this.layanan = layanan;
        this.jumlah = jumlah;
        this.harga = layanan.getHarga();
    }

    public Layanan getLayanan() {
        return layanan;
    }

    public int getJumlah() {
        return jumlah;
    }

    public BigDecimal getHarga() {
        return harga;
    }

    public BigDecimal getSubtotal() {
        return harga.multiply(
                BigDecimal.valueOf(jumlah));
    }

    public void setJumlah(int jumlah) {

        if (jumlah < 1) {
            throw new IllegalArgumentException(
                    "Jumlah minimal 1");
        }

        this.jumlah = jumlah;
    }
}