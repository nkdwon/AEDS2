#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <time.h> // Para manipulação de datas

// Variáveis Globais
#define MAX_NAME_LEN 100
#define MAX_DESC_LEN 255
#define MAX_TYPES 2
#define MAX_ABILITIES 100
#define MAX_LINE_LEN 1024
#define MAX_TAM_LISTA 802
int numPokemonsPokedex = 0;
int countComps = 0;
int countSwaps = 0;

typedef struct
{
    int id;
    int generation;
    char name[MAX_NAME_LEN];
    char description[MAX_DESC_LEN];
    char *types[MAX_TYPES];         // Array de ponteiros para strings
    char abilities[MAX_ABILITIES]; // Array de ponteiros para strings
    double weight;
    double height;
    int captureRate;
    int isLegendary; // 0 para falso, 1 para verdadeiro
    char data[80];
} Pokemon;

// Função para imprimir o pokémon
void printPokemon(Pokemon *pokemon)
{
    // Exibe a informação do Pokémon no formato desejado
    printf("[#%d -> %s: %s - [", pokemon->id, pokemon->name, pokemon->description);
    
    // Exibe os tipos
    printf("'%s'", pokemon->types[0]);
    if (pokemon->types[1][0] != '\0')
    {
        printf(", '%s'", pokemon->types[1]);
    }
    printf("] - ");

    // Exibe as habilidades
    printf("%s", pokemon->abilities);
    printf(" - %.1fkg - %.1fm - %d%% - %s - %d gen] - %s\n",
           pokemon->weight, pokemon->height, pokemon->captureRate,
           pokemon->isLegendary ? "true" : "false", pokemon->generation, pokemon->data);
}

// Função para substituir virgulas dentro colchetes 
void substituirVirgulasDentroColchetes(char *str)
{
    int dentroColchetes = 0; // Flag para indicar se estamos dentro de colchetes

    while (*str)
    {
        if (*str == '[')
        {
            dentroColchetes = 1; // Encontrou um colchete de abertura, começa a substituição
        }
        else if (*str == ']')
        {
            dentroColchetes = 0; // Encontrou um colchete de fechamento, para a substituição
        }
        else if (*str == ',' && dentroColchetes)
        {
            *str = ';'; // Substitui ',' por ';' se estivermos dentro de colchetes
        }
        str++; // Avança para o próximo caractere
    }
}

// Função strtok melhorada!!
char *strsep(char **stringp, const char *delim)
{
    char *start = *stringp;
    char *end;

    if (start == NULL)
    {
        return NULL;
    }

    end = strpbrk(start, delim); // Procura o primeiro delimitador
    if (end)
    {
        *end = '\0';        // Substitui o delimitador por \0
        *stringp = end + 1; // Atualiza o ponteiro para a próxima posição
    }
    else
    {
        *stringp = NULL; // Não há mais tokens
    }

    return start;
}

// Construtor de Pokémon
Pokemon *createPokemon(int id, int generation, const char *name, const char *description,
                       const char *type1, const char *type2, const char *ability,
                       double weight, double height, int captureRate, int isLegendary)
{
    Pokemon *pokemon = malloc(sizeof(Pokemon));
    if (pokemon == NULL)
    {
        return NULL; // Lidar com erro de alocação de memória
    }

    pokemon->id = id;
    pokemon->generation = generation;
    strncpy(pokemon->name, name, MAX_NAME_LEN - 1);
    pokemon->name[MAX_NAME_LEN - 1] = '\0'; // Garante terminação nula
    strncpy(pokemon->description, description, MAX_DESC_LEN - 1);
    pokemon->description[MAX_DESC_LEN - 1] = '\0';

    // Alocar e copiar tipos
    pokemon->types[0] = malloc(strlen(type1) + 1);
    strcpy(pokemon->types[0], type1);
    if (type2 && strlen(type2) > 0)
    {
        pokemon->types[1] = malloc(strlen(type2) + 1);
        strcpy(pokemon->types[1], type2);
    }
    else
    {
        pokemon->types[1] = NULL;
    }

    // Alocar e copiar habilidades
    strcpy(pokemon->abilities, ability);

    pokemon->weight = weight;
    pokemon->height = height;
    pokemon->captureRate = captureRate;
    pokemon->isLegendary = isLegendary;

    return pokemon;
}

