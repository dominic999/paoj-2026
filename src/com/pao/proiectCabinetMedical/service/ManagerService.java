package com.pao.proiectCabinetMedical.service;

import com.pao.proiectCabinetMedical.Manager;
import com.pao.proiectCabinetMedical.utils.Ansi;

public class ManagerService {

  private Manager[] manageri;
  Ansi a = Ansi.getInstance();

  private ManagerService(){
    this.manageri = new Manager[0];
  }

  private static class Holder{
    private static final ManagerService INSTANCE = new ManagerService();
  }

  public static ManagerService getInstance(){
    return Holder.INSTANCE;
  }

  public void addManager(Manager o){
    Manager[] now = new Manager[manageri.length + 1];
    System.arraycopy(manageri, 0, now, 0, manageri.length);
    now[manageri.length] = o;
    manageri = now;
  }

  public Manager[] getManageri(){
    return manageri.clone();
  }

  public void showManageri(){
    System.out.println(a.title("Lista managerilor"));
    System.out.println(a.line());
    for (int i = 0; i < manageri.length; i++){
      System.out.println(manageri[i]);
    }
    if (manageri.length == 0){
      System.out.println(a.warning("Nu exista manageri inregistrati."));
    }
    System.out.println(a.line());
  }

  public void deleteManager(int idx){
    if (idx < 0 || idx >= manageri.length){
      return;
    }
    Manager[] newManageri = new Manager[manageri.length - 1];
    int newIdx = 0;
    for (int i = 0; i < manageri.length; i++){
      if (i != idx){
        newManageri[newIdx++] = manageri[i];
      }
    }
    manageri = newManageri;
  }

  public void showOptions(){
    System.out.println(a.title("Ce vreti sa faceti cu managerii?"));
    System.out.println(a.line());
    System.out.println(a.option(1, "Vezi toti managerii."));
    System.out.println(a.option(2, "Adauga un manager nou."));
    System.out.println(a.option(3, "Sterge un manager dupa index."));
    System.out.println(a.option(4, "Vezi cati subordonati are fiecare manager."));
    System.out.println(a.option(5, "Inapoi la meniul entitatilor."));
    System.out.println(a.option(6, "Iesire din aplicatie."));
    System.out.println(a.line());
  }
}
