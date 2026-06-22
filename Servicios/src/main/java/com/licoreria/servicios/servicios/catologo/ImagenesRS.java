package com.licoreria.servicios.servicios.catologo;

import com.licoreria.BusinessLayer.catalogo.ImagenBL;
import com.licoreria.BusinessLayer.catalogo.ImagenBLImpl;
import com.licoreria.dominio.catalogo.Imagen;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/imagenes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ImagenesRS {

    private final ImagenBL imagenBL;

    public ImagenesRS() {
        this.imagenBL = new ImagenBLImpl();
    }

    @GET
    public Response listarTodas() {
        try {
            List<Imagen> imagenes = imagenBL.getAll();
            return Response.ok(imagenes).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al obtener imágenes\"}").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id) {
        Imagen imagen = imagenBL.get(id);
        if (imagen == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(imagen).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            Imagen imagen = imagenBL.get(id);
            if (imagen == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
            imagenBL.delete(imagen);
            return Response.noContent().build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al eliminar la imagen\"}").build();
        }
    }
}