package projetofinal;

import java.awt.Color;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;

import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import org.opencv.objdetect.CascadeClassifier;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.*;
import java.util.*;

public class ReconhecimentoCliente {

    private static final Size TAMANHO_PADRAO = new Size(100, 100);

    public static class Cliente {
        String nome;
        boolean acessoLiberado;

        public Cliente(String nome, boolean acessoLiberado) {
            this.nome = nome;
            this.acessoLiberado = acessoLiberado;
        }
    }

    private VideoCapture camera;
    private JFrame cameraFrame;
    private JLabel videoLabel;

    private Map<String, Cliente> fotoParaClienteMap;

    public void run() {
        fotoParaClienteMap = carregarClientesCSV("clientes/clientes.csv");
        if (fotoParaClienteMap.isEmpty()) {
            System.err.println("Nenhum cliente carregado do CSV.");
            return;
        }

        CascadeClassifier detector = new CascadeClassifier("libs/haarcascade_frontalface_default.xml");
        if (detector.empty()) {
            System.err.println("Erro ao carregar Haar Cascade.");
            return;
        }

        File pastaFotos = new File("clientes/fotos");
        File[] arquivosFotos = pastaFotos.listFiles();
        if (arquivosFotos == null || arquivosFotos.length == 0) {
            System.err.println("Nenhuma foto encontrada em clientes/fotos.");
            return;
        }

        List<Mat> rostosSalvos = new ArrayList<>();
        List<String> nomesFotos = new ArrayList<>(); 

        for (File arquivo : arquivosFotos) {
            if (arquivo.isFile()) {
                Mat img = Imgcodecs.imread(arquivo.getAbsolutePath(), Imgcodecs.IMREAD_GRAYSCALE);
                if (!img.empty()) {
                    Mat imgRedimensionada = new Mat();
                    Imgproc.resize(img, imgRedimensionada, TAMANHO_PADRAO);
                    rostosSalvos.add(imgRedimensionada);

                    String nomeArquivo = arquivo.getName(); 
                    nomesFotos.add("clientes/fotos/" + nomeArquivo); 
                    
                }
            }
        }

        if (rostosSalvos.isEmpty()) {
            System.err.println("Nenhum rosto carregado.");
            return;
        }

        camera = new VideoCapture(0);
        if (!camera.isOpened()) {
            System.err.println("Erro ao abrir webcam.");
            return;
        }

        SwingUtilities.invokeLater(() -> {
            cameraFrame = new JFrame("Reconhecimento Facial");
            cameraFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            videoLabel = new JLabel();
            cameraFrame.getContentPane().add(videoLabel);
            cameraFrame.setSize(640, 480);
            cameraFrame.setLocationRelativeTo(null);

            cameraFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    stopCamera();
                }
            });

