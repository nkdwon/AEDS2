import java.util.Scanner;

public class Iterativo {

    public static String cifrar(String str) {
        // Inicializa uma nova string vazia que armazenará a string cifrada
        String strCifrada = "";

        // Percorre cada caractere da string original
        for (int i = 0; i < str.length(); i++) {
            // Obtém o caractere atual da string
            char letra = str.charAt(i);

            // Verifica se o caractere é igual a \uFFFD
            if (letra == '\uFFFD') {
                strCifrada += letra; // Mantém o caractere inalterado
            } else {
                // Aplica o ciframento de César, deslocando o caractere 3 posições à frente
                letra = (char) (letra + 3);

                // Adiciona o caractere cifrado à string que armazenará o resultado final
                strCifrada += letra;
            }
        }
        return strCifrada;
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String str;

        do {
            str = sc.nextLine();
            if (!str.equals("FIM")) { // Chama o método enquanto a palavra não for FIM, caso seja, encerra o programa
                String cifrada = cifrar(str);
                System.out.println(cifrada);
            }
        } while (!str.equals("FIM")); // Enquanto a palavra não for igual a FIM, o programa continua
        sc.close();
    }
}
