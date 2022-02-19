package net.chikaboom.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Определяет модель таблицы ServiceType в базе данных
 */
@Data
@Entity
@Table(name = "servicetype")
public class ServiceType implements BaseEntity {
    /**
     * Id типа услуги
     */
    @Id
    private String idServiceType;

    /**
     * Наименование типа услуги
     */
    private String typeName;

    public ServiceType() {
        idServiceType = UUID.randomUUID().toString();
    }
}
