package com.pao.proiectCabinetMedical;

import com.pao.proiectCabinetMedical.utils.Ansi;

public class Manager extends Medic {

  Ansi a = Ansi.getInstance();
  private Medic[] subordonati;

  public Manager(String firstName, String lastName, boolean deGarda, int aniExperienta, boolean rezident, String departament, Medic[] subordonati){
    super(firstName, lastName, deGarda, aniExperienta, rezident, departament);
    this.subordonati = subordonati == null ? new Medic[0] : subordonati.clone();
  }

  public Medic[] getSubordonati(){
    return this.subordonati.clone();
  }

  public void setSubordonati(Medic[] subordonati){
    this.subordonati = subordonati == null ? new Medic[0] : subordonati.clone();
  }

  public void addSubordonat(Medic medic){
    Medic[] newSubordonati = new Medic[subordonati.length + 1];
    for (int i = 0; i < subordonati.length; i++){
      newSubordonati[i] = subordonati[i];
    }
    newSubordonati[subordonati.length] = medic;
    subordonati = newSubordonati;
  }

  @Override
  public String toString(){
    return super.toString() +
           a.setForeground(a.red) + " | subordonati: " + subordonati.length + a.reset();
  }
}
