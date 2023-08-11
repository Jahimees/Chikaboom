package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Service;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.ServiceRepository;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@org.springframework.stereotype.Service
public class ServiceDataService implements DataService<Service> {

    private final ServiceRepository serviceRepository;
    private final AccountRepository accountRepository;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Override
    public Optional<Service> findById(int idService) {
        logger.info("Loading service info with id " + idService);

        return serviceRepository.findById(idService);
    }

    @Override
    public List<Service> findAll() {
        return serviceRepository.findAll();
    }

    @Override
    public void deleteById(int idService) {
        serviceRepository.deleteById(idService);
    }

    @Override
    public Service update(Service service) {
        return serviceRepository.save(service);
    }

    //    TODO NEW проверка idService?
    @Override
    public Service create(Service service) {
        return serviceRepository.save(service);
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
}
