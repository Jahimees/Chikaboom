package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы Account
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Integer> {

    /**
     * Метод для поиска аккаунта по указанному телефону
     *
     * @param phone номер телефона
     * @return найденного пользователя
     */
    Account findOneByPhone(String phone);

}
