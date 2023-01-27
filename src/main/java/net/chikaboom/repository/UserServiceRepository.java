package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.Subservice;
import net.chikaboom.model.database.UserService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Интерфейс для CRUD обработки таблицы UserService
 */
@Repository
public interface UserServiceRepository extends JpaRepository<UserService, Integer> {

    List<UserService> findAllByAccount(Account account);

    List<UserService> findAllBySubservice(Subservice subservice);

}
