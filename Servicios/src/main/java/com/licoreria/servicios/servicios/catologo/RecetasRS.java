package com.licoreria.servicios.servicios.catologo;

import com.licoreria.BusinessLayer.catalogo.RecetaBL;
import com.licoreria.BusinessLayer.catalogo.RecetaBLImpl;
import com.licoreria.SupabaseDriver.SupabaseDriver;
import com.licoreria.dominio.catalogo.Categoria;
import com.licoreria.dominio.catalogo.Imagen;
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
    private final SupabaseDriver supabaseDriver;

    public RecetasRS() {
        this.recetaBL = new RecetaBLImpl();
        this.supabaseDriver = new SupabaseDriver();
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
            String url = this.supabaseDriver.upload(detalle.getFileName(), archivo);
            Imagen imagen = recetaBL.agregarImagen(receta, url);
            return Response.ok(imagen).build();
        } catch (Exception e) {
            return Response.status(500).entity("Error: " + e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}/imagen/{idImagen}")
    public Response eliminarImagen(@PathParam("id") int idReceta, @PathParam("idImagen") int idImagen) {
        try {
            recetaBL.removerImagen(idReceta, idImagen);
            return Response.ok("Imagen eliminada: ").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al eliminar: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}/imagenPrincipal/{idImagen}")
    public Response asignarImagenPrincipal(@PathParam("id") int id, @PathParam("idImagen") int idImagen) {
        Receta receta = recetaBL.get(id);
        if (receta == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No encontro la receta").build();
        }
        try {
            recetaBL.agregarImagenPrincipal(receta, idImagen);
            return Response.ok("Imagen asignada").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al asignar: " + e.getMessage()).build();
        }
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