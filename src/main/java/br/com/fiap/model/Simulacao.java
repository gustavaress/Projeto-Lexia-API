package br.com.fiap.model;

import java.time.LocalDateTime;
import java.util.List;

public class Simulacao {
    private int idSimulacao;
    private LocalDateTime dataSimulacao;
    private String tipoSimulacao;
    private double valorFinal;
    private List<Usuario> usuarios;

    public Simulacao() {
    }

    // Getters e Setters
    public int getIdSimulacao() {
        return idSimulacao;
    }

    public void setIdSimulacao(int idSimulacao) {
        this.idSimulacao = idSimulacao;
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

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
}
