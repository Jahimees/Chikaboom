package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.ServiceSubtypeFacade;
import net.chikaboom.model.database.ServiceSubtype;

/**
 * DOCS {@link FacadeConverter}
 */
public final class ServiceSubtypeFacadeConverter implements FacadeConverter {

    private ServiceSubtypeFacadeConverter() {
    }

    /**
     * Конвертирует объект базы данных в объект фасада - DTO
     *
     * @param model объект модели
     * @return объект фасада - DTO
     */
    public static ServiceSubtypeFacade convertToDto(ServiceSubtype model) {
        ServiceSubtypeFacade serviceSubtypeFacade = new ServiceSubtypeFacade();

        serviceSubtypeFacade.setIdServiceSubtype(model.getIdServiceSubtype());
        serviceSubtypeFacade.setName(model.getName());
        if (model.getServiceType() != null) {
            serviceSubtypeFacade.setServiceTypeFacade(ServiceTypeFacadeConverter.convertToDto(model.getServiceType()));
        }

        return serviceSubtypeFacade;
    }

    /**
     * Конвертирует объект фасада в объект модели
     *
     * @param facade объект фасада - DTO
     * @return объект модели
     */
    public static ServiceSubtype convertToModel(ServiceSubtypeFacade facade) {
        ServiceSubtype serviceSubtype = new ServiceSubtype();

        serviceSubtype.setIdServiceSubtype(facade.getIdServiceSubtype());
        serviceSubtype.setName(facade.getName());
        if (facade.getServiceTypeFacade() != null) {
            serviceSubtype.setServiceType(ServiceTypeFacadeConverter.convertToModel(facade.getServiceTypeFacade()));
        }

        return serviceSubtype;
    }
}
