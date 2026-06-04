package com.pao.proiectCabinetMedical.exception;

/**
 * Exceptie runtime care impacheteaza erorile SQL din stratul de persistenta,
 * astfel incat interfata generica Repository sa ramana curata.
 */
public class DataAccessException extends RuntimeException {

  public DataAccessException(String message, Throwable cause){
    super(message, cause);
  }
}
