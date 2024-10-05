#include <stdio.h>
#include <string.h>

void letrasMaiusculas(char *palavra) {
  int qtdLetras = 0;

  for (int i = 0; palavra[i] != '\0'; i++) {
    if (palavra[i] >= 'A' && palavra[i] <= 'Z') {
      qtdLetras++;
    }
  }

  printf("%d\n", qtdLetras);

  if (fgets(palavra, 500, stdin)) {
    if (strcmp(palavra, "FIM\n") != 0) {
      letrasMaiusculas(palavra);
    }
  }
}

int main() {

  char palavra[500];

  if (fgets(palavra, sizeof(palavra), stdin)) {
    letrasMaiusculas(palavra);
  }

  return 0;
}