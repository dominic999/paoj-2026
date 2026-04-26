package com.pao.proiectCabinetMedical.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.pao.proiectCabinetMedical.exception.EntityNotFoundException;
import com.pao.proiectCabinetMedical.exception.InvalidEntityDataException;
import com.pao.proiectCabinetMedical.model.Medic;

public class MedicService {

  private final List<Medic> medici;
  private final Map<Integer, Medic> mediciById;
  private final Map<String, List<Medic>> mediciByDepartament;

  private MedicService() {
    this.medici = new ArrayList<>();
    this.mediciById = new HashMap<>();
    this.mediciByDepartament = new HashMap<>();
  }

  private static class Holder{
    private static final MedicService INSTANCE = new MedicService();
  }

  public static MedicService getInstance(){
    return Holder.INSTANCE;
  }

  public void addMedic(Medic medic) throws InvalidEntityDataException {
    if (medic == null){
      throw new InvalidEntityDataException("Medicul nu poate fi null.");
    }
    if (medic.getFirstName().isEmpty() || medic.getLastName().isEmpty()){
      throw new InvalidEntityDataException("Medicul trebuie sa aiba nume si prenume.");
    }
    medici.add(medic);
    mediciById.put(medic.getId(), medic);
    mediciByDepartament.computeIfAbsent(medic.getDepartament(), key -> new ArrayList<>()).add(medic);
  }

  public void deleteMedic(int id) throws EntityNotFoundException {
    Medic medic = findMedicById(id);
    medici.remove(medic);
    mediciById.remove(id);
    List<Medic> departamentMedics = mediciByDepartament.get(medic.getDepartament());
    if (departamentMedics != null){
      departamentMedics.remove(medic);
      if (departamentMedics.isEmpty()){
        mediciByDepartament.remove(medic.getDepartament());
      }
    }
  }

  public Medic findMedicById(int id) throws EntityNotFoundException {
    Medic medic = mediciById.get(id);
    if (medic == null){
      throw new EntityNotFoundException("Nu exista medic cu id-ul " + id + ".");
    }
    return medic;
  }

  public List<Medic> findMediciByDepartament(String departament){
    return new ArrayList<>(mediciByDepartament.getOrDefault(departament, new ArrayList<>()));
  }

  public List<Medic> getAllMedici(){
    return new ArrayList<>(medici);
  }

  public TreeSet<Medic> getSortedMedici(){
    return new TreeSet<>(medici);
  }
}
