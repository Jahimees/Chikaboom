package net.chikaboom.dao;

import net.chikaboom.connection.ConnectionPool;
import net.chikaboom.model.Account;
import net.chikaboom.util.QueryBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static net.chikaboom.constant.FieldConstant.*;
import static net.chikaboom.constant.TableConstant.ACCOUNT;

public class AccountDAO extends AbstractDAO<Account> {
    private final QueryBuilder queryBuilder;

    public AccountDAO() {
        queryBuilder = new QueryBuilder();
    }

    @Override
    public List<Account> findAll() {
        String query = queryBuilder.select().from(ACCOUNT).build();
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Account> accountList = new ArrayList<>();
        try {
            PreparedStatement findAllStatement = connection.prepareStatement(query);
            ResultSet resultSet = findAllStatement.executeQuery();

            while (resultSet.next()) {
                Account account = new Account();
                setFieldsToEntity(account, resultSet);
                accountList.add(account);
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

        return accountList;
    }

    @Override
    public Account findEntity(String id) {
        String query = queryBuilder.select().from(ACCOUNT).where(ID_ACCOUNT).build();
        Connection connection = ConnectionPool.getInstance().getConnection();
        Account account = new Account();
        try {
            PreparedStatement findEntityStatement = connection.prepareStatement(query);
            findEntityStatement.setString(1, id);
            ResultSet resultSet = findEntityStatement.executeQuery();

            if (resultSet.next()) {
                setFieldsToEntity(account, resultSet); //TODO обработка
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

        if (!account.getIdAccount().equals(id)) {
            return null;
        }

        return account;
    }

    @Override
    public boolean delete(String id) {
        String query = queryBuilder.delete().from(ACCOUNT).where(ID_ACCOUNT).build();
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
    public boolean update(Account entity) {
        String query = queryBuilder.update(ACCOUNT, getFieldNames()).where(ID_ACCOUNT).build();
        Connection connection = ConnectionPool.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            setFieldsToPreparedStatement(preparedStatement, entity);
            preparedStatement.setString(7, entity.getIdAccount());

            return preparedStatement.execute();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); //TODO обработка
        }

        return false;
    }

    @Override
    public boolean create(Account entity) {
        String query = queryBuilder.insert(ACCOUNT, getFieldNames()).build();
        Connection connection = ConnectionPool.getInstance().getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            setFieldsToPreparedStatement(preparedStatement, entity);

            return preparedStatement.execute();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return false;
    }

    @Override
    public List<String> getFieldNames() {
        List<String> fieldNames = new ArrayList<>();

        fieldNames.add(ID_ACCOUNT);
        fieldNames.add(NAME);
        fieldNames.add(SURNAME);
        fieldNames.add(LOGIN);
        fieldNames.add(PASSWORD);
        fieldNames.add(PHONE);

        return fieldNames;
    }

    @Override
    public void setFieldsToPreparedStatement(PreparedStatement preparedStatement, Account entity) {
        try {
            preparedStatement.setString(1, entity.getIdAccount());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getSurname());
            preparedStatement.setString(4, entity.getLogin());
            preparedStatement.setString(5, entity.getPassword());
            preparedStatement.setString(6, entity.getPhone());
        } catch (SQLException sqlException) {
            sqlException.printStackTrace(); //TODO обработка
        }
    }


    @Override
    public void setFieldsToEntity(Account account, ResultSet resultSet) throws SQLException {
        account.setIdAccount(resultSet.getString(ID_ACCOUNT));
        account.setName(resultSet.getString(NAME));
        account.setSurname(resultSet.getString(SURNAME));
        account.setLogin(resultSet.getString(LOGIN));
        account.setPassword(resultSet.getString(PASSWORD));
        account.setPhone(resultSet.getString(PHONE));
    }
}
