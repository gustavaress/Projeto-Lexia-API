package br.com.fiap.dto.usuario;

import jakarta.validation.constraints.*;
import java.util.List;

public class AtualizarUsuarioDto {

    @NotBlank(message = "O username é obrigatório.")
    private String username;

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 8, message = "A senha deve ter no mínimo 8 caracteres.")
    private String senha;

    @Positive(message = "O salário deve ser maior que zero.")
    private double salario;

    @NotBlank(message = "O tipo de contrato é obrigatório.")
    private String tipoContrato;

    @NotNull(message = "O ID da empresa é obrigatório.")
    private Integer idEmpresa;

    @NotEmpty(message = "A lista de IDs de endereço não pode ser vazia.")
    private List<Integer> idEnderecos;

    // Getters e Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public double getSalario() { return salario; }
    public void setSalario(double salario) { this.salario = salario; }

    public String getTipoContrato() { return tipoContrato; }
    public void setTipoContrato(String tipoContrato) { this.tipoContrato = tipoContrato; }

    public Integer getIdEmpresa() { return idEmpresa; }
    public void setIdEmpresa(Integer idEmpresa) { this.idEmpresa = idEmpresa; }

    public List<Integer> getIdEnderecos() { return idEnderecos; }
    public void setIdEnderecos(List<Integer> idEnderecos) { this.idEnderecos = idEnderecos; }
}
