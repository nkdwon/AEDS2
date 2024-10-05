using System;
using System.Collections;

public class Program
{
  public static void Main(string[] args)
  {

    Stack pilha = new Stack();
    pilha.Push(1);
    pilha.Push(2);
    pilha.Push(3);
    pilha.Push(4);
    pilha.Push(5);
    Console.WriteLine("Pilha: ({0}) ", pilha.Count);
    pilha.Pop();
    if (pilha.Contains(1))
    {
      Console.WriteLine("sim");
    }
    else
    {
      Console.WriteLine("não");
    }
    if (pilha.Contains(5))
    {
      Console.WriteLine("sim");
    }
    else
    {
      Console.WriteLine("não");
    }
    Console.WriteLine("Pilha: ({0}) ", pilha.Peek());
    pilha.Push(6);
    pilha.Push(7);
    pilha.Push(8);
    pilha.Push(9);
    pilha.Push(10);
    Console.WriteLine("Pilha: ({0}) / ({1})", pilha.Pop(), pilha.Pop());
    pilha.Clear();
    Console.WriteLine("Pilha: ({0}) ", pilha.Count);

  }
}