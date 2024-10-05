import java.util.Random;

public class QuickSort {

  // MÉTODO DE CRIAÇÃO DE UM ARRAY ALEATÓRIO
  static int[] createArray(int size) {
    Random rnd = new Random(12345);
    int[] finalArray = new int[size];
    for (int i = 0; i < size; i++) {
      finalArray[i] = rnd.nextInt() % 10000;
    }
    return finalArray;
  }

  // MÉTODO DE CRIAÇÃO DE UM ARRAY ORDENADO
  static int[] createSortedArray(int size) {
    int[] sortedArray = new int[size];
    for (int i = 0; i < size; i++) {
      sortedArray[i] = i; // Array ordenado
    }
    return sortedArray;
  }

  // MÉTODO DE CRIAÇÃO DE UM ARRAY QUASE ORDENADO
  static int[] createAlmostSortedArray(int size) {
    int[] almostSortedArray = createSortedArray(size);
    Random rnd = new Random(12345);
    for (int i = 0; i < size / 10; i++) { // Inverte alguns elementos
      int index1 = rnd.nextInt(size);
      int index2 = rnd.nextInt(size);
      swap(almostSortedArray, index1, index2);
    }
    return almostSortedArray;
  }

  static int medianOfThree(int a, int b, int c) {
    if ((a > b) == (a < c))
      return a; // 'a' é a mediana
    else if ((b > a) == (b < c))
      return b; // 'b' é a mediana
    else
      return c; // 'c' é a mediana
  }

  static void swap(int array[], int i, int j) {
    AlgorithmLog.countSwaps();
    int temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }

  static void quickSortFirstPivot(int[] array, int left, int right) {
    if (left >= right)
      return; // Base da recursão
    int i = left, j = right;
    int pivo = array[left];
    while (i <= j) {
      while (array[i] < pivo) {
        AlgorithmLog.countComparisons();
        i++;
      }
      while (array[j] > pivo) {
        AlgorithmLog.countComparisons();
        j--;
      }
      if (i <= j) {
        swap(array, i, j);
        i++;
        j--;
      }
    }
    if (left < j)
      quickSortFirstPivot(array, left, j);
    if (i < right)
      quickSortFirstPivot(array, i, right);
  }

  static void quickSortLastPivot(int[] array, int left, int right) {
    if (left >= right)
      return; // Base da recursão
    int i = left, j = right;
    int pivo = array[right];
    while (i <= j) {
      while (array[i] < pivo) {
        AlgorithmLog.countComparisons();
        i++;
      }
      while (array[j] > pivo) {
        AlgorithmLog.countComparisons();
        j--;
      }
      if (i <= j) {
        swap(array, i, j);
        i++;
        j--;
      }
    }
    if (left < j)
      quickSortLastPivot(array, left, j);
    if (i < right)
      quickSortLastPivot(array, i, right);
  }

  static void quickSortRandomPivot(int[] array, int left, int right) {
    if (left >= right)
      return; // Base da recursão
    Random rnd = new Random();
    int i = left, j = right;
    int pivoIndex = left + rnd.nextInt(right - left + 1); // Seleciona o pivô aleatoriamente
    int pivo = array[pivoIndex];
    swap(array, pivoIndex, left); // Move o pivô para o início
    while (i <= j) {
      while (array[i] < pivo) {
        AlgorithmLog.countComparisons();
        i++;
      }
      while (array[j] > pivo) {
        AlgorithmLog.countComparisons();
        j--;
      }
      if (i <= j) {
        swap(array, i, j);
        i++;
        j--;
      }
    }
    if (left < j)
      quickSortRandomPivot(array, left, j);
    if (i < right)
      quickSortRandomPivot(array, i, right);
  }

  static void quickSortMedianOfThree(int[] array, int left, int right) {
    if (left >= right)
      return; // Base da recursão
    int i = left, j = right;
    int pivo = medianOfThree(array[left], array[(left + right) / 2], array[right]);
    while (i <= j) {
      while (array[i] < pivo) {
        AlgorithmLog.countComparisons();
        i++;
      }
      while (array[j] > pivo) {
        AlgorithmLog.countComparisons();
        j--;
      }
      if (i <= j) {
        swap(array, i, j);
        i++;
        j--;
      }
    }
    if (left < j)
      quickSortMedianOfThree(array, left, j);
    if (i < right)
      quickSortMedianOfThree(array, i, right);
  }

