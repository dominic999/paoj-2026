package com.pao.laboratory06.exercise3;

public abstract class Persoana {
    private final String nume;
    private final String prenume;
    private final String telefon;

    protected Persoana(String nume, String prenume, String telefon) {
        this.nume = valideazaText(nume, "nume");
        this.prenume = valideazaText(prenume, "prenume");
        this.telefon = telefon;
    }

    protected static String valideazaText(String text, String camp) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(camp + " invalid");
        }
        return text;
    }

    public String getNume() {
        return nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getNumeComplet() {
        return nume + " " + prenume;
    }
}
