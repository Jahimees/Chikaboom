package net.chikaboom.repository;

import net.chikaboom.model.Work;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы Work
 */
@Repository
public interface WorkRepository extends CrudRepository<Work, String> {
}
