package com.pao.proiectCabinetMedical.service;

import com.pao.proiectCabinetMedical.Sali;
import com.pao.proiectCabinetMedical.utils.Ansi;

public class SaliService {

  private Sali[] sali;
  Ansi a = Ansi.getInstance();

  private SaliService(){
    this.sali = new Sali[0];
  }

  private static class Holder{
    private static final SaliService INSTANCE = new SaliService();
  }

  public static SaliService getInstance(){
    return Holder.INSTANCE;
  }

  public void addSala(Sali o){
    Sali[] now = new Sali[sali.length + 1];
    System.arraycopy(sali, 0, now, 0, sali.length);
    now[sali.length] = o;
    sali = now;
  }

  public Sali[] getSali(){
    return sali.clone();
  }

  public void showSali(){
    System.out.println(a.title("Lista salilor"));
    System.out.println(a.line());
    for (int i = 0; i < sali.length; i++){
      System.out.println(sali[i]);
    }
    if (sali.length == 0){
      System.out.println(a.warning("Nu exista sali inregistrate."));
    }
    System.out.println(a.line());
  }

  public void deleteSala(int idx){
    if (idx < 0 || idx >= sali.length){
      return;
    }
    Sali[] newSali = new Sali[sali.length - 1];
    int newIdx = 0;
    for (int i = 0; i < sali.length; i++){
      if (i != idx){
        newSali[newIdx++] = sali[i];
      }
    }
    sali = newSali;
  }

  public void showOptions(){
    System.out.println(a.title("Ce vreti sa faceti cu salile?"));
    System.out.println(a.line());
    System.out.println(a.option(1, "Vezi toate salile."));
    System.out.println(a.option(2, "Adauga o sala noua."));
    System.out.println(a.option(3, "Sterge o sala dupa index."));
    System.out.println(a.option(4, "Inapoi la meniul entitatilor."));
    System.out.println(a.option(5, "Iesire din aplicatie."));
    System.out.println(a.line());
  }
}
