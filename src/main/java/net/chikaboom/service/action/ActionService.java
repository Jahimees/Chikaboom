package net.chikaboom.service.action;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * Родительский интеррфейс для основных сервисов-логики, выполняющих какое-либо действие и возвращающих путь
 * к странице, на которую будет перенаправляться пользователь
 */
@Service
@PropertySource("/constants.properties")
public interface ActionService {

    @Value("${page.main}")
    String MAIN_PAGE = "";

     /**
     * Метод предназначен для выполнения основной логики сервиса
      *
     * @return строку, содержащую путь до страницы, на которую будет перенаправляться пользователь
     */
    default String executeAndGetPage() {
        return MAIN_PAGE;
    }

    /**
     * Метод предназначен для выполнения основной логики сервиса. Ничего не возвращает
     */
    default void execute() {

    }

}
