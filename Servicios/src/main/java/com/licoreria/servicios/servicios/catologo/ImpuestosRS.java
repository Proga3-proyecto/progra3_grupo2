package com.licoreria.servicios.servicios.catologo;
import com.licoreria.BusinessLayer.catalogo.ImpuestoBL;
import com.licoreria.BusinessLayer.catalogo.ImpuestoBLImpl;
import com.licoreria.dominio.catalogo.Impuesto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/impuestos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ImpuestosRS {

    private final ImpuestoBL bl;

    public ImpuestosRS() {
        this.bl = new ImpuestoBLImpl();
    }

    @GET
    public Response listarTodos() {
        try {
            return Response.ok(bl.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @POST
    public Response crear(Impuesto impuesto) {
        try {
            return Response.status(Response.Status.CREATED).entity(bl.save(impuesto)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, Impuesto impuesto) {
        try {
            impuesto.setId(id);
            return Response.ok(bl.update(impuesto)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            bl.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }
}