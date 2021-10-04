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
    //    TODO DOCUMENTATION
    private String idClient;
    private String idAccount;

    public Client() {
        idClient = UUID.randomUUID().toString();
    }
}
