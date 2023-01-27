package net.chikaboom.service.action;

import net.chikaboom.exception.UserAlreadyExistsException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.PhoneCode;
import net.chikaboom.model.database.Role;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.PhoneCodeRepository;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.HashPasswordService;
import net.chikaboom.util.PhoneNumberConverter;
import net.chikaboom.util.constant.ApplicationRole;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * Сервис реализует создание нового аккаунта
 */
@Service
@Transactional
public class RegistrationActionService implements ActionService {

    @Value("${page.main}")
    private String MAIN_PAGE;
    @Value("${attr.phoneCode}")
    private String PHONE_CODE;
    @Value("${attr.phone}")
    private String PHONE;
    @Value("${attr.password}")
    private String PASSWORD;
    @Value("${attr.role}")
    private String ROLE;
    @Value("${attr.nickname}")
    private String NICKNAME;

    @Value("${converted_password}")
    private String CONVERTED_PASSWORD;
    @Value("${salt}")
    private String SALT;

    private final ClientDataStorageService clientDataStorageService;
    private final HashPasswordService hashPasswordService;
    private final AccountRepository accountRepository;
    private final PhoneCodeRepository phoneCodeRepository;

    private final Logger logger = Logger.getLogger(RegistrationActionService.class);

    @Autowired
    public RegistrationActionService(ClientDataStorageService clientDataStorageService,
                                     HashPasswordService hashPasswordService,
                                     AccountRepository accountRepository,
                                     PhoneCodeRepository phoneCodeRepository) {
        this.clientDataStorageService = clientDataStorageService;
        this.hashPasswordService = hashPasswordService;
        this.accountRepository = accountRepository;
        this.phoneCodeRepository = phoneCodeRepository;
    }

    /**
     * Реализация команды регистрации нового аккаунта
     *
     * @return возвращает главную страницу. В случае неудачи выбрасывает исключение попытки создания существующего
     * пользователя
     */
    @Override
    public String executeAndGetPage() {
        int phoneCodeNumbers = Integer.parseInt(clientDataStorageService.getData(PHONE_CODE).toString());
        PhoneCode phoneCode = phoneCodeRepository.findOneByPhoneCode(phoneCodeNumbers);

        String phone = clientDataStorageService.getData(PHONE).toString();
        phone = PhoneNumberConverter.clearPhoneNumber(phone);
        if (isUserAlreadyExists(phone, phoneCode)) {
            throw new UserAlreadyExistsException("User with phone +" + phoneCodeNumbers + " " + phone + " already exists");
        }

        Account account = new Account();

        String roleStr = clientDataStorageService.getData(ROLE).toString();
        int idRole = ApplicationRole.valueOf(roleStr.toUpperCase()).getValue();

        String nickname = clientDataStorageService.getData(NICKNAME).toString();

        String clearPassword = clientDataStorageService.getData(PASSWORD).toString();
        Map<String, Object> complexPasswordEO = hashPasswordService.convertPasswordForStorage(clearPassword);

        Role role = new Role(idRole);

        account.setPhone(phone);
        account.setPassword(complexPasswordEO.get(CONVERTED_PASSWORD).toString());
        account.setSalt(complexPasswordEO.get(SALT).toString());
        account.setRegistrationDate(Timestamp.valueOf(LocalDateTime.now()));
        account.setRole(role);
        account.setNickname(nickname);
        account.setPhoneCode(phoneCode);

        accountRepository.save(account);

        clientDataStorageService.clearAllData();

        logger.info("New account created");

        return MAIN_PAGE;
    }

    private boolean isUserAlreadyExists(String phone, PhoneCode phoneCode) {
        return accountRepository.findOneByPhoneAndPhoneCode(phone, phoneCode) != null;
    }
}
