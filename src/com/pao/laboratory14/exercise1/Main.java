package com.pao.laboratory14.exercise1;

import java.util.*;
import java.util.stream.Collector;
import java.util.Locale;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine().trim());
        List<Bilet> bilete = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] parts = sc.nextLine().trim().split("\\s+");
            int id = Integer.parseInt(parts[0]);
            String eveniment = parts[1];
            TipBilet tip = TipBilet.valueOf(parts[2]);
            double pret = Double.parseDouble(parts[3]);
            bilete.add(new Bilet(id, eveniment, tip, pret));
        }

        String comanda = sc.nextLine().trim();

        Collector<Bilet, Map<TipBilet, double[]>, RaportVanzari> collector = Collector.of(
                HashMap::new,
                (acc, bilet) -> {
                    double[] vals = acc.computeIfAbsent(bilet.getTip(), k -> new double[2]);
                    vals[0]++;
                    vals[1] += bilet.getPret();
                },
                (acc1, acc2) -> {
                    acc2.forEach((tip, vals) -> {
                        double[] existing = acc1.computeIfAbsent(tip, k -> new double[2]);
                        existing[0] += vals[0];
                        existing[1] += vals[1];
                    });
                    return acc1;
                },
                acc -> {
                    Map<TipBilet, Long> numarPerTip = new LinkedHashMap<>();
                    Map<TipBilet, Double> incasariPerTip = new LinkedHashMap<>();
                    double total = 0;
                    long count = 0;

                    for (TipBilet tip : TipBilet.values()) {
                        double[] vals = acc.get(tip);
                        if (vals != null) {
                            numarPerTip.put(tip, (long) vals[0]);
                            incasariPerTip.put(tip, vals[1]);
                            total += vals[1];
                            count += (long) vals[0];
                        }
                    }

                    double medie = count > 0 ? total / count : 0;

                    TipBilet celMaiPopular = numarPerTip.entrySet().stream()
                            .sorted(Comparator.<Map.Entry<TipBilet, Long>, Long>comparing(Map.Entry::getValue).reversed()
                                    .thenComparing(e -> e.getKey().name()))
                            .map(Map.Entry::getKey)
                            .findFirst()
                            .orElse(null);

                    return new RaportVanzari(numarPerTip, incasariPerTip, total, medie, celMaiPopular);
                }
        );

        RaportVanzari raport = bilete.stream().collect(collector);

        Arrays.stream(TipBilet.values())
                .filter(t -> raport.getNumarPerTip().containsKey(t))
                .forEach(t -> System.out.printf(Locale.US, "%s: count=%d incasari=%.2f RON%n",
                        t, raport.getNumarPerTip().get(t), raport.getIncasariPerTip().get(t)));

        if (comanda.equals("RAPORT_COMPLET")) {
            System.out.println("---");
            System.out.printf(Locale.US, "Total: %.2f RON%n", raport.getTotalGlobal());
            System.out.printf(Locale.US, "Medie: %.2f RON%n", raport.getMedieGlobala());
            System.out.printf("Cel mai popular: %s%n", raport.getTipCelMaiPopular());
        }
    }
}
