package net.chikaboom.service.data;

import net.chikaboom.facade.converter.AccountSettingsFacadeConverter;
import net.chikaboom.facade.dto.AccountFacade;
import net.chikaboom.facade.dto.AccountSettingsFacade;
import net.chikaboom.model.database.AccountSettings;
import net.chikaboom.repository.AccountSettingsRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис предназначен для обработки информации настроек аккаунта
 */
@Service
public class AccountSettingsDataService implements DataService<AccountSettingsFacade> {

    //Внедрение зависимостей через сеттер, поскольку AccountDataService вызывает циклическую ошибку инициализации бина
    private final AccountSettingsRepository accountSettingsRepository;
    private final AccountDataService accountDataService;

    public AccountSettingsDataService(AccountSettingsRepository accountSettingsRepository,
                                      @Lazy AccountDataService accountDataService) {
        this.accountSettingsRepository = accountSettingsRepository;
        this.accountDataService = accountDataService;
    }

    /**
     * Производит поиск настроек аккаунта по id
     *
     * @param idAccountSettings идентификатор настроек аккаунта
     * @return настройки аккаунта
     */
    @Override
    public AccountSettingsFacade findById(int idAccountSettings) {
        Optional<AccountSettings> accountSettings = accountSettingsRepository.findById(idAccountSettings);

        if (!accountSettings.isPresent()) {
            throw new NotFoundException("There is no account settings with " + idAccountSettings);
        }

        return AccountSettingsFacadeConverter.convertToDto(accountSettings.get());
    }

    /**
     * Производит поиск настроек аккаунта по !id аккаунта!
     *
     * @param idAccount идентификатор аккаунта
     * @return настройки аккаунта
     */
    public AccountSettingsFacade findByIdAccount(int idAccount) {
        AccountFacade accountFacade = accountDataService.findById(idAccount);

        return accountFacade.getAccountSettingsFacade();
    }

    /**
     * Производит поиск всех настроек аккаунтов всех пользователей
     *
     * @return список настроек аккаунтов
     * @deprecated вряд ли существует такая ситуация, когда этот метод сможет быть использован
     */
    @Override
    @Deprecated
    public List<AccountSettingsFacade> findAll() {
        return accountSettingsRepository.findAll().stream().map(
                        AccountSettingsFacadeConverter::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Удаляет указанные настройки аккаунта
     *
     * @param idAccountSettings идентификатор настроек аккаунта
     */
    @Override
    public void deleteById(int idAccountSettings) {
        accountSettingsRepository.deleteById(idAccountSettings);
    }

    /**
     * Производит обновление настроек аккаунта. Внимание! Полностью изменяет объект аккаунта
     *
     * @param accountSettingsFacade новый объект настроек аккаунта
     * @return обновленный объект настроек аккаунта
     * @deprecated метод требует целого нового объекта. Есть вероятность потери данных, в случае,
     * если Вы предварительно не загрузили объект из базы. Лучше воспользуйтесь методом patch
     */
    @Override
    @Deprecated
    public AccountSettingsFacade update(AccountSettingsFacade accountSettingsFacade) {
        if (accountSettingsRepository.existsById(accountSettingsFacade.getIdAccountSettings())) {
            AccountSettings accountSettings = AccountSettingsFacadeConverter.convertToModel(accountSettingsFacade);

            return AccountSettingsFacadeConverter.convertToDto(accountSettingsRepository.save(accountSettings));
        } else {
            throw new NotFoundException("Account settings not found");
        }
    }

    /**
     * Производит частичное изменение объекта в зависимости от переданных параметров в объекте.
     * Таким образом, если в объекте есть поля null, то они не будут сохранены и перезаписаны в базе данных
     *
     * @param idAccount          идентификатор аккаунта изменяемых настроек аккаунта
     * @param newAccountSettings новые настройки аккаунта
     * @return обновленные настройки аккаунта
     */
    public AccountSettingsFacade patch(int idAccount, AccountSettingsFacade newAccountSettings) {
        AccountFacade accountFacade = accountDataService.findById(idAccount);

        if (accountFacade.getAccountSettingsFacade() == null) {
            throw new NotFoundException("Settings are empty");
        }

        if (newAccountSettings == null) {
            throw new NotFoundException("new account settings are empty");
        }

        AccountSettingsFacade changedAccountSettingsFacade = accountFacade.getAccountSettingsFacade();

        if (newAccountSettings.getDefaultWorkingDayStart() != null) {
            changedAccountSettingsFacade.setDefaultWorkingDayStart(newAccountSettings.getDefaultWorkingDayStart());
        }

        if (newAccountSettings.getDefaultWorkingDayEnd() != null) {
            changedAccountSettingsFacade.setDefaultWorkingDayEnd(newAccountSettings.getDefaultWorkingDayEnd());
        }

        if (changedAccountSettingsFacade.getDefaultWorkingDayStart().getTime()
                >= changedAccountSettingsFacade.getDefaultWorkingDayEnd().getTime()) {
            throw new IllegalArgumentException("Illegal time values. Working end time less than working start time");
        }

        if (newAccountSettings.isPhoneVisible() != changedAccountSettingsFacade.isPhoneVisible()) {
            changedAccountSettingsFacade.setPhoneVisible(newAccountSettings.isPhoneVisible());
        }

        return AccountSettingsFacadeConverter.convertToDto(
                accountSettingsRepository.saveAndFlush(
                        AccountSettingsFacadeConverter.convertToModel(
                                changedAccountSettingsFacade)));
    }

    /**
     * Производит создание настроек аккаунта в базе данных
     *
     * @param accountSettingsFacade создаваемый объект настроек аккаунта
     * @return созданный объект настроек аккаунта
     */
    @Override
    public AccountSettingsFacade create(AccountSettingsFacade accountSettingsFacade) {
        if (accountSettingsRepository.existsById(accountSettingsFacade.getIdAccountSettings())) {
            throw new AlreadyExistsException("Account settings already exists");
        }

        return AccountSettingsFacadeConverter.convertToDto(
                accountSettingsRepository.saveAndFlush(
                        AccountSettingsFacadeConverter.convertToModel(accountSettingsFacade)));
    }
}
