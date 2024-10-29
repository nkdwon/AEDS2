package Estudos.Árvore.Binária.Java;

// Árvore Binária de Pesquisa
public class ArvoreBinaria {
  
  private No raiz;// Nó raiz da árvore binária

  // Construtor da classe que inicializa a árvore vazia
  public ArvoreBinaria() {
    raiz = null;
  }

  /**
   * Método público de inserção, que chama a função recursiva para inserir o elemento `x`.
   * @param x Elemento a ser inserido na árvore.
   * @throws Exception Caso o elemento já exista na árvore.
   */
  public void inserir(int x) throws Exception {
    raiz = inserir(x, raiz);
  }

  /**
   * Método recursivo de inserção que percorre a árvore até encontrar a posição correta para o novo elemento.
   * @param x Elemento a ser inserido.
   * @param i Nó atual durante a recursão.
   * @return Nó atualizado com o novo elemento.
   * @throws Exception Caso o elemento já exista na árvore.
   */
  private No inserir(int x, No i) throws Exception {
    if (i == null) {
      i = new No(x); // Insere o novo nó na posição correta
    } else if (x < i.elemento) {
      i.esq = inserir(x, i.esq); // Insere no lado esquerdo se `x` for menor que o elemento atual
    } else if (x > i.elemento) {
      i.dir = inserir(x, i.dir); // Insere no lado direito se `x` for maior que o elemento atual
    } else {
      throw new Exception("Erro! Elemento já existe."); // Erro se o elemento já existe na árvore
    }
    return i;
  }

  /**
   * Método público de inserção que utiliza um método auxiliar para manter uma referência ao pai do nó.
   * Útil para operações onde a referência ao nó pai é necessária.
   * @param x Elemento a ser inserido na árvore.
   * @throws Exception Caso o elemento já exista na árvore.
   */
  public void inserirPai(int x) throws Exception {
    if (raiz == null) {
      raiz = new No(x); // Insere na raiz se a árvore estiver vazia
    } else if (x < raiz.elemento) {
      inserirPai(x, raiz.esq, raiz); // Inicia a inserção no lado esquerdo da raiz
    } else if (x > raiz.elemento) {
      inserirPai(x, raiz.dir, raiz); // Inicia a inserção no lado direito da raiz
    } else {
      throw new Exception("Erro ao inserir Pai! Elemento já existe.");
    }
  }

  /**
   * Método recursivo de inserção com referência ao nó pai.
   * @param x Elemento a ser inserido.
   * @param i Nó atual durante a recursão.
   * @param pai Nó pai do nó atual.
   * @throws Exception Caso o elemento já exista na árvore.
   */
  private void inserirPai(int x, No i, No pai) throws Exception {
    if (i == null) {
      // Insere o elemento `x` como filho do nó `pai` na posição correta
      if (x < pai.elemento) {
        pai.esq = new No(x);
      } else {
        pai.dir = new No(x);
      }
    } else if (x < i.elemento) {
      inserirPai(x, i.esq, i); // Insere no lado esquerdo se `x` for menor que o elemento atual
    } else if (x > i.elemento) {
      inserirPai(x, i.dir, i); // Insere no lado direito se `x` for maior que o elemento atual
    } else {
      throw new Exception("Erro ao inserir Pai! Elemento já existe.");
    }
  }
}
