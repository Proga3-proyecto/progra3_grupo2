package com.licoreria.servicios.servicios;
//import com.licoreria.BusinessLayer.ClienteService.IClienteService;
//import com.licoreria.BusinessLayer.ClienteService.ClienteServiceImpl;
//import com.licoreria.BusinessLayer.EntityNotFoundException;
//import com.licoreria.BusinessLayer.ValidationException;
//import com.licoreria.dominio.pedidos.DetalleProducto;
//import com.licoreria.dominio.pedidos.DetalleReceta;
//import com.licoreria.dominio.productos.Producto;
//import com.licoreria.dominio.productos.Receta;
//import com.licoreria.dominio.usuarios.Cliente;
//
//import jakarta.ws.rs.*;
//import jakarta.ws.rs.core.MediaType;
//import jakarta.ws.rs.core.Response;
//import jakarta.ws.rs.core.Response.Status;
//
//import java.util.List;
//
//@Path("/clientes")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
public class ClientesRS {

//    private final IClienteService clienteBO;
//
//    public ClientesRS() {
//        this.clienteBO = new ClienteServiceImpl();
//    }
//
//
//    @GET
//    public Response listarTodos() {
//        try {
//            List<Cliente> clientes = clienteBO.listarTodos();
//            return Response.ok(clientes).build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR)
//                    .entity("{\"error\": \"Error interno al listar los clientes\"}")
//                    .build();
//        }
//    }
//
//    @GET
//    @Path("/{id}")
//    public Response obtenerPorId(@PathParam("id") Long id) {
//        try {
//            Cliente cliente = clienteBO.obtenerPorId(id);
//            return Response.ok(cliente).build();
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"Error interno al buscar el cliente\"}").build();
//        }
//    }
//
//    @POST
//    public Response crear(Cliente cliente) {
//        try {
//            Cliente nuevoCliente = clienteBO.crear(cliente);
//            return Response.status(Status.CREATED).entity(nuevoCliente).build();
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"Error interno al registrar el cliente\"}").build();
//        }
//    }
//
//    @PUT
//    @Path("/{id}")
//    public Response actualizar(@PathParam("id") Long id, Cliente cliente) {
//        try {
//            cliente.setId(id);
//            Cliente clienteActualizado = clienteBO.actualizar(cliente);
//            return Response.ok(clienteActualizado).build();
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"Error interno al actualizar el cliente\"}").build();
//        }
//    }
//
//    @DELETE
//    @Path("/{id}")
//    public Response eliminar(@PathParam("id") Long id) {
//        try {
//            Cliente cliente = clienteBO.obtenerPorId(id);
//            clienteBO.eliminar(cliente);
//            return Response.noContent().build();
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"Error interno al eliminar el cliente\"}").build();
//        }
//    }
//
//    @GET
//    @Path("/{idCliente}/carrito/productos")
//    public Response obtenerCarritoProductos(@PathParam("idCliente") Long idCliente) {
//        try {
//            Cliente cliente = clienteBO.obtenerPorId(idCliente);
//            List<DetalleProducto> carritoProds = clienteBO.obtenerCarritoProductos(cliente);
//            return Response.ok(carritoProds).build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"Error al obtener el carrito de productos\"}").build();
//        }
//    }
//
//    @POST
//    @Path("/{idCliente}/carrito/productos")
//    public Response agregarProductoAlCarrito(@PathParam("idCliente") Long idCliente,
//                                             @QueryParam("idProducto") Long idProducto,
//                                             @QueryParam("cantidad") int cantidad) {
//        try {
//            Cliente cliente = clienteBO.obtenerPorId(idCliente);
//
//            Producto producto = new Producto();
//            producto.setId(idProducto);
//
//            clienteBO.agregarProductoAlCarrito(cliente, producto, cantidad);
//            return Response.ok("{\"mensaje\": \"Producto añadido al carrito con éxito\"}").build();
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"Error al añadir producto al carrito\"}").build();
//        }
//    }
//
//    @DELETE
//    @Path("/{idCliente}/carrito/productos/{idProducto}")
//    public Response eliminarProductoDelCarrito(@PathParam("idCliente") Long idCliente,
//                                               @PathParam("idProducto") Long idProducto) {
//        try {
//            Cliente cliente = clienteBO.obtenerPorId(idCliente);
//            clienteBO.eliminarProductoDelCarrito(cliente, idProducto);
//            return Response.noContent().build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"Error al remover el producto del carrito\"}").build();
//        }
//    }
//
//    @DELETE
//    @Path("/{idCliente}/carrito/productos")
//    public Response limpiarCarritoProductos(@PathParam("idCliente") Long idCliente) {
//        try {
//            Cliente cliente = clienteBO.obtenerPorId(idCliente);
//            clienteBO.limpiarCarritoProductos(cliente);
//            return Response.noContent().build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"Error al limpiar el carrito de productos\"}").build();
//        }
//    }
//
//    @GET
//    @Path("/{idCliente}/carrito/recetas")
//    public Response obtenerCarritoRecetas(@PathParam("idCliente") Long idCliente) {
//        try {
//            Cliente cliente = clienteBO.obtenerPorId(idCliente);
//            List<DetalleReceta> carritoRecetas = clienteBO.obtenerCarritoRecetas(cliente);
//            return Response.ok(carritoRecetas).build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"Error al obtener el carrito de recetas\"}").build();
//        }
//    }
//
//    @POST
//    @Path("/{idCliente}/carrito/recetas")
//    public Response agregarRecetaAlCarrito(@PathParam("idCliente") Long idCliente,
//                                           @QueryParam("idReceta") Long idReceta,
//                                           @QueryParam("cantidad") int cantidad) {
//        try {
//            Cliente cliente = clienteBO.obtenerPorId(idCliente);
//
//            Receta receta = new Receta();
//            receta.setId(idReceta);
//
//            clienteBO.agregarRecetaAlCarrito(cliente, receta, cantidad);
//            return Response.ok("{\"mensaje\": \"Combo/Receta añadida al carrito con éxito\"}").build();
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"Error al añadir receta al carrito\"}").build();
//        }
//    }
//
//    @DELETE
//    @Path("/{idCliente}/carrito/recetas/{idReceta}")
//    public Response eliminarRecetaDelCarrito(@PathParam("idCliente") Long idCliente,
//                                             @PathParam("idReceta") Long idReceta) {
//        try {
//            Cliente cliente = clienteBO.obtenerPorId(idCliente);
//            clienteBO.eliminarRecetaDelCarrito(cliente, idReceta);
//            return Response.noContent().build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"Error al remover la receta del carrito\"}").build();
//        }
//    }
//
//    @DELETE
//    @Path("/{idCliente}/carrito/recetas")
//    public Response limpiarCarritoRecetas(@PathParam("idCliente") Long idCliente) {
//        try {
//            Cliente cliente = clienteBO.obtenerPorId(idCliente);
//            // Recordar que limpia ambos carritos debido a la optimización unificada que hicimos en Service
//            clienteBO.limpiarCarritoRecetas(cliente);
//            return Response.noContent().build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND).entity("{\"error\": \"" + ex.getMessage() + "\"}").build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("{\"error\": \"Error al limpiar el carrito de recetas\"}").build();
//        }
//    }
}