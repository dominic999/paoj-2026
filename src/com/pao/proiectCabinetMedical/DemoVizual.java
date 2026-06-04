package com.pao.proiectCabinetMedical;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import com.pao.proiectCabinetMedical.exception.EntityNotFoundException;
import com.pao.proiectCabinetMedical.exception.InvalidEntityDataException;
import com.pao.proiectCabinetMedical.exception.StocInsuficientException;
import com.pao.proiectCabinetMedical.model.Asistenta;
import com.pao.proiectCabinetMedical.model.Manager;
import com.pao.proiectCabinetMedical.model.MedicalRecord;
import com.pao.proiectCabinetMedical.model.Medic;
import com.pao.proiectCabinetMedical.model.MedicSpecialist;
import com.pao.proiectCabinetMedical.model.Medicamente;
import com.pao.proiectCabinetMedical.model.Pacient;
import com.pao.proiectCabinetMedical.model.Sali;
import com.pao.proiectCabinetMedical.repository.MedicRepository;
import com.pao.proiectCabinetMedical.repository.MedicamentRepository;
import com.pao.proiectCabinetMedical.repository.PacientRepository;
import com.pao.proiectCabinetMedical.repository.SalaRepository;
import com.pao.proiectCabinetMedical.service.AsistentaService;
import com.pao.proiectCabinetMedical.service.AuditService;
import com.pao.proiectCabinetMedical.service.ManagerService;
import com.pao.proiectCabinetMedical.service.MedicamenteService;
import com.pao.proiectCabinetMedical.service.MedicService;
import com.pao.proiectCabinetMedical.service.MedicSpecialistService;
import com.pao.proiectCabinetMedical.service.PacientService;
import com.pao.proiectCabinetMedical.service.PrescriptieService;
import com.pao.proiectCabinetMedical.service.SaliService;
import com.pao.proiectCabinetMedical.utils.Ansi;
import com.pao.proiectCabinetMedical.utils.SchemaRunner;

/**
 * Demo vizual: parcurge toate functionalitatile proiectului (Etapa 1 + Etapa 2),
 * afisand starea sistemului inainte si dupa fiecare operatie.
 *
 * Rulare: make demo
 */
public class DemoVizual {

  private static final Ansi ANSI = Ansi.getInstance();
  private static int pas = 0;

  public static void main(String[] args) throws Exception {
    banner("DEMO VIZUAL - SISTEM CABINET MEDICAL");

    etapa1Servicii();
    etapa1Colectii();
    etapa1Exceptii();
    etapa2Schema();
    etapa2CrudMedici();
    etapa2CrudSali();
    etapa2CrudMedicamente();
    etapa2TranzactiiPacienti();
    etapa2TranzactiiPrescriptii();
    etapa2Joinuri();
    audit();

    banner("DEMO INCHEIAT - toate functionalitatile au fost demonstrate");
  }

  // ================= ETAPA 1 =================

  private static void etapa1Servicii() throws InvalidEntityDataException, EntityNotFoundException {
    sectiune("ETAPA 1 / Servicii Singleton + CRUD in memorie");

    MedicService medici = MedicService.getInstance();
    pas("Adaug 3 medici (dintre care un specialist)");
    Medic cardiolog = new Medic("Andrei", "Tirdea", true, 7, false, "cardiologie");
    Medic rezident = new Medic("Miruna", "Zaharia", false, 2, true, "urgente");
    MedicSpecialist chirurg = new MedicSpecialist("Dominic", "Ionescu", false, 10, false, "chirurgie",
        new String[]{"chirurgie", "urologie"});
    medici.addMedic(cardiolog);
    medici.addMedic(rezident);
    medici.addMedic(chirurg);
    stare("Medici in memorie", medici.getAllMedici());

    pas("Caut medicul cu id " + cardiolog.getId() + " (cautare indexata in Map)");
    rezultat(medici.findMedicById(cardiolog.getId()));

    pas("Filtrez medicii din departamentul 'urgente' (Map<String, List<Medic>>)");
    stare("Medici din urgente", medici.findMediciByDepartament("urgente"));

    pas("Sterg medicul rezident, apoi listez din nou");
    medici.deleteMedic(rezident.getId());
    stare("Medici ramasi", medici.getAllMedici());

    pas("Polimorfism: getRol() intoarce alt text pe fiecare nivel al ierarhiei");
    rezultat("Medic.getRol()           -> " + cardiolog.getRol());
    rezultat("MedicSpecialist.getRol() -> " + chirurg.getRol());

    pas("Adaug o specializare noua chirurgului (MedicSpecialistService)");
    MedicSpecialistService specialisti = MedicSpecialistService.getInstance();
    specialisti.addMedicSpecialist(chirurg);
    specialisti.addSpecToMedic(chirurg.getId(), "laparoscopie");
    rezultat(specialisti.findMedicSpecialistById(chirurg.getId()));

    pas("Manager si Asistenta (restul ierarhiei de persoane)");
    Manager manager = new Manager("Ioan", "Marin", false, 12, false, "administrativ", List.of(cardiolog));
    Asistenta asistenta = new Asistenta("Maria", "Georgescu", 6, List.of(new Sali(101, "A")));
    ManagerService.getInstance().addManager(manager);
    AsistentaService.getInstance().addAsistenta(asistenta);
    rezultat(manager);
    rezultat(asistenta);

    pas("Pacient cu fisa medicala imutabila (MedicalRecord: final, fara setteri)");
    MedicalRecord fisa = new MedicalRecord("REC-100", LocalDate.of(2026, 5, 20), "fractura inchisa");
    Pacient pacient = new Pacient("Ana", "Popescu", "fractura", true, cardiolog, fisa);
    PacientService.getInstance().addPacient(pacient);
    rezultat(pacient);
    rezultat("Fisa (read-only): " + fisa);

    pas("Cautari pe pacienti: dupa nume, dupa diagnostic, doar urgente");
    PacientService pacienti = PacientService.getInstance();
    rezultat(pacienti.findPacientByName("ana", "POPESCU") + ANSI.label("  <- cautare case-insensitive"));
    stare("Pacienti cu diagnostic 'fractura'", pacienti.findPacientiByDiagnostic("fractura"));
    stare("Pacienti urgenti", pacienti.getUrgentPacienti());
  }

