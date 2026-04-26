package com.pao.proiectCabinetMedical.model;

import java.util.Objects;

import com.pao.proiectCabinetMedical.utils.Ansi;

public class Sali {

  private Ansi a = Ansi.getInstance();
  private int numar;
  private String cladire;

  public Sali(int numar, String cladire){
    this.numar = numar;
    this.cladire = cladire == null ? "" : cladire.trim();
  }

  public int getNumar(){
    return this.numar;
  }

  public String getCladire(){
    return this.cladire;
  }

  public void setNumar(int numar){
    this.numar = numar;
  }

  public void setCladire(String cladire){
    this.cladire = cladire == null ? "" : cladire.trim();
  }

  @Override
  public String toString(){
    return a.title("Sala: ") +
           a.value(String.valueOf(numar)) +
           a.setForeground(a.blue) + " | cladire: " + cladire + a.reset();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Sali sali = (Sali) o;
    return numar == sali.numar && Objects.equals(cladire, sali.cladire);
  }

  @Override
  public int hashCode() {
    return Objects.hash(numar, cladire);
  }
}
