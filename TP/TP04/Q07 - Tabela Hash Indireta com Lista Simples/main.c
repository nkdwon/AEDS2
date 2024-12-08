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

typedef struct
{
    int id;
    int generation;
    char name[MAX_NAME_LEN];
    char description[MAX_DESC_LEN];
    char *types[MAX_TYPES];        // Array de ponteiros para strings
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

// Estrutura da célula da lista
typedef struct Celula {
    Pokemon pokemon;       // Estrutura que armazena o Pokémon
    struct Celula *prox;   // Ponteiro para a próxima célula da lista
} Celula;

// Estrutura da lista (Pokedex)
typedef struct {
    Celula *primeiro;      // Ponteiro para o primeiro elemento da lista
    Celula *ultimo;        // Ponteiro para o último elemento da lista
    int numPokemonsPokedex; // Contador de elementos na lista
} Lista;

Celula* novaCelula(Pokemon* pokemon) {
    Celula* nova = (Celula*) malloc(sizeof(Celula));
    if (pokemon != NULL) {
        nova->pokemon = *pokemon;  // Copia o Pokémon para a nova célula
    }
    nova->prox = NULL; // Define o próximo como NULL (último elemento)
    return nova;
}

Lista* startLista() {
    Lista* pokedex = (Lista*) malloc(sizeof(Lista));
    if (pokedex) {
        pokedex->primeiro = novaCelula(NULL); // Célula sentinela no início da lista
        pokedex->ultimo = pokedex->primeiro;  // Define o último como a sentinela no início
        pokedex->numPokemonsPokedex = 0;      // Inicializa o contador de Pokémons
    }
    return pokedex;
}


void inserirFim(Lista *pokedex, Pokemon *pokemon) {
    pokedex->ultimo->prox = novaCelula(pokemon); // Liga a última célula ao novo Pokémon
    pokedex->ultimo = pokedex->ultimo->prox;     // Atualiza a última posição para o novo Pokémon
    pokedex->numPokemonsPokedex++;               // Incrementa o contador de Pokémons
}

typedef struct{ 
    Lista** tabela;
    int tamanho; 

} HashIndireta;


HashIndireta* start(int tamTab){
    HashIndireta* hash = (HashIndireta*) malloc(sizeof(HashIndireta));
    hash->tamanho = tamTab;
    hash->tabela = (Lista**) malloc(tamTab * sizeof(Lista*));
    
    for (int i = 0; i < tamTab; i++) {
        hash->tabela[i] = startLista(); // Inicializa cada entrada como uma lista
    }
    return hash;
}

// Função de cálculo do índice hash (ASCII name mod tamTab)
int calcHash(char *pokemonName, int tamTab) {
    int somaASCII = 0;
    for (int i = 0; i < strlen(pokemonName); i++) {
        somaASCII += pokemonName[i]; // Soma os valores ASCII dos caracteres
    }
    return somaASCII % tamTab;
}

// Função para inserir na tabela hash
void inserirHash(HashIndireta *hash, Pokemon *pokemon) {
    int indice = calcHash(pokemon->name, hash->tamanho); // Calcula o índice
    inserirFim(hash->tabela[indice], pokemon);           // Insere no fim da lista do índice
}

// Função para buscar na tabela hash
int buscarHash(HashIndireta *hash, char *pokemonName) {
    int indice = calcHash(pokemonName, hash->tamanho); // Calcula o índice
    Lista *lista = hash->tabela[indice];
    
    for (Celula *atual = lista->primeiro->prox; atual != NULL; atual = atual->prox) {
        countComps++;
        if (strcmp(atual->pokemon.name, pokemonName) == 0) {
           printf("(Posicao: %d) ", indice);
            return 1;
        }
    }
    return 0; // Pokémon não encontrado
}

void liberarTabelaHash(HashIndireta *hash) {
    for (int i = 0; i < hash->tamanho; i++) {
        Lista *lista = hash->tabela[i];
        Celula *atual = lista->primeiro;
        
        // Libera todas as células da lista
        while (atual != NULL) {
            Celula *temp = atual;
            atual = atual->prox;
            free(temp);
        }

        // Libera a lista
        free(lista);
    }

    // Libera a tabela e a estrutura hash
    free(hash->tabela);
    free(hash);
}

int main()
{
    // Variáveis para leitura do arquivo
    FILE *arquivo;
    Pokemon **listaPokemons = NULL;
    int numPokemons = 0;
    char input[MAX_NAME_LEN];

    HashIndireta *pokedex = start(21);
    if (pokedex == NULL)
    {
        printf("Erro ao alocar memória para pokedex.\n");
        return 1;
    }
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
            inserirHash(pokedex, pokemonEncontrado);
        }
        else
        {
            printf("Pokemon não encontrado.\n");
        }
        scanf("%s", input);
    }

    start = clock();
    scanf("%s", input);
    while (strcmp(input, "FIM") != 0)
    {
        printf("=> %s: ", input);
        if(buscarHash(pokedex, input)){
            printf("SIM\n");
        } else{
            printf("NAO\n");
        }
        scanf("%s", input);
    }

    end = clock();

    // Escrever arquivo de log
    FILE *logFile = fopen("732683_hashIndireta.txt", "w");
    if (logFile == NULL)
    {
        printf("Erro ao criar o arquivo de log.\n");
        return 1;
    }
    else
    {
        // Calcula o tempo gasto em milissegundos
        double timeTaken = ((double)(end - start)) / CLOCKS_PER_SEC * 1000;
        fprintf(logFile, "732683\t%.2fms\t%d\tcomparações", timeTaken, countComps);
        fclose(logFile);
    }

    // Liberar a memória alocada para os Pokemons
    for (int i = 0; i < numPokemons; i++)
    {
        freePokemon(listaPokemons[i]);
    }
    free(listaPokemons);
    liberarTabelaHash(pokedex);

    return 0;
}