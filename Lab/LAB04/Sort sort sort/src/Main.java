import java.util.Scanner;

public class Main {

    // método de ordenação
    public static int ordenar(int nums[], int m) {
        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {

                // mods de I (posição atual) e J (posição da frente)
                int modI = nums[i] % m;
                int modJ = nums[j] % m;

                // Se os mods forem negativos, acrescenta à eles m
                if (modI < 0) modI += m;
                if (modJ < 0) modJ += m;

                // Se os mods forem diferentes, ordenar pelo módulo por ascendência (menor pro
                // maior)
                if (modI > modJ) {
                    int temp = nums[i];
                    nums[i] = nums[j];
                    nums[j] = temp;
                } else if (modJ == modI) { // Se os mods forem iguais

                    // Se houver um empate entre um número ímpar e um número par então o número
                    // impar irá preceder o número par
                    if ((nums[i] % 2 == 0) && (nums[j] % 2 != 0)) {
                        int temp = nums[i];
                        nums[i] = nums[j];
                        nums[j] = temp;
                    } // Se houver um empate entre dois números ímpares então o maior número ímpar irá
                      // preceder o menor número ímpar.
                    else if ((nums[i] % 2 != 0) && (nums[j] % 2 != 0) && (nums[i] < nums[j])) {
                        int temp = nums[i];
                        nums[i] = nums[j];
                        nums[j] = temp;
                    } // Se houve um empate entre dois números pares, então o menor número par irá
                      // preceder o maior número par.
                    else if ((nums[i] % 2 == 0) && nums[j] % 2 == 0 && (nums[i] > nums[j])) {
                        int temp = nums[i];
                        nums[i] = nums[j];
                        nums[j] = temp;
                    }
                }
            }
        }
        return 0;
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int n;
        int m;

        while (true) {

            n = sc.nextInt();
            m = sc.nextInt();
            if (n == 0 && m == 0) {
                System.out.println(n + " " + m); // Printa primeiro a quantidade e o mod
                break;
            }
            int nums[] = new int[n];

            for (int i = 0; i < n; i++) {
                nums[i] = sc.nextInt();
            }

            ordenar(nums, m);
            System.out.println(n + " " + m); // Printa primeiro a quantidade e o mod
            for (int i = 0; i < n; i++) {
                System.out.println(nums[i]); // Depois printa os números ordenados
            }
        }
        sc.close();
    }
}
