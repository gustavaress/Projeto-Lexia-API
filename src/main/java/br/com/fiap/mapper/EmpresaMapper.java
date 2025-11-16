package br.com.fiap.mapper;

import br.com.fiap.dao.EnderecoDAO;
import br.com.fiap.dto.empresa.CadastrarEmpresaDto;
import br.com.fiap.dto.empresa.AtualizarEmpresaDto;
import br.com.fiap.dto.empresa.ListarEmpresaDto;
import br.com.fiap.dto.endereco.ListarEnderecoDto;
import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.model.Empresa;
import br.com.fiap.model.Endereco;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.SQLException;

@ApplicationScoped
public class EmpresaMapper {

    private final EnderecoDAO enderecoDAO;

    @Inject
    public EmpresaMapper(EnderecoDAO enderecoDAO) {
        this.enderecoDAO = enderecoDAO;
    }

    public Empresa toModel(CadastrarEmpresaDto dto)
            throws SQLException, EntidadeNaoEncontradaException {

        Endereco endereco = enderecoDAO.buscarPorCodigo(dto.getIdEndereco());

        Empresa empresa = new Empresa();
        empresa.setNomeFantasia(dto.getNomeFantasia());
        empresa.setCnpj(dto.getCnpj());
        empresa.setEmail(dto.getEmail());
        empresa.setTelefone(dto.getTelefone());
        empresa.setEndereco(endereco);

        return empresa;
    }

    public Empresa toModel(AtualizarEmpresaDto dto, Empresa existente)
            throws SQLException, EntidadeNaoEncontradaException {

        Endereco endereco = enderecoDAO.buscarPorCodigo(dto.getIdEndereco());

        existente.setNomeFantasia(dto.getNomeFantasia());
        existente.setCnpj(dto.getCnpj());
        existente.setEmail(dto.getEmail());
        existente.setTelefone(dto.getTelefone());
        existente.setEndereco(endereco);

        return existente;
    }

    public ListarEmpresaDto toDto(Empresa empresa) {
        Endereco endereco = empresa.getEndereco();
        ListarEnderecoDto enderecoDto = new ListarEnderecoDto(
                endereco.getIdEndereco(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep()
        );

        return new ListarEmpresaDto(
                empresa.getIdEmpresa(),
                empresa.getNomeFantasia(),
                empresa.getCnpj(),
                empresa.getEmail(),
                empresa.getTelefone(),
                enderecoDto
        );
    }
}
