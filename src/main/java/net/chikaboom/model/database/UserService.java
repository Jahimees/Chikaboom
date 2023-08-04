package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы UserService в базе данных
 */
@Data
@Entity
@Table(name = USER_SERVICE)
public class UserService implements BaseEntity {

    /**
     * id сущности в таблице userService
     */
    @Id
    @Column(name = ID_USER_SERVICE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUserService;

    /**
     * Наименование пользовательской услуги
     */
    @Column(name = USER_SERVICE_NAME)
    private String userServiceName;

    /**
     * Цена за выполнение услуги
     */
    @Column(name = PRICE)
    private double price;

    /**
     * Приблизительное время на выполнение услуги
     */
    @Column(name = TIME)
    private String time;

    /**
     * Аккаунт, владелец которого оказывает данную услугу
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ID_ACCOUNT)
    private Account account;

    /**
     * Родительский тип подуслуги для данной пользовательской услуги
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = ID_SUBSERVICE)
    private Subservice subservice;
}
