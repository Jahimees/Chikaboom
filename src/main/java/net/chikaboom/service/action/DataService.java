package net.chikaboom.service.action;

import net.chikaboom.model.ExpandableObject;
import org.springframework.stereotype.Service;

/**
 * Родительский интерфейс для сервисов-логики, которые совершают операции над данными
 * и возвращают их в преобразованном виде
 */
@Service
public interface DataService {

    /**
     * Метод предназначен для выполнения основной логики сервиса
     *
     * @return упакованные данные для отправки на клиент
     */
    ExpandableObject execute();
}
