package com.pao.proiectCabinetMedical;

import com.pao.proiectCabinetMedical.utils.Ansi;

public class Persoana {

  Ansi a = Ansi.getInstance();
  private String firstName;
  private String lastName;

  public Persoana(String firstName, String lastName){
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public String getfirstname(){
    return this.firstName;
  }

  public String getLastName(){
    return this.lastName;
  }

  public void setfirstname(String name){
    this.firstName = name;
  }

  public void setLastName(String name){
    this.lastName = name;
  }

  @Override
  public String toString(){
    return a.setForeground(a.green) + firstName + " " + lastName + a.reset();
  }
}
