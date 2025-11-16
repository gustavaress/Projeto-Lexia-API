package br.com.fiap.resource;

import br.com.fiap.dto.endereco.AtualizarEnderecoDto;
import br.com.fiap.dto.endereco.CadastrarEnderecoDto;
import br.com.fiap.dto.endereco.ListarEnderecoDto;
import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.service.EnderecoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;

@Path("/endereco")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EnderecoResource {

    private final EnderecoService enderecoService;

    @Inject
    public EnderecoResource(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @POST
    public Response cadastrar(@Valid CadastrarEnderecoDto dto) {
        try {
            ListarEnderecoDto endereco = enderecoService.cadastrar(dto);
            return Response.status(Response.Status.CREATED).entity(endereco).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao cadastrar endereço.").build();
        }
    }

    @GET
    public Response listarTodos() {
        try {
            return Response.ok(enderecoService.listarTodos()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao listar endereços.").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) {
        try {
            return Response.ok(enderecoService.buscarPorId(id)).build();
        } catch (EntidadeNaoEncontradaException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao buscar endereço.").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, @Valid AtualizarEnderecoDto dto) {
        try {
            ListarEnderecoDto endereco = enderecoService.atualizar(id, dto);
            return Response.ok(endereco).build();
        } catch (EntidadeNaoEncontradaException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao atualizar endereço.").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) {
        try {
            enderecoService.deletar(id);
            return Response.noContent().build();
        } catch (EntidadeNaoEncontradaException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao deletar endereço.").build();
        }
    }
}
