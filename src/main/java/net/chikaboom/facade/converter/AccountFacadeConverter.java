package net.chikaboom.facade.converter;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.AccountFacade;
import net.chikaboom.model.database.Account;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountFacadeConverter implements FacadeConverter<AccountFacade, Account> {

    private final AccountSettingsFacadeConverter accountSettingsFacadeConverter;
    private final RoleFacadeConverter roleFacadeConverter;
    private final UserDetailsFacadeConverter userDetailsFacadeConverter;

    @Override
    public AccountFacade convertToDto(Account model) {
        AccountFacade accountFacade = new AccountFacade();

        accountFacade.setIdAccount(model.getIdAccount());
        accountFacade.setPassword(model.getPassword());
        accountFacade.setOldPassword(model.getOldPassword());
        accountFacade.setUsername(model.getUsername());
        accountFacade.setRegistrationDate((Timestamp) model.getRegistrationDate().clone());
        accountFacade.setEmail(model.getEmail());
        accountFacade.setRoleFacades(model.getRoles().stream()
                .map(role -> roleFacadeConverter.convertToDto(role)
                ).collect(Collectors.toSet()));
        accountFacade.setAddress(model.getAddress());
        accountFacade.setUserDetailsFacade(userDetailsFacadeConverter.convertToDto(model.getUserDetails()));
        accountFacade.setAccountSettingsFacade(accountSettingsFacadeConverter.convertToDto(
                model.getAccountSettings()));

        return accountFacade;
    }

    @Override
    public Account convertToModel(AccountFacade facade) {
        Account account = new Account();

        account.setIdAccount(facade.getIdAccount());
        account.setPassword(facade.getPassword());
        account.setOldPassword(facade.getOldPassword());
        account.setUsername(facade.getUsername());
        account.setRegistrationDate((Timestamp) facade.getRegistrationDate().clone());
        account.setEmail(facade.getEmail());
        account.setRoles(facade.getRoleFacades().stream()
                .map(roleFacade -> roleFacadeConverter.convertToModel(roleFacade)
                ).collect(Collectors.toSet()));
        account.setAddress(facade.getAddress());
        account.setUserDetails(userDetailsFacadeConverter.convertToModel(facade.getUserDetailsFacade()));
        account.setAccountSettings(accountSettingsFacadeConverter.convertToModel(
                facade.getAccountSettingsFacade()));

        return account;
    }
}
