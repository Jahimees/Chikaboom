package net.chikaboom.service.action;

import net.chikaboom.exception.UserAlreadyExistsException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.PhoneCode;
import net.chikaboom.model.database.Role;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.PhoneCodeRepository;
import net.chikaboom.util.PhoneNumberConverter;
import net.chikaboom.util.constant.ApplicationRole;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Сервис реализует создание нового аккаунта
 */
@Service
@Transactional
public class RegistrationActionService {

    @Value("${page.main}")
    private String MAIN_PAGE;
    @Value("${attr.role_master}")
    private String ROLE_MASTER;
    @Value("${attr.role_client}")
    private String ROLE_CLIENT;

    private final AccountRepository accountRepository;
    private final PhoneCodeRepository phoneCodeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public RegistrationActionService(AccountRepository accountRepository, PhoneCodeRepository phoneCodeRepository,
                                     BCryptPasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.phoneCodeRepository = phoneCodeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Реализация команды регистрации нового аккаунта
     *
     * @return возвращает главную страницу. В случае неудачи выбрасывает исключение попытки создания существующего
     * пользователя
     */
    public String register(String phoneCodeString, String phone, String clearPassword, String nickname, String roleString) {
        int phoneCodeNumbers = Integer.parseInt(phoneCodeString);
        PhoneCode phoneCode = phoneCodeRepository.findFirstByPhoneCode(phoneCodeNumbers);

        phone = PhoneNumberConverter.clearPhoneNumber(phone);
        if (isUserAlreadyExists(phone, phoneCode, nickname)) {
            throw new UserAlreadyExistsException("User with phone +" + phoneCodeNumbers + " " + phone + " already exists");
        }

        int idRole = ApplicationRole.valueOf(roleString).getValue();
        Role role = new Role(idRole);

        Account account = new Account();
        account.setPhone(phone);
        account.setPassword(clearPassword);
        account.setRegistrationDate(Timestamp.valueOf(LocalDateTime.now()));
        account.setNickname(nickname);
        account.setPhoneCode(phoneCode);

        Set<Role> roleSet = new HashSet<>();
        if (role.getName().equals(ROLE_MASTER)) {
            roleSet.addAll(Arrays.asList(role, new Role(ApplicationRole.ROLE_CLIENT)));
            account.setRoles(roleSet);
        } else if (role.getName().equals(ROLE_CLIENT)) {
            roleSet.add(role);
        }

        account.setRoles(roleSet);
        account.setPassword(passwordEncoder.encode(account.getPassword()));
        accountRepository.save(account);

        logger.info("New account created");

        return MAIN_PAGE;
    }

    private boolean isUserAlreadyExists(String phone, PhoneCode phoneCode, String nickname) {
        return accountRepository.findFirstByPhoneAndPhoneCode(phone, phoneCode) != null
                || accountRepository.findAccountByNickname(nickname) != null;
    }
}
