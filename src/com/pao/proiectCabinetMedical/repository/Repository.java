package com.pao.proiectCabinetMedical.repository;

import java.util.List;
import java.util.Optional;

/**
 * Interfata generica pentru operatiile CRUD pe baza de date (Etapa 2).
 *
 * @param <T>  tipul entitatii persistate
 * @param <ID> tipul cheii primare
 */
public interface Repository<T, ID> {
  void save(T entity);
  Optional<T> findById(ID id);
  List<T> findAll();
  void update(T entity);
  void delete(ID id);
}
