/*
 No ano de 2020 o Papai Noel não poderá sair de casa para entregar presentes por conta da pandemia do Coronavirus. Então ele ordenou que seus duendes fossem entregar no lugar dele no dia do natal. Como eles são bastante inexperientes, irão se dividir em vários times compostos de três membros: Um líder, um entregador e um piloto de trenó. O plano do Papai Noel é que os líderes das equipes seja sempre os duendes mais velhos, por esse motivo ele pediu para todos escreverem seus nomes e idades em uma lista. Como você é um duende programador, resolveu ajudar o Papai Noel a organizar a lista e montar os times a partir dela.

Segue abaixo algumas regras e fatos:
- A lista deve ser organizada em ordem descendente de idade;
- Caso dois duendes possuírem a mesma idade, deve se organizar por ordem ascendente de nome;
- Não existe dois duendes de mesmo nome;
- Nenhum duende tem mais de 20 caracteres em seu nome;
- Os duendes da lista tem idade entre 10 e 100 anos;
- Os primeiros 1/3 dos duendes (os mais velhos), serão os líderes dos times;
- A ordem dos duendes entregadores e pilotos seguem a mesma lógica dos líderes. Ex) Se há 6 duendes na lista, haverá dois times, onde o duende mais velho é líder do time 1, e o segundo mais velho é líder do time 2. O terceiro mais velho é entregador do time 1 e o quarto mais velho é entregador do time 2. O quinto é piloto de trenó do time 1 e o último é piloto do time 2;

Entrada
A entrada é composta de um número inteiro N (3 <= N <= 30), onde N é múltiplo de 3, que representa a quantidade de duedes na lista. Em seguida as próximas N linhas contém o nome e a idade de cada duende.

Saída
A saída é composta de 4 linhas por time. A primeira linha deve seguir o formato "Time X", onde X é o número do time. A segunda, terceira e quarta linha contém, respectivamente, o nome e idade do duende líder, entregador e piloto de trenó. Depois de cada time, deverá haver uma linha em branco, inclusive após o último time.
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static class Duende implements Comparable<Duende>{
        private String nome;
        private int idade;

        public Duende(String nome, int idade){
            this.nome = nome;
            this.idade = idade;
        }

        @Override
        public int compareTo(Duende outro){
            
            if(this.idade == outro.idade){
                return this.nome.compareTo(outro.nome);
            }

            return Integer.compare(outro.idade, this.idade);
        }


        @Override
        public String toString(){
            return this.nome + " " + this.idade;
        }

    }


    public static void main(String[] args) throws Exception {
        
        Scanner sc = new Scanner(System.in);
        int numDuendes = sc.nextInt();
        List<Duende> duendes = new ArrayList<>();

        for(int i = 0; i < numDuendes; i++){
            String nome = sc.next();
            int idade = sc.nextInt();
            duendes.add(new Duende(nome, idade));
        }

        Collections.sort(duendes);

        int numTotalTimes = numDuendes/3;

        for(int i = 0; i < numTotalTimes; i++){
            System.out.println("Time" + (i + 1));
            System.out.println(duendes.get(i));
            System.out.println(duendes.get(i+numTotalTimes));
            System.out.println(duendes.get(i+2*numTotalTimes));
            System.out.println();
        }

        sc.close();
    }
}

// Alguns códigos de ordenação 
/*

// SelectionSort -> n^2
void selectionSort(int array[], int length)
{
    for ( int i = 0; i < length - 1; i++ )
    {
        int lowest = i;
        for ( int j = i + 1; j < length; j++ )
        {
            if ( array[lowest] > array[j] )
            {
                lowest = j;
            }
        }
        swap(array, i, lowest);
    }
}

// BubbleSort -> n^2
void bubbleSort(int array[], int length)
{
    for (int i = 0; i < length - 1; i++)
    {
        for (int j = 0; j < length - i - 1; j++)
        {
            if (array[j] > array[j+1])
            {
                swap(array, j, j + 1);
            }
        }
    }
}

// C

strcmp( )
int resultado = strcmp(palavra1, palavra2);
----
fgets(nome, 50, stdin);  // Lê até 49 caracteres ou até '\n'
---
strcpy( )
char origem[] = "Olá, Mundo!";
    char destino[50];

    strcpy(destino, origem);  // Copia "Olá, Mundo!" para a variável 'destino'
---------------
fgets é útil para capturar entrada de texto com espaços.
strcpy copia o conteúdo de uma string para outra.
strcmp compara duas strings para verificar se são iguais ou qual vem primeiro em ordem lexicográfica.

 */