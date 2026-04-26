package com.pao.proiectCabinetMedical;

import java.util.Scanner;

import com.pao.proiectCabinetMedical.service.AsistentaService;
import com.pao.proiectCabinetMedical.service.ManagerService;
import com.pao.proiectCabinetMedical.service.MedicamenteService;
import com.pao.proiectCabinetMedical.service.MedicService;
import com.pao.proiectCabinetMedical.service.MedicSpecialistService;
import com.pao.proiectCabinetMedical.service.PacientService;
import com.pao.proiectCabinetMedical.service.PersoanaService;
import com.pao.proiectCabinetMedical.service.SaliService;
import com.pao.proiectCabinetMedical.utils.Ansi;

public class Main{

  static Ansi a = Ansi.getInstance();
  static Scanner scanner = new Scanner(System.in);

  static PersoanaService persoane = PersoanaService.getInstance();
  static MedicService medici = MedicService.getInstance();
  static MedicSpecialistService specialisti = MedicSpecialistService.getInstance();
  static PacientService pacienti = PacientService.getInstance();
  static AsistentaService asistente = AsistentaService.getInstance();
  static ManagerService manageri = ManagerService.getInstance();
  static SaliService sali = SaliService.getInstance();
  static MedicamenteService medicamente = MedicamenteService.getInstance();

  public static void main(String[] args){
    seedData();
    runApp();
  }

  public static void runApp(){
    boolean running = true;
    while (running){
      showMainOptions();
      int option = readInt("Alege o optiune: ");
      switch (option){
        case 1:
          running = runPersoaneMenu();
          break;
        case 2:
          running = runMediciMenu();
          break;
        case 3:
          running = runMediciSpecialistiMenu();
          break;
        case 4:
          running = runPacientiMenu();
          break;
        case 5:
          running = runAsistenteMenu();
          break;
        case 6:
          running = runManageriMenu();
          break;
        case 7:
          running = runSaliMenu();
          break;
        case 8:
          running = runMedicamenteMenu();
          break;
        case 9:
          running = false;
          break;
        default:
          System.out.println(a.warning("Optiune invalida. Incearca din nou."));
      }
    }
    System.out.println(a.danger("Aplicatia s-a inchis."));
    scanner.close();
  }

  public static void showMainOptions(){
    System.out.println(a.title("Pe ce entitate vrei sa faci modificari?"));
    System.out.println(a.line());
    System.out.println(a.option(1, "Persoane"));
    System.out.println(a.option(2, "Medici"));
    System.out.println(a.option(3, "Medici specialisti"));
    System.out.println(a.option(4, "Pacienti"));
    System.out.println(a.option(5, "Asistente"));
    System.out.println(a.option(6, "Manageri"));
    System.out.println(a.option(7, "Sali"));
    System.out.println(a.option(8, "Medicamente"));
    System.out.println(a.option(9, "Exit"));
    System.out.println(a.line());
  }

  public static boolean runPersoaneMenu(){
    while (true){
      persoane.showOptions();
      int option = readInt("Alege o optiune: ");
      switch (option){
        case 1:
          persoane.showPersoane();
          break;
        case 2:
          persoane.addPersoana(new Persoana(readText("Prenume: "), readText("Nume: ")));
          System.out.println(a.value("Persoana a fost adaugata."));
          break;
        case 3:
          persoane.deletePersoana(readInt("Index persoana: "));
          System.out.println(a.value("Operatia de stergere a fost executata."));
          break;
        case 4:
          return true;
        case 5:
          return false;
        default:
          System.out.println(a.warning("Optiune invalida."));
      }
    }
  }

  public static boolean runMediciMenu(){
    while (true){
      medici.showOptions();
      int option = readInt("Alege o optiune: ");
      switch (option){
        case 1:
          medici.showMedics();
          break;
        case 2:
          medici.addMedic(readMedic());
          System.out.println(a.value("Medicul a fost adaugat."));
          break;
        case 3:
          medici.deleteMedic(readInt("Id medic: "));
          System.out.println(a.value("Operatia de stergere a fost executata."));
          break;
        case 4:
          medici.showMedicById(readInt("Id medic: "));
          break;
        case 5:
          return true;
        case 6:
          return false;
        default:
          System.out.println(a.warning("Optiune invalida."));
      }
    }
  }

