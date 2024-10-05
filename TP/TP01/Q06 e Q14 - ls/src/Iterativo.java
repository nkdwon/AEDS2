import java.util.Scanner;

public class Iterativo {

    // Verifica se a string contém apenas vogais.
    public static boolean soVogais(String str) {
        for (int i = 0; i < str.length(); i++) {
            char letra = str.charAt(i);
            if (letra != 'a' && letra != 'A' && letra != 'e' && letra != 'E' && letra != 'i' && letra != 'I'
                    && letra != 'o' && letra != 'O' && letra != 'u' && letra != 'U') {
                return false;
            }
        }
        return true;
    }

    // Verifica se a string contém apenas consoantes.
    public static boolean soConsoantes(String str) {
        for (int i = 0; i < str.length(); i++) {
            char letra = str.charAt(i);
            if (!((letra >= 'b' && letra <= 'z') || (letra >= 'B' && letra <= 'Z')) ||
                    letra == 'a' || letra == 'A' ||
                    letra == 'e' || letra == 'E' ||
                    letra == 'i' || letra == 'I' ||
                    letra == 'o' || letra == 'O' ||
                    letra == 'u' || letra == 'U') {
                return false;
            }
        }
        return true;
    }

    // Verifica se a string contém apenas dígitos inteiros.
    public static boolean soInteiros(String str) {
        for (int i = 0; i < str.length(); i++) {
            char letra = str.charAt(i);
            if (letra < '0' || letra > '9') {
                return false;
            }
        }
        return true;
    }

    // Verifica se a string representa um número real válido.
    public static boolean soReais(String str) {
        boolean encontrouPontoOuVirgula = false;

        for (int i = 0; i < str.length(); i++) {
            char letra = str.charAt(i);
            // Verifica se o caractere é um ponto ou vírgula decimal
            if (letra == '.' || letra == ',') {
                if (encontrouPontoOuVirgula) {
                    return false; // Já encontrou um ponto ou vírgula antes, então é inválido
                }
                encontrouPontoOuVirgula = true;

                // Verifica se o ponto ou vírgula está no início ou no final da string
                if (i == 0 || i == str.length() - 1) {
                    if (i == 0 && (str.charAt(i + 1) < '0' || str.charAt(i + 1) > '9')) {
                        return false; // Ponto ou vírgula no início sem dígito a seguir
                    }
                    if (i == str.length() - 1 && (str.charAt(i - 1) < '0' || str.charAt(i - 1) > '9')) {
                        return false; // Ponto ou vírgula no final sem dígito antes
                    }
                }
            }
            // Verifica se o caractere é um dígito fora do contexto do ponto ou vírgula
            else if (letra < '0' || letra > '9') {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str;
        do {
            str = sc.nextLine();
            if (!str.equals("FIM")) {
                boolean Vogais = soVogais(str);
                boolean Consoantes = soConsoantes(str);
                boolean Inteiros = soInteiros(str);
                boolean Reais = soReais(str);

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
