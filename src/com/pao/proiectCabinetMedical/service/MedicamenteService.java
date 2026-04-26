package com.pao.proiectCabinetMedical.service;

import com.pao.proiectCabinetMedical.Medicamente;
import com.pao.proiectCabinetMedical.utils.Ansi;

public class MedicamenteService {

  private Medicamente[] medicamente;
  Ansi a = Ansi.getInstance();

  private MedicamenteService(){
    this.medicamente = new Medicamente[0];
  }

  private static class Holder{
    private static final MedicamenteService INSTANCE = new MedicamenteService();
  }

  public static MedicamenteService getInstance(){
    return Holder.INSTANCE;
  }

  public void addMedicament(Medicamente o){
    Medicamente[] now = new Medicamente[medicamente.length + 1];
    System.arraycopy(medicamente, 0, now, 0, medicamente.length);
    now[medicamente.length] = o;
    medicamente = now;
  }

  public Medicamente[] getMedicamente(){
    return medicamente.clone();
  }

  public void showMedicamente(){
    System.out.println(a.title("Lista medicamentelor"));
    System.out.println(a.line());
    for (int i = 0; i < medicamente.length; i++){
      System.out.println(medicamente[i]);
    }
    if (medicamente.length == 0){
      System.out.println(a.warning("Nu exista medicamente inregistrate."));
    }
    System.out.println(a.line());
  }

  public void deleteMedicament(int idx){
    if (idx < 0 || idx >= medicamente.length){
      return;
    }
    Medicamente[] newMedicamente = new Medicamente[medicamente.length - 1];
    int newIdx = 0;
    for (int i = 0; i < medicamente.length; i++){
      if (i != idx){
        newMedicamente[newIdx++] = medicamente[i];
      }
    }
    medicamente = newMedicamente;
  }

  public void showOptions(){
    System.out.println(a.title("Ce vreti sa faceti cu medicamentele?"));
    System.out.println(a.line());
    System.out.println(a.option(1, "Vezi toate medicamentele."));
    System.out.println(a.option(2, "Adauga un medicament nou."));
    System.out.println(a.option(3, "Sterge un medicament dupa index."));
    System.out.println(a.option(4, "Inapoi la meniul entitatilor."));
    System.out.println(a.option(5, "Iesire din aplicatie."));
    System.out.println(a.line());
  }
}
