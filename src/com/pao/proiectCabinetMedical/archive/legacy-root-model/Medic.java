package com.pao.proiectCabinetMedical;
import com.pao.proiectCabinetMedical.utils.Ansi;
import com.pao.proiectCabinetMedical.utils.MedicIdGenerator;

public class Medic extends Persoana {

  Ansi a = Ansi.getInstance();
  static MedicIdGenerator generator = MedicIdGenerator.getInstance();
  private final int id = generator.getCurrentId();
  private boolean deGarda;
  private int aniExperienta;
  private boolean rezident;
  private String departament;

  public Medic(String firstName, String lastName, boolean deGarda, int aniExperienta, boolean rezident, String departament){
    super(firstName, lastName);
    this.deGarda = deGarda;
    this.aniExperienta = aniExperienta;
    this.rezident = rezident;
    this.departament = departament;
  }

  public int getId(){
    return this.id;
  }

  public boolean getDeGarda(){
    return this.deGarda;
  }

  public int getAniExperienta(){
    return this.aniExperienta;
  }

  public boolean getRezident(){
    return this.rezident;
  }

  public String getDepartament(){
    return this.departament;
  }

  public void setDeGarda(boolean value){
    this.deGarda = value;
  }

  public void setAniExperienta(int aniExperienta){
    this.aniExperienta = aniExperienta;
  }

  public void setRezident(boolean value){
    this.rezident = value;
  }

  public void setDepartament(String departament){
    this.departament = departament;
  }

  @Override
  public String toString(){
    return   a.setForeground(a.purple) + "(id)" + id + ": " + a.reset() +
              a.setForeground(a.green) + getfirstname() + " " + getLastName() + a.reset() +
              a.setForeground(a.blue) + " | departament: " + departament + a.reset() +
              a.setForeground(a.orange) + " | experienta: " + aniExperienta + " ani" + a.reset() +
              a.setForeground(a.cyan) + " | de garda: " + deGarda + a.reset() +
              a.setForeground(a.yellow) + " | rezident: " + rezident + a.reset();

  }
}
