package br.com.fiap.dao;

import br.com.fiap.exception.EntidadeNaoEncontradaException;
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

    public Endereco inserir(Endereco endereco) throws SQLException {
        String sql = "INSERT INTO TB_LEXIA_ENDERECO (ID_END, LOGRA_END, NUM_END, COMPLE_END, BAIRRO_END, CID_END, ESTADO_END, CEP_END) VALUES (SEQ_ENDERECO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID_END"})) {
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
        return endereco;
    }

    public List<Endereco> listarTodos() throws SQLException {
        List<Endereco> enderecos = new ArrayList<>();
        String sql = "SELECT * FROM TB_LEXIA_ENDERECO";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                enderecos.add(parseEndereco(rs));
            }
        }
        return enderecos;
    }

    public Endereco buscarPorId(int id) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "SELECT * FROM TB_LEXIA_ENDERECO WHERE ID_END = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return parseEndereco(rs);
                } else {
                    throw new EntidadeNaoEncontradaException("Endereço não encontrado");
                }
            }
        }
    }

    public void atualizar(Endereco endereco) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "UPDATE TB_LEXIA_ENDERECO SET LOGRA_END = ?, NUM_END = ?, COMPLE_END = ?, BAIRRO_END = ?, CID_END = ?, ESTADO_END = ?, CEP_END = ? WHERE ID_END = ?";
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

    public void deletar(int id) throws SQLException, EntidadeNaoEncontradaException {
        // Antes de deletar, é preciso remover as referências nas tabelas de junção
        deletarRelacionamentos(id);

        String sql = "DELETE FROM TB_LEXIA_ENDERECO WHERE ID_END = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Endereço não encontrado para excluir.");
            }
        }
    }
    
    private void deletarRelacionamentos(int idEndereco) throws SQLException {
        String sqlUser = "DELETE FROM TB_LEXIA_R_USER_END WHERE ID_END = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlUser)) {
            ps.setInt(1, idEndereco);
            ps.executeUpdate();
        }

        String sqlEmpresa = "DELETE FROM TB_LEXIA_R_EMP_END WHERE ID_END = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlEmpresa)) {
            ps.setInt(1, idEndereco);
            ps.executeUpdate();
        }
    }

    public Endereco parseEndereco(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco();
        endereco.setIdEndereco(rs.getInt("ID_END"));
        endereco.setLogradouro(rs.getString("LOGRA_END"));
        endereco.setNumero(rs.getString("NUM_END"));
        endereco.setComplemento(rs.getString("COMPLE_END"));
        endereco.setBairro(rs.getString("BAIRRO_END"));
        endereco.setCidade(rs.getString("CID_END"));
        endereco.setEstado(rs.getString("ESTADO_END"));
        endereco.setCep(rs.getString("CEP_END"));
        return endereco;
    }
}
