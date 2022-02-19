package net.chikaboom.repository;

import net.chikaboom.model.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы Client
 */
@Repository
public interface ClientRepository extends CrudRepository<Client, String> {
}
