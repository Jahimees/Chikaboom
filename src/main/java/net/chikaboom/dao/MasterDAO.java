package net.chikaboom.dao;

import net.chikaboom.connection.ConnectionPool;
import net.chikaboom.model.Master;
import net.chikaboom.util.QueryBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static net.chikaboom.constant.FieldConstant.*;
import static net.chikaboom.constant.TableConstant.MASTER;

public class MasterDAO extends AbstractDAO<Master> {

    private final QueryBuilder queryBuilder;

    public MasterDAO() {
        queryBuilder = new QueryBuilder();
    }

    @Override
    public List<Master> findAll() {
        String query = queryBuilder.select().from(MASTER).build();
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Master> masterList = new ArrayList<>();
        try {
            PreparedStatement findAllStatement = connection.prepareStatement(query);
            ResultSet resultSet = findAllStatement.executeQuery();

            while (resultSet.next()) {
                Master master = new Master();
                setFieldsToEntity(master, resultSet);
                masterList.add(master);
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); //TODO обработка
        } finally {
            try {
                connection.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace(); //TODO обработка
            }
        }

        return masterList;
    }

    @Override
    public Master findEntity(String id) {
        String query = queryBuilder.select().from(MASTER).where(ID_MASTER).build();
        Connection connection = ConnectionPool.getInstance().getConnection();
        Master master = new Master();
        try {
            PreparedStatement findEntityStatement = connection.prepareStatement(query);
            findEntityStatement.setString(1, id);
            ResultSet resultSet = findEntityStatement.executeQuery();

            if (resultSet.next()) {
                setFieldsToEntity(master, resultSet); //TODO обработка
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace(); //TODO обработка
            }
        }

        if (!master.getIdMaster().equals(id)) {
            return null;
        }

        return master;
    }

    @Override
    public boolean delete(String id) {
        String query = queryBuilder.delete().from(MASTER).where(ID_MASTER).build();
        Connection connection = ConnectionPool.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);

            return preparedStatement.execute();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); //TODO обработка
        } finally {
            try {
                connection.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace(); //TODO обработка
            }
        }

        return false;
    }

    @Override
    public boolean update(Master entity) {
        String query = queryBuilder.update(MASTER, MASTER_FIELDS).where(ID_MASTER).build();
        Connection connection = ConnectionPool.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            setFieldsToPreparedStatement(preparedStatement, entity);
            preparedStatement.setString(5, entity.getIdMaster());

            return preparedStatement.execute();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); //TODO обработка
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace(); //TODO обработка
            }
        }

        return false;
    }

    @Override
    public boolean create(Master entity) {
        String query = queryBuilder.insert(MASTER, MASTER_FIELDS).build();
        Connection connection = ConnectionPool.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            setFieldsToPreparedStatement(preparedStatement, entity);

            return preparedStatement.execute();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        } finally {
            try {
                connection.close(); //TODO обработка
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public void setFieldsToPreparedStatement(PreparedStatement preparedStatement, Master entity) {
        try {
            preparedStatement.setString(1, entity.getIdMaster());
            preparedStatement.setString(2, entity.getIdAccount());
            preparedStatement.setString(3, entity.getAddress());
            preparedStatement.setString(4, entity.getDescription());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); //TODO обработка
        }
    }

    @Override
    public void setFieldsToEntity(Master entity, ResultSet resultSet) throws SQLException {
        entity.setIdMaster(resultSet.getString(ID_MASTER));
        entity.setIdAccount(resultSet.getString(ID_ACCOUNT));
        entity.setAddress(resultSet.getString(ADDRESS));
        entity.setDescription(resultSet.getString(DESCRIPTION));
    }
}
