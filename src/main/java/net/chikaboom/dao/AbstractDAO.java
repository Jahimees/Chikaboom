package net.chikaboom.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractDAO<E> {
    abstract List<E> findAll();
    abstract E findEntity(String id);
    abstract boolean delete(String id);
    abstract boolean update(E entity);
    abstract boolean create(E entity);

    abstract void setFieldsToPreparedStatement(PreparedStatement preparedStatement, E entity);
    abstract void setFieldsToEntity(E entity, ResultSet resultSet) throws SQLException;

}
