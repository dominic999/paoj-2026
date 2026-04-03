package com.pao.laboratory06.exercise3;

import java.util.Comparator;

public class ComparatorInginerSalariu implements Comparator<Inginer> {
    @Override
    public int compare(Inginer first, Inginer second) {
        return Double.compare(second.getSalariu(), first.getSalariu());
    }
}
