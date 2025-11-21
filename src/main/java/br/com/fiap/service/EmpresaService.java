package br.com.fiap.service;

import br.com.fiap.dao.EmpresaDAO;
import br.com.fiap.dao.EnderecoDAO;
import br.com.fiap.dto.empresa.AtualizarEmpresaDto;
import br.com.fiap.dto.empresa.CadastrarEmpresaDto;
import br.com.fiap.dto.empresa.ListarEmpresaDto;
import br.com.fiap.exception.CampoJaCadastradoException;
import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.mapper.EmpresaMapper;
import br.com.fiap.model.Empresa;
import br.com.fiap.model.Endereco;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EmpresaService {

    @Inject
    private EmpresaDAO empresaDAO;

    @Inject
    private EnderecoDAO enderecoDAO;

    @Inject
    private EmpresaMapper empresaMapper;

    @Transactional
    public ListarEmpresaDto cadastrar(CadastrarEmpresaDto dto) throws SQLException, EntidadeNaoEncontradaException, CampoJaCadastradoException {
        Empresa empresa = empresaMapper.toModel(dto);
        
        List<Endereco> enderecos = new ArrayList<>();
        for (Integer id : dto.getIdEnderecos()) {
            enderecos.add(enderecoDAO.buscarPorId(id));
        }
        empresa.setEnderecos(enderecos);
        
        empresaDAO.inserir(empresa);
        return empresaMapper.toDto(empresa);
    }

    public List<ListarEmpresaDto> listarTodos() throws SQLException {
        return empresaDAO.listarTodos().stream().map(empresaMapper::toDto).collect(Collectors.toList());
    }

    public ListarEmpresaDto buscarPorId(int id) throws SQLException, EntidadeNaoEncontradaException {
        Empresa empresa = empresaDAO.buscarPorId(id);
        return empresaMapper.toDto(empresa);
    }

    @Transactional
    public ListarEmpresaDto atualizar(int id, AtualizarEmpresaDto dto) throws SQLException, EntidadeNaoEncontradaException, CampoJaCadastradoException {
        Empresa existente = empresaDAO.buscarPorId(id);
        empresaMapper.toModel(dto, existente);

        List<Endereco> enderecos = new ArrayList<>();
        for (Integer idEndereco : dto.getIdEnderecos()) {
            enderecos.add(enderecoDAO.buscarPorId(idEndereco));
        }
        existente.setEnderecos(enderecos);

        empresaDAO.atualizar(existente);
        return empresaMapper.toDto(existente);
    }

    @Transactional
    public void deletar(int id) throws SQLException, EntidadeNaoEncontradaException {
        empresaDAO.buscarPorId(id);
        empresaDAO.deletar(id);
    }
}
