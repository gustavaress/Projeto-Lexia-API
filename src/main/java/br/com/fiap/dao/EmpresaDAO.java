package br.com.fiap.dao;

import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.exception.RegraNegocioException;
import br.com.fiap.model.Empresa;
import br.com.fiap.model.Endereco;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class EmpresaDAO {

    @Inject
    private DataSource dataSource;

    public void inserir(Empresa empresa) throws SQLException, RegraNegocioException {
        String sql = """
                INSERT INTO EMPRESA 
                (ID_EMPRESA, NOME_FANTASIA, CNPJ, EMAIL, TELEFONE, ID_ENDERECO)
                VALUES (SEQ_EMPRESA.NEXTVAL, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID_EMPRESA"})) {

            ps.setString(1, empresa.getNomeFantasia());
            ps.setString(2, empresa.getCnpj());
            ps.setString(3, empresa.getEmail());
            ps.setString(4, empresa.getTelefone());
            ps.setInt(5, empresa.getEndereco().getIdEndereco());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    empresa.setIdEmpresa(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 2291) {
                throw new RegraNegocioException("Endereço inválido (violação de chave estrangeira).");
            } else if (e.getErrorCode() == 1) { // ORA-00001: unique constraint violated
                throw new RegraNegocioException("CNPJ já cadastrado.");
            }
            throw e;
        }
    }

    public List<Empresa> listarTodos() throws SQLException {
        List<Empresa> empresas = new ArrayList<>();

        String sql = """
                SELECT 
                    e.ID_EMPRESA,
                    e.NOME_FANTASIA,
                    e.CNPJ,
                    e.EMAIL,
                    e.TELEFONE,
                    e.ID_ENDERECO,
                    end.LOGRADOURO,
                    end.NUMERO,
                    end.COMPLEMENTO,
                    end.BAIRRO,
                    end.CIDADE,
                    end.ESTADO,
                    end.CEP
                FROM EMPRESA e
                LEFT JOIN ENDERECO end ON e.ID_ENDERECO = end.ID_ENDERECO
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                empresas.add(parseEmpresa(rs));
            }
        }

        return empresas;
    }

    public Empresa buscarPorCodigo(int codigo) throws SQLException, EntidadeNaoEncontradaException {
        String sql = """
                SELECT 
                    e.ID_EMPRESA,
                    e.NOME_FANTASIA,
                    e.CNPJ,
                    e.EMAIL,
                    e.TELEFONE,
                    e.ID_ENDERECO,
                    end.LOGRADOURO,
                    end.NUMERO,
                    end.COMPLEMENTO,
                    end.BAIRRO,
                    end.CIDADE,
                    end.ESTADO,
                    end.CEP
                FROM EMPRESA e
                LEFT JOIN ENDERECO end ON e.ID_ENDERECO = end.ID_ENDERECO
                WHERE e.ID_EMPRESA = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codigo);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new EntidadeNaoEncontradaException("Empresa não encontrada.");
                }
                return parseEmpresa(rs);
            }
        }
    }

    public void atualizar(Empresa empresa) throws SQLException, EntidadeNaoEncontradaException {
        String sql = """
                UPDATE EMPRESA SET
                    NOME_FANTASIA = ?, 
                    CNPJ = ?, 
                    EMAIL = ?, 
                    TELEFONE = ?, 
                    ID_ENDERECO = ?
                WHERE ID_EMPRESA = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, empresa.getNomeFantasia());
            ps.setString(2, empresa.getCnpj());
            ps.setString(3, empresa.getEmail());
            ps.setString(4, empresa.getTelefone());
            ps.setInt(5, empresa.getEndereco().getIdEndereco());
            ps.setInt(6, empresa.getIdEmpresa());

            if (ps.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Empresa não encontrada para atualizar.");
            }
        }
    }

    public void deletar(int codigo) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "DELETE FROM EMPRESA WHERE ID_EMPRESA = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, codigo);

            if (ps.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Empresa não encontrada para excluir.");
            }
        }
    }

    private Empresa parseEmpresa(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco(
                rs.getInt("ID_ENDERECO"),
                rs.getString("LOGRADOURO"),
                rs.getString("NUMERO"),
                rs.getString("COMPLEMENTO"),
                rs.getString("BAIRRO"),
                rs.getString("CIDADE"),
                rs.getString("ESTADO"),
                rs.getString("CEP")
        );

        return new Empresa(
                rs.getInt("ID_EMPRESA"),
                rs.getString("NOME_FANTASIA"),
                rs.getString("CNPJ"),
                rs.getString("EMAIL"),
                rs.getString("TELEFONE"),
                endereco
        );
    }
}
