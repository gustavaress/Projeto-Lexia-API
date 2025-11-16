package br.com.fiap.dao;

import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.exception.RegraNegocioException;
import br.com.fiap.model.Endereco;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EnderecoDAO {

    @Inject
    private DataSource dataSource;

    public void inserir(Endereco endereco) throws SQLException, RegraNegocioException {
        String sql = """
                INSERT INTO ENDERECO
                (ID_ENDERECO, LOGRADOURO, NUMERO, COMPLEMENTO, BAIRRO, CIDADE, ESTADO, CEP)
                VALUES (SEQ_ENDERECO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID_ENDERECO"})) {

            ps.setString(1, endereco.getLogradouro());
            ps.setString(2, endereco.getNumero());
            ps.setString(3, endereco.getComplemento());
            ps.setString(4, endereco.getBairro());
            ps.setString(5, endereco.getCidade());
            ps.setString(6, endereco.getEstado());
            ps.setString(7, endereco.getCep());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    endereco.setIdEndereco(rs.getInt(1));
                }
            }

        }
    }

    public List<Endereco> listarTodos() throws SQLException {
        List<Endereco> enderecos = new ArrayList<>();

        String sql = """
                SELECT 
                    ID_ENDERECO,
                    LOGRADOURO,
                    NUMERO,
                    COMPLEMENTO,
                    BAIRRO,
                    CIDADE,
                    ESTADO,
                    CEP
                FROM ENDERECO
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                enderecos.add(parseEndereco(rs));
            }
        }

        return enderecos;
    }

    public Endereco buscarPorCodigo(int codigo) throws SQLException, EntidadeNaoEncontradaException {
        String sql = """
                SELECT 
                    ID_ENDERECO,
                    LOGRADOURO,
                    NUMERO,
                    COMPLEMENTO,
                    BAIRRO,
                    CIDADE,
                    ESTADO,
                    CEP
                FROM ENDERECO
                WHERE ID_ENDERECO = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new EntidadeNaoEncontradaException("Endereço não encontrado.");
                }
                return parseEndereco(rs);
            }
        }
    }

    public void atualizar(Endereco endereco) throws SQLException, EntidadeNaoEncontradaException {
        String sql = """
                UPDATE ENDERECO SET
                    LOGRADOURO = ?, 
                    NUMERO = ?, 
                    COMPLEMENTO = ?, 
                    BAIRRO = ?, 
                    CIDADE = ?, 
                    ESTADO = ?, 
                    CEP = ?
                WHERE ID_ENDERECO = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, endereco.getLogradouro());
            ps.setString(2, endereco.getNumero());
            ps.setString(3, endereco.getComplemento());
            ps.setString(4, endereco.getBairro());
            ps.setString(5, endereco.getCidade());
            ps.setString(6, endereco.getEstado());
            ps.setString(7, endereco.getCep());
            ps.setInt(8, endereco.getIdEndereco());

            if (ps.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Endereço não encontrado para atualizar.");
            }
        }
    }

    public void deletar(int codigo) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "DELETE FROM ENDERECO WHERE ID_ENDERECO = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codigo);

            if (ps.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Endereço não encontrado para excluir.");
            }
        }
    }

    private Endereco parseEndereco(ResultSet rs) throws SQLException {
        return new Endereco(
                rs.getInt("ID_ENDERECO"),
                rs.getString("LOGRADOURO"),
                rs.getString("NUMERO"),
                rs.getString("COMPLEMENTO"),
                rs.getString("BAIRRO"),
                rs.getString("CIDADE"),
                rs.getString("ESTADO"),
                rs.getString("CEP")
        );
    }
}
