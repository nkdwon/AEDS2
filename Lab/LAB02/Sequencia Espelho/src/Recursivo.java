import java.util.Scanner;
//Feito e ajustado por Mateus E Felipe

public class Recursivo {
    public static int sequencia_espelhada(int x, int y, int x2) {
        if (x <= y) {
            System.out.print(x);
            return sequencia_espelhada(x + 1, y, x2);
        }


        if (y >= x2) {
        int temp = y;
            while (temp > 0) {
                System.out.print(temp % 10);
                temp /= 10;
            }
            return sequencia_espelhada(x, y - 1, x2);
        }

        return 0;
    }

    public static void main(String[] agr) {
        Scanner scanner = new Scanner(System.in);

        while (scanner.hasNextInt()) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();


            sequencia_espelhada(x, y, x);
            System.out.println();
        }

        scanner.close();
    }
}
