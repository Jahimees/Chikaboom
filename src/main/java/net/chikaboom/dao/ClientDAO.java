package net.chikaboom.dao;

import net.chikaboom.connection.ConnectionPool;
import net.chikaboom.model.Client;
import net.chikaboom.util.QueryBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static net.chikaboom.constant.FieldConstant.*;
import static net.chikaboom.constant.TableConstant.CLIENT;


public class ClientDAO extends AbstractDAO<Client> {
    private final QueryBuilder queryBuilder;

    public ClientDAO() {
        queryBuilder = new QueryBuilder();
    }

    @Override
    public List<Client> findAll() {
        String query = queryBuilder.select().from(CLIENT).build();
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Client
                > clientList = new ArrayList<>();
        try {
            PreparedStatement findAllStatement = connection.prepareStatement(query);
            ResultSet resultSet = findAllStatement.executeQuery();

            while (resultSet.next()) {
                Client client = new Client();
                setFieldsToEntity(client, resultSet);
                clientList.add(client);
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

        return clientList;
    }

    @Override
    public Client findEntity(String id) {
        String query = queryBuilder.select().from(CLIENT).where(ID_CLIENT).build();
        Connection connection = ConnectionPool.getInstance().getConnection();
        Client client = new Client();
        try {
            PreparedStatement findEntityStatement = connection.prepareStatement(query);
            findEntityStatement.setString(1, id);
            ResultSet resultSet = findEntityStatement.executeQuery();

            if (resultSet.next()) {
                setFieldsToEntity(client, resultSet); //TODO обработка
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

        if (!client.getIdClient().equals(id)) {
            return null;
        }

        return client;
    }

    @Override
    public boolean delete(String id) {
        String query = queryBuilder.delete().from(CLIENT).where(ID_CLIENT).build();
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
    public boolean update(Client entity) {
        String query = queryBuilder.update(CLIENT, CLIENT_FIELDS).where(ID_CLIENT).build();
        Connection connection = ConnectionPool.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            setFieldsToPreparedStatement(preparedStatement, entity);
            preparedStatement.setString(3, entity.getIdClient());

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
    public boolean create(Client entity) {
        String query = queryBuilder.insert(CLIENT, CLIENT_FIELDS).build();
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
    public void setFieldsToPreparedStatement(PreparedStatement preparedStatement, Client entity) {
        try {
            preparedStatement.setString(1, entity.getIdClient());
            preparedStatement.setString(2, entity.getIdAccount());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); //TODO обработка
        }
    }


    @Override
    public void setFieldsToEntity(Client client, ResultSet resultSet) throws SQLException {
        client.setIdClient(resultSet.getString(ID_CLIENT));
        client.setIdAccount(resultSet.getString(ID_ACCOUNT));
    }
}
