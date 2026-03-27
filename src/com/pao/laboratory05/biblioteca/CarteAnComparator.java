package com.pao.laboratory05.biblioteca;

import java.util.Comparator;

public class CarteAnComparator implements Comparator<Carte> {
    @Override
    public int compare(Carte first, Carte second) {
        return Integer.compare(first.getAn(), second.getAn());
    }
}
