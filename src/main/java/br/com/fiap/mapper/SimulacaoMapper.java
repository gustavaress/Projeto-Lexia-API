package br.com.fiap.mapper;

import br.com.fiap.dto.simulacao.AtualizarSimulacaoDto;
import br.com.fiap.dto.simulacao.CadastrarSimulacaoDto;
import br.com.fiap.dto.simulacao.ListarSimulacaoDto;
import br.com.fiap.dto.usuario.ListarUsuarioSimplesDto;
import br.com.fiap.model.Simulacao;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@ApplicationScoped
public class SimulacaoMapper {

    public Simulacao toModel(CadastrarSimulacaoDto dto) {
        Simulacao simulacao = new Simulacao();
        simulacao.setTipoSimulacao(dto.getTipoSimulacao());
        simulacao.setValorFinal(dto.getValorFinal());
        simulacao.setDataSimulacao(LocalDateTime.now());
        return simulacao;
    }

    public void toModel(AtualizarSimulacaoDto dto, Simulacao simulacao) {
        simulacao.setTipoSimulacao(dto.getTipoSimulacao());
        simulacao.setValorFinal(dto.getValorFinal());
    }

    public ListarSimulacaoDto toDto(Simulacao simulacao) {
        if (simulacao == null) return null;
        String dataFormatada = simulacao.getDataSimulacao() != null ? simulacao.getDataSimulacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : null;
        return new ListarSimulacaoDto(
                simulacao.getIdSimulacao(),
                dataFormatada,
                simulacao.getTipoSimulacao(),
                simulacao.getValorFinal(),
                simulacao.getUsuarios() != null ? simulacao.getUsuarios().stream().map(u -> new ListarUsuarioSimplesDto(u.getIdUsuario(), u.getUsername())).collect(Collectors.toList()) : null
        );
    }
}
