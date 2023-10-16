package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.WorkingDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Интерфейс для CRUD обработки таблицы workingDays
 */
@Repository
public interface WorkingDaysRepository extends JpaRepository<WorkingDay, Integer> {

    List<WorkingDay> findWorkingDaysByAccount(Account account);


    @Query("SELECT wd from WorkingDay wd WHERE wd.account.idAccount=?1 AND wd.date >= CURRENT_DATE")
    List<WorkingDay> findWorkingDaysByIdAccountAndNotPastDate(int idAccount);

    boolean existsByAccountAndDate(Account account, Timestamp date);
}
