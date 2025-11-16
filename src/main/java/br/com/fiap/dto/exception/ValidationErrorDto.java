package br.com.fiap.dto.exception;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ValidationErrorDto {

    private LocalDateTime timestamp;
    private List<FieldErrorDto> errors = new ArrayList<>();

    public ValidationErrorDto() {
        this.timestamp = LocalDateTime.now();
    }

    public void addError(String field, String message) {
        errors.add(new FieldErrorDto(field, message));
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public List<FieldErrorDto> getErrors() {
        return errors;
    }
}
