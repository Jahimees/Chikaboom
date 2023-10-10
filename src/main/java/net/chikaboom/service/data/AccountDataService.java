package net.chikaboom.service.data;

import com.google.i18n.phonenumbers.NumberParseException;
import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.exception.UserAlreadyExistsException;
import net.chikaboom.facade.converter.AccountFacadeConverter;
import net.chikaboom.facade.converter.UserDetailsFacadeConverter;
import net.chikaboom.facade.dto.AboutFacade;
import net.chikaboom.facade.dto.AccountFacade;
import net.chikaboom.facade.dto.AccountSettingsFacade;
import net.chikaboom.facade.dto.UserDetailsFacade;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.AccountSettings;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.PhoneCodeRepository;
import net.chikaboom.util.PhoneNumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис предназначен для обработки информации об аккаунте.
 */
@RequiredArgsConstructor
@Service
public class AccountDataService implements UserDetailsService, DataService<AccountFacade> {

    @Value("${regexp.email}")
    private String EMAIL_REGEXP;

    private final AccountFacadeConverter accountFacadeConverter;
    private final UserDetailsFacadeConverter userDetailsFacadeConverter;

    private final AccountRepository accountRepository;
    private final PhoneCodeRepository phoneCodeRepository;

    private final UserDetailsDataService userDetailsDataService;
    private final AccountSettingsDataService accountSettingsDataService;
    private final AboutDataService aboutDataService;
    private final PhoneCodeDataService phoneCodeDataService;

    private final BCryptPasswordEncoder passwordEncoder;
    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Производит поиск аккаунта по имени пользователя
     *
     * @param username имя пользователя, определяющее пользователя, информацию о котором нужно получить
     * @return аккаунт пользователя
     * @throws UsernameNotFoundException возникает, если в базе не было найдено искомое имя пользователя
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Account> accountOptional = accountRepository.findAccountByUsername(username);

        if (!accountOptional.isPresent()) {
            throw new UsernameNotFoundException("Account not found");
        }

        return accountOptional.get();
    }

    /**
     * Производит поиск аккаунта по его идентификатору.
     *
     * @return найденный аккаунт
     * @throws NoSuchDataException возникает, если аккаунт не был найден
     */
    @Override
    public AccountFacade findById(int idAccount) {
        Optional<Account> accountOptional = accountRepository.findById(idAccount);

        if (!accountOptional.isPresent()) {
            throw new NotFoundException("There is no account with id " + idAccount);
        }

        return accountFacadeConverter.convertToDto(accountOptional.get());
    }

    /**
     * Производит поиск всех аккаунтов
     *
     * @return список всех аккаунтов
     */
    @Override
    public List<AccountFacade> findAll() {
        return accountRepository.findAll().stream().map(accountFacadeConverter::convertToDto).collect(Collectors.toList());
    }

    /**
     * Удаляет аккаунт по его идентификатору
     *
     * @param idAccount идентификатор аккаунта
     */
    @Override
    public void deleteById(int idAccount) {
        accountRepository.deleteById(idAccount);
    }

    /**
     * Обновляет данные об аккаунте
     *
     * @param accountFacade объект аккаунта
     * @return обновленный объект
     */
    @Override
    public AccountFacade update(AccountFacade accountFacade) {
        Optional<Account> accountOptional = accountRepository.findById(accountFacade.getIdAccount());

        if (!accountOptional.isPresent()) {
            throw new NotFoundException("there is no account with id " + accountFacade.getIdAccount());
        }

        return accountFacadeConverter.convertToDto(
                accountRepository.save(
                        accountFacadeConverter.convertToModel(accountFacade)));
    }

    /**
     * Создаёт новый объект аккаунта в базе.
     *
     * @param accountFacade создаваемый объект
     * @return созданный объект
     */
    @Override
    public AccountFacade create(AccountFacade accountFacade) {
        try {
            if (isAccountExists(accountFacade)) {
                throw new UserAlreadyExistsException("The same user already exists");
            }
        } catch (NumberParseException e) {
            throw new IllegalArgumentException("The phone is invalid. Cannot create the account");
        }

        accountFacade.setIdAccount(0);
        UserDetailsFacade userDetailsFacade = accountFacade.getUserDetailsFacade();
        if (userDetailsFacade == null) {
            userDetailsFacade = userDetailsDataService.create(new UserDetailsFacade());
        } else {
            try {
                userDetailsFacade.setPhoneCodeFacade(
                        phoneCodeDataService.findFirstByCountryCut(
                                userDetailsFacade.getPhoneCodeFacade().getCountryCut()));

                userDetailsFacade.setPhone(PhoneNumberUtils.formatNumberInternational(
                        userDetailsFacade.getPhone(), userDetailsFacade.getPhoneCodeFacade().getCountryCut()));
                userDetailsFacade.setDisplayedPhone(userDetailsFacade.getPhone());

                userDetailsDataService.create(userDetailsFacade);
            } catch (NumberParseException e) {
                throw new IllegalArgumentException("Cannot save user details. Phone is incorrect. " + e.getMessage());
            }
        }

        AccountSettingsFacade accountSettingsFacade = accountSettingsDataService.create(new AccountSettingsFacade(
                new Time(9, 0, 0),
                new Time(18, 0, 0)
        ));
        accountFacade.setAccountSettingsFacade(accountSettingsFacade);
        accountFacade.setUserDetailsFacade(userDetailsFacade);

        return accountFacadeConverter.convertToDto(
                accountRepository.save(
                        accountFacadeConverter.convertToModel(accountFacade)));
    }

