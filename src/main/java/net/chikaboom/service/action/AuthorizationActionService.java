package net.chikaboom.service.action;

import net.chikaboom.exception.IncorrectInputDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.PhoneCodeRepository;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.HashPasswordService;
import net.chikaboom.util.PhoneNumberConverter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Сервис реализует авторизацию пользователя на сайте
 */
@Service
@Transactional
public class AuthorizationActionService implements ActionService {

    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;
    @Value("${attr.phone}")
    private String PHONE;
    @Value("${page.account}")
    private String ACCOUNT_PAGE;
    @Value("${attr.phoneCode}")
    private String PHONE_CODE;
    @Value("${attr.servletRequest}")
    private String SERVLET_REQUEST;
    @Value("${attr.password}")
    private String PASSWORD;

    private final ClientDataStorageService clientDataStorageService;
    private final HashPasswordService hashPasswordService;
    private final AccountRepository accountRepository;
    private final PhoneCodeRepository phoneCodeRepository;

    private final Logger logger = Logger.getLogger(AuthorizationActionService.class);

    @Autowired
    public AuthorizationActionService(ClientDataStorageService clientDataStorageService, AccountRepository accountRepository,
                                      HashPasswordService hashPasswordService, PhoneCodeRepository phoneCodeRepository) {
        this.clientDataStorageService = clientDataStorageService;
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
    @Override
    public String executeAndGetPage() {
        logger.info("Login procedure started");

        int phoneCode = Integer.parseInt(clientDataStorageService.getData(PHONE_CODE).toString());

        int idPhoneCode = phoneCodeRepository.findOneByPhoneCode(phoneCode).getIdPhoneCode();

        String phone = clientDataStorageService.getData(PHONE).toString();
        phone = PhoneNumberConverter.clearPhoneNumber(phone);

        Account account = accountRepository.findOneByPhoneAndIdPhoneCode(phone, idPhoneCode);

        if (account != null) {
            String actualPassword = hashPasswordService.convertPasswordForComparing(
                    clientDataStorageService.getData(PASSWORD).toString(), account.getSalt());

            if (account.getPassword().equals(actualPassword)) {
                logger.info("User logged in");
                initSession((HttpServletRequest) clientDataStorageService.getData(SERVLET_REQUEST), account, phoneCode);

                clientDataStorageService.clearAllData();

                return ACCOUNT_PAGE + "/" + account.getIdAccount();
            }
        }

        clientDataStorageService.clearAllData();
        logger.info("User has NOT logged in. Password or phone is incorrect.");

        throw new IncorrectInputDataException("Phone and/or password are/is incorrect");
    }

    /**
     * Метод для инициализации сессии параметрами пользователя
     *
     * @param request   запрос, в котором передана сессия
     * @param account   аккаунт пользователя
     * @param phoneCode код телефона страны
     */
    private void initSession(HttpServletRequest request, Account account, int phoneCode) {
        HttpSession session = request.getSession();

        session.setAttribute(PHONE_CODE, phoneCode);
        session.setAttribute(PHONE, account.getPhone());
        session.setAttribute(ID_ACCOUNT, account.getIdAccount());
    }
}
