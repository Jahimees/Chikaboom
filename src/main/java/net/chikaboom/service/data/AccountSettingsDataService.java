package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.AccountSettings;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.AccountSettingsRepository;
import net.chikaboom.service.data.DataService;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountSettingsDataService implements DataService<AccountSettings> {

    private final AccountSettingsRepository accountSettingsRepository;
    private final AccountDataService accountDataService;

    @Override
    public Optional<AccountSettings> findById(int idAccountSettings) {
        return accountSettingsRepository.findById(idAccountSettings);
    }

    public Optional<AccountSettings> findByIdAccount(int idAccount) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            throw new NotFoundException("Account with id " + idAccount + " not found");
        }

        return Optional.ofNullable(accountOptional.get().getAccountSettings());
    }

    @Override
    public List<AccountSettings> findAll() {
        return accountSettingsRepository.findAll();
    }

    @Override
    public void deleteById(int idAccountSettings) {
        accountSettingsRepository.deleteById(idAccountSettings);
    }

    @Override
    public AccountSettings update(AccountSettings accountSettings) {
        if (accountSettingsRepository.existsById(accountSettings.getIdAccountSettings())) {
            return accountSettingsRepository.save(accountSettings);
        } else {
            throw new NotFoundException("Account settings not found");
        }
    }

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

    @Override
    public AccountSettings create(AccountSettings accountSettings) {
        if (accountSettingsRepository.existsById(accountSettings.getIdAccountSettings())) {
            throw new AlreadyExistsException("Account settings already exists");
        }

        return accountSettingsRepository.save(accountSettings);
    }
}
