package net.chikaboom.dao;

import net.chikaboom.model.Entity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Класс, определяющий методы взаимодействия с базой данных
 * @param <E> класс-entity, соответствующий одной из таблиц модели
 */
public abstract class AbstractDAO<E extends Entity> {
//    TODO кастомное исполнение команды

    /**
     * Производит поиск всех объектов в таблице
     * @return список найденных объектов
     */
    abstract List<E> findAll();

    /**
     * Находит объект таблицы в базе по id
     * @param id идентификатор объекта
     * @return найденный объект
     */
    abstract E findEntity(String id);

    /**
     * Удаляет объект из таблицы по id
     * @param id идентификатор объекта
     * @return true, если удаление прошло успешно, false, если удалить объект не удалось
     */
    abstract boolean delete(String id);  //Или нужно передавать объект?????

    /**
     * Изменяет объект в таблице по id, который берет из передаваемого объекта
     * @param entity измененный объект
     * @return true, если обновление прошло успешно, false, если обновить объект не удалось
     */
    abstract boolean update(E entity);

    /**
     * Создает объект в таблице
     * @param entity создаваемый объект
     * @return true, если объект создан успешно, false, если объект создать не удалось
     */
    abstract boolean create(E entity);

    /**
     * Заполняет поля PreparedStatement-объекта
     * @param preparedStatement заполняемый объект
     * @param entity объект, с помощью которого производится заполнение
     */
    abstract void setFieldsToPreparedStatement(PreparedStatement preparedStatement, E entity);

    /**
     * Заполняет поля объекта из полученных результатов
     * @param entity заполняемый объект
     * @param resultSet полученные результаты поиска
     */
    abstract void setFieldsToEntity(E entity, ResultSet resultSet);
}
