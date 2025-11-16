package br.com.fiap.mapper;

import br.com.fiap.dao.UsuarioDAO;
import br.com.fiap.dto.simulacao.AtualizarSimulacaoDto;
import br.com.fiap.dto.simulacao.CadastrarSimulacaoDto;
import br.com.fiap.dto.simulacao.ListarSimulacaoDto;
import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.model.Simulacao;
import br.com.fiap.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class SimulacaoMapper {

    @Inject
    UsuarioDAO usuarioDAO;

    public Simulacao toModel(CadastrarSimulacaoDto dto)
            throws EntidadeNaoEncontradaException, SQLException {

        Usuario usuario = usuarioDAO.buscarPorCodigo(dto.getIdUsuario());

        Simulacao simulacao = new Simulacao();
        simulacao.setUsuario(usuario);
        simulacao.setTipoSimulacao(dto.getTipoSimulacao());
        simulacao.setValorFinal(dto.getValorFinal());
        simulacao.setDataSimulacao(LocalDateTime.now());

        return simulacao;
    }

    public Simulacao toModel(AtualizarSimulacaoDto dto, Simulacao existente)
            throws EntidadeNaoEncontradaException, SQLException {

        Usuario usuario = usuarioDAO.buscarPorCodigo(dto.getIdUsuario());

        existente.setUsuario(usuario);
        existente.setTipoSimulacao(dto.getTipoSimulacao());
        existente.setValorFinal(dto.getValorFinal());

        return existente;
    }

    public ListarSimulacaoDto toDto(Simulacao simulacao) {
        String dataFormatada = simulacao.getDataSimulacao()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        return new ListarSimulacaoDto(
                simulacao.getIdSimulacao(),
                dataFormatada,
                simulacao.getTipoSimulacao(),
                simulacao.getValorFinal(),
                simulacao.getUsuario().getIdUsuario(),
                simulacao.getUsuario().getNome(),
                simulacao.getUsuario().getEmail()
        );
    }
}
