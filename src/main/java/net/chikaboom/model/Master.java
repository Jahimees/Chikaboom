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
    //    TODO DOCUMENTATION
    private String idMaster;
    private String idAccount;
    private String address;
    private String description;

    public Master() {
        idMaster = UUID.randomUUID().toString();
    }
}
