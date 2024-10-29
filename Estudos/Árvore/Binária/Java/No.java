package Estudos.Árvore.Binária.Java;

/**
 * Classe que representa um nó em uma árvore binária.
 * Cada nó possui um valor inteiro e referências para subárvores à esquerda e à direita.
 */
public class No {
  public int elemento; // Valor armazenado no nó.
  public No esq; // Referência para o nó filho à esquerda.
  public No dir; // Referência para o nó filho à direita.

  /**
   * Construtor que inicializa o nó com um valor e sem filhos.
   * @param elemento Valor que será armazenado no nó.
   */
  public No(int elemento){
    this(elemento, null, null);
  }

  /**
   * Construtor que inicializa o nó com um valor e referências para subárvores.
   * @param elemento Valor que será armazenado no nó.
   * @param esq Referência para o nó filho à esquerda.
   * @param dir Referência para o nó filho à direita.
   */
  public No(int elemento, No esq, No dir){
    this.elemento = elemento;
    this.esq = esq;
    this.dir = dir;
  }

}

