package com.pao.proiectCabinetMedical.model;

import java.util.Objects;

import com.pao.proiectCabinetMedical.utils.Ansi;

public class Medicamente {

  private Ansi a = Ansi.getInstance();
  private String denumire;
  private int stoc;

  public Medicamente(String denumire, int stoc){
    this.denumire = denumire == null ? "" : denumire.trim();
    this.stoc = Math.max(0, stoc);
  }

  public String getDenumire(){
    return this.denumire;
  }

  public int getStoc(){
    return this.stoc;
  }

  public void setDenumire(String denumire){
    this.denumire = denumire == null ? "" : denumire.trim();
  }

  public void setStoc(int stoc){
    this.stoc = Math.max(0, stoc);
  }

  @Override
  public String toString(){
    return a.title("Medicament: ") +
           a.value(denumire) +
           a.setForeground(a.orange) + " | stoc: " + stoc + a.reset();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Medicamente that = (Medicamente) o;
    return Objects.equals(denumire, that.denumire);
  }

  @Override
  public int hashCode() {
    return Objects.hash(denumire);
  }
}
