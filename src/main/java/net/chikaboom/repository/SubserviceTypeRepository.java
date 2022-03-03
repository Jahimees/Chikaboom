package net.chikaboom.repository;

import net.chikaboom.model.database.SubserviceType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы SubserviceType
 */
@Repository
public interface SubserviceTypeRepository extends CrudRepository<SubserviceType, String> {
}
