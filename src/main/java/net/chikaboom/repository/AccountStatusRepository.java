package net.chikaboom.repository;

import net.chikaboom.model.database.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы AccountStatus
 */
@Repository
public interface AccountStatusRepository extends JpaRepository<AccountStatus, Integer> {
}
