package net.chikaboom.service.action;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.client.AccountInformation;
import net.chikaboom.model.database.*;
import net.chikaboom.repository.*;
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
public class AccountInfoLoaderService implements UndefinedDataService {

    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;
    @Value("${attr.account}")
    private String ACCOUNT;
    @Value("${attr.phoneCode}")
    private String PHONE_CODE;
    @Value("${attr.about}")
    private String ABOUT;
    @Value("${attr.address}")
    private String ADDRESS;

    private final AccountRepository accountRepository;
    private final ClientDataStorageService clientDataStorageService;
    private final PhoneCodeRepository phoneCodeRepository;
    private final AddressRepository addressRepository;
    private final AboutRepository aboutRepository;
    private final RoleRepository roleRepository;

    private final Logger logger = Logger.getLogger(AccountInfoLoaderService.class);

    @Autowired
    public AccountInfoLoaderService(AccountRepository accountRepository, ClientDataStorageService clientDataStorageService,
                                    PhoneCodeRepository phoneCodeRepository, AddressRepository addressRepository,
                                    AboutRepository aboutRepository, RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.clientDataStorageService = clientDataStorageService;
        this.phoneCodeRepository = phoneCodeRepository;
        this.addressRepository = addressRepository;
        this.aboutRepository = aboutRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Map<String, Object> execute() throws NoSuchDataException {
        Map<String, Object> resultParams = new HashMap<>();
        int idAccount = Integer.parseInt(clientDataStorageService.getData(ID_ACCOUNT).toString());

        Account account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccount));
        PhoneCode phoneCode = phoneCodeRepository.findById(account.getIdPhoneCode())
                .orElseThrow(() -> new NoSuchDataException("Cannot find phone code with id " + account.getIdPhoneCode()));

        Address address = addressRepository.findByIdAccount(account.getIdAccount()).orElse(new Address(idAccount));
        About about = aboutRepository.findByIdAccount(idAccount).orElse(new About(idAccount));

        String accountParamsJson = "";
        String phoneCodeParamsJson = "";
        String aboutParamsJson = "";
        String addressParamsJson = "";
        ObjectMapper mapper = new ObjectMapper();

        try {
            accountParamsJson = mapper.writeValueAsString(account);
            phoneCodeParamsJson = mapper.writeValueAsString(phoneCode);
            aboutParamsJson = mapper.writeValueAsString(about);
            addressParamsJson = mapper.writeValueAsString(address);
        } catch (JsonProcessingException e) {
            logger.error("Cannot create json from value", e);
        }

        resultParams.put(ACCOUNT, accountParamsJson);
        resultParams.put(PHONE_CODE, phoneCodeParamsJson);
        resultParams.put(ABOUT, aboutParamsJson);
        resultParams.put(ADDRESS, addressParamsJson);

        return resultParams;
    }

    public AccountInformation loadPublicAccountInformation() {
        Integer idAccount = Integer.valueOf(clientDataStorageService.getData(ID_ACCOUNT).toString());

        Account account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccount));
        PhoneCode phoneCode = phoneCodeRepository.findById(account.getIdPhoneCode())
                .orElseThrow(() -> new NoSuchDataException("Cannot find phone code with id " + account.getIdAccount()));
        Role role = roleRepository.findById(account.getIdRole())
                .orElseThrow(() -> new NoSuchDataException("Cannot find role with id " + account.getIdRole()));

        Address address = addressRepository.findByIdAccount(account.getIdAccount()).orElse(new Address());

        About about = aboutRepository.findByIdAccount(account.getIdAccount()).orElse(new About());

        return new AccountInformation(account.getIdAccount(), phoneCode.getPhoneCode(),
                account.getPhone(), account.getNickname().toUpperCase(), account.getRegistrationDate(), role.getRole(),
                account.getEmail(), address.getAddress(), about.getTags(), about.getText(), about.getProfession());
    }
}
