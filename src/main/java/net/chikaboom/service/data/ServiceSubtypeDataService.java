package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.ServiceSubtype;
import net.chikaboom.model.database.ServiceType;
import net.chikaboom.repository.ServiceSubtypeRepository;
import net.chikaboom.repository.ServiceTypeRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ServiceSubtypeDataService {

    private final ServiceSubtypeRepository serviceSubtypeRepository;
    private final ServiceTypeRepository serviceTypeRepository;
    private final Logger logger = Logger.getLogger(this.getClass());

    public Optional<ServiceSubtype> findById(int id) {
        return serviceSubtypeRepository.findById(id);
    }

    public List<ServiceSubtype> findAll() {
        return serviceSubtypeRepository.findAll();
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
}
