package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class PFAColaborator extends PersoanaFizica {
    private static final double SALARIU_MINIM_BRUT_LUNAR = 4050.0;
    private static final double CASS = SALARIU_MINIM_BRUT_LUNAR * 12 * 0.1;
    private static final double CAS = SALARIU_MINIM_BRUT_LUNAR * 24 * 0.25;

    private double cheltuieliLunare;

    public PFAColaborator() {
        super(TipColaborator.PFA);
    }

    @Override
    public void citeste(Scanner in) {
        setIdentitate(in.next(), in.next());
        setVenitBrutLunar(in.nextDouble());
        cheltuieliLunare = in.nextDouble();
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNetInainteDeTaxe = (getVenitBrutLunar() - cheltuieliLunare) * 12;
        double impozit = venitNetInainteDeTaxe * 0.1;
        return venitNetInainteDeTaxe - impozit - CASS - CAS;
    }
}
