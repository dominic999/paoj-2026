package com.pao.laboratory06.exercise2;

public abstract class Colaborator implements IOperatiiCitireScriere {
    private final TipColaborator tip;
    private String nume;
    private String prenume;
    private double venitBrutLunar;

    protected Colaborator(TipColaborator tip) {
        this.tip = tip;
    }

    public abstract double calculeazaVenitNetAnual();

    @Override
    public void afiseaza() {
        System.out.println(formatAfisare());
    }

    public String formatAfisare() {
        return String.format("%s: %s %s, venit net anual: %.2f lei",
                tipContract(), nume, prenume, calculeazaVenitNetAnual());
    }

    @Override
    public String tipContract() {
        return tip.name();
    }

    public TipColaborator getTip() {
        return tip;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public double getVenitBrutLunar() {
        return venitBrutLunar;
    }

    protected void setIdentitate(String nume, String prenume) {
        this.nume = nume;
        this.prenume = prenume;
    }

    protected void setVenitBrutLunar(double venitBrutLunar) {
        this.venitBrutLunar = venitBrutLunar;
    }
}
