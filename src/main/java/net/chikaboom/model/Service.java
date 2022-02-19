package net.chikaboom.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Определяет модель таблицы Service в базе данных
 */
@Data
@Entity
@Table(name = "service")
public class Service implements BaseEntity {
    /**
     * Id услуги
     */
    @Id
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
