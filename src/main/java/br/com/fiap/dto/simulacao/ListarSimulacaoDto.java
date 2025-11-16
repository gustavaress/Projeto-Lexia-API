package br.com.fiap.dto.simulacao;

public class ListarSimulacaoDto {

    private int idSimulacao;
    private String dataSimulacao;
    private String tipoSimulacao;
    private double valorFinal;

    private int idUsuario;
    private String nomeUsuario;
    private String emailUsuario;

    public ListarSimulacaoDto(int idSimulacao, String dataSimulacao, String tipoSimulacao, double valorFinal,
                              int idUsuario, String nomeUsuario, String emailUsuario) {

        this.idSimulacao = idSimulacao;
        this.dataSimulacao = dataSimulacao;
        this.tipoSimulacao = tipoSimulacao;
        this.valorFinal = valorFinal;
        this.idUsuario = idUsuario;
        this.nomeUsuario = nomeUsuario;
        this.emailUsuario = emailUsuario;
    }

    public int getIdSimulacao() { return idSimulacao; }
    public String getDataSimulacao() { return dataSimulacao; }
    public String getTipoSimulacao() { return tipoSimulacao; }
    public double getValorFinal() { return valorFinal; }

    public int getIdUsuario() { return idUsuario; }
    public String getNomeUsuario() { return nomeUsuario; }
    public String getEmailUsuario() { return emailUsuario; }
}
