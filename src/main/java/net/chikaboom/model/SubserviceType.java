package net.chikaboom.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SubserviceType implements Entity {
    /**
     * id подтипа услуги
     */
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
