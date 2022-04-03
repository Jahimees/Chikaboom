package net.chikaboom.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * Сервис, который хранит информацию (параметры) с клиента.
 * <p>
 * Реализован для общего доступа всех сервисов для хранения информации.
 */
//TODO Доработка уникальности параметров, очищения хранилища и т.д.
@Service
public class ClientDataStorageService {

    /**
     * Коллекция, содержащая информацию (параметры) с клиента
     */
    private final HashMap<String, Object> dataStorage;

    public ClientDataStorageService() {
        dataStorage = new HashMap<>();
    }

    /**
     * Получение параметра по имени
     *
     * @param name имя параметра
     * @return значение параметра
     */
    public Object getData(String name) {
        return dataStorage.get(name);
    }

    /**
     * Добавление нового/обновление старого параметра в хранилище
     *
     * @param name  имя параметра
     * @param value значение параметра
     */
    public void setData(String name, Object value) {
        dataStorage.put(name, value);
    }

    /**
     * Удаление параметра из хранилища
     *
     * @param name имя параметра
     */
    public void dropData(String name) {
        dataStorage.remove(name);
    }

}
