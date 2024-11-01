import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int N = sc.nextInt(); // número de motoristas
        int K = sc.nextInt(); // número de veículos que o estacionamento comporta
        Stack<Integer> estacionamento = new Stack<>(); // Pilha para controlar os horários de saída dos carros
        boolean podeEstacionarTodos = true; // Controle para verificar se todos motoristas conseguem estacionar
        
        // Condição de parada (último caso de teste com N = 0 e K = 0)
        while (N != 0 && K != 0) {

            for (int i = 0; i < N; i++) {
                int hEntrada = sc.nextInt(); // Hora de chegada do motorista
                int hSaida = sc.nextInt();   // Hora de saída do motorista

                // Remove carros que saem antes do motorista atual entrar
                while (!estacionamento.isEmpty() && estacionamento.peek() <= hEntrada) {
                    estacionamento.pop(); // Remove o último carro que saiu antes da chegada atual
                }

                // Verifica se o estacionamento está cheio
                if (estacionamento.size() >= K) {
                    podeEstacionarTodos = false; // Estacionamento lotado, motorista não consegue estacionar
                } else {
                    // Verifica se o carro atual poderá sair sem bloquear a saída de outro carro
                    if (!estacionamento.isEmpty() && estacionamento.peek() < hSaida) {
                        podeEstacionarTodos = false; // Carro não poderá sair sem bloquear outro
                    } else {
                        estacionamento.push(hSaida); // Adiciona o horário de saída do carro na pilha
                    }
                }
            }

            // Imprime "Sim" se todos motoristas conseguiram estacionar, "Nao" caso contrário
            if (podeEstacionarTodos) {
                System.out.println("Sim");
            } else {
                System.out.println("Nao");
            }

            // Lê o próximo caso de teste
            N = sc.nextInt();
            K = sc.nextInt();
        }

        sc.close();
    }
}
