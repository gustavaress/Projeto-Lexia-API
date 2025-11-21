package br.com.fiap.resource;

import br.com.fiap.dto.empresa.AtualizarEmpresaDto;
import br.com.fiap.dto.empresa.CadastrarEmpresaDto;
import br.com.fiap.dto.empresa.ListarEmpresaDto;
import br.com.fiap.exception.EntidadeNaoEncontradaException;
import br.com.fiap.service.EmpresaService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;

@Path("/empresa")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class EmpresaResource {

    @Inject
    private EmpresaService empresaService;

    @POST
    public Response cadastrar(@Valid CadastrarEmpresaDto dto) throws SQLException, EntidadeNaoEncontradaException {
        ListarEmpresaDto empresa = empresaService.cadastrar(dto);
        return Response.status(Response.Status.CREATED).entity(empresa).build();
    }

    @GET
    public Response listarTodos() throws SQLException {
        return Response.ok(empresaService.listarTodos()).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        return Response.ok(empresaService.buscarPorId(id)).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, @Valid AtualizarEmpresaDto dto) throws SQLException, EntidadeNaoEncontradaException {
        ListarEmpresaDto empresa = empresaService.atualizar(id, dto);
        return Response.ok(empresa).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) throws SQLException, EntidadeNaoEncontradaException {
        empresaService.deletar(id);
        return Response.noContent().build();
    }
}
