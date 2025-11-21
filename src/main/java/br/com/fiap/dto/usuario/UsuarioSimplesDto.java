package br.com.fiap.dto.usuario;

public class UsuarioSimplesDto {
    private int idUsuario;
    private String nome;
    private String email;

    public UsuarioSimplesDto(int idUsuario, String nome, String email) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }
}
