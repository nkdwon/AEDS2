#include <stdio.h>

int main()
{
    int n;
    double valor;
    FILE *arq;

    // Ler o número de valores reais a serem lidos
    scanf("%d", &n);

    // Abrir o arquivo para escrita
    arq = fopen("numeros.txt", "w");
    if (arq == NULL)
    {
        return 1; // Termina o programa caso o arquivo não possa ser aberto
    }

    // Ler os valores e escrever no arquivo
    for (int i = 0; i < n; i++)
    {
        scanf("%lf", &valor);
        fwrite(&valor, sizeof(double), 1, arq);
    }

    // Fechar o arquivo após a escrita
    fclose(arq);

    // Reabrir o arquivo para leitura
    arq = fopen("numeros.txt", "r");
    if (arq == NULL)
    {
        return 1; // Termina o programa caso o arquivo não possa ser aberto
    }

    // Ler os valores de trás para frente
    for (int i = n - 1; i >= 0; i--)
    {
        fseek(arq, i * sizeof(double), SEEK_SET); // Calcula a posição de leitura e posiciona o ponteiro diretamente no início do valor i-ésimo no arquivo.
        fread(&valor, sizeof(double), 1, arq);

        printf("%g\n", valor); // Imprime como double usando o formato mais compacto e adequado ( inteiro se x.0 ou double se real )
    }

    // Fechar o arquivo após a leitura
    fclose(arq);

    return 0;
}
