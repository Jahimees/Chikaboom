package net.chikaboom.facade.converter;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.AccountStatusFacade;
import net.chikaboom.model.database.AccountStatus;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
@RequiredArgsConstructor
public class AccountStatusFacadeConverter implements FacadeConverter<AccountStatusFacade, AccountStatus> {

    private final StatusFacadeConverter statusFacadeConverter;
    private final AccountFacadeConverter accountFacadeConverter;

    @Override
    public AccountStatusFacade convertToDto(AccountStatus model) {
        AccountStatusFacade accountStatusFacade = new AccountStatusFacade();

        accountStatusFacade.setIdAccountStatus(model.getIdAccountStatus());
        accountStatusFacade.setStatusFacade(statusFacadeConverter.convertToDto(model.getStatus()));
        accountStatusFacade.setAccountFacade(accountFacadeConverter.convertToDto(model.getAccount()));
        accountStatusFacade.setToDate((Timestamp) model.getToDate().clone());
        accountStatusFacade.setSinceDate((Timestamp) model.getSinceDate().clone());

        return accountStatusFacade;
    }

    @Override
    public AccountStatus convertToModel(AccountStatusFacade facade) {
        AccountStatus accountStatus = new AccountStatus();

        accountStatus.setIdAccountStatus(facade.getIdAccountStatus());
        accountStatus.setStatus(statusFacadeConverter.convertToModel(facade.getStatusFacade()));
        accountStatus.setAccount(accountFacadeConverter.convertToModel(facade.getAccountFacade()));
        accountStatus.setToDate((Timestamp) facade.getToDate().clone());
        accountStatus.setSinceDate((Timestamp) facade.getSinceDate().clone());

        return accountStatus;
    }
}
