package br.com.fiap.dto.simulacao;

import br.com.fiap.dto.usuario.ListarUsuarioSimplesDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListarSimulacaoDto {

    private int idSimulacao;
    private String dataSimulacao;
    private String tipoSimulacao;
    private double valorFinal;
    private List<ListarUsuarioSimplesDto> usuarios;

    public ListarSimulacaoDto(int idSimulacao, String dataSimulacao, String tipoSimulacao, double valorFinal, List<ListarUsuarioSimplesDto> usuarios) {
        this.idSimulacao = idSimulacao;
        this.dataSimulacao = dataSimulacao;
        this.tipoSimulacao = tipoSimulacao;
        this.valorFinal = valorFinal;
        this.usuarios = usuarios;
    }

    // Getters
    public int getIdSimulacao() { return idSimulacao; }
    public String getDataSimulacao() { return dataSimulacao; }
    public String getTipoSimulacao() { return tipoSimulacao; }
    public double getValorFinal() { return valorFinal; }
    public List<ListarUsuarioSimplesDto> getUsuarios() { return usuarios; }
}
