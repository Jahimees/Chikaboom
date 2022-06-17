package net.chikaboom.repository;

import net.chikaboom.model.database.Address;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы Address
 */
@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {
}
