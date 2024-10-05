

import java.util.Scanner;

public class Main {

  public static int letrasMaiusculas(String palavra) {

    int qtdLetras = 0;
    for (int i = 0; i < palavra.length(); i++) {
      if (palavra.charAt(i) >= 'A' && palavra.charAt(i) <= 'Z') {
        qtdLetras++;
      }
    }

    return qtdLetras;
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    String palavra = sc.nextLine();

    while (!palavra.equals("FIM")) {
      int qtdLetras = letrasMaiusculas(palavra);
      System.out.println(qtdLetras);

      palavra = sc.nextLine();
    }

    sc.close();
  }
}
