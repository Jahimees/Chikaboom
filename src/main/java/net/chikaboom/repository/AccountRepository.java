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
public interface AccountRepository extends JpaRepository<Account, Integer> {

    /**
     * Производит поиск аккаунта по псевдониму пользователя
     *
     * @param username псевдоним пользователя
     * @return найденный аккаунт
     */
    Optional<Account> findAccountByUsername(String username);

    /**
     * Производит поиск аккаунта по его пользовательским данным
     *
     * @param userDetails пользовательские данные
     * @return найденный аккаунт
     */
    Optional<Account> findAccountByUserDetails(UserDetails userDetails);

    /**
     * Проверяет, существует ли аккаунт с указанным email
     *
     * @param email электронная почта, искомого пользователя
     * @return true - если аккаунт найден, false - в противном случае
     */
    boolean existsByEmail(String email);

    /**
     * Проверяет сущестование аккаунта по псевдониму пользователя
     *
     * @param username псевдоним искомого пользователя
     * @return true - если такой пользователь существует, false - в противном случае
     */
    boolean existsAccountByUsername(String username);
}
