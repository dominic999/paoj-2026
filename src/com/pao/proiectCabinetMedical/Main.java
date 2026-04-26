package com.pao.proiectCabinetMedical;

import java.time.LocalDate;
import java.util.List;

import com.pao.proiectCabinetMedical.exception.EntityNotFoundException;
import com.pao.proiectCabinetMedical.exception.InvalidEntityDataException;
import com.pao.proiectCabinetMedical.model.Asistenta;
import com.pao.proiectCabinetMedical.model.Manager;
import com.pao.proiectCabinetMedical.model.MedicalRecord;
import com.pao.proiectCabinetMedical.model.Medic;
import com.pao.proiectCabinetMedical.model.MedicSpecialist;
import com.pao.proiectCabinetMedical.model.Medicamente;
import com.pao.proiectCabinetMedical.model.Pacient;
import com.pao.proiectCabinetMedical.model.Persoana;
import com.pao.proiectCabinetMedical.model.Sali;
import com.pao.proiectCabinetMedical.service.AsistentaService;
import com.pao.proiectCabinetMedical.service.ManagerService;
import com.pao.proiectCabinetMedical.service.MedicamenteService;
import com.pao.proiectCabinetMedical.service.MedicService;
import com.pao.proiectCabinetMedical.service.MedicSpecialistService;
import com.pao.proiectCabinetMedical.service.PacientService;
import com.pao.proiectCabinetMedical.service.PersoanaService;
import com.pao.proiectCabinetMedical.service.SaliService;
import com.pao.proiectCabinetMedical.utils.Ansi;

public class Main {

  private static final Ansi ANSI = Ansi.getInstance();
  private static final PersoanaService PERSOANA_SERVICE = PersoanaService.getInstance();
  private static final MedicService MEDIC_SERVICE = MedicService.getInstance();
  private static final MedicSpecialistService SPECIALIST_SERVICE = MedicSpecialistService.getInstance();
  private static final PacientService PACIENT_SERVICE = PacientService.getInstance();
  private static final AsistentaService ASISTENTA_SERVICE = AsistentaService.getInstance();
  private static final ManagerService MANAGER_SERVICE = ManagerService.getInstance();
  private static final SaliService SALI_SERVICE = SaliService.getInstance();
  private static final MedicamenteService MEDICAMENTE_SERVICE = MedicamenteService.getInstance();

  public static void main(String[] args){
    try {
      runDemo();
    } catch (InvalidEntityDataException | EntityNotFoundException e) {
      System.out.println(ANSI.danger("Eroare neasteptata in demo: " + e.getMessage()));
    }
  }

