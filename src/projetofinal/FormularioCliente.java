package projetofinal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FormularioCliente extends JFrame {

    private JTextField nomeField = new JTextField(20);
    private JTextField cpfField = new JTextField(14);
    private JTextField dataNascimentoField = new JTextField(10);
    private JComboBox<String> tipoPlanoBox = new JComboBox<>(new String[]{"Básico", "Premium", "VIP"});
    private JTextField codigoImagemField = new JTextField(20);

    private JButton salvarBtn = new JButton("Salvar");

    public FormularioCliente() {
        setTitle("Cadastro de Cliente");
        setLayout(new GridLayout(6, 2, 5, 5));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);

        add(new JLabel("Nome:"));
        add(nomeField);

        add(new JLabel("CPF:"));
        add(cpfField);

        add(new JLabel("Data de Nascimento (AAAA-MM-DD):"));
        add(dataNascimentoField);

        add(new JLabel("Tipo de Plano:"));
        add(tipoPlanoBox);

        add(new JLabel("Código da Imagem:"));
        add(codigoImagemField);

        add(new JLabel());
        add(salvarBtn);

        salvarBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                salvarCliente();
            }
        });
    }

    private void salvarCliente() {
        String nome = nomeField.getText().trim();
        String cpf = cpfField.getText().trim();
        String dataNascimento = dataNascimentoField.getText().trim();
        String tipoPlano = (String) tipoPlanoBox.getSelectedItem();
        String codigoImagem = codigoImagemField.getText().trim();

        if (nome.isEmpty() || cpf.isEmpty() || dataNascimento.isEmpty() || codigoImagem.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios.");
            return;
        }

        Cliente cliente = new Cliente(nome, cpf, dataNascimento, tipoPlano, codigoImagem);
        ClienteDAO.salvarCliente(cliente);
        JOptionPane.showMessageDialog(this, "Cliente salvo com sucesso!");
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormularioCliente().setVisible(true);
        });
    }
}
