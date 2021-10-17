package net.chikaboom.dao;

import net.chikaboom.connection.ConnectionPool;
import net.chikaboom.model.Account;
import net.chikaboom.model.Entity;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static net.chikaboom.constant.LoggerMessageConstant.CONNECTION_CLOSING_ERROR;
import static net.chikaboom.constant.LoggerMessageConstant.QUERY_EXECUTION_ERROR;

/**
 * Класс, определяющий методы взаимодействия с базой данных
 *
 * @param <E> класс-entity, соответствующий одной из таблиц модели
 */
public abstract class AbstractDAO<E extends Entity> {
//    TODO кастомное исполнение команды

    private static final Logger logger = Logger.getLogger(AbstractDAO.class);

    /**
     * Исполняет переданный запрос и возвращает коллекцию результатов.
     * Ответственность за передаваемые данные возлагается на программиста
     *
     * @param query      строка запроса
     * @param parameters передаваемые параметры для подстановки в запрос
     * @return объекты-результаты запроса
     */
    public List<E> executeQuery(String query, List<String> parameters) {
        Connection connection = ConnectionPool.getInstance().getConnection();
        List<E> entityList = new ArrayList<>();
        try {
            PreparedStatement customStatement = connection.prepareStatement(query);
            AtomicInteger iterator = new AtomicInteger(1);
            parameters.forEach(param -> {
                try {
                    customStatement.setString(iterator.getAndIncrement(), param);
                } catch (SQLException e) {
                    logger.error(QUERY_EXECUTION_ERROR, e);
                }
            });
            ResultSet resultSet = customStatement.executeQuery();

            while (resultSet.next()) {
                E entity = new E();
                setFieldsToEntity(entity, resultSet);
                entityList.add(entity);
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
        return entityList;
    }

    /**
     * Производит поиск всех объектов в таблице
     *
     * @return список найденных объектов
     */
    abstract List<E> findAll();

    /**
     * Находит объект таблицы в базе по id
     *
     * @param id идентификатор объекта
     * @return найденный объект
     */
    abstract E findEntity(String id);

    /**
     * Удаляет объект из таблицы по id
     *
     * @param id идентификатор объекта
     * @return true, если удаление прошло успешно, false, если удалить объект не удалось
     */
    abstract boolean delete(String id);  //Или нужно передавать объект?????

    /**
     * Изменяет объект в таблице по id, который берет из передаваемого объекта
     *
     * @param entity измененный объект
     * @return true, если обновление прошло успешно, false, если обновить объект не удалось
     */
    abstract boolean update(E entity);

    /**
     * Создает объект в таблице
     *
     * @param entity создаваемый объект
     * @return true, если объект создан успешно, false, если объект создать не удалось
     */
    abstract boolean create(E entity);

    /**
     * Заполняет поля PreparedStatement-объекта
     *
     * @param preparedStatement заполняемый объект
     * @param entity            объект, с помощью которого производится заполнение
     */
    abstract void setFieldsToPreparedStatement(PreparedStatement preparedStatement, E entity);

    /**
     * Заполняет поля объекта из полученных результатов
     *
     * @param entity    заполняемый объект
     * @param resultSet полученные результаты поиска
     */
    abstract void setFieldsToEntity(E entity, ResultSet resultSet);
}
