package net.chikaboom.facade.dto;

import lombok.Data;

/**
 * DOCS {@link Facade}
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

    public AboutFacade() {
    }

    public AboutFacade(String text, String tags, String profession) {
        this.text = text;
        this.tags = tags;
        this.profession = profession;
    }
}
