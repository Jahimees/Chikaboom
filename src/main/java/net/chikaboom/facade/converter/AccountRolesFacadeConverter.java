package net.chikaboom.facade.converter;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.AccountRolesFacade;
import net.chikaboom.model.database.AccountRoles;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountRolesFacadeConverter implements FacadeConverter<AccountRolesFacade, AccountRoles> {

    private final AccountFacadeConverter accountFacadeConverter;
    private final RoleFacadeConverter roleFacadeConverter;

    @Override
    public AccountRolesFacade convertToDto(AccountRoles model) {
        AccountRolesFacade accountRolesFacade = new AccountRolesFacade();

        accountRolesFacade.setIdAccountRoles(model.getIdAccountRoles());
        accountRolesFacade.setAccountFacade(accountFacadeConverter.convertToDto(model.getAccount()));
        accountRolesFacade.setRoleFacade(roleFacadeConverter.convertToDto(model.getRole()));

        return accountRolesFacade;
    }

    @Override
    public AccountRoles convertToModel(AccountRolesFacade facade) {
        AccountRoles accountRoles = new AccountRoles();

        accountRoles.setIdAccountRoles(facade.getIdAccountRoles());
        accountRoles.setAccount(accountFacadeConverter.convertToModel(facade.getAccountFacade()));
        accountRoles.setRole(roleFacadeConverter.convertToModel(facade.getRoleFacade()));

        return accountRoles;
    }
}
