package br.com.fiap.dto.simulacao;

import jakarta.validation.constraints.*;
import java.util.List;

public class CadastrarSimulacaoDto {

    @NotBlank(message = "O tipo de simulação é obrigatório.")
    private String tipoSimulacao;

    @Positive(message = "O valor final deve ser maior que zero.")
    private double valorFinal;

    @NotEmpty(message = "A lista de IDs de usuário não pode ser vazia.")
    private List<Integer> idUsuarios;

    // Getters e Setters
    public String getTipoSimulacao() { return tipoSimulacao; }
    public void setTipoSimulacao(String tipoSimulacao) { this.tipoSimulacao = tipoSimulacao; }

    public double getValorFinal() { return valorFinal; }
    public void setValorFinal(double valorFinal) { this.valorFinal = valorFinal; }

    public List<Integer> getIdUsuarios() { return idUsuarios; }
    public void setIdUsuarios(List<Integer> idUsuarios) { this.idUsuarios = idUsuarios; }
}
