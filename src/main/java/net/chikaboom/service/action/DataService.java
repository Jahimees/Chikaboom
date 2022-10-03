package net.chikaboom.service.action;

import net.chikaboom.model.database.BaseEntity;
import org.springframework.stereotype.Service;

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
    BaseEntity execute();
}
