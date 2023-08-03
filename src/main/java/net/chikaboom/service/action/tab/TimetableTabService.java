package net.chikaboom.service.action.tab;

import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.WorkingDays;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.WorkingDaysRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Сервис для обработки логики на странице графика работы мастера
 */
@Service
public class TimetableTabService {

    private final AccountRepository accountRepository;
    private final WorkingDaysRepository workingDaysRepository;
    private final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    public TimetableTabService(AccountRepository accountRepository,
                               WorkingDaysRepository workingDaysRepository) {
        this.accountRepository = accountRepository;
        this.workingDaysRepository = workingDaysRepository;
    }

    /**
     * Выполняет сохранение объекта WorkingDays в базу данных
     *
     * @return обновленный объект из базы
     */
    public WorkingDays updateWorkingDays(int idAccount, WorkingDays workingDays) {
        logger.info("Saving workingDays info of user with id " + idAccount);

        Account account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccount));

        if (account.getWorkingDays() != null) {
            workingDays.setIdWorkingDays(account.getWorkingDays().getIdWorkingDays());
        } else {
            if (workingDays.getWorkingDayStart() == 0 && workingDays.getWorkingDayEnd() == 0) {
                workingDays.setWorkingDayStart(900);
                workingDays.setWorkingDayStart(1800);
            }
        }

        workingDays = workingDaysRepository.saveAndFlush(workingDays);
        account.setWorkingDays(workingDays);
        accountRepository.save(account);

        logger.info("WorkingDays info saved");

        return workingDays;
    }

    /**
     * Возвращает рабочие дни для выбранного мастера. В случае первого захода мастера на страницу графика
     * устанавливает значения по умолчанию TODO FIXME так не должно быть
     *
     * @return рабочие дни мастера
     */
    public WorkingDays findWorkingDays(int idAccount) {
        logger.info("Searching workingDays info of user with id " + idAccount);

        Account account = accountRepository.findById(idAccount)
                .orElseThrow(() -> new NoSuchDataException("Cannot find account with id " + idAccount));

        if (account.getWorkingDays() == null) {
            WorkingDays workingDays = new WorkingDays(900, 1700);
            workingDays = workingDaysRepository.saveAndFlush(workingDays);

            account.setWorkingDays(workingDays);
            accountRepository.save(account);
        }

        return account.getWorkingDays();
    }
}
