package com.pao.proiectCabinetMedical.service;

import com.pao.proiectCabinetMedical.Asistenta;
import com.pao.proiectCabinetMedical.utils.Ansi;

public class AsistentaService {

  private Asistenta[] asistente;
  Ansi a = Ansi.getInstance();

  private AsistentaService(){
    this.asistente = new Asistenta[0];
  }

  private static class Holder{
    private static final AsistentaService INSTANCE = new AsistentaService();
  }

  public static AsistentaService getInstance(){
    return Holder.INSTANCE;
  }

  public void addAsistenta(Asistenta o){
    Asistenta[] now = new Asistenta[asistente.length + 1];
    System.arraycopy(asistente, 0, now, 0, asistente.length);
    now[asistente.length] = o;
    asistente = now;
  }

  public Asistenta[] getAsistente(){
    return asistente.clone();
  }

  public void showAsistente(){
    System.out.println(a.title("Lista asistentelor"));
    System.out.println(a.line());
    for (int i = 0; i < asistente.length; i++){
      System.out.println(asistente[i]);
    }
    if (asistente.length == 0){
      System.out.println(a.warning("Nu exista asistente inregistrate."));
    }
    System.out.println(a.line());
  }

  public void deleteAsistenta(int idx){
    if (idx < 0 || idx >= asistente.length){
      return;
    }
    Asistenta[] newAsistente = new Asistenta[asistente.length - 1];
    int newIdx = 0;
    for (int i = 0; i < asistente.length; i++){
      if (i != idx){
        newAsistente[newIdx++] = asistente[i];
      }
    }
    asistente = newAsistente;
  }

  public void showOptions(){
    System.out.println(a.title("Ce vreti sa faceti cu asistentele?"));
    System.out.println(a.line());
    System.out.println(a.option(1, "Vezi toate asistentele."));
    System.out.println(a.option(2, "Adauga o asistenta noua."));
    System.out.println(a.option(3, "Sterge o asistenta dupa index."));
    System.out.println(a.option(4, "Vezi salile de care raspunde o asistenta."));
    System.out.println(a.option(5, "Inapoi la meniul entitatilor."));
    System.out.println(a.option(6, "Iesire din aplicatie."));
    System.out.println(a.line());
  }
}
