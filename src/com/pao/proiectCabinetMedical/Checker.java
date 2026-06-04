package com.pao.proiectCabinetMedical;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.pao.proiectCabinetMedical.exception.EntityNotFoundException;
import com.pao.proiectCabinetMedical.exception.InvalidEntityDataException;
import com.pao.proiectCabinetMedical.exception.StocInsuficientException;
import com.pao.proiectCabinetMedical.model.Asistenta;
import com.pao.proiectCabinetMedical.model.MedicalRecord;
import com.pao.proiectCabinetMedical.model.Medic;
import com.pao.proiectCabinetMedical.model.MedicSpecialist;
import com.pao.proiectCabinetMedical.model.Medicamente;
import com.pao.proiectCabinetMedical.model.Pacient;
import com.pao.proiectCabinetMedical.model.Persoana;
import com.pao.proiectCabinetMedical.model.Sali;
import com.pao.proiectCabinetMedical.repository.MedicRepository;
import com.pao.proiectCabinetMedical.repository.MedicamentRepository;
import com.pao.proiectCabinetMedical.repository.PacientRepository;
import com.pao.proiectCabinetMedical.repository.Repository;
import com.pao.proiectCabinetMedical.repository.SalaRepository;
import com.pao.proiectCabinetMedical.service.AuditService;
import com.pao.proiectCabinetMedical.service.MedicamenteService;
import com.pao.proiectCabinetMedical.service.MedicService;
import com.pao.proiectCabinetMedical.service.PacientService;
import com.pao.proiectCabinetMedical.service.PrescriptieService;
import com.pao.proiectCabinetMedical.service.SaliService;
import com.pao.proiectCabinetMedical.utils.Ansi;
import com.pao.proiectCabinetMedical.utils.DatabaseConnection;
import com.pao.proiectCabinetMedical.utils.SchemaRunner;

/**
 * Checker pentru functionalitatile proiectului (Etapa 1 + Etapa 2).
 * Ruleaza teste cu assert-uri proprii si intoarce exit code 1 daca vreun test pica.
 *
 * Rulare: make check
 */
public class Checker {

  private static final Ansi ANSI = Ansi.getInstance();
  private static int passed = 0;
  private static final List<String> failures = new ArrayList<>();

  public static void main(String[] args){
    System.out.println(ANSI.title("Checker proiect Cabinet Medical"));
    System.out.println(ANSI.line());

    System.out.println(ANSI.label("-- Etapa 1: model, servicii in memorie, exceptii --"));
    testIerarhieMostenire();
    testMedicalRecordImutabil();
    testEqualsHashCode();
    testMedicService();
    testMedicSpecialist();
    testPacientService();
    testSaliService();
    testMedicamenteService();
    testExceptiiCustom();

    System.out.println(ANSI.label("-- Etapa 2: JDBC, repository-uri, tranzactii, JOIN, audit --"));
    testRepositoryInterfataGenerica();
    testSchema();
    testMedicRepositoryCrud();
    testMedicSpecialistRoundTrip();
    testSalaRepositoryCrud();
    testMedicamentRepositoryCrud();
    testPacientRepositoryTranzactie();
    testPrescriptieCommit();
    testPrescriptieRollback();
    testInterogariJoin();
    testAuditAppend();
    testAuditThreadSafe();

    System.out.println(ANSI.line());
    int total = passed + failures.size();
    if (failures.isEmpty()){
      System.out.println(ANSI.value("TOATE TESTELE AU TRECUT: " + passed + "/" + total));
    } else {
      System.out.println(ANSI.danger("TESTE PICATE: " + failures.size() + "/" + total));
      for (String failure : failures){
        System.out.println(ANSI.danger("  ✗ " + failure));
      }
      System.exit(1);
    }
  }

  // ---------- infrastructura de testare ----------

  @FunctionalInterface
  private interface Test {
    void run() throws Exception;
  }

