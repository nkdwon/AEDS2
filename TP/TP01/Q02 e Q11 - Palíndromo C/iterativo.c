#include <stdbool.h>
#include <stdio.h>
#include <string.h>

// Função para verificar se a string é um palíndromo.

bool ePalindromo(const char *str)
{
    int tamString = strlen(str); // Obtém o comprimento da string

    // Verifica cada caractere da string até a metade
    for (int i = 0; i < tamString / 2; i++)
    {
        // Compara o caractere atual com o caractere correspondente do final
        if (str[i] != str[tamString - i - 1])
        {
            return false; // Se qualquer caractere não corresponder, não é um palíndromo
        }
    }

    return true; // Todos os caracteres corresponderam, é um palíndromo
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
            // Verifica se a string é um palíndromo e imprime o resultado
            if (ePalindromo(str))
            {
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
