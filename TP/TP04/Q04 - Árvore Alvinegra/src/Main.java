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
}

class NoAN {
    public boolean cor; // true = vermelho, false = preto
    public Pokemon pokemon;
    public NoAN esq, dir;

    public NoAN(Pokemon pokemon) 
    {
        this(pokemon, false, null, null);
    }

    public NoAN(Pokemon pokemon, boolean cor) 
    {
        this(pokemon, cor, null, null);
    }

    public NoAN(Pokemon pokemon, boolean cor, NoAN esq, NoAN dir) 
    {
        this.cor = cor; // Cor do nó (vermelho ou preto)
        this.pokemon = pokemon; // Elemento do nó
        this.esq = esq; // Referência ao filho esquerdo 
        this.dir = dir; // Referência ao filho direito
    }
}

class Alvinegra {
    private NoAN raiz; // Raiz da árvore

    public Alvinegra() 
    {
        raiz = null; // Inicia a árvore vazia
    }

    public void pesquisar(String pokemon, Contador contador) 
    {
        System.out.print("raiz ");
        pesquisar(pokemon, raiz, contador);
    }

    private void pesquisar(String pokemon, NoAN i, Contador contador) 
    {
        if (i == null) 
        {
            contador.comparacoes++;
            System.out.println("NAO"); // Pokémon não encontrado
        } 
        else if (pokemon.equals(i.pokemon.getName())) 
        {
            contador.comparacoes += 2;
            System.out.println("SIM"); // Pokémon encontrado
        } 
        else if (pokemon.compareTo(i.pokemon.getName()) > 0) 
        {
            contador.comparacoes += 3;
            System.out.print("dir "); // Caminha para a direita
            pesquisar(pokemon, i.dir, contador);
        } 
        else 
        {
            contador.comparacoes += 4;
            System.out.print("esq "); // Caminha para a esquerda
            pesquisar(pokemon, i.esq, contador);
        }
    }

    /**
     * Método público para inserir um Pokémon na árvore.
     *
     * @param pokemon Pokémon a ser inserido.
     * @throws Exception Caso o Pokémon já exista na árvore.
     */
    public void inserir(Pokemon pokemon) throws Exception {
        if (raiz == null) 
        {
            raiz = new NoAN(pokemon, false); // Insere o primeiro Pokémon como raiz preta
            //System.out.println("Árvore vazia. Agora, raiz(" + pokemon.getName() + ").");
        } 
        else 
        {
            inserir(pokemon, null, null, null, raiz); // Insere recursivamente
        }
        raiz.cor = false; // A raiz sempre deve ser preta.
    }

    /**
     * Metodo privado recursivo para inserir elemento.
     * 
     * @param pokemon Elemento a ser inserido.
     * @param avo      NoAN em analise.
     * @param pai      NoAN em analise.
     * @param i        NoAN em analise.
     * @throws Exception Se o elemento existir.
     */
    private void inserir(Pokemon pokemon, NoAN bisavo, NoAN avo, NoAN pai, NoAN i) throws Exception 
    {
        if (i == null) // Caso base: insere o novo elemento
        {
            if (pokemon.getName().compareTo(pai.pokemon.getName()) < 0) 
            {
                i = pai.esq = new NoAN(pokemon, true); // Insere como filho esquerdo
            } 
            else 
            {
                i = pai.dir = new NoAN(pokemon, true);  // Insere como filho direito
            }
            if (pai.cor == true) 
            {
                balancear(bisavo, avo, pai, i); // Realiza balanceamento se necessário
            }
        } 
        else 
        {
            // Verifica a presença de 4-nó e faz o balanceamento, se necessário (nós com dois filhos vermelhos)
            if (i.esq != null && i.dir != null && i.esq.cor == true && i.dir.cor == true) 
            {
                i.cor = true; // Muda a cor do nó para vermelho
                i.esq.cor = i.dir.cor = false; // Filhos ficam pretos
                if (i == raiz) 
                {
                    i.cor = false; // A raiz permanece preta
                } 
                else if (pai.cor == true) 
                {
                    balancear(bisavo, avo, pai, i); // Balanceia se necessário
                }
            }

            // Caminha pela árvore para encontrar a posição correta
            if (pokemon.getName().compareTo(i.pokemon.getName()) < 0) 
            {
                inserir(pokemon, avo, pai, i, i.esq); // Caminha para a esquerda
            } 
            else if (pokemon.getName().compareTo(i.pokemon.getName()) > 0) 
            {
                inserir(pokemon, avo, pai, i, i.dir); // Caminha para a direita
            } else 
            {
                throw new Exception("Erro inserir (elemento repetido)!"); 
            }

        }
    }

