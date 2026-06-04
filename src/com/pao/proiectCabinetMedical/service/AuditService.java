package com.pao.proiectCabinetMedical.service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton care logheaza fiecare actiune executata in fisierul audit.csv.
 * Fisierul este deschis in modul append, iar metoda de scriere este
 * synchronized pentru a fi thread-safe.
 */
public final class AuditService {

  private static final Path AUDIT_FILE = Path.of("audit.csv");
  private static final String HEADER = "nume_actiune,timestamp";
  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

  private AuditService(){}

  private static class Holder{
    private static final AuditService INSTANCE = new AuditService();
  }

  public static AuditService getInstance(){
    return Holder.INSTANCE;
  }

  public synchronized void log(String numeActiune){
    boolean needsHeader = !Files.exists(AUDIT_FILE);
    try (BufferedWriter writer = Files.newBufferedWriter(AUDIT_FILE, StandardCharsets.UTF_8,
        StandardOpenOption.CREATE, StandardOpenOption.APPEND)){
      if (needsHeader){
        writer.write(HEADER);
        writer.newLine();
      }
      writer.write(numeActiune + "," + LocalDateTime.now().format(FORMATTER));
      writer.newLine();
    } catch (IOException e){
      throw new UncheckedIOException("Nu pot scrie in " + AUDIT_FILE, e);
    }
  }
}
