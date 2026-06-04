package com.pao.proiectCabinetMedical.repository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.pao.proiectCabinetMedical.exception.DataAccessException;
import com.pao.proiectCabinetMedical.model.MedicalRecord;
import com.pao.proiectCabinetMedical.model.Medic;
import com.pao.proiectCabinetMedical.model.Pacient;
import com.pao.proiectCabinetMedical.service.AuditService;
import com.pao.proiectCabinetMedical.utils.DatabaseConnection;

public class PacientRepository implements Repository<Pacient, Integer> {

  private static final AuditService AUDIT = AuditService.getInstance();

  private PacientRepository(){}

  private static class Holder{
    private static final PacientRepository INSTANCE = new PacientRepository();
  }

  public static PacientRepository getInstance(){
    return Holder.INSTANCE;
  }

  private Connection connection() throws SQLException {
    // conexiunea este reutilizabila (singleton) si nu se inchide aici
    return DatabaseConnection.getInstance().getConnection();
  }

  /**
   * Internarea unui pacient afecteaza doua tabele (medical_record + pacient),
   * deci este executata intr-o tranzactie JDBC explicita cu commit la succes
   * si rollback la eroare.
   */
  @Override
  public void save(Pacient pacient){
    String insertRecord = "INSERT INTO medical_record (code, created_at, notes) VALUES (?, ?, ?)";
    String insertPacient = "INSERT INTO pacient (first_name, last_name, diagnostic, urgenta, medic_id, record_code) " +
                           "VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
    Connection conn;
    try {
      conn = connection();
    } catch (SQLException e){
      throw new DataAccessException("Nu pot obtine conexiunea la baza de date.", e);
    }
    try {
      conn.setAutoCommit(false);
      try {
        MedicalRecord record = pacient.getRecord();
        if (record != null){
          try (PreparedStatement ps = conn.prepareStatement(insertRecord)){
            ps.setString(1, record.getCode());
            ps.setDate(2, Date.valueOf(record.getCreatedAt()));
            ps.setString(3, record.getNotes());
            ps.executeUpdate();
          }
        }
        try (PreparedStatement ps = conn.prepareStatement(insertPacient)){
          ps.setString(1, pacient.getFirstName());
          ps.setString(2, pacient.getLastName());
          ps.setString(3, pacient.getDiagnostic());
          ps.setBoolean(4, pacient.isUrgenta());
          if (pacient.getMedicSupervizor() == null){
            ps.setNull(5, java.sql.Types.INTEGER);
          } else {
            ps.setInt(5, pacient.getMedicSupervizor().getId());
          }
          ps.setString(6, record == null ? null : record.getCode());
          try (ResultSet rs = ps.executeQuery()){
            if (rs.next()){
              pacient.setId(rs.getInt(1));
            }
          }
        }
        conn.commit();
        AUDIT.log("interneaza_pacient_db");
      } catch (SQLException e){
        conn.rollback();
        throw e;
      } finally {
        conn.setAutoCommit(true);
      }
    } catch (SQLException e){
      throw new DataAccessException("Nu pot interna pacientul " + pacient.getFirstName() + " " + pacient.getLastName() + ".", e);
    }
  }

