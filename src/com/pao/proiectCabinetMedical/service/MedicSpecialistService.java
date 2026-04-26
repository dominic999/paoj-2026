package com.pao.proiectCabinetMedical.service;

import com.pao.proiectCabinetMedical.MedicSpecialist;
import com.pao.proiectCabinetMedical.utils.Ansi;

public class MedicSpecialistService {

  private MedicSpecialist[] medics;
  Ansi a = Ansi.getInstance();

  private MedicSpecialistService(){
    this.medics = new MedicSpecialist[0];
  }

  private static class Holder{
    private static final MedicSpecialistService INSTANCE = new MedicSpecialistService();
  }

  public static MedicSpecialistService getInstance(){
    return Holder.INSTANCE;
  }

  public void addMedic(MedicSpecialist o){
    MedicSpecialist[] now = new MedicSpecialist[medics.length + 1];
    System.arraycopy(medics, 0, now, 0, medics.length);
    now[medics.length] = o;
    medics = now;
  }

  public MedicSpecialist[] getMedics(){
    return medics.clone();
  }

  public void showMedics(){
    System.out.println(a.title("Lista medicilor specialisti"));
    System.out.println(a.line());
    for (int i = 0; i < medics.length; i++){
      System.out.println(medics[i]);
    }
    if (medics.length == 0){
      System.out.println(a.warning("Nu exista medici specialisti inregistrati."));
    }
    System.out.println(a.line());
  }

  public void deleteMedic(int idx){
    if (idx < 0 || idx >= medics.length){
      return;
    }
    MedicSpecialist[] newMedics = new MedicSpecialist[medics.length - 1];
    int newIdx = 0;
    for (int i = 0; i < medics.length; i++){
      if (i != idx){
        newMedics[newIdx++] = medics[i];
      }
    }
    medics = newMedics;
  }

  public void showMedSpecs(int idx){
    if (idx < 0 || idx >= medics.length){
      return;
    }
    String[] specs = medics[idx].getSpecs();
    System.out.println(a.title("Specializarile medicului"));
    System.out.println(a.line());
    for (int i = 0; i < specs.length; i++){
      System.out.println(a.option(i + 1, specs[i]));
    }
    if (specs.length == 0){
      System.out.println(a.warning("Medicul nu are specializari."));
    }
    System.out.println(a.line());
  }

  public void addMedSpec(int idx, String spec){
    if (idx < 0 || idx >= medics.length){
      return;
    }
    medics[idx].addSpec(spec);
  }

  public void showOptions(){
    System.out.println(a.title("Ce vreti sa faceti cu medicii specialisti?"));
    System.out.println(a.line());
    System.out.println(a.option(1, "Vezi toti medicii specialisti."));
    System.out.println(a.option(2, "Adauga un medic specialist nou."));
    System.out.println(a.option(3, "Sterge un medic specialist dupa index."));
    System.out.println(a.option(4, "Vezi specializarile unui medic specialist."));
    System.out.println(a.option(5, "Adauga o specializare unui medic specialist."));
    System.out.println(a.option(6, "Inapoi la meniul entitatilor."));
    System.out.println(a.option(7, "Iesire din aplicatie."));
    System.out.println(a.line());
  }
}
