import java.util.Scanner;

// Classe que representa uma célula na pilha, armazenando um dado e o ponteiro para a próxima célula
class Celula {
    public char dado; // Dado armazenado na célula
    public Celula proximo; // Referência para a próxima célula

    // Construtor padrão
    Celula() {
        dado = 0;
        proximo = null;
    }

    // Construtor que inicializa a célula com um valor
    Celula(char x) {
        dado = x;
        proximo = null;
    }
}

// Classe que representa a pilha, uma estrutura de dados do tipo LIFO (Last In, First Out)
class Pilha {
    public Celula topo; // Referência para o topo da pilha

    // Construtor padrão que inicializa a pilha como vazia
    Pilha() {
        topo = null;
    }

    // Construtor que inicializa a pilha com um valor no topo
    Pilha(char x) {
        topo = new Celula(x);
    }

    // Método para adicionar um novo elemento na pilha
    public void adicionar(char x) {
        Celula temp = new Celula(x); // Cria uma nova célula
        temp.proximo = topo; // Aponta a nova célula para o antigo topo
        topo = temp; // Atualiza o topo para a nova célula
    }

    // Método para remover o elemento do topo da pilha
    public char remover() {
        if (topo == null) return '2'; // Retorna '2' para indicar erro ou pilha vazia
        else {
            char backup = topo.dado; // Armazena o dado do topo
            Celula temp = topo; // Guarda a célula atual
            topo = topo.proximo; // Move o topo para a próxima célula
            temp.proximo = null; // Desconecta a célula removida
            return backup; // Retorna o dado removido
        }
    }
}

public class Recursivo {

    // Método que resolve a expressão booleana, usando a pilha
    public static char resolverExpressao(String novaExpressao) {
        Pilha pilha = new Pilha(); // Cria uma nova pilha
        return resolverExpressaoRecursivo(novaExpressao, 0, pilha); // Chama o método recursivo
    }

    // Método recursivo para processar a expressão e manipular a pilha
    private static char resolverExpressaoRecursivo(String novaExpressao, int index, Pilha pilha) {
        if (index >= novaExpressao.length()) {
            return pilha.topo.dado; // Retorna o resultado final da expressão
        }

        char x = novaExpressao.charAt(index); // Lê o caractere atual
        if (x != ')') { // Se não for um parêntese de fechamento
            pilha.adicionar(x); // Adiciona o caractere na pilha
        } else { // Se for um parêntese de fechamento
            String subExpressao = ""; // Inicializa a sub-expressão
            subExpressao = montarSubExpressao(pilha); // Monta a sub-expressão
            char operacao = pilha.remover(); // Remove o operador (e.g., '!', 'a', 'o')
            pilha.adicionar(operar(operacao, subExpressao)); // Realiza a operação e adiciona o resultado na pilha
        }

        return resolverExpressaoRecursivo(novaExpressao, index + 1, pilha); // Chama a recursão para o próximo caractere
    }

    // Método para montar a sub-expressão removendo caracteres da pilha até encontrar um '('
    private static String montarSubExpressao(Pilha pilha) {
        String subExpressao = "";
        while (pilha.topo.dado != '(') {
            subExpressao = pilha.remover() + subExpressao; // Monta a subexpressão
        }
        pilha.remover(); // Remove o '('
        return subExpressao;
    }

    // Método que realiza as operações NOT, AND e OR na sub-expressão
    public static char operar(char operacao, String expr) {
        if (operacao == '!') { // Operação NOT
            char resultado = '1'; // Assume inicialmente que o resultado será '1'
            for (int i = 0; i < expr.length(); i++) {
                if (expr.charAt(i) == '1') { // Se encontrar um '1', o resultado será '0'
                    resultado = '0';
                    break;
                }
            }
            return resultado;
        } else if (operacao == 'a') { // Operação AND
            char resultado = '1'; // Assume inicialmente que o resultado será '1'
            for (int i = 0; i < expr.length(); i++) {
                if (expr.charAt(i) == '0') { // Se encontrar um '0', o resultado será '0'
                    resultado = '0';
                    break;
                }
            }
            return resultado;
        } else { // Operação OR
            char resultado = '0'; // Assume inicialmente que o resultado será '0'
            for (int i = 0; i < expr.length(); i++) {
                if (expr.charAt(i) == '1') { // Se encontrar um '1', o resultado será '1'
                    resultado = '1';
                    break;
                }
            }
            return resultado;
        }
    }

    // Método para ler a expressão de entrada e substituí-la por valores e operadores correspondentes
    public static String ler(Scanner sc, Integer tam) {
        Integer valor1, valor2, valor3 = 0;
        String expressao;
        valor1 = sc.nextInt(); // Lê o valor associado a 'A'
        valor2 = sc.nextInt(); // Lê o valor associado a 'B'
        if (tam == 3) {
            valor3 = sc.nextInt(); // Lê o valor associado a 'C', se existir
        }
        expressao = sc.nextLine(); // Lê a expressão lógica como string

        String novaExpressao = ""; // Inicializa a nova expressão que será processada
        for (int i = 0; i < expressao.length(); i++) {
            if (expressao.charAt(i) != ' ' && expressao.charAt(i) != ',') { // Ignora espaços e vírgulas
                if (expressao.charAt(i) == 'a' && expressao.charAt(i + 1) == 'n' && expressao.charAt(i + 2) == 'd') {
                    novaExpressao += 'a'; // Substitui "and" por 'a' (operador AND)
                    i += 2;
                } else if (expressao.charAt(i) == 'n' && expressao.charAt(i + 1) == 'o' && expressao.charAt(i + 2) == 't') {
                    novaExpressao += '!'; // Substitui "not" por '!' (operador NOT)
                    i += 2;
                } else if (expressao.charAt(i) == 'o' && expressao.charAt(i + 1) == 'r') {
                    novaExpressao += 'o'; // Substitui "or" por 'o' (operador OR)
                    i += 1;
                } else if (expressao.charAt(i) == 'A') {
                    novaExpressao += valor1; // Substitui 'A' pelo seu valor correspondente
                } else if (expressao.charAt(i) == 'B') {
                    novaExpressao += valor2; // Substitui 'B' pelo seu valor correspondente
                } else if (expressao.charAt(i) == 'C') {
                    if (tam == 3) {
                        novaExpressao += valor3; // Substitui 'C' pelo seu valor correspondente
                    }
                } else if (expressao.charAt(i) == '(' || expressao.charAt(i) == ')') {
                    novaExpressao += expressao.charAt(i); // Mantém os parênteses na expressão
                }
            }
        }
        return novaExpressao; // Retorna a expressão substituída
    }

    // Método principal que controla a execução do programa
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Integer tam;

        do {
            tam = sc.nextInt(); // Lê o número de variáveis na expressão (A, B, C)
            if (tam != 0) {
                String novaExpressao = ler(sc, tam); // Converte a expressão de entrada
                char resultadoFinal = resolverExpressao(novaExpressao); // Resolve a expressão lógica
                System.out.println(resultadoFinal); // Imprime o resultado final
            }
        } while (tam != 0); // Continua enquanto o número de variáveis não for 0
        sc.close(); 
    }
}
