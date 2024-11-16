import java.util.Scanner;

// Classe que representa cada célula de uma matriz
class Celula {
  public int elemento; // Valor armazenado na célula
  public Celula inf, sup, esq, dir; // Ponteiros para as células vizinhas

  // Construtor padrão, inicializa a célula com 0 e sem vizinhos
  public Celula() {
    this(0);
  }

  // Construtor com um valor para a célula
  public Celula(int elemento) {
    this(elemento, null, null, null, null);
  }

  // Construtor com um valor e ponteiros para vizinhos
  public Celula(int elemento, Celula inf, Celula sup, Celula esq, Celula dir) {
    this.elemento = elemento;
    this.inf = inf;
    this.sup = sup;
    this.esq = esq;
    this.dir = dir;
  }
}

// Classe que representa uma matriz usando uma estrutura de células
class Matriz {
  private Celula inicio; // Primeira célula da matriz
  private int linha, coluna; // Dimensões da matriz

  // Construtor que inicializa uma matriz com o número de linhas e colunas
  public Matriz(int linha, int coluna) {
    this.linha = linha;
    this.coluna = coluna;

    // Criando a primeira linha da matriz
    inicio = criarLinha(coluna);
    Celula cursor = inicio;

    // Criando as demais linhas e ligando-as
    for (int i = 1; i < linha; i++) {
      Celula novaLinha = criarLinha(coluna); // Cria nova linha
      Celula aux = novaLinha;
      for (Celula temp = cursor; temp != null; temp = temp.dir) {
        temp.inf = aux; // Conecta a célula abaixo
        aux.sup = temp; // Conecta a célula acima
        aux = aux.dir; // Vai para a próxima célula na nova linha
      }
      cursor = cursor.inf; // Avança para a próxima linha
    }
  }

  // Método privado que cria uma linha com 'n' células
  private Celula criarLinha(int n) {
    Celula inicio = new Celula(); // Primeira célula da linha
    Celula atual = inicio;

    // Criando as outras células da linha e conectando-as
    for (int i = 1; i < n; i++) {
      Celula nova = new Celula(); // Nova célula
      atual.dir = nova; // Conecta a célula à direita
      nova.esq = atual; // Conecta a célula à esquerda
      atual = nova; // Atualiza a célula atual
    }
    return inicio; // Retorna a primeira célula da linha
  }

  // Preenche a matriz com os valores passados
  public void preencher(int[][] valores) {
    Celula linhaAtual = inicio;
    for (int i = 0; i < valores.length; i++) {
      Celula colunaAtual = linhaAtual;
      for (int j = 0; j < valores[i].length; j++) {
        colunaAtual.elemento = valores[i][j]; // Preenche o valor da célula
        colunaAtual = colunaAtual.dir; // Avança para a próxima célula na linha
      }
      linhaAtual = linhaAtual.inf; // Avança para a próxima linha
    }
  }

  // Exibe a diagonal principal da matriz
  public void mostrarDiagonalPrincipal() {
    Celula atual = inicio;
    while (atual != null) {
      System.out.print(atual.elemento + " "); // Imprime o valor da célula
      atual = (atual.inf != null) ? atual.inf.dir : null; // Avança para a próxima célula na diagonal principal
    }
    System.out.println(); // Quebra a linha após imprimir a diagonal
  }

  // Exibe a diagonal secundária da matriz
  public void mostrarDiagonalSecundaria() {
    Celula atual = inicio;
    // Vai até a última célula da primeira linha
    while (atual.dir != null) {
      atual = atual.dir;
    }
    // Imprime a diagonal secundária
    while (atual != null) {
      System.out.print(atual.elemento + " ");
      atual = (atual.inf != null) ? atual.inf.esq : null; // Avança para a próxima célula na diagonal secundária
    }
    System.out.println(); // Quebra a linha após imprimir a diagonal
  }

