package br.com.fiap.dto.empresa;

import br.com.fiap.dto.endereco.ListarEnderecoDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListarEmpresaDto {

    private int idEmpresa;
    private String nomeFantasia;
    private String cnpj;
    private String email;
    private String telefone;
    private List<ListarEnderecoDto> enderecos;

    public ListarEmpresaDto(int idEmpresa, String nomeFantasia, String cnpj, String email, String telefone, List<ListarEnderecoDto> enderecos) {
        this.idEmpresa = idEmpresa;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
        this.enderecos = enderecos;
    }

    // Getters
    public int getIdEmpresa() { return idEmpresa; }
    public String getNomeFantasia() { return nomeFantasia; }
    public String getCnpj() { return cnpj; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public List<ListarEnderecoDto> getEnderecos() { return enderecos; }
}
