package net.chikaboom.repository;

import net.chikaboom.model.database.About;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы About
 */
@Repository
public interface AboutRepository extends JpaRepository<About, Integer> {
}
