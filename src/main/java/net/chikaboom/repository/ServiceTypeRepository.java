package net.chikaboom.repository;

import net.chikaboom.model.ServiceType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы ServiceType
 */
@Repository
public interface ServiceTypeRepository extends CrudRepository<ServiceType, String> {
}
