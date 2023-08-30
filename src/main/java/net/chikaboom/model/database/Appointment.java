package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы appointment в базе данных
 */
@Data
@Entity
@Table(name = APPOINTMENT)
public class Appointment implements BaseEntity {

    /**
     * id записи
     */
    @Id
    @Column(name = ID_APPOINTMENT)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idAppointment;

    /**
     * Аккаунт мастера, оказывающего услугу
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ID_ACCOUNT_MASTER)
    private Account masterAccount;

    /**
     * Пользовательская услуга, на которую записывается клиент
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ID_SERVICE)
    private Service service;

    /**
     * Дата и время записи на услугу
     */
    @Column(name = APPOINTMENT_DATE_TIME)
    private Timestamp appointmentDateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ID_USER_DETAILS_CLIENT)
    private UserDetails userDetailsClient;

}
