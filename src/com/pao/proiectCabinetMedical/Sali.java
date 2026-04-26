package com.pao.proiectCabinetMedical;

import com.pao.proiectCabinetMedical.utils.Ansi;

public class Sali {

  Ansi a = Ansi.getInstance();
  private int numar;
  private String cladire;

  public Sali(int numar, String cladire){
    this.numar = numar;
    this.cladire = cladire;
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
    this.cladire = cladire;
  }

  @Override
  public String toString(){
    return a.setForeground(a.purple) + "Sala: " + a.reset() +
           a.setForeground(a.green) + numar + a.reset() +
           a.setForeground(a.blue) + " | cladire: " + cladire + a.reset();
  }
}
