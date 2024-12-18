/* O professor Rolien organizou junto às suas turmas de Ciência da Computação a confecção de uma camiseta polo que fosse ao mesmo tempo bonita e barata. Após algumas conversas, ficou decidido com os alunos que seriam feitas somente camisetas da cor preta, o que facilitaria a confecção. Os alunos poderiam escolher entre o logo do curso e os detalhes em branco ou vermelho. Assim sendo, Rolien precisa de sua ajuda para organizar as listas de quem quer a camiseta em cada uma das turmas, relacionando estas camisetas pela cor do logo do curso, tamanho (P, M ou G) e por último pelo nome.


Entrada
A entrada contém vários casos de teste. Cada caso de teste inicia com um valor N, (1 ≤ N ≤ 60) inteiro e positivo, que indica a quantidade de camisetas a serem feitas para aquela turma. As próximas N*2 linhas contém informações de cada uma das camisetas (serão duas linhas de informação para cada camiseta). A primeira linha irá conter o nome do estudante e a segunda linha irá conter a cor do logo da camiseta ("branco" ou "vermelho") seguido por um espaço e pelo tamanho da camiseta "P" "M" ou "G". A entrada termina quando o valor de N for igual a zero (0) e esta valor não deverá ser processado.


Saída
Para cada caso de entrada deverão ser impressas as informações ordenadas pela cor dos detalhes em ordem ascendente, seguido pelos tamanhos em ordem descendente e por último por ordem ascendente de nome, conforme o exemplo abaixo.

Obs.: Deverá ser impressa uma linha em branco entre dois casos de teste. */

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

  static class Camisas {

    public String cor;
    public String tamanho;
    public String nome;

    public Camisas() {
      this(null, null, null);
    }

    public Camisas(String cor, String tamanho, String nome) {
      this.cor = cor;
      this.tamanho = tamanho;
      this.nome = nome;
    }

  }

  public static void ordenarCamisas(ArrayList<Camisas> listaAlunos) {
    for (int i = 1; i < listaAlunos.size(); i++) {
      Camisas tmp = listaAlunos.get(i);
      int j = i - 1;
      while (j >= 0 && (listaAlunos.get(j).cor.compareTo(tmp.cor) > 0 ||
          (listaAlunos.get(j).cor.equals(tmp.cor) && listaAlunos.get(j).tamanho.compareTo(tmp.tamanho) < 0) ||
          (listaAlunos.get(j).cor.equals(tmp.cor) && listaAlunos.get(j).tamanho.equals(tmp.tamanho)
              && listaAlunos.get(j).nome.compareTo(tmp.nome) > 0))) {
        listaAlunos.set(j + 1, listaAlunos.get(j));
        j--;
      }
      listaAlunos.set(j + 1, tmp);
    }
  }

  public static void imprimirListaCamisas(ArrayList<Camisas> listaAlunos) {
    for (Camisas camisa : listaAlunos) {
      System.out.println(camisa.cor + " " + camisa.tamanho + " " + camisa.nome);
    }
    System.out.println();
  }

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int N = sc.nextInt();
    sc.nextLine();

    while (N != 0) {

      ArrayList<Camisas> lista = new ArrayList<>();

      for (int i = 0; i < N; i++) {
        Camisas aluno = new Camisas();
        String nomeAluno = sc.nextLine();
        aluno.nome = nomeAluno;
        String camisa = sc.nextLine();
        String[] detalhes = camisa.split(" ");
        aluno.cor = detalhes[0];
        aluno.tamanho = detalhes[1];
        lista.add(aluno);

      }

      ordenarCamisas(lista);
      imprimirListaCamisas(lista);

      N = sc.nextInt();
      if (N != 0)
        sc.nextLine();
    }

    sc.close();
  }
}