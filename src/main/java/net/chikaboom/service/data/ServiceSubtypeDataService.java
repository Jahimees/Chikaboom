package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.ServiceSubtype;
import net.chikaboom.model.database.ServiceType;
import net.chikaboom.repository.ServiceSubtypeRepository;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервис предоставляет возможность обработки данных подтипа услугии.
 * Пользователям не дана возможность создавать свои подтипы услуг.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ServiceSubtypeDataService {

    private final ServiceSubtypeRepository serviceSubtypeRepository;
    private final ServiceTypeDataService serviceTypeDataService;

    /**
     * Производит поиск подтипа услуги по его идентификатору
     *
     * @param idServiceSubtype идентификатор подтипа услуги
     * @return найденный подтип услуги
     */
    public Optional<ServiceSubtype> findById(int idServiceSubtype) {
        return serviceSubtypeRepository.findById(idServiceSubtype);
    }

    /**
     * Производит поиск всех подтипов услуг.
     *
     * @return список всех подтипов услуг
     */
    public List<ServiceSubtype> findAll() {
        return serviceSubtypeRepository.findAll();
    }

    /**
     * Производит поиск всех подуслуг, которые относятся к данной услуге (idServiceType)
     *
     * @return коллекцию подуслуг
     */
    public List<ServiceSubtype> findAllServiceSubtypesByIdServiceType(int idServiceType) {
        Optional<ServiceType> serviceTypeOptional = serviceTypeDataService.findById(idServiceType);

        if (!serviceTypeOptional.isPresent()) {
            throw new NotFoundException("There not found serviceType with id " + idServiceType);
        }

        return serviceSubtypeRepository
                .findAllByServiceType(serviceTypeOptional.get());
    }
}
