package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.exception.UserAlreadyExistsException;
import net.chikaboom.model.database.Account;
import net.chikaboom.repository.AccountRepository;
import org.apache.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public boolean isAccountExists(Account account) {
        return accountRepository.existsById(account.getIdAccount())
                || accountRepository.existsAccountByUsername(account.getUsername())
                || accountRepository.existsAccountByPhoneCodeAndPhone(account.getPhoneCode(), account.getPhone());
    }
}
