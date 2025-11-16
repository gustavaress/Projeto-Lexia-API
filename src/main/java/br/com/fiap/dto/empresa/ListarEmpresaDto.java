package br.com.fiap.dto.empresa;

import br.com.fiap.dto.endereco.ListarEnderecoDto;

public class ListarEmpresaDto {

    private int idEmpresa;
    private String nomeFantasia;
    private String cnpj;
    private String email;
    private String telefone;
    private ListarEnderecoDto endereco;

    public ListarEmpresaDto(int idEmpresa, String nomeFantasia, String cnpj,
                            String email, String telefone, ListarEnderecoDto endereco) {
        this.idEmpresa = idEmpresa;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    public int getIdEmpresa() { return idEmpresa; }
    public String getNomeFantasia() { return nomeFantasia; }
    public String getCnpj() { return cnpj; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public ListarEnderecoDto getEndereco() { return endereco; }
}
