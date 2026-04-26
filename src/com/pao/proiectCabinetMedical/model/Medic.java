package com.pao.proiectCabinetMedical.model;

import com.pao.proiectCabinetMedical.utils.Ansi;
import com.pao.proiectCabinetMedical.utils.MedicIdGenerator;

public class Medic extends Persoana implements Comparable<Medic> {

  protected Ansi ansi = Ansi.getInstance();
  private static final MedicIdGenerator GENERATOR = MedicIdGenerator.getInstance();
  private final int id;
  private boolean deGarda;
  private int aniExperienta;
  private boolean rezident;
  private String departament;

  public Medic(String firstName, String lastName, boolean deGarda, int aniExperienta, boolean rezident, String departament){
    super(firstName, lastName);
    this.id = GENERATOR.getCurrentId();
    this.deGarda = deGarda;
    this.aniExperienta = Math.max(0, aniExperienta);
    this.rezident = rezident;
    this.departament = departament == null ? "" : departament.trim();
  }

  public int getId(){
    return this.id;
  }

  public boolean isDeGarda(){
    return this.deGarda;
  }

  public int getAniExperienta(){
    return this.aniExperienta;
  }

  public boolean isRezident(){
    return this.rezident;
  }

  public String getDepartament(){
    return this.departament;
  }

  public void setDeGarda(boolean deGarda){
    this.deGarda = deGarda;
  }

  public void setAniExperienta(int aniExperienta){
    this.aniExperienta = Math.max(0, aniExperienta);
  }

  public void setRezident(boolean rezident){
    this.rezident = rezident;
  }

  public void setDepartament(String departament){
    this.departament = departament == null ? "" : departament.trim();
  }

  @Override
  public String getRol(){
    return "Medic";
  }

  @Override
  public String toString(){
    return ansi.title(getRol() + " #" + id + ": ") +
           ansi.value(getFirstName() + " " + getLastName()) +
           ansi.setForeground(ansi.blue) + " | departament: " + departament + ansi.reset() +
           ansi.setForeground(ansi.orange) + " | experienta: " + aniExperienta + " ani" + ansi.reset() +
           ansi.setForeground(ansi.cyan) + " | de garda: " + deGarda + ansi.reset() +
           ansi.setForeground(ansi.yellow) + " | rezident: " + rezident + ansi.reset();
  }

  @Override
  public int compareTo(Medic other) {
    int byLastName = this.getLastName().compareToIgnoreCase(other.getLastName());
    if (byLastName != 0){
      return byLastName;
    }
    int byFirstName = this.getFirstName().compareToIgnoreCase(other.getFirstName());
    if (byFirstName != 0){
      return byFirstName;
    }
    return Integer.compare(this.id, other.id);
  }
}
