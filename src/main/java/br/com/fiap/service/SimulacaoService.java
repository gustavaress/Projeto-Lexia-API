package br.com.fiap.service;

import br.com.fiap.dao.SimulacaoDAO;
import br.com.fiap.dao.UsuarioDAO;
import br.com.fiap.dto.simulacao.AtualizarSimulacaoDto;
import br.com.fiap.dto.simulacao.CadastrarSimulacaoDto;
import br.com.fiap.dto.simulacao.ListarSimulacaoDto;
import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.mapper.SimulacaoMapper;
import br.com.fiap.model.Simulacao;
import br.com.fiap.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class SimulacaoService {

    @Inject
    private SimulacaoDAO simulacaoDAO;

    @Inject
    private UsuarioDAO usuarioDAO;

    @Inject
    private SimulacaoMapper simulacaoMapper;

    @Transactional
    public ListarSimulacaoDto cadastrar(CadastrarSimulacaoDto dto) throws SQLException, EntidadeNaoEncontradaException {
        Simulacao simulacao = simulacaoMapper.toModel(dto);

        List<Usuario> usuarios = new ArrayList<>();
        for (Integer id : dto.getIdUsuarios()) {
            usuarios.add(usuarioDAO.buscarPorId(id));
        }
        simulacao.setUsuarios(usuarios);

        simulacaoDAO.inserir(simulacao);
        return simulacaoMapper.toDto(simulacao);
    }

    public List<ListarSimulacaoDto> listarTodos() throws SQLException {
        return simulacaoDAO.listarTodos().stream().map(simulacaoMapper::toDto).collect(Collectors.toList());
    }

    public ListarSimulacaoDto buscarPorId(int id) throws SQLException, EntidadeNaoEncontradaException {
        Simulacao simulacao = simulacaoDAO.buscarPorId(id);
        return simulacaoMapper.toDto(simulacao);
    }

    @Transactional
    public ListarSimulacaoDto atualizar(int id, AtualizarSimulacaoDto dto) throws SQLException, EntidadeNaoEncontradaException {
        Simulacao existente = simulacaoDAO.buscarPorId(id);
        simulacaoMapper.toModel(dto, existente);

        List<Usuario> usuarios = new ArrayList<>();
        for (Integer idUsuario : dto.getIdUsuarios()) {
            usuarios.add(usuarioDAO.buscarPorId(idUsuario));
        }
        existente.setUsuarios(usuarios);

        simulacaoDAO.atualizar(existente);
        return simulacaoMapper.toDto(existente);
    }

    @Transactional
    public void deletar(int id) throws SQLException, EntidadeNaoEncontradaException {
        simulacaoDAO.buscarPorId(id);
        simulacaoDAO.deletar(id);
    }
}