  private static void check(String name, Test test){
    try {
      test.run();
      passed++;
      System.out.println(ANSI.value("  ✓ ") + name);
    } catch (Throwable t){
      failures.add(name + " -> " + t.getMessage());
      System.out.println(ANSI.danger("  ✗ " + name + " -> " + t.getMessage()));
    }
  }

  private static void assertTrue(boolean condition, String message){
    if (!condition){
      throw new AssertionError(message);
    }
  }

  private static void assertEquals(Object expected, Object actual, String message){
    if (expected == null ? actual != null : !expected.equals(actual)){
      throw new AssertionError(message + " (asteptat: " + expected + ", primit: " + actual + ")");
    }
  }

  private static <E extends Exception> void assertThrows(Class<E> type, Test test, String message){
    try {
      test.run();
    } catch (Exception e){
      assertTrue(type.isInstance(e), message + " (aruncat: " + e.getClass().getSimpleName() + ")");
      return;
    }
    throw new AssertionError(message + " (nu s-a aruncat nimic)");
  }

  // ---------- Etapa 1 ----------

  private static void testIerarhieMostenire(){
    check("ierarhie de mostenire pe >=2 niveluri (Persoana -> Medic -> MedicSpecialist)", () -> {
      MedicSpecialist specialist = new MedicSpecialist("Test", "Specialist", false, 5, false, "chirurgie", new String[]{"orl"});
      assertTrue(specialist instanceof Medic, "MedicSpecialist trebuie sa fie Medic");
      assertTrue((Object) specialist instanceof Persoana, "MedicSpecialist trebuie sa fie Persoana");
      assertTrue(Modifier.isAbstract(Persoana.class.getModifiers()), "Persoana trebuie sa fie abstracta");
      assertEquals("Medic specialist", specialist.getRol(), "getRol polimorfic");
    });
  }

  private static void testMedicalRecordImutabil(){
    check("MedicalRecord este imutabila (final, fara setteri, getteri corecti)", () -> {
      assertTrue(Modifier.isFinal(MedicalRecord.class.getModifiers()), "clasa trebuie sa fie final");
      for (Method m : MedicalRecord.class.getDeclaredMethods()){
        assertTrue(!m.getName().startsWith("set"), "clasa imutabila nu trebuie sa aiba setteri: " + m.getName());
      }
      MedicalRecord record = new MedicalRecord("REC-T1", LocalDate.of(2026, 1, 15), "control");
      assertEquals("REC-T1", record.getCode(), "code");
      assertEquals(LocalDate.of(2026, 1, 15), record.getCreatedAt(), "createdAt");
      assertEquals("control", record.getNotes(), "notes");
    });
  }

  private static void testEqualsHashCode(){
    check("equals si hashCode consistente pe Persoana si MedicalRecord", () -> {
      MedicalRecord r1 = new MedicalRecord("R", LocalDate.of(2026, 1, 1), "n");
      MedicalRecord r2 = new MedicalRecord("R", LocalDate.of(2026, 1, 1), "n");
      assertEquals(r1, r2, "MedicalRecord cu aceleasi date trebuie sa fie egale");
      assertEquals(r1.hashCode(), r2.hashCode(), "hashCode egal pentru obiecte egale");
      Sali s1 = new Sali(1, "A");
      Sali s2 = new Sali(1, "A");
      assertEquals(s1, s2, "Sali cu aceleasi date trebuie sa fie egale");
    });
  }

  private static void testMedicService(){
    check("MedicService: add, findById, findByDepartament, sortare, delete", () -> {
      MedicService service = MedicService.getInstance();
      Medic zMedic = new Medic("Zoe", "Zamfir", false, 3, true, "checker-dept");
      Medic aMedic = new Medic("Ana", "Avram", true, 9, false, "checker-dept");
      service.addMedic(zMedic);
      service.addMedic(aMedic);
      assertEquals(aMedic, service.findMedicById(aMedic.getId()), "findMedicById");
      List<Medic> dept = service.findMediciByDepartament("checker-dept");
      assertEquals(2, dept.size(), "doi medici in checker-dept");
      List<Medic> sorted = new ArrayList<>(service.getSortedMedici());
      assertTrue(sorted.indexOf(aMedic) < sorted.indexOf(zMedic), "Avram inaintea lui Zamfir in sortare");
      service.deleteMedic(zMedic.getId());
      assertEquals(1, service.findMediciByDepartament("checker-dept").size(), "dupa delete ramane un medic");
      service.deleteMedic(aMedic.getId());
    });
  }

