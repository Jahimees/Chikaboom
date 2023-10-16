package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.WorkingDayFacade;
import net.chikaboom.model.database.WorkingDay;

import java.sql.Time;
import java.sql.Timestamp;

/**
 * DOCS {@link FacadeConverter}
 */
public final class WorkingDayFacadeConverter implements FacadeConverter {

    private WorkingDayFacadeConverter() {
    }

    /**
     * Конвертирует объект базы данных в объект фасада - DTO
     *
     * @param model объект модели
     * @return объект фасада - DTO
     */
    public static WorkingDayFacade convertToDto(WorkingDay model) {
        WorkingDayFacade workingDayFacade = new WorkingDayFacade();

        workingDayFacade.setIdWorkingDay(model.getIdWorkingDay());
        if (model.getDate() != null) {
            workingDayFacade.setDate((Timestamp) model.getDate().clone());
        }
        if (model.getWorkingDayStart() != null) {
            workingDayFacade.setWorkingDayStart((Time) model.getWorkingDayStart().clone());
        }
        if (model.getWorkingDayEnd() != null) {
            workingDayFacade.setWorkingDayEnd((Time) model.getWorkingDayEnd().clone());
        }
//        TODO warning place 7
        if (model.getAccount() != null) {
            workingDayFacade.setAccountFacade(AccountFacadeConverter.toDtoOnlyId(model.getAccount()));
        }

        return workingDayFacade;
    }

    /**
     * Конвертирует объект фасада в объект модели
     *
     * @param facade объект фасада - DTO
     * @return объект модели
     */
    public static WorkingDay convertToModel(WorkingDayFacade facade) {
        WorkingDay workingDay = new WorkingDay();

        workingDay.setIdWorkingDay(facade.getIdWorkingDay());
        if (facade.getDate() != null) {
            workingDay.setDate((Timestamp) facade.getDate().clone());
        }
        if (facade.getWorkingDayStart() != null) {
            workingDay.setWorkingDayStart((Time) facade.getWorkingDayStart().clone());
        }
        if (facade.getWorkingDayEnd() != null) {
            workingDay.setWorkingDayEnd((Time) facade.getWorkingDayEnd().clone());
        }
        if (facade.getAccountFacade() != null) {
            workingDay.setAccount(AccountFacadeConverter.convertToModel(facade.getAccountFacade()));
        }

        return workingDay;
    }
}
