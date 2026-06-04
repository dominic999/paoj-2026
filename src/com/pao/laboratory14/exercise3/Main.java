package com.pao.laboratory14.exercise3;

import java.util.*;

/**
 * Bonus — Alocare Automata de Sali pentru Evenimente
 * <p>
 * Problema clasica de interviu: date N evenimente cu intervale [start, end],
 * gaseste numarul minim de sali necesare si atribuie fiecare eveniment la o sala.
 * <p>
 * Doua variante demonstrate:
 * Varianta 1 — greedy simplu O(N^2): prima sala disponibila
 * Varianta 2 — PriorityQueue O(N log N): min-heap de ore de final
 */
public class Main {

    record Eveniment(String nume, int startMin, int endMin) {
    }

    private static int toMin(String hhmm) {
        String[] p = hhmm.split(":");
        return Integer.parseInt(p[0]) * 60 + Integer.parseInt(p[1]);
    }

    private static String toHHMM(int min) {
        return String.format("%02d:%02d", min / 60, min % 60);
    }

    public static void main(String[] args) {
        List<Eveniment> evenimente = List.of(
                new Eveniment("Conferinta AI",   toMin("09:00"), toMin("11:00")),
                new Eveniment("Workshop Java",   toMin("09:30"), toMin("11:30")),
                new Eveniment("Panel DevOps",    toMin("10:00"), toMin("12:00")),
                new Eveniment("Lunch Talk",      toMin("11:00"), toMin("12:30")),
                new Eveniment("Seminar Cloud",   toMin("12:00"), toMin("14:00")),
                new Eveniment("Hackathon Intro", toMin("13:00"), toMin("15:00")),
                new Eveniment("Demo Finala",     toMin("14:00"), toMin("16:00")),
                new Eveniment("Networking",      toMin("15:00"), toMin("17:00"))
        );

        List<Eveniment> sorted = evenimente.stream()
                .sorted(Comparator.comparingInt(Eveniment::startMin))
                .toList();

        System.out.println("=== Varianta 1: Greedy O(N^2) ===");
        System.out.println();

        List<Integer> rooms = new ArrayList<>();
        for (Eveniment ev : sorted) {
            int assigned = -1;
            for (int i = 0; i < rooms.size(); i++) {
                if (rooms.get(i) <= ev.startMin()) {
                    rooms.set(i, ev.endMin());
                    assigned = i + 1;
                    break;
                }
            }
            if (assigned == -1) {
                rooms.add(ev.endMin());
                assigned = rooms.size();
            }
            System.out.printf("  %-20s (%s - %s)  ->  Sala #%d%n",
                    ev.nume(), toHHMM(ev.startMin()), toHHMM(ev.endMin()), assigned);
        }
        System.out.println();
        System.out.println("Sali necesare (greedy): " + rooms.size());

        System.out.println();
        System.out.println("=== Varianta 2: PriorityQueue O(N log N) ===");
        System.out.println();

        PriorityQueue<Integer> pq = new PriorityQueue<>();
        for (Eveniment ev : sorted) {
            if (!pq.isEmpty() && pq.peek() <= ev.startMin()) {
                pq.poll();
            }
            pq.offer(ev.endMin());
            System.out.printf("  %-20s (%s - %s)  ->  Sali ocupate: %d%n",
                    ev.nume(), toHHMM(ev.startMin()), toHHMM(ev.endMin()), pq.size());
        }
        System.out.println();
        System.out.println("Sali necesare (PriorityQueue): " + pq.size());
    }
}

