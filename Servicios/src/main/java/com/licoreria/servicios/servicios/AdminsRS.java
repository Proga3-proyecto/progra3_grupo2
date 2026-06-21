package com.licoreria.servicios.servicios;

//import com.licoreria.BusinessLayer.AdminService.IAdminService;
//import com.licoreria.BusinessLayer.AdminService.AdminServiceImpl;
//import com.licoreria.BusinessLayer.EntityNotFoundException;
//import com.licoreria.BusinessLayer.ValidationException;
//import com.licoreria.dominio.usuarios.Admin;
//
//import jakarta.ws.rs.*;
//import jakarta.ws.rs.core.MediaType;
//import jakarta.ws.rs.core.Response;
//import jakarta.ws.rs.core.Response.Status;
//import java.util.List;
//
//@Path("/admins") // Sustantivo en plural y minúsculas siguiendo la convención REST
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
public class AdminsRS {

//    private final IAdminService adminBO;
//
//    public AdminsRS() {
//        this.adminBO = new AdminServiceImpl();
//    }
//
//    /**
//     * GET /webresources/admins
//     * Devuelve el listado completo de administradores registrados en el sistema.
//     */
//    @GET
//    public Response listarTodos() {
//        try {
//            List<Admin> administradores = adminBO.listarTodos();
//            return Response.ok(administradores).build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR)
//                    .entity("{\"error\": \"Error interno al listar los administradores del sistema\"}")
//                    .build();
//        }
//    }
//
//    /**
//     * GET /webresources/admins/{id}
//     * Recupera la información detallada de un administrador específico por su ID.
//     */
//    @GET
//    @Path("/{id}")
//    public Response obtenerPorId(@PathParam("id") Long id) {
//        try {
//            Admin admin = adminBO.obtenerPorId(id);
//            return Response.ok(admin).build();
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST)
//                    .entity("{\"error\": \" " + ex.getMessage() + " \"}")
//                    .build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND)
//                    .entity("{\"error\": \" " + ex.getMessage() + " \"}")
//                    .build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR)
//                    .entity("{\"error\": \"Error interno al buscar el administrador\"}")
//                    .build();
//        }
//    }
//
//    /**
//     * POST /webresources/admins
//     * Registra un nuevo administrador en el sistema (Crea tanto su Usuario base como el rol Admin).
//     */
//    @POST
//    public Response crear(Admin admin) {
//        try {
//            Admin nuevoAdmin = adminBO.crear(admin);
//            return Response.status(Status.CREATED).entity(nuevoAdmin).build();
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST)
//                    .entity("{\"error\": \" " + ex.getMessage() + " \"}")
//                    .build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR)
//                    .entity("{\"error\": \"Error interno al registrar el nuevo administrador\"}")
//                    .build();
//        }
//    }
//
//    /**
//     * PUT /webresources/admins/{id}
//     * Actualiza los datos de un administrador existente usando el ID de la ruta.
//     */
//    @PUT
//    @Path("/{id}")
//    public Response actualizar(@PathParam("id") Long id, Admin admin) {
//        try {
//            // Aseguramos que el ID de la URL se sincronice con el objeto de dominio
//            admin.setId(id);
//            Admin adminActualizado = adminBO.actualizar(admin);
//            return Response.ok(adminActualizado).build();
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST)
//                    .entity("{\"error\": \" " + ex.getMessage() + " \"}")
//                    .build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND)
//                    .entity("{\"error\": \" " + ex.getMessage() + " \"}")
//                    .build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR)
//                    .entity("{\"error\": \"Error interno al actualizar la información del administrador\"}")
//                    .build();
//        }
//    }
//
//    /**
//     * DELETE /webresources/admins/{id}
//     * Elimina a un administrador del sistema basándose en su ID único de usuario.
//     */
//    @DELETE
//    @Path("/{id}")
//    public Response eliminar(@PathParam("id") Long id) {
//        try {
//            // Verificamos la existencia del administrador antes de proceder con la eliminación
//            Admin admin = adminBO.obtenerPorId(id);
//            adminBO.eliminar(admin);
//            return Response.noContent().build(); // HTTP 204 No Content para eliminaciones exitosas
//        } catch (ValidationException ex) {
//            return Response.status(Status.BAD_REQUEST)
//                    .entity("{\"error\": \" " + ex.getMessage() + " \"}")
//                    .build();
//        } catch (EntityNotFoundException ex) {
//            return Response.status(Status.NOT_FOUND)
//                    .entity("{\"error\": \" " + ex.getMessage() + " \"}")
//                    .build();
//        } catch (Exception ex) {
//            return Response.status(Status.INTERNAL_SERVER_ERROR)
//                    .entity("{\"error\": \"Error interno al intentar eliminar al administrador\"}")
//                    .build();
//        }
//    }
}