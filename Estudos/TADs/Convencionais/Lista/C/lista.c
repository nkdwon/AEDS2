#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

#define MAX_TAM 10 // Tamanho máximo da lista

int lista[MAX_TAM]; // Array para armazenar os números
int n = 0; // Contador de elementos na lista

/**
 * Insere um número na primeira posição da lista e move os demais elementos para o fim.
 * @param elemento Valor do número a ser inserido.
 */
void inserirInicio(int elemento) {

   // Verifica se há espaço para inserir um novo número
   if (n >= MAX_TAM) {
      printf("Erro ao inserir! Lista cheia.\n"); // Mensagem de erro se a lista está cheia
      exit(1); // Encerra o programa em caso de erro
   } 

    // Move os elementos existentes para a direita, abrindo espaço no índice 0
    for (int i = n; i > 0; i--) {
      lista[i] = lista[i-1]; // Desloca cada número uma posição para a direita
    }

   // Insere o novo número na primeira posição
   lista[0] = elemento;
   n++; // Incrementa o contador de elementos  
}

/**
 * Insere um número na última posição da lista.
 * @param elemento Valor do número a ser inserido.
 */
void inserirFim(int elemento) {

   // Verifica se há espaço para inserir um novo número
    if (n >= MAX_TAM) {
        printf("Erro ao inserir! Lista cheia.\n"); // Mensagem de erro se a lista está cheia
        exit(1); // Encerra o programa em caso de erro
    }

    // Insere o novo número na última posição disponível
    lista[n] = elemento; 
    n++; // Incrementa o contador de elementos
}

/**
 * Insere um número em uma posição específica e move os demais elementos para o fim.
 * @param elemento Valor do número a ser inserido.
 * @param pos Posição onde o número será inserido.
 */
void inserir(int elemento, int pos) {

   // Verifica se a inserção é possível
    if (n >= MAX_TAM || pos < 0 || pos > n) {
        printf("Erro ao inserir! Posição inválida ou lista cheia.\n"); // Mensagem de erro para lista cheia ou posição inválida
        exit(1); // Encerra o programa em caso de erro
    }

    // Move os elementos existentes para a direita, abrindo espaço na posição `pos`
    for (int i = n; i > pos; i--) {
        lista[i] = lista[i - 1]; // Desloca cada número uma posição para a direita
    }

    // Insere o novo número na posição especificada
    lista[pos] = elemento; 
    n++; // Incrementa o contador de elementos
}


/**
 * Remove um número da primeira posição da lista e movimenta os demais elementos para o início.
 * @return Valor do número que foi removido.
 */
int removerInicio() {

   // Verifica se a lista está vazia
    if (n == 0) {
        printf("Erro ao remover! Lista vazia.\n"); // Mensagem de erro se a lista está vazia
        exit(1); // Encerra o programa em caso de erro
    }

    int resp = lista[0]; // Guarda o número que será removido
    n--; // Decrementa o contador de elementos

    // Move os elementos restantes para o início do array
    for (int i = 0; i < n; i++) {
        lista[i] = lista[i + 1]; // Desloca cada número uma posição para a esquerda
    }

    return resp; // Retorna o número removido
}

/**
 * Remove um número da última posição da lista.
 * @return Valor do número que foi removido.
 */
int removerFim() {

   // Verifica se a lista está vazia
    if (n == 0) {
        printf("Erro ao remover! Lista vazia.\n"); // Mensagem de erro se a lista está vazia
        exit(1); // Encerra o programa em caso de erro
    }

    return lista[--n]; // Decrementa o contador e retorna o número da última posição
}

/**
 * Remove um número de uma posição específica da lista e movimenta os demais elementos para o início.
 * @param pos Posição de remoção.
 * @return Valor do número que foi removido.
 */
int remover(int pos) {
    // Verifica se a remoção é possível
    if (n == 0 || pos < 0 || pos >= n) {
        printf("Erro ao remover! Lista vazia ou posição inválida.\n"); // Mensagem de erro para lista vazia ou posição inválida
        exit(1); // Encerra o programa em caso de erro
    }

    int resp = lista[pos]; // Guarda o número que será removido
    n--; // Decrementa o contador de elementos

    // Move os elementos restantes para o início do array
    for (int i = pos; i < n; i++) {
        lista[i] = lista[i + 1]; // Desloca cada número uma posição para a esquerda
    }

    return resp; // Retorna o número removido
}

/**
 * Mostra todos os números armazenados na lista.
 */
void mostrar() {
    // Imprime cada número armazenado na lista
    printf("[ ");
    for (int i = 0; i < n; i++) {
        printf("%d ", lista[i]); // Exibe o valor do número
    }
    printf("]\n");
}

/**
 * Pesquisa um número na lista e retorna se ele foi encontrado.
 * @param elemento Valor do número a ser pesquisado.
 * @return true se o número foi encontrado; false caso contrário.
 */
bool pesquisar(int elemento) {
    bool retorno = false; // Inicializa a variável que indica se o número foi encontrado

    // Itera sobre a lista para procurar o número
    for (int i = 0; i < n; i++) {
        if (lista[i] == elemento) { // Verifica se o elemento atual é o número procurado
            retorno = true; // Define como true se o número for encontrado
            i = n; // Encerra a busca antecipadamente, pois o número foi encontrado
        }
    }
    
    return retorno; // Retorna true se o número foi encontrado, caso contrário retorna false
}

int main() {
    // Aqui você pode realizar testes chamando as funções de inserção e remoção

    return 0;
}