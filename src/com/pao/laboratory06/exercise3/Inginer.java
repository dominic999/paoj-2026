package com.pao.laboratory06.exercise3;

public class Inginer extends Angajat implements PlataOnline, Comparable<Inginer> {
    private double sold;
    private boolean autentificat;

    public Inginer(String nume, String prenume, String telefon, double salariu, double sold) {
        super(nume, prenume, telefon, salariu);
        this.sold = sold;
    }

    @Override
    public void autentificare(String user, String parola) {
        Persoana.valideazaText(user, "user");
        Persoana.valideazaText(parola, "parola");
        autentificat = true;
    }

    @Override
    public double consultareSold() {
        return sold;
    }

    @Override
    public boolean efectuarePlata(double suma) {
        if (!autentificat || suma <= 0 || suma > sold) {
            return false;
        }
        sold -= suma;
        return true;
    }

    @Override
    public int compareTo(Inginer other) {
        int rezultat = getNume().compareTo(other.getNume());
        if (rezultat != 0) {
            return rezultat;
        }
        return getPrenume().compareTo(other.getPrenume());
    }

    @Override
    public String toString() {
        return String.format("Inginer %s, salariu %.2f, sold %.2f", getNumeComplet(), getSalariu(), sold);
    }
}
