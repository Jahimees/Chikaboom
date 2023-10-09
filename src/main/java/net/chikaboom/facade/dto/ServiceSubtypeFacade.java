package net.chikaboom.facade.dto;

import lombok.Data;

@Data
public class ServiceSubtypeFacade implements Facade {

    /**
     * id сущности в табилце serviceSubtype
     */
    private int idServiceSubtype;

    /**
     * Наименование подуслуги
     */
    private String name;

    /**
     * Родительский тип услуги для данной подуслуги
     */
    private ServiceTypeFacade serviceTypeFacade;
}
