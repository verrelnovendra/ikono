package edu.pradita.p14.Tax;

public class Tax {
    private int taxID;
    private String taxName;
    private double taxRate;

    public Tax(int taxID, String taxName, double taxRate) {
        this.taxID = taxID;
        this.taxName = taxName;
        this.taxRate = taxRate;
    }

    public int getTaxID() {
        return taxID;
    }

    public String getTaxName() {
        return taxName;
    }

    public double getTaxRate() {
        return taxRate;
    }
}