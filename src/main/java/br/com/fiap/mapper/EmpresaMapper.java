package br.com.fiap.mapper;

import br.com.fiap.dto.empresa.AtualizarEmpresaDto;
import br.com.fiap.dto.empresa.CadastrarEmpresaDto;
import br.com.fiap.dto.empresa.ListarEmpresaDto;
import br.com.fiap.model.Empresa;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.stream.Collectors;

@ApplicationScoped
public class EmpresaMapper {

    @Inject
    private EnderecoMapper enderecoMapper;

    public Empresa toModel(CadastrarEmpresaDto dto) {
        Empresa empresa = new Empresa();
        empresa.setNomeFantasia(dto.getNomeFantasia());
        empresa.setCnpj(dto.getCnpj());
        empresa.setEmail(dto.getEmail());
        empresa.setTelefone(dto.getTelefone());
        return empresa;
    }

    public void toModel(AtualizarEmpresaDto dto, Empresa empresa) {
        empresa.setNomeFantasia(dto.getNomeFantasia());
        empresa.setCnpj(dto.getCnpj());
        empresa.setEmail(dto.getEmail());
        empresa.setTelefone(dto.getTelefone());
    }

    public ListarEmpresaDto toDto(Empresa empresa) {
        if (empresa == null) return null;
        return new ListarEmpresaDto(
                empresa.getIdEmpresa(),
                empresa.getNomeFantasia(),
                empresa.getCnpj(),
                empresa.getEmail(),
                empresa.getTelefone(),
                empresa.getEnderecos() != null ? empresa.getEnderecos().stream().map(enderecoMapper::toDto).collect(Collectors.toList()) : null
        );
    }
}
