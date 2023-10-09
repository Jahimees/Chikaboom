package net.chikaboom.facade.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class ServiceFacade implements Facade {


    /**
     * id сущности в таблице Service
     */
    private int idService;

    /**
     * Наименование пользовательской услуги
     */
    private String name;

    /**
     * Цена за выполнение услуги
     */
    private double price;

    /**
     * Приблизительное время на выполнение услуги
     */
    private String time;

    /**
     * Аккаунт, владелец которого оказывает данную услугу
     */
    private AccountFacade accountFacade;

    /**
     * Родительский тип подуслуги для данной пользовательской услуги
     */
    private ServiceSubtypeFacade serviceSubtypeFacade;

    /**
     * Разделяет строковое представление времени на массив из двух чисел, где в первой позиции находится количество часов
     * а во второй - количество минут
     *
     * @return массив, представляющий собой продолжительность услуги
     */
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
}
