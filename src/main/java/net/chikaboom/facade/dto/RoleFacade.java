package net.chikaboom.facade.dto;

import lombok.Data;

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
}
