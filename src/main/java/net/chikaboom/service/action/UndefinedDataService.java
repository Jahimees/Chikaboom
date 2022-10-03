package net.chikaboom.service.action;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Родительский интерфейс для сервисов-логики, которые совершают операции над данными
 * и возвращают их в преобразованном виде
 */
@Service
public interface UndefinedDataService {

    /**
     * Метод предназначен для выполнения основной логики сервиса
     *
     * @return упакованные данные для отправки на клиент
     */
    Map<String, Object> execute();
}
