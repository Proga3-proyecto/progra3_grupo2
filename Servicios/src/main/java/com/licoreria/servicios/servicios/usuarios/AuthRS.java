package com.licoreria.servicios.servicios.usuarios;

import com.licoreria.BusinessLayer.usuarios.AdminBL;
import com.licoreria.BusinessLayer.usuarios.AdminBLImpl;
import com.licoreria.BusinessLayer.usuarios.ClienteBL;
import com.licoreria.BusinessLayer.usuarios.ClienteBLImpl;
import com.licoreria.dominio.usuarios.Admin;
import com.licoreria.dominio.usuarios.Cliente;
import com.licoreria.dto.LoginRequest;
import com.licoreria.dto.LoginResponse;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthRS {
    private final AdminBL adminBL;
    private final ClienteBL clienteBL;

    public AuthRS() {
        this.adminBL = new AdminBLImpl();
        this.clienteBL = new ClienteBLImpl();
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        try {
            // Validar campos nulos o vacíos básicos
            if (request.getUsuario() == null || request.getPassword() == null ||
                    request.getUsuario().isEmpty() || request.getPassword().isEmpty()) {

                LoginResponse errorResponse = new LoginResponse(false, "Usuario y contraseña son requeridos.");
                return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
            }

            Admin admin = adminBL.validarCredenciales(request.getUsuario(), request.getPassword());
            if (admin != null) {
                LoginResponse successResponse = new LoginResponse(
                        admin.getIdUsuario(),
                        admin.getNombre(), // O el campo que use tu clase Admin
                        admin.isMaster() ? "MASTER" : "ADMIN",
                        true,
                        "Inicio de sesión exitoso como Administrador."
                );
                return Response.ok(successResponse).build();
            }

            Cliente cliente = clienteBL.validarCredenciales(request.getUsuario(), request.getPassword());
            if (cliente != null) {
                LoginResponse successResponse = new LoginResponse(
                        cliente.getIdUsuario(),
                        cliente.getNombre(),
                        "CLIENTE",
                        true,
                        "Inicio de sesión exitoso como Cliente."
                );
                return Response.ok(successResponse).build();
            }

            LoginResponse failResponse = new LoginResponse(false, "Usuario o contraseña incorrectos.");
            return Response.ok(failResponse).build();

        } catch (Exception e) {
            LoginResponse exceptionResponse = new LoginResponse(false, "Error interno en el servidor: " + e.getMessage());
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exceptionResponse).build();
        }
    }
}
