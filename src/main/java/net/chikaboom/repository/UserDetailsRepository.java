package net.chikaboom.repository;

import net.chikaboom.model.database.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
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

    List<UserDetails> findByIdUserDetailsIn(List<Integer> idUserDetailsList);

    interface ExtendedUserDetails {

        int getVisitCount();
        int getIdUserDetails();
        Timestamp getLastVisitDate();
        Timestamp getFirstVisitDate();
    }

    @Query(nativeQuery = true, value = "SELECT count(appointment.idappointment) as visitCount, " +
            "user_details.iduser_details as idUserDetails, " +
            "max(appointment.appointment_date_time) as lastVisitDate, " +
            "min(appointment.appointment_date_time) as firstVisitDate " +
            "from user_details left join appointment " +
            "on user_details.iduser_details = appointment.iduser_details_client " +
            "where appointment.idaccount_master = :idAccount " +
            "or user_details.idaccount_owner = :idAccount group by user_details.iduser_details")
    List<ExtendedUserDetails> getListExtendedUserDetails(int idAccount);

}
