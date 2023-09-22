package net.chikaboom.repository;

import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.WorkingDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Интерфейс для CRUD обработки таблицы workingDays
 */
@Repository
public interface WorkingDaysRepository extends JpaRepository<WorkingDay, Integer> {

    List<WorkingDay> findWorkingDaysByAccount(Account account);

    boolean existsByAccountAndDate(Account account, Timestamp date);
}
