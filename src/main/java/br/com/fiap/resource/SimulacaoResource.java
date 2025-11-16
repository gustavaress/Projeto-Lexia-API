package br.com.fiap.resource;

import br.com.fiap.dto.simulacao.AtualizarSimulacaoDto;
import br.com.fiap.dto.simulacao.CadastrarSimulacaoDto;
import br.com.fiap.dto.simulacao.ListarSimulacaoDto;
import br.com.fiap.service.SimulacaoService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/simulacao")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SimulacaoResource {

    @Inject
    SimulacaoService simulacaoService;

    @POST
    public Response cadastrar(@Valid CadastrarSimulacaoDto dto) throws Exception {
        ListarSimulacaoDto simulacao = simulacaoService.cadastrar(dto);
        return Response.status(Response.Status.CREATED).entity(simulacao).build();
    }

    @GET
    public Response listarTodos() throws Exception {
        return Response.ok(simulacaoService.listarTodos()).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") int id) throws Exception {
        return Response.ok(simulacaoService.buscarPorId(id)).build();
    }

    @PUT
    @Path("/{id}")
    public Response atualizar(@PathParam("id") int id, @Valid AtualizarSimulacaoDto dto) throws Exception {
        ListarSimulacaoDto simulacao = simulacaoService.atualizar(id, dto);
        return Response.ok(simulacao).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deletar(@PathParam("id") int id) throws Exception {
        simulacaoService.deletar(id);
        return Response.noContent().build();
    }
}
