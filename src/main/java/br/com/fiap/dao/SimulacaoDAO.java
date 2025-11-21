package br.com.fiap.dao;

import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.model.Simulacao;
import br.com.fiap.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SimulacaoDAO {

    @Inject
    private DataSource dataSource;

    @Inject
    private UsuarioDAO usuarioDAO;

    // Métodos públicos que gerenciam a conexão
    public Simulacao inserir(Simulacao simulacao) throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            return inserir(simulacao, conn);
        }
    }

    public List<Simulacao> listarTodos() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            return listarTodos(conn);
        }
    }

    public Simulacao buscarPorId(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conn = dataSource.getConnection()) {
            return buscarPorId(id, conn);
        }
    }

    public void atualizar(Simulacao simulacao) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conn = dataSource.getConnection()) {
            atualizar(simulacao, conn);
        }
    }

    public void deletar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conn = dataSource.getConnection()) {
            deletar(id, conn);
        }
    }
    
    public List<Simulacao> buscarSimulacoesPorUsuario(int idUsuario, Connection conn) throws SQLException {
        List<Simulacao> simulacoes = new ArrayList<>();
        String sql = "SELECT s.ID_SIMULACAO, s.DT_SIMULACAO, s.TIPO_SIMULACAO, s.VL_FINAL FROM TB_LEXIA_SIMULACAO s JOIN TB_LEXIA_R_USER_SIM r ON s.ID_SIMULACAO = r.ID_SIMULACAO WHERE r.ID_USUARIO = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    simulacoes.add(parseSimulacao(rs, conn, false)); // Não busca os usuários de novo
                }
            }
        }
        return simulacoes;
    }

    // Métodos privados que usam a conexão fornecida
    private Simulacao inserir(Simulacao simulacao, Connection conn) throws SQLException {
        String sql = "INSERT INTO TB_LEXIA_SIMULACAO (ID_SIMULACAO, DT_SIMULACAO, TIPO_SIMULACAO, VL_FINAL) VALUES (SEQ_SIMULACAO.NEXTVAL, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID_SIMULACAO"})) {
            ps.setTimestamp(1, Timestamp.valueOf(simulacao.getDataSimulacao()));
            ps.setString(2, simulacao.getTipoSimulacao());
            ps.setDouble(3, simulacao.getValorFinal());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    simulacao.setIdSimulacao(rs.getInt(1));
                }
            }

            if (simulacao.getUsuarios() != null) {
                for (Usuario usuario : simulacao.getUsuarios()) {
                    inserirRelacionamentoUsuarioSimulacao(usuario.getIdUsuario(), simulacao.getIdSimulacao(), conn);
                }
            }
        }
        return simulacao;
    }

    private List<Simulacao> listarTodos(Connection conn) throws SQLException {
        List<Simulacao> simulacoes = new ArrayList<>();
        String sql = "SELECT * FROM TB_LEXIA_SIMULACAO";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                simulacoes.add(parseSimulacao(rs, conn, true));
            }
        }
        return simulacoes;
    }

    private Simulacao buscarPorId(int id, Connection conn) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "SELECT * FROM TB_LEXIA_SIMULACAO WHERE ID_SIMULACAO = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return parseSimulacao(rs, conn, true);
                } else {
                    throw new EntidadeNaoEncontradaException("Simulação não encontrada");
                }
            }
        }
    }

    private void atualizar(Simulacao simulacao, Connection conn) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "UPDATE TB_LEXIA_SIMULACAO SET DT_SIMULACAO = ?, TIPO_SIMULACAO = ?, VL_FINAL = ? WHERE ID_SIMULACAO = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(simulacao.getDataSimulacao()));
            ps.setString(2, simulacao.getTipoSimulacao());
            ps.setDouble(3, simulacao.getValorFinal());
            ps.setInt(4, simulacao.getIdSimulacao());
            if (ps.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Simulação não encontrada");
            }

            deletarRelacionamentoUsuarioSimulacao(simulacao.getIdSimulacao(), conn);
            if (simulacao.getUsuarios() != null) {
                for (Usuario usuario : simulacao.getUsuarios()) {
                    inserirRelacionamentoUsuarioSimulacao(usuario.getIdUsuario(), simulacao.getIdSimulacao(), conn);
                }
            }
        }
    }

    private void deletar(int id, Connection conn) throws SQLException, EntidadeNaoEncontradaException {
        deletarRelacionamentoUsuarioSimulacao(id, conn);
        String sql = "DELETE FROM TB_LEXIA_SIMULACAO WHERE ID_SIMULACAO = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Simulação não encontrada");
            }
        }
    }

    private void inserirRelacionamentoUsuarioSimulacao(int idUsuario, int idSimulacao, Connection conn) throws SQLException {
        String sql = "INSERT INTO TB_LEXIA_R_USER_SIM (ID_USUARIO, ID_SIMULACAO) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);
            ps.setInt(2, idSimulacao);
            ps.executeUpdate();
        }
    }

    private void deletarRelacionamentoUsuarioSimulacao(int idSimulacao, Connection conn) throws SQLException {
        String sql = "DELETE FROM TB_LEXIA_R_USER_SIM WHERE ID_SIMULACAO = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSimulacao);
            ps.executeUpdate();
        }
    }

    private Simulacao parseSimulacao(ResultSet rs, Connection conn, boolean buscarUsuarios) throws SQLException {
        Simulacao simulacao = new Simulacao();
        simulacao.setIdSimulacao(rs.getInt("ID_SIMULACAO"));
        simulacao.setDataSimulacao(rs.getTimestamp("DT_SIMULACAO").toLocalDateTime());
        simulacao.setTipoSimulacao(rs.getString("TIPO_SIMULACAO"));
        simulacao.setValorFinal(rs.getDouble("VL_FINAL"));
        if (buscarUsuarios) {
            simulacao.setUsuarios(buscarUsuariosPorSimulacao(simulacao.getIdSimulacao(), conn));
        }
        return simulacao;
    }

    private List<Usuario> buscarUsuariosPorSimulacao(int idSimulacao, Connection conn) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT u.* FROM TB_LEXIA_USUARIO u JOIN TB_LEXIA_R_USER_SIM r ON u.ID_USUARIO = r.ID_USUARIO WHERE r.ID_SIMULACAO = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSimulacao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(usuarioDAO.parseUsuario(rs, conn, false)); // Não busca as simulações de novo
                }
            }
        }
        return usuarios;
    }
}
