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
import java.util.Map;

public class ProductoBLImpl implements ProductoBL {

    ProductoDAO productoDAO;
    MarcaDAO marcaDAO;
    CategoriaDAO categoriaDAO;
    ImagenDAO imagenDAO;

    public ProductoBLImpl() {
        productoDAO = new ProductoDAOImpl();
        marcaDAO = new MarcaDAOImpl();
        categoriaDAO = new CategoriaDAOImpl();
        imagenDAO = new ImagenDAOImpl();
    }

    @Override
    public List<Producto> getAll() {
        try (Connection con = DBManager.getInstance().getConnection()) {
            List<Producto> productos = productoDAO.getAll(con);
            List<Integer> productosids = productos.stream().map(p -> p.getId()).toList();
            Map<Integer, List<Imagen>> imagenes = imagenDAO.getAllByProducts(con, productosids);
            List<Integer> marcasIds = productos.stream().map(p -> p.getMarca().getId()).toList();
            Map<Integer, List<Categoria>> categorias = categoriaDAO.getAllByProductos(con, productosids);
            Map<Integer, Marca> marcas = marcaDAO.getAllByProductos(con, marcasIds);
            for (Producto producto : productos) {
                producto.setMarca(marcas.get(producto.getMarca().getId()));
                producto.setCategorias(categorias.get(producto.getId()));
                producto.setImagenes(imagenes.get(producto.getId()));
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
            producto.setMarca(marcaDAO.get(con, producto.getMarca().getId()));
            producto.setCategorias(categoriaDAO.getAllByProducto(con, producto));
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
        try {
            Connection con = TransactionContext.getConnection();
            try {
                Producto producto = productoDAO.get(con, id);
                if (producto == null) {
                    throw new RuntimeException("No se encontroProducto");
                }
                
                List<Imagen> imagenes = imagenDAO.getAllByProduct(con, producto);
                if (imagenes != null) {
                    for (Imagen img : imagenes) productoDAO.removerImagen(con, producto, img);
                }
                
                List<Categoria> categorias = categoriaDAO.getAllByProducto(con, producto);
                if (categorias != null) {
                    for (Categoria cat : categorias) productoDAO.removerCategoria(con, producto, cat);
                }
                
                productoDAO.remove(con, producto);
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
    public Producto save(Producto producto) {
        try {
            Connection con = TransactionContext.getConnection();

            try {
                if (producto.getMarca() != null) {
                    String marcaNombre = producto.getMarca().getNombre();
                    Marca marca = null;
                    if (marcaNombre != null) {
                        marca = marcaDAO.get(con, marcaNombre);
                    }
                    if (marca == null) {
                        marca = marcaDAO.save(con, producto.getMarca());
                    }
                    producto.setMarca(marca);
                }
                productoDAO.save(con, producto);
                if (producto.getCategorias() != null) {
                    for (Categoria categoria : producto.getCategorias()) {
                        String categoriaNombre = categoria.getNombre();
                        Categoria cateDB = null;

                        if (categoriaNombre != null) {
                            cateDB = categoriaDAO.get(con, categoriaNombre);
                        }

                        if (cateDB == null) {
                            cateDB = categoriaDAO.save(con, categoria);
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
        try {
            Connection con = TransactionContext.getConnection();
            try {
                Producto existing = productoDAO.get(con, producto.getId());
                if (existing == null) throw new RuntimeException("Producto no encontrado");

                if (producto.getImpuestoBase() == null) producto.setImpuestoBase(existing.getImpuestoBase());
                if (producto.getImpuestoAlcohol() == null) producto.setImpuestoAlcohol(existing.getImpuestoAlcohol());

                if (producto.getMarca() != null) {
                    String marcaNombre = producto.getMarca().getNombre();
                    Marca marca = null;
                    if (marcaNombre != null) {
                        marca = marcaDAO.get(con, marcaNombre);
                    }
                    if (marca == null) {
                        marca = marcaDAO.save(con, producto.getMarca());
                    }
                    producto.setMarca(marca);
                }
                
                productoDAO.update(con, producto);
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
                Categoria categoria = categoriaDAO.get(con, nombreCategoria);
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
                Categoria cat = categoriaDAO.get(con, categoria.getNombre());
                if (cat == null)
                    categoriaDAO.save(con, categoria);
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

                Marca m = marcaDAO.get(con, marca.getNombre());
                if (m == null)
                    marcaDAO.save(con, marca);
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