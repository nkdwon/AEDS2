#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <math.h>
#include <time.h> // Para manipulação de datas

// Variáveis Globais
#define MAX_NAME_LEN 100
#define MAX_DESC_LEN 255
#define MAX_TYPES 2
#define MAX_ABILITIES 100
#define MAX_LINE_LEN 1024
#define MAX_TAM_FILA 5

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

// Estrutura da célula da fila
typedef struct Celula 
{
    Pokemon pokemon;       // Estrutura que armazena o Pokémon
    struct Celula *prox;   // Ponteiro para a próxima célula da fila
} Celula;

// Estrutura da fila (Pokedex)
typedef struct 
{
    Celula *primeiro;      // Ponteiro para o primeiro elemento da fila
    Celula *ultimo;        // Ponteiro para o último elemento da fila
    int numPokemonsPokedex; // Contador de elementos na fila
} Pokedex;

/**
 * Cria uma nova célula com o Pokémon fornecido.
 * @param pokemon Ponteiro para o Pokémon a ser armazenado na nova célula.
 * @return Retorna a nova célula criada.
 */
Celula* novaCelula(Pokemon* pokemon) 
{
    Celula* nova = (Celula*) malloc(sizeof(Celula));
    if (pokemon != NULL) 
    {
        nova->pokemon = *pokemon;  // Copia o Pokémon para a nova célula
    }
    nova->prox = NULL; // Define o próximo como NULL (último elemento)
    return nova;
}

/**
 * Inicializa uma nova fila circular Pokedex.
 * @return Retorna um ponteiro para a nova Pokedex criada.
 */
Pokedex* start() 
{
    Pokedex* pokedex = (Pokedex*) malloc(sizeof(Pokedex));
    if (pokedex) 
    {
        pokedex->primeiro = novaCelula(NULL); // Célula sentinela no início da fila
        pokedex->ultimo = pokedex->primeiro;  // Define o último como a sentinela no início
        pokedex->numPokemonsPokedex = 0;      // Inicializa o contador de Pokémons
    }
    return pokedex;
}

/**
 * Remove o Pokémon da primeira posição da fila e retorna o Pokémon removido.
 * @param pokedex Ponteiro para a fila Pokedex de onde o Pokémon será removido.
 * @return O Pokémon removido.
 */
Pokemon remover(Pokedex *pokedex) 
{
    // Verifica se a fila está vazia
    if (pokedex->primeiro == pokedex->ultimo) 
    {
        printf("Erro ao remover! A fila está vazia.\n");
        exit(1);
    }
    Celula* tmp = pokedex->primeiro->prox;
    pokedex->primeiro->prox = tmp->prox;
    if (tmp == pokedex->ultimo) 
    {
        pokedex->ultimo = pokedex->primeiro;
    }
    Pokemon removido = tmp->pokemon;
    free(tmp);
    pokedex->numPokemonsPokedex--; // Decrementa o contador de Pokémons
    return removido;
}

/**
 * Insere um Pokémon no final da fila circular.
 * Se a fila estiver cheia, remove o primeiro elemento antes de inserir.
 * @param pokedex Ponteiro para a fila Pokedex onde o Pokémon será inserido.
 * @param pokemon Ponteiro para o Pokémon a ser inserido.
 */
void inserir(Pokedex *pokedex, Pokemon *pokemon)
{
    // Se a fila já atingiu o tamanho máximo, removemos o primeiro elemento
    if (pokedex->numPokemonsPokedex == MAX_TAM_FILA) 
    {
        remover(pokedex);
    }
    
    // Inserção no final da fila
    pokedex->ultimo->prox = novaCelula(pokemon); // Liga a última célula ao novo Pokémon
    pokedex->ultimo = pokedex->ultimo->prox;     // Atualiza a última posição para o novo Pokémon
    pokedex->numPokemonsPokedex++;               // Incrementa o contador de Pokémons
}

/**
 * Calcula a média de capture rate de todos os Pokémons na fila flexível.
 * @param pokedex Ponteiro para a estrutura da fila.
 * @return Retorna a média de capture rate arredondada para um valor inteiro.
 */
int mediaCaptureRateFila(Pokedex *pokedex) 
{
    float media = 0;
    Celula *atual = pokedex->primeiro->prox;  // Inicia na primeira célula válida
    
    // Percorre a pokedex até o final
    while (atual != NULL) 
    {
        media += atual->pokemon.captureRate;  // Soma o capture rate
        atual = atual->prox;  // Vai para a próxima célula
    }
    
    media /= pokedex->numPokemonsPokedex;  // Calcula a média
    return (int)round(media);  // Arredonda para o inteiro mais próximo
}

/**
 * Mostra todos os Pokémons armazenados na Pokédex.
 * @param pokedex Estrutura da Pokédex a ser exibida.
 */
void mostrar(Pokedex *pokedex) 
{
    // Itera sobre a fila de Pokémons e imprime cada um
    Celula* i;
    int j = 0;
    for (i = pokedex->primeiro->prox; i != NULL; j++, i = i->prox)
    {
        printf("[%d] ", j); // Exibe o índice do Pokémon
        printPokemon(&i->pokemon); // Chama uma função para imprimir as informações do Pokémon
    }
}

int main()
{
    // Variáveis para leitura do arquivo
    FILE *arquivo;
    Pokemon **listaPokemons = NULL;
    int numPokemons = 0;
    char input[MAX_NAME_LEN];

    Pokedex* pokedex = start();

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
            inserir(pokedex ,pokemonEncontrado);
            printf("Média: %d\n", mediaCaptureRateFila(pokedex));
        }
        else
        {
            printf("Pokemon não encontrado.\n");
        }
        scanf("%s", input);
    }

    int numRegistros = 0; // Variável para armazenar o número de registros a serem lidos
    char registro[10]; // Buffer para armazenar a entrada do usuário
    scanf("%d", &numRegistros); // Lê o número de registros que o usuário deseja inserir/remover

    for(int i = 0; i < numRegistros; i++) // Loop para processar cada registro
    { 
        scanf(" %[^\n]", registro); // Lê a linha de entrada, permitindo espaços

        // Verifica se a operação é de inserção no início
        if (registro[0] == 'I') {
            int id; // Variável para armazenar o ID do Pokémon
            sscanf(registro + 2, "%d", &id); // Lê o ID a partir da string de registro
            Pokemon *pokemon = buscarPokemonPorId(listaPokemons, numPokemons, id); // Busca o Pokémon pelo ID
            if(pokemon) { // Se o Pokémon foi encontrado
                inserir(pokedex, pokemon); // Insere no início da lista
                printf("Média: %d\n", mediaCaptureRateFila(pokedex));
            }
        }
        // Verifica se a operação é de remoção de uma posição específica
        else if (registro[0] == 'R') {
            Pokemon pokemon = remover(pokedex); // Remove o Pokémon da posição especificada
            printf("(R) %s\n", pokemon.name); // Imprime o nome do Pokémon removido
        }
    }

    printf("\n");
    // Exibe todos os Pokémon restantes na pokedex
    mostrar(pokedex);

    //Liberar a memória alocada para os Pokemons
    for (int i = 0; i < numPokemons; i++)
    {
        freePokemon(listaPokemons[i]);
    }
    free(listaPokemons);

    return 0;
}
