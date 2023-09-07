package net.chikaboom.repository;

import net.chikaboom.model.database.ServiceSubtype;
import net.chikaboom.model.database.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Интерфейс для CRUD обработки таблицы ServiceSubtype
 */
@Repository
public interface ServiceSubtypeRepository extends JpaRepository<ServiceSubtype, Integer>, JpaSpecificationExecutor<ServiceSubtype> {

    /**
     * Находит все подтипы услуг по типу услуг
     *
     * @param serviceType тип услуги, по которому нужно найти все подтипы услуг
     * @return список подтипов услуг
     */
    List<ServiceSubtype> findAllByServiceType(ServiceType serviceType);
}
