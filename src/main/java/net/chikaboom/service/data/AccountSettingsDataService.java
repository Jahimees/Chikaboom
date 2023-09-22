package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.AccountSettings;
import net.chikaboom.repository.AccountSettingsRepository;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис предназначен для обработки информации настроек аккаунта
 */
@Service
@RequiredArgsConstructor
public class AccountSettingsDataService implements DataService<AccountSettings> {

    private final AccountSettingsRepository accountSettingsRepository;
    private final AccountDataService accountDataService;

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
            throw new NotFoundException("Account with id " + idAccount + " not found");
        }

        return Optional.ofNullable(accountOptional.get().getAccountSettings());
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
     * @param idAccount идентификатор аккаунта изменяемых настроек аккаунта
     * @param newAccountSettings новые настройки аккаунта
     * @return обновленные настройки аккаунта
     */
    public AccountSettings patch(int idAccount, AccountSettings newAccountSettings) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);
        if (!accountOptional.isPresent() || accountOptional.get().getAccountSettings() == null) {
            throw new NotFoundException("Account or settings are empty");
        }

        if (newAccountSettings == null) {
            throw new NotFoundException("new account settings are empty");
        }

        AccountSettings changedAccountSettings = accountOptional.get().getAccountSettings();

        if (newAccountSettings.getDefaultWorkingDayStart() != null) {
            changedAccountSettings.setDefaultWorkingDayStart(newAccountSettings.getDefaultWorkingDayStart());
        }

        if (newAccountSettings.getDefaultWorkingDayEnd() != null) {
            changedAccountSettings.setDefaultWorkingDayEnd(newAccountSettings.getDefaultWorkingDayEnd());
        }

        if (newAccountSettings.isPhoneVisible() != changedAccountSettings.isPhoneVisible()) {
            changedAccountSettings.setPhoneVisible(newAccountSettings.isPhoneVisible());
        }

        return accountSettingsRepository.saveAndFlush(changedAccountSettings);
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

        return accountSettingsRepository.save(accountSettings);
    }
}
