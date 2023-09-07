package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Интерфейс для CRUD обработки таблицы Service
 */
@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer>, JpaSpecificationExecutor<Service> {

    /**
     * Находит все услуги пользователя
     *
     * @param account пользователь, чьи услуги должны быть найдены
     * @return список услуг пользователя
     */
    List<Service> findAllByAccount(Account account);

}
