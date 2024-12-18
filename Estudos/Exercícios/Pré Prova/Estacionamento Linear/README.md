## JAVA
### Estacionamento Linear
#### Descrição

Após muito tempo juntando dinheiro, Rafael finalmente conseguiu comprar seu carro (parcelado, é claro). Chega de pegar ônibus, agora sua vida será mais fácil. Pelo menos isso é o que ele pensava, até ouvir falar do estacionamento perto da faculdade onde ele decidiu estacionar o carro todos os dias.

O estacionamento tem apenas um corredor, com largura o suficiente para acomodar um carro, e profundidade suficiente para acomodar K carros, um atrás do outro. Como este estacionamento só tem um portão, só é possível entrar e sair por ele.

Quando o primeiro carro entra no estacionamento, o mesmo ocupa a posição próxima à parede, ao fundo do estacionamento. Todos os próximos carros estacionam logo atrás dele, formando uma fila. Obviamente, não é possível que um carro passe por cima de outro, portanto só é possível que um carro saia do estacionamento se ele for o último da fila.

Dados o horário de chegada e saída prevista de N motoristas, incluindo Rafael, diga se é possível que todos consigam estacionar e remover seus carros no estacionamento citado.

### Entrada
Haverá diversos casos de teste. Cada caso de teste inicia com dois inteiros N e K (3 ≤ N ≤ 10⁴, 1 ≤ K ≤ 10³), representando o número de motoristas que farão uso do estacionamento, e o número de carros que o estacionamento consegue comportar, respectivamente.

Em seguida haverá N linhas, cada uma contendo dois inteiros Ci e Si (1 ≤ Ci, Si ≤ 10⁵), representando, respectivamente, o horário de chegada e saída do motorista i (1 ≤ i ≤ N). Os valores de Ci são dados de forma crescente, ou seja, Ci < Ci+1 para todo 1 ≤ i < N. Não haverá mais de um motorista que chegam ao mesmo tempo, e nem mais de um motorista que saiam ao mesmo tempo. É possível que um motorista consiga estacionar no mesmo momento em que outro motorista deseja sair. O último caso de teste é indicado quando N = K = 0, o qual não deverá ser processado.

### Saída
Para cada caso de teste imprima uma linha, contendo a palavra “Sim”, caso seja possível que todos os N motoristas façam uso do estacionamento, ou “Nao” caso contrário.