/* import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Pokedex {
    private List<Pokemon> pokemons;

    public Pokedex() {
        this.pokemons = new ArrayList<>();
    }

    // Método para ler o CSV e popular a lista de Pokémons
    public void lerCsv(String caminhoCsv) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(caminhoCsv));
        String linha;

        // Ignora a primeira linha se for o cabeçalho
        br.readLine();
        int id = 1;

        while ((linha = br.readLine()) != null) {
            String[] valores = linha.split(",");
            Pokemon pokemon = new Pokemon();

            pokemon.setId(id++);
            pokemon.setGeneration(valores[1].isEmpty() ? 0 : Integer.parseInt(valores[1]));
            pokemon.setName(valores[2].isEmpty() ? "" : valores[2]);
            pokemon.setDescription(valores[3].isEmpty() ? "" : valores[3]);
            pokemon.setTypes(valores[4].isEmpty() ? List.of() : List.of(valores[4].split(";")));

            // Fix for abilities: remove quotes and square brackets before splitting
            String abilitiesString = valores[5].isEmpty() ? "" : valores[5].replaceAll("[\\[\\]\"]", "").trim();
            List<String> abilitiesList = Arrays.asList(abilitiesString.split(","));
            pokemon.setAbilities(abilitiesList);

            pokemon.setWeight(valores[6].isEmpty() || valores[6].trim().equals("") ? 0.0 : Double.parseDouble(valores[6].trim()));
            pokemon.setHeight(valores[7].trim().isEmpty() ? 0 : Double.parseDouble(valores[7].trim()));
            pokemon.setCaptureRate(valores[8].isEmpty() ? 0 : Integer.parseInt(valores[8]));
            pokemon.setLegendary(valores[9].isEmpty() ? false : Boolean.parseBoolean(valores[9]));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            try {
                pokemon.setCaptureDate(valores[10].isEmpty() ? null : dateFormat.parse(valores[10]));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            pokemons.add(pokemon);
        }
        br.close();
    }

    // Método para buscar um Pokémon pelo ID
    public Pokemon buscarPokemonPorId(int id) {
        for (Pokemon pokemon : pokemons) {
            if (pokemon.getId() == id) {
                return pokemon;
            }
        }
        return null; // Se não encontrar, retorna null
    }

    // Método para imprimir todos os Pokémons
    public void imprimirTodos() {
        for (Pokemon pokemon : pokemons) {
            System.out.println(pokemon);
        }
    }
}
 */