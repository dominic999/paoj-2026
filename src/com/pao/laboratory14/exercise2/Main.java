package com.pao.laboratory14.exercise2;

import com.pao.laboratory14.exercise1.TipBilet;
import com.pao.laboratory14.exercise2.model.Eveniment;
import com.pao.laboratory14.exercise2.repository.EvenimentRepository;
import com.pao.laboratory14.exercise2.util.DatabaseConnection;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConnection.reset();
            var conn = DatabaseConnection.getInstance().getConnection();
            EvenimentRepository repo = new EvenimentRepository(conn);
            Scanner sc = new Scanner(System.in);

            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+");
                String cmd = parts[0];

                switch (cmd) {
                    case "ADD" -> {
                        String nume = parts[1];
                        String data = parts[2];
                        int capacitate = Integer.parseInt(parts[3]);
                        TipBilet tip = TipBilet.valueOf(parts[4]);
                        Eveniment ev = new Eveniment(nume, data, capacitate, tip);
                        repo.save(ev);
                        System.out.printf("Adaugat: [%d] %s%n", ev.getId(), ev.getNume());
                    }
                    case "LIST" -> {
                        List<Eveniment> all = repo.findAll();
                        for (Eveniment ev : all) {
                            System.out.printf("[%d] %s | %s | cap=%d | %s%n",
                                    ev.getId(), ev.getNume(), ev.getData(),
                                    ev.getCapacitate(), ev.getTip());
                        }
                    }
                    case "DELETE" -> {
                        int id = Integer.parseInt(parts[1]);
                        try {
                            repo.delete(id);
                            System.out.printf("Sters: %d%n", id);
                        } catch (Exception e) {
                            System.out.printf("Nu exista: %d%n", id);
                        }
                    }
                    case "COUNT" -> {
                        System.out.printf("Total: %d%n", repo.count());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
