package br.com.fiap.dao;

import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.exception.RegraNegocioException;
import br.com.fiap.model.Simulacao;
import br.com.fiap.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SimulacaoDAO {

    @Inject
    private DataSource dataSource;

    @Inject
    private UsuarioDAO usuarioDao;

    public void inserir(Simulacao simulacao) throws SQLException, RegraNegocioException {
        String sql = """
                INSERT INTO SIMULACAO
                (ID_SIMULACAO, ID_USUARIO, DATA_SIMULACAO, TIPO_SIMULACAO, VALOR_FINAL)
                VALUES (SEQ_SIMULACAO.NEXTVAL, ?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID_SIMULACAO"})) {

            ps.setInt(1, simulacao.getUsuario().getIdUsuario());
            ps.setTimestamp(2, Timestamp.valueOf(simulacao.getDataSimulacao()));
            ps.setString(3, simulacao.getTipoSimulacao());
            ps.setDouble(4, simulacao.getValorFinal());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    simulacao.setIdSimulacao(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 2291) {
                throw new RegraNegocioException("Usuário não encontrado (violação de chave estrangeira).");
            }
            throw e;
        }
    }

    public List<Simulacao>listarTodos() throws SQLException, EntidadeNaoEncontradaException {
        List<Simulacao> simulacoes = new ArrayList<>();

        String sql = """
                SELECT 
                    s.ID_SIMULACAO,
                    s.ID_USUARIO,
                    s.DATA_SIMULACAO,
                    s.TIPO_SIMULACAO,
                    s.VALOR_FINAL
                FROM SIMULACAO s
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                simulacoes.add(parseSimulacao(rs));
            }
        }

        return simulacoes;
    }

    public Simulacao buscarPorCodigo(int codigo) throws SQLException, EntidadeNaoEncontradaException {
        String sql = """
                SELECT 
                    s.ID_SIMULACAO,
                    s.ID_USUARIO,
                    s.DATA_SIMULACAO,
                    s.TIPO_SIMULACAO,
                    s.VALOR_FINAL
                FROM SIMULACAO s
                WHERE s.ID_SIMULACAO = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new EntidadeNaoEncontradaException("Simulação não encontrada.");
                }
                return parseSimulacao(rs);
            }
        }
    }

    public void atualizar(Simulacao simulacao) throws SQLException, EntidadeNaoEncontradaException {
        String sql = """
                UPDATE SIMULACAO SET
                    ID_USUARIO = ?, 
                    DATA_SIMULACAO = ?, 
                    TIPO_SIMULACAO = ?, 
                    VALOR_FINAL = ?
                WHERE ID_SIMULACAO = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, simulacao.getUsuario().getIdUsuario());
            ps.setTimestamp(2, Timestamp.valueOf(simulacao.getDataSimulacao()));
            ps.setString(3, simulacao.getTipoSimulacao());
            ps.setDouble(4, simulacao.getValorFinal());
            ps.setInt(5, simulacao.getIdSimulacao());

            if (ps.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Simulação não encontrada para atualizar.");
            }
        }
    }

    public void deletar(int codigo) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "DELETE FROM SIMULACAO WHERE ID_SIMULACAO = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codigo);

            if (ps.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Simulação não encontrada para excluir.");
            }
        }
    }

    private Simulacao parseSimulacao(ResultSet rs) throws SQLException, EntidadeNaoEncontradaException {
        int idUsuario = rs.getInt("ID_USUARIO");
        Usuario usuario = usuarioDao.buscarPorCodigo(idUsuario);

        LocalDateTime data = rs.getTimestamp("DATA_SIMULACAO").toLocalDateTime();

        return new Simulacao(
                rs.getInt("ID_SIMULACAO"),
                usuario,
                data,
                rs.getString("TIPO_SIMULACAO"),
                rs.getDouble("VALOR_FINAL")
        );
    }
}
