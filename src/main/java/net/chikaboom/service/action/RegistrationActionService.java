package net.chikaboom.service.action;

import net.chikaboom.exception.UserAlreadyExistsException;
import net.chikaboom.model.ExpandableObject;
import net.chikaboom.model.database.Account;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.HashPasswordService;
import net.chikaboom.util.PhoneNumberConverter;
import net.chikaboom.util.constant.ApplicationRole;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static net.chikaboom.util.constant.EOFieldsCostant.CONVERTED_PASSWORD;
import static net.chikaboom.util.constant.EOFieldsCostant.SALT;
import static net.chikaboom.util.constant.RequestParametersConstant.*;

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
        String phoneCode = clientDataStorageService.getData(PHONE_CODE).toString();
        String phone = clientDataStorageService.getData(PHONE).toString();
        phone = PhoneNumberConverter.clearPhoneNumber(phone);
        if (isUserAlreadyExists(phone, phoneCode)) {
            throw new UserAlreadyExistsException("User with phone " + phoneCode + " " + phone + " already exists");
        }

        Account account = new Account();

        String role = clientDataStorageService.getData(ROLE).toString();
        int idRole = ApplicationRole.valueOf(role.toUpperCase()).ordinal();

        String nickname = clientDataStorageService.getData(NICKNAME).toString();

        String clearPassword = clientDataStorageService.getData(PASSWORD).toString();
        ExpandableObject complexPasswordEO = hashPasswordService.convertPasswordForStorage(clearPassword);

        account.setPhoneCode(phoneCode);
        account.setPhone(phone);
        account.setPassword(complexPasswordEO.getField(CONVERTED_PASSWORD).toString());
        account.setSalt(complexPasswordEO.getField(SALT).toString());
        account.setRegistrationDate(Timestamp.valueOf(LocalDateTime.now()));
        account.setIdRole(idRole);
        account.setNickname(nickname);

        accountRepository.save(account);

        logger.info("New account created");

        return MAIN_PAGE;
    }

    private boolean isUserAlreadyExists(String phone, String phoneCode) {
        return accountRepository.findOneByPhoneAndPhoneCode(phone, phoneCode) != null;
    }
}
