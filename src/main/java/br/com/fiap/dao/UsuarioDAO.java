package br.com.fiap.dao;

import br.com.fiap.exception.CampoJaCadastradoException;
import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.model.Empresa;
import br.com.fiap.model.Endereco;
import br.com.fiap.model.Simulacao;
import br.com.fiap.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UsuarioDAO {

    @Inject
    private DataSource dataSource;

    @Inject
    private EmpresaDAO empresaDAO;

    @Inject
    private EnderecoDAO enderecoDAO;

    // Métodos públicos que gerenciam a conexão
    public Usuario inserir(Usuario usuario) throws SQLException, CampoJaCadastradoException {
        try (Connection conn = dataSource.getConnection()) {
            return inserir(usuario, conn);
        }
    }

    public List<Usuario> listarTodos() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            return listarTodos(conn);
        }
    }

    public Usuario buscarPorId(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conn = dataSource.getConnection()) {
            return buscarPorId(id, conn, true); // Por padrão, busca as simulações
        }
    }

    public void atualizar(Usuario usuario) throws SQLException, EntidadeNaoEncontradaException, CampoJaCadastradoException {
        try (Connection conn = dataSource.getConnection()) {
            atualizar(usuario, conn);
        }
    }

    public void deletar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conn = dataSource.getConnection()) {
            deletar(id, conn);
        }
    }

    // Métodos privados que usam a conexão fornecida
    private Usuario inserir(Usuario usuario, Connection conn) throws SQLException, CampoJaCadastradoException {
        String sql = "INSERT INTO TB_LEXIA_USUARIO (ID_USUARIO, USER_NAME, VL_SALARIO, SENHA_USUARIO, TIPO_CONTRATO, TB_LEXIA_EMPRESA_ID_EMPRESA) VALUES (SEQ_USUARIO.NEXTVAL, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID_USUARIO"})) {
            ps.setString(1, usuario.getUsername());
            ps.setDouble(2, usuario.getSalario());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, usuario.getTipoContrato());
            ps.setInt(5, usuario.getEmpresa().getIdEmpresa());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setIdUsuario(rs.getInt(1));
                }
            }

            if (usuario.getEnderecos() != null) {
                for (Endereco endereco : usuario.getEnderecos()) {
                    inserirRelacionamentoUsuarioEndereco(usuario.getIdUsuario(), endereco.getIdEndereco(), conn);
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {
                throw new CampoJaCadastradoException("Username já cadastrado.");
            }
            throw e;
        }
        return usuario;
    }

    private List<Usuario> listarTodos(Connection conn) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM TB_LEXIA_USUARIO";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                usuarios.add(parseUsuario(rs, conn, true));
            }
        }
        return usuarios;
    }

    public Usuario buscarPorId(int id, Connection conn, boolean buscarSimulacoes) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "SELECT * FROM TB_LEXIA_USUARIO WHERE ID_USUARIO = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return parseUsuario(rs, conn, buscarSimulacoes);
                } else {
                    throw new EntidadeNaoEncontradaException("Usuário não encontrado");
                }
            }
        }
    }

    private void atualizar(Usuario usuario, Connection conn) throws SQLException, EntidadeNaoEncontradaException, CampoJaCadastradoException {
        String sql = "UPDATE TB_LEXIA_USUARIO SET USER_NAME = ?, VL_SALARIO = ?, SENHA_USUARIO = ?, TIPO_CONTRATO = ?, TB_LEXIA_EMPRESA_ID_EMPRESA = ? WHERE ID_USUARIO = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getUsername());
            ps.setDouble(2, usuario.getSalario());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, usuario.getTipoContrato());
            ps.setInt(5, usuario.getEmpresa().getIdEmpresa());
            ps.setInt(6, usuario.getIdUsuario());
            if (ps.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Usuário não encontrado");
            }

            deletarRelacionamentoUsuarioEndereco(usuario.getIdUsuario(), conn);
            if (usuario.getEnderecos() != null) {
                for (Endereco endereco : usuario.getEnderecos()) {
                    inserirRelacionamentoUsuarioEndereco(usuario.getIdUsuario(), endereco.getIdEndereco(), conn);
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {
                throw new CampoJaCadastradoException("Username já cadastrado.");
            }
            throw e;
        }
    }

    private void deletar(int id, Connection conn) throws SQLException, EntidadeNaoEncontradaException {
        deletarRelacionamentoUsuarioEndereco(id, conn);
        deletarRelacionamentoUsuarioSimulacao(id, conn);
        String sql = "DELETE FROM TB_LEXIA_USUARIO WHERE ID_USUARIO = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Usuário não encontrado");
            }
        }
    }

    private void inserirRelacionamentoUsuarioEndereco(int idUsuario, int idEndereco, Connection conn) throws SQLException {
        String sql = "INSERT INTO TB_LEXIA_R_USER_END (ID_USUARIO, ID_END) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idEndereco);
            ps.executeUpdate();
        }
    }

    private void deletarRelacionamentoUsuarioEndereco(int idUsuario, Connection conn) throws SQLException {
        String sql = "DELETE FROM TB_LEXIA_R_USER_END WHERE ID_USUARIO = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        }
    }

    private void deletarRelacionamentoUsuarioSimulacao(int idUsuario, Connection conn) throws SQLException {
        String sql = "DELETE FROM TB_LEXIA_R_USER_SIM WHERE ID_USUARIO = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        }
    }

    public Usuario parseUsuario(ResultSet rs, Connection conn, boolean buscarSimulacoes) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(rs.getInt("ID_USUARIO"));
        usuario.setUsername(rs.getString("USER_NAME"));
        usuario.setSalario(rs.getDouble("VL_SALARIO"));
        usuario.setSenha(rs.getString("SENHA_USUARIO"));
        usuario.setTipoContrato(rs.getString("TIPO_CONTRATO"));

        try {
            Empresa empresa = empresaDAO.buscarPorId(rs.getInt("TB_LEXIA_EMPRESA_ID_EMPRESA"), conn);
            usuario.setEmpresa(empresa);
        } catch (EntidadeNaoEncontradaException e) {
            // Ignorar
        }

        usuario.setEnderecos(buscarEnderecosPorUsuario(usuario.getIdUsuario(), conn));
        
        if (buscarSimulacoes) {
            // A injeção do SimulacaoDAO é feita via setter para quebrar o ciclo de dependência do CDI
            // SimulacaoDAO simulacaoDAO = CDI.current().select(SimulacaoDAO.class).get();
            // usuario.setSimulacoes(simulacaoDAO.buscarSimulacoesPorUsuario(usuario.getIdUsuario(), conn));
        }

        return usuario;
    }

    private List<Endereco> buscarEnderecosPorUsuario(int idUsuario, Connection conn) throws SQLException {
        List<Endereco> enderecos = new ArrayList<>();
        String sql = "SELECT e.ID_END, e.LOGRA_END, e.NUM_END, e.COMPLE_END, e.BAIRRO_END, e.CID_END, e.ESTADO_END, e.CEP_END FROM TB_LEXIA_ENDERECO e JOIN TB_LEXIA_R_USER_END r ON e.ID_END = r.ID_END WHERE r.ID_USUARIO = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    enderecos.add(enderecoDAO.parseEndereco(rs));
                }
            }
        }
        return enderecos;
    }
}
