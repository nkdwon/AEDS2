
import java.util.Scanner;

public class Main {

  public static void letrasMaiusculas(Scanner sc) {
    String palavra = sc.nextLine();

    if(palavra.equals("FIM")) {
      return;
    }

    int qtdLetras = 0;
    for (int i = 0; i < palavra.length(); i++) {
      if (palavra.charAt(i) >= 'A' && palavra.charAt(i) <= 'Z') {
        qtdLetras++;
      }
    }

    System.out.println(qtdLetras);

    letrasMaiusculas(sc);
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    letrasMaiusculas(sc);
    sc.close();
  }
}
