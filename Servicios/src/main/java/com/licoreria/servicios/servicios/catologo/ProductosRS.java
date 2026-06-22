package com.licoreria.servicios.servicios.catologo;

import com.licoreria.BusinessLayer.EntityNotFoundException;
import com.licoreria.BusinessLayer.ValidationException;
import com.licoreria.BusinessLayer.catalogo.ProductoBL;
import com.licoreria.BusinessLayer.catalogo.ProductoBLImpl;
import com.licoreria.DriveDriver.DriveDriver;
import com.licoreria.dominio.catalogo.Categoria;
import com.licoreria.dominio.catalogo.Marca;
import com.licoreria.dominio.catalogo.Producto;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import java.io.InputStream;
import java.util.List;

@Path("/productos")
@MultipartConfig
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProductosRS {

    private final ProductoBL productoBO;

    public ProductosRS() {
        this.productoBO = new ProductoBLImpl();
    }

    @GET
    public Response listarTodos() {
        try {
            List<Producto> productos = productoBO.getAll();
            return Response.ok(productos).build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"error\":\"" + ex.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id) {
        try {
            Producto producto = productoBO.get(id);
            return Response.ok(producto).build();
        } catch (ValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"" + ex.getMessage() + "\"}").build();
        } catch (EntityNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"" + ex.getMessage() + "\"}").build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"error\":\"Error interno al buscar producto\"}").build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response crear(Producto producto) {
        try {
            Producto nuevoProducto = productoBO.save(producto);
            return Response.status(Response.Status.CREATED).entity(nuevoProducto).build();
        } catch (ValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"" + ex.getMessage() + "\"}").build();
        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"error\":\"Error interno al registrar producto\"}").build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizar(@PathParam("id") int id, Producto producto) {
        try {
            producto.setId(id);
            Producto actualizado = productoBO.update(producto);
            return Response.ok(actualizado).build();

        } catch (ValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"" + ex.getMessage() + "\"}").build();

        } catch (EntityNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"" + ex.getMessage() + "\"}").build();

        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"error\":\"Error interno al actualizar producto\"}").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            productoBO.delete(id);
            return Response.noContent().build();
        } catch (ValidationException ex) {
            return Response.status(Response.Status.BAD_REQUEST).entity("{\"error\":\"" + ex.getMessage() + "\"}").build();
        } catch (EntityNotFoundException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"" + ex.getMessage() + "\"}").build();

        } catch (Exception ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{\"error\":\"Error interno al eliminar producto\"}").build();
        }
    }

    @POST
    @Path("/{id}/imagen")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response subirImagen(@PathParam("id") int id, @FormDataParam("archivo") InputStream archivo, @FormDataParam("archivo") FormDataContentDisposition detalle) {
        Producto producto = productoBO.get(id);

        if (producto == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No encontro el producto").build();
        }
        try {
            String nombre = detalle.getFileName();
            String url = DriveDriver.uploadInputStream(archivo, nombre, "image/png", "1d-vRhALF4Myiz4BdeVqhKwEksfdokRca");
            productoBO.agregarImagen(producto, url);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al subir a Drive: " + e.getMessage()).build();
        }
        return Response.ok("Archivo recibido: ").build();
    }

    @DELETE
    @Path("/{id}/imagen/{idImagen}")
    public Response eliminarImagen(@PathParam("id") int idProducto, @PathParam("idImagen") int idImagen) {
        try {
            productoBO.removerImagen(idProducto, idImagen);
            return Response.ok("Imagen eliminada: ").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al subir a Drive: " + e.getMessage()).build();
        }
    }

    @POST
    @Path("/{id}/imagenPrincipal")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response subirImagenPrincipal(@PathParam("id") int id, @FormDataParam("archivo") InputStream archivo, @FormDataParam("archivo") FormDataContentDisposition detalle) {
        Producto producto = productoBO.get(id);

        if (producto == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No encontro el producto").build();
        }
        String nombre = detalle.getFileName();
        try {
            String url = DriveDriver.uploadInputStream(archivo, nombre, "image/png", "1d-vRhALF4Myiz4BdeVqhKwEksfdokRca");
            productoBO.agregarImagenPrincipal(producto, url);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al subir a Drive: " + e.getMessage()).build();
        }
        return Response.ok("Archivo recibido: " + nombre).build();
    }

    @DELETE
    @Path("/{id}/categoria/{categoria}")
    public Response eliminarCategoria(@PathParam("id") int id, @PathParam("categoria") String nombreCategoria) {
        Producto producto = productoBO.get(id);
        if (producto == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No encontro el producto").build();
        }
        try {
            productoBO.eliminarCategoria(producto, nombreCategoria);
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }

        return Response.ok("Categoria eliminada ").build();
    }

    @POST
    @Path("/{id}/categoria")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response agregarCategoria(@PathParam("id") int id, Categoria categoria) {
        Producto producto = productoBO.get(id);
        if (producto == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No encontro el producto").build();
        }
        try {
            productoBO.agregarCategoria(producto, categoria);
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }

        return Response.ok("Categoria agregada ").build();
    }

    @PUT
    @Path("/{id}/marca")
    public Response cambiarMarca(@PathParam("id") int id, Marca marca) {
        Producto producto = productoBO.get(id);
        if (producto == null) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("No encontro el producto").build();
        }
        try {
            productoBO.actualizarMarca(producto, marca);
        } catch (RuntimeException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
        return Response.ok("marca actualizada ").build();
    }
}