package net.chikaboom.service.action;

import net.chikaboom.model.database.BaseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Родительский интерфейс для сервисов-логики, которые совершают операции над данными
 * и возвращают их в преобразованном виде. Возвращает строго объект
 */
@Service
public interface DataService {

    /**
     * Метод предназначен для выполнения основной логики сервиса
     *
     * @return преобразованный объект для отправки на клиент
     */
    default BaseEntity executeAndGetOne() {
        return null;
    }

    /**
     * Метод предназначен для выполнения основной логики сервиса
     *
     * @return коллекцию преобразованных объектов для отправки на клиент
     */
    default List<? extends BaseEntity> executeAndGetList() {
        return null;
    }
}
