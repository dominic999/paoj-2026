-- Schema baza de date pentru proiectul Cabinet Medical (Etapa 2)
-- DROP-urile sunt in ordine inversa dependintelor pentru o re-rulare curata.

DROP TABLE IF EXISTS prescriptie;
DROP TABLE IF EXISTS pacient;
DROP TABLE IF EXISTS medical_record;
DROP TABLE IF EXISTS medic;
DROP TABLE IF EXISTS sala;
DROP TABLE IF EXISTS medicament;

CREATE TABLE medic (
  id             INTEGER PRIMARY KEY,
  first_name     VARCHAR(100) NOT NULL,
  last_name      VARCHAR(100) NOT NULL,
  de_garda       BOOLEAN      NOT NULL,
  ani_experienta INTEGER      NOT NULL,
  rezident       BOOLEAN      NOT NULL,
  departament    VARCHAR(100) NOT NULL,
  -- NULL pentru un medic obisnuit; lista separata prin virgula pentru MedicSpecialist
  specializari   TEXT
);

CREATE TABLE medical_record (
  code       VARCHAR(50) PRIMARY KEY,
  created_at DATE NOT NULL,
  notes      TEXT NOT NULL
);

CREATE TABLE pacient (
  id          SERIAL PRIMARY KEY,
  first_name  VARCHAR(100) NOT NULL,
  last_name   VARCHAR(100) NOT NULL,
  diagnostic  VARCHAR(200) NOT NULL,
  urgenta     BOOLEAN      NOT NULL,
  medic_id    INTEGER      REFERENCES medic(id),          -- FK 1
  record_code VARCHAR(50)  REFERENCES medical_record(code) -- FK 2
);

CREATE TABLE sala (
  id      SERIAL PRIMARY KEY,
  numar   INTEGER     NOT NULL,
  cladire VARCHAR(50) NOT NULL,
  UNIQUE (numar, cladire)
);

CREATE TABLE medicament (
  denumire VARCHAR(100) PRIMARY KEY,
  stoc     INTEGER NOT NULL CHECK (stoc >= 0)
);

CREATE TABLE prescriptie (
  id                 SERIAL PRIMARY KEY,
  pacient_id         INTEGER      NOT NULL REFERENCES pacient(id),        -- FK 3
  medicament_denumire VARCHAR(100) NOT NULL REFERENCES medicament(denumire), -- FK 4
  cantitate          INTEGER      NOT NULL CHECK (cantitate > 0),
  data_prescriptie   DATE         NOT NULL
);
