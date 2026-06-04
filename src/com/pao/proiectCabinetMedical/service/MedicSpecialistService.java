package com.pao.proiectCabinetMedical.service;

import java.util.ArrayList;
import java.util.List;

import com.pao.proiectCabinetMedical.exception.EntityNotFoundException;
import com.pao.proiectCabinetMedical.exception.InvalidEntityDataException;
import com.pao.proiectCabinetMedical.model.MedicSpecialist;

public class MedicSpecialistService {

  private static final AuditService AUDIT = AuditService.getInstance();

  private final List<MedicSpecialist> specialisti;

  private MedicSpecialistService(){
    this.specialisti = new ArrayList<>();
  }

  private static class Holder{
    private static final MedicSpecialistService INSTANCE = new MedicSpecialistService();
  }

  public static MedicSpecialistService getInstance(){
    return Holder.INSTANCE;
  }

  public void addMedicSpecialist(MedicSpecialist medic) throws InvalidEntityDataException {
    AUDIT.log("addMedicSpecialist");
    if (medic == null){
      throw new InvalidEntityDataException("Medicul specialist nu poate fi null.");
    }
    specialisti.add(medic);
  }

  public void deleteMedicSpecialist(int id) throws EntityNotFoundException {
    AUDIT.log("deleteMedicSpecialist");
    MedicSpecialist medic = findMedicSpecialistById(id);
    specialisti.remove(medic);
  }

  public MedicSpecialist findMedicSpecialistById(int id) throws EntityNotFoundException {
    AUDIT.log("findMedicSpecialistById");
    for (MedicSpecialist medic : specialisti){
      if (medic.getId() == id){
        return medic;
      }
    }
    throw new EntityNotFoundException("Nu exista medic specialist cu id-ul " + id + ".");
  }

  public void addSpecToMedic(int id, String spec) throws EntityNotFoundException, InvalidEntityDataException {
    AUDIT.log("addSpecToMedic");
    if (spec == null || spec.trim().isEmpty()){
      throw new InvalidEntityDataException("Specializarea nu poate fi goala.");
    }
    findMedicSpecialistById(id).addSpec(spec);
  }

  public List<MedicSpecialist> getAllMediciSpecialisti(){
    AUDIT.log("getAllMediciSpecialisti");
    return new ArrayList<>(specialisti);
  }
}
