/* CUSTO TOTAL: O(n^2) - CUSTO GLOBAL (VERDE): O(t * n^2)
*
* Operações relevantes:
* 1. encontrar_posicao: O(n)
* 2. contar_ultrapassagens: O(n^2)
* 3. main: O(t * n^2)
*
* RESUMO:
* O código tem uma complexidade total de O(n^2) para cada caso de teste, onde n é o número de competidores. 
* Como processamos múltiplos casos, a complexidade global seria O(t * n^2), onde t é o número de casos.
*
*/

#include <stdio.h>

// Função para encontrar a posição de um competidor na ordem de chegada
// Complexidade: O(n), onde n é o número de competidores
int encontrar_posicao(int competidor, int ordem_chegada[], int num_competidores)
{

  for (int i = 0; i < num_competidores; i++)
  {
    if (ordem_chegada[i] == competidor)
    {
      return i;
    }
  }
  return -1; // Caso não encontre (não deve ocorrer se a entrada for válida)
}

// Função para contar ultrapassagens usando Selection Sort
// Complexidade total: O(n^2), onde n é o número de competidores
void contar_ultrapassagens(int ordem_largada[], int ordem_chegada[], int num_competidores)
{
  int num_ultrapassagens = 0;

  // Selection Sort para simular as trocas necessárias
  // O loop externo roda O(n) vezes, onde n é o número de competidores
  for (int i = 0; i < num_competidores - 1; i++)
  {
    int pos_atual = encontrar_posicao(ordem_largada[i], ordem_chegada, num_competidores); // O(n) para encontrar a posição

    // Verifica se o competidor foi ultrapassado
    // O loop interno roda aproximadamente O(n) vezes no pior caso, comparando cada competidor à frente de 'i'
    for (int j = i + 1; j < num_competidores; j++)
    {
      int pos_proximo = encontrar_posicao(ordem_largada[j], ordem_chegada, num_competidores); // O(n) para cada comparação

      // Caso o competidor à frente tenha sido ultrapassado por um competidor que largou atrás
      if (pos_proximo < pos_atual)
      {
        num_ultrapassagens++;
      }
    }
  }
  // A operação de impressão tem uma complexidade O(1)
  printf("%d\n", num_ultrapassagens);
}

int main()
{
  int num_competidores = 0;

  // Complexidade: O(t * n^2), onde t é o número de casos (entradas) e n o número de competidores por caso
  while (scanf("%d", &num_competidores) != EOF)
  {

    int ordem_largada[num_competidores];
    int ordem_chegada[num_competidores];

    // Lê a ordem de largada - O(n)
    for (int i = 0; i < num_competidores; i++)
    {
      scanf("%d", &ordem_largada[i]);
    }

    // Lê a ordem de chegada - O(n)
    for (int i = 0; i < num_competidores; i++)
    {
      scanf("%d", &ordem_chegada[i]);
    }

    // Contabiliza as ultrapassagens - O(n^2)
    contar_ultrapassagens(ordem_largada, ordem_chegada, num_competidores);
  }

  return 0;
}