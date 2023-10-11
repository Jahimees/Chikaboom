package net.chikaboom.facade.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * DOCS {@link Facade}
 */
@Data
public class AppointmentFacade implements Facade {

    /**
     * id записи
     */
    private int idAppointment;

    /**
     * Аккаунт мастера, оказывающего услугу
     */
    private AccountFacade masterAccountFacade;

    /**
     * Пользовательская услуга, на которую записывается клиент
     */
    private ServiceFacade serviceFacade;

    /**
     * Дата и время записи на услугу
     */
    private Timestamp appointmentDateTime;

    /**
     * Пользовательская информация о клиенте
     */
    private UserDetailsFacade userDetailsFacadeClient;
}
