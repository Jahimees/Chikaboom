package net.chikaboom.service.action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import net.chikaboom.exception.IncorrectInputDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.PhoneCode;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.PhoneCodeRepository;
import net.chikaboom.service.HashPasswordService;
import net.chikaboom.util.PhoneNumberConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис реализует авторизацию пользователя на сайте
 */
@Service
@Transactional
public class AuthorizationActionService {

    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;
    @Value("${attr.phone}")
    private String PHONE;
    @Value("${page.account}")
    private String ACCOUNT_PAGE;

    private final HashPasswordService hashPasswordService;
    private final AccountRepository accountRepository;
    private final PhoneCodeRepository phoneCodeRepository;

    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public AuthorizationActionService(AccountRepository accountRepository, HashPasswordService hashPasswordService, PhoneCodeRepository phoneCodeRepository) {
        this.accountRepository = accountRepository;
        this.hashPasswordService = hashPasswordService;
        this.phoneCodeRepository = phoneCodeRepository;
    }

    /**
     * Реализация команды авторизации
     *
     * @return возвращает страницу пользователя в случае совпадения паролей,
     * выбрасывает ошибку некорректного ввода данных в случае нудачи.
     */
    public String authorize(String phoneCodeString, String phone, String password, HttpServletRequest servletRequest) {
        logger.info("Login procedure started");
        int phoneCodeNumber = Integer.parseInt(phoneCodeString);

        PhoneCode phoneCode = phoneCodeRepository.findOneByPhoneCode(phoneCodeNumber);
        phone = PhoneNumberConverter.clearPhoneNumber(phone);
        Account account = accountRepository.findOneByPhoneAndPhoneCode(phone, phoneCode);

        if (account != null) {
            String actualPassword = hashPasswordService.convertPasswordForComparing(password, account.getSalt());

            if (account.getPassword().equals(actualPassword)) {
//                TODO Убрать сессию
                logger.info("User logged in");
                initSession(servletRequest, account);

                return ACCOUNT_PAGE + "/" + account.getIdAccount();
            }
        }

        logger.info("User has NOT logged in. Password or phone is incorrect.");

        throw new IncorrectInputDataException("Phone and/or password are/is incorrect");
    }

    /**
     * Метод для инициализации сессии параметрами пользователя
     *
     * @param request запрос, в котором передана сессия
     * @param account аккаунт пользователя
     */
    private void initSession(HttpServletRequest request, Account account) {
        HttpSession session = request.getSession();

        session.setAttribute(PHONE, account.getPhone());
        session.setAttribute(ID_ACCOUNT, account.getIdAccount());
    }
}
