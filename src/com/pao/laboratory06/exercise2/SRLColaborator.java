package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class SRLColaborator extends PersoanaJuridica {
    private double cheltuieliLunare;

    public SRLColaborator() {
        super(TipColaborator.SRL);
    }

    @Override
    public void citeste(Scanner in) {
        setIdentitate(in.next(), in.next());
        setVenitBrutLunar(in.nextDouble());
        cheltuieliLunare = in.nextDouble();
    }

    @Override
    public double calculeazaVenitNetAnual() {
        return (getVenitBrutLunar() - cheltuieliLunare) * 12 * 0.84;
    }
}
