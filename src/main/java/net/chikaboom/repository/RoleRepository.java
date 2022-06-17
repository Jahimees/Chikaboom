package net.chikaboom.repository;

import net.chikaboom.model.database.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы Role
 */
@Repository
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
