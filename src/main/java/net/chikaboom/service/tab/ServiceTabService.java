package net.chikaboom.service.tab;

import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Service;
import net.chikaboom.model.database.ServiceSubtype;
import net.chikaboom.model.database.ServiceType;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.ServiceRepository;
import net.chikaboom.repository.ServiceSubtypeRepository;
import net.chikaboom.repository.ServiceTypeRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с данными услуг
 */
@org.springframework.stereotype.Service
public class ServiceTabService {

    private final ServiceRepository serviceRepository;
    private final AccountRepository accountRepository;
    private final ServiceSubtypeRepository serviceSubtypeRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public ServiceTabService(ServiceRepository serviceRepository,
                             AccountRepository accountRepository, ServiceTypeRepository serviceTypeRepository,
                             ServiceSubtypeRepository serviceSubtypeRepository) {
        this.serviceRepository = serviceRepository;
        this.accountRepository = accountRepository;
        this.serviceSubtypeRepository = serviceSubtypeRepository;
        this.serviceTypeRepository = serviceTypeRepository;
    }

    /**
     * Выполняет поиск всех услуг определенного мастера
     *
     * @return коллекцию пользовательских услуг (услуг мастера)
     */
    public List<Service> findAllServicesByIdAccount(int idAccount) {
        logger.info("Searching all services of user with id " + idAccount);
        Account account = accountRepository.findById(idAccount).
                orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccount));

        return serviceRepository.findAllByAccount(account);
    }

    /**
     * Выполняет поиск всех возможных подуслуг
     *
     * @return коллекцию подуслуг
     */
    public List<ServiceSubtype> findAllServiceSubtypesByIdServiceType() {
        return serviceSubtypeRepository.findAll();
    }

    /**
     * Сохраняет/создает пользовательскую услугу
     *
     * @return созданную услугу
     */
    public Service saveService(Service service) {
        logger.info("Saving service " + service.getName() + " of user with id " + service.getAccount().getIdAccount());

        return serviceRepository.save(service);
    }

    /**
     * Удаляет пользовательскую услугу
     */
    public void deleteService(int idService) {
        logger.info("Deleting service with id " + idService);

        serviceRepository.deleteById(idService);
    }

    /**
     * Производит поиск всех подуслуг, которые относятся к данной услуге (idServiceType)
     *
     * @return коллекцию подуслуг
     */
    public List<ServiceSubtype> findAllServiceSubtypesByIdServiceType(int idServiceType) {
        logger.info("Searching all service subtypes of service type with id " + idServiceType);
        ServiceType serviceType = serviceTypeRepository.findById(idServiceType).
                orElseThrow(() -> new NoSuchDataException("Cannot find service with id " + idServiceType));

        return serviceSubtypeRepository.findAllByServiceType(serviceType);
    }

    /**
     * Производит поиск всех пользовательских услуг, которые соответствуют представленному перечню id подуслуг.
     * Т.е., если пользовательская услуга относится к подуслуге, которая включена в перечень поиска, то она будет найдена.
     *
     * @return коллекцию пользовательских услуг
     */
    public List<Service> getServicesByServiceSubtypeIds(int[] serviceSubtypeIdArray, int idServiceType) {
        logger.info("Searching all services which match their idServiceSubtype to serviceSubtypeIdArray");

        List<Service> serviceList = new ArrayList<>();

        if (serviceSubtypeIdArray.length != 0) {
            logger.info("Found " + serviceSubtypeIdArray.length + " selected service subtypes. Searching data...");
            for (int idServiceSubtype : serviceSubtypeIdArray) {
                ServiceSubtype serviceSubtype = serviceSubtypeRepository.findById(idServiceSubtype).
                        orElseThrow(() -> new NoSuchDataException("Cannot find service subtype with"));
                serviceList.addAll(serviceRepository.findAllByServiceSubtype(serviceSubtype));
            }
        } else {
            logger.info("No one service subtype selected. Searching all services with idServiceType " + idServiceType);

            ServiceType serviceType = serviceTypeRepository.findById(idServiceType).
                    orElseThrow(() -> new NoSuchDataException("Cannot find service with id " + idServiceType));
            List<ServiceSubtype> serviceSubtypeList = serviceSubtypeRepository.findAllByServiceType(serviceType);

            for (ServiceSubtype serviceSubtype : serviceSubtypeList) {
                serviceList.addAll(serviceRepository.findAllByServiceSubtype(serviceSubtype));
            }
        }

        return serviceList;
    }
}