  public static boolean runMediciSpecialistiMenu(){
    while (true){
      specialisti.showOptions();
      int option = readInt("Alege o optiune: ");
      switch (option){
        case 1:
          specialisti.showMedics();
          break;
        case 2:
          specialisti.addMedic(readMedicSpecialist());
          System.out.println(a.value("Medicul specialist a fost adaugat."));
          break;
        case 3:
          specialisti.deleteMedic(readInt("Index medic specialist: "));
          System.out.println(a.value("Operatia de stergere a fost executata."));
          break;
        case 4:
          specialisti.showMedSpecs(readInt("Index medic specialist: "));
          break;
        case 5:
          specialisti.addMedSpec(readInt("Index medic specialist: "), readText("Noua specializare: "));
          System.out.println(a.value("Specializarea a fost adaugata."));
          break;
        case 6:
          return true;
        case 7:
          return false;
        default:
          System.out.println(a.warning("Optiune invalida."));
      }
    }
  }

  public static boolean runPacientiMenu(){
    while (true){
      pacienti.showOptions();
      int option = readInt("Alege o optiune: ");
      switch (option){
        case 1:
          pacienti.showPacienti();
          break;
        case 2:
          pacienti.addPacient(readPacient());
          System.out.println(a.value("Pacientul a fost adaugat."));
          break;
        case 3:
          pacienti.deletePacient(readInt("Index pacient: "));
          System.out.println(a.value("Operatia de stergere a fost executata."));
          break;
        case 4:
          pacienti.showPacienti();
          break;
        case 5:
          return true;
        case 6:
          return false;
        default:
          System.out.println(a.warning("Optiune invalida."));
      }
    }
  }

  public static boolean runAsistenteMenu(){
    while (true){
      asistente.showOptions();
      int option = readInt("Alege o optiune: ");
      switch (option){
        case 1:
          asistente.showAsistente();
          break;
        case 2:
          asistente.addAsistenta(readAsistenta());
          System.out.println(a.value("Asistenta a fost adaugata."));
          break;
        case 3:
          asistente.deleteAsistenta(readInt("Index asistenta: "));
          System.out.println(a.value("Operatia de stergere a fost executata."));
          break;
        case 4:
          asistente.showAsistente();
          break;
        case 5:
          return true;
        case 6:
          return false;
        default:
          System.out.println(a.warning("Optiune invalida."));
      }
    }
  }

  public static boolean runManageriMenu(){
    while (true){
      manageri.showOptions();
      int option = readInt("Alege o optiune: ");
      switch (option){
        case 1:
          manageri.showManageri();
          break;
        case 2:
          manageri.addManager(readManager());
          System.out.println(a.value("Managerul a fost adaugat."));
          break;
        case 3:
          manageri.deleteManager(readInt("Index manager: "));
          System.out.println(a.value("Operatia de stergere a fost executata."));
          break;
        case 4:
          manageri.showManageri();
          break;
        case 5:
          return true;
        case 6:
          return false;
        default:
          System.out.println(a.warning("Optiune invalida."));
      }
    }
  }

  public static boolean runSaliMenu(){
    while (true){
      sali.showOptions();
      int option = readInt("Alege o optiune: ");
      switch (option){
        case 1:
          sali.showSali();
          break;
        case 2:
          sali.addSala(new Sali(readInt("Numar sala: "), readText("Cladire: ")));
          System.out.println(a.value("Sala a fost adaugata."));
          break;
        case 3:
          sali.deleteSala(readInt("Index sala: "));
          System.out.println(a.value("Operatia de stergere a fost executata."));
          break;
        case 4:
          return true;
        case 5:
          return false;
        default:
          System.out.println(a.warning("Optiune invalida."));
      }
    }
  }

  public static boolean runMedicamenteMenu(){
    while (true){
      medicamente.showOptions();
      int option = readInt("Alege o optiune: ");
      switch (option){
        case 1:
          medicamente.showMedicamente();
          break;
        case 2:
          medicamente.addMedicament(new Medicamente(readText("Denumire medicament: "), readInt("Stoc: ")));
          System.out.println(a.value("Medicamentul a fost adaugat."));
          break;
        case 3:
          medicamente.deleteMedicament(readInt("Index medicament: "));
          System.out.println(a.value("Operatia de stergere a fost executata."));
          break;
        case 4:
          return true;
        case 5:
          return false;
        default:
          System.out.println(a.warning("Optiune invalida."));
      }
    }
  }

  public static Medic readMedic(){
    String firstName = readText("Prenume: ");
    String lastName = readText("Nume: ");
    boolean deGarda = readBoolean("Este de garda? (da/nu): ");
    int aniExperienta = readInt("Ani experienta: ");
    boolean rezident = readBoolean("Este rezident? (da/nu): ");
    String departament = readText("Departament: ");
    return new Medic(firstName, lastName, deGarda, aniExperienta, rezident, departament);
  }

