import java.util.*;
import java.util.stream.Collectors;
import java.io.File;
import java.text.*;

class Lista {
    private Pokemon[] pokedex;
    private int n;

    public Lista() {
        this(6);
    }

    public Lista(int tamanho) {
        pokedex = new Pokemon[tamanho];
        n = 0;
    }

    // Insere um elemento no final da lista
    public void inserirInicio(Pokemon pokemon) throws Exception {
        if (n >= pokedex.length) {
            throw new Exception("Erro ao inserir, a lista já está cheia!");
        }

        for (int i = n; i > 0; i--) {
            pokedex[i] = pokedex[i - 1];
        }

        pokedex[0] = pokemon;
        n++;
    }

    // Insere um elemento no final da lista
    public void inserirFim(Pokemon pokemon) throws Exception {
        if (n >= pokedex.length) {
            throw new Exception("Erro ao inserir, a lista já está cheia!");
        }

        pokedex[n] = pokemon;
        n++;
    }

    // Insere um elemento em uma posição específica da lista, movendo elementos para
    // a direita
    public void inserir(Pokemon pokemon, int pos) throws Exception {
        if (n >= pokedex.length || pos < 0 || pos > n) {
            throw new Exception("Erro ao inserir, posição inválida ou lista cheia!");
        }

        for (int i = n; i > pos; i--) {
            pokedex[i] = pokedex[i - 1];
        }

        pokedex[pos] = pokemon;
        n++;
    }

    // Remove o elemento da primeira posição, deslocando os outros para a esquerda
    public Pokemon removerInicio() throws Exception {
        if (n == 0) {
            throw new Exception("Erro ao remover, a lista está vazia!");
        }

        Pokemon resp = pokedex[0];
        n--;

        for (int i = 0; i < n; i++) {
            pokedex[i] = pokedex[i + 1];
        }

        return resp;
    }

    // Remove o último elemento da lista, reduzindo `n`
    public Pokemon removerFim() throws Exception {
        if (n == 0) {
            throw new Exception("Erro ao remover, a lista está vazia!");
        }

        return pokedex[--n];
    }

    // Remove um elemento em uma posição específica da lista, movendo elementos para
    // a direita
    public Pokemon remover(int pos) throws Exception {
        if (n == 0 || pos < 0 || pos >= n) {
            throw new Exception("Erro ao remover, posição inválida ou lista vazia!");
        }

        Pokemon resp = pokedex[pos];
        n--;

        for (int i = pos; i < n; i++) {
            pokedex[i] = pokedex[i + 1];
        }

        return resp;
    }

    // Exibe todos os elementos da lista entre colchetes
    public void mostrar() {
        for (int i = 0; i < n; i++) {
            System.out.println("[" + i + "] " + pokedex[i]);
        }
    }

}

public class Main {

    public static void main(String[] args) throws Exception {

        String caminhoCsv = "/tmp/pokemon.csv"; // Caminho do arquivo CSV

        Scanner scanner = new Scanner(System.in);
        String entrada;
        Lista pokedex = new Lista(802);
        int numRegistros;

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

        numRegistros = scanner.nextInt();
        String registro;

        for (int i = 0; i <= numRegistros; i++) {
            registro = scanner.nextLine();

            if (registro.isEmpty()) {
                continue; 
            }
            
            if (registro.charAt(0) == 'I' && registro.charAt(1) == 'I') {
                int id = Integer.parseInt(registro.split(" ")[1]);
                Pokemon pokemon = Pokemon.buscarPokemonPorId(caminhoCsv, id);
                pokedex.inserirInicio(pokemon);
            } else if (registro.charAt(0) == 'I' && registro.charAt(1) == 'F') {
                int id = Integer.parseInt(registro.split(" ")[1]);
                Pokemon pokemon = Pokemon.buscarPokemonPorId(caminhoCsv, id);
                pokedex.inserirFim(pokemon);
            } else if (registro.charAt(0) == 'I' && registro.charAt(1) == '*') {
                int pos = Integer.parseInt(registro.split(" ")[1]);
                int id = Integer.parseInt(registro.split(" ")[2]);
                Pokemon pokemon = Pokemon.buscarPokemonPorId(caminhoCsv, id);
                pokedex.inserir(pokemon, pos);
            } else if (registro.charAt(0) == 'R' && registro.charAt(1) == 'I') {
                Pokemon pokemon = pokedex.removerInicio();
                System.out.println("(R) " + pokemon.getName());
            } else if (registro.charAt(0) == 'R' && registro.charAt(1) == 'F') {
                Pokemon pokemon = pokedex.removerFim();
                System.out.println("(R) " + pokemon.getName());
            } else if (registro.charAt(0) == 'R' && registro.charAt(1) == '*') {
                int pos = Integer.parseInt(registro.split(" ")[1]);
                Pokemon pokemon = pokedex.remover(pos);
                System.out.println("(R) " + pokemon.getName());
            }
        }

        pokedex.mostrar();

        scanner.close();
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