//    TODO сброс сессии

    /**
     * Применяет частичное изменение объекта, игнорируя null поля и неизменные поля
     *
     * @param accountFacade новый аккаунт, который нужно изменить. Обязательно должен содержать idAccount != 0
     * @return обновленный аккаунт
     */
    public AccountFacade patch(AccountFacade accountFacade) throws NumberParseException {
        AccountFacade patchedAccountFacade = this.findById(accountFacade.getIdAccount());

        UserDetailsFacade patchedUserDetailsFacade = patchedAccountFacade.getUserDetailsFacade();
        UserDetailsFacade userDetailsFacade = accountFacade.getUserDetailsFacade();

        if (patchedUserDetailsFacade == null) {
            patchedUserDetailsFacade = new UserDetailsFacade();
            patchedAccountFacade.setUserDetailsFacade(patchedUserDetailsFacade);
            patchedUserDetailsFacade = userDetailsDataService.create(patchedUserDetailsFacade);
        }

        if (userDetailsFacade != null) {

            if (userDetailsFacade.getPhoneCodeFacade() != null
                    && userDetailsFacade.getPhoneCodeFacade().getCountryCut() != null
                    && !userDetailsFacade.getPhoneCodeFacade().getCountryCut().isEmpty()
                    && userDetailsFacade.getPhone() != null
                    && !userDetailsFacade.getPhone().isEmpty()) {

                if (userDetailsDataService.existsUserDetailsByPhone(userDetailsFacade.getPhone(),
                        userDetailsFacade.getPhoneCodeFacade().getCountryCut())) {
                    throw new UserAlreadyExistsException("User with the same phone already exists");
                } else {
                    String formattedPhone = PhoneNumberUtils.formatNumberInternational(
                            userDetailsFacade.getPhone(), userDetailsFacade.getPhoneCodeFacade().getCountryCut());

                    patchedUserDetailsFacade.setPhoneCodeFacade(
                            phoneCodeDataService.findFirstByCountryCut(userDetailsFacade.getPhoneCodeFacade().getCountryCut()));
                    patchedUserDetailsFacade.setPhone(formattedPhone);
                    patchedUserDetailsFacade.setDisplayedPhone(formattedPhone);
                }
            }

            if (userDetailsFacade.getAboutFacade() != null) {
                AboutFacade patchedAbout = userDetailsFacade.getAboutFacade();
                if (patchedAccountFacade.getUserDetailsFacade().getAboutFacade() == null
                        || patchedAccountFacade.getUserDetailsFacade().getAboutFacade().getIdAbout() == 0) {

                    patchedAccountFacade.getUserDetailsFacade().setAboutFacade(aboutDataService.create(new AboutFacade()));
                }

                patchedUserDetailsFacade.getAboutFacade().setText(patchedAbout.getText());
                patchedUserDetailsFacade.getAboutFacade().setProfession(patchedAbout.getProfession());
                patchedUserDetailsFacade.getAboutFacade().setTags(patchedAbout.getTags());
            }

            if (userDetailsFacade.getFirstName() != null) {
                patchedUserDetailsFacade.setFirstName(userDetailsFacade.getFirstName());
            }

            if (userDetailsFacade.getLastName() != null) {
                patchedUserDetailsFacade.setLastName(userDetailsFacade.getLastName());
            }
        }

        if (accountFacade.getPassword() != null && !accountFacade.getPassword().isEmpty()) {
            if (passwordEncoder.matches(accountFacade.getOldPassword(), patchedAccountFacade.getPassword())) {
                patchedAccountFacade.setPassword(passwordEncoder.encode(accountFacade.getPassword()));
            } else {
                throw new BadCredentialsException("Old password is incorrect");
            }
        }

        if (accountFacade.getUsername() != null && !accountFacade.getUsername().isEmpty()) {
            if (!accountFacade.getUsername().equals(patchedAccountFacade.getUsername())) {
                if (accountRepository.existsAccountByUsername(accountFacade.getUsername())) {
                    throw new UserAlreadyExistsException("User with the same username already exists");
                } else {
                    patchedAccountFacade.setUsername(accountFacade.getUsername());
                }
            }
        }

        if (accountFacade.getEmail() != null && !accountFacade.getEmail().isEmpty()) {
            if (accountRepository.existsByEmail(accountFacade.getEmail())) {
                throw new UserAlreadyExistsException("User with the same email already exists");
            } else {
                if (accountFacade.getEmail().matches(EMAIL_REGEXP)) {
                    patchedAccountFacade.setEmail(accountFacade.getEmail());
                } else {
                    throw new IllegalArgumentException("This email value doesn't matches the template form.");
                }
            }
        }

        if (accountFacade.getAddress() != null && !accountFacade.getAddress().isEmpty()) {
            patchedAccountFacade.setAddress(accountFacade.getAddress());
        }

        if (accountFacade.getAccountSettingsFacade() != null) {
            AccountSettingsFacade accountSettingsFacade = accountFacade.getAccountSettingsFacade();
            if (accountSettingsFacade.getDefaultWorkingDayStart() != null
                    || accountSettingsFacade.getDefaultWorkingDayEnd() != null
                    || patchedAccountFacade.getAccountSettingsFacade().isPhoneVisible() != accountSettingsFacade.isPhoneVisible()) {
                accountSettingsDataService.patch(accountFacade.getIdAccount(), accountSettingsFacade);
            }
        }

        logger.info("Saving account...");
        return accountFacadeConverter.convertToDto(
                accountRepository.save(
                        accountFacadeConverter.convertToModel(patchedAccountFacade)));
    }

    /**
     * Проверяет, существует ли аккаунт в базе. Проверяет по id, имени пользователя и номеру телефона.
     *
     * @param accountFacade искомый аккаунт
     * @return true - в случае, если такой аккаунт существует, false - в ином случае
     */
    public boolean isAccountExists(AccountFacade accountFacade) throws NumberParseException {
        UserDetailsFacade userDetailsFacade = accountFacade.getUserDetailsFacade();

        return accountRepository.existsById(accountFacade.getIdAccount())
                || accountRepository.existsAccountByUsername(accountFacade.getUsername())
                || userDetailsDataService.existsUserDetailsByPhone(
                userDetailsFacade.getPhone(),
                userDetailsFacade.getPhoneCodeFacade().getCountryCut());
    }
}
