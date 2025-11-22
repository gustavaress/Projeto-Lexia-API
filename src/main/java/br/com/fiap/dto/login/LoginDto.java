package br.com.fiap.dto.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginDto(
        @NotNull(message = "O id não pode ser nulo")
        Long id,
        @NotBlank(message = "O nome de usuário não pode estar em branco")
        String username,
        @NotBlank(message = "A senha não pode estar em branco")
        String senha
) {
}
