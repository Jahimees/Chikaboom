package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.AccountStatusFacade;
import net.chikaboom.model.database.AccountStatus;

import java.sql.Timestamp;

/**
 * DOCS {@link FacadeConverter}
 */
public final class AccountStatusFacadeConverter implements FacadeConverter {

    private AccountStatusFacadeConverter() {
    }

    /**
     * Конвертирует объект базы данных в объект фасада - DTO
     *
     * @param model объект модели
     * @return объект фасада - DTO
     */
    public static AccountStatusFacade convertToDto(AccountStatus model) {
        AccountStatusFacade accountStatusFacade = new AccountStatusFacade();

        accountStatusFacade.setIdAccountStatus(model.getIdAccountStatus());
        if (model.getToDate() != null) {
            accountStatusFacade.setToDate((Timestamp) model.getToDate().clone());
        }
        if (model.getSinceDate() != null) {
            accountStatusFacade.setSinceDate((Timestamp) model.getSinceDate().clone());
        }
        if (model.getStatus() != null) {
            accountStatusFacade.setStatusFacade(StatusFacadeConverter.convertToDto(model.getStatus()));
        }
//        TODO warning place 3
        if (model.getAccount() != null) {
            accountStatusFacade.setAccountFacade(AccountFacadeConverter.toDtoOnlyId(model.getAccount()));
        }

        return accountStatusFacade;
    }

    /**
     * Конвертирует объект фасада в объект модели
     *
     * @param facade объект фасада - DTO
     * @return объект модели
     */
    public static AccountStatus convertToModel(AccountStatusFacade facade) {
        AccountStatus accountStatus = new AccountStatus();

        accountStatus.setIdAccountStatus(facade.getIdAccountStatus());
        if (facade.getToDate() != null) {
            accountStatus.setToDate((Timestamp) facade.getToDate().clone());
        }
        if (facade.getSinceDate() != null) {
            accountStatus.setSinceDate((Timestamp) facade.getSinceDate().clone());
        }
        if (facade.getStatusFacade() != null) {
            accountStatus.setStatus(StatusFacadeConverter.convertToModel(facade.getStatusFacade()));
        }
        if (facade.getAccountFacade() != null) {
            accountStatus.setAccount(AccountFacadeConverter.convertToModel(facade.getAccountFacade()));
        }

        return accountStatus;
    }
}
