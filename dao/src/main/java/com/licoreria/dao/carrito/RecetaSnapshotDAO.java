package com.licoreria.dao.carrito;

import com.licoreria.dao.BaseDAO;
import com.licoreria.dominio.Snapshots.RecetaSnapshot;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface RecetaSnapshotDAO extends BaseDAO<RecetaSnapshot, Integer> {
    Map<Integer, RecetaSnapshot> getMapByIds(Connection con, List<Integer> idsSnapshots) throws SQLException;
}