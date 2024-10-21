using System;
using System.Collections;
public class Program
{
  public static void Main(string[] args)
  {
    Stack<char> pilha = new Stack<char>();

    Console.WriteLine("Digite 10 caracteres:");

    for (int i = 0; i < 10; i++)
    {
      char c = Console.ReadKey().KeyChar;
      Console.WriteLine();

      if (char.IsUpper(c))
      {
        pilha.Push(c);
      }
      else if (char.IsLower(c))
      {
        if (pilha.Count > 0)
        {
          Console.WriteLine($"O caracter {pilha.Pop()} foi removido da pilha." );
        }
        else
        {
          Console.WriteLine("Pilha vazia, nada para remover.");
        }
      }
      else
      {
        if ( pilha.Count > 0 ){
          Console.WriteLine($"O próximo elemento a ser removido da pilha é {pilha.Peek()}."); 
        }
      }
    }
  }
}