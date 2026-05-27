package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);

        int n = Integer.parseInt(in.nextLine().trim());
        List<Tranzactie> tranzactii = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String[] parts = in.nextLine().trim().split(" ");
            int id = Integer.parseInt(parts[0]);
            double suma = Double.parseDouble(parts[1]);
            String data = parts[2];
            String contSursa = parts[3];
            String contDestinatie = parts[4];
            TipTranzactie tip = TipTranzactie.valueOf(parts[5]);

            Tranzactie x = new Tranzactie(id, suma, data, contSursa, contDestinatie, tip);
            x.setNote("procesat");
            tranzactii.add(x);
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            oos.writeObject(tranzactii);
        }

        List<Tranzactie> rezultat;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))) {
            rezultat = (List<Tranzactie>) ois.readObject();
        }

        while (in.hasNextLine()) {
            String linie = in.nextLine().trim();
            if (linie.isEmpty()) continue;

            if (linie.equals("LIST")) {
                for (Tranzactie t : rezultat) {
                    System.out.printf(Locale.US, "[%d] %s %s: %.2f RON | %s -> %s%n",
                        t.getId(), t.getData(), t.getTip(), t.getSuma(),
                        t.getContSursa(), t.getContDestinatie());
                }
            } else if (linie.startsWith("FILTER")) {
                String prefix = linie.split(" ")[1];
                boolean gasit = false;
                for (Tranzactie t : rezultat) {
                    if (t.getData().startsWith(prefix)) {
                        System.out.printf(Locale.US, "[%d] %s %s: %.2f RON | %s -> %s%n",
                            t.getId(), t.getData(), t.getTip(), t.getSuma(),
                            t.getContSursa(), t.getContDestinatie());
                        gasit = true;
                    }
                }
                if (!gasit) {
                    System.out.println("Niciun rezultat.");
                }
            } else if (linie.startsWith("NOTE")) {
                int id = Integer.parseInt(linie.split(" ")[1]);
                Tranzactie gasita = null;
                for (Tranzactie t : rezultat) {
                    if (t.getId() == id) {
                        gasita = t;
                        break;
                    }
                }
                if (gasita == null) {
                    System.out.println("NOTE[" + id + "]: not found");
                } else {
                    System.out.println("NOTE[" + id + "]: " + gasita.getNote());
                }
            }
        }
    }
}
