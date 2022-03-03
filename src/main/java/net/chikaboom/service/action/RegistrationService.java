package net.chikaboom.service.action;

import net.chikaboom.model.ExpandableObject;
import net.chikaboom.model.database.Account;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.HashPasswordService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Класс реализует команду создания нового аккаунта
 */
@Service
public class RegistrationService implements ActionService {

    @Value("${page.main}")
    private String MAIN_PAGE;
    ClientDataStorageService clientDataStorageService;
    HashPasswordService hashPasswordService;
    AccountRepository accountRepository;
    AuthorizationService authorizationService;

    Logger logger = Logger.getLogger(RegistrationService.class);

    @Autowired
    public RegistrationService(ClientDataStorageService clientDataStorageService,
                               HashPasswordService hashPasswordService,
                               AuthorizationService authorizationService,
                               AccountRepository accountRepository) {
        this.clientDataStorageService = clientDataStorageService;
        this.hashPasswordService = hashPasswordService;
        this.authorizationService = authorizationService;
        this.accountRepository = accountRepository;
    }

    /**
     * Реализация команды регистрации нового аккаунта
     *
     * @return возвращает страницу аккаунта (страницу заполнения личных данных?) ВРЕМЕННО. На главную страницу
     */
    @Override
    public String execute() {

        logger.info("New account creating started.");

        Account account = new Account();

        String clearPassword = clientDataStorageService.getData("password").toString();
        ExpandableObject expandableObject = hashPasswordService.saltPassword(clearPassword);
        String convertedPassword = hashPasswordService.hashPassword(
                expandableObject.getField("saltedPassword").toString());

        account.setEmail(clientDataStorageService.getData("email").toString());
        account.setPassword(convertedPassword);
        account.setSalt(expandableObject.getField("salt").toString());

        accountRepository.save(account);

        logger.info("New account created");

        return MAIN_PAGE;
    }
}
