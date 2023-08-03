package net.chikaboom.service.action;

import net.chikaboom.exception.UserAlreadyExistsException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.PhoneCode;
import net.chikaboom.model.database.Role;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.PhoneCodeRepository;
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
public class RegistrationActionService {

    @Value("${page.main}")
    private String MAIN_PAGE;
    @Value("${converted_password}")
    private String CONVERTED_PASSWORD;
    @Value("${salt}")
    private String SALT;

    private final HashPasswordService hashPasswordService;
    private final AccountRepository accountRepository;
    private final PhoneCodeRepository phoneCodeRepository;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public RegistrationActionService(HashPasswordService hashPasswordService,
                                     AccountRepository accountRepository,
                                     PhoneCodeRepository phoneCodeRepository) {
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
    public String register(String phoneCodeString, String phone, String clearPassword, String nickname, String roleString) {
        int phoneCodeNumbers = Integer.parseInt(phoneCodeString);
        PhoneCode phoneCode = phoneCodeRepository.findOneByPhoneCode(phoneCodeNumbers);

        phone = PhoneNumberConverter.clearPhoneNumber(phone);
        if (isUserAlreadyExists(phone, phoneCode)) {
            throw new UserAlreadyExistsException("User with phone +" + phoneCodeNumbers + " " + phone + " already exists");
        }

        Map<String, Object> complexPassword = hashPasswordService.convertPasswordForStorage(clearPassword);

        int idRole = ApplicationRole.valueOf(roleString.toUpperCase()).getValue();
        Role role = new Role(idRole);

        Account account = new Account();
        account.setPhone(phone);
        account.setPassword(complexPassword.get(CONVERTED_PASSWORD).toString());
        account.setSalt(complexPassword.get(SALT).toString());
        account.setRegistrationDate(Timestamp.valueOf(LocalDateTime.now()));
        account.setRole(role);
        account.setNickname(nickname);
        account.setPhoneCode(phoneCode);

        accountRepository.save(account);

        logger.info("New account created");

        return MAIN_PAGE;
    }

    private boolean isUserAlreadyExists(String phone, PhoneCode phoneCode) {
        return accountRepository.findOneByPhoneAndPhoneCode(phone, phoneCode) != null;
    }
}
