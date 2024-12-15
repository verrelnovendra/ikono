package edu.pradita.p14.piutang;

public class Piutang {
    private int idPiutang;
    private int idPelanggan;
    private String namaPelanggan;
    private double jumlah;
    private String tanggalPinjam;
    private String dueDate;
    private boolean statusLunas;

    public Piutang(int idPiutang, int idPelanggan, String namaPelanggan, double jumlah, String tanggalPinjam, String dueDate,
			boolean statusLunas) {
    	this.idPiutang = idPiutang;
    	this.idPelanggan = idPelanggan;
    	this.namaPelanggan = namaPelanggan;
    	this.jumlah = jumlah;
    	this.tanggalPinjam = tanggalPinjam;
    	this.dueDate = dueDate;
    	this.statusLunas = statusLunas;
	}
    
	public int getIdPiutang() {
    	return idPelanggan;
    }
    public int getIdPelaggan() {
    	return idPelanggan;
    }
    public String getNamaPelanggan() {
    	return namaPelanggan;
    }
    public double getJumlah() {
    	return jumlah;
    }
    public String getTanggalPinjam() {
    	return tanggalPinjam;
    }
    public String getDueDate() {
    	return dueDate;
    }
    public String getStatusLunas() {
    	return (statusLunas) ? "Lunas" : "Belum lunas";
    }
}