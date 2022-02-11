package net.chikaboom.service.action;

import net.chikaboom.dao.AccountDAO;
import net.chikaboom.model.Account;
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
    AccountDAO accountDAO;
    AuthorizationService authorizationService;

    @Autowired
    public RegistrationService(ClientDataStorageService clientDataStorageService, AccountDAO accountDAO,
                               AuthorizationService authorizationService) {
        this.clientDataStorageService = clientDataStorageService;
        this.accountDAO = accountDAO;
        this.authorizationService = authorizationService;
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

        accountDAO.create(account);

        logger.info("New account created");

        return authorizationService.execute();
    }
}