package net.chikaboom.dao;

import net.chikaboom.connection.ConnectionPool;
import net.chikaboom.model.Work;
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
import static net.chikaboom.constant.TableConstant.WORK;

//    TODO DOCUMENTATION
public class WorkDAO extends AbstractDAO<Work> {
    private final QueryBuilder queryBuilder;
    private static final Logger logger = Logger.getLogger(WorkDAO.class);

    public WorkDAO() {
        queryBuilder = new QueryBuilder();
    }

    @Override
    public List<Work> findAll() {
        Connection connection = ConnectionPool.getInstance().getConnection();
        String query = queryBuilder.select().from(WORK).build();
        List<Work> workList = new ArrayList<>();
        try {
            PreparedStatement findAllStatement = connection.prepareStatement(query);
            ResultSet resultSet = findAllStatement.executeQuery();

            while (resultSet.next()) {
                Work work = new Work();
                setFieldsToEntity(work, resultSet);
                workList.add(work);
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

        return workList;
    }

    @Override
    public Work findEntity(String id) {
        String query = queryBuilder.select().from(WORK).where(ID_WORK).build();
        Connection connection = ConnectionPool.getInstance().getConnection();
        Work work = new Work();
        try {
            PreparedStatement findEntityStatement = connection.prepareStatement(query);
            findEntityStatement.setString(1, id);
            ResultSet resultSet = findEntityStatement.executeQuery();

            if (resultSet.next()) {
                setFieldsToEntity(work, resultSet);
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

        if (!work.getIdWork().equals(id)) {
            return null;
        }

        return work;
    }

    @Override
    public boolean delete(String id) {
        String query = queryBuilder.delete().from(WORK).where(ID_WORK).build();
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
    public boolean update(Work entity) {
        String query = queryBuilder.update(WORK, WORK_FIELDS).where(ID_WORK).build();
        Connection connection = ConnectionPool.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            setFieldsToPreparedStatement(preparedStatement, entity);
            preparedStatement.setString(5, entity.getIdWork());

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
    public boolean create(Work entity) {
        String query = queryBuilder.insert(WORK, WORK_FIELDS).build();
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
    public void setFieldsToPreparedStatement(PreparedStatement preparedStatement, Work entity) {
        try {
            preparedStatement.setString(1, entity.getIdWork());
            preparedStatement.setString(2, entity.getIdMaster());
            preparedStatement.setBytes(3, entity.getImage());
            preparedStatement.setString(4, entity.getComment());
        } catch (SQLException e) {
            logger.error(SETTING_PARAMETER_ERROR, e);
        }
    }

    @Override
    public void setFieldsToEntity(Work entity, ResultSet resultSet) {
        try {
            entity.setIdWork(resultSet.getString(ID_WORK));
            entity.setIdMaster(resultSet.getString(ID_MASTER));
            entity.setImage(resultSet.getBytes(IMAGE));
            entity.setComment(resultSet.getString(COMMENT));
        } catch (SQLException e) {
            logger.error(GETTING_PARAMETER_ERROR, e);
        }
    }

    @Override
    protected Work createEntity() {
        return new Work();
    }
}
