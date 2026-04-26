package com.pao.proiectCabinetMedical.model;

import java.util.ArrayList;
import java.util.List;

public class Manager extends Medic {

  private List<Medic> subordonati;

  public Manager(String firstName, String lastName, boolean deGarda, int aniExperienta, boolean rezident, String departament, List<Medic> subordonati){
    super(firstName, lastName, deGarda, aniExperienta, rezident, departament);
    this.subordonati = subordonati == null ? new ArrayList<>() : new ArrayList<>(subordonati);
  }

  public List<Medic> getSubordonati(){
    return new ArrayList<>(this.subordonati);
  }

  public void setSubordonati(List<Medic> subordonati){
    this.subordonati = subordonati == null ? new ArrayList<>() : new ArrayList<>(subordonati);
  }

  public void addSubordonat(Medic medic){
    if (medic != null){
      subordonati.add(medic);
    }
  }

  @Override
  public String getRol(){
    return "Manager";
  }

  @Override
  public String toString(){
    return super.toString() +
           ansi.setForeground(ansi.red) + " | subordonati: " + subordonati.size() + ansi.reset();
  }
}
