package net.chikaboom.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Определяет модель таблицы Master в базе данных
 */
@Getter
@Setter
public class Master implements Entity {
    /**
     * Id мастера
     */
    private String idMaster;

    /**
     * Id соответствующего аккаунта. Внешний ключ
     */
    private String idAccount;

    /**
     * Адрес мастера, где он работает
     */
    private String address;

    /**
     * Краткое описание мастера о себе
     */
    private String description;

    public Master() {
        idMaster = UUID.randomUUID().toString();
    }
}
