package net.chikaboom.service.action.tab;

import net.chikaboom.exception.NoSuchDataException;
import net.chikaboom.model.database.Account;
import net.chikaboom.model.database.WorkingDays;
import net.chikaboom.repository.AccountRepository;
import net.chikaboom.repository.WorkingDaysRepository;
import net.chikaboom.service.ClientDataStorageService;
import net.chikaboom.service.action.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Сервис для обработки логики на странице графика работы мастера
 */
@Service
public class TimetableTabService implements DataService {

    @Value("${attr.idAccount}")
    private String ID_ACCOUNT;
    @Value("${attr.workingDays}")
    private String WORKING_DAYS;

    private ClientDataStorageService clientDataStorageService;
    private AccountRepository accountRepository;
    private WorkingDaysRepository workingDaysRepository;

    @Autowired
    public TimetableTabService(ClientDataStorageService clientDataStorageService, AccountRepository accountRepository,
                               WorkingDaysRepository workingDaysRepository) {
        this.clientDataStorageService = clientDataStorageService;
        this.accountRepository = accountRepository;
        this.workingDaysRepository = workingDaysRepository;
    }

    /**
     * Выполняет сохранение объекта WorkingDays в базу данных
     *
     * @return обновленный объект из базы
     */
    @Override
    public WorkingDays executeAndGetOne() {
        int idAccount = (int) clientDataStorageService.getData(ID_ACCOUNT);
        WorkingDays workingDays = (WorkingDays) clientDataStorageService.getData(WORKING_DAYS);

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

        return workingDays;
    }

    /**
     * Возвращает рабочие дни для выбранного мастера. В случае первого захода мастера на страницу графика
     * устанавливает значения по умолчанию TODO FIXME так не должно быть
     *
     * @return рабочие дни мастера
     */
    public WorkingDays getWorkingDays() {
        int idAccount = (int) clientDataStorageService.getData(ID_ACCOUNT);

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
