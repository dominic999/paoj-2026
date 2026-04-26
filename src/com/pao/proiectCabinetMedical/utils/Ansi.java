package com.pao.proiectCabinetMedical.utils;

public class Ansi{
  
  public final String esc = "\u001b";
  public final String green = "118";
  public final String blue = "26";
  public final String purple = "171";
  public final String orange= "214";
  public final String red = "196";
  public final String yellow = "226";
  public final String cyan = "45";
  public final String gray = "250";
  public final String reset = "\u001b[0m";


  private Ansi(){}

  static public class Holder{
    public static final Ansi INSTANCE = new Ansi();
  }

  public static Ansi getInstance(){
    return Holder.INSTANCE;
  }

  public String setForeground(String color){
    return ("\u001b[38;5;" + color + "m");
  }

  public String title(String text){
    return setForeground(purple) + text + reset();
  }

  public String label(String text){
    return setForeground(cyan) + text + reset();
  }

  public String value(String text){
    return setForeground(green) + text + reset();
  }

  public String warning(String text){
    return setForeground(yellow) + text + reset();
  }

  public String danger(String text){
    return setForeground(red) + text + reset();
  }

  public String option(int number, String text){
    return setForeground(orange) + number + ")" + reset() + " " + text;
  }

  public String line(){
    return setForeground(gray) + "----------------------------------------" + reset();
  }

  public String reset(){
    return (reset);
  }


}
