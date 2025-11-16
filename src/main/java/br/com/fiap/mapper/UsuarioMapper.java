package br.com.fiap.mapper;

import br.com.fiap.dao.EmpresaDAO;
import br.com.fiap.dao.EnderecoDAO;
import br.com.fiap.dto.usuario.AtualizarUsuarioDto;
import br.com.fiap.dto.usuario.CadastrarUsuarioDto;
import br.com.fiap.dto.usuario.ListarUsuarioDto;
import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.model.Empresa;
import br.com.fiap.model.Endereco;
import br.com.fiap.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

@ApplicationScoped
public class UsuarioMapper {

    private final EnderecoDAO enderecoDAO;
    private final EmpresaDAO empresaDAO;

    @Inject
    public UsuarioMapper(EnderecoDAO enderecoDAO, EmpresaDAO empresaDAO) {
        this.enderecoDAO = enderecoDAO;
        this.empresaDAO = empresaDAO;
    }

    public Usuario toModel(CadastrarUsuarioDto dto) throws SQLException, EntidadeNaoEncontradaException {
        Endereco endereco = enderecoDAO.buscarPorCodigo(dto.getIdEndereco());
        Empresa empresa = empresaDAO.buscarPorCodigo(dto.getIdEmpresa());

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefone(dto.getTelefone());
        usuario.setDataNascimento(dto.getDataNascimento());
        usuario.setEndereco(endereco);
        usuario.setUsername(dto.getUsername());
        usuario.setSenha(dto.getSenha());
        usuario.setSalario(dto.getSalario());
        usuario.setTipoContrato(dto.getTipoContrato());
        usuario.setEmpresa(empresa);

        return usuario;
    }

    public Usuario toModel(AtualizarUsuarioDto dto, Usuario existente)
            throws SQLException, EntidadeNaoEncontradaException {

        Endereco endereco = enderecoDAO.buscarPorCodigo(dto.getIdEndereco());
        Empresa empresa = empresaDAO.buscarPorCodigo(dto.getIdEmpresa());

        existente.setNome(dto.getNome());
        existente.setEmail(dto.getEmail());
        existente.setTelefone(dto.getTelefone());
        existente.setDataNascimento(dto.getDataNascimento());
        existente.setEndereco(endereco);
        existente.setUsername(dto.getUsername());
        existente.setSenha(dto.getSenha());
        existente.setSalario(dto.getSalario());
        existente.setTipoContrato(dto.getTipoContrato());
        existente.setEmpresa(empresa);

        return existente;
    }

    public ListarUsuarioDto toDto(Usuario usuario) {
        String dataFormatada = usuario.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String enderecoCompleto = String.format("%s, %s - %s, %s - %s",
                usuario.getEndereco().getLogradouro(),
                usuario.getEndereco().getNumero(),
                usuario.getEndereco().getBairro(),
                usuario.getEndereco().getCidade(),
                usuario.getEndereco().getEstado());

        return new ListarUsuarioDto(
                usuario.getIdUsuario(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                dataFormatada,
                usuario.getUsername(),
                usuario.getSalario(),
                usuario.getTipoContrato(),
                usuario.getEndereco().getIdEndereco(),
                enderecoCompleto,
                usuario.getEmpresa().getIdEmpresa(),
                usuario.getEmpresa().getNomeFantasia()
        );
    }
}
