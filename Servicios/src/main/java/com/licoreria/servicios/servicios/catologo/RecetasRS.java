package com.licoreria.servicios.servicios.catologo;

import com.licoreria.BusinessLayer.catalogo.RecetaBL;
import com.licoreria.BusinessLayer.catalogo.RecetaBLImpl;
import com.licoreria.DriveDriver.DriveDriver;
import com.licoreria.dominio.catalogo.Categoria;
import com.licoreria.dominio.catalogo.Receta;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.InputStream;

@Path("/recetas")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RecetasRS {

    private final RecetaBL recetaBL;

    public RecetasRS() {
        this.recetaBL = new RecetaBLImpl();
    }

    @GET
    public Response listarTodas() {
        try {
            return Response.ok(recetaBL.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id) {
        try {
            Receta receta = recetaBL.get(id);
            return (receta != null) ? Response.ok(receta).build() : Response.status(404).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    public Response crear(Receta receta) {
        try {
            return Response.status(Response.Status.CREATED).entity(recetaBL.save(receta)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, Receta receta) {
        try {
            receta.setId(id);
            return Response.ok(recetaBL.update(receta)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            recetaBL.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    @Path("/{id}/subir")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response subirImagen(@PathParam("id") int id,
                                @FormDataParam("archivo") InputStream archivo,
                                @FormDataParam("archivo") FormDataContentDisposition detalle) {
        Receta receta = recetaBL.get(id);
        if (receta == null) return Response.status(404).entity("Receta no encontrada").build();

        try {
            String url = DriveDriver.uploadInputStream(archivo, detalle.getFileName(), "image/png", "TU_ID_CARPETA_DRIVE");
            recetaBL.agregarImagen(receta, url);
            return Response.ok("Imagen subida correctamente").build();
        } catch (Exception e) {
            return Response.status(500).entity("Error: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("/{id}/subirPrincipal")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response subirImagenPrincipal(@PathParam("id") int id,
                                         @FormDataParam("archivo") InputStream archivo,
                                         @FormDataParam("archivo") FormDataContentDisposition detalle) {
        return Response.ok("Imagen principal actualizada").build();
    }


    @POST
    @Path("/{id}/categoria")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response agregarCategoria(@PathParam("id") int id, Categoria categoria) {
        Receta receta = recetaBL.get(id);
        if (receta == null) return Response.status(404).entity("Receta no encontrada").build();
        try {
            recetaBL.agregarCategoria(receta, categoria);
            return Response.ok("Categoría agregada").build();
        } catch (RuntimeException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}/categoria/{categoria}")
    public Response eliminarCategoria(@PathParam("id") int id, @PathParam("categoria") String nombreCategoria) {
        Receta receta = recetaBL.get(id);
        if (receta == null) return Response.status(404).entity("Receta no encontrada").build();
        try {
            recetaBL.eliminarCategoria(receta, nombreCategoria);
            return Response.ok("Categoría eliminada").build();
        } catch (RuntimeException e) {
            return Response.status(500).entity(e.getMessage()).build();
        }
    }
}