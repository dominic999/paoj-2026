package com.pao.proiectCabinetMedical.service;

import com.pao.proiectCabinetMedical.Persoana;
import com.pao.proiectCabinetMedical.utils.Ansi;

public class PersoanaService {

  private Persoana[] persoane;
  Ansi a = Ansi.getInstance();

  private PersoanaService(){
    this.persoane = new Persoana[0];
  }

  private static class Holder{
    private static final PersoanaService INSTANCE = new PersoanaService();
  }

  public static PersoanaService getInstance(){
    return Holder.INSTANCE;
  }

  public void addPersoana(Persoana o){
    Persoana[] now = new Persoana[persoane.length + 1];
    System.arraycopy(persoane, 0, now, 0, persoane.length);
    now[persoane.length] = o;
    persoane = now;
  }

  public Persoana[] getPersoane(){
    return persoane.clone();
  }

  public void showPersoane(){
    System.out.println(a.title("Lista persoanelor"));
    System.out.println(a.line());
    for (int i = 0; i < persoane.length; i++){
      System.out.println(persoane[i]);
    }
    if (persoane.length == 0){
      System.out.println(a.warning("Nu exista persoane inregistrate."));
    }
    System.out.println(a.line());
  }

  public void deletePersoana(int idx){
    if (idx < 0 || idx >= persoane.length){
      return;
    }
    Persoana[] newPersoane = new Persoana[persoane.length - 1];
    int newIdx = 0;
    for (int i = 0; i < persoane.length; i++){
      if (i != idx){
        newPersoane[newIdx++] = persoane[i];
      }
    }
    persoane = newPersoane;
  }

  public void showOptions(){
    System.out.println(a.title("Ce vreti sa faceti cu persoanele?"));
    System.out.println(a.line());
    System.out.println(a.option(1, "Vezi toate persoanele."));
    System.out.println(a.option(2, "Adauga o persoana noua."));
    System.out.println(a.option(3, "Sterge o persoana dupa index."));
    System.out.println(a.option(4, "Inapoi la meniul entitatilor."));
    System.out.println(a.option(5, "Iesire din aplicatie."));
    System.out.println(a.line());
  }
}
