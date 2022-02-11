package net.chikaboom.dao;

import net.chikaboom.connection.ConnectionPool;
import net.chikaboom.model.Account;
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
import static net.chikaboom.util.constant.TableConstant.ACCOUNT;

@Service
public class AccountDAO extends AbstractDAO<Account> {

    private static final Logger logger = Logger.getLogger(AccountDAO.class);
    private ConnectionPool connectionPool;
    private final QueryBuilder queryBuilder;

    @Autowired
    public AccountDAO(ConnectionPool connectionPool) {
        super(connectionPool);
        queryBuilder = new QueryBuilder();
    }

    @Override
    public List<Account> findAll() {
        String query = queryBuilder.select().from(ACCOUNT).build();
        Connection connection = connectionPool.getConnection();
        List<Account> accountList = new ArrayList<>();
        try {
            PreparedStatement findAllStatement = connection.prepareStatement(query);
            ResultSet resultSet = findAllStatement.executeQuery();

            while (resultSet.next()) {
                Account account = new Account();
                setFieldsToEntity(account, resultSet);
                accountList.add(account);
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

        return accountList;
    }

    @Override
    public Account findEntity(String id) {
        String query = queryBuilder.select().from(ACCOUNT).where(ID_ACCOUNT).build();
        Connection connection = connectionPool.getConnection();
        Account account = new Account();
        try {
            PreparedStatement findEntityStatement = connection.prepareStatement(query);
            findEntityStatement.setString(1, id);
            ResultSet resultSet = findEntityStatement.executeQuery();

            if (resultSet.next()) {
                setFieldsToEntity(account, resultSet);
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

        if (!account.getIdAccount().equals(id)) {
            return null;
        }

        return account;
    }

    @Override
    public boolean delete(String id) {
        String query = queryBuilder.delete().from(ACCOUNT).where(ID_ACCOUNT).build();
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
    public boolean update(Account entity) {
        String query = queryBuilder.update(ACCOUNT, ACCOUNT_FIELDS).where(ID_ACCOUNT).build();
        Connection connection = connectionPool.getConnection();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            setFieldsToPreparedStatement(preparedStatement, entity);
            preparedStatement.setString(8, entity.getIdAccount());

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
    public boolean create(Account entity) {
        String query = queryBuilder.insert(ACCOUNT, ACCOUNT_FIELDS).build();
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
    public void setFieldsToPreparedStatement(PreparedStatement preparedStatement, Account entity) {
        try {
            preparedStatement.setString(1, entity.getIdAccount());
            preparedStatement.setString(2, entity.getName());
            preparedStatement.setString(3, entity.getSurname());
            preparedStatement.setString(4, entity.getEmail());
            preparedStatement.setString(5, entity.getPassword());
            preparedStatement.setString(6, entity.getPhone());
            preparedStatement.setTimestamp(7, entity.getRegistrationDate());
        } catch (SQLException e) {
            logger.error(SETTING_PARAMETER_ERROR, e);
        }
    }

    @Override
    public void setFieldsToEntity(Account account, ResultSet resultSet) {
        try {
            account.setIdAccount(resultSet.getString(ID_ACCOUNT));
            account.setName(resultSet.getString(NAME));
            account.setSurname(resultSet.getString(SURNAME));
            account.setEmail(resultSet.getString(EMAIL));
            account.setPassword(resultSet.getString(PASSWORD));
            account.setPhone(resultSet.getString(PHONE));
            account.setRegistrationDate(resultSet.getTimestamp(REGISTRATION_DATE));
        } catch (SQLException e) {
            logger.error(GETTING_PARAMETER_ERROR, e);
        }
    }

    @Override
    protected Account createEntity() {
        return new Account();
    }
}
