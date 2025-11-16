package br.com.fiap.dao;

import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.exception.RegraNegocioException;
import br.com.fiap.model.Empresa;
import br.com.fiap.model.Endereco;
import br.com.fiap.model.Usuario;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UsuarioDAO {

    @Inject
    private DataSource dataSource;

    @Inject
    private EnderecoDAO enderecoDao;

    @Inject
    private EmpresaDAO empresaDao;

    public void inserir(Usuario usuario) throws SQLException, RegraNegocioException {
        String sql = """
                INSERT INTO USUARIO
                (ID_USUARIO, NOME, EMAIL, TELEFONE, DATA_NASCIMENTO,
                 ID_ENDERECO, USERNAME, SENHA, SALARIO, TIPO_CONTRATO, ID_EMPRESA)
                VALUES (SEQ_USUARIO.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID_USUARIO"})) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getTelefone());
            ps.setDate(4, Date.valueOf(usuario.getDataNascimento()));
            ps.setInt(5, usuario.getEndereco().getIdEndereco());
            ps.setString(6, usuario.getUsername());
            ps.setString(7, usuario.getSenha());
            ps.setDouble(8, usuario.getSalario());
            ps.setString(9, usuario.getTipoContrato());
            ps.setInt(10, usuario.getEmpresa().getIdEmpresa());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    usuario.setIdUsuario(rs.getInt(1));
                }
            }

        } catch (SQLException e) {
            if (e.getErrorCode() == 2291) {
                throw new RegraNegocioException("Endereço ou empresa inválidos (violação de FK).");
            }
            if (e.getErrorCode() == 1) {
                throw new RegraNegocioException("Username ou email já existente.");
            }
            throw e;
        }
    }

    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();

        String sql = """
                SELECT
                    u.ID_USUARIO,
                    u.NOME,
                    u.EMAIL,
                    u.TELEFONE,
                    u.DATA_NASCIMENTO,
                    u.ID_ENDERECO,
                    u.USERNAME,
                    u.SENHA,
                    u.SALARIO,
                    u.TIPO_CONTRATO,
                    u.ID_EMPRESA,

                    e.LOGRADOURO,
                    e.NUMERO,
                    e.COMPLEMENTO,
                    e.BAIRRO,
                    e.CIDADE,
                    e.ESTADO,
                    e.CEP,

                    em.NOME_FANTASIA,
                    em.CNPJ,
                    em.EMAIL AS EMP_EMAIL,
                    em.TELEFONE AS EMP_TELEFONE
                FROM USUARIO u
                LEFT JOIN ENDERECO e ON u.ID_ENDERECO = e.ID_ENDERECO
                LEFT JOIN EMPRESA em ON u.ID_EMPRESA = em.ID_EMPRESA
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                usuarios.add(parseUsuario(rs));
            }
        }

        return usuarios;
    }

    public Usuario buscarPorCodigo(int id) throws SQLException, EntidadeNaoEncontradaException {
        String sql = """
                SELECT
                    u.ID_USUARIO,
                    u.NOME,
                    u.EMAIL,
                    u.TELEFONE,
                    u.DATA_NASCIMENTO,
                    u.ID_ENDERECO,
                    u.USERNAME,
                    u.SENHA,
                    u.SALARIO,
                    u.TIPO_CONTRATO,
                    u.ID_EMPRESA,

                    e.LOGRADOURO,
                    e.NUMERO,
                    e.COMPLEMENTO,
                    e.BAIRRO,
                    e.CIDADE,
                    e.ESTADO,
                    e.CEP,

                    em.NOME_FANTASIA,
                    em.CNPJ,
                    em.EMAIL AS EMP_EMAIL,
                    em.TELEFONE AS EMP_TELEFONE
                FROM USUARIO u
                LEFT JOIN ENDERECO e ON u.ID_ENDERECO = e.ID_ENDERECO
                LEFT JOIN EMPRESA em ON u.ID_EMPRESA = em.ID_EMPRESA
                WHERE u.ID_USUARIO = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new EntidadeNaoEncontradaException("Usuário não encontrado.");
                }
                return parseUsuario(rs);
            }
        }
    }

    public void atualizar(Usuario usuario) throws SQLException, EntidadeNaoEncontradaException {
        String sql = """
                UPDATE USUARIO SET
                    NOME = ?,
                    EMAIL = ?,
                    TELEFONE = ?,
                    DATA_NASCIMENTO = ?,
                    ID_ENDERECO = ?,
                    USERNAME = ?,
                    SENHA = ?,
                    SALARIO = ?,
                    TIPO_CONTRATO = ?,
                    ID_EMPRESA = ?
                WHERE ID_USUARIO = ?
                """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getTelefone());
            ps.setDate(4, Date.valueOf(usuario.getDataNascimento()));
            ps.setInt(5, usuario.getEndereco().getIdEndereco());
            ps.setString(6, usuario.getUsername());
            ps.setString(7, usuario.getSenha());
            ps.setDouble(8, usuario.getSalario());
            ps.setString(9, usuario.getTipoContrato());
            ps.setInt(10, usuario.getEmpresa().getIdEmpresa());
            ps.setInt(11, usuario.getIdUsuario());

            if (ps.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Usuário não encontrado para atualizar.");
            }
        }
    }

    public void deletar(int id) throws SQLException, EntidadeNaoEncontradaException {
        String sql = "DELETE FROM USUARIO WHERE ID_USUARIO = ?";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            if (ps.executeUpdate() == 0) {
                throw new EntidadeNaoEncontradaException("Usuário não encontrado para excluir.");
            }
        }
    }

    private Usuario parseUsuario(ResultSet rs) throws SQLException {
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

        Empresa empresa = new Empresa(
                rs.getInt("ID_EMPRESA"),
                rs.getString("NOME_FANTASIA"),
                rs.getString("CNPJ"),
                rs.getString("EMP_EMAIL"),
                rs.getString("EMP_TELEFONE"),
                null
        );

        LocalDate data = rs.getDate("DATA_NASCIMENTO").toLocalDate();

        return new Usuario(
                rs.getString("NOME"),
                rs.getString("EMAIL"),
                rs.getString("TELEFONE"),
                endereco,
                data,
                rs.getInt("ID_USUARIO"),
                rs.getString("USERNAME"),
                rs.getString("SENHA"),
                rs.getDouble("SALARIO"),
                rs.getString("TIPO_CONTRATO"),
                empresa
        );
    }
}
