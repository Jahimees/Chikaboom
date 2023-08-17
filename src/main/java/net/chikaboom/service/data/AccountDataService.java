package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.exception.UserAlreadyExistsException;
import net.chikaboom.model.database.About;
import net.chikaboom.model.database.Account;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.PhoneCodeRepository;
import net.chikaboom.util.PhoneNumberConverter;
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
     * Создаёт новый объект аккаугта в базе.
     *
     * @param account создаваемый объект
     * @return созданный объект
     */
    @Override
    public Account create(Account account) {
        if (isAccountExists(account)) {
            throw new UserAlreadyExistsException("The same user already exists");
        }
        account.setIdAccount(0);

        return accountRepository.save(account);
    }

    /**
     * Применяет частичное изменение объекта, игнорируя null поля и неизменные поля
     *
     * @param account новый аккаунт, который нужно изменить. Обязательно должен содержать idAccount != 0
     * @return обновленный аккаунт
     */
    public Account patch(Account account) {
        Account patchedAccount = accountRepository.findById(account.getIdAccount())
                .orElseThrow(() -> new NoSuchDataException("This user doesn't exist"));


        if (account.getPhoneCode() != null && account.getPhone() != null && !account.getPhone().isEmpty()) {
            if (accountRepository.existsAccountByPhoneCodeAndPhone(account.getPhoneCode(), account.getPhone())) {
                throw new UserAlreadyExistsException("User with the same phone already exists");
            } else {
                patchedAccount.setPhoneCode(phoneCodeRepository.findFirstByPhoneCode(account.getPhoneCode().getPhoneCode()));
                patchedAccount.setPhone(PhoneNumberConverter.clearPhoneNumber(account.getPhone()));
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

        if (account.getAbout() != null) {
            About patchedAbout = account.getAbout();
            patchedAccount.getAbout().setText(patchedAbout.getText());
            patchedAccount.getAbout().setProfession(patchedAbout.getProfession());
            patchedAccount.getAbout().setTags(patchedAbout.getTags());
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
    public boolean isAccountExists(Account account) {
        return accountRepository.existsById(account.getIdAccount())
                || accountRepository.existsAccountByUsername(account.getUsername())
                || accountRepository.existsAccountByPhoneCodeAndPhone(account.getPhoneCode(), account.getPhone());
    }
}
