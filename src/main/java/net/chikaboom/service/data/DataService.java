package net.chikaboom.service.data;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс с набором базовых методов
 *
 * @param <T> целевой класс сущности
 */
public interface DataService<T> {

    T findById(int id);

    List<T> findAll();

    void deleteById(int id);

    T update(T o);

    T create(T account);
}
