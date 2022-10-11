package net.chikaboom.service.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.PhoneCode;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.PhoneCodeRepository;
import net.chikaboom.service.ClientDataStorageService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@PropertySource("/constants.properties")
@Transactional
public class LoadPersonalityService implements UndefinedDataService {

    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;
    @Value("${attr.account}")
    private String ACCOUNT;
    @Value("${attr.phoneCode}")
    private String PHONE_CODE;

    private final AccountRepository accountRepository;
    private final ClientDataStorageService clientDataStorageService;
    private final PhoneCodeRepository phoneCodeRepository;

    private final Logger logger = Logger.getLogger(LoadPersonalityService.class);

    @Autowired
    public LoadPersonalityService(AccountRepository accountRepository, ClientDataStorageService clientDataStorageService, PhoneCodeRepository phoneCodeRepository) {
        this.accountRepository = accountRepository;
        this.clientDataStorageService = clientDataStorageService;
        this.phoneCodeRepository = phoneCodeRepository;
    }

    @Override
    public Map<String, Object> execute() throws NoSuchDataException {
        Map<String, Object> resultParams = new HashMap<>();
        Integer idAccount = Integer.valueOf(clientDataStorageService.getData(ID_ACCOUNT).toString());

        Account account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccount));
        PhoneCode phoneCode = phoneCodeRepository.findById(account.getIdPhoneCode())
                .orElseThrow(() -> new NoSuchDataException("Cannot find phone code with id "));

        String accountParamsJson = "";
        String phoneCodeParamsJson = "";
        ObjectMapper mapper = new ObjectMapper();

        try {
            accountParamsJson = mapper.writeValueAsString(account);
            phoneCodeParamsJson = mapper.writeValueAsString(phoneCode);
        } catch (JsonProcessingException e) {
            logger.error("Cannot create json from value", e);
        }

        resultParams.put(ACCOUNT, accountParamsJson);
        resultParams.put(PHONE_CODE, phoneCodeParamsJson);

        return resultParams;
    }
}
