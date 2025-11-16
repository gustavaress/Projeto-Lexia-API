package br.com.fiap.model;

import java.time.LocalDate;

public class Pessoa {
    private String nome;
    private String email;
    private String telefone;
    private Endereco endereco;
    private LocalDate dataNascimento;

    // Construtor padrão
    public Pessoa() {}

    // Construtor com obrigatoriedade de dataNascimento e endereco
    public Pessoa(String nome, String email, String telefone, Endereco endereco, LocalDate dataNascimento) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.dataNascimento = dataNascimento;
    }

    // Getters & Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    @Override
    public String toString() {
        return "Pessoa {" +
                "\n  nome='" + nome + '\'' +
                ",\n  email='" + email + '\'' +
                ",\n  telefone='" + telefone + '\'' +
                ",\n  dataNascimento=" + dataNascimento.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                ",\n  endereco=" + endereco +
                "\n}";
    }
}
