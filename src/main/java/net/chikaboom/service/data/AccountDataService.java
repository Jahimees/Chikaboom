package net.chikaboom.service.data;

import com.google.i18n.phonenumbers.NumberParseException;
import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.exception.UserAlreadyExistsException;
import net.chikaboom.model.database.About;
import net.chikaboom.model.database.Account;
import net.chikaboom.repository.AboutRepository;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.PhoneCodeRepository;
import net.chikaboom.repository.UserDetailsRepository;
import net.chikaboom.util.PhoneNumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис предназначен для получения информации об аккаунте.
 */
@RequiredArgsConstructor
@Service
public class AccountDataService implements UserDetailsService, DataService<Account> {

    @Value("${regexp.email}")
    private String EMAIL_REGEXP;

    private final AccountRepository accountRepository;
    private final UserDetailsRepository userDetailsRepository;
    private final UserDetailsDataService userDetailsDataService;
    private final AboutRepository aboutRepository;
    private final PhoneCodeRepository phoneCodeRepository;
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
    public Optional<Account> findById(int idAccount) {
        logger.info("Loading account info with id " + idAccount);

        return accountRepository.findById(idAccount);
    }

    /**
     * Производит поиск всех аккаунтов
     *
     * @return список всех аккаунтов
     */
    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
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
     * @param account объект аккаунта
     * @return обновленный объект
     */
    @Override
    public Account update(Account account) {
        return accountRepository.save(account);
    }

    /**
     * Создаёт новый объект аккаунта в базе.
     *
     * @param account создаваемый объект
     * @return созданный объект
     */
    @Override
    public Account create(Account account) {
        try {
            if (isAccountExists(account)) {
                throw new UserAlreadyExistsException("The same user already exists");
            }
        } catch (NumberParseException e) {
            throw new IllegalArgumentException("The phone is invalid. Cannot create the account");
        }

        account.setIdAccount(0);
        net.chikaboom.model.database.UserDetails userDetails = account.getUserDetails();
        if (userDetails == null) {
            userDetails = new net.chikaboom.model.database.UserDetails();
        } else {
            try {
                userDetails.setPhoneCode(phoneCodeRepository.findFirstByCountryCut(userDetails.getPhoneCode().getCountryCut()));
                userDetails.setPhone(PhoneNumberUtils.formatNumberInternational(
                        userDetails.getPhone(), userDetails.getPhoneCode().getCountryCut()));
                userDetails.setDisplayedPhone(userDetails.getPhone());

            } catch (NumberParseException e) {
                throw new IllegalArgumentException("Cannot save user details. Phone is incorrect. " + e.getMessage());
            }
        }

        userDetails = userDetailsRepository.saveAndFlush(userDetails);
        account.setUserDetails(userDetails);

        return accountRepository.save(account);
    }

//    TODO refactor Слишком большой метод

