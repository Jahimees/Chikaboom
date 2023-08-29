package net.chikaboom.repository;

import net.chikaboom.model.database.PhoneCode;
import net.chikaboom.model.database.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Интерфейс для CRUD обработки таблицы UserDetails
 */
@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {

    boolean existsUserDetailsByPhoneCodeAndPhone(PhoneCode phoneCode, String phone);
}
