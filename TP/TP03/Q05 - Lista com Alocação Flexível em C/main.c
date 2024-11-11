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

typedef struct {
    Pokemon pokemon;
    struct Celula *prox;
} Celula;


typedef struct {
    Celula *primeiro;   
    Celula *ultimo;
    int numPokemonsPokedex;
} Pokedex;

Pokedex* start(){
    Pokedex* pokedex = (Pokedex*) malloc(sizeof(Pokedex));
    if(pokedex){
        pokedex->primeiro = novaCelula(NULL);
        pokedex->ultimo = pokedex->primeiro;
        pokedex->numPokemonsPokedex = 0;
    }
}

Celula* novaCelula(Pokemon* pokemon){
    Celula* nova = (Celula*) malloc(sizeof(Celula));
    nova->pokemon = *pokemon;
    nova->prox = NULL;
    return nova;
}

/**
 * Insere um Pokémon na primeira posição da lista e move os demais elementos para o fim.
 * @param pokedex Array de Pokémons onde será realizada a inserção.
 * @param pokemon Ponteiro para o Pokémon a ser inserido.
 */
void inserirInicio(Pokedex *pokedex ,Pokemon *pokemon) {

    Celula* tmp = novaCelula(pokemon);
    tmp->prox = pokedex->primeiro->prox;
    pokedex->primeiro->prox = tmp;
    if(pokedex->primeiro == pokedex->ultimo){
        pokedex->ultimo = tmp;
    }
    tmp = NULL;
    
    pokedex->numPokemonsPokedex++; // Aumenta o contador de Pokémons  
}   

/**
 * Insere um Pokémon na última posição da lista.
 * @param pokedex Array de Pokémons onde será realizada a inserção.
 * @param pokemon Ponteiro para o Pokémon a ser inserido.
 */
void inserirFim(Pokedex *pokedex,Pokemon *pokemon) {

    // Insere o novo Pokémon na última posição disponível
    pokedex->ultimo->prox = novaCelula(pokemon);
    pokedex->ultimo = pokedex->ultimo->prox;
    pokedex->numPokemonsPokedex++; // Aumenta o contador de Pokémons
}


/**
 * Insere um Pokémon em uma posição específica e move os demais elementos para o fim.
 * @param pokedex Array de Pokémons onde será realizada a inserção.
 * @param pokemon Ponteiro para o Pokémon a ser inserido.
 * @param pos Posição onde o Pokémon será inserido.
 */
void inserir(Pokedex *pokedex ,Pokemon *pokemon, int pos) {

   // Validar se a inserção é possível
    if (pos < 0 || pos > pokedex->numPokemonsPokedex) {
        printf("Erro ao inserir!"); // Mensagem de erro se a lista está cheia ou a posição é inválida
        exit(1); // Encerra o programa em caso de erro
    }else if(pos == 0){
        inserirInicio(pokedex ,pokemon);
    } else if(pos == pokedex->numPokemonsPokedex){
        inserirFim(pokedex, pokemon);
    }else{
        Celula* i = pokedex->primeiro;
        for(int j = 0; j < pos; j++, i = i->prox);
            Celula* tmp =  novaCelula(pokemon);
            tmp->prox = i->prox;
            i->prox = tmp;  
            i = tmp = NULL; 
    }
    pokedex->numPokemonsPokedex++; // Aumenta o con  tador de Pokémons
}


/**
 * Remove um Pokémon da primeira posição da lista e movimenta os demais elementos para o início.
 * @param pokedex Array de Pokémons de onde será removido o Pokémon.
 * @return O Pokémon que foi removido.
 */
Pokemon removerInicio(Pokedex *pokedex) {

   // Validar se a remoção é possível
    if (pokedex->primeiro == pokedex->ultimo) {
        printf("Erro ao remover!"); // Mensagem de erro se a lista está vazia
        exit(1); // Encerra o programa em caso de erro
    }

    Celula* tmp = pokedex->primeiro;
    pokedex->primeiro = pokedex->primeiro->prox;
    Pokemon resp = pokedex->primeiro->pokemon;
    tmp->prox = NULL;
    free(tmp);
    tmp = NULL;
    pokedex->numPokemonsPokedex--; // Diminui o contador de Pokémons

    return resp; // Retorna o Pokémon removido
}


/**
 * Remove um Pokémon da última posição da lista.
 * @param pokedex Array de Pokémons de onde será removido o Pokémon.
 * @return O Pokémon que foi removido.
 */
Pokemon removerFim(Pokedex *pokedex) {

   // Validar se a remoção é possível
    if (pokedex->primeiro == pokedex->ultimo) {
        printf("Erro ao remover!"); // Mensagem de erro se a lista está vazia
        exit(1); // Encerra o programa em caso de erro
    }

    Celula* i;
    for(i = pokedex->primeiro; i->prox != pokedex->ultimo; i = i->prox);

    Pokemon resp = pokedex->ultimo->pokemon;
    pokedex->ultimo = i;
    free(pokedex->ultimo->prox);
    i = pokedex->ultimo->prox = NULL;
    pokedex->numPokemonsPokedex--;

    return resp; // Diminui o contador de Pokémons e retorna o Pokémon da última posição
}


