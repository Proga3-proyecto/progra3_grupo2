package com.licoreria.servicios.servicios.catologo;
import com.licoreria.BusinessLayer.catalogo.AlcoholImpuestoBL;
import com.licoreria.BusinessLayer.catalogo.AlcoholImpuestoBLImpl;
import com.licoreria.dominio.catalogo.AlcoholImpuesto;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/alcohol_impuestos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AlcoholImpuestoRS {

    private final AlcoholImpuestoBL bl;

    public AlcoholImpuestoRS() {
        this.bl = new AlcoholImpuestoBLImpl();
    }

    @GET
    public Response listarTodos() {
        try {
            return Response.ok(bl.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    public Response crear(AlcoholImpuesto ai) {
        try {
            return Response.status(Response.Status.CREATED).entity(bl.save(ai)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, AlcoholImpuesto ai) {
        try {
            ai.setId(id);
            return Response.ok(bl.update(ai)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
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
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}