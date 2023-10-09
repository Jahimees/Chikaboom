package net.chikaboom.facade.converter;

import net.chikaboom.facade.dto.AboutFacade;
import net.chikaboom.model.database.About;
import org.springframework.stereotype.Component;

@Component
public class AboutFacadeConverter implements FacadeConverter<AboutFacade, About> {

    @Override
    public AboutFacade convertToDto(About model) {
        AboutFacade aboutFacade = new AboutFacade();

        aboutFacade.setIdAbout(model.getIdAbout());
        aboutFacade.setText(model.getText());
        aboutFacade.setTags(model.getTags());
        aboutFacade.setProfession(model.getProfession());

        return aboutFacade;
    }

    @Override
    public About convertToModel(AboutFacade facade) {
        About about = new About();

        about.setIdAbout(facade.getIdAbout());
        about.setText(facade.getText());
        about.setTags(facade.getTags());
        about.setProfession(facade.getProfession());

        return about;
    }
}