  public static MedicSpecialist readMedicSpecialist(){
    String firstName = readText("Prenume: ");
    String lastName = readText("Nume: ");
    boolean deGarda = readBoolean("Este de garda? (da/nu): ");
    int aniExperienta = readInt("Ani experienta: ");
    boolean rezident = readBoolean("Este rezident? (da/nu): ");
    String departament = readText("Departament: ");
    int count = readInt("Numar specializari: ");
    String[] specs = new String[count < 0 ? 0 : count];
    for (int i = 0; i < specs.length; i++){
      specs[i] = readText("Specializare " + (i + 1) + ": ");
    }
    return new MedicSpecialist(firstName, lastName, deGarda, aniExperienta, rezident, departament, specs);
  }

  public static Pacient readPacient(){
    String firstName = readText("Prenume: ");
    String lastName = readText("Nume: ");
    String diagnostic = readText("Diagnostic: ");
    boolean urgenta = readBoolean("Este urgenta? (da/nu): ");
    int medicId = readInt("Id medic supervizor (-1 pentru fara): ");
    Medic medicSupervizor = medicId < 0 ? null : medici.findMedicById(medicId);
    return new Pacient(firstName, lastName, diagnostic, urgenta, medicSupervizor);
  }

  public static Asistenta readAsistenta(){
    String firstName = readText("Prenume: ");
    String lastName = readText("Nume: ");
    int aniExperienta = readInt("Ani experienta: ");
    int count = readInt("Numar sali responsabile: ");
    Sali[] saliResp = new Sali[count < 0 ? 0 : count];
    for (int i = 0; i < saliResp.length; i++){
      int numar = readInt("Numar sala " + (i + 1) + ": ");
      String cladire = readText("Cladire sala " + (i + 1) + ": ");
      saliResp[i] = new Sali(numar, cladire);
    }
    return new Asistenta(firstName, lastName, aniExperienta, saliResp);
  }

  public static Manager readManager(){
    String firstName = readText("Prenume: ");
    String lastName = readText("Nume: ");
    boolean deGarda = readBoolean("Este de garda? (da/nu): ");
    int aniExperienta = readInt("Ani experienta: ");
    boolean rezident = readBoolean("Este rezident? (da/nu): ");
    String departament = readText("Departament: ");
    int count = readInt("Numar subordonati: ");
    Medic[] subordonati = new Medic[count < 0 ? 0 : count];
    for (int i = 0; i < subordonati.length; i++){
      int medicId = readInt("Id subordonat " + (i + 1) + ": ");
      subordonati[i] = medici.findMedicById(medicId);
    }
    return new Manager(firstName, lastName, deGarda, aniExperienta, rezident, departament, subordonati);
  }

  public static int readInt(String prompt){
    while (true){
      System.out.print(a.label(prompt));
      String line = scanner.nextLine();
      try {
        return Integer.parseInt(line);
      } catch (NumberFormatException e){
        System.out.println(a.warning("Trebuie sa introduci un numar intreg."));
      }
    }
  }

  public static boolean readBoolean(String prompt){
    while (true){
      System.out.print(a.label(prompt));
      String line = scanner.nextLine().trim().toLowerCase();
      if (line.equals("da") || line.equals("d") || line.equals("true")){
        return true;
      }
      if (line.equals("nu") || line.equals("n") || line.equals("false")){
        return false;
      }
      System.out.println(a.warning("Raspunde cu da sau nu."));
    }
  }

  public static String readText(String prompt){
    System.out.print(a.label(prompt));
    return scanner.nextLine();
  }

  public static void seedData(){
    Sali sala1 = new Sali(101, "A");
    Sali sala2 = new Sali(202, "B");
    sali.addSala(sala1);
    sali.addSala(sala2);

    Medic medic1 = new Medic("Andrei", "Tirdea", true, 7, false, "cardiologie");
    Medic medic2 = new Medic("Miruna", "Zaharia", false, 2, true, "urgente");
    medici.addMedic(medic1);
    medici.addMedic(medic2);

    MedicSpecialist specialist = new MedicSpecialist("Dominic", "Ionescu", false, 10, false, "chirurgie", new String[]{"chirurgie", "urologie"});
    specialisti.addMedic(specialist);
    specialisti.addMedSpec(0, "laparoscopie");

    Pacient pacient = new Pacient("Ana", "Popescu", "fractura", true, medic1);
    pacienti.addPacient(pacient);

    Asistenta asistenta = new Asistenta("Maria", "Georgescu", 6, new Sali[]{sala1, sala2});
    asistente.addAsistenta(asistenta);

    Manager manager = new Manager("Ioan", "Marin", false, 12, false, "administrativ", new Medic[]{medic1, medic2});
    manageri.addManager(manager);

    Medicamente medicament = new Medicamente("Paracetamol", 150);
    medicamente.addMedicament(medicament);

    persoane.addPersoana(new Persoana("Elena", "Radu"));
  }
}
