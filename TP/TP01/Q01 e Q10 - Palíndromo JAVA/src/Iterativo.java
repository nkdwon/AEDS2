import java.util.Scanner;

public class Iterativo {

    // Verifica se a string é um palíndromo.

    public static boolean ePalindromo(String str) {
        int tamString = str.length(); // Obtém o comprimento da string

        // Verifica cada caractere da string até a metade
        for (int i = 0; i < tamString / 2; i++) {
            // Compara o caractere atual com o caractere correspondente do final
            if (str.charAt(i) != str.charAt(tamString - i - 1)) {
                return false; // Se qualquer caractere não corresponder, não é um palíndromo
            }
        }

        return true; // Todos os caracteres corresponderam, é um palíndromo
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str;
        do {
            // Lê a entrada do usuário
            str = sc.nextLine();
            if (!str.equals("FIM")) {

                // Verifica se a string é um palíndromo e imprime o resultado
                if (ePalindromo(str)) {
                    System.out.println("SIM");
                } else {
                    System.out.println("NAO");
                }
            }

        } while (!str.equals("FIM")); // Continua lendo até que o usuário digite "FIM"

        sc.close();
    }

}
