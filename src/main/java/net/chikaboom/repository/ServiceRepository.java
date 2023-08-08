package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Service;
import net.chikaboom.model.database.ServiceSubtype;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Интерфейс для CRUD обработки таблицы Service
 */
@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {

    List<Service> findAllByAccount(Account account);

    List<Service> findAllByServiceSubtype(ServiceSubtype serviceSubtype);

}
