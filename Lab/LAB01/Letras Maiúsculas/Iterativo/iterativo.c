#include <stdio.h>
#include <string.h>

int letrasMaiusculas(char *palavra) {
  int qtdLetras = 0;
  for (int i = 0; palavra[i] != '\0'; i++) {
    if (palavra[i] >= 'A' && palavra[i] <= 'Z') {
      qtdLetras++;
    }
  }
  return qtdLetras;
}

int main() {

  char palavra[500];

  while (fgets(palavra, sizeof(palavra), stdin)) {
      if (strcmp(palavra, "FIM\n") == 0) {
          break;
      }

      int qtdLetras = letrasMaiusculas(palavra);
      printf("%d\n", qtdLetras);
  }

  return 0;
}