    private void balancear(NoAN bisavo, NoAN avo, NoAN pai, NoAN i) 
    {
        // Se o pai também é vermelho, reequilibrar a arvore, rotacionando o avô
        if (pai.cor == true) 
        {
            // 4 tipos de reequilibrios e acoplamento
            if (pai.pokemon.getName().compareTo(avo.pokemon.getName()) > 0) 
            {   // Rotação esquerda ou direita-esquerda
                if (i.pokemon.getName().compareTo(pai.pokemon.getName()) > 0) 
                {
                    avo = rotacaoEsq(avo); // Rotação simples à esquerda.
                } 
                else 
                {
                    avo = rotacaoDirEsq(avo); // Rotação dupla direita-esquerda.
                }
            } 
            else 
            {
                // Rotação direita ou esquerda-direita.
                if (i.pokemon.getName().compareTo(pai.pokemon.getName()) < 0)
                {
                    avo = rotacaoDir(avo); // Rotação simples à direita.
                } 
                else 
                {
                    avo = rotacaoEsqDir(avo); // Rotação dupla esquerda-direita.
                }
            }
            if (bisavo == null) 
            {
                raiz = avo; // Atualiza a raiz, se necessário.
            } 
            else if (avo.pokemon.getName().compareTo(bisavo.pokemon.getName()) < 0) 
            {
                bisavo.esq = avo; // Atualiza o filho esquerdo do bisavô.
            } 
            else 
            {
                bisavo.dir = avo; // Atualiza o filho direito do bisavô.
            }
            // Ajusta cores após a rotação
            avo.cor = false;
            avo.esq.cor = avo.dir.cor = true;

            // System.out.println("Reestabeler cores: avo(" + avo.elemento + "->branco) e
            // avo.esq / avo.dir("+ avo.esq.elemento + "," + avo.dir.elemento + "->
            // pretos)");
        } // if(pai.cor == true)
    }

    private NoAN rotacaoDir(NoAN no) 
    {
        // System.out.println("Rotacao DIR(" + no.pokemon + ")");
        NoAN noEsq = no.esq;
        NoAN noEsqDir = noEsq.dir;

        noEsq.dir = no; // Ajusta referências.
        no.esq = noEsqDir;

        return noEsq; // Novo nó raiz após a rotação.
    }

    private NoAN rotacaoEsq(NoAN no) 
    {
        // System.out.println("Rotacao ESQ(" + no.pokemon + ")");
        NoAN noDir = no.dir;
        NoAN noDirEsq = noDir.esq;

        noDir.esq = no; // Ajusta referências.
        no.dir = noDirEsq;

        return noDir; // Novo nó raiz após a rotação.
    }

    private NoAN rotacaoDirEsq(NoAN no) 
    {
        no.dir = rotacaoDir(no.dir); // Primeira rotação à direita.
        return rotacaoEsq(no); // Rotação à esquerda final.
    }

    private NoAN rotacaoEsqDir(NoAN no) 
    {
        no.esq = rotacaoEsq(no.esq); // Primeira rotação à esquerda.
        return rotacaoDir(no); // Rotação à direita final.
    }
}

public class Main {

    public static void main(String[] args) throws Exception {

        String caminhoCsv = "/tmp/pokemon.csv"; // Caminho do arquivo CSV
        
        Scanner scanner = new Scanner(System.in);
        String entrada;
        Alvinegra pokedex = new Alvinegra();
        TimeLog timeLog = new TimeLog();
        Contador contador = new Contador();

        // Mede o tempo de execução ao ordenar
        timeLog.start();

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
                        pokedex.inserir(pokemonEncontrado);
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

        entrada = scanner.nextLine();
        while (!entrada.equals("FIM")) {

            System.out.println(entrada);
            pokedex.pesquisar(entrada, contador);
            entrada = scanner.nextLine();
        }

        timeLog.end();
        scanner.close();

        // Criação do arquivo de log
        String matricula = "732683";
        String logContent = matricula + "\t" + timeLog.getTime() + "ms\t" + contador.comparacoes + " comparações\t";

        // Defina o caminho para a pasta tmp ou a pasta que deseja salvar o arquivo de
        // log
        // String logPath = "../tmp/" + matricula + "_sequencial.txt";

        try (FileWriter logWriter = new FileWriter(matricula + "_alvinegra.txt")) {
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
