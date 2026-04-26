package com.pao.proiectCabinetMedical.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.pao.proiectCabinetMedical.exception.EntityNotFoundException;
import com.pao.proiectCabinetMedical.exception.InvalidEntityDataException;
import com.pao.proiectCabinetMedical.model.Medicamente;

public class MedicamenteService {

  private final Set<Medicamente> medicamente;

  private MedicamenteService(){
    this.medicamente = new LinkedHashSet<>();
  }

  private static class Holder{
    private static final MedicamenteService INSTANCE = new MedicamenteService();
  }

  public static MedicamenteService getInstance(){
    return Holder.INSTANCE;
  }

  public void addMedicament(Medicamente medicament) throws InvalidEntityDataException {
    if (medicament == null){
      throw new InvalidEntityDataException("Medicamentul nu poate fi null.");
    }
    medicamente.add(medicament);
  }

  public void deleteMedicament(String denumire) throws EntityNotFoundException {
    Medicamente medicament = findMedicamentByName(denumire);
    medicamente.remove(medicament);
  }

  public Medicamente findMedicamentByName(String denumire) throws EntityNotFoundException {
    for (Medicamente medicament : medicamente){
      if (medicament.getDenumire().equalsIgnoreCase(denumire)){
        return medicament;
      }
    }
    throw new EntityNotFoundException("Medicamentul nu a fost gasit: " + denumire);
  }

  public List<Medicamente> getAllMedicamente(){
    return new ArrayList<>(medicamente);
  }
}