  @Override
  public Optional<Pacient> findById(Integer id){
    String sql = baseSelect() + " WHERE p.id = ?";
    try (PreparedStatement ps = connection().prepareStatement(sql)){
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()){
        AUDIT.log("cauta_pacient_db");
        if (rs.next()){
          return Optional.of(mapRow(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e){
      throw new DataAccessException("Nu pot cauta pacientul cu id " + id + ".", e);
    }
  }

  @Override
  public List<Pacient> findAll(){
    String sql = baseSelect() + " ORDER BY p.id";
    List<Pacient> pacienti = new ArrayList<>();
    try (PreparedStatement ps = connection().prepareStatement(sql);
         ResultSet rs = ps.executeQuery()){
      while (rs.next()){
        pacienti.add(mapRow(rs));
      }
      AUDIT.log("listeaza_pacienti_db");
      return pacienti;
    } catch (SQLException e){
      throw new DataAccessException("Nu pot lista pacientii.", e);
    }
  }

  @Override
  public void update(Pacient pacient){
    String sql = "UPDATE pacient SET first_name = ?, last_name = ?, diagnostic = ?, urgenta = ?, medic_id = ? WHERE id = ?";
    try (PreparedStatement ps = connection().prepareStatement(sql)){
      ps.setString(1, pacient.getFirstName());
      ps.setString(2, pacient.getLastName());
      ps.setString(3, pacient.getDiagnostic());
      ps.setBoolean(4, pacient.isUrgenta());
      if (pacient.getMedicSupervizor() == null){
        ps.setNull(5, java.sql.Types.INTEGER);
      } else {
        ps.setInt(5, pacient.getMedicSupervizor().getId());
      }
      ps.setInt(6, pacient.getId());
      ps.executeUpdate();
      AUDIT.log("actualizeaza_pacient_db");
    } catch (SQLException e){
      throw new DataAccessException("Nu pot actualiza pacientul cu id " + pacient.getId() + ".", e);
    }
  }

  @Override
  public void delete(Integer id){
    String sql = "DELETE FROM pacient WHERE id = ?";
    try (PreparedStatement ps = connection().prepareStatement(sql)){
      ps.setInt(1, id);
      ps.executeUpdate();
      AUDIT.log("sterge_pacient_db");
    } catch (SQLException e){
      throw new DataAccessException("Nu pot sterge pacientul cu id " + id + ".", e);
    }
  }

  /**
   * Interogare cu JOIN: pacientii urgenti impreuna cu datele medicului supervizor.
   */
  public List<String> findUrgentiCuMedic(){
    String sql = "SELECT p.id, p.first_name, p.last_name, p.diagnostic, m.first_name AS medic_prenume, m.last_name AS medic_nume, m.departament " +
                 "FROM pacient p JOIN medic m ON p.medic_id = m.id " +
                 "WHERE p.urgenta = TRUE ORDER BY p.id";
    List<String> rezultate = new ArrayList<>();
    try (PreparedStatement ps = connection().prepareStatement(sql);
         ResultSet rs = ps.executeQuery()){
      while (rs.next()){
        rezultate.add("Pacient #" + rs.getInt("id") + " " + rs.getString("first_name") + " " + rs.getString("last_name") +
                      " (" + rs.getString("diagnostic") + ") -> medic " + rs.getString("medic_prenume") + " " +
                      rs.getString("medic_nume") + ", departament " + rs.getString("departament"));
      }
      AUDIT.log("raport_pacienti_urgenti");
      return rezultate;
    } catch (SQLException e){
      throw new DataAccessException("Nu pot genera raportul pacientilor urgenti.", e);
    }
  }

  /**
   * SELECT de baza cu JOIN pe medic si medical_record pentru a reconstrui complet pacientul.
   */
  private String baseSelect(){
    return "SELECT p.id, p.first_name, p.last_name, p.diagnostic, p.urgenta, " +
           "m.id AS medic_id, m.first_name AS medic_prenume, m.last_name AS medic_nume, m.de_garda, m.ani_experienta, m.rezident, m.departament, " +
           "r.code AS record_code, r.created_at, r.notes " +
           "FROM pacient p " +
           "LEFT JOIN medic m ON p.medic_id = m.id " +
           "LEFT JOIN medical_record r ON p.record_code = r.code";
  }

  private Pacient mapRow(ResultSet rs) throws SQLException {
    Medic medic = null;
    if (rs.getObject("medic_id") != null){
      medic = new Medic(rs.getInt("medic_id"), rs.getString("medic_prenume"), rs.getString("medic_nume"),
                        rs.getBoolean("de_garda"), rs.getInt("ani_experienta"), rs.getBoolean("rezident"),
                        rs.getString("departament"));
    }
    MedicalRecord record = null;
    if (rs.getString("record_code") != null){
      record = new MedicalRecord(rs.getString("record_code"), rs.getDate("created_at").toLocalDate(), rs.getString("notes"));
    }
    Pacient pacient = new Pacient(rs.getString("first_name"), rs.getString("last_name"),
                                  rs.getString("diagnostic"), rs.getBoolean("urgenta"), medic, record);
    pacient.setId(rs.getInt("id"));
    return pacient;
  }
}
