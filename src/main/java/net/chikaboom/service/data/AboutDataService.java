package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.facade.converter.AboutFacadeConverter;
import net.chikaboom.facade.dto.AboutFacade;
import net.chikaboom.model.database.About;
import net.chikaboom.repository.AboutRepository;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Сервис предназначен для обработки информации "О себе".
 */
@Service
@RequiredArgsConstructor
public class AboutDataService implements DataService<AboutFacade> {

    private final AboutRepository aboutRepository;

    /**
     * Производит поиск объекта "о себе" пользователя
     *
     * @param idAbout идентификатор объекта
     * @return объект "о себе"
     */
    @Override
    public AboutFacade findById(int idAbout) {
        Optional<About> aboutOptional = aboutRepository.findById(idAbout);
        if (!aboutOptional.isPresent()) {
            throw new NotFoundException("about object wasn't found");
        }

        return AboutFacadeConverter.convertToDto(aboutOptional.get());
    }

    /**
     * Производит поиск всех объектов типа "о себе"
     *
     * @return список объектов
     */
    @Override
    public List<AboutFacade> findAll() {
        return aboutRepository.findAll().stream().map(AboutFacadeConverter::convertToDto).collect(Collectors.toList());
    }

    /**
     * Производит удаление объекта "о себе" из базы данных
     *
     * @param idAbout идентификатор объекта
     */
    @Override
    public void deleteById(int idAbout) {
        aboutRepository.deleteById(idAbout);
    }

    /**
     * Производит обновление объекта "о себе". Внимание! Производит полную замену объекта.
     *
     * @param aboutFacade новый объект
     * @return сохраненный объект
     */
    @Override
    public AboutFacade update(AboutFacade aboutFacade) {
        About about = AboutFacadeConverter.convertToModel(aboutFacade);
        if (!aboutRepository.existsById(about.getIdAbout())) {
            throw new NotFoundException("Current About object not found");
        }

        return AboutFacadeConverter.convertToDto(aboutRepository.saveAndFlush(about));
    }

    /**
     * Производит создание объекта "о себе"
     *
     * @param aboutFacade создаваемый объект
     * @return созданный объект
     */
    @Override
    public AboutFacade create(AboutFacade aboutFacade) {
        About about = AboutFacadeConverter.convertToModel(aboutFacade);

        if (aboutRepository.existsById(about.getIdAbout())) {
            throw new AlreadyExistsException("Same About object already exists");
        }
//TODO AOP?
        return AboutFacadeConverter.convertToDto(aboutRepository.saveAndFlush(about));
    }
}
