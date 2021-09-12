package net.chikaboom.dao;

import net.chikaboom.connection.ConnectionPool;
import net.chikaboom.model.*;
import net.chikaboom.util.QueryBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static net.chikaboom.constant.FieldConstant.*;
import static net.chikaboom.constant.TableConstant.COMMENT;
import static net.chikaboom.constant.TableConstant.*;

public class CommonDAO extends AbstractDAO {
    private final QueryBuilder queryBuilder;

    public CommonDAO() {
        queryBuilder = new QueryBuilder();
    }

    @Override
    public List<Entity> findAll(TableEnum table /*TODO rename*/) {
        String query = queryBuilder.select().from(table.getTableName()).build();
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<Entity> entityList = new ArrayList<>();
        try {
            PreparedStatement findAllStatement = connection.prepareStatement(query);
            ResultSet resultSet = findAllStatement.executeQuery();

            while (resultSet.next()) {
                Entity entity = table.getEntity();
                setFieldsToEntity(entity, resultSet);
                entityList.add(entity);
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

        return entityList;
    }

    @Override
    public Account findEntity(String id) {
        String query = queryBuilder.select().from(ACCOUNT).where(ID_ACCOUNT).build(); //???????
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
        String query = queryBuilder.delete().from(ACCOUNT).where(ID_ACCOUNT).build(); //????????
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
        String query = queryBuilder.update(ACCOUNT, getFieldNames()).where(ID_ACCOUNT).build(); //???????????
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

    public List<String> getFieldNames(TableEnum table) {
        List<String> fieldNames = new ArrayList<>();
        switch (table.getEntity().getClass().getSimpleName()) {
            case ACCOUNT:
                return ACCOUNT_FIELDS;
            case CLIENT:
                return CLIENT_FIELDS;
            case COMMENT:
                return COMMENT_FIELDS;
            case MASTER:
                return MASTER_FIELDS;
            case SERVICE:
                return SERVICE_FIELDS;
            case SERVICE_TYPE:
                return SERVICE_TYPE_FIELDS;
            case WORK:
                return WORK_FIELDS;
        }
        return null; //TODO Исправить
    }

//    @Override
//    public void setFieldsToPreparedStatement(PreparedStatement preparedStatement, TableEnum table) {
//        switch (table.getEntity().getClass().getSimpleName()) {
//            case ACCOUNT:
//                setFieldsToAccount((Account) table.getEntity(), resultSet);
//                break;
//            case CLIENT:
//                setFieldsToClient((Client) table.getEntity(), resultSet);
//                break;
//            case COMMENT:
//                setFieldsToComment((Comment) table.getEntity(), resultSet);
//                break;
//            case MASTER:
//                setFieldsToMaster((Master) table.getEntity(), resultSet);
//                break;
//            case SERVICE:
//                setFieldsToService((Service) table.getEntity(), resultSet);
//                break;
//            case SERVICE_TYPE:
//                setFieldsToServiceType((ServiceType) table.getEntity(), resultSet);
//                break;
//            case WORK:
//                setFieldsToWork((Work) table.getEntity(), resultSet);
//                break;
//        }
//        try {
//            preparedStatement.setString(1, entity.getIdAccount());
//            preparedStatement.setString(2, entity.getName());
//            preparedStatement.setString(3, entity.getSurname());
//            preparedStatement.setString(4, entity.getLogin());
//            preparedStatement.setString(5, entity.getPassword());
//            preparedStatement.setString(6, entity.getPhone());
//            preparedStatement.setTimestamp(7, entity.getRegistrationDate());
//        } catch (SQLException sqlException) {
//            sqlException.printStackTrace(); //TODO обработка
//        }
//    }


    //    @Override
    public void setFieldsToEntity(TableEnum table, ResultSet resultSet) throws SQLException {
        switch (table.getEntity().getClass().getSimpleName()) {
            case ACCOUNT:
                setFieldsToAccount((Account) table.getEntity(), resultSet);
                break;
            case CLIENT:
                setFieldsToClient((Client) table.getEntity(), resultSet);
                break;
            case COMMENT:
                setFieldsToComment((Comment) table.getEntity(), resultSet);
                break;
            case MASTER:
                setFieldsToMaster((Master) table.getEntity(), resultSet);
                break;
            case SERVICE:
                setFieldsToService((Service) table.getEntity(), resultSet);
                break;
            case SERVICE_TYPE:
                setFieldsToServiceType((ServiceType) table.getEntity(), resultSet);
                break;
            case WORK:
                setFieldsToWork((Work) table.getEntity(), resultSet);
                break;
        }

    }

    //    TODO ЭТО ЗАЛУПА КАКАЯ-то
    private void setFieldsToAccount(Account account, ResultSet resultSet) throws SQLException {
        account.setIdAccount(resultSet.getString(ID_ACCOUNT));
        account.setName(resultSet.getString(NAME));
        account.setSurname(resultSet.getString(SURNAME));
        account.setLogin(resultSet.getString(LOGIN));
        account.setPassword(resultSet.getString(PASSWORD));
        account.setPhone(resultSet.getString(PHONE));
        account.setRegistrationDate(resultSet.getTimestamp(REGISTRATION_DATE));
    }

    private void setFieldsToClient(Client client, ResultSet resultSet) throws SQLException {
        client.setIdClient(resultSet.getString(ID_CLIENT));
        client.setIdAccount(resultSet.getString(ID_ACCOUNT));
    }

    private void setFieldsToComment(Comment comment, ResultSet resultSet) throws SQLException {
        comment.setIdComment(resultSet.getString(ID_COMMENT));
        comment.setIdClient(resultSet.getString(ID_CLIENT));
        comment.setIdMaster(resultSet.getString(ID_MASTER));
        comment.setClientMessage(resultSet.getBoolean(IS_CLIENT_MESSAGE));
        comment.setMessage(resultSet.getString(MESSAGE));
        comment.setRate(resultSet.getInt(RATE));
    }

    private void setFieldsToMaster(Master master, ResultSet resultSet) throws SQLException {
        master.setIdMaster(resultSet.getString(ID_MASTER));
        master.setIdAccount(resultSet.getString(ID_ACCOUNT));
        master.setAddress(resultSet.getString(ADDRESS));
        master.setDescription(resultSet.getString(DESCRIPTION));
    }

    private void setFieldsToService(Service service, ResultSet resultSet) throws SQLException {
        service.setIdService(resultSet.getString(ID_SERVICE));
        service.setIdServiceType(resultSet.getString(ID_SERVICE_TYPE));
        service.setIdMaster(resultSet.getString(ID_MASTER));
        service.setName(resultSet.getString(NAME));
        service.setDescription(resultSet.getString(DESCRIPTION));
        service.setCost(resultSet.getDouble(COST));
    }

    private void setFieldsToServiceType(ServiceType serviceType, ResultSet resultSet) throws SQLException {
        serviceType.setIdServiceType(resultSet.getString(ID_SERVICE_TYPE));
        serviceType.setName(resultSet.getString(NAME));
    }

    private void setFieldsToWork(Work work, ResultSet resultSet) throws SQLException {
        work.setIdWork(resultSet.getString(ID_WORK));
        work.setComment(resultSet.getString(COMMENT));
        work.setIdMaster(resultSet.getString(ID_MASTER));
        work.setImage(resultSet.getBytes(IMAGE));
    }
}
