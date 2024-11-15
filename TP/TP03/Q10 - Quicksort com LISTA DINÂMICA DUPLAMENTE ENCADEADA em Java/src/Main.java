import java.util.*;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.*;

// CLASSE PARA MEDIR O TEMPO DE EXECUÇÃO
class TimeLog {
    private long startTime;
    private long endTime;

    public TimeLog() {
        this.startTime = 0;
        this.endTime = 0;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    public void end() {
        this.endTime = System.currentTimeMillis();
    }

    public long getTime() {
        return this.endTime - this.startTime;
    }
}

// CLASSE PARA OS CONTADORES
class Contador {
    int comparacoes = 0;
    int swaps = 0;
}

// CLASSE PARA O MÉTODO QUICKSORT
class Quicksort{

    private ListaDupla pokedex;
    private Contador contador;

    /**
     * Construtor para inicializar o Quicksort com a lista de Pokémon e contador.
     *
     * @param pokedex  Lista duplamente encadeada de Pokémon
     * @param contador Objeto para contabilizar comparações e swaps
     */
    public Quicksort(ListaDupla pokedex, Contador contador) {
        this.pokedex = pokedex;
        this.contador = contador;
    }

    /**
     * Método que troca dois Pokémon na lista duplamente encadeada, dado os índices.
     *
     * @param i Índice do primeiro Pokémon
     * @param j Índice do segundo Pokémon
     */
    private void swap(int i, int j) throws Exception {
        if (i == j) return; // Não há necessidade de trocar se os índices forem iguais

        CelulaDupla celulaI = getCelulaAt(i);
        CelulaDupla celulaJ = getCelulaAt(j);

        // Realiza a troca de Pokémon
        Pokemon temp = celulaI.pokemon;
        celulaI.pokemon = celulaJ.pokemon;
        celulaJ.pokemon = temp;

        contador.swaps++;
    }

    /**
     * Retorna a célula na posição indicada.
     *
     * @param index Índice da célula desejada
     * @return Referência à célula
     * @throws Exception Se o índice for inválido
     */
    private CelulaDupla getCelulaAt(int index) throws Exception {
        if (index < 0 || index >= pokedex.numPokemonsPokedex) {
            throw new Exception("Índice fora do limite!");
        }

        CelulaDupla atual = pokedex.primeiro.prox; // Ignora a célula cabeça
        for (int i = 0; i < index; i++) {
            atual = atual.prox;
        }
        return atual;
    }


    /**
     * Compara dois Pokémon com base na geração e no nome.
     *
     * @param p1 Primeiro Pokémon
     * @param p2 Segundo Pokémon
     * @return Valor negativo se p1 < p2, positivo se p1 > p2, zero se iguais
     */
    private int compare(Pokemon p1, Pokemon p2) {
        int geracao = Integer.compare(p1.getGeneration(), p2.getGeneration());
        return geracao != 0 ? geracao : p1.getName().compareTo(p2.getName());
    }

    /**
     * Método para ordenar os Pokémon utilizando Quicksort.
     *
     * @param esq Índice esquerdo do intervalo a ser ordenado
     * @param dir Índice direito do intervalo a ser ordenado
     */
    public void ordenarPorQuickGen(int esq, int dir) throws Exception {
        if (esq < dir) {
            int i = esq, j = dir;

            // Obtém o pivô
            Pokemon pivo = getCelulaAt((esq + dir) / 2).pokemon;

            while (i <= j) {
                while (compare(getCelulaAt(i).pokemon, pivo) < 0) {
                    contador.comparacoes++;
                    i++;
                }
                while (compare(getCelulaAt(j).pokemon, pivo) > 0) {
                    contador.comparacoes++;
                    j--;
                }
                if (i <= j) {
                    swap(i, j);
                    i++;
                    j--;
                }
            }

            // Chama recursivamente para os subintervalos
            if (esq < j) ordenarPorQuickGen(esq, j);
            if (i < dir) ordenarPorQuickGen(i, dir);
        }
    }

