package net.chikaboom.dao;

import net.chikaboom.connection.ConnectionPool;
import net.chikaboom.model.Comment;
import net.chikaboom.util.QueryBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static net.chikaboom.constant.FieldConstant.*;

public class CommentDAO extends AbstractDAO<Comment> {

    private final QueryBuilder queryBuilder;

    public CommentDAO() {
        queryBuilder = new QueryBuilder();
    }

    @Override
    public List<Comment> findAll() {
        String query = queryBuilder.select().from(COMMENT).build();
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Comment> commentList = new ArrayList<>();
        try {
            PreparedStatement findAllStatement = connection.prepareStatement(query);
            ResultSet resultSet = findAllStatement.executeQuery();

            while (resultSet.next()) {
                Comment comment = new Comment();
                setFieldsToEntity(comment, resultSet);
                commentList.add(comment);
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

        return commentList;
    }

    @Override
    public Comment findEntity(String id) {
        String query = queryBuilder.select().from(COMMENT).where(ID_COMMENT).build();
        Connection connection = ConnectionPool.getInstance().getConnection();
        Comment comment = new Comment();
        try {
            PreparedStatement findEntityStatement = connection.prepareStatement(query);
            findEntityStatement.setString(1, id);
            ResultSet resultSet = findEntityStatement.executeQuery();

            if (resultSet.next()) {
                setFieldsToEntity(comment, resultSet); //TODO обработка
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

        if (!comment.getIdComment().equals(id)) {
            return null;
        }

        return comment;
    }

    @Override
    public boolean delete(String id) {
        String query = queryBuilder.delete().from(COMMENT).where(ID_COMMENT).build();
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
    public boolean update(Comment entity) {
        String query = queryBuilder.update(COMMENT, COMMENT_FIELDS).where(ID_COMMENT).build();
        Connection connection = ConnectionPool.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            setFieldsToPreparedStatement(preparedStatement, entity);
            preparedStatement.setString(7, entity.getIdComment());

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
    public boolean create(Comment entity) {
        String query = queryBuilder.insert(COMMENT, COMMENT_FIELDS).build();
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
    public void setFieldsToPreparedStatement(PreparedStatement preparedStatement, Comment entity) {
        try {
            preparedStatement.setString(1, entity.getIdComment());
            preparedStatement.setString(2, entity.getIdClient());
            preparedStatement.setString(3, entity.getIdMaster());
            preparedStatement.setString(4, entity.getMessage());
            preparedStatement.setBoolean(5, entity.isClientMessage());
            preparedStatement.setInt(6, entity.getRate());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); //TODO обработка
        }
    }


    @Override
    public void setFieldsToEntity(Comment entity, ResultSet resultSet) throws SQLException {
        entity.setIdComment(resultSet.getString(ID_COMMENT));
        entity.setIdClient(resultSet.getString(ID_CLIENT));
        entity.setIdMaster(resultSet.getString(ID_MASTER));
        entity.setMessage(resultSet.getString(MESSAGE));
        entity.setClientMessage(resultSet.getBoolean(IS_CLIENT_MESSAGE));
        entity.setRate(resultSet.getInt(RATE));
    }

}
