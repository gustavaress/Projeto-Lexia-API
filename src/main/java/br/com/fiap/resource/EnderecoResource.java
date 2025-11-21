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

    @Inject
    private EnderecoService enderecoService;

    @POST
    public Response cadastrar(@Valid CadastrarEnderecoDto dto) throws SQLException {
        ListarEnderecoDto endereco = enderecoService.cadastrar(dto);
        return Response.status(Response.Status.CREATED).entity(endereco).build();
    }

    @GET
    public Response listarTodos() throws SQLException {
        return Response.ok(enderecoService.listarTodos()).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        return Response.ok(enderecoService.buscarPorId(id)).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, @Valid AtualizarEnderecoDto dto) throws SQLException, EntidadeNaoEncontradaException {
        ListarEnderecoDto endereco = enderecoService.atualizar(id, dto);
        return Response.ok(endereco).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        enderecoService.deletar(id);
        return Response.noContent().build();
    }
}
