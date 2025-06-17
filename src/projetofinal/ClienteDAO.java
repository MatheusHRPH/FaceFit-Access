package projetofinal;

import java.io.*;
import java.util.*;

public class ClienteDAO {
    private static final String ARQUIVO = "clientes.csv";

    public static List<Cliente> lerClientes() {
        List<Cliente> lista = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                Cliente c = Cliente.fromCSV(linha);
                if (c != null) {
                    lista.add(c);
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public static void salvarCliente(Cliente cliente) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO, true))) {
            bw.write(cliente.toCSV());
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
