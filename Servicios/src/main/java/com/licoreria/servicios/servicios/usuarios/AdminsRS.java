package com.licoreria.servicios.servicios.usuarios;

import com.licoreria.BusinessLayer.usuarios.AdminBL;
import com.licoreria.BusinessLayer.usuarios.AdminBLImpl;
import com.licoreria.dominio.usuarios.Admin;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.licoreria.dto.CambiarPasswordRequest;

@Path("/administradores")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminsRS {

    private final AdminBL adminBL;

    public AdminsRS() {
        this.adminBL = new AdminBLImpl();
    }




    @GET
    public Response listarTodos() {
        try {
            return Response.ok(adminBL.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    public Response registrar(Admin admin) {
        try {
            return Response.status(Response.Status.CREATED).entity(adminBL.save(admin)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, Admin admin) {
        try {
            admin.setIdUsuario(id);
            return Response.ok(adminBL.update(admin)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            adminBL.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @PUT
    @Path("/{id}/password")
    public Response cambiarPassword(@PathParam("id") int id, CambiarPasswordRequest request) {
        try {
            Admin admin = adminBL.get(id);
            if (admin == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Administrador no encontrado\"}").build();
            }
            if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"La nueva contraseña es requerida\"}").build();
            }
            admin.setContrasenaHash(request.getNewPassword());
            adminBL.update(admin);
            return Response.ok("{\"mensaje\":\"Contraseña actualizada correctamente\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}