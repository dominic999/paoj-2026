package com.pao.proiectCabinetMedical;

import com.pao.proiectCabinetMedical.utils.Ansi;

public class MedicSpecialist extends Medic {

  Ansi a = Ansi.getInstance();
  private String[] specs;

  public MedicSpecialist(String firstName, String lastName, boolean deGarda, int aniExperienta, boolean rezident, String departament, String[] specs){
    super(firstName, lastName, deGarda, aniExperienta, rezident, departament);
    this.specs = specs == null ? new String[0] : specs.clone();
  }

  public String[] getSpecs(){
    return this.specs.clone();
  }

  public void setSpecs(String[] specs){
    this.specs = specs == null ? new String[0] : specs.clone();
  }

  public void addSpec(String spec){
    String[] newSpec = new String[specs.length + 1];
    for (int i = 0; i < specs.length; i++){
      newSpec[i] = specs[i];
    }
    newSpec[specs.length] = spec;
    specs = newSpec;
  }

  @Override
  public String toString(){
    String specText = specs.length == 0 ? "fara specializari" : specs[0];
    for (int i = 1; i < specs.length; i++){
      specText += ", " + specs[i];
    }
    return super.toString() +
           a.setForeground(a.red) + " | specializari: " + specText + a.reset();
  }
}