  private static void testMedicSpecialist(){
    check("MedicSpecialist: specializari clonate defensiv + addSpec", () -> {
      String[] specs = {"orl"};
      MedicSpecialist specialist = new MedicSpecialist("Spec", "Test", false, 5, false, "orl", specs);
      specs[0] = "modificat-extern";
      assertEquals("orl", specialist.getSpecs()[0], "constructorul trebuie sa cloneze array-ul");
      specialist.addSpec("audiologie");
      assertEquals(2, specialist.getSpecs().length, "addSpec adauga specializare");
      specialist.addSpec("   ");
      assertEquals(2, specialist.getSpecs().length, "specializarea goala este ignorata");
    });
  }

  private static void testPacientService(){
    check("PacientService: add, findByName, byDiagnostic, urgenti, delete", () -> {
      PacientService service = PacientService.getInstance();
      Pacient urgent = new Pacient("Check", "Urgent", "checker-dx", true, null, null);
      Pacient calm = new Pacient("Check", "Calm", "checker-dx", false, null, null);
      service.addPacient(urgent);
      service.addPacient(calm);
      assertEquals(urgent, service.findPacientByName("check", "URGENT"), "cautare case-insensitive");
      assertEquals(2, service.findPacientiByDiagnostic("checker-dx").size(), "doi pacienti cu checker-dx");
      assertTrue(service.getUrgentPacienti().contains(urgent), "urgentul apare in lista de urgente");
      assertTrue(!service.getUrgentPacienti().contains(calm), "calmul nu apare in lista de urgente");
      service.deletePacient("Check", "Urgent");
      service.deletePacient("Check", "Calm");
      assertEquals(0, service.findPacientiByDiagnostic("checker-dx").size(), "dupa delete nu mai raman pacienti");
    });
  }

  private static void testSaliService(){
    check("SaliService: add, find, delete, sortare in TreeSet", () -> {
      SaliService service = SaliService.getInstance();
      service.addSala(new Sali(902, "Z-check"));
      service.addSala(new Sali(901, "Z-check"));
      assertEquals(901, service.findSala(901, "Z-check").getNumar(), "findSala");
      List<Sali> all = service.getAllSali();
      assertTrue(all.indexOf(service.findSala(901, "Z-check")) < all.indexOf(service.findSala(902, "Z-check")),
                 "salile din aceeasi cladire sunt sortate dupa numar");
      service.deleteSala(901, "Z-check");
      service.deleteSala(902, "Z-check");
      assertThrows(EntityNotFoundException.class, () -> service.findSala(901, "Z-check"), "sala stearsa nu mai exista");
    });
  }

  private static void testMedicamenteService(){
    check("MedicamenteService: add, find case-insensitive, delete", () -> {
      MedicamenteService service = MedicamenteService.getInstance();
      service.addMedicament(new Medicamente("Checkerol", 10));
      assertEquals(10, service.findMedicamentByName("checkerol").getStoc(), "cautare case-insensitive");
      service.deleteMedicament("Checkerol");
      assertThrows(EntityNotFoundException.class, () -> service.findMedicamentByName("Checkerol"),
                   "medicamentul sters nu mai exista");
    });
  }

  private static void testExceptiiCustom(){
    check("exceptiile custom sunt aruncate pentru date invalide / entitati lipsa", () -> {
      assertThrows(InvalidEntityDataException.class, () -> MedicService.getInstance().addMedic(null),
                   "addMedic(null) arunca InvalidEntityDataException");
      assertThrows(InvalidEntityDataException.class,
                   () -> MedicService.getInstance().addMedic(new Medic("", "", false, 1, false, "x")),
                   "medic fara nume arunca InvalidEntityDataException");
      assertThrows(EntityNotFoundException.class, () -> MedicService.getInstance().findMedicById(999999),
                   "id inexistent arunca EntityNotFoundException");
    });
  }

