package com.pao.proiectCabinetMedical.service;
import com.pao.proiectCabinetMedical.Medic;
import com.pao.proiectCabinetMedical.utils.Ansi;

public class MedicService {

  private Medic[] medics;
  Ansi a = Ansi.getInstance();
  
  private MedicService() {
    this.medics = new Medic[0];
  }

  private static class Holder{
    private static final MedicService INSTANCE = new MedicService();
  }

  public static MedicService getInstance(){
    return Holder.INSTANCE;
  }
  
  
  public void addMedic(Medic o){
    Medic[] now = new Medic[medics.length + 1];
    System.arraycopy(medics, 0, now, 0, medics.length); 
    now[medics.length] = o;
    medics = now;
  }

  public Medic[] getMedics(){
    return medics.clone();
  }

  public void showMedics(){
    System.out.println(a.title("Lista medicilor"));
    System.out.println(a.line());
    for (int i = 0; i < medics.length; i++){
      System.out.println(medics[i]);
    }
    if (medics.length == 0){
      System.out.println(a.warning("Nu exista medici inregistrati."));
    }
    System.out.println(a.line());
  }

  public void deleteMedic(int id){
    if (medics.length == 0){
      return;
    }
    Medic[] newMedics = new Medic[medics.length - 1];
    int idx = 0;
    for (int i = 0; i < medics.length; i++){
      if (medics[i].getId() != id){
        newMedics[idx++] = medics[i];
      }
    }
    medics = newMedics;

  }

  public Medic findMedicById(int medicId){
    for (int i = 0; i < medics.length; i++){
      if (medics[i].getId() == medicId){
        return medics[i];
      }
    }
    return null;
  }

  public void showOptions(){
    System.out.println(a.title("Ce vreti sa faceti cu medicii?"));
    System.out.println(a.line());
    System.out.println(a.option(1, "Vezi toti medicii."));
    System.out.println(a.option(2, "Adauga un medic nou."));
    System.out.println(a.option(3, "Sterge un medic dupa id."));
    System.out.println(a.option(4, "Vezi datele unui medic."));
    System.out.println(a.option(5, "Inapoi la meniul entitatilor."));
    System.out.println(a.option(6, "Iesire din aplicatie."));
    System.out.println(a.line());
  }

  public void showMedicById(int medicId){
    if (medics.length == 0){
      System.out.println(a.warning("Nu exista medici inregistrati."));
      return;
    }
    Medic medic = findMedicById(medicId);
    if (medic == null){
      System.out.println(a.warning("Nu exista medic cu id-ul cerut."));
      return;
    }
    System.out.println(a.title("Detalii medic"));
    System.out.println(a.line());
    System.out.println(medic);
    System.out.println(a.line());
  } 



}
