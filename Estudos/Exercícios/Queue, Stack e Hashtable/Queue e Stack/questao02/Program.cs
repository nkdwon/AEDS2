using System;
using System.Collections;

public class Program
{
  public static void Main(string[] args)
  {

    Queue fila = new Queue();
    fila.Enqueue(1);
    fila.Enqueue(2);
    fila.Enqueue(3);
    fila.Enqueue(4);
    fila.Enqueue(5);
    Console.WriteLine("Fila: ({0}) ", fila.Count);
    fila.Dequeue();
    if (fila.Contains(1))
    {
      Console.WriteLine("sim");
    }
    else
    {
      Console.WriteLine("não");
    }
    if (fila.Contains(5))
    {
      Console.WriteLine("sim");
    }
    else
    {
      Console.WriteLine("não");
    }
    Console.WriteLine("Fila: ({0}) ", fila.Peek());
    fila.Enqueue(6);
    fila.Enqueue(7);
    fila.Enqueue(8);
    fila.Enqueue(9);
    fila.Enqueue(10);
    Console.WriteLine("Fila: ({0}) / ({1})", fila.Dequeue(), fila.Dequeue());
    fila.Clear();
    Console.WriteLine("Fila: ({0}) ", fila.Count);
  }
}