package net.chikaboom.dao;

import net.chikaboom.connection.ConnectionPool;
import net.chikaboom.model.Client;
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
import static net.chikaboom.util.constant.TableConstant.CLIENT;

@Service
public class ClientDAO extends AbstractDAO<Client> {

    private static final Logger logger = Logger.getLogger(ClientDAO.class);
    private final QueryBuilder queryBuilder;
    private ConnectionPool connectionPool;

    @Autowired
    public ClientDAO(ConnectionPool connectionPool) {
        super(connectionPool);
        queryBuilder = new QueryBuilder();
    }

    @Override
    public List<Client> findAll() {
        String query = queryBuilder.select().from(CLIENT).build();
        Connection connection = connectionPool.getConnection();
        List<Client> clientList = new ArrayList<>();
        try {
            PreparedStatement findAllStatement = connection.prepareStatement(query);
            ResultSet resultSet = findAllStatement.executeQuery();

            while (resultSet.next()) {
                Client client = new Client();
                setFieldsToEntity(client, resultSet);
                clientList.add(client);
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

        return clientList;
    }

    @Override
    public Client findEntity(String id) {
        String query = queryBuilder.select().from(CLIENT).where(ID_CLIENT).build();
        Connection connection = connectionPool.getConnection();
        Client client = new Client();
        try {
            PreparedStatement findEntityStatement = connection.prepareStatement(query);
            findEntityStatement.setString(1, id);
            ResultSet resultSet = findEntityStatement.executeQuery();

            if (resultSet.next()) {
                setFieldsToEntity(client, resultSet);
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

        if (!client.getIdClient().equals(id)) {
            return null;
        }

        return client;
    }

    @Override
    public boolean delete(String id) {
        String query = queryBuilder.delete().from(CLIENT).where(ID_CLIENT).build();
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
    public boolean update(Client entity) {
        String query = queryBuilder.update(CLIENT, CLIENT_FIELDS).where(ID_CLIENT).build();
        Connection connection = connectionPool.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            setFieldsToPreparedStatement(preparedStatement, entity);
            preparedStatement.setString(3, entity.getIdClient());

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
    public boolean create(Client entity) {
        String query = queryBuilder.insert(CLIENT, CLIENT_FIELDS).build();
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
    public void setFieldsToPreparedStatement(PreparedStatement preparedStatement, Client entity) {
        try {
            preparedStatement.setString(1, entity.getIdClient());
            preparedStatement.setString(2, entity.getIdAccount());
        } catch (SQLException e) {
            logger.error(SETTING_PARAMETER_ERROR, e);
        }
    }


    @Override
    public void setFieldsToEntity(Client client, ResultSet resultSet) {
        try {
            client.setIdClient(resultSet.getString(ID_CLIENT));
            client.setIdAccount(resultSet.getString(ID_ACCOUNT));
        } catch (SQLException e) {
            logger.error(GETTING_PARAMETER_ERROR, e);
        }
    }

    @Override
    protected Client createEntity() {
        return new Client();
    }
}
