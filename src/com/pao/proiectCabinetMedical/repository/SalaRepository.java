package com.pao.proiectCabinetMedical.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.pao.proiectCabinetMedical.exception.DataAccessException;
import com.pao.proiectCabinetMedical.model.Sali;
import com.pao.proiectCabinetMedical.service.AuditService;
import com.pao.proiectCabinetMedical.utils.DatabaseConnection;

public class SalaRepository implements Repository<Sali, Integer> {

  private static final AuditService AUDIT = AuditService.getInstance();

  private SalaRepository(){}

  private static class Holder{
    private static final SalaRepository INSTANCE = new SalaRepository();
  }

  public static SalaRepository getInstance(){
    return Holder.INSTANCE;
  }

  private Connection connection() throws SQLException {
    // conexiunea este reutilizabila (singleton) si nu se inchide aici
    return DatabaseConnection.getInstance().getConnection();
  }

  @Override
  public void save(Sali sala){
    String sql = "INSERT INTO sala (numar, cladire) VALUES (?, ?) RETURNING id";
    try (PreparedStatement ps = connection().prepareStatement(sql)){
      ps.setInt(1, sala.getNumar());
      ps.setString(2, sala.getCladire());
      try (ResultSet rs = ps.executeQuery()){
        if (rs.next()){
          sala.setId(rs.getInt(1));
        }
      }
      AUDIT.log("salveaza_sala_db");
    } catch (SQLException e){
      throw new DataAccessException("Nu pot salva sala " + sala.getNumar() + " din cladirea " + sala.getCladire() + ".", e);
    }
  }

  @Override
  public Optional<Sali> findById(Integer id){
    String sql = "SELECT id, numar, cladire FROM sala WHERE id = ?";
    try (PreparedStatement ps = connection().prepareStatement(sql)){
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery()){
        AUDIT.log("cauta_sala_db");
        if (rs.next()){
          return Optional.of(mapRow(rs));
        }
        return Optional.empty();
      }
    } catch (SQLException e){
      throw new DataAccessException("Nu pot cauta sala cu id " + id + ".", e);
    }
  }

  @Override
  public List<Sali> findAll(){
    String sql = "SELECT id, numar, cladire FROM sala ORDER BY id";
    List<Sali> sali = new ArrayList<>();
    try (PreparedStatement ps = connection().prepareStatement(sql);
         ResultSet rs = ps.executeQuery()){
      while (rs.next()){
        sali.add(mapRow(rs));
      }
      AUDIT.log("listeaza_sali_db");
      return sali;
    } catch (SQLException e){
      throw new DataAccessException("Nu pot lista salile.", e);
    }
  }

  @Override
  public void update(Sali sala){
    String sql = "UPDATE sala SET numar = ?, cladire = ? WHERE id = ?";
    try (PreparedStatement ps = connection().prepareStatement(sql)){
      ps.setInt(1, sala.getNumar());
      ps.setString(2, sala.getCladire());
      ps.setInt(3, sala.getId());
      ps.executeUpdate();
      AUDIT.log("actualizeaza_sala_db");
    } catch (SQLException e){
      throw new DataAccessException("Nu pot actualiza sala cu id " + sala.getId() + ".", e);
    }
  }

  @Override
  public void delete(Integer id){
    String sql = "DELETE FROM sala WHERE id = ?";
    try (PreparedStatement ps = connection().prepareStatement(sql)){
      ps.setInt(1, id);
      ps.executeUpdate();
      AUDIT.log("sterge_sala_db");
    } catch (SQLException e){
      throw new DataAccessException("Nu pot sterge sala cu id " + id + ".", e);
    }
  }

  private Sali mapRow(ResultSet rs) throws SQLException {
    Sali sala = new Sali(rs.getInt("numar"), rs.getString("cladire"));
    sala.setId(rs.getInt("id"));
    return sala;
  }
}
