package net.chikaboom.facade.dto;

import lombok.Data;
import net.chikaboom.util.constant.ApplicationRole;

/**
 * DOCS {@link Facade}
 */
@Data
public class RoleFacade implements Facade {

    /**
     * id сущности в таблице role
     */
    private int idRole;

    /**
     * Имя роли пользователя
     */
    private String name;

    public RoleFacade() {}

    public RoleFacade(int idRole) {
        this.idRole = idRole;
        switch (idRole) {
            case 1: {
                this.name = ApplicationRole.ROLE_MASTER.name();
                break;
            }
            case 2: {
                this.name = ApplicationRole.ROLE_CLIENT.name();
                break;
            }
            default:
                this.idRole = 2;
                this.name = ApplicationRole.ROLE_CLIENT.name();
        }
    }
}
