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

// MÉTODO DE ORDENAÇÃO COUNTING SORT
class CountingSort {

    /** MÉTODO SWAP
    * @param pokedex array de pokemons
    * @param i       indice i
    * @param menor   indice menor
    * @return maior elemento
    */
    static void swap(ArrayList<Pokemon> pokedex, int i, int menor) {
        Pokemon tmp = pokedex.get(i);
        pokedex.set(i, pokedex.get(menor));
        pokedex.set(menor, tmp);
    }

    /** MÉTODO PARA ORDERNAR OS POKÉMONS POR CAPTURE RATE USANDO COUNTING SORT
    * 
    * @param pokedex  array de pokemons
    * @param contador objeto contador
    */
    void ordernarPorCaptureRate(ArrayList<Pokemon> pokedex, Contador contador) {
        int n = pokedex.size();

        // Encontrar o maior capture rate - Custo O(n)
        int maiorCaptureRate = getMaior(pokedex);

        // Array para contar o número de ocorrências de cada capture rate
        // Custo de inicialização O(k), onde k é o maior capture rate
        int[] count = new int[maiorCaptureRate + 1];
        ArrayList<ArrayList<Pokemon>> buckets = new ArrayList<>(maiorCaptureRate + 1);

        // Inicializar o array de contagem e criar buckets para cada capture rate
        // Custo O(k), onde k é o maior capture rate
        for (int i = 0; i < count.length; i++) {
            count[i] = 0;
            buckets.add(new ArrayList<>());  // Cada bucket será uma lista de Pokémons com a mesma capture rate
        }

        // Contar a ocorrência de cada capture rate e armazenar nos buckets correspondentes
         // Custo O(n), onde n é o número total de Pokémons
        for (int i = 0; i < n; i++) {
            contador.comparacoes++;
            Pokemon p = pokedex.get(i);
            int captureRate = p.getCaptureRate();
            count[captureRate]++;
            buckets.get(captureRate).add(p);  // Adicionar o Pokémon ao bucket correspondente
        }

        // Agora, o count[i] contem o numero de elemento menores ou iguais a i
        // para obter a posição correta de cada elemento no array ordenado
        // Custo O(k), para ajustar o array de contagem
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i - 1];
        }

        // Para manter a estabilidade e desempatar por nome, ordenamos os buckets individualmente por nome
        // Ordenação interna dos buckets com custo O(m log m), onde m é o número de elementos no bucket
        // No pior caso (se todos tiverem o mesmo capture rate), temos O(n log n)
        for (int i = 0; i <= maiorCaptureRate; i++) {
            buckets.get(i).sort(Comparator.comparing(Pokemon::getName));  // Ordenar por nome no bucket
        }

        // Criação do array de saída
        // Custo O(n) para inicializar o array ordenado
        ArrayList<Pokemon> ordenado = new ArrayList<>(n);

        // Inicializar o array ordenado com null para evitar o IndexOutOfBoundsException
        for (int i = 0; i < n; i++) {
            ordenado.add(null);
        }

        // Construir o array ordenado (mantendo a estabilidade)
        // Preenchendo o array de saída com os elementos dos buckets
         // Custo O(n), já que estamos percorrendo todos os buckets
        for (int i = maiorCaptureRate; i >= 0; i--) {
            ArrayList<Pokemon> bucket = buckets.get(i); // Bucket com Pokémons de mesmo capture rate
            for (int j = bucket.size() - 1; j >= 0; j--) { // Iterar sobre o bucket em ordem reversa (decrescente)
                Pokemon p = bucket.get(j); // Pegar o Pokémon da posição j do bucket (decrescente)
                int captureRate = p.getCaptureRate(); // Taxa de captura do Pokémon atual

                // Determinar a posição correta no array ordenado, de acordo com o array de contagem
                int pos = count[captureRate] - 1;
                ordenado.set(pos, p); // Colocar o Pokémon na posição correta do array ordenado
                count[captureRate]--; // Decrementar o contador para o capture rate correspondente
            }
        } 
        
        // Copiar o array ordenado de volta para a pokedex
         // Custo O(n), já que percorremos todos os elementos para sobrescrever a lista original
        for (int i = 0; i < n; i++) {
            pokedex.set(i, ordenado.get(i));
        }

    }

    /** MÉTODO PARA OBTER O MAIOR CAPTURE RATE
    * @param pokedex array de pokemons
    * @return maior elemento
    */
    public int getMaior(ArrayList<Pokemon> pokedex) {
         // Custo O(n), onde n é o tamanho da lista
        int maior = pokedex.get(0).getCaptureRate();

        for (int i = 0; i < pokedex.size(); i++) {
            if (maior < pokedex.get(i).getCaptureRate()) {
                maior = pokedex.get(i).getCaptureRate();
            }
        }
        return maior;
    }
}

public class Main {

    // MÉTODO PARA IMPRIMIR OS POKÉMONS ORDENADOS E COM IDS PASSADOS
    static void imprimirPokemonsOrdenados(ArrayList<Pokemon> pokedex) {
        for (int i = 0; i < pokedex.size(); i++) {
            System.out.println(pokedex.get(i));
        }
    }

    public static void main(String[] args) throws Exception {

        String caminhoCsv = "/tmp/pokemon.csv"; // Caminho do arquivo CSV

        Scanner scanner = new Scanner(System.in);
        String entrada;
        ArrayList<Pokemon> pokedex = new ArrayList<Pokemon>();

        // Log de tempo de execução
        TimeLog timeLog = new TimeLog();

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
                        pokedex.add(pokemonEncontrado);
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

        Contador contador = new Contador();
        CountingSort counting = new CountingSort();

        // Mede o tempo de execução ao ordenar
        timeLog.start();
        counting.ordernarPorCaptureRate(pokedex, contador);
        timeLog.end();

        // Chama o método para imprimir os Pokémons ordenados
        imprimirPokemonsOrdenados(pokedex);

        scanner.close();

        // Criação do arquivo de log
        String matricula = "732683";
        String logContent = matricula + "\t" + timeLog.getTime() + "ms\t" + contador.comparacoes + " comparações\t"
                + contador.swaps + " trocas.";

        // Defina o caminho para a pasta tmp ou a pasta que deseja salvar o arquivo de log
        // String logPath = "../tmp/" + matricula + "_sequencial.txt";

        try (FileWriter logWriter = new FileWriter(matricula + "_countingsort.txt")) {
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