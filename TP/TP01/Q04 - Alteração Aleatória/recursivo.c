#include <stdio.h>
#include <stdlib.h>
#include <time.h>

// Função recursiva para substituir letras
void substituirLetras(char *str, char primeiraLetra, char segundaLetra)
{
  // Caso base: se o caractere atual for o caractere nulo, retornar
  if (*str == '\0')
  {
    return;
  }

  // Substituir o caractere atual se ele for a primeira letra
  if (*str == primeiraLetra)
  {
    *str = segundaLetra;
  }

  // Chamada recursiva para o próximo caractere
  substituirLetras(str + 1, primeiraLetra, segundaLetra);
}

int main()
{
  // Configuração do gerador de números aleatórios
  srand(4);

  // Sorteia duas letras aleatórias
  char primeiraLetra = 'a' + rand() % 26;
  char segundaLetra = 'a' + rand() % 26;

  char str[1000];

  do
  {
    if (fgets(str, sizeof(str), stdin) != NULL)
    {
      // Remove o caractere de nova linha, se presente
      str[strcspn(str, "\n")] = '\0';
    }

    // Verifica se a entrada é "FIM" para encerrar o programa
    if (strcmp(str, "FIM") != 0)
    {
      // Substitui as letras na string
      substituirLetras(str, primeiraLetra, segundaLetra);

      // Exibe a string alterada
      printf("%s\n", str);
    }

  } while (strcmp(str, "FIM") != 0);

  return 0;
}
