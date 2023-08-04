package net.chikaboom.service.action;

import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.repository.AccountRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис предназначен для получения информации об аккаунте.
 */
@Service
@Transactional
public class AccountInfoLoaderService {

    private final AccountRepository accountRepository;

    private final Logger logger = Logger.getLogger(AccountInfoLoaderService.class);

    @Autowired
    public AccountInfoLoaderService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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
