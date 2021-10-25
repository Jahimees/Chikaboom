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
    /**
     * Id услуги
     */
    private String idService;

    /**
     * Id мастера. Внешний ключ
     */
    private String idMaster;

    /**
     * Id вида услуги. Внешний ключ
     */
    private String idServiceType;

    /**
     * Наименование услуги
     */
    private String name;

    /**
     * Описание услуги
     */
    private String description;

    /**
     * Стоимость услуги
     */
    private double cost; //TODO заменить на decimal

    public Service() {
        idService = UUID.randomUUID().toString();
    }
}
