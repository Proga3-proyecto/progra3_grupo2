package com.licoreria.BusinessLayer.catalogo;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.DBmanager.TransactionContext;
import com.licoreria.dao.catalogo.*;
import com.licoreria.dominio.catalogo.Categoria;
import com.licoreria.dominio.catalogo.Imagen;
import com.licoreria.dominio.catalogo.Marca;
import com.licoreria.dominio.catalogo.Producto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductoBLImpl implements ProductoBL {

    ProductoDAO productoDAO;
    MarcaDAO daoMarca;
    CategoriaDAO daoCategoria;
    ImagenDAO imagenDAO;

    public ProductoBLImpl() {
        productoDAO = new ProductoDAOImpl();
        daoMarca = new MarcaDAOImpl();
        daoCategoria = new CategoriaDAOImpl();
        imagenDAO = new ImagenDAOImpl();
    }

    @Override
    public List<Producto> getAll() {
        try (Connection con = DBManager.getInstance().getConnection()) {
            List<Producto> productos = productoDAO.getAll(con);
            for (Producto producto : productos) {
                producto.setMarca(daoMarca.get(con, producto.getId()));
                producto.setCategorias(daoCategoria.getAllByProducto(con, producto));
                producto.setImagenes(imagenDAO.getAllByProduct(con, producto));
            }
            return productos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Producto get(int id) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            Producto producto = productoDAO.get(con, id);
            if (producto == null) return null;
            producto.setMarca(daoMarca.get(con, producto.getId()));
            producto.setCategorias(daoCategoria.getAllByProducto(con, producto));
            producto.setImagenes(imagenDAO.getAllByProduct(con, producto));
            return producto;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(Producto producto) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            productoDAO.remove(con, producto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(int id) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            Producto producto = this.get(id);
            if (producto == null) {
                throw new RuntimeException("No se encontroProducto");
            }
            productoDAO.remove(con, producto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Producto save(Producto producto) {
        try {
            Connection con = TransactionContext.getConnection();

            try {
                if (producto.getMarca() != null) {
                    String marcaNombre = producto.getMarca().getNombre();
                    Marca marca = null;
                    if (marcaNombre != null) {
                        marca = daoMarca.get(con, marcaNombre);
                    }
                    if (marca == null) {
                        marca = daoMarca.save(con, producto.getMarca());
                    }
                    producto.setMarca(marca);
                }
                productoDAO.save(con, producto);
                if (producto.getCategorias() != null) {
                    for (Categoria categoria : producto.getCategorias()) {
                        String categoriaNombre = categoria.getNombre();
                        Categoria cateDB = null;

                        if (categoriaNombre != null) {
                            cateDB = daoCategoria.get(con, categoriaNombre);
                        }

                        if (cateDB == null) {
                            cateDB = daoCategoria.save(con, categoria);
                        }
                        categoria.setId(cateDB.getId());
                        productoDAO.asignarCategoria(con, producto, cateDB);
                    }
                }
                TransactionContext.commit();
                return producto;
            } catch (SQLException e) {
                TransactionContext.rollback();
                throw new RuntimeException(e);
            } finally {
                TransactionContext.close();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Producto update(Producto producto) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            return productoDAO.update(con, producto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void agregarImagen(Producto producto, String url) {
        Imagen imagen = new Imagen(url);
        try {
            Connection con = TransactionContext.getConnection();
            try {
                Imagen imageDB = imagenDAO.get(con, url);
                if (imageDB == null) {
                    imagenDAO.save(con, imagen);
                } else {
                    throw new RuntimeException("La imagen ya esta cargada");
                }

                productoDAO.cargarImagen(con, producto, imagen);
                TransactionContext.commit();
            } catch (SQLException e) {
                TransactionContext.rollback();
                throw new RuntimeException(e);
            } finally {
                TransactionContext.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void agregarImagenPrincipal(Producto producto, String url) {
        Imagen imagen = new Imagen(url);
        try {
            Connection con = TransactionContext.getConnection();
            try {
                Imagen imageDB = imagenDAO.get(con, url);
                if (imageDB == null) {
                    imagenDAO.save(con, imagen);
                } else {
                    throw new RuntimeException("La imagen ya esta cargada");
                }

                productoDAO.asignarImagenPrincipal(con, producto, imagen);
                TransactionContext.commit();
            } catch (SQLException e) {
                TransactionContext.rollback();
                throw new RuntimeException(e);
            } finally {
                TransactionContext.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removerImagen(int idProducto, int idImagen) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                productoDAO.removerImagen(con, idProducto, idImagen);
                TransactionContext.commit();
            } catch (SQLException e) {
                TransactionContext.rollback();
                throw new RuntimeException(e);
            } finally {
                TransactionContext.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void eliminarCategoria(Producto producto, String nombreCategoria) {
        try {
            Connection con = TransactionContext.getConnection();

            try {
                Categoria categoria = daoCategoria.get(con, nombreCategoria);
                if (categoria == null)
                    throw new RuntimeException("No se encontro categoria");
                productoDAO.removerCategoria(con, producto, categoria);
                TransactionContext.commit();
            } catch (SQLException e) {
                TransactionContext.rollback();
                throw new RuntimeException(e);
            } finally {
                TransactionContext.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void agregarCategoria(Producto producto, Categoria categoria) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                Categoria cat = daoCategoria.get(con, categoria.getNombre());
                if (cat == null)
                    daoCategoria.save(con, categoria);
                productoDAO.asignarCategoria(con, producto, categoria);
                TransactionContext.commit();
            } catch (SQLException e) {
                TransactionContext.rollback();
                throw new RuntimeException(e);
            } finally {
                TransactionContext.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void actualizarMarca(Producto producto, Marca marca) {
        try {
            Connection con = TransactionContext.getConnection();
            try {

                Marca m = daoMarca.get(con, marca.getNombre());
                if (m == null)
                    daoMarca.save(con, marca);
                else marca = m;

                producto.setMarca(marca);

                productoDAO.update(con, producto);


                TransactionContext.commit();
            } catch (SQLException e) {
                TransactionContext.rollback();
                throw new RuntimeException(e);
            } finally {
                TransactionContext.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}