package net.chikaboom.dao;

import net.chikaboom.connection.ConnectionPool;
import net.chikaboom.model.Service;
import net.chikaboom.util.QueryBuilder;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static net.chikaboom.constant.FieldConstant.*;
import static net.chikaboom.constant.LoggerMessageConstant.*;
import static net.chikaboom.constant.TableConstant.SERVICE;

public class ServiceDAO extends AbstractDAO<Service> {
    private final QueryBuilder queryBuilder;
    private static final Logger logger = Logger.getLogger(ServiceDAO.class);

    public ServiceDAO() {
        queryBuilder = new QueryBuilder();
    }

    @Override
    public List<Service> findAll() {
        String query = queryBuilder.select().from(SERVICE).build();
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Service> serviceList = new ArrayList<>();
        try {
            PreparedStatement findAllStatement = connection.prepareStatement(query);
            ResultSet resultSet = findAllStatement.executeQuery();

            while (resultSet.next()) {
                Service service = new Service();
                setFieldsToEntity(service, resultSet);
                serviceList.add(service);
            }
        } catch (SQLException e) {
            logger.error(QUERY_EXECUTION_ERROR, e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(CONNECTION_CLOSING_ERROR, e);
            }
        }

        return serviceList;
    }

    @Override
    public Service findEntity(String id) {
        String query = queryBuilder.select().from(SERVICE).where(ID_SERVICE).build();
        Connection connection = ConnectionPool.getInstance().getConnection();
        Service service = new Service();
        try {
            PreparedStatement findEntityStatement = connection.prepareStatement(query);
            findEntityStatement.setString(1, id);
            ResultSet resultSet = findEntityStatement.executeQuery();

            if (resultSet.next()) {
                setFieldsToEntity(service, resultSet);
            }
        } catch (SQLException e) {
            logger.error(QUERY_EXECUTION_ERROR, e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(CONNECTION_CLOSING_ERROR, e);
            }
        }

        if (!service.getIdService().equals(id)) {
            return null;
        }

        return service;
    }

    @Override
    public boolean delete(String id) {
        String query = queryBuilder.delete().from(SERVICE).where(ID_SERVICE).build();
        Connection connection = ConnectionPool.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);

            return preparedStatement.execute();

        } catch (SQLException e) {
            logger.error(QUERY_EXECUTION_ERROR, e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(CONNECTION_CLOSING_ERROR, e);
            }
        }

        return false;
    }

    @Override
    public boolean update(Service entity) {
        String query = queryBuilder.update(SERVICE, SERVICE_FIELDS).where(ID_SERVICE).build();
        Connection connection = ConnectionPool.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            setFieldsToPreparedStatement(preparedStatement, entity);
            preparedStatement.setString(7, entity.getIdService());

            return preparedStatement.execute();

        } catch (SQLException e) {
            logger.error(QUERY_EXECUTION_ERROR, e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(CONNECTION_CLOSING_ERROR, e);
            }
        }

        return false;
    }

    @Override
    public boolean create(Service entity) {
        String query = queryBuilder.insert(SERVICE, SERVICE_FIELDS).build();
        Connection connection = ConnectionPool.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            setFieldsToPreparedStatement(preparedStatement, entity);

            return preparedStatement.execute();
        } catch (SQLException e) {
            logger.error(QUERY_EXECUTION_ERROR, e);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(CONNECTION_CLOSING_ERROR, e);
            }
        }

        return false;
    }

    @Override
    public void setFieldsToPreparedStatement(PreparedStatement preparedStatement, Service entity) {
        try {
            preparedStatement.setString(1, entity.getIdService());
            preparedStatement.setString(2, entity.getIdMaster());
            preparedStatement.setString(3, entity.getIdServiceType());
            preparedStatement.setString(4, entity.getName());
            preparedStatement.setString(5, entity.getDescription());
            preparedStatement.setDouble(6, entity.getCost());
        } catch (SQLException e) {
            logger.error(SETTING_PARAMETER_ERROR, e);
        }
    }

    @Override
    public void setFieldsToEntity(Service entity, ResultSet resultSet) {
        try {
            entity.setIdService(resultSet.getString(ID_SERVICE));
            entity.setIdMaster(resultSet.getString(ID_MASTER));
            entity.setIdServiceType(resultSet.getString(ID_SERVICE_TYPE));
            entity.setName(resultSet.getString(NAME));
            entity.setDescription(resultSet.getString(DESCRIPTION));
            entity.setCost(resultSet.getDouble(COST));
        } catch (SQLException e) {
            logger.error(GETTING_PARAMETER_ERROR, e);
        }
    }

    @Override
    protected Service createEntity() {
        return new Service();
    }
}
