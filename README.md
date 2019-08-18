# Redes neurais para diagnósticos de câncer de pele

Projeto realizado para a **Faculdade de Tecnologia de São José dos Campos - SP (FATEC SJC)** para o trabalho de graduação do curso superior de tecnologia em Banco de Dados, realizado por mim e orientado pelo professor Me. Giuliano Araujo Bertoti.

O projeto consiste em fazer uma **rede neural convolucional (CNN)** à apreder as diferenças entre as **lesões de pele** nos quais foram diagnosticadas como **câncer de pele ou não**. A análise de imagens de lesões de pele são classificadas entre *malígnas (malignant)* e *benignas (benign)*, nos quais possuem representatividade como câncer e não-câncer, respectivamente.

O projeto é dividido entre dois sub projetos: 

## dataset-downloader: 
Efetua o download das imagens das lesões de pele do repositório The International Skin Imaging Collaboration (https://www.isic-archive.com), efetua a classificação das imagens e as separa nos diretórios malignant e benign. 

## CNN: 
é a rede neural no qual faz o treinamento através desse dataset.

O dataset já baixado pelo *dataset-downloader* pode ser encontrado através do seguinte link: https://drive.google.com/drive/folders/19Ty6xcwckFJf3MhrbTy5pmKD8PDGMPWb?usp=sharing
