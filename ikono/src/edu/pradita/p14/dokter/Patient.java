package dokter;

import java.time.LocalDate;

public class Patient {
    private int idPasien;
    private String namaLengkap;
    private LocalDate tanggalLahir;
    private String jenisKelamin;
    private String alamat;
    private String nomorTelepon;
    private String email;
    private String golonganDarah;
    private String statusPernikahan;
    private String riwayatPenyakit;
    private String alergi;
    private LocalDate tanggalRegistrasi;

    // Konstruktor
    public Patient(int idPasien, String namaLengkap, LocalDate tanggalLahir, String jenisKelamin, String alamat, 
                   String nomorTelepon, String email, String golonganDarah, String statusPernikahan, 
                   String riwayatPenyakit, String alergi, LocalDate tanggalRegistrasi) {
        this.idPasien = idPasien;
        this.namaLengkap = namaLengkap;
        this.tanggalLahir = tanggalLahir;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.nomorTelepon = nomorTelepon;
        this.email = email;
        this.golonganDarah = golonganDarah;
        this.statusPernikahan = statusPernikahan;
        this.riwayatPenyakit = riwayatPenyakit;
        this.alergi = alergi;
        this.tanggalRegistrasi = tanggalRegistrasi;
    }

    // Getter dan Setter
    public int getIdPasien() {
        return idPasien;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getNomorTelepon() {
        return nomorTelepon;
    }

    public String getEmail() {
        return email;
    }

    public String getGolonganDarah() {
        return golonganDarah;
    }

    public String getStatusPernikahan() {
        return statusPernikahan;
    }

    public String getRiwayatPenyakit() {
        return riwayatPenyakit;
    }

    public String getAlergi() {
        return alergi;
    }

    public LocalDate getTanggalRegistrasi() {
        return tanggalRegistrasi;
    }
}
