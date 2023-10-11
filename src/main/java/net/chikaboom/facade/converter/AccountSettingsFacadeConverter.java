package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.AccountSettingsFacade;
import net.chikaboom.model.database.AccountSettings;

import java.sql.Time;

/**
 * DOCS {@link FacadeConverter}
 */
public final class AccountSettingsFacadeConverter implements FacadeConverter {

    private AccountSettingsFacadeConverter() {
    }

    /**
     * Конвертирует объект базы данных в объект фасада - DTO
     *
     * @param model объект модели
     * @return объект фасада - DTO
     */
    public static AccountSettingsFacade convertToDto(AccountSettings model) {
        AccountSettingsFacade accountSettingsFacade = new AccountSettingsFacade();

        accountSettingsFacade.setIdAccountSettings(model.getIdAccountSettings());
        accountSettingsFacade.setPhoneVisible(model.isPhoneVisible());
        if (model.getDefaultWorkingDayStart() != null) {
            accountSettingsFacade.setDefaultWorkingDayStart((Time) model.getDefaultWorkingDayStart().clone());
        }
        if (model.getDefaultWorkingDayEnd() != null) {
            accountSettingsFacade.setDefaultWorkingDayEnd((Time) model.getDefaultWorkingDayEnd().clone());
        }

        return accountSettingsFacade;
    }

    /**
     * Конвертирует объект фасада в объект модели
     *
     * @param facade объект фасада - DTO
     * @return объект модели
     */
    public static AccountSettings convertToModel(AccountSettingsFacade facade) {
        AccountSettings accountSettings = new AccountSettings();

        accountSettings.setIdAccountSettings(facade.getIdAccountSettings());
        accountSettings.setPhoneVisible(facade.isPhoneVisible());
        if (facade.getDefaultWorkingDayStart() != null) {
            accountSettings.setDefaultWorkingDayStart((Time) facade.getDefaultWorkingDayStart().clone());
        }
        if (facade.getDefaultWorkingDayEnd() != null) {
            accountSettings.setDefaultWorkingDayEnd((Time) facade.getDefaultWorkingDayEnd().clone());
        }

        return accountSettings;
    }
}
