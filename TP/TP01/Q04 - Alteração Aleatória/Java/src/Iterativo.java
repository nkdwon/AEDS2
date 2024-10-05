import java.util.Random;
import java.util.Scanner;

public class Iterativo {

    public static String alterar(String str, Random gerador) {
        // Sorteando duas letras aleatórias
        char primeiraLetra = (char) ('a' + (Math.abs(gerador.nextInt()) % 26));
        char segundaLetra = (char) ('a' + (Math.abs(gerador.nextInt()) % 26));

        String strAlterada = "";
        // Substituindo todas as ocorrências da primeira letra pela segunda
        for (int i = 0; i < str.length(); i++) {
            char letraAtual = str.charAt(i);
            if (letraAtual == primeiraLetra) {
                strAlterada += segundaLetra; // Substitui a primeira letra pela segunda
            } else {
                strAlterada += letraAtual; // Mantém a letra atual se não for a primeira letra
            }
        }

        return strAlterada;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str;
        Random gerador = new Random();
        gerador.setSeed(4); // Configurando a semente como 4 para garantir resultados reproduzíveis
        // Loop para processar as entradas até que "FIM" sejaa digitado
        do {
            str = sc.nextLine();
            if (!str.equals("FIM")) {
                System.out.println(alterar(str, gerador));
            }
        } while (!str.equals("FIM")); // Continua o loop enquanto a entrada não for "FIM"
        sc.close();
    }
}
