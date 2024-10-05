using System;
using System.Collections;

public class Program
{
  public static void Main(string[] args)
  {
    Hashtable h = new Hashtable();
    h.Add(1, "João");
    h.Add(2, "Maria");
    h.Add(3, "José");
    Console.WriteLine("({0}) / ({1})", h.Contains(1), h.Count);
    Console.WriteLine(h.ContainsKey(1));
    Console.WriteLine(h.ContainsValue("João"));
    h.Remove("João");
    Console.WriteLine(h.ContainsValue("João"));

  }
}

/* Hashtable - Estrutura do tipo Dictionary (Par chave/valor)

  1. Primeiro, a Hashtable h, é criada
  2. Depois, os elementos de chave 1, 2 e 3 são inseridos juntos de seus valores João, Maria e José, respectivamente
  3. Em seguida, é imprimido no console se h contém algum objeto com a chave 1 (retornará true, João) e a quantidade de elementos em h.
  4. Depois, é imprimido se h contem a chave 1 (retornará true)
  5. Agora, é verificado se h contém algum objeto com o João (retornará true)
  6. Remove um elemento cuja chave é João de h (não acontece nada, pois o João que está em h é valor )
  7. Verifica e imprime novamente se h contém o valor João (retornará true, pois contém)

 */