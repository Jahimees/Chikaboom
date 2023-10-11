package net.chikaboom.facade.dto;

import lombok.Data;

/**
 * DOCS {@link Facade}
 */
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
