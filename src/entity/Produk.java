
package entity;

public class Produk {

    private int id;
    private String namaProduk;
    private String kategori;
    private double harga;
    private int stok;
    private String satuan;

    public Produk(int id, String namaProduk, String kategori,
                  double harga, int stok, String satuan) {
        this.id = id;
        this.namaProduk = namaProduk;
        this.kategori = kategori;
        this.harga = harga;
        this.stok = stok;
        this.satuan = satuan;
    }

    public int getId() {
        return id;
    }

    public String getNamaProduk() {
        return namaProduk;
    }

    public String getKategori() {
        return kategori;
    }

    public double getHarga() {
        return harga;
    }

    public int getStok() {
        return stok;
    }

    public String getSatuan() {
        return satuan;
    }

    public String getStatusStok() {
        if (stok <= 0) {
            return "Habis";
        } else if (stok <= 5) {
            return "Menipis";
        }
        return "Tersedia";
    }
}
