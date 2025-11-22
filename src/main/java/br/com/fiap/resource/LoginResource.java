package br.com.fiap.resource;

import br.com.fiap.dto.login.LoginDto;
import br.com.fiap.service.LoginService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("login")
@ApplicationScoped
public class LoginResource {

    @Inject
    private LoginService service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(@Valid LoginDto loginDto) {
        service.login(loginDto);
        return Response.ok().build();
    }
}
