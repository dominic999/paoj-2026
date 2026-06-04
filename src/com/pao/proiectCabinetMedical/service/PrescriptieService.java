package com.pao.proiectCabinetMedical.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.pao.proiectCabinetMedical.exception.DataAccessException;
import com.pao.proiectCabinetMedical.exception.StocInsuficientException;
import com.pao.proiectCabinetMedical.utils.DatabaseConnection;

/**
 * Serviciu Singleton pentru prescriptii. Prescrierea unui medicament
 * afecteaza doua tabele (prescriptie + medicament), deci este executata
 * intr-o tranzactie JDBC explicita cu commit la succes si rollback la eroare.
 */
public class PrescriptieService {

  private static final AuditService AUDIT = AuditService.getInstance();

  private PrescriptieService(){}

  private static class Holder{
    private static final PrescriptieService INSTANCE = new PrescriptieService();
  }

  public static PrescriptieService getInstance(){
    return Holder.INSTANCE;
  }

  private Connection connection() throws SQLException {
    // conexiunea este reutilizabila (singleton) si nu se inchide aici
    return DatabaseConnection.getInstance().getConnection();
  }

  /**
   * Tranzactie JDBC explicita:
   *   1. insereaza prescriptia in tabela prescriptie
   *   2. scade stocul din tabela medicament
   * Daca stocul este insuficient, intreaga tranzactie este anulata cu rollback.
   */
  public void prescrieMedicament(int pacientId, String denumireMedicament, int cantitate) throws StocInsuficientException {
    String insertPrescriptie = "INSERT INTO prescriptie (pacient_id, medicament_denumire, cantitate, data_prescriptie) " +
                               "VALUES (?, ?, ?, ?)";
    String scadeStoc = "UPDATE medicament SET stoc = stoc - ? WHERE denumire = ? AND stoc >= ?";
    Connection conn;
    try {
      conn = connection();
    } catch (SQLException e){
      throw new DataAccessException("Nu pot obtine conexiunea la baza de date.", e);
    }
    try {
      conn.setAutoCommit(false);
      try {
        // operatie 1: insereaza prescriptia (tabela prescriptie)
        try (PreparedStatement ps = conn.prepareStatement(insertPrescriptie)){
          ps.setInt(1, pacientId);
          ps.setString(2, denumireMedicament);
          ps.setInt(3, cantitate);
          ps.setDate(4, Date.valueOf(LocalDate.now()));
          ps.executeUpdate();
        }
        // operatie 2: scade stocul (tabela medicament)
        try (PreparedStatement ps = conn.prepareStatement(scadeStoc)){
          ps.setInt(1, cantitate);
          ps.setString(2, denumireMedicament);
          ps.setInt(3, cantitate);
          int updated = ps.executeUpdate();
          if (updated == 0){
            conn.rollback();
            AUDIT.log("prescrie_medicament_rollback");
            throw new StocInsuficientException("Stoc insuficient pentru " + denumireMedicament +
                                               " (cerut: " + cantitate + "). Tranzactie anulata cu rollback.");
          }
        }
        conn.commit();
        AUDIT.log("prescrie_medicament_db");
      } catch (SQLException e){
        conn.rollback();
        AUDIT.log("prescrie_medicament_rollback");
        throw e;
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (SQLException e){
      throw new DataAccessException("Nu pot prescrie medicamentul " + denumireMedicament + ".", e);
    }
  }

  /**
   * Interogare cu JOIN: cele mai prescrise medicamente, cu totalul cantitatilor.
   */
  public List<String> topMedicamentePrescrise(){
    String sql = "SELECT med.denumire, med.stoc, COUNT(pr.id) AS nr_prescriptii, SUM(pr.cantitate) AS total_cantitate " +
                 "FROM prescriptie pr JOIN medicament med ON pr.medicament_denumire = med.denumire " +
                 "GROUP BY med.denumire, med.stoc ORDER BY total_cantitate DESC";
    List<String> rezultate = new ArrayList<>();
    try (PreparedStatement ps = connection().prepareStatement(sql);
         ResultSet rs = ps.executeQuery()){
      while (rs.next()){
        rezultate.add(rs.getString("denumire") + " -> " + rs.getInt("nr_prescriptii") + " prescriptii, total " +
                      rs.getInt("total_cantitate") + " bucati, stoc ramas " + rs.getInt("stoc"));
      }
      AUDIT.log("raport_top_medicamente");
      return rezultate;
    } catch (SQLException e){
      throw new DataAccessException("Nu pot genera raportul medicamentelor prescrise.", e);
    }
  }

  /**
   * Interogare cu JOIN pe trei tabele: prescriptiile unui pacient,
   * cu numele pacientului si denumirea medicamentului.
   */
  public List<String> prescriptiilePacientului(int pacientId){
    String sql = "SELECT pr.id, p.first_name, p.last_name, pr.medicament_denumire, pr.cantitate, pr.data_prescriptie " +
                 "FROM prescriptie pr " +
                 "JOIN pacient p ON pr.pacient_id = p.id " +
                 "JOIN medicament med ON pr.medicament_denumire = med.denumire " +
                 "WHERE p.id = ? ORDER BY pr.id";
    List<String> rezultate = new ArrayList<>();
    try (PreparedStatement ps = connection().prepareStatement(sql)){
      ps.setInt(1, pacientId);
      try (ResultSet rs = ps.executeQuery()){
        while (rs.next()){
          rezultate.add("Prescriptie #" + rs.getInt("id") + " pentru " + rs.getString("first_name") + " " +
                        rs.getString("last_name") + ": " + rs.getString("medicament_denumire") + " x" +
                        rs.getInt("cantitate") + " (" + rs.getDate("data_prescriptie") + ")");
        }
      }
      AUDIT.log("raport_prescriptii_pacient");
      return rezultate;
    } catch (SQLException e){
      throw new DataAccessException("Nu pot lista prescriptiile pacientului " + pacientId + ".", e);
    }
  }
}