  // ---------- Etapa 2 ----------

  private static void testRepositoryInterfataGenerica(){
    check("interfata generica Repository<T, ID> are exact metodele cerute", () -> {
      assertTrue(Repository.class.isInterface(), "Repository trebuie sa fie interfata");
      assertEquals(2, Repository.class.getTypeParameters().length, "Repository are doi parametri de tip");
      String[] expected = {"save", "findById", "findAll", "update", "delete"};
      for (String name : expected){
        boolean found = false;
        for (Method m : Repository.class.getDeclaredMethods()){
          if (m.getName().equals(name)){
            found = true;
            break;
          }
        }
        assertTrue(found, "lipseste metoda " + name);
      }
      assertTrue(Repository.class.isAssignableFrom(MedicRepository.class), "MedicRepository implementeaza Repository");
      assertTrue(Repository.class.isAssignableFrom(PacientRepository.class), "PacientRepository implementeaza Repository");
      assertTrue(Repository.class.isAssignableFrom(SalaRepository.class), "SalaRepository implementeaza Repository");
      assertTrue(Repository.class.isAssignableFrom(MedicamentRepository.class), "MedicamentRepository implementeaza Repository");
    });
  }

  private static void testSchema(){
    check("schema.sql ruleaza curat (de doua ori la rand) si creeaza cele 6 tabele", () -> {
      SchemaRunner.run("resources/schema.sql");
      SchemaRunner.run("resources/schema.sql"); // re-rulare curata datorita DROP TABLE IF EXISTS
      String[] tabele = {"medic", "medical_record", "pacient", "sala", "medicament", "prescriptie"};
      for (String tabela : tabele){
        assertEquals(0, countRows(tabela), "tabela " + tabela + " exista si e goala dupa initializare");
      }
    });
  }

  private static void testMedicRepositoryCrud(){
    check("MedicRepository: ciclu complet save -> findById -> update -> delete", () -> {
      MedicRepository repo = MedicRepository.getInstance();
      Medic medic = new Medic("Crud", "Medic", true, 4, false, "checker");
      repo.save(medic);
      Optional<Medic> found = repo.findById(medic.getId());
      assertTrue(found.isPresent(), "medicul salvat este gasit");
      assertEquals("Crud", found.get().getFirstName(), "prenumele persistat corect");
      assertEquals(4, found.get().getAniExperienta(), "experienta persistata corect");

      medic.setAniExperienta(11);
      medic.setDepartament("checker-updated");
      repo.update(medic);
      Medic updated = repo.findById(medic.getId()).orElseThrow();
      assertEquals(11, updated.getAniExperienta(), "update persistat");
      assertEquals("checker-updated", updated.getDepartament(), "departament actualizat");

      assertEquals(1, repo.findAll().size(), "findAll vede un medic");
      repo.delete(medic.getId());
      assertTrue(repo.findById(medic.getId()).isEmpty(), "dupa delete findById intoarce Optional.empty");
      assertEquals(0, repo.findAll().size(), "dupa delete findAll e gol");
    });
  }

  private static void testMedicSpecialistRoundTrip(){
    check("MedicRepository: MedicSpecialist isi pastreaza tipul si specializarile dupa round-trip", () -> {
      MedicRepository repo = MedicRepository.getInstance();
      MedicSpecialist specialist = new MedicSpecialist("Round", "Trip", false, 8, false, "orl", new String[]{"orl", "audiologie"});
      repo.save(specialist);
      Medic loaded = repo.findById(specialist.getId()).orElseThrow();
      assertTrue(loaded instanceof MedicSpecialist, "entitatea incarcata trebuie sa fie MedicSpecialist");
      assertEquals(2, ((MedicSpecialist) loaded).getSpecs().length, "specializarile sunt pastrate");
      repo.delete(specialist.getId());
    });
  }

