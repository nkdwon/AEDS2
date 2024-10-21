/*
No ano de 2020 o Papai Noel n�o poder� sair de casa para entregar presentes por conta da pandemia do Coronavirus.
Ent�o ele ordenou que seus duendes fossem entregar no lugar dele no dia do natal.
Como eles s�o bastante inexperientes, ir�o se dividir em v�rios times compostos de tr�s membros: Um l�der, um entregador e um piloto de tren�.
O plano do Papai Noel � que os l�deres das equipes seja sempre os duendes mais velhos, por esse motivo ele pediu para todos escreverem seus nomes e idades em uma lista.
Como voc� � um duende programador, resolveu ajudar o Papai Noel a organizar a lista e montar os times a partir dela.

Segue abaixo algumas regras e fatos:
- A lista deve ser organizada em ordem descendente de idade;
- Caso dois duendes possu�rem a mesma idade, deve se organizar por ordem ascendente de nome;
- N�o existe dois duendes de mesmo nome;
- Nenhum duende tem mais de 20 caracteres em seu nome;
- Os duendes da lista tem idade entre 10 e 100 anos;
- Os primeiros 1/3 dos duendes (os mais velhos), ser�o os l�deres dos times;
- A ordem dos duendes entregadores e pilotos seguem a mesma l�gica dos l�deres.
Ex) Se h� 6 duendes na lista, haver� dois times, onde o duende mais velho � l�der do time 1, e o segundo mais velho � l�der do time 2.
O terceiro mais velho � entregador do time 1 e o quarto mais velho � entregador do time 2.
O quinto � piloto de tren� do time 1 e o �ltimo � piloto do time 2;

Entrada
A entrada � composta de um n�mero inteiro N (3 <= N <= 30), onde N � m�ltiplo de 3, que representa a quantidade de duedes na lista.
Em seguida as pr�ximas N linhas cont�m o nome e a idade de cada duende.

Sa�da
A sa�da � composta de 4 linhas por time. A primeira linha deve seguir o formato "Time X", onde X � o n�mero do time.
A segunda, terceira e quarta linha cont�m, respectivamente, o nome e idade do duende l�der, entregador e piloto de tren�.
Depois de cada time, dever� haver uma linha em branco, inclusive ap�s o �ltimo time.


*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct{

    char nome[21];
    int idade;

} Duende;

int comparaDuende(const void *a, const void *b){
    int constA = ((Duende *)a)->idade;
    int constB = ((Duende *)b)->idade;

    if(constA == constB){
        char *nomeA = ((Duende *)a)->nome;
        char *nomeB = ((Duende *)b)->nome;

        /*
        Outra forma de fazer:

        return strcmp(((Duende *)a)->nome, ((Duende *)b)->nome);
        */

        return strcmp(nomeA, nomeB);
    }

    return constB - constA;

}


int main()
{
    int total;
    scanf("%d", &total);
    Duende duendes[total];

    for(int i = 0; i < total; i++){
        scanf("%20s", duendes[i].nome);
        scanf("%d", &duendes[i].idade);
    }

    qsort(duendes, total, sizeof(Duende), comparaDuende);

    int numTotalTimes = total/3;

    for(int i = 0; i < numTotalTimes; i++){
        printf("Time %d\n", i + 1);
        printf("%s %d\n", duendes[i].nome, duendes[i].idade);
        printf("%s %d\n", duendes[i+numTotalTimes].nome, duendes[i+numTotalTimes].idade);
        printf("%s %d\n", duendes[i+2*numTotalTimes].nome, duendes[i+2*numTotalTimes].idade);
        printf("\n");
    }

    return 0;
}
