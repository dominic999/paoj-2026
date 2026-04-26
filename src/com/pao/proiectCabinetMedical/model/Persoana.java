package com.pao.proiectCabinetMedical.model;

import java.util.Objects;

import com.pao.proiectCabinetMedical.utils.Ansi;

public abstract class Persoana {

  protected Ansi a = Ansi.getInstance();
  private String firstName;
  private String lastName;

  protected Persoana(String firstName, String lastName){
    this.firstName = firstName == null ? "" : firstName.trim();
    this.lastName = lastName == null ? "" : lastName.trim();
  }

  public String getFirstName(){
    return this.firstName;
  }

  public String getLastName(){
    return this.lastName;
  }

  public void setFirstName(String firstName){
    this.firstName = firstName == null ? "" : firstName.trim();
  }

  public void setLastName(String lastName){
    this.lastName = lastName == null ? "" : lastName.trim();
  }

  public abstract String getRol();

  @Override
  public String toString(){
    return a.title(getRol() + ": ") +
           a.value(firstName + " " + lastName);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Persoana persoana = (Persoana) o;
    return Objects.equals(firstName, persoana.firstName) &&
           Objects.equals(lastName, persoana.lastName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, lastName);
  }
}
