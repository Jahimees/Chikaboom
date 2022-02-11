package net.chikaboom.service.action;

import org.springframework.stereotype.Service;

/**
 * Родительский интеррфейс для основных сервисов-логики
 */
@Service
public interface ActionService {

     /**
     * Метод предназначен для выполнения основной логики сервиса
      *
     * @return строку, содержащую путь до страницы, на которую будет перенаправляться пользователь
     */
    String execute();
}
