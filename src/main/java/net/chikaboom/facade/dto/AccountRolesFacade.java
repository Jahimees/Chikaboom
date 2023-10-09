package net.chikaboom.facade.dto;

import lombok.Data;

@Data
public class AccountRolesFacade implements Facade {

    /**
     * Идентификатор сущности
     */
    private int idAccountRoles;

    /**
     * Ссылка на аккаунт
     */
    private AccountFacade accountFacade;

    /**
     * Ссылка на роль
     */
    private RoleFacade roleFacade;
}