  private static void etapa1Colectii() throws InvalidEntityDataException, EntityNotFoundException {
    sectiune("ETAPA 1 / Colectii: List, Map, TreeSet sortat, Set");

    pas("TreeSet<Medic> sortat alfabetic (Comparable: nume, prenume, id)");
    stare("Medici sortati", MedicService.getInstance().getSortedMedici().stream().toList());

    pas("Sali intr-un TreeSet cu Comparator (cladire, apoi numar) - le adaug intentionat invers");
    SaliService sali = SaliService.getInstance();
    sali.addSala(new Sali(305, "B"));
    sali.addSala(new Sali(101, "B"));
    sali.addSala(new Sali(204, "A"));
    stare("Sali (sortate automat la afisare)", sali.getAllSali());

    pas("Medicamente intr-un LinkedHashSet (fara duplicate)");
    MedicamenteService medicamente = MedicamenteService.getInstance();
    medicamente.addMedicament(new Medicamente("Paracetamol", 100));
    medicamente.addMedicament(new Medicamente("Ibuprofen", 50));
    stare("Medicamente in stoc", medicamente.getAllMedicamente());

    pas("Sterg un medicament si arat ca dispare din colectie");
    medicamente.deleteMedicament("Ibuprofen");
    stare("Medicamente dupa stergere", medicamente.getAllMedicamente());
  }

  private static void etapa1Exceptii(){
    sectiune("ETAPA 1 / Exceptii custom - aruncate si tratate");

    pas("InvalidEntityDataException: incerc sa adaug un medic null");
    try {
      MedicService.getInstance().addMedic(null);
    } catch (InvalidEntityDataException e){
      tratat(e);
    }

    pas("InvalidEntityDataException: medic fara nume");
    try {
      MedicService.getInstance().addMedic(new Medic("", "", false, 1, false, "x"));
    } catch (InvalidEntityDataException e){
      tratat(e);
    }

    pas("EntityNotFoundException: caut un medic cu id inexistent (9999)");
    try {
      MedicService.getInstance().findMedicById(9999);
    } catch (EntityNotFoundException e){
      tratat(e);
    }

    pas("EntityNotFoundException: caut un medicament sters");
    try {
      MedicamenteService.getInstance().findMedicamentByName("Ibuprofen");
    } catch (EntityNotFoundException e){
      tratat(e);
    }
  }

  // ================= ETAPA 2 =================

  private static void etapa2Schema(){
    sectiune("ETAPA 2 / Schema bazei de date (PostgreSQL + JDBC)");

    pas("Rulez resources/schema.sql: DROP TABLE IF EXISTS + CREATE TABLE (6 tabele, PK toate, 4 FK)");
    SchemaRunner.run("resources/schema.sql");
    rezultat("Schema initializata. Tabele: medic, medical_record, pacient, sala, medicament, prescriptie");
    rezultat("Conexiunea e configurata din resources/db.properties (DatabaseConnection - Singleton)");
  }

