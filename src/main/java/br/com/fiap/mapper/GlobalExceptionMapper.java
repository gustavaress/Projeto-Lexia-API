package br.com.fiap.mapper;

import br.com.fiap.dto.exception.ErrorResponseDto;
import br.com.fiap.dto.exception.ValidationErrorDto;
import br.com.fiap.exception.CampoJaCadastradoException;
import br.com.fiap.exception.DadoInvalidoException;
import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.exception.RegraNegocioException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable exception) {

        Response.Status status;
        Object dto;

        if (exception instanceof ConstraintViolationException e) {
            status = Response.Status.BAD_REQUEST;
            ValidationErrorDto validationErrorDto = new ValidationErrorDto();
            for (ConstraintViolation<?> v : e.getConstraintViolations()) {
                validationErrorDto.addError(
                        v.getPropertyPath().toString(),
                        v.getMessage()
                );
            }
            dto = validationErrorDto;
        } else if (exception instanceof CampoJaCadastradoException e) {
            status = Response.Status.CONFLICT;
            dto = new ErrorResponseDto(e.getMessage(), status.getStatusCode());
        } else if (exception instanceof DadoInvalidoException e) {
            status = Response.Status.BAD_REQUEST;
            dto = new ErrorResponseDto(e.getMessage(), status.getStatusCode());
        } else if (exception instanceof RegraNegocioException e) {
            status = Response.Status.BAD_REQUEST;
            dto = new ErrorResponseDto(e.getMessage(), status.getStatusCode());
        } else if (exception instanceof EntidadeNaoEncontradaException e) {
            status = Response.Status.NOT_FOUND;
            dto = new ErrorResponseDto(e.getMessage(), status.getStatusCode());
        } else {
            status = Response.Status.INTERNAL_SERVER_ERROR;
            dto = new ErrorResponseDto("Erro interno no servidor.", status.getStatusCode());
        }

        return Response.status(status)
                .entity(dto)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
