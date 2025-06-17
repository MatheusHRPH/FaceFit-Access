# FaceFit Access

**Sistema de Controle de Acesso por Reconhecimento Facial para Academias**  
🚀 Projeto final de Processamento de Imagens

## 🧠 Resumo

O **FaceFit Access** é uma aplicação Java com interface gráfica baseada em JavaFX e OpenCV, desenvolvida para automatizar o controle de entrada de alunos em academias utilizando reconhecimento facial. O sistema permite o cadastro e a verificação de clientes por imagem, garantindo segurança, praticidade e inovação no processo de identificação.

## 📸 Funcionalidades Principais

- Cadastro de novos alunos com captura da imagem facial via webcam.
- Reconhecimento facial em tempo real para liberação de acesso.
- Visualização e gerenciamento da lista de clientes.
- Interface gráfica intuitiva com feedback visual.

## 🛠️ Tecnologias Utilizadas

- Java 17+
- JavaFX 19.0.2.1
- OpenCV 4.10.0
- Launch4j (empacotamento em `.exe`)
- Maven (opcional)
- IDE: NetBeans

## 🧩 Setup do Projeto

### 🔧 Requisitos

- Java Runtime Environment (JRE) 17 instalado.
- JavaFX 19.0.2.1 (https://gluonhq.com/products/javafx/)
- OpenCV 4.11.0 (https://opencv.org/releases/)
- Windows (para execução do `.exe`)

### ⚙️ Como Executar

1. Baixe todos os arquivos do repositório e coloque a pasta "FaceFit Access" onde desejar, é recomendado não deixar em caminhos com acento (Exe: Área de Trabalho/) para garantir o que todas as funcionalidades do sistema funcionem de forma correta.

2. Acesse (https://gluonhq.com/products/javafx/) e desça até a sessão de Downloads, marque a opção ![image](https://github.com/user-attachments/assets/e79e2998-d298-422f-88bb-0b6c9b493196)
 e ajuste os filtros conforme a imagem:
![image](https://github.com/user-attachments/assets/e641c98a-9d51-45ae-b4c4-2dd442251256)

3. Clique em ![image](https://github.com/user-attachments/assets/f4337a30-9c92-451b-91a5-82c1542e6719)

4. Após concluído o Download, Extraia os arquivos e copie a pasta ![image](https://github.com/user-attachments/assets/b1bbe18a-9618-4b8a-92c7-87b711bc0eb0) e cole ela dentro da pasta ![image](https://github.com/user-attachments/assets/651b82ef-de9d-49e4-b7a8-37764b937a1d) no projeto.

5. Acesse (https://opencv.org/releases/), na área do OpenCV 4.11.0 clique em ![image](https://github.com/user-attachments/assets/0b4239ab-8b5b-4989-a681-09892be2f31b)


![image](https://github.com/user-attachments/assets/1d9de16a-b2aa-42f7-bc7d-ba142f8325dc)


Após o Download, execute o arquivo baixado e extraia os arquivos

![image](https://github.com/user-attachments/assets/9e4fa971-6f96-426e-8d24-8e1b9327a6e9)

6. Após extrair os arquivos, navegue até opencv\build\java\x64, copie o arquivo ![image](https://github.com/user-attachments/assets/55becf57-833d-4061-beb3-476e92ddea71)
 e cole ele dentro da pasta ![image](https://github.com/user-attachments/assets/68449321-578c-4d0f-9e20-45f40e768a42)

7. Sua pasta libs deve estar assim:

![image](https://github.com/user-attachments/assets/2ace6faa-373c-4d43-bf45-a018057c7410)


8. Após a concluir os passos anteriores, execute o arquivo ![image](https://github.com/user-attachments/assets/ad91470c-6b61-4bda-a489-ebf8480afb93) na pasta do projeto.

**Obs:** O download desses arquivos de forma externa é necessário pois o GitHub não suporta arquivos grandes.


#### Executar via Terminal:

1. Abra o terminal (cmd) no diretório raiz do projeto `FaceFit Access`.
2. Execute o comando abaixo para rodar a aplicação:

```bash
java --module-path libs/javafx-sdk-19.0.2.1/lib --add-modules javafx.controls,javafx.fxml,javafx.swing -cp "build\classes;libs\opencv-4110.jar" projetofinal.ProjetoFinal
```

## 👨 Autor
- Matheus Hoffmann

