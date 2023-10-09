package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.RoleFacade;
import net.chikaboom.model.database.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleFacadeConverter implements FacadeConverter<RoleFacade, Role> {

    @Override
    public RoleFacade convertToDto(Role model) {
        RoleFacade roleFacade = new RoleFacade();

        roleFacade.setIdRole(model.getIdRole());
        roleFacade.setName(model.getName());

        return roleFacade;
    }

    @Override
    public Role convertToModel(RoleFacade facade) {
        Role role = new Role();

        role.setIdRole(facade.getIdRole());
        role.setName(facade.getName());

        return role;
    }
}
