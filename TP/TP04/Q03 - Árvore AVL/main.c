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

typedef struct No_AVL No_AVL;
typedef struct AVL AVL;

// Função para inicializar a árvore AVL
void iniciarAVL(AVL *avl);
// Altura de um nó
int obterAlturaAVL(No_AVL *n);
// Maior altura entre os 2 filhos de um nó
int maiorAlturaSubAVL(No_AVL *n);
// Fator de balanceamento de um nó
int obterBalanceamentoAVL(No_AVL *n);
// Função para balancear e retornar o nó
No_AVL *balancearNoAVL(No_AVL *no);
// Rotação simples para a direita
No_AVL *rotacaoDireitaAVL(No_AVL *atual);
// Rotação simples para a esquerda
No_AVL *rotacaoEsquerdaAVL(No_AVL *atual);
// Rotação esquerda-direita
No_AVL *rotacaoEsquerdaDireitaAVL(No_AVL *no);
// Rotação direita-esquerda
No_AVL *rotacaoDireitaEsquerdaAVL(No_AVL *no);
// Criar um novo nó
No_AVL *novoNoAVL(Pokemon *p);
// Criar e inserir um novo nó
void inserirAVL(AVL *avl, Pokemon *p);
No_AVL *inserirChamadaAVL(No_AVL *no, Pokemon *p);
// Pesquisar Pokémon e imprimir caminho da árvore
void pesquisar(char pokemon[], AVL *pokedex);
void pesquisarAVL(char nomePokemon[], No_AVL *avl);

struct AVL
{
    No_AVL *raiz;
};

struct No_AVL
{
    No_AVL *esquerda;
    No_AVL *direita;
    Pokemon pokemon;
    int altura;
};

void startAVL(AVL *avl)
{
    avl->raiz = NULL;
}

No_AVL *novoNoAVL(Pokemon *p)
{
    No_AVL *tmp = (No_AVL *)malloc(sizeof(No_AVL));

    tmp->esquerda = NULL;
    tmp->direita = NULL;
    tmp->altura = 1;
    tmp->pokemon = *p;

    return tmp;
}

int obterAlturaAVL(No_AVL *n)
{
    return n ? n->altura : 0;
}

int maiorAlturaSubAVL(No_AVL *n)
{
    int l = obterAlturaAVL(n->esquerda);
    int r = obterAlturaAVL(n->direita);
    return l > r ? l : r;
}

int obterBalanceamentoAVL(No_AVL *n)
{
    return n ? obterAlturaAVL(n->esquerda) - obterAlturaAVL(n->direita) : 0;
}

No_AVL *balancearNoAVL(No_AVL *no)
{
    int balanceamento = obterBalanceamentoAVL(no);
    int balanceamentoEsq = obterBalanceamentoAVL(no->esquerda);
    int balanceamentoDir = obterBalanceamentoAVL(no->direita);

    // Desbalanceamento à esquerda (rotação simples à direita)
    if (balanceamento > 1 && balanceamentoEsq >= 0)
        return rotacaoDireitaAVL(no);

    // Desbalanceamento à direita (rotação simples à esquerda)
    if (balanceamento < -1 && balanceamentoDir <= 0)
        return rotacaoEsquerdaAVL(no);

    // Desbalanceamento à esquerda-direita (rotação dupla à esquerda)
    if (balanceamento > 1 && balanceamentoEsq < 0)
        return rotacaoEsquerdaDireitaAVL(no);

    // Desbalanceamento à direita-esquerda (rotação dupla à direita)
    if (balanceamento < -1 && balanceamentoDir > 0)
        return rotacaoDireitaEsquerdaAVL(no);

    // Se o nó estiver balanceado
    return no;
}

No_AVL *rotacaoDireitaAVL(No_AVL *atual)
{
    No_AVL *esquerdaAtual = atual->esquerda;             // O filho à esquerda de "atual"
    No_AVL *direitaEsquerdaAtual = esquerdaAtual->direita; // O filho à direita de "esquerda"

    // Realizando a rotação
    esquerdaAtual->direita = atual;
    atual->esquerda = direitaEsquerdaAtual;

    // Atualizando as alturas
    atual->altura = 1 + maiorAlturaSubAVL(atual);
    esquerdaAtual->altura = 1 + maiorAlturaSubAVL(esquerdaAtual);

    // Retornando o novo nó raiz
    return esquerdaAtual;
}