// Função para liberar memória dos 'tipos' de pokémon
void freePokemon(Pokemon *pokemon)
{
    for (int i = 0; i < MAX_TYPES; i++)
    {
        if (pokemon->types[i] != NULL)
        {
            free(pokemon->types[i]);
        }
    }
    free(pokemon);
}

// Função para remover as aspas de uma string
void removeAspas(char *str)
{
    char *p = str;
    while (*str)
    {
        if (*str != '"')
        {
            *p++ = *str;
        }
        str++;
    }
    *p = '\0';
}

// Função para trocar ';' por ','
void trocaPontoVirgula(char *str)
{
    char *p = str;
    while (*p)
    {
        if (*p == ';')
        {
            *p = ',';
        }
        p++;
    }
    *p = '\0';
}

// Função para ler linha do csv
void lerLinhaCsv(char *linha, Pokemon *pokemon)
{
    substituirVirgulasDentroColchetes(linha);

    char *token;

    // Remover quebra de linha
    linha[strcspn(linha, "\r\n")] = 0;

    // Ponteiro que vai andar pela linha
    char *rest = linha;

    // Processar CSV e lidar com campos entre aspas
    token = strsep(&rest, ",");
    pokemon->id = atoi(token);

    token = strsep(&rest, ",");
    pokemon->generation = atoi(token);

    token = strsep(&rest, ",");
    strncpy(pokemon->name, token, MAX_NAME_LEN - 1);
    pokemon->name[MAX_NAME_LEN - 1] = '\0';

    token = strsep(&rest, ",");
    strncpy(pokemon->description, token, MAX_DESC_LEN - 1);
    pokemon->description[MAX_DESC_LEN - 1] = '\0';

    // Lendo tipos
    token = strsep(&rest, ",");
    if (token)
    {
        pokemon->types[0] = strdup(token);
        removeAspas(pokemon->types[0]);
    }
    else
    {
        pokemon->types[0] = NULL;
    }

    token = strsep(&rest, ",");
    if (token)
    {
        pokemon->types[1] = strdup(token);
        removeAspas(pokemon->types[1]);
    }
    else
    {
        pokemon->types[1] = NULL;
    }

    // Ler habilidades, e remover aspas se necessário
    token = strsep(&rest, ",");
    removeAspas(token);

    // troca ';' por ',' no token
    trocaPontoVirgula(token);
    strcpy(pokemon->abilities, token);

    // Lendo peso e altura corretamente
    token = strsep(&rest, ",");
    if (token)
    {
        pokemon->weight = atof(token); // Peso
    }
    else
    {
        pokemon->weight = 0.0;
    }

    token = strsep(&rest, ",");
    if (token)
    {
        pokemon->height = atof(token); // Altura
    }
    else
    {
        pokemon->height = 0.0;
    }

    // Lendo captureRate corretamente
    token = strsep(&rest, ",");
    if (token)
    {
        pokemon->captureRate = atoi(token); // Taxa de captura
    }
    else
    {
        pokemon->captureRate = 0;
    }

    // Lendo isLegendary corretamente
    token = strsep(&rest, ",");
    if (token)
    {
        pokemon->isLegendary = atoi(token); // Se é lendário
    }
    else
    {
        pokemon->isLegendary = 0;
    }

    token = strsep(&rest, ",");
    strcpy(pokemon->data, token);
}

Pokemon *buscarPokemonPorId(Pokemon *lista[], int tamanhoLista, int id)
{
    for (int i = 0; i < tamanhoLista; i++)
    {
        if (lista[i]->id == id)
        {
            return lista[i];
        }
    }
    return NULL;
}

typedef struct CelulaDupla
{
    Pokemon pokemon;              // Dados do Pokémon
    struct CelulaDupla *prox;     // Ponteiro para o próximo
    struct CelulaDupla *ant;      // Ponteiro para o anterior
} CelulaDupla;

