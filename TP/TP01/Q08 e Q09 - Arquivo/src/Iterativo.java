import java.io.RandomAccessFile;
import java.util.Scanner;

public class Iterativo {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt(); // Número de valores reais a serem lidos
        RandomAccessFile arq = new RandomAccessFile("numeros.txt", "rw");

        // Ler os valores e escrever no arquivo
        for (int i = 0; i < n; i++) {
            double valor = sc.nextDouble();
            arq.writeDouble(valor);
        }
        
        // Fechar o arquivo após a escrita
        arq.close();

        // Reabrir o arquivo para leitura
        arq = new RandomAccessFile("numeros.txt", "r");

        // Ler os valores de trás para frente
        for (int i = n - 1; i >= 0; i--) {
            arq.seek(i * 8); // Cada double ocupa 8 bytes
            double valor = arq.readDouble();

            // Verifica se a parte decimal é zero
            if (valor % 1 == 0) {
                System.out.println((int) valor); // Imprime como inteiro
            } else {
                System.out.println(valor); // Imprime como double
            }
        }

        // Fechar o arquivo após a leitura
        arq.close();
        sc.close();
    }
}
