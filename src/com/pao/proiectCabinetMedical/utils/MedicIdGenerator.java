package com.pao.proiectCabinetMedical.utils;

public class MedicIdGenerator{

  int curr_id;

  private MedicIdGenerator(){
    curr_id = 0;
  }

  public static class Holder{
    public static final MedicIdGenerator INSTANCE = new MedicIdGenerator();
  }

  public static MedicIdGenerator getInstance(){
    return Holder.INSTANCE;
  }

  public int getCurrentId(){
    return curr_id++;
  }

}
