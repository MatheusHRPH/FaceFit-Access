package projetofinal;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class VisualizarClientesApp {

    public static class Cliente {
        String nome, telefone, email, cpf, endereco, plano;
        List<String> fotos;
        boolean acessoLiberado;

        public Cliente(String nome, String telefone, String email, String cpf,
                       String endereco, String plano, List<String> fotos, boolean acessoLiberado) {
            this.nome = nome;
            this.telefone = telefone;
            this.email = email;
            this.cpf = cpf;
            this.endereco = endereco;
            this.plano = plano;
            this.fotos = fotos;
            this.acessoLiberado = acessoLiberado;
        }

        public String getNome() { return nome; }
        public String getTelefone() { return telefone; }
        public String getEmail() { return email; }
        public String getCpf() { return cpf; }
        public String getEndereco() { return endereco; }
        public String getPlano() { return plano; }
        public List<String> getFotos() { return fotos; }
        public boolean isAcessoLiberado() { return acessoLiberado; }
        public void setAcessoLiberado(boolean acessoLiberado) { this.acessoLiberado = acessoLiberado; }

        public String getFotoPrincipal() {
            if (fotos != null && !fotos.isEmpty()) {
                String foto1 = fotos.get(0).trim();
                File f = new File(foto1);
                if (f.exists()) return f.getPath();
            }
            return "clientes/semfoto.jpg";
        }

        @Override
        public String toString() {
            return nome + " | " + telefone + " | " + email + " | " + cpf + " | " + plano;
        }
    }

    public static void showWindow() {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle("Visualizar Clientes");

            ObservableList<Cliente> todosClientes = FXCollections.observableArrayList();
            File csvFile = new File("clientes/clientes.csv");

            if (csvFile.exists()) {
                try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
                    String linha;
                    boolean primeiraLinha = true;
                    while ((linha = br.readLine()) != null) {
                        if (primeiraLinha) {
                            primeiraLinha = false;
                            continue;
                        }
                        String[] campos = parseCSVLine(linha);
                        if (campos.length >= 8) {
                            String nome = campos[0];
                            if (nome.isEmpty()) continue;

                            String telefone = campos[1];
                            String email = campos[2];
                            String cpf = campos[3];
                            String endereco = campos[4];
                            String plano = campos[5];
                            String fotosRaw = campos[6];
                            boolean acesso = campos[7].equalsIgnoreCase("true") || campos[7].equalsIgnoreCase("sim");

                            List<String> listaFotos = new ArrayList<>();
                            for (String foto : fotosRaw.split(",")) {
                                foto = foto.trim().replaceAll("^\"|\"$", "");
                                if (!foto.isEmpty()) listaFotos.add(foto);
                            }

                            Cliente cliente = new Cliente(nome, telefone, email, cpf, endereco, plano, listaFotos, acesso);
                            todosClientes.add(cliente);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            TextField filtroNome = new TextField();
            filtroNome.setPromptText("Filtrar por nome...");
            filtroNome.setMaxWidth(300);

            GridPane gridPane = new GridPane();
            gridPane.setStyle("-fx-background-color: #1A1A40;");
            gridPane.setHgap(15);
            gridPane.setVgap(15);
            gridPane.setPadding(new Insets(15));
            gridPane.setAlignment(Pos.TOP_CENTER);

            ScrollPane scrollPane = new ScrollPane(gridPane);
            scrollPane.setFitToWidth(true);
            scrollPane.setStyle("-fx-background: #1A1A40; -fx-background-color: #1A1A40;");
            scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            scrollPane.setPadding(new Insets(0));

            filtroNome.textProperty().addListener((obs, oldVal, newVal) -> {
                atualizarGrid(gridPane, todosClientes, newVal.trim().toLowerCase(), stage);
            });

            Button btnFechar = new Button("Fechar");
            btnFechar.setStyle("-fx-background-color: #F1C40F; -fx-text-fill: #1A1A40; -fx-font-weight: bold;");

            btnFechar.setOnMouseEntered(e -> btnFechar.setStyle("-fx-background-color: #F39C12; -fx-text-fill: #1A1A40; -fx-font-weight: bold;"));
            btnFechar.setOnMouseExited(e -> btnFechar.setStyle("-fx-background-color: #F1C40F; -fx-text-fill: #1A1A40; -fx-font-weight: bold;"));

            btnFechar.setOnAction(e -> stage.close());


            VBox root = new VBox(10,
                    new Label("Lista de Clientes") {{
                        setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #F1C40F;");
                    }},
                    filtroNome,
                    scrollPane,
                    btnFechar
            );
            root.setPadding(new Insets(20, 25, 25, 25));
            root.setAlignment(Pos.TOP_CENTER);
            root.setStyle("-fx-background-color: #1A1A40;");
            VBox.setVgrow(scrollPane, Priority.ALWAYS);

            atualizarGrid(gridPane, todosClientes, "", stage);

            var screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMinY());
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());

            stage.setScene(new Scene(root));
            Platform.runLater(() -> root.requestFocus());
            stage.setMaximized(true);
            stage.show();
        });
    }
    

    private static void atualizarGrid(GridPane gridPane, List<Cliente> clientes, String filtro, Stage stage) {
        gridPane.getChildren().clear();
        int col = 0, row = 0;

        for (Cliente cliente : clientes) {
            if (!cliente.getNome().toLowerCase().contains(filtro)) continue;

            ImageView imageView = new ImageView();
            imageView.setFitWidth(70);
            imageView.setFitHeight(70);
            imageView.setPreserveRatio(true);

            File imgFile = new File(cliente.getFotoPrincipal());
            if (imgFile.exists()) {
                try (FileInputStream fis = new FileInputStream(imgFile)) {
                    imageView.setImage(new Image(fis));
                } catch (IOException e) {
                    imageView.setImage(getImagemDefault());
                }
            } else {
                imageView.setImage(getImagemDefault());
            }

            Label lblNome = new Label(cliente.getNome());
            Label lblTelefone = new Label("Telefone: " + cliente.getTelefone());
            Label lblEmail = new Label("Email: " + cliente.getEmail());
            Label lblCpf = new Label("CPF: " + cliente.getCpf());
            Label lblEndereco = new Label("Endereço: " + cliente.getEndereco());
            Label lblPlano = new Label("Plano: " + cliente.getPlano());
            CheckBox chkAcesso = new CheckBox("Acesso Liberado");
            chkAcesso.setSelected(cliente.isAcessoLiberado());

            Button btnExcluir = new Button("Excluir");

            lblNome.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: white;");
            for (Label lbl : new Label[]{lblTelefone, lblEmail, lblCpf, lblEndereco, lblPlano}) {
                lbl.setStyle("-fx-text-fill: white;");
            }
            chkAcesso.setStyle("-fx-text-fill: white;");
            btnExcluir.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold;");

            chkAcesso.setOnAction(e -> {
                cliente.setAcessoLiberado(chkAcesso.isSelected());
                atualizarCSV(clientes);
            });

    btnExcluir.setOnAction(e -> {
        Stage popupStage = new Stage();
        popupStage.setTitle("Confirmação de Exclusão");

        Label mensagem = new Label("Deseja realmente excluir o cliente \"" + cliente.getNome() + "\"?");
        mensagem.setWrapText(true);
        mensagem.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");

        Button btnSim = new Button("Sim");
        Button btnNao = new Button("Cancelar");

        btnSim.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-weight: bold;");
        btnNao.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; -fx-font-weight: bold;");

        btnSim.setOnAction(ev -> {
            popupStage.close();
            clientes.remove(cliente);
            deletarFotosCliente(cliente.getFotos());
            atualizarCSV(clientes);
            atualizarGrid(gridPane, clientes, filtro, stage);
        });

        btnNao.setOnAction(ev -> popupStage.close());

        HBox botoes = new HBox(20, btnSim, btnNao);
        botoes.setAlignment(Pos.CENTER);

        VBox layout = new VBox(15, mensagem, botoes);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: #1A1A40; -fx-border-color: #F1C40F; -fx-border-width: 2px; -fx-border-radius: 8; -fx-background-radius: 8;");

        Scene popupScene = new Scene(layout, 400, 150);
        popupStage.setScene(popupScene);
        popupStage.initOwner(stage); 
        popupStage.setResizable(false);
        popupStage.showAndWait();
    });


            VBox infoBox = new VBox(4, lblNome, lblTelefone, lblEmail, lblCpf, lblEndereco, lblPlano, chkAcesso, btnExcluir);
            HBox card = new HBox(15, imageView, infoBox);
            card.setAlignment(Pos.CENTER_LEFT);
            card.setPadding(new Insets(10));

            VBox wrapper = new VBox(card);
            wrapper.setPadding(new Insets(5));
            wrapper.setStyle("-fx-background-color: #273746; -fx-border-color: #F1C40F; -fx-border-radius: 8; -fx-background-radius: 8;");
            wrapper.setPrefWidth(500);

            gridPane.add(wrapper, col, row);
            if (++col >= 4) {
                col = 0;
                row++;
            }
        }

        if (gridPane.getChildren().isEmpty()) {
            Label vazio = new Label("Nenhum cliente encontrado.");
            vazio.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
            gridPane.add(new StackPane(vazio), 0, 0, 4, 1);
        }
    }

    private static void atualizarCSV(List<Cliente> clientes) {
        File csvFile = new File("clientes/clientes.csv");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            bw.write("Nome,\"Telefone\",\"Email\",\"CPF\",\"Endereco\",\"Plano\",\"Fotos\",\"Acesso\"");
            bw.newLine();
            for (Cliente c : clientes) {
                String fotosConcatenadas = String.join(", ", c.getFotos());

                bw.write(String.join(",",
                        "\"" + c.getNome() + "\"",
                        "\"" + c.getTelefone() + "\"",
                        "\"" + c.getEmail() + "\"",
                        "\"" + c.getCpf() + "\"",
                        "\"" + c.getEndereco() + "\"",
                        "\"" + c.getPlano() + "\"",
                        "\"" + fotosConcatenadas + "\"",
                        c.isAcessoLiberado() ? "\"Sim\"" : "\"Não\""
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deletarFotosCliente(List<String> fotos) {
        for (String caminhoFoto : fotos) {
            File f = new File(caminhoFoto.trim());
            if (f.exists()) f.delete();
        }
    }

    private static Image getImagemDefault() {
        File fallbackFile = new File("clientes/semfoto.jpg");
        if (fallbackFile.exists()) {
            return new Image(fallbackFile.toURI().toString());
        } else {
            return new Image("https://via.placeholder.com/70");
        }
    }

    private static String[] parseCSVLine(String line) {
        return Arrays.stream(line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)"))
                .map(s -> s.replaceAll("^\"|\"$", "").trim())
                .toArray(String[]::new);
    }
}
