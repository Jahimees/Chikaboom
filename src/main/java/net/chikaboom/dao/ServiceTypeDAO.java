package net.chikaboom.dao;

import net.chikaboom.connection.ConnectionPool;
import net.chikaboom.model.ServiceType;
import net.chikaboom.util.QueryBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static net.chikaboom.constant.FieldConstant.*;
import static net.chikaboom.constant.TableConstant.SERVICE_TYPE;

public class ServiceTypeDAO extends AbstractDAO<ServiceType> {

    private final QueryBuilder queryBuilder;

    public ServiceTypeDAO() {
        queryBuilder = new QueryBuilder();
    }

    @Override
    public List<ServiceType> findAll() {
        Connection connection = ConnectionPool.getInstance().getConnection();
        String query = queryBuilder.select().from(SERVICE_TYPE).build();
        List<ServiceType> serviceTypeList = new ArrayList<>();
        try {
            PreparedStatement findAllStatement = connection.prepareStatement(query);
            ResultSet resultSet = findAllStatement.executeQuery();

            while (resultSet.next()) {
                ServiceType serviceType = new ServiceType();
                setFieldsToEntity(serviceType, resultSet);
                serviceTypeList.add(serviceType);
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

        return serviceTypeList;
    }

    @Override
    public ServiceType findEntity(String id) {
        String query = queryBuilder.select().from(SERVICE_TYPE).where(ID_SERVICE_TYPE).build();
        Connection connection = ConnectionPool.getInstance().getConnection();
        ServiceType serviceType = new ServiceType();
        try {
            PreparedStatement findEntityStatement = connection.prepareStatement(query);
            findEntityStatement.setString(1, id);
            ResultSet resultSet = findEntityStatement.executeQuery();

            if (resultSet.next()) {
                setFieldsToEntity(serviceType, resultSet); //TODO обработка
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

        if (!serviceType.getIdServiceType().equals(id)) {
            return null;
        }

        return serviceType;
    }

    @Override
    public boolean delete(String id) {
        String query = queryBuilder.delete().from(SERVICE_TYPE).where(ID_SERVICE_TYPE).build();
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
    public boolean update(ServiceType entity) {
        String query = queryBuilder.update(SERVICE_TYPE, SERVICE_TYPE_FIELDS).where(ID_SERVICE_TYPE).build();
        Connection connection = ConnectionPool.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            setFieldsToPreparedStatement(preparedStatement, entity);
            preparedStatement.setString(3, entity.getIdServiceType());

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
    public boolean create(ServiceType entity) {
        String query = queryBuilder.insert(SERVICE_TYPE, SERVICE_TYPE_FIELDS).build();
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
    public void setFieldsToPreparedStatement(PreparedStatement preparedStatement, ServiceType entity) {
        try {
            preparedStatement.setString(1, entity.getIdServiceType());
            preparedStatement.setString(2, entity.getName());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); //TODO обработка
        }
    }

    @Override
    public void setFieldsToEntity(ServiceType entity, ResultSet resultSet) throws SQLException {
        entity.setIdServiceType(resultSet.getString(ID_SERVICE_TYPE));
        entity.setName(resultSet.getString(NAME));
    }
}
