package KoreksiStokOutput;

public class Barang {
    private int idBarang;
    private String namaBarang;
    private int stok;
    private String satuan;

    public Barang(int idBarang, String namaBarang, int stok, String satuan) {
        this.idBarang = idBarang;
        this.namaBarang = namaBarang;
        this.stok = stok;
        this.satuan = satuan;
    }

    // Getters and setters
    public int getIdBarang() {
        return idBarang;
    }

    public void setIdBarang(int idBarang) {
        this.idBarang = idBarang;
    }

    public String getNamaBarang() {
        return namaBarang;
    }

    public void setNamaBarang(String namaBarang) {
        this.namaBarang = namaBarang;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }
}