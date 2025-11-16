package br.com.fiap.dto.exception;

import java.time.LocalDateTime;

public class ErrorResponseDto {

    private String message;
    private int status;
    private LocalDateTime timestamp;

    public ErrorResponseDto(String message, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponseDto(String message) {
        this.message = message;
        this.status = 500; // padrão caso o status não seja fornecido
        this.timestamp = LocalDateTime.now();
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