    /**
     * Método para imprimir os Pokémon ordenados da lista.
     */
    public void imprimirPokemonsOrdenados() {
        CelulaDupla atual = pokedex.primeiro.prox; // Começa pela primeira célula válida
        while (atual != null) {
            System.out.println(atual.pokemon);
            atual = atual.prox;
        }
    }
}

class CelulaDupla {
    public Pokemon pokemon; // Objeto Pokemon armazenado na célula
    public CelulaDupla prox;     // Referência para a próxima célula na lista
    public CelulaDupla ant;      // Referência para a célula anterior na lista

    // Construtor que inicializa a célula com um Pokémon
    public CelulaDupla(Pokemon pokemon) {
        this.pokemon = pokemon;
        this.ant = this.prox = null;
    }
}

class ListaDupla {
    public CelulaDupla primeiro; // Primeira célula da lista (cabeça)
    public CelulaDupla ultimo;   // Última célula da lista
    public int numPokemonsPokedex; // Número total de Pokémon na lista

    // Construtor: Inicializa a lista com uma célula cabeça (sem Pokémon)
    public ListaDupla() {
        primeiro = new CelulaDupla(null); // Célula inicial vazia
        ultimo = primeiro;
        numPokemonsPokedex = 0; // Lista começa vazia
    }

    // Insere um elemento no início da lista
    public void inserirInicio(Pokemon pokemon) throws Exception {
        CelulaDupla tmp = new CelulaDupla(pokemon); // Nova célula com o Pokémon
        tmp.ant = primeiro; // Ajusta ponteiro anterior
        tmp.prox = primeiro.prox; // Ajusta ponteiro próximo
        primeiro.prox = tmp; // Atualiza o próximo da célula cabeça
        if (primeiro == ultimo) { 
            ultimo = tmp; // Atualiza último caso seja o primeiro elemento
        } else {
            tmp.prox.ant = tmp; // Ajusta o anterior da célula seguinte
        }
        numPokemonsPokedex++; 
    }

    // Insere um elemento no final da lista
    public void inserirFim(Pokemon pokemon) throws Exception {
        ultimo.prox = new CelulaDupla(pokemon); // Cria nova célula após o último
        ultimo.prox.ant = ultimo; // Ajusta ponteiro anterior da nova célula
        ultimo = ultimo.prox; // Atualiza o último para a nova célula
        numPokemonsPokedex++; 
    }

    // Insere um elemento em uma posição específica
    public void inserir(Pokemon pokemon, int pos) throws Exception {
        if (pos < 0 || pos > numPokemonsPokedex) { 
            throw new Exception("Erro ao inserir: posição inválida!");
        } else if (pos == 0) {
            inserirInicio(pokemon); // Insere no início
        } else if (pos == numPokemonsPokedex) {
            inserirFim(pokemon); // Insere no final
        } else {
            CelulaDupla i = primeiro;
            for (int j = 0; j < pos; j++, i = i.prox); // Percorre até a posição
            CelulaDupla tmp = new CelulaDupla(pokemon);
            tmp.ant = i; 
            tmp.prox = i.prox; 
            tmp.ant.prox = tmp.prox.ant = tmp; // Ajusta ponteiros
            numPokemonsPokedex++; 
        }
    }

    // Remove o elemento da primeira posição
    public Pokemon removerInicio() throws Exception {
        if (primeiro == ultimo) {
            throw new Exception("Erro ao remover: lista vazia!");
        }
        CelulaDupla tmp = primeiro; // Temporário para remover
        primeiro = primeiro.prox; // Avança o início
        Pokemon resp = primeiro.pokemon; // Obtém o Pokémon a ser removido
        tmp.prox = primeiro.ant = null; // Ajusta ponteiros para evitar lixo
        numPokemonsPokedex--; 
        return resp;
    }

