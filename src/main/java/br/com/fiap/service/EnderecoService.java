package br.com.fiap.service;

import br.com.fiap.dao.EnderecoDAO;
import br.com.fiap.dto.endereco.AtualizarEnderecoDto;
import br.com.fiap.dto.endereco.CadastrarEnderecoDto;
import br.com.fiap.dto.endereco.ListarEnderecoDto;
import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.mapper.EnderecoMapper;
import br.com.fiap.model.Endereco;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EnderecoService {

    @Inject
    EnderecoDAO enderecoDAO;

    @Inject
    EnderecoMapper enderecoMapper;

    public ListarEnderecoDto cadastrar(CadastrarEnderecoDto dto) throws SQLException {
        Endereco endereco = enderecoMapper.toModel(dto);
        enderecoDAO.inserir(endereco);
        return enderecoMapper.toDto(endereco);
    }

    public List<ListarEnderecoDto> listarTodos() throws SQLException {
        return enderecoDAO.listarTodos()
                .stream()
                .map(enderecoMapper::toDto)
                .collect(Collectors.toList());
    }

    public ListarEnderecoDto buscarPorId(int id)
            throws SQLException, EntidadeNaoEncontradaException {

        Endereco endereco = enderecoDAO.buscarPorCodigo(id);
        return enderecoMapper.toDto(endereco);
    }

    public ListarEnderecoDto atualizar(int id, AtualizarEnderecoDto dto)
            throws SQLException, EntidadeNaoEncontradaException {

        Endereco existente = enderecoDAO.buscarPorCodigo(id);
        enderecoMapper.toModel(dto, existente);
        enderecoDAO.atualizar(existente);

        return enderecoMapper.toDto(existente);
    }

    public void deletar(int id)
            throws SQLException, EntidadeNaoEncontradaException {

        enderecoDAO.deletar(id);
    }
}
