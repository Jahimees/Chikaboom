package net.chikaboom.repository;

import net.chikaboom.model.database.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы Service
 */
@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {
}
