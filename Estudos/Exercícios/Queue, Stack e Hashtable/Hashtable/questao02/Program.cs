using System;
using System.Collections;

public class Program
{
  public static void Main(string[] args)
  {
    Hashtable hash = new Hashtable();

    for (int i = 1; i <= 5; i++)
    {
      hash.Add(i, 10 * i);
    }
    Console.WriteLine("Count:" + hash.Count);
    hash.Remove(1);
    if (hash.ContainsKey(1) == true)
    {
      Console.WriteLine("sim");
    }
    else
    {
      Console.WriteLine("não");
    }
    if (hash.ContainsValue(5) == true)
    {
      Console.WriteLine("sim");
    }
    else
    {
      Console.WriteLine("não");
    }
    for (int i = 6; i <= 10; i++)
    {
      hash.Add(i, 10 * i);
    }
    hash.Remove(5);
    Console.WriteLine("Contains(5): " + hash.ContainsKey(5));
    hash.Remove(8);
    Console.WriteLine("Contains(8): " + hash.ContainsKey(8));
    hash.Clear();
    Console.WriteLine("Count: " + hash.Count);


  }
}