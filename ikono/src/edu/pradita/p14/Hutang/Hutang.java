package application;

public class Hutang {
    private final int id;
    private final String nama;
    private final String jenisKelamin;
    private final String jumlah;
    private final String status;
    private final String tanggal;

    public Hutang(int id, String nama, String jenisKelamin, String jumlah, String status, String tanggal) {
        this.id = id;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.jumlah = jumlah;
        this.status = status;
        this.tanggal = tanggal;
    }

    public int getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public String getJumlah() {
        return jumlah;
    }

    public String getStatus() {
        return status;
    }

    public String getTanggal() {
        return tanggal;
    }
}