typedef struct Pokedex
{
    CelulaDupla *primeiro;        // Ponteiro para a cabeça da lista
    CelulaDupla *ultimo;          // Ponteiro para a cauda da lista
    int numPokemonsPokedex; // Contador de elementos na lista
} Pokedex;

/**
 * Cria uma nova célula com o Pokémon fornecido.
 * @param pokemon Ponteiro para o Pokémon a ser armazenado na nova célula.
 * @return Retorna a nova célula criada.
 */
CelulaDupla* novaCelulaDupla(Pokemon *pokemon) {
    CelulaDupla* nova = (CelulaDupla*) malloc(sizeof(CelulaDupla)); // Aloca memória para a nova célula
    nova->pokemon = *pokemon;  // Copia o Pokémon para a nova célula
    nova->ant = nova->prox = NULL; // Define o próximo como NULL (último elemento)
    return nova;
}

/**
 * Inicializa uma nova lista Pokedex.
 * @return Retorna um ponteiro para a nova Pokedex criada.
 */
Pokedex* start() {
    Pokedex* pokedex = (Pokedex*) malloc(sizeof(Pokedex)); // Aloca memória para a Pokedex
    if (pokedex) {
        Pokemon *x;
        pokedex->primeiro = novaCelulaDupla(x); // Cria o topo inicial da lista dupla como uma célula vazia
        pokedex->ultimo = pokedex->primeiro;
        pokedex->numPokemonsPokedex = 0;    // Inicializa o contador de Pokémons
    }
    return pokedex;
}

/**
 * Insere um Pokémon na primeira posição da lista e move os demais elementos para o fim.
 * @param pokedex Array de Pokémons onde será realizada a inserção.
 * @param pokemon Ponteiro para o Pokémon a ser inserido.
 */
void inserirInicio(Pokedex *pokedex, Pokemon *pokemon)
{

   CelulaDupla* tmp = novaCelulaDupla(pokemon);

   tmp->ant = pokedex->primeiro;
   tmp->prox = pokedex->primeiro->prox;
   pokedex->primeiro->prox = tmp;
   if (pokedex->primeiro == pokedex->ultimo) 
   {                    
      pokedex->ultimo = tmp;
   } else 
   {
      tmp->prox->ant = tmp;
   }
   tmp = NULL;
   pokedex->numPokemonsPokedex++;  
}

/**
 * Insere um Pokémon na última posição da lista.
 * @param pokedex Array de Pokémons onde será realizada a inserção.
 * @param pokemon Ponteiro para o Pokémon a ser inserido.
 */
void inserirFim(Pokedex *pokedex, Pokemon *pokemon) 
{
    pokedex->ultimo->prox = novaCelulaDupla(pokemon);
    pokedex->ultimo->prox->ant = pokedex->ultimo;
    pokedex->ultimo = pokedex->ultimo->prox;
    pokedex->numPokemonsPokedex++;
}

/**
 * Insere um Pokémon em uma posição específica e move os demais elementos para o fim.
 * @param pokedex Array de Pokémons onde será realizada a inserção.
 * @param pokemon Ponteiro para o Pokémon a ser inserido.
 * @param pos Posição onde o Pokémon será inserido.
 */
void inserir(Pokedex *pokedex, Pokemon *pokemon, int pos) 
{
    if(pos < 0 || pos > numPokemonsPokedex)
    {
        printf("Erro ao remover (posicao %d/%d invalida!", pos, numPokemonsPokedex);
        exit(1);
    } else if (pos == 0)
    {
        inserirInicio(pokedex, pokemon);
    } else if (pos == numPokemonsPokedex - 1)
    {
        inserirFim(pokedex, pokemon);
    } else 
    {
        // Caminhar ate a posicao anterior a insercao
        CelulaDupla* i = pokedex->primeiro;
        int j;
        for(j = 0; j < pos; j++, i = i->prox);

        CelulaDupla* tmp = novaCelulaDupla(pokemon);
        tmp->ant = i;
        tmp->prox = i->prox;
        tmp->ant->prox = tmp->prox->ant = tmp;
        tmp = i = NULL;
        pokedex->numPokemonsPokedex++;
    }
}

