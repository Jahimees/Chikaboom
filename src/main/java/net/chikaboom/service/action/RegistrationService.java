package net.chikaboom.service.action;

import net.chikaboom.model.Account;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.service.ClientDataStorageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Класс реализует команду создания нового аккаунта
 */
@Service
public class RegistrationService implements ActionService {

    Logger logger = Logger.getLogger(RegistrationService.class);
    ClientDataStorageService clientDataStorageService;
    AccountRepository accountRepository;
    AuthorizationService authorizationService;

    @Autowired
    public RegistrationService(ClientDataStorageService clientDataStorageService,
                               AuthorizationService authorizationService,
                               AccountRepository accountRepository) {
        this.clientDataStorageService = clientDataStorageService;
        this.authorizationService = authorizationService;
        this.accountRepository = accountRepository;
    }
    /**
     * Реализация команды регистрации нового аккаунта
     * @return возвращает страницу аккаунта (страницу заполнения личных данных?)
     */
    @Override
    public String execute() {

        logger.info("New account creating started.");

        Account account = new Account();

        account.setEmail(clientDataStorageService.getData("email").toString());
        account.setPassword(clientDataStorageService.getData("password").toString());

        accountRepository.save(account);

        logger.info("New account created");

        return authorizationService.execute();
    }
}
