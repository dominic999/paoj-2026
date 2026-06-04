package com.pao.proiectCabinetMedical.service;

import java.util.ArrayList;
import java.util.List;

import com.pao.proiectCabinetMedical.exception.EntityNotFoundException;
import com.pao.proiectCabinetMedical.exception.InvalidEntityDataException;
import com.pao.proiectCabinetMedical.model.Manager;

public class ManagerService {

  private static final AuditService AUDIT = AuditService.getInstance();

  private final List<Manager> manageri;

  private ManagerService(){
    this.manageri = new ArrayList<>();
  }

  private static class Holder{
    private static final ManagerService INSTANCE = new ManagerService();
  }

  public static ManagerService getInstance(){
    return Holder.INSTANCE;
  }

  public void addManager(Manager manager) throws InvalidEntityDataException {
    AUDIT.log("addManager");
    if (manager == null){
      throw new InvalidEntityDataException("Managerul nu poate fi null.");
    }
    manageri.add(manager);
  }

  public void deleteManager(int id) throws EntityNotFoundException {
    AUDIT.log("deleteManager");
    Manager manager = findManagerById(id);
    manageri.remove(manager);
  }

  public Manager findManagerById(int id) throws EntityNotFoundException {
    AUDIT.log("findManagerById");
    for (Manager manager : manageri){
      if (manager.getId() == id){
        return manager;
      }
    }
    throw new EntityNotFoundException("Nu exista manager cu id-ul " + id + ".");
  }

  public List<Manager> getAllManageri(){
    AUDIT.log("getAllManageri");
    return new ArrayList<>(manageri);
  }
}
