package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
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
    Account findOneByEmail(String email);

}