    // Remove o último elemento da lista
    public Pokemon removerFim() throws Exception {
        if (primeiro == ultimo) {
            throw new Exception("Erro ao remover: lista vazia!");
        }
        Pokemon resp = ultimo.pokemon; // Obtém o Pokémon a ser removido
        ultimo = ultimo.ant; // Retrocede o último
        ultimo.prox.ant = null; 
        ultimo.prox = null; // Ajusta ponteiros para evitar lixo
        numPokemonsPokedex--; 
        return resp;
    }

    // Remove um elemento em uma posição específica
    public Pokemon remover(int pos) throws Exception {
        if (primeiro == ultimo) {
            throw new Exception("Erro ao remover: lista vazia!");
        } else if (pos < 0 || pos >= numPokemonsPokedex) {
            throw new Exception("Erro ao remover: posição inválida!");
        } else if (pos == 0) {
            return removerInicio(); // Remove do início
        } else if (pos == numPokemonsPokedex - 1) {
            return removerFim(); // Remove do final
        } else {
            CelulaDupla i = primeiro.prox;
            for (int j = 0; j < pos; j++, i = i.prox); // Percorre até a posição
            i.ant.prox = i.prox; 
            i.prox.ant = i.ant; // Ajusta ponteiros para remoção
            Pokemon resp = i.pokemon; // Obtém o Pokémon removido
            i.prox = i.ant = null; // Ajusta ponteiros para evitar lixo
            numPokemonsPokedex--; 
            return resp;
        }
    }

    // Exibe todos os elementos da lista
    public void mostrar() {
        int j = 0;
        for (CelulaDupla i = primeiro.prox; i != null; j++, i = i.prox) {
            System.out.println("[" + j + "] " + i.pokemon); // Exibe posição e Pokémon
        }
    }
}

public class Main {

    public static void main(String[] args) throws Exception {

        String caminhoCsv = "/tmp/pokemon.csv"; // Caminho do arquivo CSV

        Scanner scanner = new Scanner(System.in);
        String entrada;
        ListaDupla pokedex = new ListaDupla();

        while (true) {
            entrada = scanner.nextLine();

            if (entrada.equals("FIM")) {
                break;
            }

            // Verifica se a entrada contém apenas números
            if (entrada.matches("\\d+")) { // Verifica se a string contém apenas dígitos
                try {
                    int id = Integer.parseInt(entrada); // Converte para inteiro

                    Pokemon pokemonEncontrado = Pokemon.buscarPokemonPorId(caminhoCsv, id);

                    if (pokemonEncontrado != null) {
                        pokedex.inserirFim(pokemonEncontrado);
                    } else {
                        System.out.println("Pokémon com ID " + id + " não encontrado.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Erro ao converter a entrada para número inteiro.");
                    e.printStackTrace(); // Depuração: Imprimir o stack trace
                } catch (Exception e) {
                    System.out.println("Erro ao buscar o Pokémon.");
                    e.printStackTrace(); // Depuração: Imprimir o stack trace de qualquer outro erro
                }
            } else {
                System.out.println("Entrada inválida! Por favor, insira um número inteiro ou 'FIM'.");
            }
        }

        TimeLog timeLog = new TimeLog();
        Contador contador = new Contador();
        Quicksort quick = new Quicksort(pokedex, contador);

        // Mede o tempo de execução ao ordenar
        timeLog.start();
        quick.ordenarPorQuickGen(0, pokedex.numPokemonsPokedex - 1);
        timeLog.end();

        // Chama o método para imprimir os Pokémons ordenados
        quick.imprimirPokemonsOrdenados();

        scanner.close();

        // Criação do arquivo de log
        String matricula = "732683";
        String logContent = matricula + "\t" + timeLog.getTime() + "ms\t" + contador.comparacoes + " comparações\t"
                + contador.swaps + " trocas.";

        // Defina o caminho para a pasta tmp ou a pasta que deseja salvar o arquivo de
        // log
        // String logPath = "../tmp/" + matricula + "_sequencial.txt";

        try (FileWriter logWriter = new FileWriter(matricula + "_quicksort3.txt")) {
            logWriter.write(logContent);
        } catch (IOException e) {
            System.out.println("Erro ao criar o arquivo de log.");
            e.printStackTrace();
        }

    }
}

class Pokemon {

