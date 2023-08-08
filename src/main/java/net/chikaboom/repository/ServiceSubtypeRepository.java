package net.chikaboom.repository;

import net.chikaboom.model.database.ServiceSubtype;
import net.chikaboom.model.database.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Интерфейс для CRUD обработки таблицы ServiceSubtype
 */
@Repository
public interface ServiceSubtypeRepository extends JpaRepository<ServiceSubtype, Integer> {

    List<ServiceSubtype> findAllByServiceType(ServiceType serviceType);
}
