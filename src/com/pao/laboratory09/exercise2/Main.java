package com.pao.laboratory09.exercise2;

import com.pao.laboratory09.exercise1.TipTranzactie;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex2.bin";
    private static final int RECORD_SIZE = 32;

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);

        int n = Integer.parseInt(in.nextLine().trim());

        int[] ids = new int[n];
        double[] sume = new double[n];
        String[] date = new String[n];
        TipTranzactie[] tipuri = new TipTranzactie[n];

        for (int i = 0; i < n; i++) {
            String[] parts = in.nextLine().trim().split(" ");
            ids[i] = Integer.parseInt(parts[0]);
            sume[i] = Double.parseDouble(parts[1]);
            date[i] = parts[2];
            tipuri[i] = TipTranzactie.valueOf(parts[3]);
        }

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(OUTPUT_FILE))) {
            for (int i = 0; i < n; i++) {
                dos.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(ids[i]).array());

                dos.write(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN).putDouble(sume[i]).array());

                dos.write(String.format("%-10s", date[i]).getBytes());

                dos.write(tipuri[i] == TipTranzactie.CREDIT ? 0 : 1);

                dos.write(0);

                dos.write(new byte[8]);
            }
        }

        RandomAccessFile raf = new RandomAccessFile(OUTPUT_FILE, "rw");

        while (in.hasNextLine()) {
            String linie = in.nextLine().trim();
            if (linie.isEmpty()) continue;

            if (linie.startsWith("READ")) {
                int idx = Integer.parseInt(linie.split(" ")[1]);
                afiseazaInregistrare(raf, idx);

            } else if (linie.startsWith("UPDATE")) {
                String[] parts = linie.split(" ");
                int idx = Integer.parseInt(parts[1]);
                String statusStr = parts[2];

                int statusByte;
                switch (statusStr) {
                    case "PROCESSED": statusByte = 1; break;
                    case "REJECTED":  statusByte = 2; break;
                    default:          statusByte = 0; break;
                }

                raf.seek((long) idx * RECORD_SIZE + 23);
                raf.write(statusByte); 

                System.out.println("Updated [" + idx + "]: " + statusStr);

            } else if (linie.equals("PRINT_ALL")) {
                for (int i = 0; i < n; i++) {
                    afiseazaInregistrare(raf, i);
                }
            }
        }

        raf.close();
    }

    private static void afiseazaInregistrare(RandomAccessFile raf, int idx) throws IOException {
        raf.seek((long) idx * RECORD_SIZE); 
        byte[] bytes = new byte[RECORD_SIZE];
        raf.read(bytes);

        ByteBuffer buf = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);

        int id = buf.getInt(0);                         
        double suma = buf.getDouble(4);                 
        String data = new String(bytes, 12, 10).trim(); 
        int tipByte = buf.get(22);                      
        int statusByte = buf.get(23);                   

        String tip = tipByte == 0 ? "CREDIT" : "DEBIT";
        String status = statusByte == 0 ? "PENDING" : statusByte == 1 ? "PROCESSED" : "REJECTED";

        System.out.printf(Locale.US, "[%d] id=%d data=%s tip=%s suma=%.2f RON status=%s%n",
            idx, id, data, tip, suma, status);
    }
}
