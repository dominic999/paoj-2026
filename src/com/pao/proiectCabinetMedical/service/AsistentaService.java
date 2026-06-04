package com.pao.proiectCabinetMedical.service;

import java.util.ArrayList;
import java.util.List;

import com.pao.proiectCabinetMedical.exception.EntityNotFoundException;
import com.pao.proiectCabinetMedical.exception.InvalidEntityDataException;
import com.pao.proiectCabinetMedical.model.Asistenta;

public class AsistentaService {

  private static final AuditService AUDIT = AuditService.getInstance();

  private final List<Asistenta> asistente;

  private AsistentaService(){
    this.asistente = new ArrayList<>();
  }

  private static class Holder{
    private static final AsistentaService INSTANCE = new AsistentaService();
  }

  public static AsistentaService getInstance(){
    return Holder.INSTANCE;
  }

  public void addAsistenta(Asistenta asistenta) throws InvalidEntityDataException {
    AUDIT.log("addAsistenta");
    if (asistenta == null){
      throw new InvalidEntityDataException("Asistenta nu poate fi null.");
    }
    asistente.add(asistenta);
  }

  public void deleteAsistenta(String firstName, String lastName) throws EntityNotFoundException {
    AUDIT.log("deleteAsistenta");
    Asistenta asistenta = findAsistentaByName(firstName, lastName);
    asistente.remove(asistenta);
  }

  public Asistenta findAsistentaByName(String firstName, String lastName) throws EntityNotFoundException {
    AUDIT.log("findAsistentaByName");
    for (Asistenta asistenta : asistente){
      if (asistenta.getFirstName().equalsIgnoreCase(firstName) &&
          asistenta.getLastName().equalsIgnoreCase(lastName)){
        return asistenta;
      }
    }
    throw new EntityNotFoundException("Asistenta nu a fost gasita: " + firstName + " " + lastName);
  }

  public List<Asistenta> getAllAsistente(){
    AUDIT.log("getAllAsistente");
    return new ArrayList<>(asistente);
  }
}
