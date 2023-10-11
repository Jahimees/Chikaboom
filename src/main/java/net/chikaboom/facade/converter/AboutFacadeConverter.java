package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.AboutFacade;
import net.chikaboom.model.database.About;

/**
 * DOCS {@link FacadeConverter}
 */
public final class AboutFacadeConverter implements FacadeConverter {

    private AboutFacadeConverter() {
    }

    /**
     * Конвертирует объект базы данных в объект фасада - DTO
     *
     * @param model объект модели
     * @return объект фасада - DTO
     */
    public static AboutFacade convertToDto(About model) {
        AboutFacade aboutFacade = new AboutFacade();

        aboutFacade.setIdAbout(model.getIdAbout());
        aboutFacade.setText(model.getText());
        aboutFacade.setTags(model.getTags());
        aboutFacade.setProfession(model.getProfession());

        return aboutFacade;
    }

    /**
     * Конвертирует объект фасада в объект модели
     *
     * @param facade объект фасада - DTO
     * @return объект модели
     */
    public static About convertToModel(AboutFacade facade) {
        About about = new About();

        about.setIdAbout(facade.getIdAbout());
        about.setText(facade.getText());
        about.setTags(facade.getTags());
        about.setProfession(facade.getProfession());

        return about;
    }
}
