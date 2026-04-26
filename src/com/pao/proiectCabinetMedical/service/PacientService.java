package com.pao.proiectCabinetMedical.service;

import com.pao.proiectCabinetMedical.Pacient;
import com.pao.proiectCabinetMedical.utils.Ansi;

public class PacientService {

  private Pacient[] pacienti;
  Ansi a = Ansi.getInstance();

  private PacientService(){
    this.pacienti = new Pacient[0];
  }

  private static class Holder{
    private static final PacientService INSTANCE = new PacientService();
  }

  public static PacientService getInstance(){
    return Holder.INSTANCE;
  }

  public void addPacient(Pacient o){
    Pacient[] now = new Pacient[pacienti.length + 1];
    System.arraycopy(pacienti, 0, now, 0, pacienti.length);
    now[pacienti.length] = o;
    pacienti = now;
  }

  public Pacient[] getPacienti(){
    return pacienti.clone();
  }

  public void showPacienti(){
    System.out.println(a.title("Lista pacientilor"));
    System.out.println(a.line());
    for (int i = 0; i < pacienti.length; i++){
      System.out.println(pacienti[i]);
    }
    if (pacienti.length == 0){
      System.out.println(a.warning("Nu exista pacienti inregistrati."));
    }
    System.out.println(a.line());
  }

  public void deletePacient(int idx){
    if (idx < 0 || idx >= pacienti.length){
      return;
    }
    Pacient[] newPacienti = new Pacient[pacienti.length - 1];
    int newIdx = 0;
    for (int i = 0; i < pacienti.length; i++){
      if (i != idx){
        newPacienti[newIdx++] = pacienti[i];
      }
    }
    pacienti = newPacienti;
  }

  public void showOptions(){
    System.out.println(a.title("Ce vreti sa faceti cu pacientii?"));
    System.out.println(a.line());
    System.out.println(a.option(1, "Vezi toti pacientii."));
    System.out.println(a.option(2, "Adauga un pacient nou."));
    System.out.println(a.option(3, "Sterge un pacient dupa index."));
    System.out.println(a.option(4, "Vezi diagnosticul si medicul supervizor."));
    System.out.println(a.option(5, "Inapoi la meniul entitatilor."));
    System.out.println(a.option(6, "Iesire din aplicatie."));
    System.out.println(a.line());
  }
}
