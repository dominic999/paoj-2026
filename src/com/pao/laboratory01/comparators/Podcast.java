package com.pao.laboratory01.comparators;

import java.util.Arrays;
import java.util.Comparator;

public class Podcast implements Comparable<Podcast> {
    private int durata;
    private String titlu;

    public Podcast(String titlu, int durata) {
        this.titlu = titlu;
        this.durata = durata;
    }

    public int getDurata() {
        return this.durata;
    }

    public String getTitlu() {
        return this.titlu;
    }

    @Override
    public String toString() {
        return "Podcast " + titlu + ", durata: " + durata;
    }

    @Override
    public int compareTo(Podcast o) {
        return this.titlu.compareTo(o.titlu);
    }

    public static void main(String[] args) {
        Podcast[] podcasts = {
            new Podcast("Tech Talk", 2400),
            new Podcast("Arta Conversatiei", 3600),
            new Podcast("Mindset", 1800)
        };

        Arrays.sort(podcasts);
        System.out.println("Sortate dupa titlu:");
        System.out.println(Arrays.toString(podcasts));

        Arrays.sort(podcasts, new PodcastLengthComparator());
        System.out.println("Sortate dupa durata (crescator):");
        System.out.println(Arrays.toString(podcasts));



        PodcastLengthComparator comp = new PodcastLengthComparator();
        Arrays.sort(podcasts, comp.reversed());

        System.out.println("Sortate dupa durata (descrescator, lambda):");
        System.out.println(Arrays.toString(podcasts));
    }
}

class PodcastLengthComparator implements Comparator<Podcast> {
    @Override
    public int compare(Podcast p1, Podcast p2) {
        return Integer.compare(p1.getDurata(), p2.getDurata());
    }
}
