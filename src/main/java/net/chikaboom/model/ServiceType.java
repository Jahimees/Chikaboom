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
    /**
     * Id типа услуги
     */
    private String idServiceType;

    /**
     * Наименование типа услуги
     */
    private String typeName;

    public ServiceType() {
        idServiceType = UUID.randomUUID().toString();
    }
}
