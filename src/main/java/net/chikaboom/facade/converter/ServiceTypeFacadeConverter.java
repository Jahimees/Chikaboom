package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.ServiceTypeFacade;
import net.chikaboom.model.database.ServiceType;
import org.springframework.stereotype.Component;

@Component
public class ServiceTypeFacadeConverter implements FacadeConverter<ServiceTypeFacade, ServiceType> {

    @Override
    public ServiceTypeFacade convertToDto(ServiceType model) {
        ServiceTypeFacade serviceTypeFacade = new ServiceTypeFacade();

        serviceTypeFacade.setIdServiceType(model.getIdServiceType());
        serviceTypeFacade.setName(model.getName());

        return serviceTypeFacade;
    }

    @Override
    public ServiceType convertToModel(ServiceTypeFacade facade) {
        ServiceType serviceType = new ServiceType();

        serviceType.setIdServiceType(facade.getIdServiceType());
        serviceType.setName(facade.getName());

        return serviceType;
    }
}
