package br.com.fiap.dto.usuario;

// DTO simples para evitar referência circular na listagem de simulações
public class ListarUsuarioSimplesDto {
    private int idUsuario;
    private String username;

    public ListarUsuarioSimplesDto(int idUsuario, String username) {
        this.idUsuario = idUsuario;
        this.username = username;
    }

    // Getters
    public int getIdUsuario() { return idUsuario; }
    public String getUsername() { return username; }
}
