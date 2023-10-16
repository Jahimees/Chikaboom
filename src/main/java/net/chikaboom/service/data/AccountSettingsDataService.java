package net.chikaboom.service.data;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.AccountSettings;
import net.chikaboom.repository.AccountSettingsRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис предназначен для обработки информации настроек аккаунта
 */
@Service
public class AccountSettingsDataService implements DataService<AccountSettings> {

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
    public Optional<AccountSettings> findById(int idAccountSettings) {
        return accountSettingsRepository.findById(idAccountSettings);
    }

    /**
     * Производит поиск настроек аккаунта по !id аккаунта!
     *
     * @param idAccount идентификатор аккаунта
     * @return настройки аккаунта
     */
    public Optional<AccountSettings> findByIdAccount(int idAccount) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            throw new NotFoundException("There not found account with id " + idAccount);
        }

        return Optional.of(accountOptional.get().getAccountSettings());
    }

    /**
     * Производит поиск всех настроек аккаунтов всех пользователей
     *
     * @return список настроек аккаунтов
     * @deprecated вряд ли существует такая ситуация, когда этот метод сможет быть использован
     */
    @Override
    @Deprecated
    public List<AccountSettings> findAll() {
        return accountSettingsRepository.findAll();
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
     * @param accountSettings новый объект настроек аккаунта
     * @return обновленный объект настроек аккаунта
     * @deprecated метод требует целого нового объекта. Есть вероятность потери данных, в случае,
     * если Вы предварительно не загрузили объект из базы. Лучше воспользуйтесь методом patch
     */
    @Override
    @Deprecated
    public AccountSettings update(AccountSettings accountSettings) {
        if (accountSettingsRepository.existsById(accountSettings.getIdAccountSettings())) {

            return accountSettingsRepository.save(accountSettings);
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
    public AccountSettings patch(int idAccount, AccountSettings newAccountSettings) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            throw new NotFoundException("Settings are empty");
        }

        if (newAccountSettings == null) {
            throw new NotFoundException("new account settings are empty");
        }

        Account accountFromDb = accountOptional.get();

        AccountSettings patchedAccountSettings = accountFromDb.getAccountSettings();

        if (newAccountSettings.getDefaultWorkingDayStart() != null) {
            patchedAccountSettings.setDefaultWorkingDayStart(newAccountSettings.getDefaultWorkingDayStart());
        }

        if (newAccountSettings.getDefaultWorkingDayEnd() != null) {
            patchedAccountSettings.setDefaultWorkingDayEnd(newAccountSettings.getDefaultWorkingDayEnd());
        }

        if (patchedAccountSettings.getDefaultWorkingDayStart().getTime()
                >= patchedAccountSettings.getDefaultWorkingDayEnd().getTime()) {
            throw new IllegalArgumentException("Illegal time values. Working end time less than working start time");
        }

        if (newAccountSettings.isPhoneVisible() != patchedAccountSettings.isPhoneVisible()) {
            patchedAccountSettings.setPhoneVisible(newAccountSettings.isPhoneVisible());
        }

        return accountSettingsRepository.saveAndFlush(patchedAccountSettings);
    }

    /**
     * Производит создание настроек аккаунта в базе данных
     *
     * @param accountSettings создаваемый объект настроек аккаунта
     * @return созданный объект настроек аккаунта
     */
    @Override
    public AccountSettings create(AccountSettings accountSettings) {
        if (accountSettingsRepository.existsById(accountSettings.getIdAccountSettings())) {
            throw new AlreadyExistsException("Account settings already exists");
        }

        return accountSettingsRepository.saveAndFlush(accountSettings);
    }
}
