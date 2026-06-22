package com.licoreria.servicios.servicios.catologo;
import com.licoreria.BusinessLayer.catalogo.MarcaBL;
import com.licoreria.BusinessLayer.catalogo.MarcaBLImpl;
import com.licoreria.dominio.catalogo.Marca;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/marcas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MarcasRS {

    private final MarcaBL marcaBL;

    public MarcasRS() {
        this.marcaBL = new MarcaBLImpl();
    }

    @GET
    public Response listarTodas() {
        try {
            return Response.ok(marcaBL.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @POST
    @Path("/{nombreMarca}")
    public Response crear(@PathParam("nombreMarca") String nombre) {
        try {
            Marca marca = new Marca();
            marca.setNombre(nombre);
            marcaBL.save(marca);
            return Response.status(Response.Status.CREATED).entity(marca).build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{nombreMarca}")
    public Response eliminar(@PathParam("nombreMarca") String nombre) {
        try {
            marcaBL.delete(nombre);
            return Response.noContent().build();
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}