  class AlgorithmLog {
    private static long beginTime = 0;
    private static long endTime = 0;
    private static int countComparisons = 0;
    private static int countSwaps = 0;

    public static void countComparisons() {
      countComparisons++;
    }

    public static void countSwaps() {
      countSwaps++;
    }

    public static void resetCounts() {
      countComparisons = 0;
      countSwaps = 0;
    }

    public static int getComparisons() {
      return countComparisons;
    }

    public static int getSwaps() {
      return countSwaps;
    }

    public static void start() {
      beginTime = System.currentTimeMillis();
    }

    public static void stop() {
      endTime = System.currentTimeMillis();
    }

    public static long getTime() {
      return endTime - beginTime;
    }
  }

  public static void runAndLogSort(int[] array, String type) {
    int[] tempArray;

    // FIRST PIVOT
    tempArray = array.clone();
    System.out.println(type + " - QuickSort com primeiro elemento de pivô");
    AlgorithmLog.resetCounts();
    AlgorithmLog.start();
    quickSortFirstPivot(tempArray, 0, tempArray.length - 1);
    AlgorithmLog.stop();
    System.out.println("Tempo: " + AlgorithmLog.getTime() + " ms | Comparações: " + AlgorithmLog.getComparisons()
        + " | Trocas: " + AlgorithmLog.getSwaps());

    // LAST PIVOT
    tempArray = array.clone();
    System.out.println(type + " - QuickSort com último elemento de pivô");
    AlgorithmLog.resetCounts();
    AlgorithmLog.start();
    quickSortLastPivot(tempArray, 0, tempArray.length - 1);
    AlgorithmLog.stop();
    System.out.println("Tempo: " + AlgorithmLog.getTime() + " ms | Comparações: " + AlgorithmLog.getComparisons()
        + " | Trocas: " + AlgorithmLog.getSwaps());

    // RANDOM PIVOT
    tempArray = array.clone();
    System.out.println(type + " - QuickSort com pivô aleatório");
    AlgorithmLog.resetCounts();
    AlgorithmLog.start();
    quickSortRandomPivot(tempArray, 0, tempArray.length - 1);
    AlgorithmLog.stop();
    System.out.println("Tempo: " + AlgorithmLog.getTime() + " ms | Comparações: " + AlgorithmLog.getComparisons()
        + " | Trocas: " + AlgorithmLog.getSwaps());

    // MEDIAN OF THREE
    tempArray = array.clone();
    System.out.println(type + " - QuickSort com mediana de três");
    AlgorithmLog.resetCounts();
    AlgorithmLog.start();
    quickSortMedianOfThree(tempArray, 0, tempArray.length - 1);
    AlgorithmLog.stop();
    System.out.println("Tempo: " + AlgorithmLog.getTime() + " ms | Comparações: " + AlgorithmLog.getComparisons()
        + " | Trocas: " + AlgorithmLog.getSwaps());
    System.out.println();
  }

  public static void main(String[] args) {

    // 100 ELEMENTOS
    int[] randomArray = createArray(100);
    int[] sortedArray = createSortedArray(100);
    int[] almostSortedArray = createAlmostSortedArray(100);
    System.out.println();
    System.out.println("==== 100 ELEMENTOS ====");
    runAndLogSort(randomArray, "Array Aleatório");
    runAndLogSort(sortedArray, "Array Ordenado");
    runAndLogSort(almostSortedArray, "Array Quase Ordenado");
    System.out.println();

    // 1000 ELEMENTOS
    randomArray = createArray(1000);
    sortedArray = createSortedArray(1000);
    almostSortedArray = createAlmostSortedArray(1000);

    System.out.println("==== 1000 ELEMENTOS ====");
    runAndLogSort(randomArray, "Array Aleatório");
    runAndLogSort(sortedArray, "Array Ordenado");
    runAndLogSort(almostSortedArray, "Array Quase Ordenado");
    System.out.println();

    // 10000 ELEMENTOS
    randomArray = createArray(10000);
    sortedArray = createSortedArray(10000);
    almostSortedArray = createAlmostSortedArray(10000);

    System.out.println("==== 10000 ELEMENTOS ====");
    runAndLogSort(randomArray, "Array Aleatório");
    runAndLogSort(sortedArray, "Array Ordenado");
    runAndLogSort(almostSortedArray, "Array Quase Ordenado");
  }
}
