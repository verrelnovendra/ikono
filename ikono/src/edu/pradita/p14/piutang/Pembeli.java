package edu.pradita.p14.piutang;

public class Pembeli {
    private int idPembeli;
    private String namaLengkap;
    private String alamat;
    private String kota;
    private String kodePos;
    private String noTelepon;
    private String email;
    private String jenisKelamin;
    private String tanggalDaftar;
    private String status;

    public Pembeli(int idPembeli, String namaLengkap, String alamat, String kota, String kodePos, String noTelepon,
    		String email, String jenisKelamin, String tanggalDaftar, String status) {
    	this.idPembeli = idPembeli;
    	this.namaLengkap = namaLengkap;
    	this.alamat = alamat;
    	this.kota = kota;
    	this.kodePos = kodePos;
    	this.noTelepon = noTelepon;
    	this.email = email;
    	this.jenisKelamin = jenisKelamin;
    	this.tanggalDaftar = tanggalDaftar;
    	this.status = status;
	}

	public int getIdPembeli() {
    	return idPembeli;
    }
    
    public String getNamaLengkap() {
    	return namaLengkap;
    }
    
    public String getAlamat() {
    	return alamat;
    }
    
    public String getKota() {
    	return kota;
    }
    
    public String getKodePos() {
    	return kodePos;
    }
    
    public String getNoTelpon() {
    	return noTelepon;
    }
    
    public String getEmail() {
    	return email;
    }
    
    public String getJenisKelamin() {
    	return jenisKelamin;
    }
    
    public String getTanggalDaftar() {
    	return tanggalDaftar;
    }
    
    public String getStatus() {
    	return status;
    }
}
