package com.pao.proiectCabinetMedical.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton care citeste configuratia din resources/db.properties
 * si expune o conexiune JDBC reutilizabila.
 */
public final class DatabaseConnection {

  private static final String PROPERTIES_PATH = "resources/db.properties";

  private final String url;
  private final String user;
  private final String password;
  private Connection connection;

  private DatabaseConnection(){
    Properties props = new Properties();
    try (InputStream in = new FileInputStream(PROPERTIES_PATH)){
      props.load(in);
    } catch (IOException e){
      throw new IllegalStateException("Nu pot citi " + PROPERTIES_PATH + ": " + e.getMessage(), e);
    }
    this.url = props.getProperty("db.url");
    this.user = props.getProperty("db.user");
    this.password = props.getProperty("db.password", "");
    if (url == null || user == null){
      throw new IllegalStateException("db.url si db.user sunt obligatorii in " + PROPERTIES_PATH);
    }
  }

  private static class Holder{
    private static final DatabaseConnection INSTANCE = new DatabaseConnection();
  }

  public static DatabaseConnection getInstance(){
    return Holder.INSTANCE;
  }

  /**
   * Conexiunea este reutilizabila: este deschisa o singura data si
   * re-deschisa doar daca a fost inchisa intre timp. Nu se inchide
   * cu try-with-resources de catre apelanti.
   */
  public synchronized Connection getConnection() throws SQLException {
    if (connection == null || connection.isClosed()){
      connection = DriverManager.getConnection(url, user, password);
    }
    return connection;
  }
}
