package br.com.fiap.resource;

import br.com.fiap.dto.usuario.AtualizarUsuarioDto;
import br.com.fiap.dto.usuario.CadastrarUsuarioDto;
import br.com.fiap.dto.usuario.ListarUsuarioDto;
import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;

@Path("/usuario")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    private UsuarioService usuarioService;

    @POST
    public Response cadastrar(@Valid CadastrarUsuarioDto dto) throws SQLException, EntidadeNaoEncontradaException {
        ListarUsuarioDto usuario = usuarioService.cadastrar(dto);
        return Response.status(Response.Status.CREATED).entity(usuario).build();
    }

    @GET
    public Response listarTodos() throws SQLException {
        return Response.ok(usuarioService.listarTodos()).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        return Response.ok(usuarioService.buscarPorId(id)).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, @Valid AtualizarUsuarioDto dto) throws SQLException, EntidadeNaoEncontradaException {
        ListarUsuarioDto usuario = usuarioService.atualizar(id, dto);
        return Response.ok(usuario).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        usuarioService.deletar(id);
        return Response.noContent().build();
    }
}
