package net.chikaboom.model;

import java.util.UUID;

/**
 * Определяет модель таблицы ServiceType в базе данных
 */
public class ServiceType implements Entity {
    private String idServiceType;
    private String name;

    public ServiceType() {
        idServiceType = UUID.randomUUID().toString();
    }

    public String getIdServiceType() {
        return idServiceType;
    }

    public void setIdServiceType(String idServiceType) {
        this.idServiceType = idServiceType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
