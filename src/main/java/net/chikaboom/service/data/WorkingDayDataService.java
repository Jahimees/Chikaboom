package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.converter.AccountFacadeConverter;
import net.chikaboom.facade.converter.WorkingDayFacadeConverter;
import net.chikaboom.facade.dto.AccountFacade;
import net.chikaboom.facade.dto.WorkingDayFacade;
import net.chikaboom.model.database.WorkingDay;
import net.chikaboom.repository.WorkingDaysRepository;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static net.chikaboom.util.constant.UtilConstant.DEFAULT_WORKING_DAY_END_TIME;
import static net.chikaboom.util.constant.UtilConstant.DEFAULT_WORKING_DAY_START_TIME;

/**
 * Сервис для обработки информации о рабочих днях пользователя
 */
@Service
@RequiredArgsConstructor
public class WorkingDayDataService implements DataService<WorkingDayFacade> {

    private final WorkingDaysRepository workingDaysRepository;
    private final AccountDataService accountDataService;

    private final WorkingDayFacadeConverter workingDayFacadeConverter;
    private final AccountFacadeConverter accountFacadeConverter;
//    private final AccountRepository accountRepository;

    /**
     * Производит поиск конкретного рабочего дня пользователя
     *
     * @param idWorkingDay идентификатор рабочего дня
     * @return рабочий день пользователя
     */
    @Override
    public WorkingDayFacade findById(int idWorkingDay) {
        Optional<WorkingDay> workingDayOptional = workingDaysRepository.findById(idWorkingDay);

        if (!workingDayOptional.isPresent()) {
            throw new NotFoundException("There is not found working day with id " + idWorkingDay);
        }

        return workingDayFacadeConverter.convertToDto(workingDayOptional.get());
    }

    /**
     * Производит поиск всех рабочих дней всех пользователей
     *
     * @return список всех объектов рабочих дней
     * @deprecated не существует бизнес-процессов требующих существование данного метода
     */
    @Override
    @Deprecated
    public List<WorkingDayFacade> findAll() {
        return workingDaysRepository.findAll().stream().map(
                workingDayFacadeConverter::convertToDto).collect(Collectors.toList());
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
     * @param workingDayFacade новый объект рабочего дня
     * @return обновленный рабочий день
     * @deprecated в приложении существует лишь одно состояние данного объекта - только существует. Инчае объект не
     * хранится в базе данных. Возможно, в будущем будет возможность изменять рабочее время дня, а не пересоздавать
     * его.
     */
    @Override
    @Deprecated
    public WorkingDayFacade update(WorkingDayFacade workingDayFacade) {
        Optional<WorkingDay> workingDayOptional = workingDaysRepository.findById(workingDayFacade.getIdWorkingDay());

        if (!workingDayOptional.isPresent()) {
            throw new NotFoundException("There is not found working day");
        }

        return workingDayFacadeConverter.convertToDto(
                workingDaysRepository.save(
                        workingDayFacadeConverter.convertToModel(workingDayFacade)));
    }

    /**
     * Создает объект рабочего дня в базе данных
     *
     * @param workingDayFacade сохраняемый в базу данных объект
     * @return созданных объект
     */
    @Override
    public WorkingDayFacade create(WorkingDayFacade workingDayFacade) {
        if (workingDayFacade.getAccountFacade() == null || workingDayFacade.getDate() == null) {
            throw new IllegalArgumentException("There is empty account or date");
        }

        if (isWorkingDayExists(workingDayFacade)) {
            throw new AlreadyExistsException("Same working day for this user already exists");
        }

        workingDayFacade.setIdWorkingDay(0);

        if (workingDayFacade.getWorkingDayStart() == null) {
            workingDayFacade.setWorkingDayStart(Time.valueOf(DEFAULT_WORKING_DAY_START_TIME));
        }
        if (workingDayFacade.getWorkingDayEnd() == null) {
            workingDayFacade.setWorkingDayEnd(Time.valueOf(DEFAULT_WORKING_DAY_END_TIME));
        }

        return workingDayFacadeConverter.convertToDto(
                workingDaysRepository.save(
                        workingDayFacadeConverter.convertToModel(workingDayFacade)));
    }

    /**
     * Проверяет существует ли уже такой рабочий день. Проверка производится по аккаунту и дате
     *
     * @param workingDayFacade проверяемый рабочий день
     * @return true - если ррабочий день существует, false - в противном случае
     */
    public boolean isWorkingDayExists(WorkingDayFacade workingDayFacade) {
        return workingDaysRepository.existsByAccountAndDate(
                accountFacadeConverter.convertToModel(
                        workingDayFacade.getAccountFacade()), workingDayFacade.getDate());
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
    public List<WorkingDayFacade> findWorkingDaysByIdAccount(int idAccount) {
        AccountFacade accountFacade = accountDataService.findById(idAccount);

        return workingDaysRepository
                .findWorkingDaysByAccount(accountFacadeConverter.convertToModel(accountFacade))
                .stream().map(workingDayFacadeConverter::convertToDto).collect(Collectors.toList());
    }
}
