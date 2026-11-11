package entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Pesanan {

    private String namaPelanggan;
    private String kelas;
    private String catatan;

    private final List<DetailPesanan> daftarDetail =
            new ArrayList<>();

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public List<DetailPesanan> getDaftarDetail() {
        return daftarDetail;
    }

    public void tambahDetail(DetailPesanan detail) {
        daftarDetail.add(detail);
    }

    public void hapusDetail(int index) {
        if (index >= 0 && index < daftarDetail.size()) {
            daftarDetail.remove(index);
        }
    }

    public void kosongkanDetail() {
        daftarDetail.clear();
    }

    public BigDecimal getTotal() {

        BigDecimal total = BigDecimal.ZERO;

        for (DetailPesanan detail : daftarDetail) {
            total = total.add(detail.getSubtotal());
        }

        return total;
    }

    public DetailPesanan cariDetail(Layanan layanan) {

        if (layanan == null) {
            return null;
        }

        for (DetailPesanan detail : daftarDetail) {

            if (detail.getLayanan().getId()
                    == layanan.getId()
                    && detail.getHarga().compareTo(
                            layanan.getHarga()) == 0) {

                return detail;
            }
        }

        return null;
    }
}