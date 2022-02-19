package net.chikaboom.repository;

import net.chikaboom.model.Master;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы Master
 */
@Repository
public interface MasterRepository extends CrudRepository<Master, String> {
}
