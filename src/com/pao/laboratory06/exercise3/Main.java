package com.pao.laboratory06.exercise3;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Vezi Readme.md pentru cerințe
        Inginer[] ingineri = {
                new Inginer("Popescu", "Ana", "0711000001", 12000, 3000),
                new Inginer("Ionescu", "Vlad", "0711000002", 9000, 1500),
                new Inginer("Georgescu", "Maria", "0711000003", 15000, 5000)
        };

        Arrays.sort(ingineri);
        System.out.println("Sortare naturala:");
        for (Inginer inginer : ingineri) {
            System.out.println(inginer);
        }

        Arrays.sort(ingineri, new ComparatorInginerSalariu());
        System.out.println();
        System.out.println("Sortare dupa salariu:");
        for (Inginer inginer : ingineri) {
            System.out.println(inginer);
        }

        PlataOnline plataInginer = ingineri[0];
        plataInginer.autentificare("ana.popescu", "parola123");
        System.out.println();
        System.out.printf("Sold inginer prin interfata: %.2f%n", plataInginer.consultareSold());
        System.out.println("Plata efectuata: " + plataInginer.efectuarePlata(750));
        System.out.printf("Sold ramas: %.2f%n", plataInginer.consultareSold());

        PersoanaJuridica firmaCuTelefon = new PersoanaJuridica("Tech", "SRL", "0722000001", 20000);
        PlataOnlineSMS plataFirma = firmaCuTelefon;
        plataFirma.autentificare("tech.srl", "secret");
        System.out.println();
        System.out.println("SMS valid trimis: " + plataFirma.trimiteSMS("Plata confirmata"));
        System.out.println("SMS invalid trimis: " + plataFirma.trimiteSMS(" "));
        System.out.println("Mesaje stocate: " + firmaCuTelefon.getSmsTrimise());

        PersoanaJuridica firmaFaraTelefon = new PersoanaJuridica("NoPhone", "SRL", "", 10000);
        System.out.println("SMS fara telefon: " + firmaFaraTelefon.trimiteSMS("Test"));

        System.out.println();
        System.out.println("Constanta TVA: " + ConstanteFinanciare.TVA.getValoare());

        try {
            plataInginerFaraSMS(plataInginer, "Mesaj catre inginer");
        } catch (UnsupportedOperationException e) {
            System.out.println("Eroare SMS: " + e.getMessage());
        }

        try {
            plataInginer.autentificare(null, "parola");
        } catch (IllegalArgumentException e) {
            System.out.println("Eroare autentificare: " + e.getMessage());
        }
    }

    private static boolean plataInginerFaraSMS(PlataOnline plataOnline, String mesaj) {
        if (plataOnline instanceof PlataOnlineSMS plataOnlineSMS) {
            return plataOnlineSMS.trimiteSMS(mesaj);
        }
        throw new UnsupportedOperationException("Entitatea nu suporta SMS");
    }
}