  private static void etapa2CrudMedici(){
    sectiune("ETAPA 2 / CRUD complet: MedicRepository (Repository<Medic, Integer>)");

    MedicRepository repo = MedicRepository.getInstance();
    Medic medic = new Medic("Elena", "Vasile", true, 9, false, "neurologie");
    MedicSpecialist specialist = new MedicSpecialist("Radu", "Popa", false, 14, false, "ortopedie",
        new String[]{"ortopedie", "traumatologie"});

    pas("save: inserez un medic si un specialist");
    repo.save(medic);
    repo.save(specialist);
    stare("SELECT * FROM medic", repo.findAll());

    pas("findById(" + medic.getId() + "): citesc inapoi din baza");
    rezultat(repo.findById(medic.getId()).orElseThrow());

    pas("update: medicul primeste +1 an experienta si trece de garda");
    medic.setAniExperienta(10);
    medic.setDeGarda(false);
    repo.update(medic);
    rezultat(repo.findById(medic.getId()).orElseThrow());

    pas("specialistul isi pastreaza tipul si specializarile dupa round-trip in baza");
    rezultat(repo.findById(specialist.getId()).orElseThrow());

    pas("delete(" + specialist.getId() + "): sterg specialistul");
    repo.delete(specialist.getId());
    stare("SELECT * FROM medic (dupa delete)", repo.findAll());
  }

  private static void etapa2CrudSali(){
    sectiune("ETAPA 2 / CRUD: SalaRepository - id generat de baza (SERIAL)");

    SalaRepository repo = SalaRepository.getInstance();
    Sali sala = new Sali(401, "C");

    pas("inainte de save: sala.getId() = " + sala.getId());
    repo.save(sala);
    rezultat("dupa save: sala.getId() = " + sala.getId() + ANSI.label("  <- completat de PostgreSQL"));

    pas("update: mut sala la numarul 405");
    sala.setNumar(405);
    repo.update(sala);
    stare("SELECT * FROM sala", repo.findAll());

    pas("delete: sterg sala");
    repo.delete(sala.getId());
    stare("SELECT * FROM sala (dupa delete)", repo.findAll());
  }

  private static void etapa2CrudMedicamente(){
    sectiune("ETAPA 2 / CRUD: MedicamentRepository - cheie primara String");

    MedicamentRepository repo = MedicamentRepository.getInstance();
    pas("save: trei medicamente (PK = denumirea, demonstreaza Repository<T, String>)");
    repo.save(new Medicamente("Paracetamol", 100));
    repo.save(new Medicamente("Ibuprofen", 5));
    repo.save(new Medicamente("Algocalmin", 60));
    stare("SELECT * FROM medicament", repo.findAll());

    pas("findById(\"Ibuprofen\") + update stoc 5 -> 8");
    repo.update(new Medicamente("Ibuprofen", 8));
    rezultat(repo.findById("Ibuprofen").orElseThrow());

    pas("delete(\"Algocalmin\")");
    repo.delete("Algocalmin");
    stare("SELECT * FROM medicament (dupa delete)", repo.findAll());
  }

  private static void etapa2TranzactiiPacienti(){
    sectiune("ETAPA 2 / Tranzactie 1: internare pacient (medical_record + pacient, atomic)");

    MedicRepository medicRepo = MedicRepository.getInstance();
    PacientRepository pacientRepo = PacientRepository.getInstance();
    Medic supervizor = medicRepo.findAll().get(0);

    pas("save pacient: insereaza fisa in medical_record SI pacientul in pacient, in aceeasi tranzactie");
    Pacient ana = new Pacient("Ana", "Popescu", "fractura", true, supervizor,
        new MedicalRecord("REC-501", LocalDate.of(2026, 6, 1), "fractura inchisa"));
    Pacient vlad = new Pacient("Vlad", "Stan", "apendicita", true, supervizor,
        new MedicalRecord("REC-502", LocalDate.of(2026, 6, 2), "apendicita acuta"));
    pacientRepo.save(ana);
    pacientRepo.save(vlad);
    stare("SELECT cu JOIN pe medic si medical_record", pacientRepo.findAll());

    pas("pacientul reconstruit din baza contine medicul si fisa (JOIN pe 3 tabele)");
    Pacient reconstruit = pacientRepo.findById(ana.getId()).orElseThrow();
    rezultat(reconstruit);
    rezultat("Fisa lui: " + reconstruit.getRecord());
  }

