package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Интерфейс для CRUD обработки таблицы Account
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account> {

    Optional<Account> findAccountByUsername(String username);

    Optional<Account> findAccountByUserDetails(UserDetails userDetails);

    boolean existsByEmail(String email);

    boolean existsAccountByUsername(String username);
}
