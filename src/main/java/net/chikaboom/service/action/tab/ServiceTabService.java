package net.chikaboom.service.action.tab;

import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Service;
import net.chikaboom.model.database.Subservice;
import net.chikaboom.model.database.UserService;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.ServiceRepository;
import net.chikaboom.repository.SubserviceRepository;
import net.chikaboom.repository.UserServiceRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с данными услуг
 */
@org.springframework.stereotype.Service
public class ServiceTabService {

    private final UserServiceRepository userServiceRepository;
    private final AccountRepository accountRepository;
    private final SubserviceRepository subserviceRepository;
    private final ServiceRepository serviceRepository;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public ServiceTabService(UserServiceRepository userServiceRepository,
                             AccountRepository accountRepository, ServiceRepository serviceRepository,
                             SubserviceRepository subserviceRepository) {
        this.userServiceRepository = userServiceRepository;
        this.accountRepository = accountRepository;
        this.subserviceRepository = subserviceRepository;
        this.serviceRepository = serviceRepository;
    }

    /**
     * Выполняет поиск всех услуг определенного мастера
     *
     * @return коллекцию пользовательских услуг (услуг мастера)
     */
    public List<UserService> findAllUserServicesByIdAccount(int idAccount) {
        logger.info("Searching all userServices of user with id " + idAccount);
        Account account = accountRepository.findById(idAccount).
                orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccount));

        return userServiceRepository.findAllByAccount(account);
    }

    /**
     * Выполняет поиск всех возможных подуслуг
     *
     * @return коллекцию подуслуг
     */
    public List<Subservice> findAllSubservices() {
        return subserviceRepository.findAll();
    }

    /**
     * Сохраняет/создает пользовательскую услугу
     *
     * @return созданную услугу
     */
    public UserService saveUserService(UserService userService) {
        logger.info("Saving userService " + userService.getUserServiceName() + " of user with id " + userService.getAccount().getIdAccount());

        return userServiceRepository.save(userService);
    }

    /**
     * Удаляет пользовательскую услугу
     */
    public void deleteUserService(int idUserService) {
        logger.info("Deleting userService with id " + idUserService);

        userServiceRepository.deleteById(idUserService);
    }

    /**
     * Производит поиск всех подуслуг, которые относятся к данной услуге (idService)
     *
     * @return коллекцию подуслуг
     */
    public List<Subservice> findAllSubservices(int idService) {
        logger.info("Searching all subservices of service with id " + idService);
        Service service = serviceRepository.findById(idService).
                orElseThrow(() -> new NoSuchDataException("Cannot find service with id " + idService));

        return subserviceRepository.findAllByService(service);
    }

    /**
     * Производит поиск всех пользовательских услуг, которые соответствуют представленному перечню id подуслуг.
     * Т.е., если пользовательская услуга относится к подуслуге, которая включена в перечень поиска, то она будет найдена.
     *
     * @return коллекцию пользовательских услуг
     */
    public List<UserService> getUserServicesByServiceIds(int[] subserviceIdArray, int idService) {
        logger.info("Searching all userServices which match their idSubservice to subserviceIdArray");

        List<UserService> userServiceList = new ArrayList<>();

        if (subserviceIdArray.length != 0) {
            logger.info("Found " + subserviceIdArray.length + " selected subservices. Searching data...");
            for (int idSubservice : subserviceIdArray) {
                Subservice subservice = subserviceRepository.findById(idSubservice).
                        orElseThrow(() -> new NoSuchDataException("Cannot find subservice with"));
                userServiceList.addAll(userServiceRepository.findAllBySubservice(subservice));
            }
        } else {
            logger.info("No one subservice selected. Searching all userServices with idService " + idService);

            Service service = serviceRepository.findById(idService).
                    orElseThrow(() -> new NoSuchDataException("Cannot find service with id " + idService));
            List<Subservice> subserviceList = subserviceRepository.findAllByService(service);

            for (Subservice subservice : subserviceList) {
                userServiceList.addAll(userServiceRepository.findAllBySubservice(subservice));
            }
        }

        return userServiceList;
    }
}
