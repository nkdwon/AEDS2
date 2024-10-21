// Exercício 01
// Explique em Português o que acontece no programa abaixo


using System;
using System.Collections;

public class Program
{
  public static void Main(string[] args)
  {
    Queue f = new Queue();
    f.Enqueue(1); f.Enqueue(2); f.Enqueue(3);
    Console.Write("Fila: ({0}) / ({1}) / ({2})\n", f.Contains(1), f.Count, f.Peek());
    Console.WriteLine(f.Dequeue());
    Console.Write("Fila: ({0}) / ({1}) / ({2})\n", f.Contains(1), f.Count, f.Peek());

    Stack p = new Stack();
    p.Push(1); p.Push(2); p.Push(3);
    Console.Write("Pilha: ({0}) / ({1}) / ({2})\n", p.Contains(1), p.Count, p.Peek());
    Console.WriteLine(p.Pop());
    Console.Write("Pilha: ({0}) / ({1}) / ({2})\n", p.Contains(1), p.Count, p.Peek());
  }

  /* FILA ( QUEUE ) - FIFO ( First in, First out )
	1. Primeiro a fila f está sendo criada 
	2. Depois, com o Enqueue, estão sendo adicionados os números 1, 2 e 3 na fila f
	3. Depois printa se a fila f contém o elemento 1 ( retorna true ), depois quantos elementos tem ( 3 ), e o próximo elemento a ser removido, sem remove-lo ( o 1, pois foi o primeiro a entrar, então será o primeiro a sair )
	4. Remove um elemento e retorna ele ( será o 1, já que foi o primeiro a entrar )
	5. Novamente, vai printar se a fila contém o número 1 ( agora retornará false, pois o 1 foi removido ), depois quantos elementos tem na fila ( 2 ), e o próximo a ser removido, sem remove-lo ( será o numero 2)
	
	Pilha ( Stack ) - FILO ( First in, Last out )
	1. Primeiro a pilha p está sendo criada
	2. Depois o push está sendo utilizado 3 vezes para adicionar 3 eleme
	3. Depois printa se a pilha p contém o elemento 1 ( retorna true ), depois quantos elementos tem ( 3 ), e o próximo elemento a ser removido, sem remove-lo ( o 3, pois foi o ultimo a entrar, então será o primeiro a sair )
	4. Remove um elemento e retorna ele ( será o 3, já que foi o ultimo a entrar )
	5. Novamente, vai printar se a pilha contém o número 1 ( agora retornará true, pois o 1 continua na lista ), depois quantos elementos tem na fila ( 2 ), e o próximo a ser removido, sem remove-lo ( será o numero 2)

	
	
	*/

}