package net.chikaboom.model.database;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

import static net.chikaboom.util.constant.DbNamesConstant.SERVICE_TYPE;

/**
 * Определяет модель таблицы ServiceType в базе данных
 */
@Data
@Entity
@Table(name = SERVICE_TYPE)
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
