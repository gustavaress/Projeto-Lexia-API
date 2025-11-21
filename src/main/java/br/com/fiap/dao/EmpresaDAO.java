package br.com.fiap.dao;

import br.com.fiap.exception.CampoJaCadastradoException;
import br.com.fiap.exception.EntidadeNaoEncontradaException;
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

    @Inject
    private EnderecoDAO enderecoDAO;

    // Métodos públicos que gerenciam a conexão
    public Empresa inserir(Empresa empresa) throws SQLException, CampoJaCadastradoException {
        try (Connection conn = dataSource.getConnection()) {
            return inserir(empresa, conn);
        }
    }

    public List<Empresa> listarTodos() throws SQLException {
        try (Connection conn = dataSource.getConnection()) {
            return listarTodos(conn);
        }
    }

    public Empresa buscarPorId(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conn = dataSource.getConnection()) {
            return buscarPorId(id, conn);
        }
    }

    public void atualizar(Empresa empresa) throws SQLException, EntidadeNaoEncontradaException, CampoJaCadastradoException {
        try (Connection conn = dataSource.getConnection()) {
            atualizar(empresa, conn);
        }
    }

    public void deletar(int id) throws SQLException, EntidadeNaoEncontradaException {
        try (Connection conn = dataSource.getConnection()) {
            deletar(id, conn);
        }
    }

    // Métodos privados que usam a conexão fornecida
    private Empresa inserir(Empresa empresa, Connection conn) throws SQLException, CampoJaCadastradoException {
        String sql = "INSERT INTO TB_LEXIA_EMPRESA (ID_EMPRESA, NM_FANTASIA, CNPJ_EMP, EMAIL_EMP, TEL_EMP) VALUES (SEQ_EMPRESA.NEXTVAL, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID_EMPRESA"})) {
            ps.setString(1, empresa.getNomeFantasia());
            ps.setString(2, empresa.getCnpj());
            ps.setString(3, empresa.getEmail());
            ps.setString(4, empresa.getTelefone());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    empresa.setIdEmpresa(rs.getInt(1));
                }
            }

            if (empresa.getEnderecos() != null && !empresa.getEnderecos().isEmpty()) {
                for (Endereco endereco : empresa.getEnderecos()) {
                    inserirRelacionamentoEmpresaEndereco(empresa.getIdEmpresa(), endereco.getIdEndereco(), conn);
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) { // ORA-00001: unique constraint violated
                throw new CampoJaCadastradoException("CNPJ ou E-mail já cadastrado.");
            }
            throw e;
        }
        return empresa;
    }

    private List<Empresa> listarTodos(Connection conn) throws SQLException {
        List<Empresa> empresas = new ArrayList<>();
        String sql = "SELECT * FROM TB_LEXIA_EMPRESA";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                empresas.add(parseEmpresa(rs, conn));
            }
        }
        return empresas;
    }

    public Empresa buscarPorId(int id, Connection conn) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "SELECT * FROM TB_LEXIA_EMPRESA WHERE ID_EMPRESA = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return parseEmpresa(rs, conn);
                } else {
                    throw new EntidadeNaoEncontradaException("Empresa não encontrada");
                }
            }
        }
    }

    private void atualizar(Empresa empresa, Connection conn) throws SQLException, EntidadeNaoEncontradaException, CampoJaCadastradoException {
        String sql = "UPDATE TB_LEXIA_EMPRESA SET NM_FANTASIA = ?, CNPJ_EMP = ?, EMAIL_EMP = ?, TEL_EMP = ? WHERE ID_EMPRESA = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, empresa.getNomeFantasia());
            ps.setString(2, empresa.getCnpj());
            ps.setString(3, empresa.getEmail());
            ps.setString(4, empresa.getTelefone());
            ps.setInt(5, empresa.getIdEmpresa());
            if (ps.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Empresa não encontrada");
            }

            deletarRelacionamentoEmpresaEndereco(empresa.getIdEmpresa(), conn);
            if (empresa.getEnderecos() != null && !empresa.getEnderecos().isEmpty()) {
                for (Endereco endereco : empresa.getEnderecos()) {
                    inserirRelacionamentoEmpresaEndereco(empresa.getIdEmpresa(), endereco.getIdEndereco(), conn);
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 1) {
                throw new CampoJaCadastradoException("CNPJ ou E-mail já cadastrado.");
            }
            throw e;
        }
    }

    private void deletar(int id, Connection conn) throws SQLException, EntidadeNaoEncontradaException {
        deletarRelacionamentoEmpresaEndereco(id, conn);
        String sql = "DELETE FROM TB_LEXIA_EMPRESA WHERE ID_EMPRESA = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Empresa não encontrada");
            }
        }
    }

    private void inserirRelacionamentoEmpresaEndereco(int idEmpresa, int idEndereco, Connection conn) throws SQLException {
        String sql = "INSERT INTO TB_LEXIA_R_EMP_END (ID_EMPRESA, ID_END) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            ps.setInt(2, idEndereco);
            ps.executeUpdate();
        }
    }

    private void deletarRelacionamentoEmpresaEndereco(int idEmpresa, Connection conn) throws SQLException {
        String sql = "DELETE FROM TB_LEXIA_R_EMP_END WHERE ID_EMPRESA = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            ps.executeUpdate();
        }
    }

    private Empresa parseEmpresa(ResultSet rs, Connection conn) throws SQLException {
        Empresa empresa = new Empresa();
        empresa.setIdEmpresa(rs.getInt("ID_EMPRESA"));
        empresa.setNomeFantasia(rs.getString("NM_FANTASIA"));
        empresa.setCnpj(rs.getString("CNPJ_EMP"));
        empresa.setEmail(rs.getString("EMAIL_EMP"));
        empresa.setTelefone(rs.getString("TEL_EMP"));
        empresa.setEnderecos(buscarEnderecosPorEmpresa(empresa.getIdEmpresa(), conn));
        return empresa;
    }

    private List<Endereco> buscarEnderecosPorEmpresa(int idEmpresa, Connection conn) throws SQLException {
        List<Endereco> enderecos = new ArrayList<>();
        String sql = "SELECT e.ID_END, e.LOGRA_END, e.NUM_END, e.COMPLE_END, e.BAIRRO_END, e.CID_END, e.ESTADO_END, e.CEP_END FROM TB_LEXIA_ENDERECO e JOIN TB_LEXIA_R_EMP_END r ON e.ID_END = r.ID_END WHERE r.ID_EMPRESA = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEmpresa);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    enderecos.add(enderecoDAO.parseEndereco(rs));
                }
            }
        }
        return enderecos;
    }
}
