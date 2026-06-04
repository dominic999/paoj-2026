package com.pao.proiectCabinetMedical.model;

public class Pacient extends Persoana {

  private Integer id; // atribuit de baza de date (Etapa 2), null cat timp pacientul nu e persistat
  private String diagnostic;
  private boolean urgenta;
  private Medic medicSupervizor;
  private final MedicalRecord record;

  public Pacient(String firstName, String lastName, String diagnostic, boolean urgenta, Medic medicSupervizor, MedicalRecord record){
    super(firstName, lastName);
    this.diagnostic = diagnostic == null ? "" : diagnostic.trim();
    this.urgenta = urgenta;
    this.medicSupervizor = medicSupervizor;
    this.record = record;
  }

  public Integer getId(){
    return this.id;
  }

  public void setId(Integer id){
    this.id = id;
  }

  public String getDiagnostic(){
    return this.diagnostic;
  }

  public boolean isUrgenta(){
    return this.urgenta;
  }

  public Medic getMedicSupervizor(){
    return this.medicSupervizor;
  }

  public MedicalRecord getRecord(){
    return this.record;
  }

  public void setDiagnostic(String diagnostic){
    this.diagnostic = diagnostic == null ? "" : diagnostic.trim();
  }

  public void setUrgenta(boolean urgenta){
    this.urgenta = urgenta;
  }

  public void setMedicSupervizor(Medic medicSupervizor){
    this.medicSupervizor = medicSupervizor;
  }

  @Override
  public String getRol(){
    return "Pacient";
  }

  @Override
  public String toString(){
    String medicLabel = medicSupervizor == null ? "fara medic" : medicSupervizor.getFirstName() + " " + medicSupervizor.getLastName();
    return super.toString() +
           a.setForeground(a.red) + " | diagnostic: " + diagnostic + a.reset() +
           a.setForeground(a.orange) + " | urgenta: " + urgenta + a.reset() +
           a.setForeground(a.blue) + " | medic supervizor: " + medicLabel + a.reset() +
           a.setForeground(a.gray) + " | record: " + (record == null ? "lipsa" : record.getCode()) + a.reset();
  }
}
