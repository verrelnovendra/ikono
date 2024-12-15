package edu.pradita.p14.piutang;

public class Pelunasan {
	private int idPelunasan;
    private String namaLengkap;
    private double jumlahBayar;
    private String tanggalBayar;
    
    public Pelunasan(int idPelunasan, String namaLengkap, double jumlahBayar, String tanggalBayar) {
    	this.idPelunasan = idPelunasan;
    	this.namaLengkap = namaLengkap;
    	this.jumlahBayar = jumlahBayar;
    	this.tanggalBayar = tanggalBayar;
	}

    public int getIdPelunasan() {
    	return idPelunasan;
    }
    public String getIdPiutang() {
    	return namaLengkap;
    }
    public double getJumlahBayar() {
    	return jumlahBayar;
    }
    public String getTanggalBayar() {
    	return tanggalBayar;
    }
}