/**
 * Remove um Pokémon da primeira posição da lista e movimenta os demais elementos para o início.
 * @param pokedex Array de Pokémons de onde será removido o Pokémon.
 * @return O Pokémon que foi removido.
 */
Pokemon removerInicio(Pokedex *pokedex) 
{

    if (pokedex->primeiro == pokedex->ultimo) 
    {
        printf("Erro ao remover (vazia)!");
        exit(1);
    }

    CelulaDupla* tmp = pokedex->primeiro;
    pokedex->primeiro = pokedex->primeiro->prox;
    Pokemon resp = pokedex->primeiro->pokemon;
    tmp->prox = pokedex->primeiro->ant = NULL;
    free(tmp);
    tmp = NULL;
    pokedex->numPokemonsPokedex--; 
    return resp; 
}

/**
 * Remove um Pokémon da última posição da lista.
 * @param pokedex Array de Pokémons de onde será removido o Pokémon.
 * @return O Pokémon que foi removido.
 */
Pokemon removerFim(Pokedex *pokedex) 
{

    if (pokedex->primeiro == pokedex->ultimo) 
    {
        printf("Erro ao remover (vazia)!");
        exit(1);
    } 
    Pokemon resp = pokedex->ultimo->pokemon;
    pokedex->ultimo = pokedex->ultimo->ant;
    pokedex->ultimo->prox->ant = NULL;
    free(pokedex->ultimo->prox);
    pokedex->ultimo->prox = NULL;
    pokedex->numPokemonsPokedex--;
    return resp;
}

/**
 * Remove um Pokémon de uma posição específica da lista e movimenta os demais elementos para o início.
 * @param pokedex Array de Pokémons de onde será removido o Pokémon.
 * @param pos Posição de remoção.
 * @return O Pokémon que foi removido.
 */
Pokemon remover(Pokedex *pokedex, int pos) 
{
    Pokemon resp;
    if (pokedex->primeiro == pokedex->ultimo)
    {
        printf("Erro ao remover (vazia)!");
        exit(1);
    } else if(pos < 0 || pos >= numPokemonsPokedex)
    {
        printf("Erro ao remover (posicao %d/%d invalida!", pos, numPokemonsPokedex);
        exit(1);
    } else if (pos == 0)
    {
        resp = removerInicio(pokedex);
    } else if (pos == numPokemonsPokedex - 1)
    {
        resp = removerFim(pokedex);
    } else 
    {
        // Caminhar ate a posicao anterior a insercao
        CelulaDupla* i = pokedex->primeiro->prox;
        int j;
        for(j = 0; j < pos; j++, i = i->prox);

        i->ant->prox = i->prox;
        i->prox->ant = i->ant;
        resp = i->pokemon;
        i->prox = i->ant = NULL;
        free(i);
        i = NULL;
        pokedex->numPokemonsPokedex--;
    }

    return resp;
}

/**
 * Mostra todos os Pokémons armazenados na Pokédex.
 * @param pokedex Array de Pokémons a ser exibido.
 */
void mostrar(Pokedex *pokedex) 
{
    // Itera sobre a lista de Pokémons e imprime cada um
    for (CelulaDupla* i = pokedex->primeiro->prox; i != NULL; i = i->prox) 
    {
        printf("[%d] ", i); // Exibe o índice do Pokémon
        printPokemon(&i->pokemon); // Chama uma função para imprimir as informações do Pokémon
    }
}

void swap(CelulaDupla *a, CelulaDupla *b) {
    Pokemon temp = a->pokemon; // Salva temporariamente o Pokémon da célula 'a'
    a->pokemon = b->pokemon;  // Copia o Pokémon da célula 'b' para 'a'
    b->pokemon = temp;        // Restaura o Pokémon salvo de 'a' para 'b'
}

