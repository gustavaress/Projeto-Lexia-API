package br.com.fiap.service;

import br.com.fiap.dao.SimulacaoDAO;
import br.com.fiap.dto.simulacao.AtualizarSimulacaoDto;
import br.com.fiap.dto.simulacao.CadastrarSimulacaoDto;
import br.com.fiap.dto.simulacao.ListarSimulacaoDto;
import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.exception.RegraNegocioException;
import br.com.fiap.mapper.SimulacaoMapper;
import br.com.fiap.model.Simulacao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SimulacaoService {

    private final SimulacaoDAO simulacaoDAO;
    private final SimulacaoMapper simulacaoMapper;

    @Inject
    public SimulacaoService(SimulacaoDAO simulacaoDAO, SimulacaoMapper simulacaoMapper) {
        this.simulacaoDAO = simulacaoDAO;
        this.simulacaoMapper = simulacaoMapper;
    }

    public ListarSimulacaoDto cadastrar(CadastrarSimulacaoDto dto)
            throws SQLException, EntidadeNaoEncontradaException, RegraNegocioException {

        Simulacao simulacao = simulacaoMapper.toModel(dto);
        simulacaoDAO.inserir(simulacao);

        return simulacaoMapper.toDto(simulacao);
    }

    public List<ListarSimulacaoDto> listarTodos() throws SQLException, EntidadeNaoEncontradaException {
        return simulacaoDAO.listarTodos()
                .stream()
                .map(simulacaoMapper::toDto)
                .collect(Collectors.toList());
    }

    public ListarSimulacaoDto buscarPorId(int id)
            throws SQLException, EntidadeNaoEncontradaException {

        Simulacao simulacao = simulacaoDAO.buscarPorCodigo(id);
        return simulacaoMapper.toDto(simulacao);
    }

    public ListarSimulacaoDto atualizar(int id, AtualizarSimulacaoDto dto)
            throws SQLException, EntidadeNaoEncontradaException {

        Simulacao existente = simulacaoDAO.buscarPorCodigo(id);

        simulacaoMapper.toModel(dto, existente);
        simulacaoDAO.atualizar(existente);

        return simulacaoMapper.toDto(existente);
    }

    public void deletar(int id)
            throws SQLException, EntidadeNaoEncontradaException {

        simulacaoDAO.deletar(id);
    }
}
