package net.chikaboom.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы AccountStatus
 */
@Repository
public interface AccountStatusRepository extends CrudRepository<AccountStatusRepository, Integer> {
}
