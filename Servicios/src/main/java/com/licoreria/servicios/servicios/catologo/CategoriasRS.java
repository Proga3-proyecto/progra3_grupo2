package com.licoreria.servicios.servicios.catologo;

import com.licoreria.BusinessLayer.catalogo.CategoriaBL;
import com.licoreria.BusinessLayer.catalogo.CategoriaBLImpl;
import com.licoreria.dominio.catalogo.Categoria;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/categorias")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CategoriasRS {

    private final CategoriaBL categoriaBL;

    public CategoriasRS() {
        this.categoriaBL = new CategoriaBLImpl();
    }

    @GET
    public Response listarTodas() {
        try {
            return Response.ok(categoriaBL.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @POST
    @Path("/{nombreCategoria}")
    public Response crear(@PathParam("nombreCategoria") String nombre) {
        try {
            Categoria categoria = new Categoria();
            categoria.setNombre(nombre);
            categoriaBL.save(categoria);
            return Response.status(Response.Status.CREATED).entity(categoria).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{nombreCategoria}")
    public Response eliminar(@PathParam("nombreCategoria") String nombre) {
        try {
            categoriaBL.delete(nombre);
            return Response.noContent().build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}