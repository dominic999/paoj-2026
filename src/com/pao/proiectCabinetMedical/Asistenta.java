package com.pao.proiectCabinetMedical;

import com.pao.proiectCabinetMedical.utils.Ansi;

public class Asistenta extends Persoana {

  Ansi a = Ansi.getInstance();
  private int aniExperienta;
  private Sali[] saliResp;

  public Asistenta(String firstName, String lastName, int aniExperienta, Sali[] saliResp){
    super(firstName, lastName);
    this.aniExperienta = aniExperienta;
    this.saliResp = saliResp == null ? new Sali[0] : saliResp.clone();
  }

  public int getAniExperienta(){
    return this.aniExperienta;
  }

  public Sali[] getSaliResp(){
    return this.saliResp.clone();
  }

  public void setAniExperienta(int aniExperienta){
    this.aniExperienta = aniExperienta;
  }

  public void setSaliResp(Sali[] saliResp){
    this.saliResp = saliResp == null ? new Sali[0] : saliResp.clone();
  }

  public void addSalaResp(Sali sala){
    Sali[] newSaliResp = new Sali[saliResp.length + 1];
    for (int i = 0; i < saliResp.length; i++){
      newSaliResp[i] = saliResp[i];
    }
    newSaliResp[saliResp.length] = sala;
    saliResp = newSaliResp;
  }

  @Override
  public String toString(){
    return a.setForeground(a.purple) + "Asistenta: " + a.reset() + super.toString() +
           a.setForeground(a.orange) + " | experienta: " + aniExperienta + " ani" + a.reset() +
           a.setForeground(a.blue) + " | sali responsabile: " + saliResp.length + a.reset();
  }
}
