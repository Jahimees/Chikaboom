package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.WorkingDay;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.WorkingDaysRepository;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static net.chikaboom.util.constant.UtilConstant.DEFAULT_WORKING_DAY_END_TIME;
import static net.chikaboom.util.constant.UtilConstant.DEFAULT_WORKING_DAY_START_TIME;

@Service
@RequiredArgsConstructor
public class WorkingDayDataService implements DataService<WorkingDay> {

    private final WorkingDaysRepository workingDaysRepository;
    private final AccountRepository accountRepository;

    @Override
    public Optional<WorkingDay> findById(int id) {
        return workingDaysRepository.findById(id);
    }

    @Override
    public List<WorkingDay> findAll() {
        return workingDaysRepository.findAll();
    }

    @Override
    public void deleteById(int id) {
        workingDaysRepository.deleteById(id);
    }

    @Override
    public WorkingDay update(WorkingDay workingDay) {
        return workingDaysRepository.save(workingDay);
    }

    @Override
    public WorkingDay create(WorkingDay workingDay) {
        if (workingDay.getAccount() == null || workingDay.getDate() == null) {
            throw new IllegalArgumentException("There is empty account or date");
        }

        if (isWorkingDayExists(workingDay)) {
            throw new AlreadyExistsException("Same working day for this user already exists");
        }

        workingDay.setIdWorkingDay(0);

        if (workingDay.getWorkingDayStart() == null) {
            workingDay.setWorkingDayStart(Timestamp.valueOf(DEFAULT_WORKING_DAY_START_TIME));

        }
        if (workingDay.getWorkingDayEnd() == null) {
            workingDay.setWorkingDayEnd(Timestamp.valueOf(DEFAULT_WORKING_DAY_END_TIME));
        }

        return workingDaysRepository.save(workingDay);
    }

    public boolean isWorkingDayExists(WorkingDay workingDay) {
        return workingDaysRepository.existsByAccountAndDate(workingDay.getAccount(), workingDay.getDate());
    }

    public boolean isWorkingDayExists(int idWorkingDay) {
        return workingDaysRepository.existsById(idWorkingDay);
    }

    /**
     * Возвращает рабочие дни для выбранного мастера. В случае первого захода мастера на страницу графика
     * устанавливает значения по умолчанию
     *
     * @return рабочие дни мастера
     */
    public List<WorkingDay> findWorkingDaysByIdAccount(int idAccount) {
        Account account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccount));

        return workingDaysRepository.findWorkingDaysByAccount(account);
    }
}
