using System;
using System.Collections;
public class Program
{
  public static void Main(string[] args)
  {
    Queue<char> fila = new Queue<char>();

    Console.WriteLine("Digite 10 caracteres:");

    for (int i = 0; i < 10; i++)
    {
      char c = Console.ReadKey().KeyChar;
      Console.WriteLine();

      if (char.IsUpper(c))
      {
        fila.Enqueue(c);
      }
      else if (char.IsLower(c))
      {
        if (fila.Count > 0)
        {
          Console.WriteLine($"O caracter {fila.Dequeue()} foi removido da fila.");

        }
        else
        {
          Console.WriteLine("Fila vazia, nada para remover.");
        }
      }
      else
      {
        if ( fila.Count > 0 ){
          Console.WriteLine($"O próximo elemento a ser removido da fila é {fila.Peek()}."); 
        }
      }
    }
  }
}