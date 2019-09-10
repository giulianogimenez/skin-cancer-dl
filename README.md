# Redes neurais para diagnósticos de câncer de pele

Projeto realizado para a **Faculdade de Tecnologia de São José dos Campos - SP (FATEC SJC)** para o trabalho de graduação do curso superior de tecnologia em Banco de Dados, realizado por mim e orientado pelo professor Me. Giuliano Araujo Bertoti.

O projeto consiste em fazer um conjunto de **redes neurais convolucionais (CNN)** e que elas aprendam as diferenças entre as **lesões de pele** nos quais foram diagnosticadas como **câncer de pele ou não**. A análise de imagens de lesões de pele são classificadas entre *malígnas (malignant)* e *benignas (benign)*, nos quais possuem representatividade como câncer e não-câncer, respectivamente.

Para isso, foi necessário montar um processo end-to-end desde a ingestão de dados para montar um dataset, os treinos das redes neurais, uma API para ter acesso aos modelos de redes neurais e uma interface de usuário para consultá-la.

## Ingestão dos dados:
A partir do projeto *isic-downloader* feito em Python 3, é possível efetuar o download das imagens das lesões de pele do repositório The International Skin Imaging Collaboration (https://www.isic-archive.com), efetua a classificação das imagens e as separa nos diretórios malignant e benign. 

## Treinando as redes neurais: 
O projeto *CNN* treina as redes neurais utilizando os modelos LeNet, AlexNet e VGG utilizando a biblioteca Deeplearning4J (https://deeplearning4j.org/).

## API Rest
Foi criada uma API Restful para utilizar os modelos de redes neurais treinadas para receber uma imagem e realizar as consultas. O projeto *cancer-consult-api* é responsável por isto.

## UI
A interface de usuário é apenas uma página feita para comunicar com a API Rest e efetuar o upload de uma imagem de lesão de pele, devendo mostrar a resposta ao usuário após o upload da imagem desejada.

O dataset já baixado pelo *dataset-downloader* pode ser encontrado através do seguinte link: https://drive.google.com/drive/folders/19Ty6xcwckFJf3MhrbTy5pmKD8PDGMPWb?usp=sharing
