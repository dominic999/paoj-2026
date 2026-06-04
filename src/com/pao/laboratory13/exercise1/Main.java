package com.pao.laboratory13.exercise1;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int q = Integer.parseInt(sc.nextLine().trim());
        ProtocolEngine engine = new ProtocolEngine();

        for (int i = 0; i < q; i++) {
            String line = sc.nextLine();
            if (line.isBlank()) {
                i--;
                continue;
            }
            System.out.println(engine.process(line));
        }
    }
}
