package net.chikaboom.service;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.dto.AccountFacade;
import net.chikaboom.facade.dto.RoleFacade;
import net.chikaboom.service.data.AccountDataService;
import net.chikaboom.util.constant.ApplicationRole;
import org.apache.log4j.Logger;
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

    private final AccountDataService accountDataService;
    private final BCryptPasswordEncoder passwordEncoder;

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     * Реализация команды регистрации нового аккаунта
     *
     * @return возвращает созданный аккаунт. В случае неудачи выбрасывает исключение попытки создания существующего
     * пользователя
     */
    public AccountFacade register(AccountFacade accountFacade) {

        Set<RoleFacade> roleSet = new HashSet<>();
        accountFacade.getRolesFacade().forEach(role -> {
            int idRole = ApplicationRole.valueOf(role.getName()).getValue();
            roleSet.add(new RoleFacade(idRole));
        });
        accountFacade.setRolesFacade(roleSet);

        accountFacade.setRegistrationDate(Timestamp.valueOf(LocalDateTime.now()));
        accountFacade.setPassword(passwordEncoder.encode(accountFacade.getPassword()));

        logger.info("Saving new account...");

        return accountDataService.create(accountFacade);
    }
}
