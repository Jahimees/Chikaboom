package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.PhoneCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Интерфейс для CRUD обработки таблицы Account
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    /**
     * Метод для поиска аккаунта по указанному телефону и коду страны
     *
     * @param phone номер телефона
     * @return найденного пользователя
     */
    Account findFirstByPhoneAndPhoneCode(String phone, PhoneCode phoneCode);

    Optional<Account> findAccountByUsername(String username);

    boolean existsAccountByUsername(String username);

    boolean existsAccountByPhoneCodeAndPhone(PhoneCode phoneCode, String phone);

}