No_AVL *rotacaoEsquerdaAVL(No_AVL *atual)
{
    No_AVL *direitaAtual = atual->direita;           // O filho à direita de "atual"
    No_AVL *esquerdaDireitaAtual = direitaAtual->esquerda; // O filho à esquerda de "direita"

    // Realizando a rotação
    direitaAtual->esquerda = atual;
    atual->direita = esquerdaDireitaAtual;

    // Atualizando as alturas
    atual->altura = 1 + maiorAlturaSubAVL(atual);
    direitaAtual->altura = 1 + maiorAlturaSubAVL(direitaAtual);

    // Retornando o novo nó raiz
    return direitaAtual;
}

No_AVL *rotacaoEsquerdaDireitaAVL(No_AVL *no)
{
    no->esquerda = rotacaoEsquerdaAVL(no->esquerda);
    return rotacaoDireitaAVL(no);
}

No_AVL *rotacaoDireitaEsquerdaAVL(No_AVL *no)
{
    no->direita = rotacaoDireitaAVL(no->direita);
    return rotacaoEsquerdaAVL(no);
}

void inserirAVL(AVL *avl, Pokemon *p)
{
    avl->raiz = inserirChamadaAVL(avl->raiz, p);
}

No_AVL *inserirChamadaAVL(No_AVL *no, Pokemon *p)
{
    if (!no)
        return novoNoAVL(p); // Cria um novo nó se o nó atual for NULL
    else if (strcmp(p->name, no->pokemon.name) < 0)
        no->esquerda = inserirChamadaAVL(no->esquerda, p);
    else if (strcmp(p->name, no->pokemon.name) > 0)
        no->direita = inserirChamadaAVL(no->direita, p);
    else
        return no; // Não permite duplicatas

    no->altura = 1 + maiorAlturaSubAVL(no);

    return balancearNoAVL(no); // Realiza o balanceamento após a inserção
}

void pesquisar(char pokemonName[], AVL *pokedex)
{
    printf("raiz ");
    pesquisarAvl(pokemonName, pokedex->raiz);
}

void pesquisarAvl(char pokemonName[], No_AVL *no)
{
    if (no == NULL)
    {
        countComps++;
        printf("NAO\n");
    }
    else if (strcmp(pokemonName, no->pokemon.name) == 0)
    {
        countComps += 2;
        printf("SIM\n");
    }
    else if (strcmp(pokemonName, no->pokemon.name) < 0)
    {
        countComps += 3;
        printf("esq ");
        pesquisarAvl(pokemonName, no->esquerda);
    }
    else
    {
        countComps += 4;
        printf("dir ");
        pesquisarAvl(pokemonName, no->direita);
    }
}

void liberarNoAVL(No_AVL *no)
{
    if (no == NULL)
        return;

    // Libera os nós filhos recursivamente
    liberarNoAVL(no->esquerda);
    liberarNoAVL(no->direita);

    // Libera o nó em si
    free(no);
}

void liberarAVL(AVL *avl)
{
    if (avl == NULL)
        return;

    liberarNoAVL(avl->raiz);
    free(avl);
}

int main()
{
    // Variáveis para leitura do arquivo
    FILE *arquivo;
    Pokemon **listaPokemons = NULL;
    int numPokemons = 0;
    char input[MAX_NAME_LEN];

    AVL *pokedex = malloc(sizeof(AVL));
    if (pokedex == NULL)
    {
        printf("Erro ao alocar memória para pokedex.\n");
        return 1;
    }
    startAVL(pokedex);
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

    start = clock();

    scanf("%s", input);
    while (strcmp(input, "FIM") != 0)
    {
        int id = atoi(input);
        Pokemon *pokemonEncontrado = buscarPokemonPorId(listaPokemons, numPokemons, id);
        if (pokemonEncontrado)
        {
            inserirAVL(pokedex, pokemonEncontrado);
        }
        else
        {
            printf("Pokemon não encontrado.\n");
        }
        scanf("%s", input);
    }

    scanf("%s", input);
    while (strcmp(input, "FIM") != 0)
    {
        printf("%s\n", input);
        pesquisar(input, pokedex);
        scanf("%s", input);
    }

    end = clock();

    // Escrever arquivo de log
    FILE *logFile = fopen("732683__avl.txt", "w");
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
    liberarAVL(pokedex);

    return 0;
}