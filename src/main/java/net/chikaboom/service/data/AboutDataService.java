package net.chikaboom.service.data;

import lombok.RequiredArgsConstructor;
import net.chikaboom.model.database.About;
import net.chikaboom.repository.AboutRepository;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Сервис предназначен для обработки информации "О себе".
 */
@Service
@RequiredArgsConstructor
public class AboutDataService implements DataService<About> {

    private final AboutRepository aboutRepository;

    /**
     * Производит поиск объекта "о себе" пользователя
     *
     * @param idAbout идентификатор объекта
     * @return объект "о себе"
     */
    @Override
    public Optional<About> findById(int idAbout) {
        return aboutRepository.findById(idAbout);
    }

    /**
     * Производит поиск всех объектов типа "о себе"
     *
     * @return список объектов
     */
    @Override
    public List<About> findAll() {
        return aboutRepository.findAll();
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
     * @param about новый объект
     * @return сохраненный объект
     */
    @Override
    public About update(About about) {
        if (!aboutRepository.existsById(about.getIdAbout())) {
            throw new NotFoundException("Current About object not found");
        }

        return aboutRepository.saveAndFlush(about);
    }

    /**
     * Производит создание объекта "о себе"
     *
     * @param about создаваемый объект
     * @return созданный объект
     */
    @Override
    public About create(About about) {

        if (aboutRepository.existsById(about.getIdAbout())) {
            throw new AlreadyExistsException("Same About object already exists");
        }

        return aboutRepository.saveAndFlush(about);
    }
}
