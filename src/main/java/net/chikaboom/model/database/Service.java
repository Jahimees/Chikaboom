package net.chikaboom.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    /**
     * Разделяет строковое представление времени на массив из двух чисел, где в первой позиции находится количество часов
     * а во второй - количество минут
     * @return массив, представляющий собой продолжительность услуги
     */
    @Transient
    @JsonIgnore
    public int[] getServiceTimeNumbers() {
        int[] resultTime = new int[2];
        //2 часа 30 минут; 1 час 30 минут; 1 час; 30 минут; 5 часов
        String[] splittedServiceDurationTime = time.replace(" минут", "").replace("а", "")
                .replace("ов", "").split(" чс");

        if (splittedServiceDurationTime.length == 1) {
//            TODO булшит. Если число меньше 24, то это полюбому часы? Это будет работать пока шаг по времени = 30 минут
            if (Integer.parseInt(splittedServiceDurationTime[0]) <= 24) {
                resultTime[0] = Integer.parseInt(splittedServiceDurationTime[0]);
                resultTime[1] = 0;
            } else {
                resultTime[0] = 0;
                resultTime[1] = Integer.parseInt(splittedServiceDurationTime[0]);
            }
        } else {
            resultTime[0] = Integer.parseInt(splittedServiceDurationTime[0]);
            resultTime[1] = Integer.parseInt(splittedServiceDurationTime[1]);
        }

        return resultTime;
    }

    public void clearPersonalFields() {
        account.clearPersonalFields();
    }
}
