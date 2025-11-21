package br.com.fiap.service;

import br.com.fiap.dao.EmpresaDAO;
import br.com.fiap.dao.EnderecoDAO;
import br.com.fiap.dao.UsuarioDAO;
import br.com.fiap.dto.usuario.AtualizarUsuarioDto;
import br.com.fiap.dto.usuario.CadastrarUsuarioDto;
import br.com.fiap.dto.usuario.ListarUsuarioDto;
import br.com.fiap.exception.CampoJaCadastradoException;
import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.mapper.UsuarioMapper;
import br.com.fiap.model.Empresa;
import br.com.fiap.model.Endereco;
import br.com.fiap.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UsuarioService {

    @Inject
    private UsuarioDAO usuarioDAO;

    @Inject
    private EmpresaDAO empresaDAO;

    @Inject
    private EnderecoDAO enderecoDAO;

    @Inject
    private UsuarioMapper usuarioMapper;

    @Transactional
    public ListarUsuarioDto cadastrar(CadastrarUsuarioDto dto) throws SQLException, EntidadeNaoEncontradaException, CampoJaCadastradoException {
        Usuario usuario = usuarioMapper.toModel(dto);

        Empresa empresa = empresaDAO.buscarPorId(dto.getIdEmpresa());
        usuario.setEmpresa(empresa);

        List<Endereco> enderecos = new ArrayList<>();
        for (Integer id : dto.getIdEnderecos()) {
            enderecos.add(enderecoDAO.buscarPorId(id));
        }
        usuario.setEnderecos(enderecos);

        usuarioDAO.inserir(usuario);
        return usuarioMapper.toDto(usuario);
    }

    public List<ListarUsuarioDto> listarTodos() throws SQLException {
        return usuarioDAO.listarTodos().stream().map(usuarioMapper::toDto).collect(Collectors.toList());
    }

    public ListarUsuarioDto buscarPorId(int id) throws SQLException, EntidadeNaoEncontradaException {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        return usuarioMapper.toDto(usuario);
    }

    @Transactional
    public ListarUsuarioDto atualizar(int id, AtualizarUsuarioDto dto) throws SQLException, EntidadeNaoEncontradaException, CampoJaCadastradoException {
        Usuario existente = usuarioDAO.buscarPorId(id);
        usuarioMapper.toModel(dto, existente);

        Empresa empresa = empresaDAO.buscarPorId(dto.getIdEmpresa());
        existente.setEmpresa(empresa);

        List<Endereco> enderecos = new ArrayList<>();
        for (Integer idEndereco : dto.getIdEnderecos()) {
            enderecos.add(enderecoDAO.buscarPorId(idEndereco));
        }
        existente.setEnderecos(enderecos);

        usuarioDAO.atualizar(existente);
        return usuarioMapper.toDto(existente);
    }

    @Transactional
    public void deletar(int id) throws SQLException, EntidadeNaoEncontradaException {
        usuarioDAO.buscarPorId(id);
        usuarioDAO.deletar(id);
    }
}
