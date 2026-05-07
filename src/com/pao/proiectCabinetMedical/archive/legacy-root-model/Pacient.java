package com.pao.proiectCabinetMedical;

import com.pao.proiectCabinetMedical.utils.Ansi;

public class Pacient extends Persoana {

  Ansi a = Ansi.getInstance();
  private String diagnostic;
  private boolean urgenta;
  private Medic medicSupervizor;

  public Pacient(String firstName, String lastName, String diagnostic, boolean urgenta, Medic medicSupervizor){
    super(firstName, lastName);
    this.diagnostic = diagnostic;
    this.urgenta = urgenta;
    this.medicSupervizor = medicSupervizor;
  }

  public String getDiagnostic(){
    return this.diagnostic;
  }

  public boolean getUrgenta(){
    return this.urgenta;
  }

  public Medic getMedicSupervizor(){
    return this.medicSupervizor;
  }

  public void setDiagnostic(String diagnostic){
    this.diagnostic = diagnostic;
  }

  public void setUrgenta(boolean urgenta){
    this.urgenta = urgenta;
  }

  public void setMedicSupervizor(Medic medicSupervizor){
    this.medicSupervizor = medicSupervizor;
  }

  @Override
  public String toString(){
    String medicName = medicSupervizor == null ? "fara medic" : medicSupervizor.getfirstname() + " " + medicSupervizor.getLastName();
    return a.setForeground(a.purple) + "Pacient: " + a.reset() + super.toString() +
           a.setForeground(a.red) + " | diagnostic: " + diagnostic + a.reset() +
           a.setForeground(a.orange) + " | urgenta: " + urgenta + a.reset() +
           a.setForeground(a.blue) + " | medic supervizor: " + medicName + a.reset();
  }
}
