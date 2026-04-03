package com.pao.laboratory06.exercise2;

import java.util.Scanner;

public class CIMColaborator extends PersoanaFizica {
    private boolean bonus;

    public CIMColaborator() {
        super(TipColaborator.CIM);
    }

    @Override
    public void citeste(Scanner in) {
        setIdentitate(in.next(), in.next());
        setVenitBrutLunar(in.nextDouble());
        bonus = in.hasNext("DA|NU") && "DA".equals(in.next());
    }

    @Override
    public boolean areBonus() {
        return bonus;
    }

    @Override
    public double calculeazaVenitNetAnual() {
        double venitNet = getVenitBrutLunar() * 12 * 0.55;
        if (bonus) {
            venitNet *= 1.1;
        }
        return venitNet;
    }
}
