package com.pao.laboratory13.exercise2;

import com.pao.laboratory13.exercise1.ProtocolEngine;

import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class Main {

    private static final int PORT = 9000;

    public static void main(String[] args) throws Exception {
        CountDownLatch clientsDone = new CountDownLatch(2);
        ExecutorService pool = Executors.newCachedThreadPool();

        Thread serverThread = new Thread(() -> {
            try (ServerSocket server = new ServerSocket(PORT)) {
                server.setSoTimeout(5000);
                System.out.println("[SERVER] Listening on port " + PORT);

                for (int i = 0; i < 2; i++) {
                    Socket client = server.accept();
                    int id = i + 1;
                    pool.submit(() -> handleClient(client, id, clientsDone));
                }

                clientsDone.await();
                pool.shutdown();
                System.out.println("[SERVER] All clients done. Shutting down.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.start();

        Thread.sleep(300);

        Thread c1 = new Thread(() -> runClient(1, new String[]{
                "AUTH alice", "OPEN", "SEND hi", "BROADCAST ping", "HISTORY", "CLOSE"
        }));
        Thread c2 = new Thread(() -> runClient(2, new String[]{
                "AUTH bob", "OPEN", "SEND hello world", "CLOSE"
        }));

        c1.start();
        c2.start();

        c1.join();
        c2.join();
        serverThread.join();
    }

    private static void handleClient(Socket socket, int id, CountDownLatch latch) {
        String tag = "[CLIENT-" + id + "]";
        try (socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            System.out.println(tag + " Connected");
            ProtocolEngine engine = new ProtocolEngine();

            String line;
            while ((line = in.readLine()) != null) {
                String response = engine.process(line);
                out.println(response);
                System.out.println(tag + " >> " + line + "  =>  " + response);
            }

            System.out.println(tag + " Disconnected");
        } catch (IOException e) {
            System.out.println(tag + " Error: " + e.getMessage());
        } finally {
            latch.countDown();
        }
    }

    private static void runClient(int id, String[] commands) {
        try (Socket socket = new Socket("localhost", PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            for (String cmd : commands) {
                out.println(cmd);
                in.readLine();
                Thread.sleep(50);
            }
        } catch (Exception e) {
            System.out.println("[CLIENT-" + id + "] Error: " + e.getMessage());
        }
    }
}
