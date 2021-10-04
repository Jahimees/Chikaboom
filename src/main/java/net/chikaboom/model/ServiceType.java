package net.chikaboom.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * Определяет модель таблицы ServiceType в базе данных
 */
@Getter
@Setter
public class ServiceType implements Entity {
    //    TODO DOCUMENTATION
    private String idServiceType;
    private String name;

    public ServiceType() {
        idServiceType = UUID.randomUUID().toString();
    }
}
