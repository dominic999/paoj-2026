package com.pao.laboratory06.exercise1;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe
        Scanner scanner = new Scanner(System.in);
        String optiune = scanner.next();
        int numarAngajati = scanner.nextInt();
        Angajat[] angajati = new Angajat[numarAngajati];
        for (int i = 0; i < numarAngajati; i++) {
            angajati[i] = Angajat.citeste(scanner);
        }
        // cerinte: sorteaza in functie de optiune
        switch (optiune) {
            case "by_name" -> Arrays.sort(angajati, Comparator.comparing(Angajat::getNume));
            case "by_salary" -> Arrays.sort(angajati);
            case "by_salary_desc" -> Arrays.sort(angajati, Comparator.reverseOrder());
            default -> {
            }
        }
        StringBuilder rezultat = new StringBuilder();
        for (Angajat angajat : angajati) {
            if (!rezultat.isEmpty()) {
                rezultat.append('\n');
            }
            rezultat.append(angajat);
        }
        System.out.print(rezultat);
    }
}
