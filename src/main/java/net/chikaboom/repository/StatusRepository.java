package net.chikaboom.repository;

import net.chikaboom.model.database.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы StatusRepository
 */
@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {
}
