package br.com.fiap.mapper;

import br.com.fiap.dto.endereco.AtualizarEnderecoDto;
import br.com.fiap.dto.endereco.CadastrarEnderecoDto;
import br.com.fiap.dto.endereco.ListarEnderecoDto;
import br.com.fiap.model.Endereco;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EnderecoMapper {

    public Endereco toModel(CadastrarEnderecoDto dto) {
        Endereco endereco = new Endereco();
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setCep(dto.getCep());
        return endereco;
    }

    public void toModel(AtualizarEnderecoDto dto, Endereco endereco) {
        endereco.setLogradouro(dto.getLogradouro());
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setBairro(dto.getBairro());
        endereco.setCidade(dto.getCidade());
        endereco.setEstado(dto.getEstado());
        endereco.setCep(dto.getCep());
    }

    public ListarEnderecoDto toDto(Endereco endereco) {
        if (endereco == null) return null;
        return new ListarEnderecoDto(
                endereco.getIdEndereco(),
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep()
        );
    }
}
