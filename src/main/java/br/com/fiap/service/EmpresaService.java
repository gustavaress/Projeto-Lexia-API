package br.com.fiap.service;

import br.com.fiap.dao.EmpresaDAO;
import br.com.fiap.dto.empresa.CadastrarEmpresaDto;
import br.com.fiap.dto.empresa.AtualizarEmpresaDto;
import br.com.fiap.dto.empresa.ListarEmpresaDto;
import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.exception.RegraNegocioException;
import br.com.fiap.mapper.EmpresaMapper;
import br.com.fiap.model.Empresa;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EmpresaService {

    private final EmpresaDAO empresaDAO;
    private final EmpresaMapper empresaMapper;

    @Inject
    public EmpresaService(EmpresaDAO empresaDAO, EmpresaMapper empresaMapper) {
        this.empresaDAO = empresaDAO;
        this.empresaMapper = empresaMapper;
    }

    public ListarEmpresaDto cadastrar(CadastrarEmpresaDto dto)
            throws SQLException, RegraNegocioException, EntidadeNaoEncontradaException {

        Empresa empresa = empresaMapper.toModel(dto);
        empresaDAO.inserir(empresa);

        return empresaMapper.toDto(empresa);
    }

    public List<ListarEmpresaDto> listarTodas() throws SQLException {
        return empresaDAO.listarTodos()
                .stream()
                .map(empresaMapper::toDto)
                .collect(Collectors.toList());
    }

    public ListarEmpresaDto buscarPorId(int id)
            throws SQLException, EntidadeNaoEncontradaException {

        Empresa empresa = empresaDAO.buscarPorCodigo(id);
        return empresaMapper.toDto(empresa);
    }

    public ListarEmpresaDto atualizar(int id, AtualizarEmpresaDto dto)
            throws SQLException, EntidadeNaoEncontradaException {

        Empresa existente = empresaDAO.buscarPorCodigo(id);
        empresaMapper.toModel(dto, existente);
        empresaDAO.atualizar(existente);

        return empresaMapper.toDto(existente);
    }

    public void deletar(int id)
            throws SQLException, EntidadeNaoEncontradaException {

        empresaDAO.deletar(id);
    }
}
