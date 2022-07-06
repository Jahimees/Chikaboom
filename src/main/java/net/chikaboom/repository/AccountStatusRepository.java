package net.chikaboom.repository;

import net.chikaboom.model.database.AccountStatus;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы AccountStatus
 */
@Repository
public interface AccountStatusRepository extends CrudRepository<AccountStatus, Integer> {
}
