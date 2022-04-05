package net.chikaboom.model.database;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

import static net.chikaboom.util.constant.DbNamesConstant.SUBSERVICE_TYPE;

@Data
@Entity
@Table(name = SUBSERVICE_TYPE)
public class SubserviceType implements BaseEntity {
    /**
     * id подтипа услуги
     */
    @Id
    private String idSubserviceType;

    /**
     * Наименование подтипа услуги
     */
    private String name;

    /**
     * id вида услуги. Внешний ключ.
     */
    private String idServiceType;

    public SubserviceType() {
        idSubserviceType = UUID.randomUUID().toString();
    }
}
