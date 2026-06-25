package com.licoreria.servicios.servicios.carrito;

import com.licoreria.BusinessLayer.carrito.PedidoBL;
import com.licoreria.BusinessLayer.carrito.PedidoBLImpl;
import com.licoreria.dominio.carrito.EstadoPedido;
import com.licoreria.dominio.carrito.Pedido;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/pedidos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PedidoRS {

    private final PedidoBL pedidoBL;

    public PedidoRS() {
        this.pedidoBL = new PedidoBLImpl();
    }

    @GET
    public Response listarTodos() {
        try {
            return Response.ok(pedidoBL.getAll()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") int id) {
        try {
            Pedido pedido = pedidoBL.get(id);
            if (pedido != null) {
                return Response.ok(pedido).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Pedido no encontrado\"}").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    // NUEVO: Obtener pedidos por Cliente
    @GET
    @Path("/cliente/{idCliente}")
    public Response obtenerPorCliente(@PathParam("idCliente") int idCliente) {
        try {
            return Response.ok(pedidoBL.getPedidosPorCliente(idCliente)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    // NUEVO: Obtener pedidos por Estado
    @GET
    @Path("/estado/{estado}")
    public Response obtenerPorEstado(@PathParam("estado") String estadoStr) {
        try {
            EstadoPedido estado = EstadoPedido.valueOf(estadoStr.toUpperCase());
            return Response.ok(pedidoBL.getPedidosPorEstado(estado)).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Estado no válido\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    public Response registrar(Pedido pedido) {
        try {
            return Response.status(Response.Status.CREATED).entity(pedidoBL.save(pedido)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") int id, Pedido pedido) {
        try {
            pedido.setId(id);
            return Response.ok(pedidoBL.update(pedido)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    // NUEVO: Actualizar solo el estado del pedido (Usamos PATCH en lugar de PUT)
    @PATCH
    @Path("/{id}/estado")
    public Response actualizarEstado(@PathParam("id") int id, @QueryParam("nuevoEstado") String estadoStr) {
        try {
            if (estadoStr == null || estadoStr.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"Debe proporcionar el 'nuevoEstado' como query param\"}").build();
            }
            EstadoPedido nuevoEstado = EstadoPedido.valueOf(estadoStr.toUpperCase());
            pedidoBL.actualizarEstadoPedido(id, nuevoEstado);
            return Response.ok("{\"mensaje\":\"Estado actualizado correctamente\"}").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Estado no válido\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") int id) {
        try {
            pedidoBL.delete(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}