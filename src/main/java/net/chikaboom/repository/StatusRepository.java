package net.chikaboom.repository;

import net.chikaboom.model.database.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы StatusRepository
 */
@Repository
public interface StatusRepository extends CrudRepository<Status, Integer> {
}
