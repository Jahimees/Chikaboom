package net.chikaboom.dao;

import net.chikaboom.connection.ConnectionPool;
import net.chikaboom.model.Work;
import net.chikaboom.util.QueryBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static net.chikaboom.constant.FieldConstant.*;
import static net.chikaboom.constant.TableConstant.SERVICE_TYPE;
import static net.chikaboom.constant.TableConstant.WORK;

public class WorkDAO extends AbstractDAO<Work> {

    private final QueryBuilder queryBuilder;

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
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); //TODO обработка
        } finally {
            try {
                connection.close();
            } catch (SQLException sqlException) {
                sqlException.printStackTrace(); //TODO обработка
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
                setFieldsToEntity(work, resultSet); //TODO обработка
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
    public boolean update(Work entity) {
        String query = queryBuilder.update(WORK, WORK_FIELDS).where(ID_WORK).build();
        Connection connection = ConnectionPool.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            setFieldsToPreparedStatement(preparedStatement, entity);
            preparedStatement.setString(5, entity.getIdWork());

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
    public boolean create(Work entity) {
        String query = queryBuilder.insert(WORK, WORK_FIELDS).build();
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
    public void setFieldsToPreparedStatement(PreparedStatement preparedStatement, Work entity) {
        try {
            preparedStatement.setString(1, entity.getIdWork());
            preparedStatement.setString(2, entity.getIdMaster());
            preparedStatement.setBytes(3, entity.getImage());
            preparedStatement.setString(4, entity.getComment());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); //TODO обработка
        }
    }

    @Override
    public void setFieldsToEntity(Work entity, ResultSet resultSet) throws SQLException {
        entity.setIdWork(resultSet.getString(ID_WORK));
        entity.setIdMaster(resultSet.getString(ID_MASTER));
        entity.setImage(resultSet.getBytes(IMAGE));
        entity.setComment(resultSet.getString(COMMENT));
    }
}
