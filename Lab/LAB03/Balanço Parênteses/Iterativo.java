import java.util.Scanner;

public class Iterativo {
  public static Boolean contaParenteses(String str) {
    int cont1 = 0;
    int cont2 = 0;
    for (int i = 0; i < str.length(); i++) {

      if (str.charAt(i) == '(') {
        cont1++;
      }
      if (str.charAt(i) == ')') {
        cont2++;
      }
      if (cont2 > cont1) {
        return false;
      }
    }
    if (cont1 != cont2) {
      return false;
    }
    return true;

  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String str;
    do {
      str = sc.nextLine();
      if (str.equals("FIM")) {
        return;
      }
      if (contaParenteses(str)) {
        System.out.println("correto");
      } else {
        System.out.println("incorreto");
      }

    } while (!str.equals("FIM"));
    sc.close();
  }

}