CelulaDupla* particionar(CelulaDupla *esq, CelulaDupla *dir, int *comps, int *swaps) {
    Pokemon *pivo = &dir->pokemon; // Define o pivô como o Pokémon na célula 'dir' (último elemento)
    CelulaDupla *i = esq->ant;    // Ponteiro que acompanhará os menores que o pivô

    for (CelulaDupla *j = esq; j != dir; j = j->prox) { // Itera de 'esq' até 'dir'
        (*comps)++; // Incrementa o contador de comparações
        if (j->pokemon.generation < pivo->generation || 
            (j->pokemon.generation == pivo->generation && strcmp(j->pokemon.name, pivo->name) < 0)) {
            i = (i == NULL) ? esq : i->prox; // Avança 'i' para o próximo elemento
            swap(i, j);              // Troca os elementos nas posições 'i' e 'j'
            (*swaps)++;                     // Incrementa o contador de trocas
        }
    }
    i = (i == NULL) ? esq : i->prox; // Avança 'i' para a posição do pivô
    swap(i, dir); // Coloca o pivô na posição correta
    (*swaps)++;          // Incrementa o contador de trocas
    return i; // Retorna o ponteiro para o pivô
}

void quickSort(CelulaDupla *esq, CelulaDupla *dir, int *comps, int *swaps) {
    // Verifica se o intervalo é válido (não vazio ou já ordenado)
    if (esq != NULL && dir != NULL && esq != dir && esq != dir->prox) {
        CelulaDupla *pivo = particionar(esq, dir, comps, swaps); // Particiona a lista
        quickSort(esq, pivo->ant, comps, swaps);  // Ordena a parte à esquerda do pivô
        quickSort(pivo->prox, dir, comps, swaps); // Ordena a parte à direita do pivô
    }
}

int main()
{
    // Variáveis para leitura do arquivo
    FILE *arquivo;
    Pokemon **listaPokemons = NULL;
    int numPokemons = 0;
    char input[MAX_NAME_LEN];

    // Variáveis para Lista Sequencial simples de Pokémons
    Pokedex* pokedex = start();
     clock_t start, end;

    // Diretório arquivo CSV
    arquivo = fopen("D:/FELIPE/PUC Faculdade/SEMESTRE II/AEDS2/TP/TP03/pokemon.csv", "r");
    if (arquivo == NULL)
    { 
        printf("Erro ao abrir o arquivo.\n");
        return 1;
    }

    char linha[MAX_LINE_LEN];
    // Lê o cabeçalho do arquivo
    fgets(linha, MAX_LINE_LEN, arquivo);

    // Leitura do arquivo CSV
    while (fgets(linha, MAX_LINE_LEN, arquivo) != NULL)
    {
        Pokemon *pokemon = createPokemon(0, 0, "", "", "", "", "", 0.0, 0.0, 0, 0);
        lerLinhaCsv(linha, pokemon);
        listaPokemons = realloc(listaPokemons, (numPokemons + 1) * sizeof(Pokemon *));
        listaPokemons[numPokemons] = pokemon;
        numPokemons++;
    }

    fclose(arquivo);

    scanf("%s", input);
    while (strcmp(input, "FIM") != 0)
    {
        int id = atoi(input);
        Pokemon *pokemonEncontrado = buscarPokemonPorId(listaPokemons, numPokemons, id);
        if (pokemonEncontrado)
        {
            inserirFim(pokedex, pokemonEncontrado);
        }
        else
        {
            printf("Pokemon não encontrado.\n");
        }
        scanf("%s", input);
    }

    // Medir o tempo de execução da ordenação
    start = clock();
    quickSort(pokedex->primeiro, pokedex->ultimo, &countComps, &countSwaps);
    end = clock();
    for(CelulaDupla* i = pokedex->primeiro->prox; i != NULL; i = i->prox){ // !
        printPokemon(&i->pokemon);
    }

    // Escrever arquivo de log
    FILE *logFile = fopen("732683_quicksort2.txt", "w");
    if (logFile == NULL) {

        printf("Erro ao criar o arquivo de log.\n");
        return 1;

    } else {
        // Calcula o tempo gasto em milissegundos
        double timeTaken = ((double)(end - start)) / CLOCKS_PER_SEC * 1000;
        fprintf(logFile, "732683\t%.2fms\t%d\tcomparações\t%d\ttrocas", timeTaken, countComps, countSwaps);
        fclose(logFile);
    }

    //Liberar a memória alocada para os Pokemons
    for (int i = 0; i < numPokemons; i++)
    {
        freePokemon(listaPokemons[i]);
    }
    free(listaPokemons);

    return 0;
}
