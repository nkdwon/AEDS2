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

// MÉTODO SWAP
void swap(Pokemon *a, Pokemon *b) {
    Pokemon temp = *a;
    *a = *b;
    *b = temp;
}

// Método para comparar dois Pokémons (primeiro pela altura, e pelo nome em caso de empate)
int comparePokemon(Pokemon *a, Pokemon *b) {
    if (a->height != b->height) {
        return (a->height > b->height) - (a->height < b->height); // Comparação pela altura
    }
    return strcmp(a->name, b->name); // Desempate pelo nome
}

// Verifica se o nó tem dois filhos e retorna o maior
int getMaiorFilho(int i, int tamHeap, Pokemon pokedex[]) {
    int filhoEsquerda = 2 * i + 1;
    int filhoDireita = 2 * i + 2;
    if (filhoDireita > tamHeap || comparePokemon(&pokedex[filhoEsquerda], &pokedex[filhoDireita]) > 0) {
        return filhoEsquerda; // Filho da esquerda é o maior
    } else {
        return filhoDireita; // Filho da direita é o maior
    }
}

// Método para construir o heap a partir do índice tamHeap
void construir(int tamHeap, Pokemon pokedex[]) {
    // Enquanto o filho for maior que o pai, troque e suba o elemento
    for (int i = tamHeap; i > 0 && comparePokemon(&pokedex[i], &pokedex[(i - 1) / 2]) > 0; i = (i - 1)/ 2) {
        countComps++;
        countSwaps++;
        swap(&pokedex[i], &pokedex[(i - 1) / 2]); // Troca o filho com o pai
    }
}

// Método para reconstruir o heap após uma troca
void reconstruir(int tamHeap, Pokemon pokedex[]) {
    int i = 0;
    // Enquanto houver filhos
    while (2 * i + 1 <= tamHeap) {
        int filho = getMaiorFilho(i, tamHeap, pokedex); // Pega o maior filho
        countComps++;
        // Se o pai for menor que o maior filho
        if (comparePokemon(&pokedex[i], &pokedex[filho]) < 0) {
            countSwaps++;;
            swap(&pokedex[i], &pokedex[filho]);
            i = filho; // Continua para a nova posição do elemento
        } else {
            i = tamHeap; // Sai do loop se a ordem estiver correta
        }
    }
}

// MÉTODO PARA ORDENAR OS POKEMON POR HEIGHT COM HEAPSORT PARCIAL
void ordernarPorHeapParcial(Pokemon pokedex[], int numPokemonsPokedex, int k) {

    // Construção do heap a partir do índice 0 até o k-1
    for (int tamHeap = 1; tamHeap < k; tamHeap++) {
        construir(tamHeap, pokedex);
    }
    // Para cada um dos (n-k) demais elementos, se ele for
    // menor que o do raiz, troque-o com o raiz
    // e reorganize o heap
    for(int i = k; i < numPokemonsPokedex; i++){
        if(comparePokemon(&pokedex[i], &pokedex[0]) < 0){
            countSwaps++;;
            swap(&pokedex[i], &pokedex[0]);
            reconstruir(k - 1, pokedex); // k-1 porque estamos trabalhando com índices baseados em zero (heap 0-based)
        }
    }  

    // Ordenação propriamente dita (ordernação final)
    int tamHeap = k -1;
    while (tamHeap > 0) {
        countSwaps++;
        swap(&pokedex[0], &pokedex[tamHeap--]); // Troca o maior elemento (raiz) com o último e diminui o heap
        reconstruir(tamHeap, pokedex); // Reconstrói o heap para manter a estrutura
    }
}

int main()
{
    // Variáveis para leitura do arquivo
    FILE *arquivo;
    Pokemon **listaPokemons = NULL;
    int numPokemons = 0;
    char input[MAX_NAME_LEN];

    // Variáveis para ordenar e imprimir os Pokémons presentes em outra lista
    int numPokemonsPokedex = 0;
    Pokemon *pokedex = malloc((802) * sizeof(Pokemon));
    clock_t start, end;

    // Diretório arquivo CSV
    arquivo = fopen("/tmp/pokemon.csv", "r");
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
            pokedex[numPokemonsPokedex] = *pokemonEncontrado; 
            numPokemonsPokedex++;
        }
        else
        {
            printf("Pokemon não encontrado.\n");
        }
        scanf("%s", input);
    }

    pokedex = realloc(pokedex, (numPokemonsPokedex + 1) * sizeof(Pokemon));

    // Variável para definir o tamanho da ordenação parcial
    int k = 10; 
    // Medir o tempo de execução da ordenação
    start = clock();
    ordernarPorHeapParcial(pokedex, numPokemonsPokedex, k);
    for(int i = 0; i < k; i++){
        printPokemon(&pokedex[i]);
    }
    end = clock();

    // Escrever arquivo de log
    FILE *logFile = fopen("732683_heapsort_parcial.txt", "w");
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
    free(pokedex);

    return 0;
}
