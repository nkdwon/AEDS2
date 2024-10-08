import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        
        String caminhoCsv = "./tmp/pokemon.csv"; // Caminho do arquivo CSV

        Scanner scanId = new Scanner(System.in);
        String entrada;

        while (true) {
            entrada = scanId.nextLine();

            if (entrada.equals("FIM")) {
                break; // Sai do loop se o usuário digitar "FIM"
            }

            // Verifica se a entrada contém apenas números
            if (entrada.matches("\\d+")) { // Verifica se a string contém apenas dígitos
                try {
                    System.out.println("Entrada válida: " + entrada); // Depuração: ver o que está sendo lido
                    int id = Integer.parseInt(entrada); // Converte para inteiro

                    // Depuração: Verifique se a conversão foi bem-sucedida
                    System.out.println("ID convertido: " + id);

                    // Depuração: Verifique o caminho e o método de busca
                    System.out.println("Caminho do CSV: " + caminhoCsv);

                    Pokemon pokemonEncontrado = Pokemon.buscarPokemonPorId(caminhoCsv, id);

                    if (pokemonEncontrado != null) {
                        System.out.println(pokemonEncontrado);
                    } else {
                        System.out.println("Pokémon com ID " + id + " não encontrado.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Erro ao converter a entrada para número inteiro.");
                    e.printStackTrace(); // Depuração: Imprimir o stack trace
                } catch (Exception e) {
                    System.out.println("Erro ao buscar o Pokémon.");
                    e.printStackTrace(); // Depuração: Imprimir o stack trace de qualquer outro erro
                }
            } else {
                System.out.println("Entrada inválida! Por favor, insira um número inteiro ou 'FIM'.");
            }
        }

        scanId.close();
    }
}