    private int id;
    private int generation;
    private String name;
    private String description;
    private List<String> types;
    private List<String> abilities;
    private double weight;
    private double height;
    private int captureRate;
    private boolean isLegendary;
    private Date captureDate;

    // CONSTRUTOR DEFAULT
    public Pokemon() {
        this(0, 0, "", "", new ArrayList<>(), new ArrayList<>(), 0.0, 0.0, 0, false, null);
    }

    // CONSTRUTOR COM PARÂMETROS
    Pokemon(int id, int generation, String name, String description, List<String> types, List<String> abilities,
            double weight, double height, int captureRate, boolean isLegendary, Date captureDate) {

        setId(id);
        setGeneration(generation);
        setName(name);
        setDescription(description);
        setTypes(types);
        setAbilities(abilities);
        setWeight(weight);
        setHeight(height);
        setCaptureRate(captureRate);
        setLegendary(isLegendary);
        setCaptureDate(captureDate);

    }

    /* ------------------------------------------------------------ */

    // MÉTODOS GETTERS AND SETTERS PARA OS ATRIBUTOS

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name.isEmpty() ? "" : name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description.isEmpty() ? "" : description;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types == null || types.isEmpty() ? List.of() : types;
    }

    public List<String> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<String> abilities) {
        this.abilities = abilities == null || abilities.isEmpty() ? List.of() : abilities;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getCaptureRate() {
        return captureRate;
    }

    public void setCaptureRate(int captureRate) {
        this.captureRate = captureRate;
    }

    public boolean isLegendary() {
        return isLegendary;
    }

    public void setLegendary(boolean isLegendary) {
        this.isLegendary = isLegendary;
    }

    public Date getCaptureDate() {
        return captureDate;
    }

    public void setCaptureDate(Date captureDate) {
        this.captureDate = captureDate;
    }

    // DATA FORMATADA
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    /* --------------------------------------------------------- */

    // MÉTODOS CLONE, IMPRIMIR E LER

    // CLONE
    public Pokemon clone() {
        Pokemon clone = new Pokemon();
        clone.setId(this.id);
        clone.setGeneration(this.generation);
        clone.setName(this.name);
        clone.setDescription(this.description);
        clone.setTypes(this.types);
        clone.setAbilities(this.abilities);
        clone.setWeight(this.weight);
        clone.setHeight(this.height);
        clone.setCaptureRate(this.captureRate);
        clone.setLegendary(this.isLegendary);
        return clone;
    }

    // LER 1 POKÉMON DO CSV
    public void lerLinhaCsv(String linha) {
        // Verifica se a linha não está vazia
        if (linha == null || linha.isEmpty()) {
            throw new IllegalArgumentException("A linha do CSV não pode ser vazia.");
        }

        List<String> valores = new ArrayList<>();
        StringBuilder campoAtual = new StringBuilder();
        int colcheteNivel = 0; // Para rastrear o nível de colchetes

        for (char c : linha.toCharArray()) {
            if (c == '[') {
                colcheteNivel++; // Entramos em uma lista
            } else if (c == ']') {
                colcheteNivel--; // Saímos de uma lista
            }

            if (c == ',' && colcheteNivel == 0) {
                // Se estamos fora de colchetes, adicionamos o campo atual à lista
                valores.add(campoAtual.toString().trim());
                campoAtual.setLength(0); // Limpa o StringBuilder
            } else {
                campoAtual.append(c); // Adiciona o caractere ao campo atual
            }
        }

        // Adiciona o último campo
        if (campoAtual.length() > 0) {
            valores.add(campoAtual.toString().trim());
        }

        // A partir daqui, pegamos os valores da lista e atribuímos aos atributos do
        // Pokémon
        try {
            this.setId(Integer.parseInt(valores.get(0))); // ID no índice 0
            this.setGeneration(Integer.parseInt(valores.get(1))); // Geração no índice 1
            this.setName(valores.get(2).isEmpty() ? "0" : valores.get(2)); // Nome no índice 2
            this.setDescription(valores.get(3).isEmpty() ? "0" : valores.get(3)); // Descrição no índice 3

            // Processa tipos e habilidades
            this.setTypes(List.of(valores.get(4), valores.get(5))); // Tipos no índice 4 e 5
            this.setAbilities(limparListaCsv(valores.get(6))); // Habilidades no índice 6

            this.setWeight(valores.get(7).isEmpty() ? 0.0 : Double.parseDouble(valores.get(7))); // Peso no índice 7
            this.setHeight(valores.get(8).isEmpty() ? 0.0 : Double.parseDouble(valores.get(8))); // Altura no índice 8
            this.setCaptureRate(valores.get(9).isEmpty() ? 0 : Integer.parseInt(valores.get(9))); // Taxa de captura no
                                                                                                  // índice 9
            this.setLegendary(valores.get(10).isEmpty() ? false : Integer.parseInt(valores.get(10)) != 0); // Lendário
                                                                                                           // no índice
                                                                                                           // 10
            this.setCaptureDate(dateFormat.parse(valores.get(11).isEmpty() ? "01/01/1970" : valores.get(11))); // Data
                                                                                                               // de
                                                                                                               // captura
                                                                                                               // no
                                                                                                               // índice
                                                                                                               // 11

        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter a entrada para número inteiro.");
            e.printStackTrace();
        } catch (ParseException e) {
            System.err.println("Erro ao converter a data.");
            e.printStackTrace();
        }
    }

    // MÉTODO PARA LIMPAR A LISTA/FORMATAR DE HABILIDADES
    private List<String> limparListaCsv(String abilitiesCsv) {
        // Remove colchetes e aspas duplas, e separa as habilidades
        return Arrays.stream(abilitiesCsv.replaceAll("[\\[\\]\"]", "").split(",")) // Remove colchetes e aspas duplas
                .map(String::trim) // Remove espaços em branco
                .collect(Collectors.toList()); // Coleta as habilidades em uma lista
    }

    // MÉTODO PARA BUSCAR POKÉMON PELO ID
    public static Pokemon buscarPokemonPorId(String caminhoCsv, int idProcurado) throws Exception {
        Scanner scanner = new Scanner(new File(caminhoCsv));
        String linha;

        scanner.nextLine(); // Ignora a primeira linha do csv, que é o cabeçalho

        while (scanner.hasNextLine()) {
            linha = scanner.nextLine();
            Pokemon pokemon = new Pokemon(); // Cria um objeto temporário para cada linha

            // Lê a linha e transforma em um objeto Pokémon
            pokemon.lerLinhaCsv(linha);

            // Verifica se o ID do Pokémon corresponde ao ID procurado
            if (pokemon.getId() == idProcurado) {
                scanner.close();
                return pokemon; // Retorna o Pokémon encontrado
            }
        }

        scanner.close();
        return null; // Retorna null se não encontrar o Pokémon com o ID fornecido
    }

    // FORMATAR A IMPRESSÃO
    @Override
    public String toString() {

        // Converte os tipos(types) para a formatação desejada
        String formattedTypes = types.stream()
                .filter(type -> !type.isEmpty()) // Remove tipos vazios
                .map(type -> "'" + type + "'") // Coloca cada tipo entre aspas simples
                .collect(Collectors.joining(", ")); // Junta os tipos com vírgula

        // Mantém os colchetes em torno de tipos e habilidades
        return "[#" + id + " -> " + name + ": " + description +
                " - [" + formattedTypes + "] - " + abilities + " - " +
                weight + "kg - " + height + "m - " +
                captureRate + "% - " + isLegendary +
                " - " + generation + " gen] - " + dateFormat.format(captureDate);
    }
}
