package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.StatusFacade;
import net.chikaboom.model.database.Status;
import org.springframework.stereotype.Component;

@Component
public class StatusFacadeConverter implements FacadeConverter<StatusFacade, Status> {

    @Override
    public StatusFacade convertToDto(Status model) {
        StatusFacade statusFacade = new StatusFacade();

        statusFacade.setIdStatus(model.getIdStatus());
        statusFacade.setStatus(model.getStatus());

        return statusFacade;
    }

    @Override
    public Status convertToModel(StatusFacade facade) {
        Status status = new Status();

        status.setIdStatus(facade.getIdStatus());
        status.setStatus(facade.getStatus());

        return status;
    }
}
