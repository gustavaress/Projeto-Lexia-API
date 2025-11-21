package br.com.fiap.dto.empresa;

import jakarta.validation.constraints.*;
import java.util.List;

public class CadastrarEmpresaDto {

    @NotBlank(message = "O nome fantasia é obrigatório.")
    private String nomeFantasia;

    @NotBlank(message = "O CNPJ é obrigatório.")
    private String cnpj;

    @Email(message = "E-mail inválido.")
    @NotBlank(message = "O e-mail é obrigatório.")
    private String email;

    @NotBlank(message = "O telefone é obrigatório.")
    private String telefone;

    @NotEmpty(message = "A lista de IDs de endereço não pode ser vazia.")
    private List<Integer> idEnderecos;

    // Getters e Setters
    public String getNomeFantasia() { return nomeFantasia; }
    public void setNomeFantasia(String nomeFantasia) { this.nomeFantasia = nomeFantasia; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public List<Integer> getIdEnderecos() { return idEnderecos; }
    public void setIdEnderecos(List<Integer> idEnderecos) { this.idEnderecos = idEnderecos; }
}
