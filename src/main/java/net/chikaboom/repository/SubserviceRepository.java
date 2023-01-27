package net.chikaboom.repository;

import net.chikaboom.model.database.Service;
import net.chikaboom.model.database.Subservice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Интерфейс для CRUD обработки таблицы Subservice
 */
@Repository
public interface SubserviceRepository extends JpaRepository<Subservice, Integer> {

    List<Subservice> findAllByService(Service service);
}
