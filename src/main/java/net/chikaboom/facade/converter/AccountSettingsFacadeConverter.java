package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.AccountSettingsFacade;
import net.chikaboom.model.database.AccountSettings;
import org.springframework.stereotype.Component;

import java.sql.Time;

@Component
public class AccountSettingsFacadeConverter implements FacadeConverter<AccountSettingsFacade, AccountSettings> {

    @Override
    public AccountSettingsFacade convertToDto(AccountSettings model) {
        AccountSettingsFacade accountSettingsFacade = new AccountSettingsFacade();

        accountSettingsFacade.setIdAccountSettings(model.getIdAccountSettings());
        accountSettingsFacade.setPhoneVisible(model.isPhoneVisible());
        accountSettingsFacade.setDefaultWorkingDayStart((Time) model.getDefaultWorkingDayStart().clone());
        accountSettingsFacade.setDefaultWorkingDayEnd((Time) model.getDefaultWorkingDayEnd().clone());

        return accountSettingsFacade;
    }

    @Override
    public AccountSettings convertToModel(AccountSettingsFacade facade) {
        AccountSettings accountSettings = new AccountSettings();

        accountSettings.setIdAccountSettings(facade.getIdAccountSettings());
        accountSettings.setPhoneVisible(facade.isPhoneVisible());
        accountSettings.setDefaultWorkingDayStart((Time) facade.getDefaultWorkingDayStart().clone());
        accountSettings.setDefaultWorkingDayEnd((Time) facade.getDefaultWorkingDayEnd().clone());

        return accountSettings;
    }
}
