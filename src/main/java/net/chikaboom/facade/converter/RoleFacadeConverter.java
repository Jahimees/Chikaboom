package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.RoleFacade;
import net.chikaboom.model.database.Role;

/**
 * DOCS {@link FacadeConverter}
 */
public final class RoleFacadeConverter implements FacadeConverter {

    private RoleFacadeConverter() {
    }

    /**
     * Конвертирует объект базы данных в объект фасада - DTO
     *
     * @param model объект модели
     * @return объект фасада - DTO
     */
    public static RoleFacade convertToDto(Role model) {
        RoleFacade roleFacade = new RoleFacade();

        roleFacade.setIdRole(model.getIdRole());
        roleFacade.setName(model.getName());

        return roleFacade;
    }

    /**
     * Конвертирует объект фасада в объект модели
     *
     * @param facade объект фасада - DTO
     * @return объект модели
     */
    public static Role convertToModel(RoleFacade facade) {
        Role role = new Role();

        role.setIdRole(facade.getIdRole());
        role.setName(facade.getName());

        return role;
    }
}
