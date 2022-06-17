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
import static net.chikaboom.util.constant.RequestParametersConstant.PASSWORD;
import static net.chikaboom.util.constant.RequestParametersConstant.PHONE;

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

    Logger logger = Logger.getLogger(RegistrationActionService.class);

    @Autowired
    public RegistrationActionService(ClientDataStorageService clientDataStorageService,
                                     HashPasswordService hashPasswordService,
                                     AccountRepository accountRepository) {
        this.clientDataStorageService = clientDataStorageService;
        this.hashPasswordService = hashPasswordService;
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
        String phone = clientDataStorageService.getData(PHONE).toString();
        if (isUserAlreadyExists(phone)) {
            throw new UserAlreadyExistsException("User with phone " + phone + " already exists");
        }

        Account account = new Account();

        String clearPassword = clientDataStorageService.getData(PASSWORD).toString();
        ExpandableObject complexPasswordEO = hashPasswordService.convertPasswordForStorage(clearPassword);

        account.setPhone(phone);
        account.setPassword(complexPasswordEO.getField(CONVERTED_PASSWORD).toString());
        account.setSalt(complexPasswordEO.getField(SALT).toString());
        account.setRegistrationDate(Timestamp.valueOf(LocalDateTime.now()));

        accountRepository.save(account);

        logger.info("New account created");

        return MAIN_PAGE;
    }

    private boolean isUserAlreadyExists(String phone) {
        return accountRepository.findOneByPhone(phone) != null;
    }
}
