package com.pao.proiectCabinetMedical.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.pao.proiectCabinetMedical.exception.DataAccessException;

/**
 * Executa instructiunile din resources/schema.sql pentru o re-rulare curata a demo-ului.
 */
public final class SchemaRunner {

  private SchemaRunner(){}

  public static void run(String schemaPath){
    String sql;
    try {
      sql = Files.readString(Path.of(schemaPath));
    } catch (IOException e){
      throw new DataAccessException("Nu pot citi schema: " + schemaPath, e);
    }
    try {
      Connection connection = DatabaseConnection.getInstance().getConnection();
      // comentariile se elimina inainte de split, ca sa nu contina ';' derutante
      for (String raw : stripComments(sql).split(";")){
        String statement = raw.trim();
        if (statement.isEmpty()){
          continue;
        }
        try (PreparedStatement ps = connection.prepareStatement(statement)){
          ps.execute();
        }
      }
    } catch (SQLException e){
      throw new DataAccessException("Eroare la executarea schemei.", e);
    }
  }

  private static String stripComments(String statement){
    StringBuilder sb = new StringBuilder();
    for (String line : statement.split("\n")){
      int idx = line.indexOf("--");
      sb.append(idx >= 0 ? line.substring(0, idx) : line).append('\n');
    }
    return sb.toString();
  }
}
