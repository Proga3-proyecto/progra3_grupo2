package com.licoreria.app;

import com.licoreria.app.dao.adminDAO.AdminDAO;
import com.licoreria.app.dao.adminDAO.AdminDAOImpl;
import com.licoreria.app.dao.clienteDAO.ClienteDAO;
import com.licoreria.app.dao.clienteDAO.ClienteDAOImpl;
import com.licoreria.app.dao.detalleProductoDAO.DetalleProductoDAO;
import com.licoreria.app.dao.detalleProductoDAO.DetalleProductoDAOImpl;
import com.licoreria.app.dao.productoDAO.ProductoDAO;
import com.licoreria.app.dao.productoDAO.ProductoDAOImpl;
import com.licoreria.app.dao.recetaDAO.RecetaDAO;
import com.licoreria.app.dao.recetaDAO.RecetaDAOImpl;
import com.licoreria.app.modelo.pedidos.DetalleProducto;
import com.licoreria.app.modelo.productos.ElementoReceta;
import com.licoreria.app.modelo.productos.Producto;
import com.licoreria.app.modelo.productos.Receta;
import com.licoreria.app.modelo.usuarios.Admin;
import com.licoreria.app.modelo.usuarios.Cliente;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {
    public static void main(String[] args) {
//        Admin admin  = new Admin(
//                "12323",
//                "sax",
//                "adada@gmail.com",
//                "99999999",
//                "sax",
//                new Date(10,8,2025),
//                "####"
//        );
//
//         AdminDAO adminDao = new AdminDAOImpl();
//         adminDao.get()
//        List<Admin> admins = adminDao.getAll();

//        for(Admin admin: admins){
//            String result = admin.getNombre() + " "  + admin.getApellidoCompleto();
//            System.out.println(result);
//        }


//        adminDao.save(admin);

        //Admin admin = adminDao.get(1);
//        admin.setApellidoCompleto("hola como estas");
//        adminDao.update(admin);
        //adminDao.delete(admin);

       // Cliente cliente = new Cliente("3231231", "sax 123","sdsada","32321","dsdsa",new Date(10,10,2026), "dsdsa");
        ClienteDAO clienteDao = new ClienteDAOImpl();
//        clienteDao.save(cliente);
//        clienteDao.delete(cliente);
        //cliente.setId(4);
        //clienteDao.delete(cliente);

//        Producto producto = new Producto();
//        producto.setNombre("prod 1");
//        producto.setPrecio(123.3);
//        producto.setStock(120);
//        producto.setDescuento(200);
//        producto.setId(1);
        ProductoDAO prodDao = new ProductoDAOImpl();
        //prodDao.delete(producto);
        //prodDao.update(producto);

//        for (int i = 0; i < 30; i++) {
//            Producto producto = new Producto();
//            producto.setNombre("prod " + i );
//            producto.setPrecio(123.3);
//            producto.setStock(120);
//            producto.setDescuento(200);
//            prodDao.save(producto);
//        }

        List<Producto> productos = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Producto producto = prodDao.get(i + 2);
            productos.add(producto);
        }
//
//        Receta receta = new Receta();
//        receta.setNombre("coltel 1");
//        receta.setDescripcion("dadasd");
//        receta.setElementos(elementos);
        RecetaDAO recetaDAO = new RecetaDAOImpl();
//        recetaDAO.save(receta);

        Receta receta = recetaDAO.get(1);


        DetalleProducto detalleProducto  = new DetalleProducto(productos.get(0),20);

        DetalleProductoDAO dtProdDao = new DetalleProductoDAOImpl();

        //dtProdDao.save(detalleProducto,null, (long) 5);
       List<DetalleProducto> carrProd =  dtProdDao.getByCarrito(5);

        System.out.println("hola");

    }
}