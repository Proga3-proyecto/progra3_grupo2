package com.licoreria.dao.carrito;

import com.licoreria.dao.BaseDAO;
import com.licoreria.dominio.Snapshots.ProductoSnapshot;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ProductoSnapshotDAO extends BaseDAO<ProductoSnapshot, Integer> {
    Map<Integer, ProductoSnapshot> getMapByIds(Connection con, List<Integer> idsSnapshots) throws SQLException;
}