package net.chikaboom.facade.converter;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.ServiceFacade;
import net.chikaboom.model.database.Service;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceFacadeConverter implements FacadeConverter<ServiceFacade, Service> {

    private final AccountFacadeConverter accountFacadeConverter;
    private final ServiceSubtypeFacadeConverter serviceSubtypeFacadeConverter;

    @Override

    public ServiceFacade convertToDto(Service model) {
        ServiceFacade serviceFacade = new ServiceFacade();

        serviceFacade.setIdService(model.getIdService());
        serviceFacade.setName(model.getName());
        serviceFacade.setPrice(model.getPrice());
        serviceFacade.setTime(model.getTime());
        serviceFacade.setAccountFacade(accountFacadeConverter.convertToDto(model.getAccount()));
        serviceFacade.setServiceSubtypeFacade(serviceSubtypeFacadeConverter.convertToDto(model.getServiceSubtype()));

        return serviceFacade;
    }

    @Override
    public Service convertToModel(ServiceFacade facade) {
        Service service = new Service();

        service.setIdService(facade.getIdService());
        service.setName(facade.getName());
        service.setPrice(facade.getPrice());
        service.setTime(facade.getTime());
        service.setAccount(accountFacadeConverter.convertToModel(facade.getAccountFacade()));
        service.setServiceSubtype(serviceSubtypeFacadeConverter.convertToModel(facade.getServiceSubtypeFacade()));

        return service;
    }
}