  private static void testSalaRepositoryCrud(){
    check("SalaRepository: id generat de baza de date + ciclu CRUD", () -> {
      SalaRepository repo = SalaRepository.getInstance();
      Sali sala = new Sali(303, "C");
      assertTrue(sala.getId() == null, "inainte de save id-ul este null");
      repo.save(sala);
      assertTrue(sala.getId() != null, "dupa save id-ul este completat din SERIAL");
      sala.setNumar(304);
      repo.update(sala);
      assertEquals(304, repo.findById(sala.getId()).orElseThrow().getNumar(), "update persistat");
      repo.delete(sala.getId());
      assertTrue(repo.findById(sala.getId()).isEmpty(), "sala stearsa nu mai e gasita");
    });
  }

  private static void testMedicamentRepositoryCrud(){
    check("MedicamentRepository: CRUD cu cheie primara String", () -> {
      MedicamentRepository repo = MedicamentRepository.getInstance();
      repo.save(new Medicamente("CheckerPil", 25));
      assertEquals(25, repo.findById("CheckerPil").orElseThrow().getStoc(), "stoc persistat");
      repo.update(new Medicamente("CheckerPil", 30));
      assertEquals(30, repo.findById("CheckerPil").orElseThrow().getStoc(), "stoc actualizat");
      repo.delete("CheckerPil");
      assertTrue(repo.findById("CheckerPil").isEmpty(), "medicament sters");
    });
  }

  private static void testPacientRepositoryTranzactie(){
    check("PacientRepository.save: tranzactie pe doua tabele + reconstructie cu JOIN", () -> {
      MedicRepository medicRepo = MedicRepository.getInstance();
      PacientRepository pacientRepo = PacientRepository.getInstance();
      Medic medic = new Medic("Super", "Vizor", true, 15, false, "checker");
      medicRepo.save(medic);
      MedicalRecord record = new MedicalRecord("REC-CHK", LocalDate.of(2026, 6, 4), "test checker");
      Pacient pacient = new Pacient("Pac", "Ient", "checker-dx", true, medic, record);
      pacientRepo.save(pacient);
      assertTrue(pacient.getId() != null, "pacientul primeste id dupa save");
      assertEquals(1, countRows("medical_record"), "fisa medicala inserata in aceeasi tranzactie");

      Pacient loaded = pacientRepo.findById(pacient.getId()).orElseThrow();
      assertEquals("checker-dx", loaded.getDiagnostic(), "diagnostic persistat");
      assertTrue(loaded.getMedicSupervizor() != null, "medicul supervizor reconstruit din JOIN");
      assertEquals(medic.getId(), Integer.valueOf(loaded.getMedicSupervizor().getId()), "id-ul medicului corect");
      assertTrue(loaded.getRecord() != null, "fisa medicala reconstruita din JOIN");
      assertEquals("REC-CHK", loaded.getRecord().getCode(), "codul fisei corect");
    });
  }

  private static void testPrescriptieCommit(){
    check("tranzactie cu commit: prescriptia se insereaza si stocul scade atomic", () -> {
      MedicamentRepository medRepo = MedicamentRepository.getInstance();
      PacientRepository pacientRepo = PacientRepository.getInstance();
      medRepo.save(new Medicamente("TxPil", 50));
      int pacientId = pacientRepo.findAll().get(0).getId();

      PrescriptieService.getInstance().prescrieMedicament(pacientId, "TxPil", 20);
      assertEquals(30, medRepo.findById("TxPil").orElseThrow().getStoc(), "stocul a scazut cu 20");
      assertEquals(1, countRows("prescriptie"), "prescriptia a fost inserata");
    });
  }

