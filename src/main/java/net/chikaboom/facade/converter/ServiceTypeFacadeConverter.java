package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.ServiceTypeFacade;
import net.chikaboom.model.database.ServiceType;

/**
 * DOCS {@link FacadeConverter}
 */
public final class ServiceTypeFacadeConverter implements FacadeConverter {

    private ServiceTypeFacadeConverter() {}

    /**
     * Конвертирует объект базы данных в объект фасада - DTO
     *
     * @param model объект модели
     * @return объект фасада - DTO
     */
    public static ServiceTypeFacade convertToDto(ServiceType model) {
        ServiceTypeFacade serviceTypeFacade = new ServiceTypeFacade();

        serviceTypeFacade.setIdServiceType(model.getIdServiceType());
        serviceTypeFacade.setName(model.getName());

        return serviceTypeFacade;
    }

    /**
     * Конвертирует объект фасада в объект модели
     *
     * @param facade объект фасада - DTO
     * @return объект модели
     */
    public static ServiceType convertToModel(ServiceTypeFacade facade) {
        ServiceType serviceType = new ServiceType();

        serviceType.setIdServiceType(facade.getIdServiceType());
        serviceType.setName(facade.getName());

        return serviceType;
    }
}
