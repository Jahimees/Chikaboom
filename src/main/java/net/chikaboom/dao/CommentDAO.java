package net.chikaboom.dao;

import net.chikaboom.connection.ConnectionPool;
import net.chikaboom.model.Comment;
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

@Service
public class CommentDAO extends AbstractDAO<Comment> {

    private static final Logger logger = Logger.getLogger(CommentDAO.class);
    private final QueryBuilder queryBuilder;
    private ConnectionPool connectionPool;

    @Autowired
    public CommentDAO(ConnectionPool connectionPool) {
        super(connectionPool);
        queryBuilder = new QueryBuilder();
    }

    @Override
    public List<Comment> findAll() {
        String query = queryBuilder.select().from(COMMENT).build();
        Connection connection = connectionPool.getConnection();
        List<Comment> commentList = new ArrayList<>();
        try {
            PreparedStatement findAllStatement = connection.prepareStatement(query);
            ResultSet resultSet = findAllStatement.executeQuery();

            while (resultSet.next()) {
                Comment comment = new Comment();
                setFieldsToEntity(comment, resultSet);
                commentList.add(comment);
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

        return commentList;
    }

    @Override
    public Comment findEntity(String id) {
        String query = queryBuilder.select().from(COMMENT).where(ID_COMMENT).build();
        Connection connection = connectionPool.getConnection();
        Comment comment = new Comment();
        try {
            PreparedStatement findEntityStatement = connection.prepareStatement(query);
            findEntityStatement.setString(1, id);
            ResultSet resultSet = findEntityStatement.executeQuery();

            if (resultSet.next()) {
                setFieldsToEntity(comment, resultSet);
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

        if (!comment.getIdComment().equals(id)) {
            return null;
        }

        return comment;
    }

    @Override
    public boolean delete(String id) {
        String query = queryBuilder.delete().from(COMMENT).where(ID_COMMENT).build();
        Connection connection = connectionPool.getConnection();

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
    public boolean update(Comment entity) {
        String query = queryBuilder.update(COMMENT, COMMENT_FIELDS).where(ID_COMMENT).build();
        Connection connection = connectionPool.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            setFieldsToPreparedStatement(preparedStatement, entity);
            preparedStatement.setString(7, entity.getIdComment());

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
    public boolean create(Comment entity) {
        String query = queryBuilder.insert(COMMENT, COMMENT_FIELDS).build();
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
    public void setFieldsToPreparedStatement(PreparedStatement preparedStatement, Comment entity) {
        try {
            preparedStatement.setString(1, entity.getIdComment());
            preparedStatement.setString(2, entity.getIdClient());
            preparedStatement.setString(3, entity.getIdMaster());
            preparedStatement.setString(4, entity.getMessage());
            preparedStatement.setBoolean(5, entity.isClientMessage());
            preparedStatement.setInt(6, entity.getRate());
        } catch (SQLException e) {
            logger.error(SETTING_PARAMETER_ERROR, e);
        }
    }


    @Override
    public void setFieldsToEntity(Comment entity, ResultSet resultSet) {
        try {
            entity.setIdComment(resultSet.getString(ID_COMMENT));
            entity.setIdClient(resultSet.getString(ID_CLIENT));
            entity.setIdMaster(resultSet.getString(ID_MASTER));
            entity.setMessage(resultSet.getString(MESSAGE));
            entity.setClientMessage(resultSet.getBoolean(IS_CLIENT_MESSAGE));
            entity.setRate(resultSet.getInt(RATE));
        } catch (SQLException e) {
            logger.error(GETTING_PARAMETER_ERROR, e);
        }
    }

    @Override
    protected Comment createEntity() {
        return new Comment();
    }
}
