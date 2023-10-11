package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.AccountFacade;
import net.chikaboom.model.database.Account;

import java.sql.Timestamp;
import java.util.stream.Collectors;

/**
 * DOCS {@link FacadeConverter}
 */
public final class AccountFacadeConverter implements FacadeConverter {

    private AccountFacadeConverter() {
    }

    /**
     * Конвертирует объект базы данных в объект фасада - DTO
     *
     * @param model объект модели
     * @return объект фасада - DTO
     */
    public static AccountFacade convertToDto(Account model) {
        AccountFacade accountFacade = new AccountFacade();

        accountFacade.setIdAccount(model.getIdAccount());
        accountFacade.setPassword(model.getPassword());
        accountFacade.setOldPassword(model.getOldPassword());
        accountFacade.setUsername(model.getUsername());
        accountFacade.setEmail(model.getEmail());
        accountFacade.setAddress(model.getAddress());
        if (model.getRegistrationDate() != null) {
            accountFacade.setRegistrationDate((Timestamp) model.getRegistrationDate().clone());
        }
        if (model.getRoles() != null) {
            accountFacade.setRolesFacade(model.getRoles().stream()
                    .map(RoleFacadeConverter::convertToDto
                    ).collect(Collectors.toSet()));
        }
        if (model.getUserDetails() != null) {
            accountFacade.setUserDetailsFacade(UserDetailsFacadeConverter.convertToDto(model.getUserDetails()));
        }
        if (model.getAccountSettings() != null) {
            accountFacade.setAccountSettingsFacade(AccountSettingsFacadeConverter.convertToDto(
                    model.getAccountSettings()));
        }

        return accountFacade;
    }

    /**
     * Конвертирует объект фасада в объект модели
     *
     * @param facade объект фасада - DTO
     * @return объект модели
     */
    public static Account convertToModel(AccountFacade facade) {
        Account account = new Account();

        account.setIdAccount(facade.getIdAccount());
        account.setPassword(facade.getPassword());
        account.setOldPassword(facade.getOldPassword());
        account.setUsername(facade.getUsername());
        account.setEmail(facade.getEmail());
        account.setAddress(facade.getAddress());
        if (facade.getRegistrationDate() != null) {
            account.setRegistrationDate((Timestamp) facade.getRegistrationDate().clone());
        }
        if (facade.getRolesFacade() != null) {
            account.setRoles(facade.getRolesFacade().stream()
                    .map(RoleFacadeConverter::convertToModel
                    ).collect(Collectors.toSet()));
        }
        if (facade.getUserDetailsFacade() != null) {
            account.setUserDetails(UserDetailsFacadeConverter.convertToModel(facade.getUserDetailsFacade()));
        }
        if (facade.getAccountSettingsFacade() != null) {
            account.setAccountSettings(AccountSettingsFacadeConverter.convertToModel(
                    facade.getAccountSettingsFacade()));
        }

        return account;
    }
}
