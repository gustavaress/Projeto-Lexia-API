package br.com.fiap.dto.usuario;

import br.com.fiap.dto.empresa.ListarEmpresaDto;
import br.com.fiap.dto.endereco.ListarEnderecoDto;
import br.com.fiap.dto.simulacao.ListarSimulacaoDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListarUsuarioDto {

    private int idUsuario;
    private String username;
    private double salario;
    private String tipoContrato;
    private ListarEmpresaDto empresa;
    private List<ListarEnderecoDto> enderecos;
    private List<ListarSimulacaoDto> simulacoes;

    public ListarUsuarioDto(int idUsuario, String username, double salario, String tipoContrato, ListarEmpresaDto empresa, List<ListarEnderecoDto> enderecos, List<ListarSimulacaoDto> simulacoes) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.salario = salario;
        this.tipoContrato = tipoContrato;
        this.empresa = empresa;
        this.enderecos = enderecos;
        this.simulacoes = simulacoes;
    }

    // Getters
    public int getIdUsuario() { return idUsuario; }
    public String getUsername() { return username; }
    public double getSalario() { return salario; }
    public String getTipoContrato() { return tipoContrato; }
    public ListarEmpresaDto getEmpresa() { return empresa; }
    public List<ListarEnderecoDto> getEnderecos() { return enderecos; }
    public List<ListarSimulacaoDto> getSimulacoes() { return simulacoes; }
}
