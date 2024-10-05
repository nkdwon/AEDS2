import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            int a = sc.nextInt();
            int b = sc.nextInt();

            for (int i = a; i <= b; i++) {
                System.out.print(i);

            }
            for (int i = b; i >= a; i--) {
                if (i >= 10) {
                    int temp = i;
                    while (temp > 0) {
                        System.out.print(temp%10);
                        temp/=10;
                    }
                } else {
                    System.out.print(i);
                }
            }
            System.out.println();
        }
        sc.close();
    }
}
