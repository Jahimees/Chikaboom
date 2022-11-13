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
     * Метод предназначен для выполнения основной логики сервиса над комбинированными данными.
     * Например, если необходимо вернуть на сервер полные данные аккаунта без id, а с чистыми значениями
     *
     * @return упакованные данные для отправки на клиент
     */
    Map<String, Object> execute();
}
