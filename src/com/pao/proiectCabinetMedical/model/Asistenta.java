package com.pao.proiectCabinetMedical.model;

import java.util.ArrayList;
import java.util.List;

public class Asistenta extends Persoana {

  private int aniExperienta;
  private List<Sali> saliResp;

  public Asistenta(String firstName, String lastName, int aniExperienta, List<Sali> saliResp){
    super(firstName, lastName);
    this.aniExperienta = Math.max(0, aniExperienta);
    this.saliResp = saliResp == null ? new ArrayList<>() : new ArrayList<>(saliResp);
  }

  public int getAniExperienta(){
    return this.aniExperienta;
  }

  public List<Sali> getSaliResp(){
    return new ArrayList<>(this.saliResp);
  }

  public void setAniExperienta(int aniExperienta){
    this.aniExperienta = Math.max(0, aniExperienta);
  }

  public void setSaliResp(List<Sali> saliResp){
    this.saliResp = saliResp == null ? new ArrayList<>() : new ArrayList<>(saliResp);
  }

  public void addSalaResp(Sali sala){
    if (sala != null){
      saliResp.add(sala);
    }
  }

  @Override
  public String getRol(){
    return "Asistenta";
  }

  @Override
  public String toString(){
    return super.toString() +
           a.setForeground(a.orange) + " | experienta: " + aniExperienta + " ani" + a.reset() +
           a.setForeground(a.blue) + " | sali responsabile: " + saliResp.size() + a.reset();
  }
}
