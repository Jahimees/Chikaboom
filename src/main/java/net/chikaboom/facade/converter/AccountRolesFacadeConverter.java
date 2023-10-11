package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.AccountRolesFacade;
import net.chikaboom.model.database.AccountRoles;

/**
 * DOCS {@link FacadeConverter}
 */
public final class AccountRolesFacadeConverter implements FacadeConverter {

    private AccountRolesFacadeConverter() {
    }

    /**
     * Конвертирует объект базы данных в объект фасада - DTO
     *
     * @param model объект модели
     * @return объект фасада - DTO
     */
    public static AccountRolesFacade convertToDto(AccountRoles model) {
        AccountRolesFacade accountRolesFacade = new AccountRolesFacade();

        accountRolesFacade.setIdAccountRoles(model.getIdAccountRoles());
        if (model.getAccount() != null) {
            accountRolesFacade.setAccountFacade(AccountFacadeConverter.convertToDto(model.getAccount()));
        }
        if (model.getRole() != null) {
            accountRolesFacade.setRoleFacade(RoleFacadeConverter.convertToDto(model.getRole()));
        }

        return accountRolesFacade;
    }

    /**
     * Конвертирует объект фасада в объект модели
     *
     * @param facade объект фасада - DTO
     * @return объект модели
     */
    public static AccountRoles convertToModel(AccountRolesFacade facade) {
        AccountRoles accountRoles = new AccountRoles();

        accountRoles.setIdAccountRoles(facade.getIdAccountRoles());
        if (facade.getAccountFacade() != null) {
            accountRoles.setAccount(AccountFacadeConverter.convertToModel(facade.getAccountFacade()));
        }
        if (facade.getRoleFacade() != null) {
            accountRoles.setRole(RoleFacadeConverter.convertToModel(facade.getRoleFacade()));
        }

        return accountRoles;
    }
}
