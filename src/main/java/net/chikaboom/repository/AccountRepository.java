package net.chikaboom.repository;

import net.chikaboom.model.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы Account
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

    /**
     * Метод для поиска аккаунта по указанному email
     *
     * @param email электронная почта (логин)
     * @return найденного пользователя
     */
    @Query(value = "select * from Account where email=?1", nativeQuery = true)
    Account findByEmail(String email);

}
