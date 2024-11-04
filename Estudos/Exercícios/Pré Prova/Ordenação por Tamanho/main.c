#include <stdio.h>   // Biblioteca para entrada e saída padrão
#include <stdlib.h>  // Biblioteca para manipulação de memória e utilidades
#include <string.h>  // Biblioteca para manipulação de strings

// Função para ordenar as palavras por comprimento em ordem decrescente
void insertion(char *palavras[], int count_frases)
{
  // Laço para percorrer todas as palavras a partir da segunda
  for (int i = 1; i < count_frases; i++)
  {
    char *temp = palavras[i]; // Guarda a palavra atual em temp
    int j = i - 1;

    // Desloca palavras maiores do que `temp` para a direita
    while (j >= 0 && strlen(palavras[j]) < strlen(temp))
    {
      palavras[j + 1] = palavras[j];
      j--;
    }
    palavras[j + 1] = temp; // Insere `temp` na posição correta
  }
}

int main()
{
  int n; // Número de casos de teste
  scanf("%d", &n);
  getchar(); // Consome o caractere de nova linha após o número de casos

  // Loop para cada caso de teste
  for (int i = 0; i < n; i++)
  {
    char frase[1024]; // Armazena a frase completa
    fgets(frase, sizeof(frase), stdin); // Lê a frase incluindo espaços

    // Remove o caractere de nova linha, caso exista
    frase[strcspn(frase, "\n")] = '\0';

    char *palavras[50]; // Array de ponteiros para palavras
    int count_frases = 0; // Contador de palavras na frase

    // Separa a frase em palavras usando strtok
    char *token = strtok(frase, " ");
    while (token != NULL && count_frases < 50)
    {
      palavras[count_frases++] = token; // Armazena cada palavra
      token = strtok(NULL, " "); // Próxima palavra
    }

    // Ordena as palavras pelo comprimento em ordem decrescente
    insertion(palavras, count_frases);

    // Imprime as palavras ordenadas
    for (int j = 0; j < count_frases; j++)  
    {
      printf("%s", palavras[j]); // Imprime cada palavra
      if (j < count_frases - 1)
      {
        printf(" "); // Adiciona espaço entre palavras
      }
    }
    printf("\n"); // Quebra de linha após cada caso de teste
  }

  return 0;
}
