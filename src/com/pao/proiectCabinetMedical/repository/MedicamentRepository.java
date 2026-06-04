package com.pao.proiectCabinetMedical.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.pao.proiectCabinetMedical.exception.DataAccessException;
import com.pao.proiectCabinetMedical.model.Medicamente;
import com.pao.proiectCabinetMedical.service.AuditService;
import com.pao.proiectCabinetMedical.utils.DatabaseConnection;

/**
 * Repository cu cheie primara de tip String (denumirea medicamentului),
 * pentru a demonstra flexibilitatea interfetei generice Repository&lt;T, ID&gt;.
 */
public class MedicamentRepository implements Repository<Medicamente, String> {

  private static final AuditService AUDIT = AuditService.getInstance();

  private MedicamentRepository(){}

  private static class Holder{
    private static final MedicamentRepository INSTANCE = new MedicamentRepository();
  }

  public static MedicamentRepository getInstance(){
    return Holder.INSTANCE;
  }

  private Connection connection() throws SQLException {
    // conexiunea este reutilizabila (singleton) si nu se inchide aici
    return DatabaseConnection.getInstance().getConnection();
  }

  @Override
  public void save(Medicamente medicament){
    String sql = "INSERT INTO medicament (denumire, stoc) VALUES (?, ?)";
    try (PreparedStatement ps = connection().prepareStatement(sql)){
      ps.setString(1, medicament.getDenumire());
      ps.setInt(2, medicament.getStoc());
      ps.executeUpdate();
      AUDIT.log("salveaza_medicament_db");
    } catch (SQLException e){
      throw new DataAccessException("Nu pot salva medicamentul " + medicament.getDenumire() + ".", e);
    }
  }

  @Override
  public Optional<Medicamente> findById(String denumire){
    String sql = "SELECT denumire, stoc FROM medicament WHERE denumire = ?";
    try (PreparedStatement ps = connection().prepareStatement(sql)){
      ps.setString(1, denumire);
      try (ResultSet rs = ps.executeQuery()){
        AUDIT.log("cauta_medicament_db");
        if (rs.next()){
          return Optional.of(new Medicamente(rs.getString("denumire"), rs.getInt("stoc")));
        }
        return Optional.empty();
      }
    } catch (SQLException e){
      throw new DataAccessException("Nu pot cauta medicamentul " + denumire + ".", e);
    }
  }

  @Override
  public List<Medicamente> findAll(){
    String sql = "SELECT denumire, stoc FROM medicament ORDER BY denumire";
    List<Medicamente> medicamente = new ArrayList<>();
    try (PreparedStatement ps = connection().prepareStatement(sql);
         ResultSet rs = ps.executeQuery()){
      while (rs.next()){
        medicamente.add(new Medicamente(rs.getString("denumire"), rs.getInt("stoc")));
      }
      AUDIT.log("listeaza_medicamente_db");
      return medicamente;
    } catch (SQLException e){
      throw new DataAccessException("Nu pot lista medicamentele.", e);
    }
  }

  @Override
  public void update(Medicamente medicament){
    String sql = "UPDATE medicament SET stoc = ? WHERE denumire = ?";
    try (PreparedStatement ps = connection().prepareStatement(sql)){
      ps.setInt(1, medicament.getStoc());
      ps.setString(2, medicament.getDenumire());
      ps.executeUpdate();
      AUDIT.log("actualizeaza_medicament_db");
    } catch (SQLException e){
      throw new DataAccessException("Nu pot actualiza medicamentul " + medicament.getDenumire() + ".", e);
    }
  }

  @Override
  public void delete(String denumire){
    String sql = "DELETE FROM medicament WHERE denumire = ?";
    try (PreparedStatement ps = connection().prepareStatement(sql)){
      ps.setString(1, denumire);
      ps.executeUpdate();
      AUDIT.log("sterge_medicament_db");
    } catch (SQLException e){
      throw new DataAccessException("Nu pot sterge medicamentul " + denumire + ".", e);
    }
  }
}
