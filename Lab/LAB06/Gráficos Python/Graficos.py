import pandas as pd
import matplotlib.pyplot as plt

# Carregando os dados do arquivo CSV
dados = pd.read_csv(r'd:\FELIPE\PUC Faculdade\SEMESTRE II\AEDS2\Lab\LAB06\Gráficos Python\data.csv')


# Filtrando os dados para um gráfico específico
# Por exemplo, comparar o tempo de execução para cada método em diferentes tamanhos de array
metodos = dados['Metodo'].unique()
tamanhos = dados['Tamanho'].unique()

# Configurando o gráfico
plt.figure(figsize=(12, 6))

# Para cada método, plote o tempo em função do tamanho
for metodo in metodos:
    subset = dados[dados['Metodo'] == metodo]
    plt.plot(subset['Tamanho'], subset['Tempo (ms)'], marker='o', label=metodo)

# Adicionando título e rótulos
plt.title('Desempenho do QuickSort por Método e Tamanho do Array')
plt.xlabel('Tamanho do Array')
plt.ylabel('Tempo (ms)')
plt.legend()
plt.grid()
plt.show()
