package net.chikaboom.facade.converter;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.ServiceSubtypeFacade;
import net.chikaboom.model.database.ServiceSubtype;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceSubtypeFacadeConverter implements FacadeConverter<ServiceSubtypeFacade, ServiceSubtype> {

    private ServiceTypeFacadeConverter serviceTypeFacadeConverter;

    @Override
    public ServiceSubtypeFacade convertToDto(ServiceSubtype model) {
        ServiceSubtypeFacade serviceSubtypeFacade = new ServiceSubtypeFacade();

        serviceSubtypeFacade.setIdServiceSubtype(model.getIdServiceSubtype());
        serviceSubtypeFacade.setName(model.getName());
        serviceSubtypeFacade.setServiceTypeFacade(serviceTypeFacadeConverter.convertToDto(model.getServiceType()));

        return serviceSubtypeFacade;
    }

    @Override
    public ServiceSubtype convertToModel(ServiceSubtypeFacade facade) {
        ServiceSubtype serviceSubtype = new ServiceSubtype();

        serviceSubtype.setIdServiceSubtype(facade.getIdServiceSubtype());
        serviceSubtype.setName(facade.getName());
        serviceSubtype.setServiceType(serviceTypeFacadeConverter.convertToModel(facade.getServiceTypeFacade()));

        return serviceSubtype;
    }
}
