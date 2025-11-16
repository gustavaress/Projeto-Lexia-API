package br.com.fiap.model;

public class Empresa {
    private int idEmpresa;
    private String nomeFantasia;
    private String cnpj;
    private String email;
    private String telefone;
    private Endereco endereco;

    // Construtor padrão
    public Empresa() {}

    public Empresa(int idEmpresa, String nomeFantasia, String cnpj, String email, String telefone, Endereco endereco) {
        this.idEmpresa = idEmpresa;
        this.nomeFantasia = nomeFantasia;
        this.cnpj = cnpj;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    //Getters & Setters

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
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

    // toString
    @Override
    public String toString() {
        return "Empresa {" +
                "\n  idEmpresa=" + idEmpresa +
                ",\n  nomeFantasia='" + nomeFantasia + '\'' +
                ",\n  cnpj='" + cnpj + '\'' +
                ",\n  email='" + email + '\'' +
                ",\n  telefone='" + telefone + '\'' +
                ",\n  endereco=" + endereco +
                "\n}";
    }
}
