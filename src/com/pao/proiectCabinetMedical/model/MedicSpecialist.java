package com.pao.proiectCabinetMedical.model;

public class MedicSpecialist extends Medic {

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
    if (spec == null || spec.trim().isEmpty()){
      return;
    }
    String[] next = new String[specs.length + 1];
    for (int i = 0; i < specs.length; i++){
      next[i] = specs[i];
    }
    next[specs.length] = spec.trim();
    specs = next;
  }

  @Override
  public String getRol(){
    return "Medic specialist";
  }

  @Override
  public String toString(){
    String joined = specs.length == 0 ? "fara specializari" : specs[0];
    for (int i = 1; i < specs.length; i++){
      joined += ", " + specs[i];
    }
    return super.toString() +
           ansi.setForeground(ansi.red) + " | specializari: " + joined + ansi.reset();
  }
}
