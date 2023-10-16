package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.WorkingDay;
import net.chikaboom.repository.WorkingDaysRepository;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;
import java.util.Optional;

import static net.chikaboom.util.constant.UtilConstant.DEFAULT_WORKING_DAY_END_TIME;
import static net.chikaboom.util.constant.UtilConstant.DEFAULT_WORKING_DAY_START_TIME;

/**
 * Сервис для обработки информации о рабочих днях пользователя
 */
@Service
@RequiredArgsConstructor
public class WorkingDayDataService implements DataService<WorkingDay> {

    private final WorkingDaysRepository workingDaysRepository;
    private final AccountDataService accountDataService;

    /**
     * Производит поиск конкретного рабочего дня пользователя
     *
     * @param idWorkingDay идентификатор рабочего дня
     * @return рабочий день пользователя
     */
    @Override
    public Optional<WorkingDay> findById(int idWorkingDay) {
        return workingDaysRepository.findById(idWorkingDay);
    }

    /**
     * Производит поиск всех рабочих дней всех пользователей
     *
     * @return список всех объектов рабочих дней
     * @deprecated не существует бизнес-процессов требующих существование данного метода
     */
    @Override
    @Deprecated
    public List<WorkingDay> findAll() {
        return workingDaysRepository.findAll();
    }

    /**
     * Производит удаление конкретного рабочего дня по его идентификатору
     *
     * @param idWorkingDay идентификатор рабочего дня
     */
    @Override
    public void deleteById(int idWorkingDay) {
        workingDaysRepository.deleteById(idWorkingDay);
    }

    /**
     * Производит обновление рабочего дня. Внимание! Полностью перезаписывает объект в базе данных
     *
     * @param workingDay новый объект рабочего дня
     * @return обновленный рабочий день
     * @deprecated в приложении существует лишь одно состояние данного объекта - только существует. Иначе объект не
     * хранится в базе данных. Возможно, в будущем будет возможность изменять рабочее время дня, а не пересоздавать
     * его.
     */
    @Override
    @Deprecated
    public WorkingDay update(WorkingDay workingDay) {
        Optional<WorkingDay> workingDayOptional = workingDaysRepository.findById(workingDay.getIdWorkingDay());

        if (!workingDayOptional.isPresent()) {
            throw new NotFoundException("There is not found working day");
        }

        return workingDaysRepository.save(workingDay);
    }

    /**
     * Создает объект рабочего дня в базе данных
     *
     * @param workingDay сохраняемый в базу данных объект
     * @return созданных объект
     */
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
            workingDay.setWorkingDayStart(Time.valueOf(DEFAULT_WORKING_DAY_START_TIME));
        }
        if (workingDay.getWorkingDayEnd() == null) {
            workingDay.setWorkingDayEnd(Time.valueOf(DEFAULT_WORKING_DAY_END_TIME));
        }

        return workingDaysRepository.save(workingDay);
    }

    /**
     * Проверяет, существует ли уже такой рабочий день. Проверка производится по аккаунту и дате
     *
     * @param workingDay проверяемый рабочий день
     * @return true - если рабочий день существует, false - в противном случае
     */
    public boolean isWorkingDayExists(WorkingDay workingDay) {
        return workingDaysRepository.existsByAccountAndDate(workingDay.getAccount(), workingDay.getDate());
    }

    /**
     * Проверяет существование рабочего дня по идентификатору
     *
     * @param idWorkingDay идентификатор рабочего дня
     * @return true - если существует, false - в противном случае
     */
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
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            throw new NotFoundException("There not found account with id " + idAccount);
        }

        return workingDaysRepository.findWorkingDaysByAccount(accountOptional.get());
    }

    /**
     * Возвращает рабочие дни для выбранного мастера, но не прошедшие.
     *
     * @return рабочие дни мастера
     */
    public List<WorkingDay> findWorkingDaysByIdAccountWithoutPast(int idAccount) {
        Optional<Account> accountOptional = accountDataService.findById(idAccount);

        if (!accountOptional.isPresent()) {
            throw new NotFoundException("There not found account with id " + idAccount);
        }

        return workingDaysRepository.findWorkingDaysByIdAccountAndNotPastDate(idAccount);
    }
}
