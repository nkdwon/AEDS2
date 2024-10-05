#include <stdio.h>
#include <string.h>

// Recursivo e Funcional :))
/* void Combinador(char string1[], char string2[], char string3[], int tam1, int
tam2, int i, int j) {

  if (i >= tam1 && i >= tam2) {
    string3[j] = '\0';
    printf("%s\n", string3);
    return;
  }

    if (i < tam1) {
      string3[j++] = string1[i];
    }
    if (i < tam2) {
      string3[j++] = string2[i];
    }

  Combinador(string1, string2, string3, tam1, tam2, ++i, j);
} */

int main(void) {

  char string1[500], string2[500], string3[1000];

  while ((scanf("%s", string1) == 1) && (scanf("%s", string2) == 1)) {
    int tam1 = strlen(string1);
    int tam2 = strlen(string2);
    int j = 0;
    // Iterativo
    for (int i = 0; i < tam1 || i < tam2; i++) {
      if (i < tam1) {
        string3[j++] = string1[i];
      }
      if (i < tam2) {
        string3[j++] = string2[i];
      }
    }

    string3[j] = '\0';

    // Chamada Recursão
    // Combinador(string1, string2, string3, tam1, tam2, 0, 0);

    printf("%s\n", string3);
  }
  return 0;
}