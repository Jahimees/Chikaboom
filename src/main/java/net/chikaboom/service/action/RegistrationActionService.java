package net.chikaboom.service.action;

import net.chikaboom.exception.UserAlreadyExistsException;
import net.chikaboom.model.ExpandableObject;
import net.chikaboom.model.database.Account;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.HashPasswordService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static net.chikaboom.util.constant.EOFieldsCostant.CONVERTED_PASSWORD;
import static net.chikaboom.util.constant.EOFieldsCostant.SALT;
import static net.chikaboom.util.constant.RequestParametersConstant.EMAIL;
import static net.chikaboom.util.constant.RequestParametersConstant.PASSWORD;

/**
 * Сервис реализует создание нового аккаунта
 */
@Service
public class RegistrationActionService implements ActionService {

    @Value("${page.main}")
    private String MAIN_PAGE;

    private final ClientDataStorageService clientDataStorageService;
    private final HashPasswordService hashPasswordService;
    private final AccountRepository accountRepository;
    private final AuthorizationActionService authorizationActionService;

    Logger logger = Logger.getLogger(RegistrationActionService.class);

    @Autowired
    public RegistrationActionService(ClientDataStorageService clientDataStorageService,
                                     HashPasswordService hashPasswordService,
                                     AuthorizationActionService authorizationActionService,
                                     AccountRepository accountRepository) {
        this.clientDataStorageService = clientDataStorageService;
        this.hashPasswordService = hashPasswordService;
        this.authorizationActionService = authorizationActionService;
        this.accountRepository = accountRepository;
    }

    /**
     * Реализация команды регистрации нового аккаунта
     * TODO Реализовать автоматическую авторизацию после регистрации. Надо ли?
     *
     * @return возвращает главную страницу. В случае неудачи выбрасывает исключение попытки создания существующего
     * пользователя
     */
    @Override
    public String execute() {
        String email = clientDataStorageService.getData(EMAIL).toString();
        if (isUserAlreadyExists(email)) {
            throw new UserAlreadyExistsException("User with email " + email + " already exists");
        }

        Account account = new Account();

        String clearPassword = clientDataStorageService.getData(PASSWORD).toString();
        ExpandableObject complexPasswordEO = hashPasswordService.convertPasswordForStorage(clearPassword);

        account.setEmail(email);
        account.setPassword(complexPasswordEO.getField(CONVERTED_PASSWORD).toString());
        account.setSalt(complexPasswordEO.getField(SALT).toString());
        account.setRegistrationDate(Timestamp.valueOf(LocalDateTime.now()));

        accountRepository.save(account);

        logger.info("New account created");

        return MAIN_PAGE;
    }

    private boolean isUserAlreadyExists(String email) {
        return accountRepository.findOneByEmail(email) != null;
    }
}
