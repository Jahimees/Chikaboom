package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.ServiceFacade;
import net.chikaboom.model.database.Service;

/**
 * DOCS {@link FacadeConverter}
 */
public final class ServiceFacadeConverter implements FacadeConverter {

    private ServiceFacadeConverter() {
    }

    /**
     * Поля все кроме accountFacade и serviceSubtypeFacade
     */
    public static ServiceFacade toDtoForAppointmentDataTable(Service model) {
        ServiceFacade serviceFacade = convertToDto(model);

        serviceFacade.setAccountFacade(null);
        serviceFacade.setServiceSubtypeFacade(null);

        return serviceFacade;
    }

    /**
     * Нет поля serviceSubtypeFacade
     */
    public static ServiceFacade toSearchResultPage(Service model) {
        ServiceFacade serviceFacade = convertToDto(model);
        serviceFacade.setServiceSubtypeFacade(null);

        return serviceFacade;
    }

    /**
     * Конвертирует объект базы данных в объект фасада - DTO
     *
     * @param model объект модели
     * @return объект фасада - DTO
     */
    public static ServiceFacade convertToDto(Service model) {
        ServiceFacade serviceFacade = new ServiceFacade();

        serviceFacade.setIdService(model.getIdService());
        serviceFacade.setName(model.getName());
        serviceFacade.setPrice(model.getPrice());
        serviceFacade.setTime(model.getTime());
//        TODO warning place 5
        if (model.getAccount() != null) {
            serviceFacade.setAccountFacade(AccountFacadeConverter.toDtoOnlyId(model.getAccount()));
        }
        if (model.getServiceSubtype() != null) {
            serviceFacade.setServiceSubtypeFacade(ServiceSubtypeFacadeConverter.convertToDto(model.getServiceSubtype()));
        }

        return serviceFacade;
    }

    /**
     * Конвертирует объект фасада в объект модели
     *
     * @param facade объект фасада - DTO
     * @return объект модели
     */
    public static Service convertToModel(ServiceFacade facade) {
        Service service = new Service();

        service.setIdService(facade.getIdService());
        service.setName(facade.getName());
        service.setPrice(facade.getPrice());
        service.setTime(facade.getTime());
        if (facade.getAccountFacade() != null) {
            service.setAccount(AccountFacadeConverter.convertToModel(facade.getAccountFacade()));
        }
        if (facade.getServiceSubtypeFacade() != null) {
            service.setServiceSubtype(ServiceSubtypeFacadeConverter.convertToModel(facade.getServiceSubtypeFacade()));
        }

        return service;
    }
}
