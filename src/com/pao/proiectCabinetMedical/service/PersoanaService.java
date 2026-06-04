package com.pao.proiectCabinetMedical.service;

import java.util.ArrayList;
import java.util.List;

import com.pao.proiectCabinetMedical.exception.EntityNotFoundException;
import com.pao.proiectCabinetMedical.exception.InvalidEntityDataException;
import com.pao.proiectCabinetMedical.model.Persoana;

public class PersoanaService {

  private static final AuditService AUDIT = AuditService.getInstance();

  private final List<Persoana> persoane;

  private PersoanaService(){
    this.persoane = new ArrayList<>();
  }

  private static class Holder{
    private static final PersoanaService INSTANCE = new PersoanaService();
  }

  public static PersoanaService getInstance(){
    return Holder.INSTANCE;
  }

  public void addPersoana(Persoana persoana) throws InvalidEntityDataException {
    AUDIT.log("addPersoana");
    if (persoana == null){
      throw new InvalidEntityDataException("Persoana nu poate fi null.");
    }
    if (persoana.getFirstName().isEmpty() || persoana.getLastName().isEmpty()){
      throw new InvalidEntityDataException("Persoana trebuie sa aiba prenume si nume.");
    }
    persoane.add(persoana);
  }

  public void deletePersoana(String firstName, String lastName) throws EntityNotFoundException {
    AUDIT.log("deletePersoana");
    Persoana persoana = findPersoanaByName(firstName, lastName);
    persoane.remove(persoana);
  }

  public Persoana findPersoanaByName(String firstName, String lastName) throws EntityNotFoundException {
    AUDIT.log("findPersoanaByName");
    for (Persoana persoana : persoane){
      if (persoana.getFirstName().equalsIgnoreCase(firstName) &&
          persoana.getLastName().equalsIgnoreCase(lastName)){
        return persoana;
      }
    }
    throw new EntityNotFoundException("Persoana nu a fost gasita: " + firstName + " " + lastName);
  }

  public List<Persoana> getAllPersoane(){
    AUDIT.log("getAllPersoane");
    return new ArrayList<>(persoane);
  }
}
