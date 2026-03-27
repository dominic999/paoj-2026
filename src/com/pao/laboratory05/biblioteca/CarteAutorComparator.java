package com.pao.laboratory05.biblioteca;

import java.util.Comparator;

public class CarteAutorComparator implements Comparator<Carte> {
    @Override
    public int compare(Carte first, Carte second) {
        return first.getAutor().compareTo(second.getAutor());
    }
}
