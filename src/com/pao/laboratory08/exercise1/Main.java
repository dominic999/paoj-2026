package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = citesteStudenti();

        Scanner scanner = new Scanner(System.in);
        String comanda = scanner.nextLine().trim();

        if (comanda.equals("PRINT")) {
            for (Student s : studenti) {
                System.out.println(s);
            }
        } else {
            String[] parts = comanda.split(" ", 2);
            String tip = parts[0];
            String nume = parts[1].trim();

            Student gasit = null;
            for (Student s : studenti) {
                if (s.getNume().equals(nume)) {
                    gasit = s;
                    break;
                }
            }

            if (tip.equals("SHALLOW")) {
                Student clona = gasit.shallowClone();
                clona.getAdresa().setOras("MODIFICAT");
                System.out.println("Original: " + gasit);
                System.out.println("Clona: " + clona);
            } else if (tip.equals("DEEP")) {
                Student clona = gasit.deepClone();
                clona.getAdresa().setOras("MODIFICAT");
                System.out.println("Original: " + gasit);
                System.out.println("Clona: " + clona);
            }
        }
    }

    private static List<Student> citesteStudenti() throws IOException {
        List<Student> studenti = new ArrayList<>();
        BufferedReader fin = new BufferedReader(new FileReader(FILE_PATH));
        String linie;
        while ((linie = fin.readLine()) != null) {
            if (linie.trim().isEmpty()) continue;
            String[] parts = linie.split(",");
            String nume = parts[0].trim();
            int varsta = Integer.parseInt(parts[1].trim());
            String oras = parts[2].trim();
            String strada = parts[3].trim();
            studenti.add(new Student(nume, varsta, new Adresa(oras, strada)));
        }
        fin.close();
        return studenti;
    }
}
