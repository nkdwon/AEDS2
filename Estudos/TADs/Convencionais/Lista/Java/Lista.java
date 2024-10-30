package Estudos.TADs.Convencionais.Lista.Java;

public class Lista {
   // Array que representa a lista de elementos
  private int[] lista;
  // Número atual de elementos na lista
  private int n;

  // Construtor padrão que define um tamanho padrão (por exemplo, 6)
  public Lista() {
    this(6);
  }

  // Construtor da Classe que inicializa o array com o tamanho especificado e define `n` como 0
  public Lista(int tamanho) {
    lista = new int[tamanho];
    n = 0;
  }

  // Insere um elemento no início da lista movendo os demais para a direita
  public void inserirInicio(int x) throws Exception {
    if (n >= lista.length) {
      throw new Exception("Erro ao inserir, a lista já está cheia!");
    }

    // Move todos os elementos para a direita, liberando a posição 0
    for (int i = n; i > 0; i--) {
      lista[i] = lista[i - 1];
    }

    // Insere o elemento `x` na primeira posição e incrementa `n`
    lista[0] = x;
    n++;
  }
  
  // Insere um elemento no final da lista
  public void inserirFim(int x) throws Exception {
    if (n >= lista.length) {
      throw new Exception("Erro ao inserir, a lista já está cheia!");
    }
    
    // Adiciona o elemento `x` na posição `n` e incrementa `n`
    lista[n] = x;
    n++;
  }
  
  // Insere um elemento em uma posição específica da lista, movendo elementos para a direita
  // Só é possível se a lista já tiver tido algum elemento inserido (por qualquer um dos 2 métodos anteriores)
  public void inserir(int x, int pos) throws Exception {
    if (n >= lista.length || pos < 0 || pos > n) {
      throw new Exception("Erro ao inserir, posição inválida ou lista cheia!");
    }

    // Move os elementos para a direita para abrir espaço na posição `pos`
    for (int i = n; i > pos; i--) {
        lista[i] = lista[i - 1];
    }

    // Insere o elemento `x` na posição especificada e incrementa `n`
    lista[pos] = x;
    n++;
  }

  // Remove o elemento da primeira posição, deslocando os outros para a esquerda
  public int removerInicio() throws Exception {
    if (n == 0) {
      throw new Exception("Erro ao remover, a lista está vazia!");
    }

    // Armazena o elemento a ser removido
    int resp = lista[0];
    n--;

    // Move todos os elementos para a esquerda
    for (int i = 0; i < n; i++) {
      lista[i] = lista[i + 1];
    }

    return resp;
  }

  // Remove o último elemento da lista, reduzindo `n`
  public int removerFim() throws Exception {
    if (n == 0) {
      throw new Exception("Erro ao remover, a lista está vazia!");
    }

    // Decrementa `n` e retorna o elemento removido
    return lista[--n];
  }

   // Remove o elemento na posição especificada e move os elementos restantes para a esquerda
  public int remover(int pos) throws Exception {
    if (n == 0 || pos < 0 || pos >= n) {
      throw new Exception("Erro ao remover, posição inválida ou lista vazia!");
    }

    // Armazena o elemento a ser removido
    int resp = lista[pos];
    n--;

    // Move os elementos para a esquerda a partir da posição `pos`
    for (int i = pos; i < n; i++) {
      lista[i] = lista[i + 1];
    }

    return resp;
  }

  // Exibe todos os elementos da lista entre colchetes
  public void mostrar(){
    System.out.print("[ ");
    for (int i = 0; i < n; i++) {
      System.out.print(lista[i] + " ");
    }
    System.out.println("]");
  }

  // Verifica se o elemento `x` está presente na lista, retornando true ou false
  public boolean pesquisar(int x) {
    for (int i = 0; i < n; i++) {
      if (lista[i] == x) {
        return true;
      }
    }
    return false;
  }
}
