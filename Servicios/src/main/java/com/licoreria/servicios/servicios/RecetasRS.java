package com.licoreria.servicios.servicios;

//import com.licoreria.BusinessLayer.EntityNotFoundException;
//import com.licoreria.BusinessLayer.ValidationException;
//import com.licoreria.BusinessLayer.RecetaService.IRecetaService;
//import com.licoreria.BusinessLayer.RecetaService.RecetaServiceImpl;
//import com.licoreria.dominio.productos.Receta;
//
//import jakarta.ws.rs.*;
//import jakarta.ws.rs.core.MediaType;
//import jakarta.ws.rs.core.Response;
//import jakarta.ws.rs.core.Response.Status;
//
//import java.util.List;
//
//@Path("/recetas")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
public class RecetasRS {

//    private final IRecetaService recetaBO;
//
//    public RecetasRS() {
//        this.recetaBO = new RecetaServiceImpl();
//    }

//    @GET
//    public Response listarTodos() {
//        try {
//            List<Receta> recetas = recetaBO.listarTodas();
//            return Response.ok(recetas).build(); // HTTP 200 OK
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR)
//                    .entity("{\"error\": \"Error interno al listar las recetas\"}")
//                    .build();
//        }
//    }
//
//    @GET
//    @Path("/{id}")
//    public Response obtenerPorId(@PathParam("id") Long id) {
//        try {
//            Receta receta = recetaBO.obtenerPorId(id);
//            return Response.ok(receta).build();
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST)
//                    .entity("{\"error\": \"" + ex.getMessage() + "\"}")
//                    .build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND)
//                    .entity("{\"error\": \"" + ex.getMessage() + "\"}")
//                    .build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR)
//                    .entity("{\"error\": \"Error interno al buscar la receta\"}")
//                    .build();
//        }
//    }
//
//
//    @POST
//    public Response crear(Receta receta) {
//        try {
//            Receta nuevaReceta = recetaBO.crear(receta);
//            return Response.status(Status.CREATED).entity(nuevaReceta).build(); // HTTP 201 Created
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST)
//                    .entity("{\"error\": \"" + ex.getMessage() + "\"}")
//                    .build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR)
//                    .entity("{\"error\": \"Error interno al registrar la receta\"}")
//                    .build();
//        }
//    }
//
//    @PUT
//    @Path("/{id}")
//    public Response actualizar(@PathParam("id") Long id, Receta receta) {
//        try {
//            receta.setId(id);
//            Receta recetaActualizada = recetaBO.actualizar(receta);
//            return Response.ok(recetaActualizada).build();
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST)
//                    .entity("{\"error\": \"" + ex.getMessage() + "\"}")
//                    .build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND)
//                    .entity("{\"error\": \"" + ex.getMessage() + "\"}")
//                    .build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR)
//                    .entity("{\"error\": \"Error interno al actualizar la receta\"}")
//                    .build();
//        }
//    }
//
//
//    @DELETE
//    @Path("/{id}")
//    public Response eliminar(@PathParam("id") Long id) {
//        try {
//            Receta receta = recetaBO.obtenerPorId(id);
//            recetaBO.eliminar(receta);
//            return Response.noContent().build();
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST)
//                    .entity("{\"error\": \"" + ex.getMessage() + "\"}")
//                    .build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND)
//                    .entity("{\"error\": \"" + ex.getMessage() + "\"}")
//                    .build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR)
//                    .entity("{\"error\": \"Error interno al intentar eliminar la receta\"}")
//                    .build();
//        }
//    }
}