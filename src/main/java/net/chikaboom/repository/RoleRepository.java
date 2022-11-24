package net.chikaboom.repository;

import net.chikaboom.model.database.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы Role
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}
