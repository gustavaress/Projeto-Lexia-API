package br.com.fiap.dto.usuario;

public class ListarUsuarioDto {

    private int idUsuario;
    private String nome;
    private String email;
    private String telefone;
    private String dataNascimento;
    private String username;
    private double salario;
    private String tipoContrato;

    private int idEndereco;
    private String enderecoCompleto;

    private int idEmpresa;
    private String nomeEmpresa;

    public ListarUsuarioDto(int idUsuario, String nome, String email, String telefone, String dataNascimento,
                            String username, double salario, String tipoContrato,
                            int idEndereco, String enderecoCompleto,
                            int idEmpresa, String nomeEmpresa) {

        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.username = username;
        this.salario = salario;
        this.tipoContrato = tipoContrato;
        this.idEndereco = idEndereco;
        this.enderecoCompleto = enderecoCompleto;
        this.idEmpresa = idEmpresa;
        this.nomeEmpresa = nomeEmpresa;
    }

    public int getIdUsuario() { return idUsuario; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public String getDataNascimento() { return dataNascimento; }
    public String getUsername() { return username; }
    public double getSalario() { return salario; }
    public String getTipoContrato() { return tipoContrato; }
    public int getIdEndereco() { return idEndereco; }
    public String getEnderecoCompleto() { return enderecoCompleto; }
    public int getIdEmpresa() { return idEmpresa; }
    public String getNomeEmpresa() { return nomeEmpresa; }
}
