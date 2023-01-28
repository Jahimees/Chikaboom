package net.chikaboom.model.database;

import lombok.Data;

import javax.persistence.*;

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
     * Аккаунт клиента, который регистрируется на услугу
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ID_ACCOUNT_CLIENT)
    private Account clientAccount;

    /**
     * Пользовательская услуга, на которую записывается клиент
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ID_USER_SERVICE)
    private UserService userService;

    /**
     * Дата записи на услугу
     */
    @Column(name = APPOINTMENT_DATE)
    private String appointmentDate;

    /**
     * Время записи на услугу
     */
    @Column(name = APPOINTMENT_TIME)
    private String appointmentTime;

}
