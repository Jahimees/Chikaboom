package net.chikaboom.facade.dto;

import lombok.Data;

/**
 * Определяет модель таблицы About в базе данных
 */
@Data
public class AboutFacade implements Facade {

    /**
     * id сущности about
     */
    private int idAbout;

    /**
     * Текст, содержащийся в разделе "о себе"
     */
    private String text;

    /**
     * Набор тегов, определенных пользователем. Разделитель - ','
     */
    private String tags;

    /**
     * Краткое отражение вида деятельности мастера
     */
    private String profession;
}
