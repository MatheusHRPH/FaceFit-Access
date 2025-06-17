package projetofinal;

public class Cliente {
    private String nome;
    private String cpf;
    private String dataNascimento;
    private String tipoPlano;
    private String codigoImagem;

    public Cliente(String nome, String cpf, String dataNascimento, String tipoPlano, String codigoImagem) {
        this.nome = nome;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.tipoPlano = tipoPlano;
        this.codigoImagem = codigoImagem;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public String getTipoPlano() {
        return tipoPlano;
    }

    public String getCodigoImagem() {
        return codigoImagem;
    }

    public String toCSV() {
        return String.format("%s,%s,%s,%s,%s", nome, cpf, dataNascimento, tipoPlano, codigoImagem);
    }

    public static Cliente fromCSV(String linha) {
        String[] partes = linha.split(",");
        if (partes.length < 5) {
            return null;
        }
        return new Cliente(partes[0], partes[1], partes[2], partes[3], partes[4]);
    }
}
