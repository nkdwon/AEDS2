import java.util.Scanner;

public class Recursivo {

    // Verifica se a string contém apenas vogais.
    public static boolean soVogais(String str, int i) {
        if (i == str.length()) {
            return true; // Condição de parada: se percorremos toda a string, é porque só havia vogais.
        }
        char letra = str.charAt(i);
        if (letra != 'a' && letra != 'A' && letra != 'e' && letra != 'E'
                && letra != 'i' && letra != 'I' && letra != 'o' && letra != 'O'
                && letra != 'u' && letra != 'U') {
            return false; // Se encontrar uma letra que não é vogal, retorna falso.
        }
        return soVogais(str, i + 1); // Chama a função recursivamente para verificar a próxima letra.
    }

    // Verifica se a string contém apenas consoantes.
    public static boolean soConsoantes(String str, int i) {
        if (i == str.length()) {
            return true; // Se chegamos ao final da string, é porque só havia consoantes.
        }
        char letra = str.charAt(i);
        // Verifica se a letra não é uma vogal e está entre 'b' e 'z' ou 'B' e 'Z'.
        if (!((letra >= 'b' && letra <= 'z') || (letra >= 'B' && letra <= 'Z'))
                || letra == 'a' || letra == 'A'
                || letra == 'e' || letra == 'E'
                || letra == 'i' || letra == 'I'
                || letra == 'o' || letra == 'O'
                || letra == 'u' || letra == 'U') {
            return false; // Se encontrar uma letra que não é consoante, retorna falso.
        }
        return soConsoantes(str, i + 1); // Chama a função recursivamente para verificar a próxima letra.
    }

    // Verifica se a string contém apenas dígitos inteiros.
    public static boolean soInteiros(String str, int i) {
        if (i == str.length()) {
            return true; // Se chegamos ao final da string, é porque só havia dígitos.
        }
        char letra = str.charAt(i);
        if (letra < '0' || letra > '9') {
            return false; // Se encontrar um caractere que não é dígito, retorna falso.
        }
        return soInteiros(str, i + 1); // Chama a função recursivamente para verificar o próximo caractere.
    }

    // Verifica se a string contém números reais.
    public static boolean soReais(String str, int i, boolean encontrouSeparador) {
        // Se chegar ao final da string e nenhum erro foi encontrado, é um número real
        // válido
        if (i >= str.length()) {
            return true;
        }

        char letra = str.charAt(i);

        // Verifica se o caractere atual é um ponto ou vírgula
        if (letra == '.' || letra == ',') {
            if (encontrouSeparador) {
                return false; // Se já encontrou um separador antes, a string é inválida
            }
            return soReais(str, i + 1, true);
        }

        // Verifica se o caractere atual é um dígito
        if (letra >= '0' && letra <= '9') {
            return soReais(str, i + 1, encontrouSeparador);
        }

        // Se encontrar um caractere que não é dígito ou separador, é inválido
        return false;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str;
        do {
            str = sc.nextLine();
            if (!str.equals("FIM")) {
                
                boolean Vogais = soVogais(str, 0);
                boolean Consoantes = soConsoantes(str, 0);
                boolean Inteiros = soInteiros(str, 0);
                boolean Reais = soReais(str, 0, false);

                System.out.print(Vogais ? "SIM" : "NAO");
                System.out.print(" ");
                System.out.print(Consoantes ? "SIM" : "NAO");
                System.out.print(" ");
                System.out.print(Inteiros ? "SIM" : "NAO");
                System.out.print(" ");
                System.out.println(Reais ? "SIM" : "NAO");
            }

        } while (!str.equals("FIM"));
        sc.close();
    }

}
