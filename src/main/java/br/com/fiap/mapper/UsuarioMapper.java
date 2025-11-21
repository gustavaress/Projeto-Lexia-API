package br.com.fiap.mapper;

import br.com.fiap.dto.usuario.AtualizarUsuarioDto;
import br.com.fiap.dto.usuario.CadastrarUsuarioDto;
import br.com.fiap.dto.usuario.ListarUsuarioDto;
import br.com.fiap.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.stream.Collectors;

@ApplicationScoped
public class UsuarioMapper {

    @Inject
    private EmpresaMapper empresaMapper;
    @Inject
    private EnderecoMapper enderecoMapper;
    @Inject
    private SimulacaoMapper simulacaoMapper;

    public Usuario toModel(CadastrarUsuarioDto dto) {
        Usuario usuario = new Usuario();
        usuario.setUsername(dto.getUsername());
        usuario.setSenha(dto.getSenha());
        usuario.setSalario(dto.getSalario());
        usuario.setTipoContrato(dto.getTipoContrato());
        return usuario;
    }

    public void toModel(AtualizarUsuarioDto dto, Usuario usuario) {
        usuario.setUsername(dto.getUsername());
        usuario.setSenha(dto.getSenha());
        usuario.setSalario(dto.getSalario());
        usuario.setTipoContrato(dto.getTipoContrato());
    }

    public ListarUsuarioDto toDto(Usuario usuario) {
        if (usuario == null) return null;
        return new ListarUsuarioDto(
                usuario.getIdUsuario(),
                usuario.getUsername(),
                usuario.getSalario(),
                usuario.getTipoContrato(),
                empresaMapper.toDto(usuario.getEmpresa()),
                usuario.getEnderecos() != null ? usuario.getEnderecos().stream().map(enderecoMapper::toDto).collect(Collectors.toList()) : null,
                usuario.getSimulacoes() != null ? usuario.getSimulacoes().stream().map(simulacaoMapper::toDto).collect(Collectors.toList()) : null
        );
    }
}