  private static void testPrescriptieRollback(){
    check("tranzactie cu rollback: stoc insuficient nu modifica nicio tabela", () -> {
      MedicamentRepository medRepo = MedicamentRepository.getInstance();
      int pacientId = PacientRepository.getInstance().findAll().get(0).getId();
      int prescriptiiInainte = countRows("prescriptie");

      assertThrows(StocInsuficientException.class,
                   () -> PrescriptieService.getInstance().prescrieMedicament(pacientId, "TxPil", 999),
                   "stoc insuficient arunca StocInsuficientException");
      assertEquals(30, medRepo.findById("TxPil").orElseThrow().getStoc(), "stocul nu s-a modificat dupa rollback");
      assertEquals(prescriptiiInainte, countRows("prescriptie"), "nicio prescriptie noua dupa rollback");
    });
  }

  private static void testInterogariJoin(){
    check("interogarile cu JOIN intorc datele asteptate", () -> {
      List<String> urgenti = PacientRepository.getInstance().findUrgentiCuMedic();
      assertEquals(1, urgenti.size(), "un pacient urgent cu medic");
      assertTrue(urgenti.get(0).contains("Pac Ient") && urgenti.get(0).contains("Super Vizor"),
                 "raportul contine pacientul si medicul");

      List<String> perMedic = MedicRepository.getInstance().countPacientiPerMedic();
      assertTrue(perMedic.get(0).contains("1 pacienti"), "medicul supervizor are un pacient");

      List<String> top = PrescriptieService.getInstance().topMedicamentePrescrise();
      assertEquals(1, top.size(), "un medicament prescris");
      assertTrue(top.get(0).contains("TxPil") && top.get(0).contains("total 20"), "totalul cantitatilor corect");

      int pacientId = PacientRepository.getInstance().findAll().get(0).getId();
      List<String> prescriptii = PrescriptieService.getInstance().prescriptiilePacientului(pacientId);
      assertEquals(1, prescriptii.size(), "o prescriptie pentru pacient");
      assertTrue(prescriptii.get(0).contains("TxPil x20"), "prescriptia contine medicamentul si cantitatea");
    });
  }

  private static void testAuditAppend(){
    check("AuditService: scrie header + linii in format CSV, in modul append", () -> {
      Path audit = Path.of("audit.csv");
      AuditService.getInstance().log("checker_actiune_1");
      long inainte = Files.lines(audit).count();
      AuditService.getInstance().log("checker_actiune_2");
      List<String> lines = Files.readAllLines(audit);
      assertEquals("nume_actiune,timestamp", lines.get(0), "header-ul CSV este primul rand");
      assertEquals(inainte + 1, (long) lines.size(), "o singura linie noua per actiune (append, nu suprascriere)");
      String last = lines.get(lines.size() - 1);
      assertTrue(last.startsWith("checker_actiune_2,20"), "linia contine numele actiunii si timestamp ISO");
    });
  }

  private static void testAuditThreadSafe(){
    check("AuditService este thread-safe: 20 thread-uri x 25 log-uri, fara linii corupte", () -> {
      Path audit = Path.of("audit.csv");
      long inainte = Files.lines(audit).count();
      int threads = 20, perThread = 25;
      List<Thread> pool = new ArrayList<>();
      for (int t = 0; t < threads; t++){
        final int idx = t;
        pool.add(new Thread(() -> {
          for (int i = 0; i < perThread; i++){
            AuditService.getInstance().log("checker_concurent_" + idx);
          }
        }));
      }
      for (Thread t : pool) t.start();
      for (Thread t : pool) t.join();

      List<String> lines = Files.readAllLines(audit);
      assertEquals(inainte + threads * perThread, (long) lines.size(), "toate liniile au fost scrise");
      for (int i = (int) inainte; i < lines.size(); i++){
        String[] parts = lines.get(i).split(",");
        assertTrue(parts.length == 2 && parts[0].startsWith("checker_concurent_"),
                   "linie corupta la index " + i + ": " + lines.get(i));
      }
    });
  }

  // ---------- utilitare ----------

  private static int countRows(String tabela) throws Exception {
    // numele tabelei vine doar din lista interna a checker-ului, nu din input extern
    try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection()
        .prepareStatement("SELECT COUNT(*) FROM " + tabela);
         ResultSet rs = ps.executeQuery()){
      rs.next();
      return rs.getInt(1);
    }
  }
}