  // Soma duas matrizes e retorna a matriz resultado
  public Matriz soma(Matriz outra) {
    Matriz resultado = new Matriz(linha, coluna); // Cria a matriz resultado
    Celula linhaA = inicio, linhaB = outra.inicio, linhaRes = resultado.inicio;

    while (linhaA != null && linhaB != null) {
      Celula colunaA = linhaA, colunaB = linhaB, colunaRes = linhaRes;
      while (colunaA != null && colunaB != null) {
        colunaRes.elemento = colunaA.elemento + colunaB.elemento; // Soma os elementos
        colunaA = colunaA.dir; // Avança para a próxima célula na linha
        colunaB = colunaB.dir;
        colunaRes = colunaRes.dir;
      }
      linhaA = linhaA.inf; // Avança para a próxima linha
      linhaB = linhaB.inf;
      linhaRes = linhaRes.inf;
    }
    return resultado; // Retorna a matriz de soma
  }

  // Multiplica duas matrizes e retorna a matriz resultado
  public Matriz multiplicacao(Matriz outra) {
    Matriz resultado = new Matriz(linha, outra.coluna); // Cria a matriz resultado

    // Percorre cada célula da matriz de resultado
    for (int i = 0; i < linha; i++) {
      for (int j = 0; j < outra.coluna; j++) {
        int soma = 0;
        // Calcula o valor da célula na posição [i][j]
        for (int k = 0; k < coluna; k++) {
          soma += this.get(i, k) * outra.get(k, j); // Soma dos produtos
        }
        resultado.set(i, j, soma); // Atribui o valor à célula
      }
    }
    return resultado; // Retorna a matriz de multiplicação
  }

  // Retorna o valor da célula na posição [linha][coluna]
  public int get(int linha, int coluna) {
    Celula atual = inicio;
    for (int i = 0; i < linha; i++) {
      atual = atual.inf; // Avança para a linha desejada
    }
    for (int j = 0; j < coluna; j++) {
      atual = atual.dir; // Avança para a coluna desejada
    }
    return atual.elemento; // Retorna o valor da célula
  }

  // Atribui um valor à célula na posição [linha][coluna]
  public void set(int linha, int coluna, int valor) {
    Celula atual = inicio;
    for (int i = 0; i < linha; i++) {
      atual = atual.inf; // Avança para a linha desejada
    }
    for (int j = 0; j < coluna; j++) {
      atual = atual.dir; // Avança para a coluna desejada
    }
    atual.elemento = valor; // Atribui o valor à célula
  }

  // Imprime a matriz
  public void imprimir() {
    Celula linhaAtual = inicio;
    while (linhaAtual != null) {
      Celula colunaAtual = linhaAtual;
      while (colunaAtual != null) {
        System.out.print(colunaAtual.elemento + " "); // Imprime o valor da célula
        colunaAtual = colunaAtual.dir; // Avança para a próxima célula
      }
      System.out.println(); // Quebra a linha
      linhaAtual = linhaAtual.inf; // Avança para a próxima linha
    }
  }

}

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int t = sc.nextInt(); // Número de casos de teste

    // Para cada caso de teste
    for (int i = 0; i < t; i++) {

      // Leitura da primeira matriz
      int l1 = sc.nextInt();
      int c1 = sc.nextInt();
      int[][] mat1 = new int[l1][c1];

      for (int j = 0; j < l1; j++) {
        for (int k = 0; k < c1; k++) {
          mat1[j][k] = sc.nextInt(); // Preenche a primeira matriz
        }
      }

      // Leitura da segunda matriz
      int l2 = sc.nextInt();
      int c2 = sc.nextInt();
      int[][] mat2 = new int[l2][c2];

      for (int j = 0; j < l2; j++) {
        for (int k = 0; k < c2; k++) {
          mat2[j][k] = sc.nextInt(); // Preenche a segunda matriz
        }
      }

      // Criando e preenchendo as matrizes a partir dos dados de entrada
      Matriz m1 = new Matriz(l1, c1);
      m1.preencher(mat1);
      Matriz m2 = new Matriz(l2, c2);
      m2.preencher(mat2);

      // Exibindo as diagonais da primeira matriz
      m1.mostrarDiagonalPrincipal();
      m1.mostrarDiagonalSecundaria();

      // Exibindo a matriz resultante da soma
      Matriz soma = m1.soma(m2);
      soma.imprimir();

      // Exibindo a matriz resultante da multiplicação
      Matriz multiplicacao = m1.multiplicacao(m2);
      multiplicacao.imprimir();
    }

    sc.close();
  }
}