package net.chikaboom.repository;

import net.chikaboom.model.database.WorkingDays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы workingDays
 */
@Repository
public interface WorkingDaysRepository extends JpaRepository<WorkingDays, Integer> {
}
