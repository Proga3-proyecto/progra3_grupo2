package com.licoreria.servicios.servicios;
//import com.licoreria.BusinessLayer.EntityNotFoundException;
//import com.licoreria.BusinessLayer.ValidationException;
//import com.licoreria.BusinessLayer.PedidoService.IPedidoService;
//import com.licoreria.BusinessLayer.PedidoService.PedidoServiceImpl;
//import com.licoreria.dominio.pedidos.Pedido;
//import com.licoreria.dominio.usuarios.Cliente;
//
//import jakarta.ws.rs.*;
//import jakarta.ws.rs.core.MediaType;
//import jakarta.ws.rs.core.Response;
//import jakarta.ws.rs.core.Response.Status;
//import java.util.List;
//
//@Path("/pedidos") // Sustantivo en plural y minúsculas
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
public class PedidosRS {

//    private final IPedidoService pedidoBO;
//
//    public PedidosRS() {
//        this.pedidoBO = new PedidoServiceImpl();
//    }
//
//
//    @GET
//    @Path("/{id}")
//    public Response obtenerPorId(@PathParam("id") Long id) {
//        try {
//            Pedido pedido = pedidoBO.obtenerPorId(id);
//            return Response.ok(pedido).build();
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"Error al recuperar el pedido\"}").build();
//        }
//    }
//
//    @GET
//    public Response listarTodos() {
//        try {
//            List<Pedido> pedidos = pedidoBO.listarTodos();
//            return Response.ok(pedidos).build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"Error al listar los pedidos\"}").build();
//        }
//    }
//
//
//    @GET
//    @Path("/cliente/{idCliente}")
//    public Response obtenerHistorialCliente(@PathParam("idCliente") Long idCliente) {
//        try {
//            Cliente cliente = new Cliente();
//            cliente.setId(idCliente);
//
//            List<Pedido> historial = pedidoBO.obtenerHistorialCliente(cliente);
//            return Response.ok(historial).build();
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"Error al recuperar el historial del cliente\"}").build();
//        }
//    }
//
//
//    @POST
//    @Path("/checkout")
//    public Response crearPedidoDesdeCarrito(@QueryParam("idCliente") Long idCliente,
//                                            @QueryParam("direccion") String direccionDestino) {
//        try {
//            Cliente cliente = new Cliente();
//            cliente.setId(idCliente);
//     Pedido pedidoProcesado = pedidoBO.crearPedidoDesdeCarrito(cliente, direccionDestino);
//
//            return Response.status(Status.CREATED).entity(pedidoProcesado).build(); // HTTP 201 Created
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        }
//    }
//
//    @PUT
//    @Path("/{id}/estado")
//    public Response cambiarEstado(@PathParam("id") Long id, @QueryParam("nuevoEstado") String nuevoEstado) {
//        try {
//            Pedido pedido = pedidoBO.obtenerPorId(id);
//            pedidoBO.cambiarEstado(pedido, nuevoEstado);
//
//            return Response.ok("{\"mensaje\": \"Estado de pedido actualizado a: " + nuevoEstado.toUpperCase() + "\"}").build();
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"Error al cambiar el estado del pedido\"}").build();
//        }
//    }
//
//
//    @POST
//    @Path("/{id}/cancelar")
//    public Response cancelarPedido(@PathParam("id") Long id) {
//        try {
//            Pedido pedido = pedidoBO.obtenerPorId(id);
//            pedidoBO.cancelarPedido(pedido);
//
//            return Response.ok("{\"mensaje\": \"El pedido ha sido cancelado con éxito\"}").build();
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"Error al intentar cancelar el pedido\"}").build();
//        }
//    }
}
