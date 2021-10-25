package net.chikaboom.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Определяет модель таблицы Client в базе данных
 */
@Getter
@Setter
public class Client implements Entity {
    /**
     * Id клиента
     */
    private String idClient;

    /**
     * Id аккаунта. Внешний ключ
     */
    private String idAccount;

    public Client() {
        idClient = UUID.randomUUID().toString();
    }
}
