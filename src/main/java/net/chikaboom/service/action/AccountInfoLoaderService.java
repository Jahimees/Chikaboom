package net.chikaboom.service.action;

import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.service.ClientDataStorageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис предназначен для получения информации об аккаунте.
 */
@Service
@Transactional
public class AccountInfoLoaderService implements DataService {

    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;

    private final AccountRepository accountRepository;
    private final ClientDataStorageService clientDataStorageService;

    private final Logger logger = Logger.getLogger(AccountInfoLoaderService.class);

    @Autowired
    public AccountInfoLoaderService(AccountRepository accountRepository, ClientDataStorageService clientDataStorageService) {
        this.accountRepository = accountRepository;
        this.clientDataStorageService = clientDataStorageService;
    }

    /**
     * Производит поиск аккаунта по его идентификатору.
     *
     * @return найденный аккаунт
     * @throws NoSuchDataException возникает, если аккаунт не был найден
     */
    @Override
    public Account executeAndGetOne() throws NoSuchDataException {
        Integer idAccount = Integer.valueOf(clientDataStorageService.getData(ID_ACCOUNT).toString());
        logger.info("Loading account info with id " + idAccount);

        return accountRepository.findById(idAccount)
                .orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccount));
    }
}
