package com.pao.proiectCabinetMedical.model;

import java.time.LocalDate;
import java.util.Objects;

public final class MedicalRecord {

  private final String code;
  private final LocalDate createdAt;
  private final String notes;

  public MedicalRecord(String code, LocalDate createdAt, String notes){
    this.code = code == null ? "" : code.trim();
    this.createdAt = createdAt == null ? LocalDate.now() : createdAt;
    this.notes = notes == null ? "" : notes.trim();
  }

  public String getCode(){
    return this.code;
  }

  public LocalDate getCreatedAt(){
    return this.createdAt;
  }

  public String getNotes(){
    return this.notes;
  }

  @Override
  public String toString(){
    return "MedicalRecord{code='" + code + "', createdAt=" + createdAt + ", notes='" + notes + "'}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MedicalRecord that = (MedicalRecord) o;
    return Objects.equals(code, that.code) &&
           Objects.equals(createdAt, that.createdAt) &&
           Objects.equals(notes, that.notes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(code, createdAt, notes);
  }
}
