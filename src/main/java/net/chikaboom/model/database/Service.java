package net.chikaboom.model.database;

import jakarta.persistence.*;
import lombok.Data;

import static net.chikaboom.util.constant.DbNamesConstant.*;

/**
 * Определяет модель таблицы Service в базе данных
 */
@Data
@Entity
@Table(name = SERVICE)
public class Service implements BaseEntity {

    /**
     * id сущности в таблице Service
     */
    @Id
    @Column(name = ID_SERVICE)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idService;

    /**
     * Наименование пользовательской услуги
     */
    @Column(name = NAME)
    private String name;

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
    @JoinColumn(name = ID_SERVICE_SUBTYPE)
    private ServiceSubtype serviceSubtype;
}
