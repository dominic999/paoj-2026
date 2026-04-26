package com.pao.proiectCabinetMedical.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.pao.proiectCabinetMedical.exception.EntityNotFoundException;
import com.pao.proiectCabinetMedical.exception.InvalidEntityDataException;
import com.pao.proiectCabinetMedical.model.Sali;

public class SaliService {

  private final Set<Sali> sali;

  private SaliService(){
    this.sali = new TreeSet<>((left, right) -> {
      int byCladire = left.getCladire().compareToIgnoreCase(right.getCladire());
      if (byCladire != 0){
        return byCladire;
      }
      return Integer.compare(left.getNumar(), right.getNumar());
    });
  }

  private static class Holder{
    private static final SaliService INSTANCE = new SaliService();
  }

  public static SaliService getInstance(){
    return Holder.INSTANCE;
  }

  public void addSala(Sali sala) throws InvalidEntityDataException {
    if (sala == null){
      throw new InvalidEntityDataException("Sala nu poate fi null.");
    }
    sali.add(sala);
  }

  public void deleteSala(int numar, String cladire) throws EntityNotFoundException {
    Sali sala = findSala(numar, cladire);
    sali.remove(sala);
  }

  public Sali findSala(int numar, String cladire) throws EntityNotFoundException {
    for (Sali sala : sali){
      if (sala.getNumar() == numar && sala.getCladire().equalsIgnoreCase(cladire)){
        return sala;
      }
    }
    throw new EntityNotFoundException("Sala nu a fost gasita: " + numar + " / " + cladire);
  }

  public List<Sali> getAllSali(){
    return new ArrayList<>(sali);
  }
}
