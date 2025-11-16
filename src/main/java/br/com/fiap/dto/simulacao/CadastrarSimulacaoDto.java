package br.com.fiap.dto.simulacao;

import jakarta.validation.constraints.*;

public class CadastrarSimulacaoDto {

    @NotNull(message = "O ID do usuário é obrigatório.")
    private Integer idUsuario;

    @NotBlank(message = "O tipo de simulação é obrigatório.")
    private String tipoSimulacao;

    @Positive(message = "O valor final deve ser maior que zero.")
    private double valorFinal;

    // Getters e Setters
    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
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
}
