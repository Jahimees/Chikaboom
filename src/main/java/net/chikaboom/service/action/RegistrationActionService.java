package net.chikaboom.service.action;

import net.chikaboom.exception.UserAlreadyExistsException;
import net.chikaboom.model.database.Account;
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

import static net.chikaboom.util.constant.EOFieldsCostant.CONVERTED_PASSWORD;
import static net.chikaboom.util.constant.EOFieldsCostant.SALT;

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
        int phoneCode = Integer.parseInt(clientDataStorageService.getData(PHONE_CODE).toString());
        int idPhoneCode = phoneCodeRepository.findOneByPhoneCode(phoneCode).getIdPhoneCode();
        String phone = clientDataStorageService.getData(PHONE).toString();
        phone = PhoneNumberConverter.clearPhoneNumber(phone);
        if (isUserAlreadyExists(phone, idPhoneCode)) {
            throw new UserAlreadyExistsException("User with phone +" + phoneCode + " " + phone + " already exists");
        }

        Account account = new Account();

        String role = clientDataStorageService.getData(ROLE).toString();
        int idRole = ApplicationRole.valueOf(role.toUpperCase()).ordinal();

        String nickname = clientDataStorageService.getData(NICKNAME).toString();

        String clearPassword = clientDataStorageService.getData(PASSWORD).toString();
        Map<String, Object> complexPasswordEO = hashPasswordService.convertPasswordForStorage(clearPassword);

        account.setIdPhoneCode(idPhoneCode);
        account.setPhone(phone);
        account.setPassword(complexPasswordEO.get(CONVERTED_PASSWORD).toString());
        account.setSalt(complexPasswordEO.get(SALT).toString());
        account.setRegistrationDate(Timestamp.valueOf(LocalDateTime.now()));
        account.setIdRole(idRole);
        account.setNickname(nickname);

        accountRepository.save(account);

        clientDataStorageService.clearAllData();

        logger.info("New account created");

        return MAIN_PAGE;
    }

    private boolean isUserAlreadyExists(String phone, int idPhoneCode) {
        return accountRepository.findOneByPhoneAndIdPhoneCode(phone, idPhoneCode) != null;
    }
}
