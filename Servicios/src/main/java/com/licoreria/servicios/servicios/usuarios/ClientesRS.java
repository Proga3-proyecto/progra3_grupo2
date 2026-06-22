package com.licoreria.servicios.servicios.usuarios;

import com.licoreria.BusinessLayer.usuarios.ClienteBL;
import com.licoreria.BusinessLayer.usuarios.ClienteBLImpl;
import com.licoreria.dominio.catalogo.Producto;
import com.licoreria.dominio.catalogo.Receta;
import com.licoreria.dominio.usuarios.Cliente;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/clientes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ClientesRS {

    private final ClienteBL clienteBL;

    public ClientesRS() {
        this.clienteBL = new ClienteBLImpl();
    }

    @GET
    public Response listarTodos() {
        try {
            return Response.ok(clienteBL.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    public Response registrar(Cliente cliente) {
        try {
            return Response.status(Response.Status.CREATED).entity(clienteBL.save(cliente)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, Cliente cliente) {
        try {
            cliente.setIdUsuario(id);
            return Response.ok(clienteBL.update(cliente)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            clienteBL.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("/{id}/carritoProductos")
    public Response obtenerCarritoProductos(@PathParam("id") int id) {
        try {
            List<Producto> productos = clienteBL.getProductosEnCarrito(id);
            return Response.ok(productos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al obtener el carrito del cliente: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/{id}/carritoRecetas")
    public Response obtenerCarritoRecetas(@PathParam("id") int id) {
        try {
            List<Receta> recetas = clienteBL.getRecetasEnCarrito(id);
            return Response.ok(recetas).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al obtener las recetas del carrito: " + e.getMessage() + "\"}")
                    .build();
        }
    }

}