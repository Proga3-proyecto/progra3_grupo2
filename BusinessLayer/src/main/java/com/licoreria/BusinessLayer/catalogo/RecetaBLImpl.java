package com.licoreria.BusinessLayer.catalogo;

import com.licoreria.DBmanager.DBManager;
import com.licoreria.DBmanager.TransactionContext;
import com.licoreria.dao.catalogo.*;
import com.licoreria.dominio.catalogo.*;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecetaBLImpl implements RecetaBL {
    private final RecetaDAO recetaDAO;
    private final ImagenDAO imagenDAO;
    private final CategoriaDAO categoriaDAO;
    private final ProductoDAO productoDAO;
    public RecetaBLImpl() {
        this.recetaDAO = new RecetaDAOImpl();
        this.imagenDAO = new ImagenDAOImpl();
        this.categoriaDAO = new CategoriaDAOImpl();
        this.productoDAO = new ProductoDAOImpl();
    }

    @Override
    public List<Receta> getAll() {
        try (Connection con = DBManager.getInstance().getConnection()) {
            List<Receta> recetas = recetaDAO.getAll(con);
            if (recetas == null || recetas.isEmpty()) {
                return new ArrayList<>();
            }
            List<Integer> recetasIds = recetas.stream().map(Receta::getId).toList();
            Map<Integer, List<Imagen>> imagenes = imagenDAO.getAllByRecetas(con, recetasIds);
            Map<Integer, List<Categoria>> categorias = categoriaDAO.getAllByRecetas(con, recetasIds);
            List<Integer> idsProductos = recetas.stream()
                    .flatMap(r -> r.getElementos().stream()) // Extraemos los elementos de todas las recetas
                    .filter(e -> e.getProducto() != null)
                    .map(e -> e.getProducto().getId())
                    .distinct()
                    .toList();
            Map<Integer, Producto> productosMap = idsProductos.isEmpty() ?
                    new java.util.HashMap<>() :
                    productoDAO.getMapByIds(con, idsProductos);

            for (Receta receta : recetas) {
                receta.setCategorias(categorias.getOrDefault(receta.getId(), new ArrayList<>()));
                receta.setImagenes(imagenes.getOrDefault(receta.getId(), new ArrayList<>()));
                for (ElementoReceta elemento : receta.getElementos()) {
                    if (elemento.getProducto() != null) {
                        Producto productoCompleto = productosMap.get(elemento.getProducto().getId());
                        elemento.setProducto(productoCompleto);
                    }
                }
            }
            return recetas;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener recetas", e);
        }
    }

    @Override
    public Receta get(int id) {
        try (Connection con = DBManager.getInstance().getConnection()) {
            // 1. Obtener la receta (ya trae sus elementos desde el DAO)
            Receta receta = recetaDAO.get(con, id);

            if (receta == null) return null;

            // 2. Cargar sus categorías e imágenes
            receta.setCategorias(categoriaDAO.getAllByReceta(con, receta));
            receta.setImagenes(imagenDAO.getAllByReceta(con, receta));

            // 3. Extraer IDs de los productos de esta receta en particular
            List<Integer> idsProductos = receta.getElementos().stream()
                    .filter(e -> e.getProducto() != null)
                    .map(e -> e.getProducto().getId())
                    .distinct()
                    .toList();

            // 4. Buscar los productos completos
            Map<Integer, Producto> productosMap = idsProductos.isEmpty() ?
                    new java.util.HashMap<>() :
                    productoDAO.getMapByIds(con, idsProductos);

            // 5. Reemplazar los productos en los elementos
            for (ElementoReceta elemento : receta.getElementos()) {
                if (elemento.getProducto() != null) {
                    elemento.setProducto(productosMap.get(elemento.getProducto().getId()));
                }
            }

            return receta;
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener receta", e);
        }
    }
    @Override
    public Receta save(Receta receta) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                recetaDAO.save(con, receta);
                TransactionContext.commit();
                return receta;
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar receta", e);
        }
    }

    @Override
    public Receta update(Receta receta) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                recetaDAO.update(con, receta);
                TransactionContext.commit();
                return receta;
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar receta", e);
        }
    }

    @Override
    public void delete(int id) {
        try {
            Connection con = TransactionContext.getConnection();
            try {
                Receta receta = recetaDAO.get(con, id);
                if (receta != null) {
                    recetaDAO.remove(con, receta);
                }
                TransactionContext.commit();
            } catch (Exception e) {
                TransactionContext.rollback();
                throw e;
            } finally {
                TransactionContext.close();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar receta", e);
        }
    }

    @Override
    public void agregarImagen(Receta receta, String url) {
        Imagen imagen = new Imagen(url);
        try (Connection con = TransactionContext.getConnection()) {
            Imagen imageDB = imagenDAO.get(con, url);
            if (imageDB == null) imageDB = imagenDAO.save(con, imagen);

            recetaDAO.cargarImagen(con, receta, imageDB);
            TransactionContext.commit();
        } catch (Exception e) {
            TransactionContext.rollback();
            throw new RuntimeException("Error al agregar imagen", e);
        }
    }
    @Override
    public void agregarCategoria(Receta receta, Categoria categoria) {
        try (Connection con = TransactionContext.getConnection()) {
            // Asegurar que la categoría exista o crearla si es necesario
            Categoria catDB = categoriaDAO.get(con, categoria.getNombre());
            if (catDB == null) catDB = categoriaDAO.save(con, categoria);

            recetaDAO.asignarCategoria(con, receta, catDB);
            TransactionContext.commit();
        } catch (Exception e) {
            TransactionContext.rollback();
            throw new RuntimeException("Error al agregar categoría a la receta", e);
        }
    }

    @Override
    public void eliminarCategoria(Receta receta, String nombreCategoria) {
        try (Connection con = TransactionContext.getConnection()) {
            Categoria cat = categoriaDAO.get(con, nombreCategoria);
            if (cat == null) throw new RuntimeException("Categoría no encontrada");
            recetaDAO.removerCategoria(con, receta, cat);
            TransactionContext.commit();
        } catch (Exception e) {
            TransactionContext.rollback();
            throw new RuntimeException("Error al eliminar categoría", e);
        }
    }

}