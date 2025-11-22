package br.com.fiap.service;

import br.com.fiap.dao.UsuarioDAO;
import br.com.fiap.dto.login.LoginDto;
import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.exception.RegraNegocioException;
import br.com.fiap.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.sql.SQLException;

@ApplicationScoped
public class LoginService {

    @Inject
    private UsuarioDAO usuarioDAO;

    public void login(LoginDto loginDto) {
        try {
            Usuario usuario = usuarioDAO.buscarPorId(loginDto.id().intValue());
            if (!usuario.getUsername().equals(loginDto.username()) || !usuario.getSenha().equals(loginDto.senha())) {
                throw new RegraNegocioException("Nome de usuário ou senha inválidos");
            }
        } catch (SQLException e) {
            throw new RegraNegocioException("Erro ao consultar o banco de dados", e);
        } catch (EntidadeNaoEncontradaException e) {
            throw new RegraNegocioException("Nome de usuário ou senha inválidos");
        }
    }
}
