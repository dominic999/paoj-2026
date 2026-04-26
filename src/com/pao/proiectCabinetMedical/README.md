# Sistem Cabinet Medical

Acest proiect modeleaza un cabinet medical folosind concepte OOP, servicii singleton, colectii Java, exceptii custom si o ierarhie de mostenire centrata pe clasa abstracta `Persoana`.

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
├── exception/
├── utils/
└── Main.java
```

## 4. Observatii de implementare

- `Persoana` este clasa abstracta de baza pentru entitatile de tip persoana.
- `MedicalRecord` este o clasa imutabila.
- `MedicService` foloseste `List`, `Map` si `TreeSet`.
- `SaliService` si `MedicamenteService` folosesc `Set`.
- Exceptiile custom folosite sunt:
  - `EntityNotFoundException`
  - `InvalidEntityDataException`
