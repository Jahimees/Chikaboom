package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.StatusFacade;
import net.chikaboom.model.database.Status;

/**
 * DOCS {@link FacadeConverter}
 */
public final class StatusFacadeConverter implements FacadeConverter {

    private StatusFacadeConverter() {}

    /**
     * Конвертирует объект базы данных в объект фасада - DTO
     *
     * @param model объект модели
     * @return объект фасада - DTO
     */
    public static StatusFacade convertToDto(Status model) {
        StatusFacade statusFacade = new StatusFacade();

        statusFacade.setIdStatus(model.getIdStatus());
        statusFacade.setStatus(model.getStatus());

        return statusFacade;
    }

    /**
     * Конвертирует объект фасада в объект модели
     *
     * @param facade объект фасада - DTO
     * @return объект модели
     */
    public static Status convertToModel(StatusFacade facade) {
        Status status = new Status();

        status.setIdStatus(facade.getIdStatus());
        status.setStatus(facade.getStatus());

        return status;
    }
}