    /**
     * Применяет частичное изменение объекта, игнорируя null поля и неизменные поля
     *
     * @param account новый аккаунт, который нужно изменить. Обязательно должен содержать idAccount != 0
     * @return обновленный аккаунт
     */
    public Account patch(Account account) throws NumberParseException {
        Account patchedAccount = accountRepository.findById(account.getIdAccount())
                .orElseThrow(() -> new NoSuchDataException("This user doesn't exist"));

        net.chikaboom.model.database.UserDetails patchedUserDetails = patchedAccount.getUserDetails();
        net.chikaboom.model.database.UserDetails userDetails = account.getUserDetails();

        if (patchedUserDetails == null) {
            patchedUserDetails = new net.chikaboom.model.database.UserDetails();
            patchedAccount.setUserDetails(patchedUserDetails);
            userDetailsRepository.saveAndFlush(patchedUserDetails);
        }

        if (userDetails != null) {

            if (userDetails.getPhoneCode() != null
                    && userDetails.getPhone() != null
                    && !userDetails.getPhone().isEmpty()) {

                if (userDetailsDataService.existsUserDetailsByPhone(userDetails.getPhone(),
                        userDetails.getPhoneCode().getCountryCut())) {
                    throw new UserAlreadyExistsException("User with the same phone already exists");
                } else {
                    String formattedPhone = PhoneNumberUtils.formatNumberInternational(
                            userDetails.getPhone(), userDetails.getPhoneCode().getCountryCut());

                    patchedUserDetails.setPhoneCode(
                            phoneCodeRepository.findFirstByCountryCut(userDetails.getPhoneCode().getCountryCut()));
                    patchedUserDetails.setPhone(formattedPhone);
                    patchedUserDetails.setDisplayedPhone(formattedPhone);
                }
            }

            if (userDetails.getAbout() != null) {
                About patchedAbout = userDetails.getAbout();
                if (patchedAccount.getUserDetails().getAbout() == null
                        || patchedAccount.getUserDetails().getAbout().getIdAbout() == 0) {
                    patchedAccount.getUserDetails().setAbout(new About());
                    aboutRepository.saveAndFlush(patchedAccount.getUserDetails().getAbout());
                }

                patchedUserDetails.getAbout().setText(patchedAbout.getText());
                patchedUserDetails.getAbout().setProfession(patchedAbout.getProfession());
                patchedUserDetails.getAbout().setTags(patchedAbout.getTags());
            }

            if (userDetails.getFirstName() != null) {
                patchedUserDetails.setFirstName(userDetails.getFirstName());
            }

            if (userDetails.getLastName() != null) {
                patchedUserDetails.setLastName(userDetails.getLastName());
            }
        }

        if (account.getPassword() != null && !account.getPassword().isEmpty()) {
            if (passwordEncoder.matches(account.getOldPassword(), patchedAccount.getPassword())) {
                patchedAccount.setPassword(passwordEncoder.encode(account.getPassword()));
            } else {
                throw new BadCredentialsException("Old password is incorrect");
            }
        }

        if (account.getUsername() != null && !account.getUsername().isEmpty()) {
            if (!account.getUsername().equals(patchedAccount.getUsername())) {
                if (accountRepository.existsAccountByUsername(account.getUsername())) {
                    throw new UserAlreadyExistsException("User with the same username already exists");
                } else {
                    patchedAccount.setUsername(account.getUsername());
                }
            }
        }

        if (account.getEmail() != null && !account.getEmail().isEmpty()) {
            if (accountRepository.existsByEmail(account.getEmail())) {
                throw new UserAlreadyExistsException("User with the same email already exists");
            } else {
                if (account.getEmail().matches(EMAIL_REGEXP)) {
                    patchedAccount.setEmail(account.getEmail());
                } else {
                    throw new IllegalArgumentException("This email value doesn't matches the template form.");
                }
            }
        }

        if (account.getAddress() != null && !account.getAddress().isEmpty()) {
            patchedAccount.setAddress(account.getAddress());
        }

        if (account.isPhoneVisible() != patchedAccount.isPhoneVisible()) {
            patchedAccount.setPhoneVisible(account.isPhoneVisible());
        }

        logger.info("Saving account...");
        return accountRepository.save(patchedAccount);
    }

    /**
     * Проверяет, существует ли аккаунт в базе. Проверяет по id, имени пользователя и номеру телефона.
     *
     * @param account искомый аккаунт
     * @return true - в случае, если такой аккаунт существует, false - в ином случае
     */
    public boolean isAccountExists(Account account) throws NumberParseException {
        net.chikaboom.model.database.UserDetails userDetails = account.getUserDetails();

        return accountRepository.existsById(account.getIdAccount())
                || accountRepository.existsAccountByUsername(account.getUsername())
                || userDetailsDataService.existsUserDetailsByPhone(
                userDetails.getPhone(),
                userDetails.getPhoneCode().getCountryCut());
    }
}
