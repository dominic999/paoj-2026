package com.pao.proiectCabinetMedical;

import com.pao.proiectCabinetMedical.utils.Ansi;

public class Medicamente {

  Ansi a = Ansi.getInstance();
  private String denumire;
  private int stoc;

  public Medicamente(String denumire, int stoc){
    this.denumire = denumire;
    this.stoc = stoc;
  }

  public String getDenumire(){
    return this.denumire;
  }

  public int getStoc(){
    return this.stoc;
  }

  public void setDenumire(String denumire){
    this.denumire = denumire;
  }

  public void setStoc(int stoc){
    this.stoc = stoc;
  }

  @Override
  public String toString(){
    return a.setForeground(a.purple) + "Medicament: " + a.reset() +
           a.setForeground(a.green) + denumire + a.reset() +
           a.setForeground(a.orange) + " | stoc: " + stoc + a.reset();
  }
}
