package br.com.fiap.service;

import br.com.fiap.dao.UsuarioDAO;
import br.com.fiap.dto.usuario.AtualizarUsuarioDto;
import br.com.fiap.dto.usuario.CadastrarUsuarioDto;
import br.com.fiap.dto.usuario.ListarUsuarioDto;
import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.exception.RegraNegocioException;
import br.com.fiap.mapper.UsuarioMapper;
import br.com.fiap.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;
    private final UsuarioMapper usuarioMapper;

    @Inject
    public UsuarioService(UsuarioDAO usuarioDAO, UsuarioMapper usuarioMapper) {
        this.usuarioDAO = usuarioDAO;
        this.usuarioMapper = usuarioMapper;
    }

    public ListarUsuarioDto cadastrar(CadastrarUsuarioDto dto)
            throws SQLException, EntidadeNaoEncontradaException, RegraNegocioException {

        Usuario usuario = usuarioMapper.toModel(dto);
        usuarioDAO.inserir(usuario);

        return usuarioMapper.toDto(usuario);
    }

    public List<ListarUsuarioDto> listarTodos() throws SQLException {
        return usuarioDAO.listarTodos()
                .stream()
                .map(usuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    public ListarUsuarioDto buscarPorId(int id)
            throws SQLException, EntidadeNaoEncontradaException {

        Usuario usuario = usuarioDAO.buscarPorCodigo(id);
        return usuarioMapper.toDto(usuario);
    }

    public ListarUsuarioDto atualizar(int id, AtualizarUsuarioDto dto)
            throws SQLException, EntidadeNaoEncontradaException {

        Usuario existente = usuarioDAO.buscarPorCodigo(id);

        usuarioMapper.toModel(dto, existente);
        usuarioDAO.atualizar(existente);

        return usuarioMapper.toDto(existente);
    }

    public void deletar(int id)
            throws SQLException, EntidadeNaoEncontradaException {

        usuarioDAO.deletar(id);
    }
}
