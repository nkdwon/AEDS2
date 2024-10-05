#include <stdbool.h>
#include <stdio.h>
#include <string.h>

// Função recursiva para verificar se a string é um palíndromo.
// Utiliza dois índices, i e j, para comparar os caracteres das extremidades da string.
bool ePalindromo(const char *str, int i, int j)
{
  // Se o índice inicial for maior ou igual ao índice final, a string é um palíndromo, pois já vai ter percorrido toda palavra
  if (i >= j)
  {
    return true;
  }

  // Se os caracteres nas posições i e j não corresponderem, não é um palíndromo e retorna falso
  if (str[i] != str[j])
  {
    return false;
  }

  // Chama recursivamente a função com os índices atualizados
  return ePalindromo(str, ++i, --j);
}

int main()
{
  char str[500]; // Variável para armazenar a entrada do usuário

  do
  {
    // Lê uma linha de entrada do usuário
    if (fgets(str, sizeof(str), stdin) != NULL)
    {
      // Remove o caractere de nova linha, se presente
      str[strcspn(str, "\n")] = '\0';
    }

    if (strcmp(str, "FIM") != 0)
    {
      // Obtém o comprimento da string
      int tamString = strlen(str);

      // Verifica se a string é um palíndromo e imprime o resultado
      if (ePalindromo(str, 0, tamString - 1))
      { // 0 primeira letra e tamString - 1 última letra
        printf("SIM\n");
      }
      else
      {
        printf("NAO\n");
      }
    }

  } while (strcmp(str, "FIM") != 0); // Continua lendo até que o usuário digite "FIM"

  return 0;
}
