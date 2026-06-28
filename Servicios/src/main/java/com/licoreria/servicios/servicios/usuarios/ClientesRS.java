package com.licoreria.servicios.servicios.usuarios;

import com.licoreria.BusinessLayer.usuarios.ClienteBL;
import com.licoreria.BusinessLayer.usuarios.ClienteBLImpl;
import com.licoreria.dominio.catalogo.Producto;
import com.licoreria.dominio.catalogo.Receta;
import com.licoreria.dominio.usuarios.Cliente;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.licoreria.dto.CambiarPasswordRequest;

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

    @GET
    @Path("/{id}")
    public Response getClient(@PathParam("id") int id){
        try {
            return Response.ok(clienteBL.get(id)).build();
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

    @GET
    @Path("/{id}/pedidos")
    public Response obtenerPedidosPasados(@PathParam("id") int id) {
        try {
            List<com.licoreria.dominio.carrito.Pedido> pedidos = clienteBL.getPedidos(id);
            return Response.ok(pedidos).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al obtener los pedidos del cliente: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @PUT
    @Path("/{id}/password")
    public Response cambiarPassword(@PathParam("id") int id, CambiarPasswordRequest request) {
        try {
            Cliente cliente = clienteBL.get(id);
            if (cliente == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Cliente no encontrado\"}").build();
            }
            if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"La nueva contraseña es requerida\"}").build();
            }
            cliente.setContrasenaHash(request.getNewPassword());
            clienteBL.update(cliente);
            return Response.ok("{\"mensaje\":\"Contraseña actualizada correctamente\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    @Path("/{id}/carritoProductos")
    public Response agregarProductoAlCarrito(@PathParam("id") int id, com.licoreria.dto.AgregarAlCarritoRequest request) {
        try {
            if (request.getIdProducto() <= 0 || request.getCantidad() <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"ID de producto y cantidad deben ser mayores a 0\"}").build();
            }
            
            clienteBL.agregarProductoAlCarrito(id, request.getIdProducto(), request.getCantidad());
            return Response.status(Response.Status.CREATED)
                    .entity("{\"mensaje\":\"Producto agregado al carrito correctamente\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    @Path("/{id}/carritoRecetas")
    public Response agregarRecetaAlCarrito(@PathParam("id") int id, com.licoreria.dto.AgregarRecetaAlCarritoRequest request) {
        try {
            if (request.getIdReceta() <= 0 || request.getCantidad() <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"ID de receta y cantidad deben ser mayores a 0\"}").build();
            }
            
            clienteBL.agregarRecetaAlCarrito(id, request.getIdReceta(), request.getCantidad());
            return Response.status(Response.Status.CREATED)
                    .entity("{\"mensaje\":\"Receta agregada al carrito correctamente\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @DELETE
    @Path("/{id}/carritoProductos/{idProducto}")
    public Response eliminarProductoDelCarrito(@PathParam("id") int id, @PathParam("idProducto") int idProducto) {
        try {
            clienteBL.eliminarProductoDelCarrito(id, idProducto);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @DELETE
    @Path("/{id}/carritoRecetas/{idReceta}")
    public Response eliminarRecetaDelCarrito(@PathParam("id") int id, @PathParam("idReceta") int idReceta) {
        try {
            clienteBL.eliminarRecetaDelCarrito(id, idReceta);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @PUT
    @Path("/{id}/carritoProductos/{idProducto}")
    public Response actualizarCantidadProductoEnCarrito(@PathParam("id") int id, @PathParam("idProducto") int idProducto, com.licoreria.dto.ActualizarCantidadRequest request) {
        try {
            if (request.getCantidad() <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"La cantidad debe ser mayor a 0\"}").build();
            }
            clienteBL.actualizarCantidadProductoEnCarrito(id, idProducto, request.getCantidad());
            return Response.ok("{\"mensaje\":\"Cantidad actualizada correctamente\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @PUT
    @Path("/{id}/carritoRecetas/{idReceta}")
    public Response actualizarCantidadRecetaEnCarrito(@PathParam("id") int id, @PathParam("idReceta") int idReceta, com.licoreria.dto.ActualizarCantidadRequest request) {
        try {
            if (request.getCantidad() <= 0) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"La cantidad debe ser mayor a 0\"}").build();
            }
            clienteBL.actualizarCantidadRecetaEnCarrito(id, idReceta, request.getCantidad());
            return Response.ok("{\"mensaje\":\"Cantidad actualizada correctamente\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

}