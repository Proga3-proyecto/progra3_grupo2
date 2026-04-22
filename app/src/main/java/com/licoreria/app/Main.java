package com.licoreria.app;

import com.licoreria.dao.AdminDAO;
import com.licoreria.dao.ClienteDAO;
import com.licoreria.dao.ImpuestoDAO;
import com.licoreria.dao.ProductoDAO;
import com.licoreria.dao.impl.AdminDAOImpl;
import com.licoreria.dao.impl.ClienteDAOImpl;
import com.licoreria.dao.impl.ImpuestoDAOImpl;
import com.licoreria.dao.impl.ProductoDAOImpl;
import com.licoreria.dominio.productos.Impuesto;
import com.licoreria.dominio.productos.Producto;
import com.licoreria.dominio.productos.Receta;
import com.licoreria.dominio.productos.TipoImpuesto;
import com.licoreria.dominio.usuarios.Admin;
import com.licoreria.dominio.usuarios.Cliente;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.licoreria.dominio.productos.TipoImpuesto.PORCENTAJE;

public class Main {
    public static void main(String[] args) {

//        //------------------------------------
//
//        // 1) PRUEBA DEL CRUD DEL ADMIN
//
//        //------------------------------------
//
        Admin admin  = new Admin(
                "12323123",
                "sax",
                "adada123200@gmail.com",
                "99999999",
                "sax",
                new Date(2025, Calendar.MARCH,5),
                "####"
        );
        //------DECLARACION DEL DAO PARA MANEJAR EL ADMIN------
        AdminDAO adminDao = new AdminDAOImpl();
        //------GUARDAR EL USUARIO EN LA BD---------
        try{
            adminDao.save(admin);
        }catch (SQLException e){
            e.printStackTrace();
        }
        //---------BUSCAR UN ADMIN---------
        long id=1;
        Admin adminPrueba  = new Admin();
        try{
            adminPrueba=adminDao.get(id);
            System.out.print("dni: ");
            System.out.println(adminPrueba.getDni());
        }catch (SQLException e){
            e.printStackTrace();
        }
        //--------GUARDAR EL LISTADO DE TODOS LOS USUARIOS-------
        try{
            List<Admin> admins = adminDao.getAll();
            for(Admin admine: admins){
                System.out.print("nombre completo:   ");
                String result = admine.getNombre() + " "  + admine.getApellidoCompleto();
                System.out.println(result);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        //---------UPDATE Y REMOVE DE UN USUARIO DE LA BD---------
        try{
            adminPrueba.setApellidoCompleto("hola como estas");
            adminDao.update(adminPrueba);
            String result = adminPrueba.getNombre() + " "  + adminPrueba.getApellidoCompleto();
            System.out.println("nombre actualizado: ");
            System.out.println(result);
            adminDao.remove(adminPrueba);

        }catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("------------------------------------");




        //------------------------------------

        // 2) PRUEBA DEL CRUD DEL CLIENTE

        //------------------------------------

        Cliente cliente = new Cliente("3231231", "cliente1",
                "sdsada","32321","dsdsa",
                new Date(10,10,2026), "dsdsa");
        //---------------DECLARACION DEL DAO PARA MANEJAR EL CLIENTE-------------
        ClienteDAO clienteDao = new ClienteDAOImpl();
        //-------------------GUARDAR EL USUARIO EN LA BD----------------------
        try{
            clienteDao.save(cliente);
        }catch (SQLException e){
            e.printStackTrace();
        }
        //
        long id2=2;
        //-----------BUSCAR UN CLIENTE------------------
        Cliente clientePrueba  = new Cliente();
        try{
            clientePrueba=clienteDao.get(id2);
            System.out.print("dni: ");
            System.out.println(clientePrueba.getDni());
        }catch (SQLException e){
            e.printStackTrace();
        }
        //--------GUARDAR EL LISTADO DE TODOS LOS USUARIOS-------
        try{
            List<Cliente> clientess = clienteDao.getAll();
            for(Cliente clientee: clientess){
                System.out.print("nombre completo:   ");
                String result = clientee.getNombre() + " "  + clientee.getApellidoCompleto();
                System.out.println(result);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        //---------UPDATE Y REMOVE DE UN USUARIO DE LA BD---------
        try{
            clientePrueba.setApellidoCompleto("hola como estas");
            clienteDao.update(clientePrueba);
            String result = clientePrueba.getNombre() + " "  + clientePrueba.getApellidoCompleto();
            System.out.println("nombre actualizado: ");
            System.out.println(result);
            clienteDao.remove(clientePrueba);

        }catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("------------------------------------");

        //------------------------------------

        // 3) PRUEBA DEL CRUD DEL PRODUCTO

        //------------------------------------

        Producto producto = new Producto();
        producto.setNombre("prod 1");
        producto.setPrecio(123.3);
        producto.setStock(120);
        producto.setDescuento(200);
        producto.setId(1);
        producto.setPorcentajeAlcohol(0.5);

        //------DECLARACION DEL DAO PARA MANEJAR EL PRODUCTO------
        ProductoDAO prodDao = new ProductoDAOImpl();
        //------GUARDAR EL PRODUCTO EN LA BD---------
        try{
            prodDao.save(producto);
        }catch (SQLException e){
            e.printStackTrace();
        }
        //------VAMOS A INTRODUCIR MAS PRODUCTOS PARA POSTERIOMENTE USARLOS EN LA RECETA-----
        for (int i = 2; i < 15; i++) {
            producto.setNombre("prod " + i );
            producto.setPrecio(123.3);
            producto.setStock(120);
            producto.setDescuento(200);
            producto.setPorcentajeAlcohol(0.5);

            try{
                prodDao.save(producto);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        //-----------BUSCAR UN PRODUCTO------------------
        Producto productoPrueba  = new Producto();
        long id3=1;
        try{
            productoPrueba=prodDao.get(id3);
            System.out.print("nombre : ");
            System.out.println(productoPrueba.getNombre());
        }catch (SQLException e){
            e.printStackTrace();
        }
        //--------GUARDAR EL LISTADO DE TODOS LOS USUARIOS-------
        try{
            List<Producto> productos = prodDao.getAll();
            for(Producto productoo: productos){
                String result = productoo.getNombre() + " "  + productoo.getPrecio();
                System.out.println(result);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        //---------UPDATE Y REMOVE DE UN PRODUCTO DE LA BD---------
        try{
            productoPrueba.setPrecio(50.7);
            prodDao.update(productoPrueba);
            String result = productoPrueba.getNombre() + " "  + productoPrueba.getPrecio();
            System.out.println("PRECIO actualizado: ");
            System.out.println(result);
            prodDao.remove(productoPrueba);

        }catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("------------------------------------");

        //------------------------------------

        // 4) PRUEBA DEL CRUD DE LA IMPUESTO

        //------------------------------------

        Impuesto impuesto = new Impuesto();
        impuesto.setId((long) (1));
        impuesto.setNombre("Impuesto 1");
        impuesto.setValor((double)100); // valor entre 0 y 100
        impuesto.setTipo(PORCENTAJE);
        impuesto.setActivo(true);

        //------DECLARACION DEL DAO PARA MANEJAR EL IMPUESTO------
        ImpuestoDAO impuestDao = new ImpuestoDAOImpl();
        //------GUARDAR EL USUARIO EN LA BD---------
        try{
            impuestDao.save(impuesto);
        }catch (SQLException e){
            e.printStackTrace();
        }
        //---------BUSCAR UN IMPUESTO---------
        long id5=1;
        Impuesto impuestoPrueba  = new Impuesto();
        try{
            impuestoPrueba=impuestDao.get(id5);
            System.out.print("id: ");
            System.out.println(impuestoPrueba.getId());
        }catch (SQLException e){
            e.printStackTrace();
        }
        //--------GUARDAR EL LISTADO DE TODOS LOS IMPUESTOS-------
        try{
            List<Impuesto> impuestos = impuestDao.getAll();
            for(Impuesto impuestoo: impuestos){
                System.out.print("id:   ");
                String result = impuestoo.getNombre() + " "  + impuestoo.getId();
                System.out.println(result);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        //---------UPDATE Y REMOVE DE UN IMPUESTO DE LA BD---------
        try{
            impuestoPrueba.setNombre("impuestoReal");
            impuestDao.update(impuestoPrueba);
            String result = impuestoPrueba.getNombre() + " "  + impuestoPrueba.getId();
            System.out.println("Nombre actualizado: ");
            System.out.println(result);
            impuestDao.remove(impuestoPrueba);

        }catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println("------------------------------------");

    }
}