            cameraFrame.setVisible(true);
        });

        Mat frame = new Mat();
        final boolean[] reconhecido = {false};

        while (!reconhecido[0]) {
            if (!camera.read(frame)) {
                System.err.println("Erro ao capturar frame.");
                break;
            }

            Mat frameCinza = new Mat();
            Imgproc.cvtColor(frame, frameCinza, Imgproc.COLOR_BGR2GRAY);

            MatOfRect facesDetectadas = new MatOfRect();
            detector.detectMultiScale(frameCinza, facesDetectadas, 1.1, 4, 0, new Size(100, 100), new Size());

            for (Rect face : facesDetectadas.toArray()) {
                Mat rostoAtual = new Mat(frameCinza, face);

                Mat rostoRedimensionado = new Mat();
                Imgproc.resize(rostoAtual, rostoRedimensionado, TAMANHO_PADRAO);

                String nomeReconhecido = "desconhecido";
                double melhorSimilaridade = 0;

                Mat histRostoAtual = computaHistograma(rostoRedimensionado);

                for (int i = 0; i < rostosSalvos.size(); i++) {
                    Mat histSalvo = computaHistograma(rostosSalvos.get(i));
                    double similaridade = Imgproc.compareHist(histRostoAtual, histSalvo, Imgproc.CV_COMP_CORREL);

                    if (similaridade > 0.8 && similaridade > melhorSimilaridade) {
                        melhorSimilaridade = similaridade;
                        nomeReconhecido = nomesFotos.get(i); 
                    }
                }

                Scalar cor = nomeReconhecido.equals("desconhecido") ? new Scalar(0, 0, 255) : new Scalar(0, 255, 0);
                Imgproc.rectangle(frame, face, cor, 2);

                String nomeBase = nomeReconhecido.equals("desconhecido") ? "desconhecido" :
                    new File(nomeReconhecido).getName().split("_")[0]; 

                Imgproc.putText(frame, nomeBase,
                        new Point(face.x, face.y - 10),
                        Imgproc.FONT_HERSHEY_SIMPLEX,
                        0.9, cor, 2);

                if (!nomeReconhecido.equals("desconhecido")) {
                    reconhecido[0] = true;
                    final String caminhoFotoReconhecida = nomeReconhecido;

                    Cliente clienteReconhecido = fotoParaClienteMap.get(caminhoFotoReconhecida.toLowerCase());

                    
                    if (clienteReconhecido != null) {
                        
                    } else {
                        
                    }

                    SwingUtilities.invokeLater(() -> {
                        stopCamera();
                        if (clienteReconhecido != null && clienteReconhecido.acessoLiberado) {
                            mostrarPopup(clienteReconhecido.nome, true);
                        } else {
                            mostrarPopup(clienteReconhecido != null ? clienteReconhecido.nome : nomeBase, false);
                        }
                    });

                    break;
                }
            }

            if (!reconhecido[0]) {
                BufferedImage image = matToBufferedImage(frame);
                SwingUtilities.invokeLater(() -> videoLabel.setIcon(new ImageIcon(image)));
            }

            frameCinza.release();

            try {
                Thread.sleep(33);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        frame.release();
    }

    private void stopCamera() {
        if (camera != null && camera.isOpened()) {
            camera.release();
        }
        if (cameraFrame != null) {
            cameraFrame.dispose();
        }
    }

    private static Mat computaHistograma(Mat imagem) {
        List<Mat> imagens = new ArrayList<>();
        imagens.add(imagem);

        Mat hist = new Mat();
        Imgproc.calcHist(imagens, new MatOfInt(0), new Mat(), hist, new MatOfInt(256), new MatOfFloat(0, 256));
        Core.normalize(hist, hist);
        return hist;
    }

    private static void mostrarPopup(String nome, boolean acessoLiberado) {
       SwingUtilities.invokeLater(() -> {
           final JDialog dialog = new JDialog();
           dialog.setAlwaysOnTop(true);
           dialog.setModal(false);
           dialog.setUndecorated(true);

           String primeiroNome = nome.split(" ")[0];

           JLabel label;
           if (acessoLiberado) {
               label = new JLabel("Bem-vindo, " + primeiroNome + "! Acesso Liberado.");
               label.setForeground(new Color(0, 128, 0));
           } else {
               label = new JLabel("Acesso Negado, " + primeiroNome + "! Verificar na Recepção");
               label.setForeground(Color.RED);
           }
           label.setFont(label.getFont().deriveFont(20f));
           label.setHorizontalAlignment(SwingConstants.CENTER);
           label.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

           dialog.getContentPane().add(label);
           dialog.pack();
           dialog.setLocationRelativeTo(null);
           dialog.setVisible(true);

           new javax.swing.Timer(3000, e -> dialog.dispose()).start();
       });
   }


    private static BufferedImage matToBufferedImage(Mat matrix) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (matrix.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = matrix.channels() * matrix.cols() * matrix.rows();
        byte[] b = new byte[bufferSize];
        matrix.get(0, 0, b);
        BufferedImage image = new BufferedImage(matrix.cols(), matrix.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    private static Map<String, Cliente> carregarClientesCSV(String caminhoCsv) {
        Map<String, Cliente> mapaFotosParaCliente = new HashMap<>();

        File csvFile = new File(caminhoCsv);
        if (!csvFile.exists()) {
            System.err.println("Arquivo CSV não encontrado: " + caminhoCsv);
            return mapaFotosParaCliente;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false; 
                }
                String[] campos = linha.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

                if (campos.length >= 8) {
                    String nome = campos[0].replaceAll("^\"|\"$", "").trim();
                    String fotosStr = campos[6].replaceAll("^\"|\"$", "").trim();
                    String acessoStr = campos[7].replaceAll("^\"|\"$", "").trim();
                    boolean acesso = acessoStr.equalsIgnoreCase("true") || acessoStr.equalsIgnoreCase("sim");

                    Cliente cliente = new Cliente(nome, acesso);

                    String[] fotos = fotosStr.split(",");
                    for (String foto : fotos) {
                        foto = foto.trim().toLowerCase(); 
                        mapaFotosParaCliente.put(foto, cliente);
                    }                 
                }                               
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mapaFotosParaCliente;
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        new ReconhecimentoCliente().run();
    }
}
