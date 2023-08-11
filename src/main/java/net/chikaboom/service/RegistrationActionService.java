package net.chikaboom.service;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Role;
import net.chikaboom.repository.PhoneCodeRepository;
import net.chikaboom.service.data.AccountDataService;
import net.chikaboom.util.PhoneNumberConverter;
import net.chikaboom.util.constant.ApplicationRole;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Сервис реализует создание нового аккаунта
 */
@RequiredArgsConstructor
@Service
@Transactional
public class RegistrationActionService {

    @Value("${url.main}")
    private String MAIN_URL;

    private final AccountDataService accountDataService;
    private final PhoneCodeRepository phoneCodeRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Реализация команды регистрации нового аккаунта
     *
     * @return возвращает созданный аккаунт. В случае неудачи выбрасывает исключение попытки создания существующего
     * пользователя
     */
    public Account register(Account account) {
        account.setPhoneCode(phoneCodeRepository.findFirstByPhoneCode(account.getPhoneCode().getPhoneCode()));

        account.setPhone(PhoneNumberConverter.clearPhoneNumber(account.getPhone()));

        Set<Role> roleSet = new HashSet<>();
        account.getRoles().forEach(role -> {
            int idRole = ApplicationRole.valueOf(role.getName()).getValue();
            roleSet.add(new Role(idRole));
        });
        account.setRoles(roleSet);

        account.setRegistrationDate(Timestamp.valueOf(LocalDateTime.now()));
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        logger.info("Saving new account...");

        return accountDataService.create(account);
    }
}
