package net.chikaboom.dao;

import net.chikaboom.connection.ConnectionPool;
import net.chikaboom.model.SubserviceType;
import net.chikaboom.util.sql.QueryBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static net.chikaboom.util.constant.FieldConstant.*;
import static net.chikaboom.util.constant.LoggerMessageConstant.*;
import static net.chikaboom.util.constant.TableConstant.SUBSERVICE_TYPE;

@Service
public class SubserviceTypeDAO extends AbstractDAO<SubserviceType> {

    private static final Logger logger = Logger.getLogger(SubserviceTypeDAO.class);
    private final QueryBuilder queryBuilder;
    private ConnectionPool connectionPool;

    @Autowired
    public SubserviceTypeDAO(ConnectionPool connectionPool) {
        super(connectionPool);
        queryBuilder = new QueryBuilder();
    }

    @Override
    public List<SubserviceType> findAll() {
        Connection connection = connectionPool.getConnection();
        String query = queryBuilder.select().from(SUBSERVICE_TYPE).build();
        List<SubserviceType> subserviceTypeList = new ArrayList<>();
        try {
            PreparedStatement findAllStatement = connection.prepareStatement(query);
            ResultSet resultSet = findAllStatement.executeQuery();

            while (resultSet.next()) {
                SubserviceType subserviceType = new SubserviceType();
                setFieldsToEntity(subserviceType, resultSet);
                subserviceTypeList.add(subserviceType);
            }
        } catch (SQLException e) {
            logger.error(QUERY_EXECUTION_ERROR);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error(CONNECTION_CLOSING_ERROR);
            }
        }
        return subserviceTypeList;
    }

    @Override
    public SubserviceType findEntity(String id) {
        String query = queryBuilder.select().from(SUBSERVICE_TYPE).where(ID_SUBSERVICE_TYPE).build();
        Connection connection = connectionPool.getConnection();
        SubserviceType subserviceType = new SubserviceType();
        try {
            PreparedStatement findEntityStatement = connection.prepareStatement(query);
            findEntityStatement.setString(1, id);
            ResultSet resultSet = findEntityStatement.executeQuery();

            if (resultSet.next()) {
                setFieldsToEntity(subserviceType, resultSet);
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

        if (!subserviceType.getIdSubserviceType().equals(id)) {
            return null;
        }

        return subserviceType;
    }

    @Override
    public boolean delete(String id) {
        String query = queryBuilder.delete().from(SUBSERVICE_TYPE).where(ID_SUBSERVICE_TYPE).build();
        Connection connection = connectionPool.getConnection();

        try {
            PreparedStatement deleteStatement = connection.prepareStatement(query);
            deleteStatement.setString(1, id);

            return deleteStatement.execute();
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
    public boolean update(SubserviceType entity) {
        String query = queryBuilder.update(SUBSERVICE_TYPE, SUBSERVICE_TYPE_FIELDS).where(ID_SUBSERVICE_TYPE).build();
        Connection connection = connectionPool.getConnection();

        try {
            PreparedStatement updateStatement = connection.prepareStatement(query);
            setFieldsToPreparedStatement(updateStatement, entity);
            updateStatement.setString(3, entity.getIdSubserviceType());

            return updateStatement.execute();
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
    boolean create(SubserviceType entity) {
        String query = queryBuilder.insert(SUBSERVICE_TYPE, SUBSERVICE_TYPE_FIELDS).build();
        Connection connection = connectionPool.getConnection();

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
    void setFieldsToPreparedStatement(PreparedStatement preparedStatement, SubserviceType entity) {
        try {
            preparedStatement.setString(1, ID_SUBSERVICE_TYPE);
            preparedStatement.setString(2, NAME);
            preparedStatement.setString(3, ID_SERVICE_TYPE);
        } catch (SQLException e) {
            logger.error(SETTING_PARAMETER_ERROR, e);
        }
    }

    @Override
    void setFieldsToEntity(SubserviceType entity, ResultSet resultSet) {
        try {
            entity.setIdSubserviceType(resultSet.getString(ID_SUBSERVICE_TYPE));
            entity.setName(resultSet.getString(NAME));
            entity.setIdServiceType(resultSet.getString(ID_SERVICE_TYPE));
        } catch (SQLException e) {
            logger.error(SETTING_PARAMETER_ERROR, e);
        }
    }

    @Override
    public SubserviceType createEntity() {
        return new SubserviceType();
    }
}
