package net.chikaboom.repository;

import net.chikaboom.model.database.PhoneCode;
import net.chikaboom.model.database.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Интерфейс для CRUD обработки таблицы UserDetails
 */
@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer> {

    /**
     * Производит поиск пользовательской информации по номеру телефона
     *
     * @param phone номер телефона
     * @return найденную пользовательскую информацию
     */
    Optional<UserDetails> findUserDetailsByPhone(String phone);

    /**
     * Проверяет существование пользовательской информации по номеру телефона
     *
     * @param phone номер телефона
     * @return true - если пользовательская информация существует, false - в противном случае
     */
    boolean existsUserDetailsByPhone(String phone);

}
