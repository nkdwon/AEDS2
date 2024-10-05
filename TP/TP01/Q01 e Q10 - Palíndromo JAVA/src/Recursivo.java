import java.util.Scanner;

public class Recursivo {

    // Função recursiva para verificar se a string é um palíndromo.
    // Utiliza dois índices, i e j, para comparar os caracteres das extremidades da
    // string.
    public static boolean ePalindromo(String str, int i, int j) {
        // Base da recursão: se o índice inicial for maior ou igual ao índice final, a
        // string é um palíndromo, pois já vai ter sido toda percorrida
        if (i >= j) {
            return true;
        }
        // Se os caracteres nas posições i e j não corresponderem, não é um palíndromo
        if (str.charAt(i) != str.charAt(j)) {
            return false;
        }

        // Chama recursivamente a função com os índices atualizados
        return ePalindromo(str, ++i, --j);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str;
        do {
            // Lê a entrada do usuário
            str = sc.nextLine();
            if (!str.equals("FIM")) {

                int tamString = str.length(); // Obtém o comprimento da string
                // Verifica se a string é um palíndromo usando a função recursiva e imprime o
                // resultado
                if (ePalindromo(str, 0, tamString - 1)) {
                    System.out.println("SIM");
                } else {
                    System.out.println("NAO");
                }
            }

        } while (!str.equals("FIM")); // Continua lendo até que o usuário digite "FIM"

        sc.close();
    }

}
