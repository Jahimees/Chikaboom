package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.ServiceType;
import net.chikaboom.repository.ServiceTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Сервис предоставляет возможность обработки данных типа услуг.
 */
@RequiredArgsConstructor
@Service
@Transactional
public class ServiceTypeDataService {

    private final ServiceTypeRepository serviceTypeRepository;

    /**
     * Производит поиск типа услуги по его идентификатору.
     *
     * @param idServiceType идентификатор типа услуги
     * @return объект типа услуги
     */
    public Optional<ServiceType> findById(int idServiceType) {
        return serviceTypeRepository.findById(idServiceType);
    }

    /**
     * Производит поиск всех типов услуг.
     *
     * @return список всех типов услуг
     */
    public List<ServiceType> findAll() {
        return serviceTypeRepository.findAll();
    }
}
