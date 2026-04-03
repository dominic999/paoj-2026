package com.pao.laboratory06.exercise3;

import java.util.ArrayList;
import java.util.List;

public class PersoanaJuridica extends Persoana implements PlataOnlineSMS {
    private final List<String> smsTrimise = new ArrayList<>();
    private double sold;
    private boolean autentificat;

    public PersoanaJuridica(String nume, String prenume, String telefon, double sold) {
        super(nume, prenume, telefon);
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
    public boolean trimiteSMS(String mesaj) {
        if (mesaj == null || mesaj.isBlank()) {
            return false;
        }
        if (getTelefon() == null || getTelefon().isBlank()) {
            return false;
        }
        smsTrimise.add(mesaj);
        return true;
    }

    public List<String> getSmsTrimise() {
        return List.copyOf(smsTrimise);
    }

    @Override
    public String toString() {
        return String.format("PersoanaJuridica %s, sold %.2f", getNumeComplet(), sold);
    }
}
