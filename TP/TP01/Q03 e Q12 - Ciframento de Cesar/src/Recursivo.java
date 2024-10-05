import java.util.Scanner;

public class Recursivo {
    // A forma como estou fazendo não é a mais adequada pois a cada chamada está
    // sendo criada uma String nova. Porém como é um problema simples e entradas
    // simples, não tem muito problema.
    // Outra forma de fazer seria utilizar um array, porém optei por apenas modificar o meu código iterativo
    public static String cifrar(String str, int i, String strCifrada) {

        if (i == str.length()) {
            return strCifrada;
        }
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
        return cifrar(str, i + 1, strCifrada);
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        String str;
        String strCifrada = ""; // Inicializa uma nova string vazia que armazenará a string cifrada
        do {
            str = sc.nextLine();
            if (!str.equals("FIM")) { // Chama o método enquanto a palavra não for FIM, caso seja, encerra o programa
                String cifrada = cifrar(str, 0, strCifrada);
                System.out.println(cifrada);
            }
        } while (!str.equals("FIM")); // Enquanto a palavra não for igual a FIM, o programa continua
        sc.close();
    }
}
