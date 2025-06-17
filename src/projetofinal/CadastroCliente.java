package projetofinal;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javafx.stage.Modality;

public class CadastroCliente {

    private static Stage cameraStage;
    private static VideoCapture capture;

    private static String nomeCliente;
    private static String telefoneCliente;
    private static String emailCliente;
    private static String cpfCliente;
    private static String enderecoCliente;
    private static String planoCliente;
    private static boolean acessoLiberado;

    private static List<String> fotosCaminhos = new ArrayList<>();
    private static final int NUM_FOTOS_PARA_CAPTURA = 5;

    private static boolean capturando = false;
    private static int fotosCapturadas = 0;
    private static long ultimoTempoCaptura = 0;

    public static void showFormAndRun(Stage ownerStage) {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            stage.setTitle("Cadastro de Cliente");

            VBox root = new VBox(15);
            root.setPadding(new Insets(25));
            root.setStyle("-fx-background-color: #1A1A40; -fx-border-radius: 12px; -fx-background-radius: 12px;");
            root.setAlignment(Pos.TOP_CENTER);

            Label titulo = new Label("Cadastro de Cliente");
            titulo.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #F1C40F;");

            GridPane grid = new GridPane();
            grid.setHgap(15);
            grid.setVgap(15);
            grid.setAlignment(Pos.CENTER);

            Label lblNome = new Label("Nome:");
            Label lblTelefone = new Label("Telefone:");
            Label lblEmail = new Label("Email:");
            Label lblCpf = new Label("CPF:");
            Label lblEndereco = new Label("Endereço:");
            Label lblPlano = new Label("Plano:");

            Label[] labels = {lblNome, lblTelefone, lblEmail, lblCpf, lblEndereco, lblPlano};
            for (Label lbl : labels) {
                lbl.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #F1C40F;");
            }

            TextField txtNome = new TextField();
            TextField txtTelefone = new TextField();
            TextField txtEmail = new TextField();
            TextField txtCpf = new TextField();
            TextField txtEndereco = new TextField();

            TextField[] textFields = {txtNome, txtTelefone, txtEmail, txtCpf, txtEndereco};
            for (TextField tf : textFields) {
                tf.setPrefWidth(280);
                tf.setStyle("-fx-background-radius: 6px; -fx-border-radius: 6px; -fx-border-color: #64B5F6; -fx-padding: 6 10; -fx-font-size: 13px;");
            }

            ComboBox<String> cbPlano = new ComboBox<>();
            cbPlano.getItems().addAll("Basico", "Padrao", "Premium");
            cbPlano.getSelectionModel().selectFirst();
            cbPlano.setPrefWidth(280);
            cbPlano.setStyle("-fx-background-radius: 6px; -fx-border-radius: 6px; -fx-border-color: #64B5F6; -fx-font-size: 13px;");

            CheckBox chkAcesso = new CheckBox("Acesso Liberado");
            chkAcesso.setSelected(true);
            chkAcesso.setStyle("-fx-font-size: 13px; -fx-text-fill: #F1C40F;");

            grid.add(lblNome, 0, 0);       grid.add(txtNome, 1, 0);
            grid.add(lblTelefone, 0, 1);   grid.add(txtTelefone, 1, 1);
            grid.add(lblEmail, 0, 2);      grid.add(txtEmail, 1, 2);
            grid.add(lblCpf, 0, 3);        grid.add(txtCpf, 1, 3);
            grid.add(lblEndereco, 0, 4);   grid.add(txtEndereco, 1, 4);
            grid.add(lblPlano, 0, 5);      grid.add(cbPlano, 1, 5);
            grid.add(chkAcesso, 1, 6);

            Button btnSalvar = new Button("Cadastrar Fotos");
            btnSalvar.setStyle("-fx-background-color: #F1C40F; -fx-text-fill: #1A1A40; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 8 20; -fx-background-radius: 8;");
            btnSalvar.setOnMouseEntered(e -> btnSalvar.setStyle("-fx-background-color: #F39C12; -fx-text-fill: #1A1A40; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 8 20; -fx-background-radius: 8;"));
            btnSalvar.setOnMouseExited(e -> btnSalvar.setStyle("-fx-background-color: #F1C40F; -fx-text-fill: #1A1A40; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 8 20; -fx-background-radius: 8;"));

            Button btnCancelar = new Button("Cancelar");
            btnCancelar.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 8 20; -fx-background-radius: 8;");
            btnCancelar.setOnMouseEntered(e -> btnCancelar.setStyle("-fx-background-color: #d32f2f; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 8 20; -fx-background-radius: 8;"));
            btnCancelar.setOnMouseExited(e -> btnCancelar.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-padding: 8 20; -fx-background-radius: 8;"));

            HBox botoes = new HBox(15, btnSalvar, btnCancelar);
            botoes.setAlignment(Pos.CENTER);
            botoes.setPadding(new Insets(15, 0, 0, 0));

            root.getChildren().addAll(titulo, grid, botoes);

            btnSalvar.setOnAction(e -> {
                nomeCliente = txtNome.getText().trim();
                telefoneCliente = txtTelefone.getText().trim();
                emailCliente = txtEmail.getText().trim();
                cpfCliente = txtCpf.getText().trim();
                enderecoCliente = txtEndereco.getText().trim();
                planoCliente = cbPlano.getSelectionModel().getSelectedItem();
                acessoLiberado = chkAcesso.isSelected();

                if (nomeCliente.isEmpty() || telefoneCliente.isEmpty() || emailCliente.isEmpty()
                        || cpfCliente.isEmpty() || enderecoCliente.isEmpty() || planoCliente.isEmpty()) {
                    showAlert("Erro", "Por favor, preencha todos os campos.");
                    return;
                }

                fotosCaminhos.clear();
                fotosCapturadas = 0;
                capturando = true;
                stage.close();
                abrirCameraParaCaptura();
            });

            btnCancelar.setOnAction(e -> stage.close());

            Scene scene = new Scene(root, 520, 540);
            stage.setScene(scene);
            stage.initOwner(ownerStage);
            stage.setResizable(false);
            stage.show();
        });
    }

        private static void showPopupMensagem(String titulo, String mensagemTexto, boolean sucesso) {
        Stage popupStage = new Stage();
        popupStage.setTitle(titulo);

        Label mensagem = new Label(mensagemTexto);
        mensagem.setWrapText(true);
        mensagem.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px;");

        Button btnOk = new Button("OK");
        btnOk.setStyle("-fx-background-color: #F1C40F; -fx-text-fill: #1A1A40; -fx-font-weight: bold;");

        btnOk.setOnMouseEntered(e -> btnOk.setStyle("-fx-background-color: #F39C12; -fx-text-fill: #1A1A40; -fx-font-weight: bold;"));
        btnOk.setOnMouseExited(e -> btnOk.setStyle("-fx-background-color: #F1C40F; -fx-text-fill: #1A1A40; -fx-font-weight: bold;"));


        btnOk.setOnAction(ev -> popupStage.close());

        VBox layout = new VBox(15, mensagem, btnOk);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(25));
        layout.setStyle("-fx-background-color: #1A1A40; -fx-border-color: #F1C40F; -fx-border-width: 2px; -fx-border-radius: 8; -fx-background-radius: 8;");

        Scene popupScene = new Scene(layout, 400, 150);
        popupStage.setScene(popupScene);
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setResizable(false);
        popupStage.showAndWait();
    }

    
    
    private static void abrirCameraParaCaptura() {
        cameraStage = new Stage();
        cameraStage.setTitle("Captura de Fotos - " + nomeCliente);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(640);
        imageView.setFitHeight(480);
        imageView.setPreserveRatio(true);
        imageView.setStyle("-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 12, 0, 0, 6);");

        Label lblContador = new Label("Fotos capturadas: 0/" + NUM_FOTOS_PARA_CAPTURA);
        lblContador.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        lblContador.setPadding(new Insets(10, 0, 0, 0));

        Button btnFinalizar = new Button("Finalizar Cadastro");
        btnFinalizar.setDisable(true);
        btnFinalizar.setStyle("-fx-background-color: #F1C40F; -fx-text-fill: #1A1A40; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 24; -fx-background-radius: 10;");
        btnFinalizar.setOnMouseEntered(e -> btnFinalizar.setStyle("-fx-background-color: #F39C12; -fx-text-fill: #1A1A40; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 24; -fx-background-radius: 10;"));
        btnFinalizar.setOnMouseExited(e -> btnFinalizar.setStyle("-fx-background-color: #F1C40F; -fx-text-fill: #1A1A40; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 24; -fx-background-radius: 10;"));

        Button btnCancelar = new Button("Cancelar");
        btnCancelar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 24; -fx-background-radius: 10;");
        btnCancelar.setOnMouseEntered(e -> btnCancelar.setStyle("-fx-background-color: #c0392b; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 24; -fx-background-radius: 10;"));
        btnCancelar.setOnMouseExited(e -> btnCancelar.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-padding: 10 24; -fx-background-radius: 10;"));

        HBox buttons = new HBox(20, btnFinalizar, btnCancelar);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10, 0, 0, 0));

        VBox root = new VBox(20, imageView, lblContador, buttons);
        root.setPadding(new Insets(25));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1a1a40;");

        Scene scene = new Scene(root, 720, 620);
        cameraStage.setScene(scene);
        cameraStage.setResizable(false);
        cameraStage.show();

        capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            showAlert("Erro", "Não foi possível abrir a webcam.");
            cameraStage.close();
            return;
        }

        CascadeClassifier faceDetector = new CascadeClassifier("libs/haarcascade_frontalface_default.xml");

        Thread thread = new Thread(() -> {
            Mat frame = new Mat();
            while (capture.isOpened()) {
                if (capture.read(frame)) {
                    Imgproc.cvtColor(frame, frame, Imgproc.COLOR_BGR2RGB); 
                    Mat frameClone = frame.clone();
                    javafx.scene.image.Image img = Utils.mat2Image(frameClone);
                    Platform.runLater(() -> imageView.setImage(img));

                    Mat gray = new Mat();
                    Imgproc.cvtColor(frame, gray, Imgproc.COLOR_RGB2GRAY);
                    MatOfRect faces = new MatOfRect();
                    faceDetector.detectMultiScale(gray, faces);

                    if (!faces.empty() && capturando) {
                        long tempoAtual = System.currentTimeMillis();
                        if (tempoAtual - ultimoTempoCaptura >= 1000) {
                            Rect face = faces.toArray()[0];
                            Mat somenteFace = new Mat(frame, face);
                            String caminho = salvarFoto(somenteFace, nomeCliente);
                            if (!caminho.isEmpty()) {
                                fotosCaminhos.add(caminho);
                                fotosCapturadas++;
                                ultimoTempoCaptura = tempoAtual;

                                Platform.runLater(() -> lblContador.setText("Fotos capturadas: " + fotosCapturadas + "/" + NUM_FOTOS_PARA_CAPTURA));

                                if (fotosCapturadas >= NUM_FOTOS_PARA_CAPTURA) {
                                    capturando = false;
                                    Platform.runLater(() -> btnFinalizar.setDisable(false));
                                }
                            }
                            somenteFace.release();
                        }
                    }

                    gray.release();
                }

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            frame.release();
        });
        thread.setDaemon(true);
        thread.start();

        btnFinalizar.setOnAction(e -> {
            capture.release();
            cameraStage.close();

            boolean sucesso = salvarClienteCSV(nomeCliente, telefoneCliente, emailCliente, cpfCliente, enderecoCliente, planoCliente, fotosCaminhos, acessoLiberado);
            if (sucesso) {
                showPopupMensagem("Cadastro Realizado", "Cliente cadastrado com sucesso!", true);
            } else {
                showPopupMensagem("Erro", "Falha ao salvar os dados do cliente.", false);
            }

        });

        btnCancelar.setOnAction(e -> {
            if (capture != null && capture.isOpened()) {
                capture.release();
            }
            apagarFotosCapturadas();
            cameraStage.close();
        });
    }

    
    private static void apagarFotosCapturadas() {
        for (String caminho : fotosCaminhos) {
            try {
                Files.deleteIfExists(Paths.get(caminho));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        fotosCaminhos.clear();
        fotosCapturadas = 0;
    }

    private static String salvarFoto(Mat somenteFace, String nomeCliente) {
        try {
            String pastaFotos = "clientes/fotos";
            Path dirPath = Paths.get(pastaFotos);
            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            String primeiroNome = nomeCliente.trim().split("\\s+")[0];

            String nomeArquivo = primeiroNome + "_" + UUID.randomUUID() + ".png";
            String caminhoCompleto = pastaFotos + "/" + nomeArquivo;

            Mat bgrFace = new Mat();
            Imgproc.cvtColor(somenteFace, bgrFace, Imgproc.COLOR_RGB2BGR);
            Imgcodecs.imwrite(caminhoCompleto, bgrFace);
            bgrFace.release();

            return caminhoCompleto;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    private static boolean salvarClienteCSV(String nome, String telefone, String email, String cpf, String endereco, String plano, List<String> caminhosFotos, boolean acessoLiberado) {
        StringBuilder sbFotos = new StringBuilder();
        for (int i = 0; i < caminhosFotos.size(); i++) {
            sbFotos.append(caminhosFotos.get(i));
            if (i < caminhosFotos.size() - 1) {
                sbFotos.append(", ");
            }
        }

        String acessoTexto = acessoLiberado ? "Sim" : "Não";
        String arquivoCSV = "clientes/clientes.csv";

        try {
            if (!Files.exists(Paths.get(arquivoCSV))) {
                String cabecalho = "\"Nome\",\"Telefone\",\"Email\",\"CPF\",\"Endereco\",\"Plano\",\"Fotos\",\"Acesso\"\n";
                Files.writeString(Paths.get(arquivoCSV), cabecalho, StandardOpenOption.CREATE);
            }

            String linha = String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"\n",
                    nome, telefone, email, cpf, endereco, plano, sbFotos.toString(), acessoTexto);

            Files.writeString(Paths.get(arquivoCSV), linha, StandardOpenOption.APPEND);
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static void showAlert(String titulo, String mensagem) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(titulo);
            alert.setHeaderText(null);
            alert.setContentText(mensagem);
            alert.showAndWait();
        });
    }
}
