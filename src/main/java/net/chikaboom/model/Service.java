package net.chikaboom.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Определяет модель таблицы Service в базе данных
 */
@Getter
@Setter
public class Service implements Entity {
    //    TODO DOCUMENTATION
    private String idService;
    private String idMaster;
    private String idServiceType;
    private String name;
    private String description;
    private double cost; //TODO заменить на decimal

    public Service() {
        idService = UUID.randomUUID().toString();
    }
}
