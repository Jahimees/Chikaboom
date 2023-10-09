package net.chikaboom.facade.dto;

import lombok.Data;

@Data
public class StatusFacade implements Facade {

    /**
     * id сущности в таблице Status
     */
    private int idStatus;

    /**
     * Имя статуса
     */
    private String status;
}
