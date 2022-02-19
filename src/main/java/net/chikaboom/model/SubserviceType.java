package net.chikaboom.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "subservicetype")
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
