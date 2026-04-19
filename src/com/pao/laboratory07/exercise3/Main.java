package com.pao.laboratory07.exercise3;

import com.pao.laboratory07.exercise1.OrderState;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = Integer.parseInt(scanner.nextLine().trim());
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] tokens = scanner.nextLine().trim().split(" ");
            switch (tokens[0]) {
                case "STANDARD" -> comenzi.add(new ComandaStandard(tokens[1], Double.parseDouble(tokens[2]), tokens[3]));
                case "DISCOUNTED" -> comenzi.add(new ComandaRedusa(tokens[1], Double.parseDouble(tokens[2]), Integer.parseInt(tokens[3]), tokens[4]));
                case "GIFT" -> comenzi.add(new ComandaGratuita(tokens[1], tokens[2]));
                default -> throw new IllegalArgumentException("Tip de comanda invalid: " + tokens[0]);
            }
        }

        for (Comanda comanda : comenzi) {
            System.out.println(comanda.descriereCuClient());
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            if (line.equals("QUIT")) {
                return;
            }
            if (line.equals("STATS")) {
                printStats(comenzi);
            } else if (line.startsWith("FILTER ")) {
                double threshold = Double.parseDouble(line.substring("FILTER ".length()).trim());
                printFilter(comenzi, threshold);
            } else if (line.equals("SORT")) {
                printSorted(comenzi);
            } else if (line.equals("SPECIAL")) {
                printSpecial(comenzi);
            } else {
                throw new IllegalArgumentException("Comanda invalida: " + line);
            }
        }
    }

    private static void printStats(List<Comanda> comenzi) {
        System.out.println();
        System.out.println("--- STATS ---");
        Map<String, DoubleSummaryStatistics> stats = comenzi.stream()
                .collect(Collectors.groupingBy(Comanda::tip, LinkedHashMap::new, Collectors.summarizingDouble(Comanda::pretFinal)));
        for (String tip : List.of("STANDARD", "DISCOUNTED", "GIFT")) {
            if (stats.containsKey(tip)) {
                System.out.printf("%s: medie = %.2f lei%n", tip, stats.get(tip).getAverage());
            }
        }
    }

    private static void printFilter(List<Comanda> comenzi, double threshold) {
        System.out.println();
        System.out.printf("--- FILTER (>= %.2f) ---%n", threshold);
        comenzi.stream()
                .filter(comanda -> comanda.pretFinal() >= threshold)
                .forEach(comanda -> System.out.println(comanda.descriereFaraStare()));
    }

    private static void printSorted(List<Comanda> comenzi) {
        System.out.println();
        System.out.println("--- SORT (by client, then by pret) ---");
        comenzi.stream()
                .sorted(Comparator.comparing(Comanda::client).thenComparing(Comanda::pretFinal))
                .forEach(comanda -> System.out.println(comanda.descriereFaraStare()));
    }

    private static void printSpecial(List<Comanda> comenzi) {
        System.out.println();
        System.out.println("--- SPECIAL (discount > 15%) ---");
        comenzi.stream()
                .filter(ComandaRedusa.class::isInstance)
                .map(ComandaRedusa.class::cast)
                .filter(comanda -> comanda.discountProcent() > 15)
                .forEach(comanda -> System.out.println(comanda.descriereFaraStare()));
    }

    private abstract sealed static class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
        protected final String nume;
        protected final String client;
        protected final OrderState stare;

        protected Comanda(String nume, String client) {
            this.nume = nume;
            this.client = client;
            this.stare = OrderState.PLACED;
        }

        public abstract double pretFinal();

        public abstract String tip();

        public abstract String descriereCuClient();

        public abstract String descriereFaraStare();

        public String client() {
            return client;
        }
    }

    private static final class ComandaStandard extends Comanda {
        private final double pret;

        private ComandaStandard(String nume, double pret, String client) {
            super(nume, client);
            this.pret = pret;
        }

        @Override
        public double pretFinal() {
            return pret;
        }

        @Override
        public String tip() {
            return "STANDARD";
        }

        @Override
        public String descriereCuClient() {
            return String.format("STANDARD: %s, pret: %.2f lei [%s] - client: %s", nume, pretFinal(), stare, client);
        }

        @Override
        public String descriereFaraStare() {
            return String.format("STANDARD: %s, pret: %.2f lei - client: %s", nume, pretFinal(), client);
        }
    }

    private static final class ComandaRedusa extends Comanda {
        private final double pret;
        private final int discountProcent;

        private ComandaRedusa(String nume, double pret, int discountProcent, String client) {
            super(nume, client);
            this.pret = pret;
            this.discountProcent = discountProcent;
        }

        @Override
        public double pretFinal() {
            return pret * (1 - discountProcent / 100.0);
        }

        @Override
        public String tip() {
            return "DISCOUNTED";
        }

        public int discountProcent() {
            return discountProcent;
        }

        @Override
        public String descriereCuClient() {
            return String.format("DISCOUNTED: %s, pret: %.2f lei (-%d%%) [%s] - client: %s", nume, pretFinal(), discountProcent, stare, client);
        }

        @Override
        public String descriereFaraStare() {
            return String.format("DISCOUNTED: %s, pret: %.2f lei (-%d%%) - client: %s", nume, pretFinal(), discountProcent, client);
        }
    }

    private static final class ComandaGratuita extends Comanda {
        private ComandaGratuita(String nume, String client) {
            super(nume, client);
        }

        @Override
        public double pretFinal() {
            return 0.0;
        }

        @Override
        public String tip() {
            return "GIFT";
        }

        @Override
        public String descriereCuClient() {
            return String.format("GIFT: %s, gratuit [%s] - client: %s", nume, stare, client);
        }

        @Override
        public String descriereFaraStare() {
            return String.format("GIFT: %s, gratuit - client: %s", nume, client);
        }
    }
}
