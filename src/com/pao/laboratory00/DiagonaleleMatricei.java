package com.pao.laboratory00;

import java.util.Scanner;

/**
 * Exercitiul 2
 *
 * Cititi de la tastatura o matrice de n ori n elemente REALE.
 *
 * 1. Afisati matricea in consola.
 * 2. Afisati suma elementelor de pe diagonala principala
 *    si produsul elementelor de pe diagonala secundara.
 *
 */

public class DiagonaleleMatricei {
    public static void main(String[] args) {

      Scanner scanner = new Scanner(System.in);
      int n = scanner.nextInt();
      int[][] matrice = new int[n][n];
      for (int i = 0; i < n; i++){
        for(int j = 0; j < n; j++){
          matrice[i][j] = scanner.nextInt();
        }
      }

      int sum = 0;
      int produs = 1;
      for(int i = 0; i < n; i++){
        sum = sum + matrice[i][i];
        produs = produs * matrice[i][n-1-i];
      }
      System.out.println(sum);
      System.out.println(produs);

    }
}
