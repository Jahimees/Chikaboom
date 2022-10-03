package net.chikaboom.service.action.tab;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.PhoneCode;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.PhoneCodeRepository;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.UndefinedDataService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Сервис загрузки данных для вкладки "Настройки - основные"
 */
@Service
public class GeneralTabLoaderDataService implements UndefinedDataService {

    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;
    @Value("${attr.phone}")
    private String PHONE;
    @Value("${attr.phoneCode}")
    private String PHONE_CODE;
    @Value("${attr.email}")
    private String EMAIL;

    private final Logger logger = Logger.getLogger(GeneralTabLoaderDataService.class);
    private final AccountRepository accountRepository;
    private final PhoneCodeRepository phoneCodeRepository;
    private final ClientDataStorageService clientDataStorageService;

    @Autowired
    public GeneralTabLoaderDataService(AccountRepository accountRepository, PhoneCodeRepository phoneCodeRepository,
                                       ClientDataStorageService clientDataStorageService) {
        this.accountRepository = accountRepository;
        this.phoneCodeRepository = phoneCodeRepository;
        this.clientDataStorageService = clientDataStorageService;
    }

    /**
     * Собирает необходимые данные для отображения на клиенте в Map и возвращает их в контроллер
     *
     * @return набор параметров для отображения на странице
     */
    @Override
    public Map<String, Object> execute() {
        logger.info("loading general tab setting client info...");
        int idAccount = (int) clientDataStorageService.getData(ID_ACCOUNT);

        Account account = accountRepository.findById(idAccount).get(); //TODO РАЗОБРАТЬСЯ С GET(); isPresent();
        PhoneCode phoneCode = phoneCodeRepository.findById(account.getIdPhoneCode()).get();

        Map<String, Object> accountParameters = new HashMap<>();
        accountParameters.put(EMAIL, account.getEmail());
        accountParameters.put(ID_ACCOUNT, account.getIdAccount());
        accountParameters.put(PHONE, account.getPhone());
        accountParameters.put(PHONE_CODE, phoneCode.getPhoneCode());

        clientDataStorageService.dropData(ID_ACCOUNT);
        logger.info("general tab setting info loaded successfully");

        return accountParameters;
    }
}
