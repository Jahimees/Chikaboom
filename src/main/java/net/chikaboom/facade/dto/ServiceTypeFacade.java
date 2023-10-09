package net.chikaboom.facade.dto;

import lombok.Data;

@Data
public class ServiceTypeFacade implements Facade {

    /**
     * id сущности в таблице service
     */
    private int idServiceType;

    /**
     * Наименование услуги
     */
    private String name;
}
