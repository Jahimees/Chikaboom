package net.chikaboom.repository;

import net.chikaboom.model.database.About;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы About
 */
@Repository
public interface AboutRepository extends CrudRepository<About, Integer> {
}
