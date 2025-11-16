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

        if (exception instanceof ConstraintViolationException e) {
            ValidationErrorDto dto = new ValidationErrorDto();

            for (ConstraintViolation<?> v : e.getConstraintViolations()) {
                dto.addError(
                        v.getPropertyPath().toString(),
                        v.getMessage()
                );
            }

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(dto)
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        if (exception instanceof CampoJaCadastradoException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ErrorResponseDto(e.getMessage()))
                    .build();
        }

        if (exception instanceof DadoInvalidoException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponseDto(e.getMessage()))
                    .build();
        }

        if (exception instanceof RegraNegocioException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorResponseDto(e.getMessage()))
                    .build();
        }

        if (exception instanceof EntidadeNaoEncontradaException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponseDto(e.getMessage()))
                    .build();
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponseDto("Erro interno no servidor."))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
