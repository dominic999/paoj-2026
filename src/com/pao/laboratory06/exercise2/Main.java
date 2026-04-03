package com.pao.laboratory06.exercise2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        Scanner in = new Scanner(System.in);
//        int n = in.nextInt();
//        List<Colaborator> colaboratori = new ArrayList<>();
//        for (int i = 0; i < n; i++) {
//            String tip = in.next();
//            Colaborator c = switch (tip) {
//                case "CIM" -> {
//                    CIMColaborator obj = new CIMColaborator();
//                    obj.citeste(in);
//                    yield obj;
//                }
//                case "PFA" -> {
//                    PFAColaborator obj = new PFAColaborator();
//                    obj.citeste(in);
//                    yield obj;
//                }
//                case "SRL" -> {
//                    SRLColaborator obj = new SRLColaborator();
//                    obj.citeste(in);
//                    yield obj;
//                }
//                default -> throw new IllegalArgumentException("Tip necunoscut: " + tip);
//            };
//            colaboratori.add(c);
//        }
//        // Sortează și afișează pe tip, fiecare descrescător după venit net anual
//        for (TipColaborator tipColab : TipColaborator.values()) {
//            colaboratori.stream()
//                    .filter(c -> c.getTip() == tipColab)
//                    .sorted((a, b) -> Double.compare(b.calculeazaVenitNetAnual(), a.calculeazaVenitNetAnual()))
//                    .forEach(Colaborator::afiseaza);
//        }
//        // Colaborator cu venit net maxim
//        Colaborator max = colaboratori.stream().max(Comparator.comparingDouble(Colaborator::calculeazaVenitNetAnual)).orElse(null);
//        System.out.printf("\nColaborator cu venit net maxim: ");
//        if (max != null) max.afiseaza();
//        // Colaboratori persoane juridice (SRL)
//        System.out.println("\nColaboratori persoane juridice:");
//        colaboratori.stream()
//                .filter(c -> c instanceof PersoanaJuridica)
//                .sorted((a, b) -> Double.compare(b.calculeazaVenitNetAnual(), a.calculeazaVenitNetAnual()))
//                .forEach(Colaborator::afiseaza);
//        // Sume și număr colaboratori pe tip
//        System.out.println("\nSume și număr colaboratori pe tip:");
//        Map<TipColaborator, Double> suma = new EnumMap<>(TipColaborator.class);
//        Map<TipColaborator, Integer> numar = new EnumMap<>(TipColaborator.class);
//        var typesOfCollaborators = new HashSet<TipColaborator>();
//        for (Colaborator c : colaboratori) {
//            typesOfCollaborators.add(c.getTip());
//        }
//        for (TipColaborator t : typesOfCollaborators) {
//            suma.put(t, 0.0);
//            numar.put(t, 0);
//        }
//        for (Colaborator c : colaboratori) {
//            TipColaborator t = c.getTip();
//            suma.put(t, suma.get(t) + c.calculeazaVenitNetAnual());
//            numar.put(t, numar.get(t) + 1);
//        }
//        for (TipColaborator t : TipColaborator.values()) {
//            System.out.printf("%s: suma = %.2f lei, număr = %d\n", t, suma.get(t), numar.get(t));
//        }
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        List<Colaborator> colaboratori = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String tip = in.next();
            Colaborator colaborator = switch (tip) {
                case "CIM" -> new CIMColaborator();
                case "PFA" -> new PFAColaborator();
                case "SRL" -> new SRLColaborator();
                default -> throw new IllegalArgumentException("Tip necunoscut: " + tip);
            };
            colaborator.citeste(in);
            colaboratori.add(colaborator);
        }

        Comparator<Colaborator> dupaVenitNet = Comparator.comparingDouble(Colaborator::calculeazaVenitNetAnual)
                .reversed();
        List<String> linii = new ArrayList<>();

        for (TipColaborator tip : TipColaborator.values()) {
            colaboratori.stream()
                    .filter(colaborator -> colaborator.getTip() == tip)
                    .sorted(dupaVenitNet)
                    .map(Colaborator::formatAfisare)
                    .forEach(linii::add);
        }

        linii.add("");

        Colaborator maxim = colaboratori.stream()
                .max(Comparator.comparingDouble(Colaborator::calculeazaVenitNetAnual))
                .orElse(null);
        if (maxim == null) {
            linii.add("Colaborator cu venit net maxim: ");
        } else {
            linii.add("Colaborator cu venit net maxim: " + maxim.formatAfisare());
        }

        linii.add("");
        linii.add("Colaboratori persoane juridice:");
        colaboratori.stream()
                .filter(PersoanaJuridica.class::isInstance)
                .sorted(dupaVenitNet)
                .map(Colaborator::formatAfisare)
                .forEach(linii::add);

        linii.add("");
        linii.add("Sume și număr colaboratori pe tip:");
        Map<TipColaborator, Double> sume = new EnumMap<>(TipColaborator.class);
        Map<TipColaborator, Integer> numere = new EnumMap<>(TipColaborator.class);
        for (Colaborator colaborator : colaboratori) {
            TipColaborator tip = colaborator.getTip();
            sume.put(tip, sume.getOrDefault(tip, 0.0) + colaborator.calculeazaVenitNetAnual());
            numere.put(tip, numere.getOrDefault(tip, 0) + 1);
        }
        for (TipColaborator tip : TipColaborator.values()) {
            Double suma = sume.get(tip);
            Integer numar = numere.get(tip);
            String sumaFormatata = suma == null ? "nu" : String.format("%.2f", suma);
            linii.add(String.format("%s: suma = %s lei, număr = %s", tip, sumaFormatata, numar));
        }
        System.out.print(String.join("\n", linii));
    }
}
