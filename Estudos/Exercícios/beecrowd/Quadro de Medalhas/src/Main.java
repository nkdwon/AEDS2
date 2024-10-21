/*
Alguém deixou o quadro de medalhas das olimpíadas fora de ordem. Seu programa deve colocá-lo na ordem correta. A ordem dos países no quadro de medalhas é dada pelo número de medalhas de ouro. Se há empate em medalhas de ouro, a nação que tiver mais medalhas de prata fica a frente. Havendo empate em medalhas de ouro e prata, fica mais bem colocado o país com mais medalhas de bronze. Se dois ou mais países empatarem nos três tipos de medalhas, seu programa deve mostrá-los em ordem alfabética.

Entrada
A entrada é dada pelo número de países participantes N (0 ≤ N ≤ 500) seguido pela lista dos países, com suas medalhas de ouro O (0 ≤ O ≤ 10000), prata P (0 ≤ P ≤ 10000) e bronze B (0 ≤ B ≤ 10000).
Saída
A saída deve ser a lista de países, com suas medalhas de ouro, prata e bronze, na ordem correta do quadro de medalhas, com as nações mais premiadas aparecendo primeiro.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static class Nation implements Comparable<Nation> {
        private String name;
        private int ouro = 0;
        private int prata = 0;
        private int bronze = 0;

        public Nation(String name, int ouro, int prata, int bronze) {
            this.name = name;
            this.ouro = ouro;
            this.prata = prata;
            this.bronze = bronze;
        }

        @Override
        public int compareTo(Nation outro) {

            if (this.ouro != outro.ouro) {
                return Integer.compare(outro.ouro, this.ouro);
            }
            if (this.prata != outro.prata) {
                return Integer.compare(outro.prata, this.prata);
            }
            if (this.bronze != outro.bronze) {
                return Integer.compare(outro.bronze, this.bronze);
            }

            return this.name.compareTo(outro.name);
        }

        @Override
        public String toString() {
            return this.name + " " + this.ouro + " " + this.prata + " " + this.bronze;
        }

    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        int numNations = sc.nextInt();
        List<Nation> nations = new ArrayList<>();

        for (int i = 0; i < numNations; i++) {
            String name = sc.next();
            int ouro = sc.nextInt();
            int prata = sc.nextInt();
            int bronze = sc.nextInt();
            nations.add(new Nation(name, ouro, prata, bronze));
        }

        Collections.sort(nations);

        for (Nation nation : nations) {
            System.out.println(nation);
        }

        sc.close();
    }

}
