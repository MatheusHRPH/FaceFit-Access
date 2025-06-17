package projetofinal;

import java.io.File;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class ProjetoFinal extends Application {

    @Override
    public void start(Stage primaryStage) {
        System.load(new File("libs/opencv_java4110.dll").getAbsolutePath());
        String userDir = System.getProperty("user.dir");
        String dllPath = Paths.get(userDir, "libs", "opencv_java4110.dll").toString();
        System.load(dllPath);
        System.out.println("OpenCV carregado com sucesso!");

        Label lblTitulo = new Label("FaceFit Access");
        lblTitulo.setStyle(
            "-fx-font-size: 28px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #f1c40f;" +
            "-fx-padding: 0 0 8 0;"
        );

        Label lblSubtitulo = new Label("Sistema de Reconhecimento Facial");
        lblSubtitulo.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: #dcdde1;" +
            "-fx-padding: 0 0 25 0;"
        );

        Button btnCadastro = new Button("Cadastrar Cliente");
        Button btnVisualizar = new Button("Visualizar Clientes");
        Button btnReconhecimento = new Button("Iniciar Reconhecimento");
        Button btnSair = new Button("Sair");

        String estiloBotaoAmarelo = ""
            + "-fx-background-color: #f1c40f;"
            + "-fx-text-fill: #1a1a40;"
            + "-fx-font-size: 16px;"
            + "-fx-font-weight: bold;"
            + "-fx-padding: 12 24 12 24;"
            + "-fx-background-radius: 8;"
            + "-fx-cursor: hand;"
            + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 4, 0, 0, 1);";

        String hoverBotao = "-fx-background-color: #f39c12;";

        String estiloSair = "-fx-background-color: #e74c3c;" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 12 24 12 24;" +
            "-fx-background-radius: 8;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 4, 0, 0, 1);";

        String estiloSairHover = "-fx-background-color: #c0392b;";

        btnCadastro.setStyle(estiloBotaoAmarelo);
        btnVisualizar.setStyle(estiloBotaoAmarelo);
        btnReconhecimento.setStyle(estiloBotaoAmarelo);
        btnSair.setStyle(estiloSair);

        setHoverEffect(btnCadastro, estiloBotaoAmarelo, hoverBotao);
        setHoverEffect(btnVisualizar, estiloBotaoAmarelo, hoverBotao);
        setHoverEffect(btnReconhecimento, estiloBotaoAmarelo, hoverBotao);
        setHoverEffect(btnSair, estiloSair, estiloSairHover);

        btnCadastro.setMaxWidth(Double.MAX_VALUE);
        btnVisualizar.setMaxWidth(Double.MAX_VALUE);
        btnReconhecimento.setMaxWidth(Double.MAX_VALUE);
        btnSair.setMaxWidth(Double.MAX_VALUE);

        btnCadastro.setOnAction(e -> CadastroCliente.showFormAndRun(primaryStage));
        btnVisualizar.setOnAction(e -> VisualizarClientesApp.showWindow());
        btnReconhecimento.setOnAction(e -> {
            ReconhecimentoCliente rc = new ReconhecimentoCliente();
            rc.run();
        });
        btnSair.setOnAction(e -> {
            Platform.exit();
            System.exit(0);
        });

        VBox root = new VBox(16, lblTitulo, lblSubtitulo, btnCadastro, btnVisualizar, btnReconhecimento, btnSair);
        root.setAlignment(Pos.CENTER);
        root.setStyle(
            "-fx-padding: 40;" +
            "-fx-background-color: #1a1a40;"  
        );

        Scene scene = new Scene(root, 500, 400);
        primaryStage.setTitle("FaceFit Access - Menu Principal");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void setHoverEffect(Button button, String normalStyle, String hoverStyle) {
        button.setOnMouseEntered(e -> button.setStyle(normalStyle + hoverStyle));
        button.setOnMouseExited(e -> button.setStyle(normalStyle));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