  private static void runDemo() throws InvalidEntityDataException, EntityNotFoundException {
    printTitle("Demo sistem Cabinet Medical");

    Sali sala1 = new Sali(101, "A");
    Sali sala2 = new Sali(202, "B");
    Medicamente medicament = new Medicamente("Paracetamol", 150);
    Medic medic1 = new Medic("Andrei", "Tirdea", true, 7, false, "cardiologie");
    Medic medic2 = new Medic("Miruna", "Zaharia", false, 2, true, "urgente");
    MedicSpecialist specialist = new MedicSpecialist("Dominic", "Ionescu", false, 10, false, "chirurgie", new String[]{"chirurgie", "urologie"});
    Manager manager = new Manager("Ioan", "Marin", false, 12, false, "administrativ", List.of(medic1, medic2));
    Asistenta asistenta = new Asistenta("Maria", "Georgescu", 6, List.of(sala1, sala2));
    MedicalRecord record = new MedicalRecord("REC-1001", LocalDate.of(2026, 4, 27), "fractura inchisa");
    Pacient pacient = new Pacient("Ana", "Popescu", "fractura", true, medic1, record);

    printAction(1, "Adauga doua sali in sistem");
    SALI_SERVICE.addSala(sala1);
    SALI_SERVICE.addSala(sala2);
    printList("Sali inregistrate", SALI_SERVICE.getAllSali());

    printAction(2, "Adauga un medicament in sistem");
    MEDICAMENTE_SERVICE.addMedicament(medicament);
    printList("Medicamente disponibile", MEDICAMENTE_SERVICE.getAllMedicamente());

    printAction(3, "Adauga doi medici");
    MEDIC_SERVICE.addMedic(medic1);
    MEDIC_SERVICE.addMedic(medic2);
    printList("Medici inregistrati", MEDIC_SERVICE.getAllMedici());

    printAction(4, "Adauga un medic specialist");
    SPECIALIST_SERVICE.addMedicSpecialist(specialist);
    printList("Medici specialisti", SPECIALIST_SERVICE.getAllMediciSpecialisti());

    printAction(5, "Adauga un manager si o asistenta");
    MANAGER_SERVICE.addManager(manager);
    ASISTENTA_SERVICE.addAsistenta(asistenta);
    printList("Manageri", MANAGER_SERVICE.getAllManageri());
    printList("Asistente", ASISTENTA_SERVICE.getAllAsistente());

    printAction(6, "Adauga un pacient cu fisa medicala imutabila");
    PACIENT_SERVICE.addPacient(pacient);
    printList("Pacienti", PACIENT_SERVICE.getAllPacienti());

    printAction(7, "Adauga toate persoanele relevante in registrul general");
    PERSOANA_SERVICE.addPersoana(medic1);
    PERSOANA_SERVICE.addPersoana(medic2);
    PERSOANA_SERVICE.addPersoana(specialist);
    PERSOANA_SERVICE.addPersoana(manager);
    PERSOANA_SERVICE.addPersoana(asistenta);
    PERSOANA_SERVICE.addPersoana(pacient);
    printList("Registru general persoane", PERSOANA_SERVICE.getAllPersoane());

    printAction(8, "Cauta un medic dupa id si afiseaza rezultatul");
    System.out.println(MEDIC_SERVICE.findMedicById(medic1.getId()));

    printAction(9, "Listeaza medicii din departamentul cardiologie");
    printList("Medici din cardiologie", MEDIC_SERVICE.findMediciByDepartament("cardiologie"));

    printAction(10, "Afiseaza medicii sortati alfabetic");
    printList("Medici sortati", MEDIC_SERVICE.getSortedMedici().stream().toList());

    printAction(11, "Adauga o noua specializare unui medic specialist");
    SPECIALIST_SERVICE.addSpecToMedic(specialist.getId(), "laparoscopie");
    System.out.println(SPECIALIST_SERVICE.findMedicSpecialistById(specialist.getId()));

    printAction(12, "Cauta un pacient dupa nume");
    System.out.println(PACIENT_SERVICE.findPacientByName("Ana", "Popescu"));

    printAction(13, "Listeaza pacientii dupa diagnostic");
    printList("Pacienti cu diagnostic fractura", PACIENT_SERVICE.findPacientiByDiagnostic("fractura"));

    printAction(14, "Afiseaza doar pacientii urgenti");
    printList("Pacienti urgenti", PACIENT_SERVICE.getUrgentPacienti());

    printAction(15, "Sterge un medicament din sistem");
    MEDICAMENTE_SERVICE.deleteMedicament("Paracetamol");
    printList("Medicamente ramase", MEDICAMENTE_SERVICE.getAllMedicamente());

    printAction(16, "Demonstreaza tratarea celor doua exceptii custom");
    try {
      MEDICAMENTE_SERVICE.addMedicament(null);
    } catch (InvalidEntityDataException e) {
      System.out.println(ANSI.warning("Exceptie tratata: " + e.getMessage()));
    }
    try {
      MEDIC_SERVICE.findMedicById(9999);
    } catch (EntityNotFoundException e) {
      System.out.println(ANSI.warning("Exceptie tratata: " + e.getMessage()));
    }
  }

  private static void printTitle(String title){
    System.out.println(ANSI.title(title));
    System.out.println(ANSI.line());
  }

  private static void printAction(int index, String action){
    System.out.println(ANSI.option(index, action));
  }

  private static void printList(String label, List<?> values){
    System.out.println(ANSI.label(label + ":"));
    if (values.isEmpty()){
      System.out.println(ANSI.warning("  - lista goala"));
      return;
    }
    for (Object value : values){
      System.out.println("  " + value);
    }
  }
}
