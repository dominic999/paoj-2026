package com.pao.laboratory14.exercise2.repository;

import com.pao.laboratory14.exercise1.TipBilet;
import com.pao.laboratory14.exercise2.model.Eveniment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EvenimentRepository implements Repository<Eveniment, Integer> {

    private final Connection connection;

    public EvenimentRepository(Connection connection) throws SQLException {
        this.connection = connection;
        initSchema();
    }

    private void initSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DROP TABLE IF EXISTS evenimente");
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS evenimente (
                    id         INTEGER PRIMARY KEY AUTOINCREMENT,
                    nume       TEXT    NOT NULL,
                    data       TEXT    NOT NULL,
                    capacitate INTEGER,
                    tip        TEXT
                )""");
        }
    }

    @Override
    public void save(Eveniment entity) throws SQLException {
        String sql = "INSERT INTO evenimente (nume, data, capacitate, tip) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getData());
            ps.setInt(3, entity.getCapacitate());
            ps.setString(4, entity.getTip().name());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    entity.setId(keys.getInt(1));
                }
            }
        }
    }

    @Override
    public Optional<Eveniment> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM evenimente WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Eveniment> findAll() throws SQLException {
        List<Eveniment> list = new ArrayList<>();
        String sql = "SELECT * FROM evenimente ORDER BY id";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public void update(Eveniment entity) throws SQLException {
        String sql = "UPDATE evenimente SET nume=?, data=?, capacitate=?, tip=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, entity.getNume());
            ps.setString(2, entity.getData());
            ps.setInt(3, entity.getCapacitate());
            ps.setString(4, entity.getTip().name());
            ps.setInt(5, entity.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM evenimente WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("NOT_FOUND");
            }
        }
    }

    public int count() throws SQLException {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM evenimente")) {
            rs.next();
            return rs.getInt(1);
        }
    }

    private Eveniment mapRow(ResultSet rs) throws SQLException {
        return new Eveniment(
                rs.getInt("id"),
                rs.getString("nume"),
                rs.getString("data"),
                rs.getInt("capacitate"),
                TipBilet.valueOf(rs.getString("tip"))
        );
    }
}
