package com.pao.proiectCabinetMedical.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.pao.proiectCabinetMedical.exception.DataAccessException;
import com.pao.proiectCabinetMedical.model.Medic;
import com.pao.proiectCabinetMedical.model.MedicSpecialist;
import com.pao.proiectCabinetMedical.service.AuditService;
import com.pao.proiectCabinetMedical.utils.DatabaseConnection;

public class MedicRepository implements Repository<Medic, Integer> {

  private static final AuditService AUDIT = AuditService.getInstance();

  private MedicRepository(){}

  private static class Holder{
    private static final MedicRepository INSTANCE = new MedicRepository();
  }

  public static MedicRepository getInstance(){
    return Holder.INSTANCE;
  }

  private Connection connection() throws SQLException {
    // conexiunea este reutilizabila (singleton) si nu se inchide aici
    return DatabaseConnection.getInstance().getConnection();
  }

  @Override
  public void save(Medic medic){
    String sql = "INSERT INTO medic (id, first_name, last_name, de_garda, ani_experienta, rezident, departament, specializari) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement ps = connection().prepareStatement(sql)){
      ps.setInt(1, medic.getId());
      ps.setString(2, medic.getFirstName());
      ps.setString(3, medic.getLastName());
      ps.setBoolean(4, medic.isDeGarda());
      ps.setInt(5, medic.getAniExperienta());
      ps.setBoolean(6, medic.isRezident());
      ps.setString(7, medic.getDepartament());
      ps.setString(8, specializariOf(medic));
      ps.executeUpdate();
      AUDIT.log("salveaza_medic_db");
    } catch (SQLException e){
      throw new DataAccessException("Nu pot salva medicul cu id " + medic.getId() + ".", e);
    }
  }

  @Override
  public Optional<Medic> findById(Integer id){
    String sql = "SELECT id, first_name, last_name, de_garda, ani_experienta, rezident, departament, specializari " +
                 "FROM medic WHERE id = ?";
    try (PreparedStatement ps = connection().prepareStatement(sql)){
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()){
        AUDIT.log("cauta_medic_db");
        if (rs.next()){
          return Optional.of(mapRow(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e){
      throw new DataAccessException("Nu pot cauta medicul cu id " + id + ".", e);
    }
  }

  @Override
  public List<Medic> findAll(){
    String sql = "SELECT id, first_name, last_name, de_garda, ani_experienta, rezident, departament, specializari " +
                 "FROM medic ORDER BY id";
    List<Medic> medici = new ArrayList<>();
    try (PreparedStatement ps = connection().prepareStatement(sql);
         ResultSet rs = ps.executeQuery()){
      while (rs.next()){
        medici.add(mapRow(rs));
      }
      AUDIT.log("listeaza_medici_db");
      return medici;
    } catch (SQLException e){
      throw new DataAccessException("Nu pot lista medicii.", e);
    }
  }

  @Override
  public void update(Medic medic){
    String sql = "UPDATE medic SET first_name = ?, last_name = ?, de_garda = ?, ani_experienta = ?, " +
                 "rezident = ?, departament = ?, specializari = ? WHERE id = ?";
    try (PreparedStatement ps = connection().prepareStatement(sql)){
      ps.setString(1, medic.getFirstName());
      ps.setString(2, medic.getLastName());
      ps.setBoolean(3, medic.isDeGarda());
      ps.setInt(4, medic.getAniExperienta());
      ps.setBoolean(5, medic.isRezident());
      ps.setString(6, medic.getDepartament());
      ps.setString(7, specializariOf(medic));
      ps.setInt(8, medic.getId());
      ps.executeUpdate();
      AUDIT.log("actualizeaza_medic_db");
    } catch (SQLException e){
      throw new DataAccessException("Nu pot actualiza medicul cu id " + medic.getId() + ".", e);
    }
  }

  @Override
  public void delete(Integer id){
    String sql = "DELETE FROM medic WHERE id = ?";
    try (PreparedStatement ps = connection().prepareStatement(sql)){
      ps.setInt(1, id);
      ps.executeUpdate();
      AUDIT.log("sterge_medic_db");
    } catch (SQLException e){
      throw new DataAccessException("Nu pot sterge medicul cu id " + id + ".", e);
    }
  }

  /**
   * Interogare cu JOIN: fiecare medic impreuna cu numarul de pacienti supervizati.
   */
  public List<String> countPacientiPerMedic(){
    String sql = "SELECT m.id, m.first_name, m.last_name, COUNT(p.id) AS nr_pacienti " +
                 "FROM medic m LEFT JOIN pacient p ON p.medic_id = m.id " +
                 "GROUP BY m.id, m.first_name, m.last_name ORDER BY nr_pacienti DESC, m.id";
    List<String> rezultate = new ArrayList<>();
    try (PreparedStatement ps = connection().prepareStatement(sql);
         ResultSet rs = ps.executeQuery()){
      while (rs.next()){
        rezultate.add("Medic #" + rs.getInt("id") + " " + rs.getString("first_name") + " " +
                      rs.getString("last_name") + " -> " + rs.getInt("nr_pacienti") + " pacienti");
      }
      AUDIT.log("raport_pacienti_per_medic");
      return rezultate;
    } catch (SQLException e){
      throw new DataAccessException("Nu pot genera raportul pacienti per medic.", e);
    }
  }

  private String specializariOf(Medic medic){
    if (medic instanceof MedicSpecialist specialist){
      return String.join(",", specialist.getSpecs());
    }
    return null;
  }

  private Medic mapRow(ResultSet rs) throws SQLException {
    int id = rs.getInt("id");
    String firstName = rs.getString("first_name");
    String lastName = rs.getString("last_name");
    boolean deGarda = rs.getBoolean("de_garda");
    int aniExperienta = rs.getInt("ani_experienta");
    boolean rezident = rs.getBoolean("rezident");
    String departament = rs.getString("departament");
    String specializari = rs.getString("specializari");
    if (specializari != null){
      String[] specs = specializari.isEmpty() ? new String[0] : specializari.split(",");
      return new MedicSpecialist(id, firstName, lastName, deGarda, aniExperienta, rezident, departament, specs);
    }
    return new Medic(id, firstName, lastName, deGarda, aniExperienta, rezident, departament);
  }
}
