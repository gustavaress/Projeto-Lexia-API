package br.com.fiap.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Simulacao {
    private int idSimulacao;
    private Usuario usuario;
    private LocalDateTime dataSimulacao;
    private String tipoSimulacao;
    private double valorFinal;

    //Construtores
    public Simulacao() {}

    public Simulacao(int idSimulacao, Usuario usuario, LocalDateTime dataSimulacao, String tipoSimulacao, double valorFinal) {
        this.idSimulacao = idSimulacao;
        this.usuario = usuario;
        this.dataSimulacao = dataSimulacao;
        this.tipoSimulacao = tipoSimulacao;
        this.valorFinal = valorFinal;
    }

    //Getters e Setters

    public int getIdSimulacao() {
        return idSimulacao;
    }

    public void setIdSimulacao(int idSimulacao) {
        this.idSimulacao = idSimulacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getDataSimulacao() {
        return dataSimulacao;
    }

    public void setDataSimulacao(LocalDateTime dataSimulacao) {
        this.dataSimulacao = dataSimulacao;
    }

    public String getTipoSimulacao() {
        return tipoSimulacao;
    }

    public void setTipoSimulacao(String tipoSimulacao) {
        this.tipoSimulacao = tipoSimulacao;
    }

    public double getValorFinal() {
        return valorFinal;
    }

    public void setValorFinal(double valorFinal) {
        this.valorFinal = valorFinal;
    }
    @Override
    public String toString() {
        return "Simulacao {" +
                ",\n  usuario=" + (usuario != null ? usuario.getNome() : "não informado") +
                ",\n  dataSimulacao=" + (dataSimulacao != null
                ? dataSimulacao.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))
                : "não informada") +
                ",\n  tipoSimulacao='" + tipoSimulacao + '\'' +
                ",\n  valorFinal=" + valorFinal +
                "\n}";
    }

}
