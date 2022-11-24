package net.chikaboom.repository;

import net.chikaboom.model.database.Subservice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы Subservice
 */
@Repository
public interface SubserviceRepository extends JpaRepository<Subservice, Integer> {
}
