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
- JavaFX
- OpenCV 4.x (incluso no projeto)
- Launch4j (empacotamento em `.exe`)
- Maven (opcional)
- IDE: NetBeans

## 🧩 Setup do Projeto

### 🔧 Requisitos

- Java Runtime Environment (JRE) 17 ou superior instalado.
- Windows (para execução do `.exe`)

> **Obs:** O OpenCV está incluso na pasta `libs/`, portanto não é necessário instalação separada.

### ⚙️ Como Executar

#### 📌 Opção 1: Executar via `.exe` (distribuição fácil)

1. Baixe todos os arquivos do repositório e coloque a pasta "FaceFit Access" onde desejar, é recomendado não deixar em caminhos com acento (Exe: Área de Trabalho/) para garantir o que todas as funcionalidades do sistema funcionem de forma correta.
2. Certifique-se que a pasta `libs/` com as DLLs do OpenCV esteja no mesmo diretório do `.exe`.
3. Clique duas vezes no `FaceFitAccess.exe` para abrir o sistema.

#### 📌 Opção 2: Executar via Terminal

1. Abra o terminal (cmd) no diretório raiz do projeto `FaceFit Access`.
2. Execute o comando abaixo para rodar a aplicação:

```bash
java --module-path libs/javafx-sdk-19.0.2.1/lib --add-modules javafx.controls,javafx.fxml,javafx.swing -cp "build\classes;libs\opencv-4110.jar" projetofinal.ProjetoFinal

## 👨 Autor
- Matheus Hoffmann