  private static void etapa2TranzactiiPrescriptii(){
    sectiune("ETAPA 2 / Tranzactie 2: prescriptie cu COMMIT si ROLLBACK");

    MedicamentRepository medRepo = MedicamentRepository.getInstance();
    PrescriptieService prescriptii = PrescriptieService.getInstance();
    int pacientId = PacientRepository.getInstance().findAll().get(0).getId();

    pas("STOC INAINTE de prescriptii");
    stare("Stoc curent", medRepo.findAll());

    pas("COMMIT: prescriu Paracetamol x30 (insert prescriptie + UPDATE stoc, atomic)");
    try {
      prescriptii.prescrieMedicament(pacientId, "Paracetamol", 30);
      rezultat("Tranzactie finalizata cu commit.");
    } catch (StocInsuficientException e){
      tratat(e);
    }
    stare("Stoc dupa commit (Paracetamol 100 -> 70)", medRepo.findAll());

    pas("ROLLBACK: incerc Ibuprofen x999 - stoc insuficient");
    try {
      prescriptii.prescrieMedicament(pacientId, "Ibuprofen", 999);
    } catch (StocInsuficientException e){
      tratat(e);
    }
    stare("Stoc dupa rollback (NIMIC nu s-a modificat)", medRepo.findAll());
  }

  private static void etapa2Joinuri(){
    sectiune("ETAPA 2 / Interogari avansate cu JOIN");

    pas("JOIN 1: pacientii urgenti cu datele medicului supervizor (pacient JOIN medic)");
    stare("Rezultat", PacientRepository.getInstance().findUrgentiCuMedic());

    pas("JOIN 2: numarul de pacienti per medic (medic LEFT JOIN pacient + GROUP BY)");
    stare("Rezultat", MedicRepository.getInstance().countPacientiPerMedic());

    pas("JOIN 3: cele mai prescrise medicamente (prescriptie JOIN medicament + GROUP BY)");
    stare("Rezultat", PrescriptieService.getInstance().topMedicamentePrescrise());

    pas("JOIN 4: prescriptiile unui pacient (prescriptie JOIN pacient JOIN medicament - 3 tabele)");
    int pacientId = PacientRepository.getInstance().findAll().get(0).getId();
    stare("Rezultat", PrescriptieService.getInstance().prescriptiilePacientului(pacientId));
  }

  private static void audit() throws Exception {
    sectiune("ETAPA 2 / AuditService - CSV, append, thread-safe");

    pas("Toate actiunile de mai sus au fost deja logate automat. Demonstrez si scrierea concurenta:");
    List<Thread> pool = new java.util.ArrayList<>();
    for (int t = 0; t < 5; t++){
      final int idx = t;
      pool.add(new Thread(() -> {
        for (int i = 0; i < 3; i++){
          AuditService.getInstance().log("demo_thread_" + idx);
        }
      }));
    }
    for (Thread t : pool) t.start();
    for (Thread t : pool) t.join();
    rezultat("5 thread-uri x 3 actiuni scrise concurent, fara coruperea fisierului (metoda e synchronized).");

    pas("Ultimele 10 linii din audit.csv (fisierul creste intre rulari - mod append):");
    List<String> lines = Files.readAllLines(Path.of("audit.csv"));
    rezultat("total linii in fisier: " + lines.size());
    for (int i = Math.max(0, lines.size() - 10); i < lines.size(); i++){
      System.out.println("      " + ANSI.label(lines.get(i)));
    }
  }

  // ================= helpere de afisare =================

  private static void banner(String text){
    String border = "=".repeat(text.length() + 8);
    System.out.println(ANSI.title(border));
    System.out.println(ANSI.title("==  " + text + "  =="));
    System.out.println(ANSI.title(border));
  }

  private static void sectiune(String titlu){
    System.out.println();
    System.out.println(ANSI.setForeground(ANSI.cyan) + "+" + "-".repeat(70) + ANSI.reset());
    System.out.println(ANSI.setForeground(ANSI.cyan) + "| " + titlu + ANSI.reset());
    System.out.println(ANSI.setForeground(ANSI.cyan) + "+" + "-".repeat(70) + ANSI.reset());
  }

  private static void pas(String descriere){
    pas++;
    System.out.println();
    System.out.println(ANSI.option(pas, descriere));
  }

  private static void rezultat(Object value){
    System.out.println("    " + value);
  }

  private static void stare(String label, List<?> values){
    System.out.println("    " + ANSI.label(label + ":"));
    if (values.isEmpty()){
      System.out.println("      " + ANSI.warning("(gol)"));
      return;
    }
    for (Object value : values){
      System.out.println("      " + value);
    }
  }

  private static void tratat(Exception e){
    System.out.println("    " + ANSI.warning("Exceptie tratata [" + e.getClass().getSimpleName() + "]: " + e.getMessage()));
  }
}
