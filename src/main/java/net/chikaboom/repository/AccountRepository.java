package net.chikaboom.repository;

import net.chikaboom.model.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы Account
 */
@PropertySource("/constants.properties")
@Repository
public interface AccountRepository extends CrudRepository<Account, String> {

    @Value("${tbl.account}")
    String ACCOUNT = null;
    /**
     * Метод для поиска аккаунта по указанному email
     *
     * @param email электронная почта (логин)
     * @return найденного пользователя
     */
    @Query(value = "select * from #{ACCOUNT} where email=?1", nativeQuery = true)
    Account findByEmail(String email);

}
