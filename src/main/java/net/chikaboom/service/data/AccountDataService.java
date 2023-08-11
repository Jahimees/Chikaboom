package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.IncorrectInputDataException;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.exception.UserAlreadyExistsException;
import net.chikaboom.model.database.About;
import net.chikaboom.model.database.Account;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.PhoneCodeRepository;
import net.chikaboom.util.PhoneNumberConverter;
import org.apache.log4j.Logger;
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

    @Override
    public List<Account> findAll() {
        return accountRepository.findAll();
    }

    @Override
    public void deleteById(int idAccount) {
        accountRepository.deleteById(idAccount);
    }

    @Override
    public Account update(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account create(Account account) {
        if (isAccountExists(account)) {
            throw new UserAlreadyExistsException("The same user already exists");
        }

        return accountRepository.save(account);
    }

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
                throw new IncorrectInputDataException("Old password is incorrect");
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

//        TODO NEW Проверка на регулярку
        if (account.getEmail() != null && !account.getEmail().isEmpty()) {
            if (accountRepository.existsByEmail(account.getEmail())) {
                throw new UserAlreadyExistsException("User with the same email already exists");
            } else {
                patchedAccount.setEmail(account.getEmail());
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

        logger.info("Saving account...");
        return accountRepository.save(patchedAccount);
    }

    public boolean isAccountExists(Account account) {
        return accountRepository.existsById(account.getIdAccount())
                || accountRepository.existsAccountByUsername(account.getUsername())
                || accountRepository.existsAccountByPhoneCodeAndPhone(account.getPhoneCode(), account.getPhone());
    }
}
