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
- Geração de executável `.exe` para fácil distribuição.

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
- OpenCV 4.10.0 (https://opencv.org/blog/opencv-4-10-0/)
- Windows (para execução do `.exe`)

### ⚙️ Como Executar

#### 📌 Opção 1: Executar via `.exe` (distribuição fácil)

1. Baixe todos os arquivos do repositório e coloque a pasta "FaceFit Access" onde desejar, é recomendado não deixar em caminhos com acento (Exe: Área de Trabalho/) para garantir o que todas as funcionalidades do sistema funcionem de forma correta.
2. Acesse (https://gluonhq.com/products/javafx/) e desça na sessão de Downloads, marque a opção ![image](https://github.com/user-attachments/assets/e79e2998-d298-422f-88bb-0b6c9b493196)
 e ajuste os filtros conforme a imagem:
![image](https://github.com/user-attachments/assets/e641c98a-9d51-45ae-b4c4-2dd442251256)

3. Clique em ![image](https://github.com/user-attachments/assets/f4337a30-9c92-451b-91a5-82c1542e6719)



4. Clique duas vezes no `FaceFit Access.exe` para abrir o sistema.

#### 📌 Opção 2: Executar via Terminal

1. Abra o terminal (cmd) no diretório raiz do projeto `FaceFit Access`.
2. Execute o comando abaixo para rodar a aplicação:

```bash
java --module-path libs/javafx-sdk-19.0.2.1/lib --add-modules javafx.controls,javafx.fxml,javafx.swing -cp "build\classes;libs\opencv-4110.jar" projetofinal.ProjetoFinal
```

## 👨 Autor
- Matheus Hoffmann

