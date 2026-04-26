package com.pao.proiectCabinetMedical.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pao.proiectCabinetMedical.exception.EntityNotFoundException;
import com.pao.proiectCabinetMedical.exception.InvalidEntityDataException;
import com.pao.proiectCabinetMedical.model.Pacient;

public class PacientService {

  private final List<Pacient> pacienti;
  private final Map<String, List<Pacient>> pacientiByDiagnostic;

  private PacientService(){
    this.pacienti = new ArrayList<>();
    this.pacientiByDiagnostic = new HashMap<>();
  }

  private static class Holder{
    private static final PacientService INSTANCE = new PacientService();
  }

  public static PacientService getInstance(){
    return Holder.INSTANCE;
  }

  public void addPacient(Pacient pacient) throws InvalidEntityDataException {
    if (pacient == null){
      throw new InvalidEntityDataException("Pacientul nu poate fi null.");
    }
    if (pacient.getFirstName().isEmpty() || pacient.getLastName().isEmpty()){
      throw new InvalidEntityDataException("Pacientul trebuie sa aiba nume si prenume.");
    }
    pacienti.add(pacient);
    pacientiByDiagnostic.computeIfAbsent(pacient.getDiagnostic(), key -> new ArrayList<>()).add(pacient);
  }

  public void deletePacient(String firstName, String lastName) throws EntityNotFoundException {
    Pacient pacient = findPacientByName(firstName, lastName);
    pacienti.remove(pacient);
    List<Pacient> diagnosticGroup = pacientiByDiagnostic.get(pacient.getDiagnostic());
    if (diagnosticGroup != null){
      diagnosticGroup.remove(pacient);
      if (diagnosticGroup.isEmpty()){
        pacientiByDiagnostic.remove(pacient.getDiagnostic());
      }
    }
  }

  public Pacient findPacientByName(String firstName, String lastName) throws EntityNotFoundException {
    for (Pacient pacient : pacienti){
      if (pacient.getFirstName().equalsIgnoreCase(firstName) &&
          pacient.getLastName().equalsIgnoreCase(lastName)){
        return pacient;
      }
    }
    throw new EntityNotFoundException("Pacientul nu a fost gasit: " + firstName + " " + lastName);
  }

  public List<Pacient> findPacientiByDiagnostic(String diagnostic){
    return new ArrayList<>(pacientiByDiagnostic.getOrDefault(diagnostic, new ArrayList<>()));
  }

  public List<Pacient> getUrgentPacienti(){
    List<Pacient> urgent = new ArrayList<>();
    for (Pacient pacient : pacienti){
      if (pacient.isUrgenta()){
        urgent.add(pacient);
      }
    }
    return urgent;
  }

  public List<Pacient> getAllPacienti(){
    return new ArrayList<>(pacienti);
  }
}
