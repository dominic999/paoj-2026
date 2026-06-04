# Sistem Cabinet Medical

Acest proiect modeleaza un cabinet medical folosind concepte OOP, servicii singleton, colectii Java, exceptii custom si o ierarhie de mostenire centrata pe clasa abstracta `Persoana`.

Etapa 2 adauga persistenta JDBC (PostgreSQL), tranzactii explicite, interogari cu JOIN si un serviciu de audit.

## 1. Actiuni / interogari posibile in sistem

1. Adauga o sala noua in sistem.
2. Adauga un medicament nou in stoc.
3. Adauga un medic nou.
4. Adauga un medic specialist.
5. Adauga un manager.
6. Adauga o asistenta.
7. Adauga un pacient cu fisa medicala.
8. Cauta un medic dupa id.
9. Listeaza medicii dintr-un departament.
10. Afiseaza medicii sortati alfabetic.
11. Adauga o specializare unui medic specialist.
12. Cauta un pacient dupa nume.
13. Listeaza pacientii grupati dupa diagnostic.
14. Afiseaza pacientii urgenti.
15. Sterge un medicament din sistem.

## 2. Tipuri de obiecte din domeniu

1. Persoana
2. Medic
3. MedicSpecialist
4. Manager
5. Asistenta
6. Pacient
7. Sali
8. Medicamente
9. MedicalRecord

## 3. Structura pachetelor

```text
com.pao.proiectCabinetMedical/
├── model/
├── service/
├── repository/      <- Etapa 2: interfata generica Repository + repository-uri JDBC
├── exception/
├── utils/           <- include DatabaseConnection (singleton) si SchemaRunner
└── Main.java
resources/           <- in radacina depozitului
├── schema.sql
└── db.properties
```

## 4. Observatii de implementare

- `Persoana` este clasa abstracta de baza pentru entitatile de tip persoana.
- `MedicalRecord` este o clasa imutabila.
- `MedicService` foloseste `List`, `Map` si `TreeSet`.
- `SaliService` si `MedicamenteService` folosesc `Set`.
- Exceptiile custom folosite sunt:
  - `EntityNotFoundException`
  - `InvalidEntityDataException`
  - `StocInsuficientException` (Etapa 2)
  - `DataAccessException` (Etapa 2, runtime, impacheteaza `SQLException`)

## 5. Etapa 2 — Persistenta JDBC, Tranzactii si Audit

### Baza de date

- PostgreSQL, baza `paoj_proiect`; configurarea conexiunii se face in `resources/db.properties` (`db.url`, `db.user`, `db.password`) — fara credentiale hardcodate in Java.
- `resources/schema.sql` contine `DROP TABLE IF EXISTS` + `CREATE TABLE` pentru: `medic`, `medical_record`, `pacient`, `sala`, `medicament`, `prescriptie`. Toate tabelele au `PRIMARY KEY` si exista 4 relatii `FOREIGN KEY` (`pacient.medic_id`, `pacient.record_code`, `prescriptie.pacient_id`, `prescriptie.medicament_denumire`).
- `DatabaseConnection` este Singleton si expune o conexiune JDBC reutilizabila.

### Repository-uri

- Interfata generica `Repository<T, ID>` cu `save`, `findById`, `findAll`, `update`, `delete`.
- Implementari CRUD complete pentru 4 entitati: `MedicRepository` (`Integer`), `PacientRepository` (`Integer`), `SalaRepository` (`Integer`), `MedicamentRepository` (cheie `String`).
- Toate interogarile folosesc `PreparedStatement`, iar `PreparedStatement`/`ResultSet` sunt inchise cu try-with-resources (conexiunea singleton ramane deschisa, fiind reutilizabila).

### Tranzactii JDBC explicite

- `PacientRepository.save`: internarea unui pacient insereaza in `medical_record` si `pacient` intr-o singura tranzactie (`setAutoCommit(false)` + `commit`/`rollback`).
- `PrescriptieService.prescrieMedicament`: insereaza in `prescriptie` si scade stocul din `medicament`; la stoc insuficient face `rollback` si arunca `StocInsuficientException`.

### Interogari cu JOIN

1. Pacientii urgenti cu datele medicului supervizor (`pacient JOIN medic`).
2. Numarul de pacienti per medic (`medic LEFT JOIN pacient` + `GROUP BY`).
3. Cele mai prescrise medicamente (`prescriptie JOIN medicament` + `GROUP BY`).
4. Prescriptiile unui pacient (`prescriptie JOIN pacient JOIN medicament` — 3 tabele).

### Audit

- `AuditService` este Singleton si logheaza fiecare actiune in `audit.csv` (format `nume_actiune,timestamp`).
- Fisierul este deschis in modul append (nu se suprascrie intre rulari) si metoda `log` este `synchronized` (thread-safe).
- Toate actiunile din Etapa 1 (metodele serviciilor) si toate operatiile pe baza de date apeleaza `AuditService`.

### Rulare

```bash
# din radacina depozitului
make proiect
# sau manual:
javac -cp src:lib/postgresql-42.7.4.jar src/com/pao/proiectCabinetMedical/Main.java
java  -cp src:lib/postgresql-42.7.4.jar com.pao.proiectCabinetMedical.Main
```

Demo-ul ruleaza intai actiunile Etapei 1 (in memorie), apoi initializeaza schema si demonstreaza CRUD, tranzactii (commit + rollback) si interogarile cu JOIN.

Baza de date se creeaza o singura data cu: `psql -h localhost -c "CREATE DATABASE paoj_proiect"`.
