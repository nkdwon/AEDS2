import pandas as pd
import matplotlib.pyplot as plt

# Ler os dados do CSV - Aponte o seu caminho para o arquivo
df = pd.read_csv('data.csv')

# Filtrar os dados por tamanho de array
array_100 = df[df['Tamanho'] == 100]
array_1000 = df[df['Tamanho'] == 1000]
array_10000 = df[df['Tamanho'] == 10000]

# Função para plotar os gráficos de barras
def plotar_grafico(array_data, tamanho):
    metodos = array_data['Metodo'].unique()
    tipos = array_data['Tipo'].unique()

    for tipo in tipos:
        dados_tipo = array_data[array_data['Tipo'] == tipo]
        tempos = dados_tipo['Tempo (ms)']
        
        plt.figure(figsize=(8, 5))
        plt.bar(dados_tipo['Metodo'], tempos, color=['blue', 'green', 'red', 'orange'])
        plt.title(f'Tempos de Execução ({tipo}) - Tamanho {tamanho}')
        plt.xlabel('Método')
        plt.ylabel('Tempo de Execução (ms)')
        plt.show()

# Plotar gráficos para cada tamanho
plotar_grafico(array_100, 100)
plotar_grafico(array_1000, 1000)
plotar_grafico(array_10000, 10000)