/**
 * Remove um Pokémon de uma posição específica da lista e movimenta os demais elementos para o início.
 * @param pokedex Array de Pokémons de onde será removido o Pokémon.
 * @param pos Posição de remoção.
 * @return O Pokémon que foi removido.
 */
Pokemon remover(Pokedex *pokedex ,int pos) {
    
    Pokemon resp;

    if(pokedex->primeiro == pokedex->ultimo){
        printf("Erro ao remover!"); // Mensagem de erro se a lista está vazia
        exit(1); // Encerra o programa em caso de erro
    } else if(pos < 0 || pos >= pokedex->numPokemonsPokedex){
        printf("Erro ao remover!"); // Mensagem de erro se a lista está vazia
        exit(1); // Encerra o programa em caso de erro
    } else if (pos == pokedex->primeiro){
        resp = removerInicio(pokedex);
    } else if (pos == pokedex->ultimo){
        resp = removerFim(pokedex);
    } else {

        Celula* i = pokedex->primeiro;

        for(int j = 0; j < pos; j++, i = i->prox);
        Celula* tmp = i ->prox;
        resp = tmp->pokemon;
        i->prox = tmp->prox;
        tmp->prox = NULL;
        free(tmp);
        i = tmp = NULL;
    }
    pokedex->numPokemonsPokedex--;
    return resp; // Retorna o Pokémon removido
}


/**
 * Mostra todos os Pokémons armazenados na Pokédex.
 * @param pokedex Array de Pokémons a ser exibido.
 */
void mostrar(Pokedex *pokedex) {
    // Itera sobre a lista de Pokémons e imprime cada um
    Celula* i;
    int j = 0;
    for (i = pokedex->primeiro->prox; i != NULL;j++, i = i->prox) {
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
    arquivo = fopen("C:/Users/732683/Atividades/AEDS2/TP/TP03/pokemon.csv", "r");
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
            inserirFim(pokedex ,pokemonEncontrado);
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
        if (registro[0] == 'I' && registro[1] == 'I') {
            int id; // Variável para armazenar o ID do Pokémon
            sscanf(registro + 3, "%d", &id); // Lê o ID a partir da string de registro
            Pokemon *pokemon = buscarPokemonPorId(listaPokemons, numPokemons, id); // Busca o Pokémon pelo ID
            if(pokemon) { // Se o Pokémon foi encontrado
                inserirInicio(pokedex, pokemon); // Insere no início da lista
            }
        }
        // Verifica se a operação é de inserção no fim
        else if (registro[0] == 'I' && registro[1] == 'F') {
            int id; // Variável para armazenar o ID do Pokémon
            sscanf(registro + 3, "%d", &id); // Lê o ID a partir da string de registro
            Pokemon *pokemon = buscarPokemonPorId(listaPokemons, numPokemons, id); // Busca o Pokémon pelo ID
            if(pokemon) { // Se o Pokémon foi encontrado
                inserirFim(pokedex, pokemon); // Insere no fim da lista
            }
        }
        // Verifica se a operação é de inserção em uma posição específica
        else if (registro[0] == 'I' && registro[1] == '*') {
            int pos, id; // Variáveis para armazenar a posição e o ID do Pokémon
            sscanf(registro + 3, "%d %d", &pos, &id); // Lê a posição e o ID
            Pokemon *pokemon = buscarPokemonPorId(listaPokemons, numPokemons, id); // Busca o Pokémon pelo ID
            if(pokemon) { // Se o Pokémon foi encontrado
                inserir(pokedex, pokemon, pos); // Insere na posição especificada
            }
        }
        // Verifica se a operação é de remoção do início
        else if (registro[0] == 'R' && registro[1] == 'I') {
            Pokemon pokemon = removerInicio(pokedex); // Remove o Pokémon do início
            printf("(R) %s\n", pokemon.name); // Imprime o nome do Pokémon removido
        }
        // Verifica se a operação é de remoção do fim
        else if (registro[0] == 'R' && registro[1] == 'F') {
            Pokemon pokemon = removerFim(pokedex); // Remove o Pokémon do fim
            printf("(R) %s\n", pokemon.name); // Imprime o nome do Pokémon removido
        }
        // Verifica se a operação é de remoção de uma posição específica
        else if (registro[0] == 'R' && registro[1] == '*') {
            int pos; // Variável para armazenar a posição
            sscanf(registro + 3, "%d", &pos); // Lê a posição
            Pokemon pokemon = remover(pokedex, pos); // Remove o Pokémon da posição especificada
            printf("(R) %s\n", pokemon.name); // Imprime o nome do Pokémon removido
        }
    }

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
