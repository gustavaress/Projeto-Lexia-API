package br.com.fiap.model;

import java.time.LocalDate;

public class Usuario extends Pessoa {
    private int idUsuario;
    private String username;
    private String senha;
    private double salario;
    private String tipoContrato;
    private Empresa empresa;

    // Construtor padrão
    public Usuario() {
        super();
    }

    // Construtor completo (com obrigatoriedade herdada)


    public Usuario(int idUsuario, String username, String senha, double salario, String tipoContrato, Empresa empresa) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.senha = senha;
        this.salario = salario;
        this.tipoContrato = tipoContrato;
        this.empresa = empresa;
    }

    public Usuario(String nome, String email, String telefone, Endereco endereco, LocalDate dataNascimento, int idUsuario, String username, String senha, double salario, String tipoContrato, Empresa empresa) {
        super(nome, email, telefone, endereco, dataNascimento);
        this.idUsuario = idUsuario;
        this.username = username;
        this.senha = senha;
        this.salario = salario;
        this.tipoContrato = tipoContrato;
        this.empresa = empresa;
    }

    // Getters e Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    // toString()
    @Override
    public String toString() {
        return "Usuário {" +
                "\n  nome='" + getNome() + '\'' +
                ",\n  email='" + getEmail() + '\'' +
                ",\n  telefone='" + getTelefone() + '\'' +
                ",\n  dataNascimento=" + getDataNascimento().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ",\n  endereco=" + getEndereco() +
                ",\n  username='" + username + '\'' +
                ",\n  salario=" + salario +
                ",\n  tipoContrato='" + tipoContrato + '\'' +
                ",\n  Empresa='" + getEmpresa().getNomeFantasia() + '\'' +
                "\n}";
    }
}
