package net.chikaboom.service;

import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.repository.AccountRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Сервис предназначен для получения информации об аккаунте.
 */
@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    /**
     * Производит поиск аккаунта по имени пользователя
     *
     * @param username имя пользователя, определяющее пользователя, информацию о котором нужно получить
     * @return аккаунт пользователя
     * @throws UsernameNotFoundException возникает, если в базе не было найдено искомое имя пользователя
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findAccountByUsername(username);

        if (account == null) {
            throw new UsernameNotFoundException("Account not found");
        }

        return account;
    }

    /**
     * Производит поиск аккаунта по его идентификатору.
     *
     * @return найденный аккаунт
     * @throws NoSuchDataException возникает, если аккаунт не был найден
     */
    public Account findAccountById(int idAccount) {
        logger.info("Loading account info with id " + idAccount);

        return accountRepository.findById(idAccount)
                .orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccount));
    }
}
